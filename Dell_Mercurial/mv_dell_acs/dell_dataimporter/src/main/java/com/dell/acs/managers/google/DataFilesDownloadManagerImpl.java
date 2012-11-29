/*
 * Copyright (c) Marketvine by Dell 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.google;

import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataFilesDownloadManagerBase;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@Service("dataFilesDownloadGoogleManagerImpl")
public class DataFilesDownloadManagerImpl extends DataFilesDownloadManagerBase
implements DataFilesDownloadManager, ApplicationContextAware {
	public static final String PROVIDER_NAME = "google";


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.managers.DataFilesDownloadManagerBase#
	 * shouldIgnoreOnlyLatestFeeds()
	 */
	@Override
	protected boolean shouldIgnoreOnlyLatestFeeds() {
		return true;
	}

	@Override
	public String getProviderName() {
		return PROVIDER_NAME;
	}
}
