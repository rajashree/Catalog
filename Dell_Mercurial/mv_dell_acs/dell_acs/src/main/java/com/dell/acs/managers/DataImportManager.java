/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.managers.Manager;

import java.util.Collection;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 2886 $, $Date:: 2012-06-04 12:15:28#$
 */
public interface DataImportManager extends Manager {

	/**
	 * fetch the collection of the data files for the retailer which are being
	 * currently downloading
	 * 
	 * @param retailerSite
	 *            for filter the data file specific to retailer
	 * @param maxResultSize
	 *            number of the records to be display
	 * @return Collection of data files which are being currently downloaded
	 */
	Collection<String> getDownLoadedDataFiles(RetailerSite retailerSite,
			int maxResultSize);

	/**
	 * Checks whether the image resizing is in process.
	 * 
	 * @return return true if the images are being processed.
	 */
	Boolean isProcessingImages();

	/**
	 * Gets the data files for the given retailer site.
	 * 
	 * @param retailerSite
	 * @return Collection of data files of given retailer site
	 */
	Collection<DataFile> getDataFiles(RetailerSite retailerSite);

	/**
	 * Gets the latest imported file from the database.
	 * 
	 * @return dataFile - which are in queue to be processed
	 */
	DataFile getLatestImportedFile();

	/**
	 * Get the latest imported image file from the database
	 * 
	 * @return datafile - latest imported image file
	 */
	DataFile getLatestImportedImageFile();

	/**
	 * Processes a csv file and dumps the data into the database.
	 * 
	 * @param dataFile
	 *            to be processed
	 */
	void processImportedFile(DataFile dataFile);

	/**
	 * Resize the images according to the retailer.
	 * 
	 * @param dataFile
	 *            - image file to be processed
	 */
	void processImages(DataFile dataFile);

	/**
	 * Updates the dataFile
	 * 
	 * @param dataFile
	 */
	void update(DataFile dataFile);

	/**
	 * Gets all the data files which are in processing stage.
	 * 
	 * @return Collection of data files
	 */
	Collection<DataFile> getAllDataFilesInProcessingStatus();

	/**
	 * Inner static class which has pre-defined status codes for the FILE
	 * OPERATIONS.
	 */
	public static class FileStatus {

		public static final Integer IN_QUEUE = 0;

		public static final Integer PROCESSING = 1;

		public static final Integer DONE = 2;

		public static final Integer ERROR_READ = 3;

		public static final Integer ERROR_EXTRACTING = 4;

		public static final Integer ERROR_PARSING = 5;

		public static final Integer IMAGES_IMPORTED = 6;

		public static final Integer IMAGES_RESIZING = 7;

		public static final Integer IMAGES_RESIZED = 8;

		public static final Integer ERROR_RESIZING = 9;

		public static final Integer ERROR_WRITE = 10;

		public static final Integer PREPROCESS_QUEUE = 11;

		public static final Integer PREPROCESS_RUNNING = 12;

		public static final Integer PREPROCESS_ERROR = 13;

		public static final Integer PREPROCESS_SPLITTING_UP_DONE = 14;

        public static final Integer PREPROCESS_CONVERT_TO_FICSTAR_DONE = 18;

        public static final Integer ERROR_UNKNOWN = 15;

		public static final Integer READY_TO_VALIDATE = 16;

		public static final Integer TRANSFER_DONE = 17;

		private static final String[] labels = new String[] { "IN_QUEUE",
				"PROCESSING", "DONE", "ERROR_READ", "ERROR_EXTRACTING",
				"ERROR_PARSING", "IMAGES_IMPORTED", "IMAGES_RESIZING",
				"IMAGES_RESIZED", "ERROR_RESIZING", "ERROR_WRITE",
				"PREPROCESS_QUEUE", "PREPROCESS_RUNNING", "PREPROCESS_ERROR",
				"PREPROCESS_SPLITTING_UP_DONE", "ERROR_UNKNOWN", "READY_TO_VALIDATE", "TRANSFER_DONE",
                "PREPROCESS_CONVERT_TO_FICSTAR_DONE"  };

		public static final Integer[] COMPLETE_DONE_STATUS = { FileStatus.DONE,
				FileStatus.ERROR_READ, FileStatus.ERROR_EXTRACTING,
				FileStatus.ERROR_PARSING, FileStatus.ERROR_RESIZING,
				FileStatus.ERROR_WRITE, FileStatus.PREPROCESS_ERROR,
				FileStatus.PREPROCESS_SPLITTING_UP_DONE,
                FileStatus.PREPROCESS_CONVERT_TO_FICSTAR_DONE,
                FileStatus.ERROR_UNKNOWN, FileStatus.TRANSFER_DONE };

		public static String getLabel(final int index) {
			try {
				return labels[index];
			} catch (ArrayIndexOutOfBoundsException e) {
				return "UNKNOWN";
			}
		}
	}

	/**
	 * Enumeration which contains the 4 types of csv import types
	 * Product,Image,Review,Slider.
	 */
	public static enum ImportType {

		UNKNOWN("unknown", "unknown", 0), products("products_",
				"com.dell.acs.persistence.domain.Product", 5), images(
				"images_", "com.dell.acs.persistence.domain.ProductImage", 4), reviews(
				"reviews_", "com.dell.acs.persistence.domain.ProductReview", 3), sliders(
				"sliders_", "com.dell.acs.persistence.domain.ProductSlider", 2), images_zip(
				"images", "images", 1), ALL("all","all", 99);

		private String filePrefix;

		private String tableName;

		private Integer priority;

		private ImportType(final String filePrefix, final String tableName,
				final Integer priority) {
			this.filePrefix = filePrefix;
			this.tableName = tableName;
			this.priority = priority;
		}

		public Integer getPriority() {
			return priority;
		}

		public String getTableName() {
			return this.tableName;
		}

		public String getFilePrefix() {
			return filePrefix;
		}

		public static ImportType getTypeFromFilename(String filename) {
			filename = filename.toLowerCase();
			for (ImportType type : ImportType.values()) {
				if (filename.startsWith(type.getFilePrefix())
						|| filename.contains(type.getFilePrefix() + "-")
						|| filename.contains("-" + type.getFilePrefix())
						|| filename.contains("_" + type.getFilePrefix() + "_")) {
					return type;
				}
			}
			return ImportType.products;
		}

		public static ImportType getTypeFromTableName(String tableName) {
			for (ImportType type : ImportType.values()) {
				if (type.getTableName().equals(tableName)) {
					return type;
				}
			}
			return ImportType.UNKNOWN;
		}
	}
}
