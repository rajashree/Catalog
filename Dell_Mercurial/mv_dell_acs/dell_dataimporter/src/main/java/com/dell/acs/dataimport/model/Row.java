package com.dell.acs.dataimport.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Row implements Iterable<Map.Entry<String, Object>> {
    private int _rowNum;
	private Map<String, Object> _row;

	public Row() {
		this._row = new HashMap<String, Object>();
	}

	public Object get(String column) {
		return this._row.get(column);
	}

	public <T> T get(Class<T> type, String column) {
		return type.cast(this._row.get(column));
	}

	public void set(String column, Object value) {
		this._row.put(column, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return this._row.entrySet().iterator();
	}

	public Collection<Object> values() {
		return this._row.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("row: {");
		boolean firstTime = true;
		for(Map.Entry<String, Object> entry : this._row.entrySet()) {
			if (!firstTime) {
				sb.append(",");
			}
			firstTime = false;
			sb.append(entry.getKey() + ":" + entry.getValue());
		}
		sb.append("}");

		return sb.toString();
	}

    public void setRowNum(int rowNum) {
        this._rowNum = rowNum;
    }

    public int getRowNum() {
        return this._rowNum;
    }
}
