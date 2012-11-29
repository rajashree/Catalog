/**
 * 
 */
package com.dell.acs.dataimport.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shawn_Fisk
 *
 */
public class Errors<T extends Error> implements Iterable<T> {
	private List<T> _errors = new ArrayList<T>();

	/**
	 * 
	 */
	public Errors() {
	}

	public void add(T error) {
		this._errors.add(error);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this._errors.iterator();
	}

	public int getNumErrors() {
		return this._errors.size();
	}
}
