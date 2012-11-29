package com.dell.acs.managers;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.dell.acs.persistence.domain.DataFile;

public interface FeedPreprocessorManager {
	/**
	 * @param retailerIds
	 *            the set of retailer site identifiers to filter by.
	 * @return the oldest file that has not been processed.
	 */
	@Transactional
	public DataFile getLatestPreprocessFile(Collection<Long> retailerIds);

	/**
	 * Preprocess a csv file and dumps the data into the database.
	 * 
	 * @param dataFile
	 *            to be processed
	 */
	@Transactional
	void preprocessDataFile(DataFile dataFile);
}
