package com.dell.acs.dataimport.preprocessor;

import com.dell.acs.dataimport.CSVReaderException;
import com.dell.acs.dataimport.CSVWriter;
import com.dell.acs.dataimport.CSVWriterException;
import com.dell.acs.dataimport.model.Row;
import com.dell.acs.managers.DataFileStatisticService;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.managers.FileSystemUtil;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileUtils;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.Schema;
import com.sourcen.dataimport.definition.TableDefinition;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Date;

public abstract class PreprocessorHandlerBase implements PreprocessorHandler {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public PreprocessorHandlerBase() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.dataimport.preprocessor.PreprocessorHandler#preprocess(com
	 * .dell.acs.persistence.domain.DataFile)
	 */
	@Override
	public void preprocess(DataFile dataFile) {
		RetailerSite retailerSite = retailerSiteRepository.getByName(dataFile
				.getRetailerSite().getSiteName(), true);

		if (configurationService.getBooleanProperty(
				DataFilesDownloadManager.class, retailerSite.getSiteName()
						+ ".blocking", false)) {
			int blockSize = configurationService.getIntegerProperty(
					DataFilesDownloadManager.class, retailerSite.getSiteName()
							+ ".blockSize", -1);

			if (blockSize == -1) {
				throw new RuntimeException(
						"Protocol Error, Invalid configuration for block size discovered!");
			}

			this.blockDataFile(dataFile, retailerSite, blockSize);
		} else if (configurationService.getBooleanProperty(
				DataFilesDownloadManager.class, retailerSite.getSiteName()
						+ ".splitting", false)) {
			int splitSize = configurationService.getIntegerProperty(
					DataFilesDownloadManager.class, retailerSite.getSiteName()
							+ ".splitSize", -1);

			if (splitSize == -1) {
				throw new RuntimeException(
						"Protocol Error, Invalid configuration for split size discovered!");
			}

			this.splitDataFile(dataFile, retailerSite, splitSize);
		} else {
			this.noSplit(dataFile, retailerSite);
		}
	}

	private void blockDataFile(DataFile dataFile, RetailerSite retailerSite,
			int blockSize) {
		File filePath = null;
		PreprocessorHandlerContext context = null;
		
		try {
            dataFile.setStartTime(new Date());

            // TODO srfisk 2012/07/31 Only split CSV, might split images zip
			// when they are very large.
			if (dataFile.getImportType().compareTo(
					ImportType.images_zip.getTableName()) != 0) {
                filePath = configurationService.getFileSystem().getDirectory(
                        dataFile.getFilePath(), true);

                TableDefinition tableDefinition = this
						.getTableDefinition(retailerSite, dataFile);

				ImportType importType = ImportType.getTypeFromTableName(dataFile
						.getImportType());

				String encoding = this.getDataImportEncoding(tableDefinition,
						filePath);

                String separator = Character.toString((char) Integer.valueOf(
                        tableDefinition.getProperty("separator", "9"))
                        .intValue());
                String quotechar = this.getQuoteCharacter(tableDefinition);

                int blockCount = 1;
				int totalRowCount = 0;
				boolean noSplit = false;
				boolean done = false;
				context = this.createContext(retailerSite, dataFile, filePath, tableDefinition, encoding, separator,
						quotechar);
				while (!done) {
					context.startWriters(blockCount);
					context.writeHeader();

					Row row = null;

					while ((row = context.reader.readNext()) != null
							&& context.rowCount < blockSize) {
                        context.writeRow(row);
					}
					context.closeWriters();
					blockCount++;

					if (context.isEmptyBlock()) {
						done = true;
						continue;
					}

					context.createDataFiles(retailerSite, importType.getTableName(),
							importType.getPriority());
					
					context.inQueueDataFiles();
				}

				dataFile.setHost(null);
				dataFile.setNumRows(totalRowCount);

				if (!noSplit) {
					dataFile.setEndDate(new Date());
                    dataFile.setEndTime(new Date());
                    dataFile.setStatus(this.getEndStatus());
				} else {
					dataFile.setStatus(FileStatus.IN_QUEUE);
				}
			} else {
				dataFile.setHost(null);
				dataFile.setStatus(FileStatus.IN_QUEUE);
			}
			
			dataFileRepository.update(dataFile);
		} catch (IOException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_READ);
			dataFileRepository.update(dataFile);
		} catch (CSVReaderException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_WRITE);
			dataFileRepository.update(dataFile);
		} catch (CSVWriterException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_READ);
			dataFileRepository.update(dataFile);
        } catch (Throwable t) {
            logger.error(
                    String.format("Failed to unknown %s data file", filePath),
                    t);
            dataFile.setStatus(FileStatus.ERROR_UNKNOWN);
            dataFileRepository.update(dataFile);
        } finally {
			if (context != null) {
				try {
					context.cleanup();
				} catch (IOException e) {
					// Ignore, did everything we could to close it.
				}
			}
		}
	}

    protected String getDefaultQuoteCharVal() {
        return "34";
    }

    protected final String getQuoteCharacter(TableDefinition tableDefinition) {
        String quoteCharVal = tableDefinition.getProperty("quotechar", this.getDefaultQuoteCharVal());

        if (quoteCharVal.equals("")) {
            return "";
        } else {
            return Character.toString((char)Integer.valueOf(quoteCharVal).intValue());
        }
    }
    private void splitDataFile(DataFile dataFile, RetailerSite retailerSite,
			int splitSize) {
		File filePath = null;
		PreprocessorHandlerContext context = null;

		try {
            dataFile.setStartTime(new Date());

            // TODO srfisk 2012/07/31 Only split CSV, might split images zip
			// when they are very large.
			if (dataFile.getImportType().compareTo(
					ImportType.images_zip.getTableName()) != 0) {
				TableDefinition tableDefinition = this
						.getTableDefinition(retailerSite, dataFile);

				ImportType importType = ImportType.getTypeFromTableName(dataFile
						.getImportType());

				filePath = configurationService.getFileSystem().getDirectory(
						dataFile.getFilePath(), true);

				String encoding = this.getDataImportEncoding(tableDefinition,
						filePath);
				
				String separator = Character.toString((char) Integer.valueOf(
                        tableDefinition.getProperty("separator", "9"))
                        .intValue());
                String quotechar = this.getQuoteCharacter(tableDefinition);

                int totalRowCount = this.countRows(retailerSite, dataFile);
				int rowsPerSplit = totalRowCount / splitSize
						+ (totalRowCount % splitSize == 0 ? 0 : 1);
				context = this.createContext(retailerSite, dataFile, filePath, tableDefinition, encoding, separator,
						quotechar);
				if (totalRowCount > 0) {
					for (int splitCount = 0; splitCount < splitSize; splitCount++) {
						context.startWriters(splitCount);
						context.writeHeader();
						
						for (int i = 0; i < rowsPerSplit; i++) {
							Row row = context.reader.readNext();

                            if (row != null) {
								context.writeRow(row);
							}
						}
						
						context.closeWriters();

						context.createDataFiles(retailerSite, importType.getTableName(),
								importType.getPriority());

						context.inQueueDataFiles();
					}

					dataFile.setHost(null);
					dataFile.setNumRows(totalRowCount);
                    dataFile.setEndTime(new Date());
                    dataFile.setEndDate(new Date());
					dataFile.setStatus(this.getEndStatus());
				} else {
					dataFile.setStatus(FileStatus.IN_QUEUE);
				}
			} else {
				dataFile.setHost(null);
				dataFile.setStatus(FileStatus.IN_QUEUE);
			}
			
			dataFileRepository.update(dataFile);
		} catch (IOException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_READ);
			dataFileRepository.update(dataFile);
		} catch (CSVReaderException e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_READ);
			dataFileRepository.update(dataFile);
		} catch (CSVWriterException e) {
			logger.error(
					String.format("Failed to writer %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_WRITE);
			dataFileRepository.update(dataFile);
        } catch (Throwable t) {
            logger.error(
                    String.format("Failed to unknown %s data file", filePath),
                    t);
            dataFile.setStatus(FileStatus.ERROR_UNKNOWN);
            dataFileRepository.update(dataFile);
        } finally {
			if (context != null) {
				try {
					context.cleanup();
				} catch (IOException e) {
					// Ignore, tried everything to close it.
				}
			}
		}
	}

    protected Integer getEndStatus() {
        return FileStatus.PREPROCESS_SPLITTING_UP_DONE;
    }

    protected class PreprocessorHandlerContext {
        public DataFile dataFile;
        public CVSTranslatorReader reader;
		public String srcFile; 
		public String dataFilePath; 
		public File fullDataFilePath;
		public String base;
		public String type;
		public String ext;
		public String path;
		public String encoding;
		public String separator;
		public String quoteChar;
		
		public DataFile blockDataFile;
		public String fileName;
		public File filePath;
		public CSVWriter writer;
		public int rowCount;

		public boolean noSplit;
		public int totalRowCount;

		public PreprocessorHandlerContext(RetailerSite retailerSite, DataFile dataFile, File fullDataFilePath, TableDefinition tableDefinition, String encoding,
				String separator, String quoteChar) throws IOException, CSVReaderException {
            this.dataFile = dataFile;
			this.srcFile = dataFile.getSrcFile();
			this.dataFilePath = dataFile.getFilePath();
			this.fullDataFilePath = fullDataFilePath;
			this.base = FilenameUtils.getBaseName(this.fullDataFilePath.getName());
			int pos = this.base.indexOf('-');
            if (pos == -1) {
                throw new RuntimeException("Protocol error, invalid feed file name: " + this.base);
            }
			this.type = this.base.substring(0, pos);
			this.base = this.base.substring(pos + 1);
			this.ext = FilenameUtils.getExtension(this.fullDataFilePath.getName());
			this.path = this.fullDataFilePath.getParentFile().getPath();
			this.encoding = encoding;
			this.separator = separator;
			this.quoteChar = quoteChar;
			this.reader = new CVSTranslatorReader(this.fullDataFilePath, this.encoding, this.separator,
					quoteChar);
		}
		public void startWriters(int blockCount) throws CSVWriterException {
            if (blockCount != -1) {
                this.fileName = String.format("%s-%s_%d.%s", type, base,
                        blockCount, ext);
            } else {
                this.fileName = String.format("%s-%s.%s", type, base, ext);
            }
			this.filePath = new File(String.format("%s%s%s", path,
					File.separatorChar, this.fileName));
			this.writer = new CSVWriter(this.filePath, this.encoding,
					this.separator, this.quoteChar);
			this.rowCount = 0;
		}
		public void writeHeader() {
			this.writer.writeHeader(this.reader.getHeader()); 
		}
		public void writeRow(Row row) {
			this.writer.writeRow(row);
			this.rowCount++;
		}
		public void closeWriters() {
			this.writer.close();
		}
		public boolean isEmptyBlock() {
			if (this.rowCount == 0) {
				this.filePath.delete();
				this.noSplit = true;
				return true;
			} else {
				return false;
			}
		}
		public void createDataFiles(RetailerSite retailerSite, String tableName, Integer priority) {
			String retailerSiteFeedsDir = FileSystemUtil.getPath(retailerSite,
					"feeds");
			this.blockDataFile = new DataFile(retailerSite,
					this.srcFile,
					(retailerSiteFeedsDir + this.fileName),
					FileStatus.PREPROCESS_RUNNING, tableName, priority);
			this.blockDataFile.setSplitSrcFile(this.dataFilePath);
			this.blockDataFile.setStartDate(new Date());
			this.blockDataFile.setNumRows(this.rowCount);
			this.totalRowCount += this.rowCount;
			dataFileRepository.insert(this.blockDataFile);
			PreprocessorHandlerBase.this.getDataFileStatisticService().initializeStatistics(this.blockDataFile);
			PreprocessorHandlerBase.this.getDataFileStatisticService().startImport(this.blockDataFile);
		}
		public void inQueueDataFiles() {
			this.blockDataFile.setStatus(FileStatus.IN_QUEUE);
			dataFileRepository.update(this.blockDataFile);
		}
		public void cleanup() throws IOException {
			this.reader.close();
			this.writer.close();
		}

        public String stripProperties(String destination) {
            if (destination.startsWith("properties.")) {
                return destination.substring("properties.".length());
            } else {
                return destination;
            }
        }
    };

	protected PreprocessorHandlerContext createContext(RetailerSite retailerSite, DataFile dataFile, File fullDataFilePath, TableDefinition tableDefinition, String encoding,
			String separator, String quotechar) throws IOException, CSVReaderException {
		return new PreprocessorHandlerContext(retailerSite, dataFile, fullDataFilePath, tableDefinition, encoding, separator, quotechar);
	}

	protected void noSplit(DataFile dataFile, RetailerSite retailerSite) {
		if (dataFile.getImportType().compareTo(
				ImportType.images_zip.getTableName()) != 0) {
			int numRows = this.countRows(retailerSite, dataFile);

			dataFile.setHost(null);
			dataFile.setNumRows(numRows);
			this.getDataFileStatisticService().initializeStatistics(dataFile);
			this.getDataFileStatisticService().startImport(dataFile);
		} else {
			dataFile.setNumRows(1);
			dataFile.setHost(null);
		}

		dataFile.setStatus(FileStatus.IN_QUEUE);
		dataFileRepository.update(dataFile);
	}

	protected int countRows(RetailerSite retailerSite, DataFile dataFile) {
		File filePath = null;

		try {
			filePath = configurationService.getFileSystem().getDirectory(
					dataFile.getFilePath(), true);
			TableDefinition tableDefinition = this.getTableDefinition(retailerSite, dataFile);
			String encoding = getDataImportEncoding(tableDefinition, filePath);

			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(
					new FileInputStream(filePath), encoding));
			lnr.readLine(); // Skip header
			int numRows = 0;
			while (lnr.readLine() != null) {
				numRows++;
			}
			lnr.close();

			return numRows;
		} catch (Exception e) {
			logger.error(
					String.format("Failed to reading %s data file", filePath),
					e);
			dataFile.setStatus(FileStatus.ERROR_READ);
			dataFileRepository.update(dataFile);

			return -1;
		}
	}

	protected TableDefinition getTableDefinition(RetailerSite retailerSite, DataFile dataFile)
			throws IOException {
		File schemaFile = this.getSchemaFile(retailerSite);

		DataImportConfig dataImportConfig = applicationContext.getBean(
				"dataImportConfig", DataImportConfig.class);
		dataImportConfig.setConfigFilePath(schemaFile.getAbsolutePath());
		dataImportConfig.afterPropertiesSet();
		// now the config is ready.

		Schema schema = dataImportConfig.getSchema();
		TableDefinition tableDefinition = schema
				.getDefinitionByDestination(dataFile.getImportType());

		Assert.notNull(tableDefinition,
				"cannot find tableDefinition for :=" + dataFile.getImportType()
						+ " in schema :=" + schemaFile.getAbsolutePath());

		String csvFilePath = FilenameUtils.normalize(configurationService
				.getFileSystem().getFileSystemAsString()
				+ "/"
				+ dataFile.getFilePath());
		tableDefinition.setProperty("path", csvFilePath);
		tableDefinition.setProperty("relativePath", dataFile.getFilePath());
		tableDefinition.setProperty("isPathAbsolute", "true");

		// some feeds like google dont have siteName == RetailerSiteName
		tableDefinition.setProperty("columns.siteName.defaultValue",
				retailerSite.getSiteName());

		return tableDefinition;
	}

	File getSchemaFile(RetailerSite retailerSite) throws IOException {
		String dataImportType = retailerSite.getProperties()
				.getProperty("retailerSite.dataImportType.name", "ficstar")
				.toLowerCase();

		String dataImportFileLocation = null;
		if (dataImportType.equalsIgnoreCase("merchant")) {
			// Get the schema file for merchant);
			// should give
			// /config/dataimport/merchant/dellsmb/data_import_config.xml
			dataImportFileLocation = "/config/dataimport/providers/merchant/"
					+ retailerSite.getSiteName() + "/data_import_config.xml";
		} else {
			// Get the schema file for providers (CJ, GOOGLE, FICSTAR)
			// /config/dataimport/ficstar/data_import_config.xml
			dataImportFileLocation = "/config/dataimport/providers/"
					+ dataImportType + "/data_import_config.xml";
		}
		return configurationService.getFileSystem().getFile(
				dataImportFileLocation, false, true);
	}

	protected TableDefinition getFicstarTableDefinition(DataFile dataFile)
			throws IOException {
		RetailerSite retailerSite = dataFile.getRetailerSite();

		File schemaFile = this.getFicstarSchemaFile();

		DataImportConfig dataImportConfig = applicationContext.getBean(
				"dataImportConfig", DataImportConfig.class);
		dataImportConfig.setConfigFilePath(schemaFile.getAbsolutePath());
		dataImportConfig.afterPropertiesSet();
		// now the config is ready.

		Schema schema = dataImportConfig.getSchema();
		TableDefinition tableDefinition = schema
				.getDefinitionByDestination(dataFile.getImportType());

		Assert.notNull(tableDefinition,
				"cannot find tableDefinition for :=" + dataFile.getImportType()
						+ " in schema :=" + schemaFile.getAbsolutePath());

		String csvFilePath = FilenameUtils.normalize(configurationService
				.getFileSystem().getFileSystemAsString()
				+ "/"
				+ dataFile.getFilePath());
		tableDefinition.setProperty("path", csvFilePath);
		tableDefinition.setProperty("relativePath", dataFile.getFilePath());
		tableDefinition.setProperty("isPathAbsolute", "true");

		// some feeds like google dont have siteName == RetailerSiteName
		tableDefinition.setProperty("columns.siteName.defaultValue",
				retailerSite.getSiteName());

		return tableDefinition;
	}

	File getFicstarSchemaFile() throws IOException {
		return configurationService.getFileSystem().getFile(
				"/config/dataimport/providers/ficstar/data_import_config.xml",
				false, true);
	}

	protected String getDataImportEncoding(TableDefinition tableDefinition,
			File filePath) {
		String encoding = null;
		if (tableDefinition.getProperty("encoding") == null) {
			try {
				encoding = FileUtils.getCharacterEncoding(filePath);
			} catch (UnsupportedEncodingException e) {
				logger.error(
						"Unable to read the encoding. using default encoding UTF-8",
						e);
				encoding = "UTF-8";
			} catch (IOException e) {
				logger.error(
						"Unable to read the encoding. using default encoding UTF-8",
						e);
				encoding = "UTF-8";
			}
		} else {
			encoding = tableDefinition.getProperty("encoding");
		}

		return encoding;
	}

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	protected ConfigurationService configurationService;

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService pConfigurationService) {
		this.configurationService = pConfigurationService;
	}

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	protected RetailerSiteRepository retailerSiteRepository;

	/**
	 * Setter for retailerSiteRepository
	 */
	public void setRetailerSiteRepository(
			final RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	protected DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository dataFileRepository) {
		this.dataFileRepository = dataFileRepository;
	}

	protected DataFileStatisticService getDataFileStatisticService() {
		return this.dataFileStatistService;
	}

	/**
	 * ApplicationContext bean injection.
	 */
	@Autowired
	protected ApplicationContext applicationContext;

	/**
	 * Setter for applicationContext
	 */
	public void setApplicationContext(
			final ApplicationContext pApplicationContext) {
		this.applicationContext = pApplicationContext;
	}

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

	@Autowired
	@Qualifier("hibernateSessionFactory")
	protected SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}

	@Autowired
	private DataFileStatisticService dataFileStatistService;

	public void setDataFileStatisticService(final DataFileStatisticService pDataFileStatistService) {
		this.dataFileStatistService = pDataFileStatistService;
	}
}
