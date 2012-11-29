package com.dell.acs.persistence.repository.model;

public class ProductSearchModel {
	private String _siteName;
	private int _totalCount;

	public String getSiteName() {
		return _siteName;
	}

	public void setSiteName(String pSiteName) {
		this._siteName = pSiteName;
	}

	public int getTotalCount() {
		return _totalCount;
	}

	public void setTotalCount(int pTotalCount) {
		this._totalCount = pTotalCount;
	}
}
