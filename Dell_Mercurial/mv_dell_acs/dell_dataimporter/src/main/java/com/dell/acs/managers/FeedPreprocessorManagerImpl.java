package com.dell.acs.managers;

import com.dell.acs.dataimport.DataImportService;
import com.dell.acs.dataimport.preprocessor.PreprocessorHandler;
import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.config.ConfigurationService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class FeedPreprocessorManagerImpl extends ManagerBase implements FeedPreprocessorManager,
		ApplicationContextAware {
	private static final Logger logger = LoggerFactory
			.getLogger(FeedPreprocessorManagerImpl.class);

	public FeedPreprocessorManagerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.FeedPreprocessorManager#getLatestPreprocessFile
	 * (java.util.Collection)
	 */
	@Override
	public DataFile getLatestPreprocessFile(Collection<Long> retailerSiteIds) {
		DataFile dataFile = dataFileRepository
				.getLatestPreprocessFile(retailerSiteIds);
		if (dataFile != null) {
			DataFile lockedDataFile = dataFileRepository.acquireLock(dataFile,
					FileStatus.PREPROCESS_QUEUE, FileStatus.PREPROCESS_RUNNING, this.configurationService.getApplicationUrl());
			// acquire lock.
			if (lockedDataFile == null) {
				logger.error("Unable to lock object from PREPROCESS_QUEUE to PREPROCESS_RUNNING objID:="
						+ dataFile
						+ " as it was locked by "
						+ dataFile.getLockedThread());
				return null;
			}
			dataFile.getRetailerSite(); // load the lazy object for future use.
			dataFile.getRetailerSite().getRetailer();
			return lockedDataFile;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.FeedPreprocessorManager#preprocessDataFile(com.
	 * dell.acs.persistence.domain.DataFile)
	 */
	@Override
	@Transactional
	public void preprocessDataFile(DataFile dataFile) {
		RetailerSite retailerSite = retailerSiteRepository.getByName(
				dataFile.getRetailerSite().getSiteName(), true);
		String provider = retailerSite.getProperties()
				.getProperty("retailerSite.dataImportType.name", "ficstar")
				.toLowerCase();
		
		PreprocessorHandler ph = this.dataImportService.getPreprocessorHandler(provider, retailerSite.getSiteName());
		
		ph.preprocess(dataFile);
	}

	/**
	 * ConfigurationService bean injection.
	 */
	@Autowired
	private ConfigurationService configurationService;

	/**
	 * Setter for configurationService
	 */
	public void setConfigurationService(
			final ConfigurationService pConfigurationService) {
		this.configurationService = pConfigurationService;
	}

	/**
	 * DataFileRepository bean injection.
	 */
	@Autowired
	@Qualifier("dataFileRepositoryImpl")
	private DataFileRepository dataFileRepository;

	/**
	 * Setter for dataFileRepository
	 */
	public void setDataFileRepository(
			final DataFileRepository dataFileRepository) {
		this.dataFileRepository = dataFileRepository;
	}

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	private RetailerSiteRepository retailerSiteRepository;

	/**
	 * Setter for retailerSiteRepository
	 */
	public void setRetailerSiteRepository(
			final RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}
	
	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Autowired
	private DataImportService dataImportService;

	public void setDataImportService(final DataImportService pDataImportService) {
		this.dataImportService = pDataImportService;
	}
}
