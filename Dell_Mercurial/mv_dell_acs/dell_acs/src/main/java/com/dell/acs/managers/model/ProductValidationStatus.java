package com.dell.acs.managers.model;

/**
 * Inner static class which has pre-defined status codes for the FILE
 * OPERATIONS.
 */
public enum ProductValidationStatus {
	IN_QUEUE(0, "IN_QUEUE"),
	PROCESSING(1, "PROCESSING"),
	WAITING_IMAGES(2, "WAITING_IMAGES"),
	VALIDATING_IMAGES(3, "VALDATING_IMAGES"),
	WAITING_DOWNLOAD_IMAGES(14, "WAITING_DOWNLOAD_IMAGES"),
    PROCESS_DOWNLOAD_IMAGES(15, "PROCESS_DOWNLOAD_IMAGES"),
	DONE(4, "DONE"),
	INVALID(5, "INVALID"),
	ERROR(6, "ERROR"),
	ETL_PROCESSING(7, "ETL PROCESSING"),
	ETL_SLIDER_INQUEUE(8, "ETL SLIDER INQUEUE"),
	ETL_SLIDER_PROCESSING(9, "ETL SLIDER PROCESSING"),
	ETL_DELETION_INQUEUE(10, "ETL DELETION INQUEUE"),
	ETL_DELETING(12, "ETL DELETING"),
	ETL_DELETED(13, "ETL DELETED"),
	UNKNOWN(99, "unknown"),;
	
	private Integer _dbValue;
	private String _label;
	public static final Integer[]  COMPLETE_STATUS_LIST = {
		INVALID.getDbValue(),
		ERROR.getDbValue()
	};
	
	private ProductValidationStatus(Integer dbValue, String label) {
		this._dbValue = dbValue;
		this._label = label;
	}
	
	public Integer getDbValue() {
		return this._dbValue;
	}
	
	public String getLabel() {
		return this._label;
	}

	public static ProductValidationStatus lookup(int value) {
		for(ProductValidationStatus status : ProductValidationStatus.values()) {
			if (status.getDbValue() == value) {
				return status;
			}
		}
		
		return ProductValidationStatus.UNKNOWN;
	}
}
