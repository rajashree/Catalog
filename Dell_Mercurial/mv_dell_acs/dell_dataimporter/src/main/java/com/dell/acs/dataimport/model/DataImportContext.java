package com.dell.acs.dataimport.model;

import com.dell.acs.persistence.domain.DataFile;

public class DataImportContext {
	private DataFile _dataFile;
	private Errors<DataImportError> _errors;

	public DataImportContext(DataFile dataFile) {
		this._dataFile = dataFile;
		this._errors = new Errors<DataImportError>();
	}

	public DataFile getDataFile() {
		return this._dataFile;
	}
	
	public boolean hasErrors() {
		return this._errors.getNumErrors() > 0;
	}
	
	public Iterable<DataImportError> getErrors() {
		return this._errors;
	}
	
	public void handleException(Throwable t, int row, int column, String msg, Object... args) {
		DataImportError err = new DataImportError(row, column, Error.Severity.FATAL, String.format(msg, args), t);
		this._errors.add(err);
	}

	public boolean isValid() {
		boolean result = true;
		if (this._errors.getNumErrors() > 0) {
			for(DataImportError error : this._errors) {
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
			for(DataImportError error : this._errors) {
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

    public void error(int row, int column, String msg, Object... args) {
        DataImportError err = new DataImportError(row, column, Error.Severity.ERROR, String.format(msg, args));
        this._errors.add(err);
    }

    public void info(int row, int column, String msg, Object... args) {
        DataImportError err = new DataImportError(row, column, Error.Severity.INFO, String.format(msg, args));
        this._errors.add(err);
    }

    public void warning(int row, int column, String msg, Object... args) {
        DataImportError err = new DataImportError(row, column, Error.Severity.WARNING, String.format(msg, args));
        this._errors.add(err);
    }

    public void def(int row, int column, String msg, Object... args) {
        DataImportError err = new DataImportError(row, column, Error.Severity.DEFAULT, String.format(msg, args));
        this._errors.add(err);
    }
}
