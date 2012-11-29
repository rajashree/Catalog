/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.managers.DataFilesDownloadManager.Source;
import com.dell.acs.managers.cj.DataFilesDownloadManagerImpl;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.DataImportDataFileRepository;
import com.dell.acs.persistence.repository.ProductRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.testing.DellBaseTest;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.FileSystem;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Shawn_Fisk
 * 
 */
public class DataFilesDownloadCJManagerImplTest extends DellBaseTest {
	@Test
	public void test1() {
/*		Properties p = System.getProperties();
		for (Map.Entry<Object, Object> entry : p.entrySet()) {
			System.out.printf("%s -> %s\n", entry.getKey(), entry.getValue());
		}*/
        String userDir = DellBaseTest.getTestingRootDir();
		String merchantName = "merchant1";
		String volumeActualLocation = String.format(
				"%s/testing/test1/actual/Volume",
                userDir);
		FileUtils.deleteQuietly(new File(volumeActualLocation));
		String volumeExpectedLocation = String.format(
				"%s/testing/test1/expected/Volume",
                userDir);
		String testFileLocation = String.format("%s/testing/fileSystem/test1",
                userDir);
		String testOutputActualLocation = String.format(
				"%s/testing/fileSystem/test1/actual",
                userDir);
		String testOutputTempLocation = String.format(
				"%s/testing/fileSystem/test1/temp",
                userDir);
		ApplicationContext ac = mock(ApplicationContext.class);
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
		try {
			when(cs.getFileSystem()).thenReturn(
					new FileSystem(volumeActualLocation));
		} catch (IOException e) {
			e.printStackTrace();
			fail("ConfigurationService.getFileSystem() failed!");
		}
		DataImportDataFileRepository didfr = mock(DataImportDataFileRepository.class);
		DataFileRepository dfr = mock(DataFileRepository.class);
		List<DataFile> returnList = new ArrayList<DataFile>();
		File testFile = new File(testFileLocation);
		for (File childFile : testFile.listFiles()) {
			DataFile dataFile1 = mock(DataFile.class);
			String srcFile = childFile.getName();
			when(dataFile1.getSrcFile()).thenReturn(srcFile);
			returnList.add(dataFile1);
		}
		when(dfr.getDataFilesWithSrcPath(new HashSet<String>())).thenReturn(
				returnList);
		ProductRepository pr = mock(ProductRepository.class);
		Retailer r = mock(Retailer.class);
		when(r.getId()).thenReturn(1L);
		when(r.getName()).thenReturn(merchantName);
		RetailerSite rs = mock(RetailerSite.class);
		when(rs.getRetailer()).thenReturn(r);
		when(rs.getId()).thenReturn(1L);
		when(rs.getSiteName()).thenReturn(merchantName);
		RetailerSiteRepository rsr = mock(RetailerSiteRepository.class);
		when(rsr.getByName(merchantName)).thenReturn(rs);
		DataFilesDownloadManagerImpl dfdm = new DataFilesDownloadManagerImpl();
		GenerateTempFileUtilImpl gtfu = new GenerateTempFileUtilImpl();
		gtfu.setConfigurationService(cs);

		dfdm.setApplicationContext(ac);
		dfdm.setConfigurationService(cs);
		dfdm.setDataFileRepository(dfr);
		dfdm.setDataImportDataFileRepository(didfr);
		dfdm.setProductRepository(pr);
		dfdm.setRetailerSiteRepository(rsr);
		dfdm.setGenerateTempFileUtil(gtfu);

		dfdm.downloadDataFiles();

		DellBaseTest.compareFileSystem(volumeActualLocation, volumeExpectedLocation);
	}
	
	@Test
	public void test2() {
		String merchantName = "merchant1";
        String userDir = DellBaseTest.getTestingRootDir();
        String volumeActualLocation = String.format(
				"%s/testing/test2/actual/Volume",
                userDir);
		FileUtils.deleteQuietly(new File(volumeActualLocation));
		String volumeExpectedLocation = String.format(
				"%s/testing/test2/expected/Volume",
                userDir);
        String testFileLocation = String.format("%s/testing/fileSystem/test2",
                userDir);
        String testOutputActualLocation = String.format(
				"%s/testing/fileSystem/test2/actual",
                userDir);
        String testOutputTempLocation = String.format(
				"%s/testing/fileSystem/test2/temp",
                userDir);
        ApplicationContext ac = mock(ApplicationContext.class);
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
		// Only do merchant files.
		when(
				cs.getProperty(
						DataFilesDownloadManager.class,
						DataFilesDownloadManagerImpl.SITE_NAMES_KEY))
				.thenReturn(merchantName);
		try {
			when(cs.getFileSystem()).thenReturn(
					new FileSystem(volumeActualLocation));
		} catch (IOException e) {
			e.printStackTrace();
			fail("ConfigurationService.getFileSystem() failed!");
		}
		DataImportDataFileRepository didfr = mock(DataImportDataFileRepository.class);
		DataFileRepository dfr = mock(DataFileRepository.class);
		List<DataFile> returnList = new ArrayList<DataFile>();
		File testFile = new File(testFileLocation);
		for (File childFile : testFile.listFiles()) {
			DataFile dataFile1 = mock(DataFile.class);
			String srcFile = childFile.getName();
			when(dataFile1.getSrcFile()).thenReturn(srcFile);
			returnList.add(dataFile1);
		}
		when(dfr.getDataFilesWithSrcPath(new HashSet<String>())).thenReturn(
				returnList);
		when(dfr.getDataFilesWithSrcPath(new HashSet<String>())).thenReturn(
				new ArrayList<DataFile>());
		ProductRepository pr = mock(ProductRepository.class);
		Retailer r = mock(Retailer.class);
		when(r.getId()).thenReturn(1L);
		when(r.getName()).thenReturn(merchantName);
		RetailerSite rs = mock(RetailerSite.class);
		when(rs.getRetailer()).thenReturn(r);
		when(rs.getId()).thenReturn(1L);
		when(rs.getSiteName()).thenReturn(merchantName);
		RetailerSiteRepository rsr = mock(RetailerSiteRepository.class);
		when(rsr.getByName(merchantName)).thenReturn(rs);
		DataFilesDownloadManagerImpl dfdm = new DataFilesDownloadManagerImpl();
		GenerateTempFileUtilImpl gtfu = new GenerateTempFileUtilImpl();
		gtfu.setConfigurationService(cs);

		dfdm.setApplicationContext(ac);
		dfdm.setConfigurationService(cs);
		dfdm.setDataFileRepository(dfr);
		dfdm.setDataImportDataFileRepository(didfr);
		dfdm.setProductRepository(pr);
		dfdm.setRetailerSiteRepository(rsr);
		dfdm.setGenerateTempFileUtil(gtfu);

		dfdm.downloadDataFiles();

		DellBaseTest.compareFileSystem(volumeActualLocation, volumeExpectedLocation);
	}
	
	@Test
	public void test3() {
		String merchant1Name = "merchant1";
		String merchant2Name = "merchant2";
        String userDir = DellBaseTest.getTestingRootDir();
        String volumeActualLocation = String.format(
				"%s/testing/test3/actual/Volume",
                userDir);
        FileUtils.deleteQuietly(new File(volumeActualLocation));
		String volumeExpectedLocation = String.format(
				"%s/testing/test3/expected/Volume",
                userDir);
        String testFileLocation = String.format("%s/testing/fileSystem/test3",
                userDir);
        String testOutputActualLocation = String.format(
				"%s/testing/fileSystem/test3/actual",
                userDir);
        String testOutputTempLocation = String.format(
				"%s/testing/fileSystem/test3/temp",
                userDir);
        ApplicationContext ac = mock(ApplicationContext.class);
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
						merchant1Name + ".enabled", true)).thenReturn(true);
		when(
				cs.getBooleanProperty(DataFilesDownloadManager.class,
						merchant2Name + ".enabled", true)).thenReturn(true);
		try {
			when(cs.getFileSystem()).thenReturn(
					new FileSystem(volumeActualLocation));
		} catch (IOException e) {
			e.printStackTrace();
			fail("ConfigurationService.getFileSystem() failed!");
		}
		DataImportDataFileRepository didfr = mock(DataImportDataFileRepository.class);
		DataFileRepository dfr = mock(DataFileRepository.class);
		List<DataFile> returnList = new ArrayList<DataFile>();
		File testFile = new File(testFileLocation);
		for (File childFile : testFile.listFiles()) {
			DataFile dataFile1 = mock(DataFile.class);
			String srcFile = childFile.getName();
			when(dataFile1.getSrcFile()).thenReturn(srcFile);
			returnList.add(dataFile1);
		}
		when(dfr.getDataFilesWithSrcPath(new HashSet<String>())).thenReturn(
				returnList);
		when(dfr.getDataFilesWithSrcPath(new HashSet<String>())).thenReturn(
				new ArrayList<DataFile>());
		ProductRepository pr = mock(ProductRepository.class);
		Retailer r1 = mock(Retailer.class);
		when(r1.getId()).thenReturn(1L);
		when(r1.getName()).thenReturn(merchant1Name);
		RetailerSite rs1 = mock(RetailerSite.class);
		when(rs1.getRetailer()).thenReturn(r1);
		when(rs1.getId()).thenReturn(1L);
		when(rs1.getSiteName()).thenReturn(merchant1Name);
		
		Retailer r2 = mock(Retailer.class);
		when(r2.getId()).thenReturn(2L);
		when(r2.getName()).thenReturn(merchant2Name);
		RetailerSite rs2 = mock(RetailerSite.class);
		when(rs2.getRetailer()).thenReturn(r2);
		when(rs2.getId()).thenReturn(2L);
		when(rs2.getSiteName()).thenReturn(merchant2Name);
		
		RetailerSiteRepository rsr = mock(RetailerSiteRepository.class);
		when(rsr.getByName(merchant1Name)).thenReturn(rs1);
		when(rsr.getByName(merchant2Name)).thenReturn(rs2);
		
		DataFilesDownloadManagerImpl dfdm = new DataFilesDownloadManagerImpl();
		GenerateTempFileUtilImpl gtfu = new GenerateTempFileUtilImpl();
		gtfu.setConfigurationService(cs);

		dfdm.setApplicationContext(ac);
		dfdm.setConfigurationService(cs);
		dfdm.setDataFileRepository(dfr);
		dfdm.setDataImportDataFileRepository(didfr);
		dfdm.setProductRepository(pr);
		dfdm.setRetailerSiteRepository(rsr);
		dfdm.setGenerateTempFileUtil(gtfu);

		dfdm.downloadDataFiles();

		DellBaseTest.compareFileSystem(volumeActualLocation, volumeExpectedLocation);
	}
}
