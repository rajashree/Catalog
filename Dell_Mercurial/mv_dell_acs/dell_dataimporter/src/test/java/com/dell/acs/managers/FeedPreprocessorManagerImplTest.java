/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.preprocessor.FicstarPreprocessorHandler;
import com.dell.acs.managers.DataFilesDownloadManager.Source;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.cj.DataFilesDownloadManagerImpl;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.testing.DellBaseTest;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.collections.PropertiesProvider;
import com.sourcen.dataimport.definition.DataImportConfig;
import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

/**
 * @author Shawn_Fisk
 * 
 */
public class FeedPreprocessorManagerImplTest {

	public static class TestDataFile {
		private DataFile _mock;
		private int _status = -1;
		private Integer _expectedFileStatus;

		public TestDataFile(DataFile mock, Integer expectedFileStatus) {
			this._mock = mock;
			this._expectedFileStatus = expectedFileStatus;
		}

		public void setStatus(int status) {
			this._status = status;
		}

		public int getStatus() {
			return this._status;
		}

		public DataFile getMock() {
			return this._mock;
		}

		public void validate() {
			assertEquals("DataFile is in invalid final status!",
					this._expectedFileStatus.intValue(), this._status);
		}
	}

	public static class TestDataFileStatusAction implements Answer<Object> {
		private TestDataFile _dataFile;

		public TestDataFileStatusAction(TestDataFile dataFile) {
			this._dataFile = dataFile;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			if (invocation.getMethod().getName().compareTo("getStatus") == 0) {
				return this._dataFile.getStatus();
			} else if (invocation.getMethod().getName().compareTo("setStatus") == 0) {
				this._dataFile
						.setStatus((Integer) invocation.getArguments()[0]);
			}

			return null;
		}
	}

	public static class DataRepositoryAcquireLockAnswer implements
			Answer<DataFile> {
		@Override
		public DataFile answer(InvocationOnMock invocation) throws Throwable {
			DataFile dataFile = (DataFile) invocation.getArguments()[0];
			Integer currentStatus = (Integer) invocation.getArguments()[1];
			Integer nextStatus = (Integer) invocation.getArguments()[2];
			return setStatus(dataFile, currentStatus, nextStatus);
		}
	}

	public static class DataRepositoryInsertAnswer implements Answer<Object> {
		private List<DataFile> _inserts = new ArrayList<DataFile>();
		
		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			this._inserts.add((DataFile)invocation.getArguments()[0]);
			return null;
		}
		
		public List<DataFile> getInserts() {
			return this._inserts;
		}
	}

	private static final String[] newDataFilePaths = {
		"images-2012_07_25_08_09_00_1.csv",
		"images-2012_07_25_08_09_00_2.csv",
		"images-2012_07_25_08_09_00_3.csv",
		"images-2012_07_25_08_09_00_4.csv",
		"images-2012_07_25_08_09_00_5.csv",
		"images-2012_07_25_08_09_00_6.csv",
		"images-2012_07_25_08_09_00_7.csv",
		"images-2012_07_25_08_09_00_8.csv",
		"images-2012_07_25_08_09_00_9.csv",
		"images-2012_07_25_08_09_00_10.csv",
		"images-2012_07_25_08_09_00_11.csv",
		"images-2012_07_25_08_09_00_12.csv",
		"images-2012_07_25_08_09_00_13.csv",
		"images-2012_07_25_08_09_00_14.csv",
		"images-2012_07_25_08_09_00_15.csv",
		"images-2012_07_25_08_09_00_16.csv",
		"images-2012_07_25_08_09_00_17.csv",
		"images-2012_07_25_08_09_00_18.csv",
		"images-2012_07_25_08_09_00_19.csv",
		"images-2012_07_25_08_09_00_20.csv",
		"images-2012_07_25_08_09_00_21.csv",
		"images-2012_07_25_08_09_00_22.csv",
		"images-2012_07_25_08_09_00_23.csv",
		"images-2012_07_25_08_09_00_24.csv",
		"images-2012_07_25_08_09_00_25.csv",
		"images-2012_07_25_08_09_00_26.csv",
		"products-2012_07_25_08_09_00_1.csv",
		"products-2012_07_25_08_09_00_2.csv",
		"products-2012_07_25_08_09_00_3.csv",
		"products-2012_07_25_08_09_00_4.csv",
		"products-2012_07_25_08_09_00_5.csv",
		"products-2012_07_25_08_09_00_6.csv",
		"products-2012_07_25_08_09_00_7.csv",
		"products-2012_07_25_08_09_00_8.csv",
		"products-2012_07_25_08_09_00_9.csv",
		"products-2012_07_25_08_09_00_10.csv",
		"products-2012_07_25_08_09_00_11.csv",
		"products-2012_07_25_08_09_00_12.csv",
		"products-2012_07_25_08_09_00_13.csv",
		"products-2012_07_25_08_09_00_14.csv",
		"products-2012_07_25_08_09_00_15.csv",
		"products-2012_07_25_08_09_00_16.csv",
		"products-2012_07_25_08_09_00_17.csv",
		"products-2012_07_25_08_09_00_18.csv",
		"products-2012_07_25_08_09_00_19.csv",
		"products-2012_07_25_08_09_00_20.csv",
		"products-2012_07_25_08_09_00_21.csv",
		"products-2012_07_25_08_09_00_22.csv",
		"products-2012_07_25_08_09_00_23.csv",
		"products-2012_07_25_08_09_00_24.csv",
		"products-2012_07_25_08_09_00_25.csv"
	};


	private static DataFile setStatus(DataFile dataFile, Integer currentStatus,
			Integer nextStatus) {
		assertEquals("DataFile in invalidate status!", currentStatus,
				dataFile.getStatus());
		dataFile.setStatus(nextStatus);
		return dataFile;
	}

	@Test
	public void test4() {
		String merchantName = "merchant3";
        String userDir = DellBaseTest.getTestingRootDir();
        String volumeActualLocation = String.format(
				"%s/testing/test4/actual/Volume",
                userDir);
		FileUtils.deleteQuietly(new File(volumeActualLocation));
        try {
            FileUtils.copyDirectory(new File(userDir + "/src/main/resources/filesystem"), new File(volumeActualLocation));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize the filesystem for the test", e);
        }
        String volumeExpectedLocation = String.format(
				"%s/testing/test4/expected/Volume",
                userDir);
        String testFileLocation = String.format("%s/testing/fileSystem/test4",
                userDir);
        String testOutputActualLocation = String.format(
				"%s/testing/fileSystem/test4/actual",
                userDir);
        String testOutputTempLocation = String.format(
				"%s/testing/fileSystem/test4/temp",
                userDir);
        String testProvider = "ficstar";
        String retailerSiteName = merchantName;
        Retailer r = mock(Retailer.class);
		when(r.getId()).thenReturn(3L);
		when(r.getName()).thenReturn(merchantName);
		RetailerSite rs = mock(RetailerSite.class);
		when(rs.getRetailer()).thenReturn(r);
		when(rs.getId()).thenReturn(3L);
		when(rs.getSiteName()).thenReturn(merchantName);
        PropertiesProvider rsp = mock(PropertiesProvider.class);
        when(rsp.getProperty("retailerSite.dataImportType.name", "ficstar")).thenReturn("ficstar");
        when(rs.getProperties()).thenReturn(rsp);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        DataImportConfig dic = new DataImportConfig();
        dic.setApplicationContext(applicationContext);
        when(applicationContext.getBean("dataImportConfig", DataImportConfig.class)).thenReturn(dic);
		ConfigurationService cs = mock(ConfigurationService.class);
		when(
				cs.getProperty(DataFilesDownloadManager.class,
						DataFilesDownloadManagerImpl.PROVIDER_NAME
								+ DataFilesDownloadManagerBase.SOURCE_KEY,
						Source.FTP.name()))
				.thenReturn(Source.FILESYSTEM.name());
		when(
				cs.getProperty(
						DataFilesDownloadManager.class,
						DataFilesDownloadManagerImpl.PROVIDER_NAME
								+ DataFilesDownloadManagerBase.FILESYSTEM_DIRECTORY_LOCATION_KEY))
				.thenReturn(testFileLocation);
		when(
				cs.getProperty(DataFilesDownloadManagerBase.FILESYSTEM_DATAFILES_DIRECTORY_KEY))
				.thenReturn(testOutputActualLocation);
		when(
				cs.getProperty(DataFilesDownloadManagerBase.FILESYSTEM_DATAFILES_TEMP_KEY))
				.thenReturn(testOutputTempLocation);
		when(
				cs.getBooleanProperty(DataFilesDownloadManager.class,
						merchantName + ".enabled", true)).thenReturn(true);
		when(
				cs.getBooleanProperty(DataFilesDownloadManager.class,
						rs.getSiteName() + ".blocking", false))
				.thenReturn(true);
		when(
				cs.getIntegerProperty(DataFilesDownloadManager.class,
						rs.getSiteName() + ".blockSize", -1)).thenReturn(10);
		when(cs.getApplicationUrl()).thenReturn("localhost:2701");

		try {
			when(cs.getFileSystem()).thenReturn(
					new FileSystem(volumeActualLocation));
		} catch (IOException e) {
			e.printStackTrace();
			fail("ConfigurationService.getFileSystem() failed!");
		}
		DataFileRepository dfr = mock(DataFileRepository.class);
		List<TestDataFile> testDataFiles = new ArrayList<TestDataFile>();
		File testFile = new File(testFileLocation);
		String retailerSiteFeedsDir = FileSystemUtil.getPath(rs, "feeds");
		DataRepositoryInsertAnswer dataRepositoryInsertAnswer = new DataRepositoryInsertAnswer();
		for (File childFile : testFile.listFiles()) {
			String fileName = childFile.getName();
            String importType = "";
            DataFile dataFile = mock(DataFile.class);
            int expectedFileStatus = FileStatus.IN_QUEUE;
			if (fileName.contains("images")) {
				expectedFileStatus = FileStatus.PREPROCESS_SPLITTING_UP_DONE;
                importType = DataImportManager.ImportType.images.getTableName();
            } else if (fileName.contains("products")) {
				expectedFileStatus = FileStatus.PREPROCESS_SPLITTING_UP_DONE;
                importType = DataImportManager.ImportType.products.getTableName();
            } else if (fileName.contains("reviews")) {
                importType = DataImportManager.ImportType.reviews.getTableName();
            } else if (fileName.contains("sliders")) {
                importType = DataImportManager.ImportType.sliders.getTableName();
            }
			TestDataFile testDataFile = new TestDataFile(dataFile,
					expectedFileStatus);
			doAnswer(new TestDataFileStatusAction(testDataFile)).when(dataFile)
					.setStatus(anyInt());
			doAnswer(new TestDataFileStatusAction(testDataFile)).when(dataFile)
					.getStatus();
			dataFile.setStatus(FileStatus.PREPROCESS_QUEUE);
			String filePath = retailerSiteFeedsDir + File.separatorChar
					+ childFile.getName();
			File retailerSiteDataDir = null;
			try {
				retailerSiteDataDir = cs.getFileSystem().getDirectory(
						retailerSiteFeedsDir, true);
				retailerSiteDataDir.mkdirs();
			} catch (IOException e) {
				fail("ConfigurationService.getFileSystem() failed!");
			}
			try {
				FileUtils.copyFile(childFile, new File(retailerSiteDataDir,
						childFile.getName()));
			} catch (IOException e) {
				fail("ConfigurationService.getFileSystem() failed!");
			}
			when(dataFile.getSrcFile()).thenReturn("stubbed in by a test4");
			when(dataFile.getFilePath()).thenReturn(filePath);
			when(dataFile.getRetailerSite()).thenReturn(rs);
            when(dataFile.getImportType()).thenReturn(importType);
            doAnswer(new DataRepositoryAcquireLockAnswer()).when(dfr)
					.acquireLock(dataFile, FileStatus.PREPROCESS_QUEUE,
							FileStatus.PREPROCESS_RUNNING);
			doAnswer(new DataRepositoryAcquireLockAnswer()).when(dfr)
					.acquireLock(dataFile, FileStatus.PREPROCESS_QUEUE,
							FileStatus.IN_QUEUE);
			doAnswer(new DataRepositoryAcquireLockAnswer()).when(dfr)
					.acquireLock(dataFile, FileStatus.PREPROCESS_QUEUE,
							FileStatus.PREPROCESS_SPLITTING_UP_DONE);
			doAnswer(new DataRepositoryAcquireLockAnswer()).when(dfr)
					.acquireLock(dataFile, FileStatus.PREPROCESS_RUNNING,
							FileStatus.PREPROCESS_ERROR);
			testDataFiles.add(testDataFile);
		}
		doAnswer(dataRepositoryInsertAnswer).when(dfr).insert((DataFile)anyObject());
		RetailerSiteRepository rsr = mock(RetailerSiteRepository.class);
		when(rsr.getByName(rs.getSiteName(), true)).thenReturn(rs);
        FeedPreprocessorManagerImpl fpm = new FeedPreprocessorManagerImpl();
		fpm.setApplicationContext(applicationContext);
		fpm.setConfigurationService(cs);
		fpm.setDataFileRepository(dfr);
		fpm.setRetailerSiteRepository(rsr);
        DataFileStatisticService dfss = mock(DataFileStatisticService.class);
        fpm.setDataFileStatisticService(dfss);
        DataImportService dis = mock(DataImportService.class);
        SessionFactory sessionFactory = mock(SessionFactory.class);
        FicstarPreprocessorHandler fph = new FicstarPreprocessorHandler();
        fph.setApplicationContext(applicationContext);
        fph.setConfigurationService(cs);
        fph.setDataFileRepository(dfr);
        fph.setDataFileStatisticService(dfss);
        fph.setRetailerSiteRepository(rsr);
        fph.setSessionFactory(sessionFactory);
        when(dis.getPreprocessorHandler(testProvider, retailerSiteName)).thenReturn(fph);
        fpm.setDataImportService(dis);

        for (TestDataFile testDataFile : testDataFiles) {
			fpm.preprocessDataFile(testDataFile.getMock());
        }

        List<DataFile> newDataFiles = dataRepositoryInsertAnswer.getInserts();
		assertEquals("Invalid number of data files created!", newDataFilePaths.length, newDataFiles.size());
		for(int i = 0;i < newDataFilePaths.length;i++) {
			String expectedFilePath = retailerSiteFeedsDir + newDataFilePaths[i];
			DataFile newDataFile = newDataFiles.get(i);
			
			assertEquals("Invalid data file path!", expectedFilePath, newDataFile.getFilePath());
		}

        com.dell.acs.testing.DellBaseTest.compareFileSystem(volumeActualLocation,
				volumeExpectedLocation);
	}
}
