/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.ACSException;
import com.dell.acs.FeedUtil;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.DataImportDataFileRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.stats.*;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.FileUtils;
import com.sourcen.core.util.ZipUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.hibernate.NonUniqueObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public abstract class DataFilesDownloadManagerBase implements
		DataFilesDownloadManager, ApplicationContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(DataFilesDownloadManagerBase.class);

	public static final String SITE_NAMES_KEY = ".siteNames";
	public static final String SOURCE_KEY = ".source";
	public static final String FILESYSTEM_DIRECTORY_LOCATION_KEY = ".directoryLocation";
	public static final String FILESYSTEM_DATAFILES_DIRECTORY_KEY = "filesystem.dataFiles.directory";
	public static final String FILESYSTEM_DATAFILES_TEMP_KEY = "filesystem.dataFiles.temp";
	public static final String INCLUDE_ONLY_LATEST_FEEDS_KEY = "includeOnlyLatestFeeds";
	/**
	 * {@inheritDoc}
	 */
	protected FileDownLoadStatus FILE_DOWNLOAD_STATUS = DataFilesDownloadManager.FileDownLoadStatus.FILE_DOWNLOAD_STARTED;

	/**
	 * Status to check whether the downloading is in progress from different
	 * providers.
	 */
	private AtomicBoolean downloadingFiles = new AtomicBoolean(false);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isDownloadingFiles() {
		return downloadingFiles.get();
	}

	public abstract String getProviderName();

	private static final class LastModifiedTimeAcscendingOrderComparator
			implements Comparator<FileObject> {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(FileObject a, FileObject b) {
			try {
				return (int) (a.getContent().getLastModifiedTime() - b
						.getContent().getLastModifiedTime());
			} catch (FileSystemException e) {
				throw new RuntimeException(
						"Failed to read time stamp file in the file system", e);
			}
		}
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void downloadDataFiles() {
		String providerName = this.getProviderName();
		CountStatMutator downloadCount = null;
		CountStatMutator fileCount = null;
		TimerStatMutator downloadTiming = null;
		TimerStatMutator downloadFileTiming = null;
		StandardFileSystemManager fileSystemManager = null;
		if (this.setDownloadingTrue()) {
			try {
				downloadCount = (CountStatMutator) StatUtil.getInstance()
						.getStat(CountMinMaxStat.class,
								"DownloadDataFiles.DownloadCount");
				fileCount = (CountStatMutator) StatUtil.getInstance().getStat(
						CountMinMaxStat.class, "DownloadDataFiles.FileCount");
				downloadTiming = (TimerStatMutator) StatUtil.getInstance()
						.getStat(TimerStat.class,
								"DownloadDataFiles.DownloadTiming");
				downloadTiming.start();
				downloadFileTiming = (TimerStatMutator) StatUtil.getInstance()
						.getStat(TimerStat.class,
								"DownloadDataFiles.DownloadFileTiming");

				// Set the downloading as true for current provider
				Set<String> retailerSiteNames = FeedUtil
						.getRetailerSiteRestriction(configurationService);

				logger.debug("Downloading data files job started for "
						+ providerName.toUpperCase() + " provider");

				List<String> filesToProcess = new ArrayList<String>();

				Set<FileObject> feedFiles = new TreeSet<FileObject>(
						new LastModifiedTimeAcscendingOrderComparator());
				Set<FileObject> imageFiles = new TreeSet<FileObject>(
						new LastModifiedTimeAcscendingOrderComparator());

				Source source = Source.valueOf(Source.class,
						configurationService.getProperty(
								DataFilesDownloadManager.class, providerName
										+ SOURCE_KEY, Source.FTP.name()));

				// create filesystem options for accessing feed files & get
				// file
				// name.
				FileSystemOptions fileSystemOptions = new FileSystemOptions();
				String feedLocation = null;
				switch (source) {
				case FTP: {
					String sftpHostname = configurationService.getProperty(
							DataFilesDownloadManager.class, providerName
									+ ".sftpHostname");
					String sftpUsername = configurationService.getProperty(
							DataFilesDownloadManager.class, providerName
									+ ".sftpUsername");
					String sftpPassword = configurationService.getProperty(
							DataFilesDownloadManager.class, providerName
									+ ".sftpPassword");
					String sftpLocation = configurationService.getProperty(
							DataFilesDownloadManager.class, providerName
									+ ".sftpLocation");
					String sftpPortNumber = configurationService.getProperty(
							DataFilesDownloadManager.class, providerName
									+ ".sftpPortNumber");
					if (sftpHostname == null || sftpLocation == null
							|| sftpPassword == null || sftpUsername == null
							|| sftpPortNumber == null) {
						throw new ACSException(
								"Unable to find FTP configurations for the provider/retailerSite:= "
										+ providerName.toUpperCase());
					}
					feedLocation = "sftp://" + sftpUsername + ":"
							+ sftpPassword + "@" + sftpHostname + ":"
							+ sftpPortNumber + "/" + sftpLocation;
					SftpFileSystemConfigBuilder.getInstance()
							.setStrictHostKeyChecking(fileSystemOptions, "no");
					SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(
							fileSystemOptions, true);
					break;
				}
				case FILESYSTEM: {
					String feedDirectoryLocation = configurationService
							.getProperty(DataFilesDownloadManager.class,
									providerName
											+ FILESYSTEM_DIRECTORY_LOCATION_KEY);
					if (feedDirectoryLocation == null) {
						throw new ACSException(
								"Unable to find FILESYSTEM configurations for the provider/retailerSite:= "
										+ providerName.toUpperCase());
					}
					feedLocation = "file://" + feedDirectoryLocation;
					break;
				}
				default: {
					throw new ACSException(
							"Invalid source specified for the provider/retailerSite:= "
									+ providerName.toUpperCase());
				}
				}

				// Append a seperator if it does not exist.
				if (feedLocation.charAt(feedLocation.length() - 1) != File.separatorChar) {
					feedLocation += File.separatorChar;
				}

				// Boolean property to check whether to download only the
				// latest
				// feeds for retailers(Default is true).
				Boolean includeOnlyLatestFeeds = configurationService
						.getBooleanProperty(DataFilesDownloadManager.class,
								INCLUDE_ONLY_LATEST_FEEDS_KEY, true);

				if (shouldIgnoreOnlyLatestFeeds()) {
					includeOnlyLatestFeeds = false;
				}

				try {
					fileSystemManager = new StandardFileSystemManager();
					fileSystemManager.init();
					FileObject serverFileList = null;
					try {
						serverFileList = fileSystemManager.resolveFile(
								feedLocation, fileSystemOptions);
					} catch (FileSystemException fse) {
						logger.error(fse.getMessage());
						return;
					}
					FileObject[] filesInLocation = serverFileList.getChildren();

					for (FileObject child : filesInLocation) {
						if (!includeOnlyLatestFeeds) {
							filesToProcess.add(child.getName().getBaseName());
							continue;
						}
						if (child.getName().getExtension()
								.equalsIgnoreCase("csv")) {
							filesToProcess.add(child.getName().getBaseName());
						} else if (child.getName().getExtension()
								.equalsIgnoreCase("zip")) {
							// if its zip, check that its the latest for the
							// retailer.
							String fileName = child.getName().getBaseName();
							String probableRetailerSiteName = null;
							if (fileName.indexOf("_feed") > 0) {
								probableRetailerSiteName = fileName.substring(
										0, fileName.indexOf("_feed")).toLowerCase();
								if (retailerSiteNames != null) {
									if (!retailerSiteNames
											.contains(probableRetailerSiteName)) {
										continue;
									}
								}

								feedFiles.add(child);
							} else if (fileName.indexOf("_images") > 0) {
								probableRetailerSiteName = fileName.substring(
										0, fileName.indexOf("_images")).toLowerCase();
								if (retailerSiteNames != null) {
									if (!retailerSiteNames
											.contains(probableRetailerSiteName)) {
										continue;
									}
								}

								imageFiles.add(child);
							} else {
                                probableRetailerSiteName = fileName.substring(
                                        0, fileName.indexOf("_")).toLowerCase();

                                if (retailerSiteNames != null) {
                                    if (!retailerSiteNames
                                            .contains(probableRetailerSiteName)) {
                                        continue;
                                    }
                                }

                                feedFiles.add(child);
                            }
						}
					}
					downloadCount.incBy(feedFiles.size());
					downloadCount.incBy(imageFiles.size());
				} catch (FileNotFolderException fnfe) {
					logger.error(fnfe.getMessage(), fnfe);
					return;
				} catch (FileSystemException e) {
					logger.error(e.getMessage(), e);
					return;
				}
				logger.debug("Connection to FTP server was successful for provider."
						+ providerName.toUpperCase());

				for (FileObject imageFile : imageFiles) {
					String srcFile = imageFile.getName().getBaseName();
					if (!dataImportDataFileRepository.hasProcessed(srcFile)) {
						filesToProcess.add(srcFile);
						break;
					}
				}
				for (FileObject feedFile : feedFiles) {
					String srcFile = feedFile.getName().getBaseName();
					if (!dataImportDataFileRepository.hasProcessed(srcFile)) {
						filesToProcess.add(srcFile);
						break;
					}
				}

				logger.debug("Number of files to be downloaded from " + source
						+ " for " + providerName.toUpperCase() + " := "
						+ filesToProcess.size());
				fileCount.incBy(filesToProcess.size());
				String filesystem = configurationService
						.getProperty(FILESYSTEM_DATAFILES_DIRECTORY_KEY);
				String tmpDir = filesystem
						+ configurationService
								.getProperty(FILESYSTEM_DATAFILES_TEMP_KEY);
				RetailerSite retailerSite = null;
				for (String downloadFilename : filesToProcess) {
					downloadFileTiming.start();
					File tmpFile = null;
					FileObject remoteFile = null;
					FileObject localFile = null;
					String probableRetailerSiteName = null;
					try {
						logger.debug("Downloading the file " + downloadFilename
								+ " from server");
						this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWNLOADING;
						remoteFile = fileSystemManager.resolveFile(feedLocation
								+ downloadFilename, fileSystemOptions);
						if (!remoteFile.exists()) {
							logger.warn("File not found := " + downloadFilename);
						} else {
							if (FilenameUtils.isExtension(downloadFilename,
									"gz")) {
								if (!downloadFilename.contains("-")) {
									throw new RuntimeException(
											"Unable to determine the siteName from the file :="
													+ downloadFilename);
								}
								// do not generate the timestamp for the gz
								// files
								tmpFile = generateTempFileUtil
										.generateTempFile(downloadFilename);
								probableRetailerSiteName = downloadFilename
										.substring(0,
												downloadFilename.indexOf("-"));
								retailerSite = this.retailerSiteRepository
										.getByName(probableRetailerSiteName);
							} else {
								if (!downloadFilename.contains("_")) {
									throw new RuntimeException(
											"Unable to determine the siteName from the file :="
													+ downloadFilename);
								}
								tmpFile = generateTempFileUtil
										.generateTempFile(downloadFilename);
								probableRetailerSiteName = downloadFilename
										.substring(0,
												downloadFilename.indexOf("_")).toLowerCase();
								retailerSite = this.retailerSiteRepository
										.getByName(probableRetailerSiteName);
							}

							if (retailerSiteNames != null) {
								if (!retailerSiteNames
										.contains(probableRetailerSiteName)) {
									continue;
								}
							}

							if (!configurationService.getBooleanProperty(
									DataFilesDownloadManager.class,
									retailerSite.getSiteName() + ".enabled",
									true)) {
								throw new RuntimeException("download for "
										+ retailerSite.getSiteName()
										+ " has been disabled");
							}

							if (tmpFile == null) {
								throw new RuntimeException(
										String.format(
												"Protocol Error: The temporay file for %s already exists!",
												downloadFilename));
							}

							localFile = fileSystemManager.resolveFile(tmpFile
									.getAbsolutePath());
							// download the remote file
							localFile.copyFrom(remoteFile,
									Selectors.SELECT_SELF);
							logger.debug("Download Complete - File :="
									+ downloadFilename + " is downloaded at "
									+ tmpDir);
							this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL;
							this.addFileIntoBatch(retailerSite,
									downloadFilename, source, tmpFile);
							DataFilesDownloadCache
									.setDownloadFile(
											retailerSite.getSiteName(),
											downloadFilename,
											FileDownLoadStatus.FILE_DOWN_LOAD_SUCESSFULL);
						}
					} catch (EntityNotFoundException enfe) {
						logger.error("Unable to find retailerSite - "
								+ probableRetailerSiteName);
						this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
					} catch (NonUniqueObjectException nuoe) {
						logger.error("We found more than 1 retailer site with the name :="
								+ probableRetailerSiteName
								+ " please fix this in the admin console.");
						this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
					} catch (Exception e) {
						logger.error("Unable to download - " + downloadFilename
								+ " - " + e.getMessage(), e);
						this.FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR;
					} finally {
						if (retailerSite != null) {
							DataFilesDownloadCache
									.setDownloadFile(
											retailerSite.getSiteName(),
											downloadFilename,
											this.FILE_DOWNLOAD_STATUS);
						}
						FileUtils.deleteQuietly(tmpFile);
						if (localFile != null) {
							localFile.close();
						}
						if (remoteFile != null) {
							remoteFile.close();
						}
						downloadFileTiming.stop();
					}
				}
			} catch (Exception e) {
				logger.error("error while downloading files", e);
				this.FILE_DOWNLOAD_STATUS = FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
			} finally {
				if (fileSystemManager != null) {
					fileSystemManager.close();
				}
				if (FILE_DOWNLOAD_STATUS == DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_ERROR) {
					FILE_DOWNLOAD_STATUS = DataFilesDownloadManagerImpl.FileDownLoadStatus.FILE_DOWN_LOAD_FAIL;
					logger.error("FILE_DOWNLOAD_STATUS :-> FILE_DOWN_LOAD_FAIL");
				}

				if (downloadTiming != null) {
					downloadTiming.stop();
					downloadTiming.apply();
				}
				if (downloadCount != null) {
					downloadCount.apply();
				}
				if (fileCount != null) {
					fileCount.apply();
				}
				if (downloadFileTiming != null) {
					downloadFileTiming.apply();
				}

				setDownloadingFalse();
			}
		}

		logger.debug("FILE_DOWNLOAD_STATUS " + this.FILE_DOWNLOAD_STATUS);
	}

	/**
	 * @return true if data files downloader should ingore only latest feeds
	 *         options.
	 */
	protected abstract boolean shouldIgnoreOnlyLatestFeeds();

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Collection<DataFile> addFileIntoBatch(
			final RetailerSite retailerSite, final String srcFile,
			final Source fileSource, File tempFile) throws IOException {
		Collection<DataFile> resultFiles = new ArrayList<DataFile>();
		String tempFileName = tempFile.getName();

		FileSystem fileSystem = configurationService.getFileSystem();
		String retailerSiteCDNDir = FileSystemUtil.getPath(retailerSite, "cdn");
		String retailerSiteFeedsDir = FileSystemUtil.getPath(retailerSite,
				"feeds");

		File retailerSiteDataDir = fileSystem.getDirectory(
				retailerSiteFeedsDir, true);
		fileSystem.getDirectory(retailerSiteCDNDir, true);

		String srcFileTimestamp = null;
		if (FilenameUtils.isExtension(tempFileName, "gz")) {
			srcFileTimestamp = "gz";
			File newTmpFile = ZipUtils.extractGZipFile(tempFile, tempFile
					.getParentFile().getAbsolutePath());
			tempFile.delete();
			tempFile = newTmpFile;
			tempFileName = tempFile.getName();
		} else {
			srcFileTimestamp = this.generateTempFileUtil.getTimestamp(srcFile);
		}

		if (FilenameUtils.isExtension(tempFileName, new String[] { "csv",
				"txt", "tab" })) {
			DataImportManager.ImportType importType = DataImportManager.ImportType
					.getTypeFromFilename(tempFileName);
			if (!importType.equals(DataImportManager.ImportType.UNKNOWN)) {
				if (!this.dataImportDataFileRepository
						.isRetailerSiteStillProcessing(retailerSite, importType)) {
					String tempFileTimestamp = this.generateTempFileUtil
							.getTimestamp(tempFileName, importType);
					String destFilename = importType + "-" + srcFileTimestamp
							+ "-" + tempFileTimestamp + "."
							+ FilenameUtils.getExtension(tempFileName);
					File finalFile = new File(retailerSiteDataDir, destFilename);
					FileUtils.copyFile(tempFile, finalFile);

					DataFile dataFile = new DataFile(retailerSite, srcFile,
							(retailerSiteFeedsDir + destFilename),
							DataImportManager.FileStatus.PREPROCESS_QUEUE,
							importType.getTableName(), importType.getPriority());
					dataFile.setSplitSrcFile(srcFile);
					dataFile.setStartDate(new Date());
					dataImportDataFileRepository.insert(dataFile);
					resultFiles.add(dataFile);
					FileUtils.deleteQuietly(tempFile);
				}
			} else {
				logger.warn("Uploaded csv was not in standard format");
			}
		}

		if (FilenameUtils.isExtension(tempFileName, "zip")) {
			File extractedLocation = new File(tempFile.getParentFile(),
					FilenameUtils.getBaseName(tempFile.getName())
							+ "_extracted");
			if (tempFileName.contains("images")) {
				DataImportManager.ImportType importType = DataImportManager.ImportType
						.getTypeFromFilename(tempFile.getName());
				if (!this.dataImportDataFileRepository
						.isRetailerSiteStillProcessing(retailerSite, importType)) {
					String tempFileTimestamp = this.generateTempFileUtil
							.getTimestamp(tempFile.getName(), importType);
					String destFilename = retailerSiteFeedsDir + importType
							+ "-" + srcFileTimestamp + "-" + tempFileTimestamp
							+ ".zip";
					File finalFile = new File(retailerSiteDataDir, importType
							+ "-" + srcFileTimestamp + "-" + tempFileTimestamp
							+ ".zip");
					FileUtils.copyFile(tempFile, finalFile);
					DataFile dataFile = new DataFile(retailerSite, srcFile,
							destFilename, FileStatus.PREPROCESS_QUEUE,
							importType.getTableName(), importType.getPriority());
					dataFile.setSplitSrcFile(srcFile);
					dataFile.setStartDate(new Date());
					dataImportDataFileRepository.insert(dataFile);
					FileUtils.deleteQuietly(tempFile);
				}
			} else {
				try {
					if (!this.dataImportDataFileRepository
							.isRetailerSiteStillProcessing(retailerSite,
									DataImportManager.ImportType.ALL)) {
						logger.debug("Found a feeds zip:= " + tempFileName);
						if (!extractedLocation.mkdirs()) {
							logger.error("Unable to create directory:= "
									+ extractedLocation.getAbsolutePath());
						}
						Collection<File> extractedFiles = ZipUtils
								.extractZipFileWithStructure(tempFile,
										extractedLocation.getAbsolutePath(),
										new FileFilter() {
											@Override
											public boolean accept(File pathname) {
                                                if (pathname.getAbsolutePath().contains("__MACOSX")) {
                                                    return false;
                                                } else {
                                                    String ext = FilenameUtils
                                                            .getExtension(
                                                                    pathname.getAbsolutePath());
                                                    return ext.equals("csv") || ext.equals("txt");
                                                }
											}
										});

						for (File file : extractedFiles) {
							if (FilenameUtils
									.isExtension(file.getName(), "csv")
									|| FilenameUtils.isExtension(
											file.getName(), "txt")) {
								DataImportManager.ImportType importType = DataImportManager.ImportType
										.getTypeFromFilename(file.getName());
								String tempFileTimestamp = this.generateTempFileUtil
										.getTimestamp(file.getName(),
												importType);

								if (!importType
										.equals(DataImportManager.ImportType.UNKNOWN)) {

									String destFilename = retailerSiteFeedsDir
											+ importType + "-"
											+ srcFileTimestamp + "-"
											+ tempFileTimestamp + ".csv";
									File finalFile = fileSystem.getFile(
											destFilename, true, true);
									FileUtils.copyFile(file, finalFile);
									logger.debug("Copied feed file - "
											+ FilenameUtils
													.getName(destFilename)
											+ " to " + retailerSiteFeedsDir);

									DataFile dataFile = new DataFile(
											retailerSite,
											srcFile,
											destFilename,
											DataImportManager.FileStatus.PREPROCESS_QUEUE,
											importType.getTableName(),
											importType.getPriority());
									dataFile.setStartDate(new Date());
									dataFile.setSplitSrcFile(srcFile);
									dataImportDataFileRepository
											.insert(dataFile);
									resultFiles.add(dataFile);
								} else {
									logger.debug("Ignoring file as we cannot determine the fileType :="
											+ file.getAbsolutePath());
								}
							} else {
								logger.debug("Ignoring file as its not a csv :="
										+ file.getAbsolutePath());
							}
						}
					}
				} catch (IOException ioe) {
					logger.error("Unable to extract zip file:=" + tempFileName,
							ioe);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			try {
				FileUtils.deleteDirectory(extractedLocation);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			try {
				FileUtils.deleteQuietly(tempFile);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		return resultFiles;
	}

	@Override
	public FileDownLoadStatus getFileDownLoadStatus() {
		return null;
	}

	protected boolean setDownloadingFalse() {
		boolean result = downloadingFiles.compareAndSet(true, false);
//		logger.error("Thread " + Thread.currentThread().getId() + " has "
//				+ (result ? "" : "failed ") + "unlocked the download: "
//				+ this.downloadingFiles.get() + ", provider "
//				+ this.getProviderName());
		return result;
	}

	protected boolean setDownloadingTrue() {
		boolean result = downloadingFiles.compareAndSet(false, true);
//		logger.error("Thread " + Thread.currentThread().getId() + " has "
//				+ (result ? "" : "failed ") + "locked the download: "
//				+ this.downloadingFiles.get() + ", provider "
//				+ this.getProviderName());
		return result;
	}

	//
	// IoC
	//

	/**
	 * ApplicationContext bean injection.
	 */
	@Autowired
	protected ApplicationContext applicationContext;

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	protected DataFileRepository dataFileRepository;

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	protected DataImportDataFileRepository dataImportDataFileRepository;

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	protected RetailerSiteRepository retailerSiteRepository;

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	protected ConfigurationService configurationService;

	@Autowired
	protected ProductRepository productRepository;

	@Autowired
	protected GenerateTempFileUtil generateTempFileUtil;

	/**
	 * Setter for applicationContext
	 */
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Setter for retailerSiteRepository
	 */
	public void setRetailerSiteRepository(
			final RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository pDataFileRepository) {
		this.dataFileRepository = pDataFileRepository;
	}

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataImportDataFileRepository(
			final DataImportDataFileRepository pDataImportDataFileRepository) {
		this.dataImportDataFileRepository = pDataImportDataFileRepository;
	}

	public void setProductRepository(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Setter for generateTempFileUtil
	 */
	public void setGenerateTempFileUtil(
			GenerateTempFileUtil pGenerateTempFileUtil) {
		this.generateTempFileUtil = pGenerateTempFileUtil;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.DataFilesDownloadManager#getCompleteFileList(com
	 * .dell.acs.persistence.domain.RetailerSite)
	 */
	@Override
	public Collection<File> getCompleteFileList(RetailerSite retailerSite) {
		Collection<File> result = new ArrayList<File>();

		try {
			String retailerSiteFeedsDir = FileSystemUtil.getPath(retailerSite,
					"feeds");
			File fileSystemFile = new File(configurationService.getFileSystem()
					.getFileSystemAsString() + "/" + retailerSiteFeedsDir);
			for (String fileName : fileSystemFile.list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return !name.contains("errors");
				}
			})) {
				result.add(new File(fileSystemFile, fileName));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
