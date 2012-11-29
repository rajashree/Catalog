package com.dell.acs.dataimport.model;

public class DataImportError extends Error {
	private int _row;
	private int _column;

	public DataImportError(int row, int column, Severity serverity, String msg) {
		this(row, column, serverity, msg, null);
	}

	public DataImportError(int row, int column, Severity serverity, String msg,
			Throwable t) {
		super(serverity, msg, t);
		
		this._row = row;
		this._column = column;
	}

	public int getRow() {
		return this._row;
	}

	public int getColumn() {
		return this._column;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("column:(");
		sb.append(this._column);
		sb.append("severity:(");
		sb.append(this.getServerity().getText());
		sb.append(") msg:(");
		sb.append(this.getMsg());
		sb.append(")");
		if (this.getThrowable() != null) {
			sb.append(" check log for exception");
		}
		
		return sb.toString();
	}
}
