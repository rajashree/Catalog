/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: vivekk $
 * @version $Revision: 2798 $, $Date:: 2012-06-01 07:10:48#$
 */

public interface DataFileRepository extends
		IdentifiableEntityRepository<Long, DataFile> {

	/**
	 * Gets the data files with given retailerSite.
	 * 
	 * @param retailerSite
	 *            for which the data files to be returned
	 * @return Collection of data files
	 */
	Collection<DataFile> getFiles(RetailerSite retailerSite);

	/**
	 * Gets the data files which are available in database.
	 * 
	 * @param availableFiles
	 *            to be checked in the database
	 * @return List of data files
	 */
	List<DataFile> getDataFilesWithSrcPath(Set<String> availableFiles);

	/**
	 * Gets the latest unprocessed data file from the database checking the
	 * priority.
	 * 
	 * @return dataFile
	 */
	DataFile getLatestImportedFile();

	/**
	 * @param retailerIds
	 *            the set of ids for the retailer sites.
	 * @param currentState
	 *            the current state.
	 * @param nextState
	 *            the next state.
	 * @return the latest data file filter by retailerSiteIds.
	 */
	DataFile getLatestNonImagesDataFile(Collection<Long> retailerSiteIds,
			Integer currentState, Integer nextState);

	/**
	 * @param retailerIds
	 *            the set of ids for the retailer sites.
	 * @return the latest data file filter by retailerSiteIds.
	 */
	DataFile getLatestImagesDataFile(Collection<Long> retailerSiteIds);

	DataFile getPendingFileForExport(RetailerSite retailerSite);

	/**
	 * Get the Image files
	 * 
	 * @return List of Image files
	 */
	DataFile getLatestImageFile();

	/**
	 * Retrieves the data files which are under processing.
	 * 
	 * @return Collection of data files
	 */
	Collection<DataFile> getAllDataFilesInProcessingStatus();

	/**
	 * Return all the data files which are being currently downloaded
	 * 
	 * @return Collection of data files
	 */
	Collection<String> getDownLoadedDataFiles(RetailerSite retailerSite,
			int maxResultSize);

	/**
	 * Gets the latest files that has not been preprocessed from the database
	 * checking the priority.
	 * 
	 * @param retailerSiteIds
	 *            the set of retailer site identifiers to filter by.
	 * 
	 * @return the data file that has not been preprocessed.
	 */
	DataFile getLatestPreprocessFile(Collection<Long> retailerSiteIds);

	/**
	 * @param fileName
	 *            the name of the file to retrieve the data file.
	 * @return the data file for the corresponding file name if it exists else
	 *         null.
	 */
	DataFile getDataFileForFilePath(String fileName);

	/**
	 * @param retailerSite
	 *            the retailer site to filter the data files.
	 * @return the set of data files for the specified retailer site.
	 */
	Collection<DataFile> getDataFilesByRetailerSite(RetailerSite rs);

	/**
	 * @return the list host current processing data import files.
	 */
	List<String> getHosts();

	/**
	 * @param retailerSite
	 *            the retailer site to filter the data files.
	 * @param host
	 *            the host running the process.
	 * @return the set of data files for the specified retailer site and host.
	 */
	Collection<DataFile> getDataFilesByRetailerSiteAndHost(RetailerSite rs,
			String host);

	/**
	 * 
	 * @param host
	 *            the host running the process.
	 * @return the set of data files the were being preprocess by the specified
	 *         host.
	 */
	Collection<DataFile> getRecoverPreprocessFiles(String host);

	/**
	 * @param host
	 *            the host running the process.
	 * @return the set of data files that was being processed for data import
	 *         process that needs to be recovered.
	 */
	Collection<DataFile> getRecoverDataFiles(String host);

	/**
	 * Do an atomic update of the data file with content in the specified data
	 * file.
	 * 
	 * @param dataFile
	 *            the data file containing the new set to write to the database.
	 */
	DataFile atomicUpdate(DataFile dataFile);

	DataFile acquireLock(DataFile dataFile, Integer currentStatus,
			Integer  nextStatus, String host);
}
