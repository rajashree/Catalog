package com.dell.acs.dataimport.model;

public class Error {
	public enum Severity {
		INFO("inf"), WARNING("wrn"), ERROR("err"), FATAL("fat"), DEFAULT(
				"def");

		private String _text;

		private Severity(String text) {
			this._text = text;
		}

		public String getText() {
			return this._text;
		}
	};

	private Severity _serverity;
	private String _msg;
	private Throwable _throwable;

	public Error(Severity serverity, String msg, Throwable throwable) {
		this._serverity = serverity;
		this._msg = msg;
		this._throwable = throwable;
	}

	public Severity getServerity() {
		return _serverity;
	}

	public String getMsg() {
		return _msg;
	}

	public Throwable getThrowable() {
		return this._throwable;
	}
}
