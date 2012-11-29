/*
 * Copyright (c) Marketvine by Dell 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers.merchant;

import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataFilesDownloadManagerBase;

/**
 * @author Shawn R Fisk
 * @author $LastChangedBy
 * @version $Revision
 */
@Service("dataFilesDownloadMerchantManagerImpl")
public class DataFilesDownloadManagerImpl extends DataFilesDownloadManagerBase
implements DataFilesDownloadManager, ApplicationContextAware {
	public static final String PROVIDER_NAME = "merchant";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.managers.DataFilesDownloadManagerBase#
	 * shouldIgnoreOnlyLatestFeeds()
	 */
	@Override
	protected boolean shouldIgnoreOnlyLatestFeeds() {
		return false;
	}

	@Override
	public String getProviderName() {
		return PROVIDER_NAME;
	}
}
