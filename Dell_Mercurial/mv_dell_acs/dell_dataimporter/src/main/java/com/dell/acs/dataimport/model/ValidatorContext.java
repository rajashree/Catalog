package com.dell.acs.dataimport.model;

import com.dell.acs.dataimport.DataImportServiceException;
import com.dell.acs.persistence.domain.DataFile;
import org.codehaus.plexus.util.StringUtils;

public class ValidatorContext {
	private DataFile _dataFile;
	private int _row;
	private Errors<ValidatorError> _errors;

	public ValidatorContext(DataFile dataFile, int row) {
		this._dataFile = dataFile;
		this._row = row;
		this._errors = new Errors<ValidatorError>();
	}

	public DataFile getDataFile() {
		return this._dataFile;
	}
	
	public int getRow() {
		return this._row;
	}
	
	public Iterable<ValidatorError> getErrors() {
		return this._errors;
	}

	public void notNull(Long id, Object value, Error.Severity serverity, String msg) {
		if (value == null) {
			this.add(id, serverity, msg);
		}
	}

	public void notEmptyString(Long id, String value, Error.Severity serverity,
			String msg) {
        if (StringUtils.isEmpty(value)) {
			this.add(id, serverity, msg);
		}
	}

	public void lessThanOrEqual(Long id, Float a, Float b, Error.Severity serverity,
			String msg) {
		if (a - b > 0.0f) {
			this.add(id, serverity, msg);
		}
	}

	private void add(Long id, Error.Severity serverity, String msg) {
		ValidatorError err = new ValidatorError(id, serverity, msg);
		this._errors.add(err);
	}

	public void handleException(long id, String msg, DataImportServiceException e) {
		ValidatorError err = new ValidatorError(id, Error.Severity.FATAL, msg, e);
		this._errors.add(err);
	}

	public boolean isValid() {
		boolean result = true;
		if (this._errors.getNumErrors() > 0) {
			for(ValidatorError error : this._errors) {
				switch (error.getServerity()) {
				case INFO: {
					break;
				}
				case WARNING: {
					break;
				}
				case ERROR: {
					result = false;
					break;
				}
				case FATAL: {
					result = false;
					break;
				}
				case DEFAULT:
					break;
				}
			}
		}
		
		return result;
	}

	public boolean isFatal() {
		boolean result = false;
		if (this._errors.getNumErrors() > 0) {
			for(ValidatorError error : this._errors) {
				switch (error.getServerity()) {
				case INFO: {
					break;
				}
				case WARNING: {
					break;
				}
				case ERROR: {
					break;
				}
				case FATAL: {
					result = true;
					break;
				}
				case DEFAULT:
					break;
				}
			}
		}
		
		return result;
	}

    public void invalid(Long id, Error.Severity serverity, String msg, Object... args) {
        ValidatorError err = new ValidatorError(id, serverity, String.format(msg, args));
        this._errors.add(err);
    }
}
