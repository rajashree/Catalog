/**
 * 
 */
package com.dell.acs.managers;

import com.dell.acs.managers.DataImportManager.ImportType;
import com.sourcen.core.config.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author Shawn_Fisk
 * 
 */
@Service
public class GenerateTempFileUtilImpl implements GenerateTempFileUtil {

	/**
	 * Constructor
	 */
	public GenerateTempFileUtilImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.managers.GenerateTempFileUtil#generateTempFile(java.lang
	 * .String)
	 */
	@Override
	public File generateTempFile(final String originalFilename)
			throws IOException {
		File tempDir = configurationService.getFileSystem().getTempDirectory();
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		File tempFile = new File(tempDir, originalFilename);
		if (tempFile.createNewFile() && tempFile.canRead()) {
			return tempFile;
		}
		return null;
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
			final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public String getTimestamp(String originalFilename) {
		int startPos = -1;
		if (originalFilename.contains("_feed_") || originalFilename.contains("_feeds-")) {
			startPos = originalFilename.indexOf("feed")+5;
		} else if (originalFilename.contains("_images_") || originalFilename.contains("_images-")) {
			startPos = originalFilename.indexOf("images")+7;
		} else {
            // Assume to be a product feed
            startPos = originalFilename.indexOf("_")+1;
        }
		int endPos = originalFilename.lastIndexOf('.');

		return originalFilename.substring(startPos, endPos);
	}

	@Override
	public String getTimestamp(String originalFilename, ImportType importType) {
		int startPos = -1;
		switch(importType) {
		case products: {
            if (originalFilename.contains("products")) {
			    startPos = originalFilename.indexOf("products")+9;
            } else {
                // Case without type, assume to be product.
                startPos = originalFilename.indexOf("_")+1;
            }
			break;
		}
		case reviews: {
			startPos = originalFilename.indexOf("reviews")+8;
			break;
		}
		case sliders: {
			startPos = originalFilename.indexOf("sliders")+8;
			break;
		}
		case images: {
			startPos = originalFilename.indexOf("images")+7;
			break;
		}
		case images_zip: {
			startPos = originalFilename.indexOf("images")+7;
			break;
		}
		default:
			throw new RuntimeException("Protocol error, unknown import type: " + importType.toString());
		}
		int endPos = originalFilename.lastIndexOf('.');
		
		return originalFilename.substring(startPos, endPos);
	}
}
