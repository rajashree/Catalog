/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import java.util.Collection;

import com.dell.acs.persistence.domain.DataFile;
import com.sourcen.core.managers.Manager;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
public interface PreValidatedDataImportManager extends Manager {
	String ADDITIONAL_KEYS_COLUMN = "_additionalKeys";

	/**
	 * Gets the latest imported file from the database.
	 * 
	 * @param retailerSiteIds the set of ids for the retailer site to filter.
	 * @return dataFile - which are in queue to be processed
	 */
	DataFile getLatestProductDataFile(Collection<Long> retailerSiteIds);

	/**
	 * Processes a csv file and dumps the data into the database.
	 * 
	 * @param dataFile
	 *            to be processed
	 */
	void processDataFile(DataFile dataFile);
}
