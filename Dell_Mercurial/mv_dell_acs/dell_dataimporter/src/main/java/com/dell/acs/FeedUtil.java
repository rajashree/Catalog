package com.dell.acs;

import java.util.HashSet;
import java.util.Set;

import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataFilesDownloadManagerBase;
import com.sourcen.core.config.ConfigurationService;

public class FeedUtil {

	public static Set<String> getRetailerSiteRestriction(
			ConfigurationService configurationService) {
		String merchantsStr = configurationService.getProperty(
				DataFilesDownloadManager.class,
				DataFilesDownloadManagerBase.SITE_NAMES_KEY);
		Set<String> merchants = null;
		if (merchantsStr != null) {
			merchants = new HashSet<String>();

			String[] merchantNames = merchantsStr.split(",");

			for (String merchantName : merchantNames) {
				merchants.add(merchantName.trim());
			}
		}
		
		return merchants;
	}

}
