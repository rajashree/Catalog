package com.dell.acs.managers.model;

/**
 * Inner static class which has pre-defined status codes for the FILE
 * OPERATIONS.
 */
public enum ProductImageCache {
	NOT_CACHE(0, "Unkown"),
	DOWNLOADED(1, "Downloaded"),
	ERROR(2, "Error"),
	FEED(3, "Feed"),
	UNKNOWN(99, "Unknown");
	
	private Integer _dbValue;
	private String _label;
	
	private ProductImageCache(Integer dbValue, String label) {
		this._dbValue = dbValue;
		this._label = label;
	}
	
	public Integer getDbValue() {
		return this._dbValue;
	}
	
	public String getLabel() {
		return this._label;
	}

	public static ProductImageCache lookup(int value) {
		for(ProductImageCache status : ProductImageCache.values()) {
			if (status.getDbValue() == value) {
				return status;
			}
		}
		
		return ProductImageCache.UNKNOWN;
	}
}
