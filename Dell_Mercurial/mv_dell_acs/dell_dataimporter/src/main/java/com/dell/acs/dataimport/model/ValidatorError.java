package com.dell.acs.dataimport.model;

import com.dell.acs.dataimport.DataImportServiceException;

public class ValidatorError extends Error {
	private Long _entityId;

	public ValidatorError(Long id, Severity serverity, String msg) {
		super(serverity, msg, null);
		
		this._entityId = id;
	}

	public ValidatorError(long id, Severity serverity, String msg,
			DataImportServiceException e) {
		super(serverity, msg, e);
		
		this._entityId = id;
	}

	public Long getEntityId() {
		return this._entityId;
	}
}
