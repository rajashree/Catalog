package com.sourcen.microsite.model;

import java.io.Serializable;

public class Property implements Serializable {

	private static final long serialVersionUID = 3434972458764657217L;

	private String key;

	private String value;

	public Property(){
		
	}
	public Property(String key, String value) {

		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toXML() {

		return "<property><key>" + this.key + "</key><value>" + this.value
				+ "</value></property>";
	}
}
