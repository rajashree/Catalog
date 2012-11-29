/**
 * 
 */
package com.dell.acs.dataimport.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Shawn_Fisk
 * 
 */
public class KeyPairs implements Iterable<KeyPair> {
	private Map<String, KeyPair> _keyPairs = new HashMap<String, KeyPair>();

	/**
	 * 
	 */
	public KeyPairs() {
	}

	public void add(KeyPair keyPair) {
		this._keyPairs.put(keyPair.getKey(), keyPair);
	}

	public Object get(String key) {
		KeyPair kp = this._keyPairs.get(key);
		if (kp != null) {
			return kp.getValue();
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<KeyPair> iterator() {
		return this._keyPairs.values().iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("keypairs [");
		sb.append("keypairs ]");
		for (KeyPair keyPair : this._keyPairs.values()) {
			sb.append("{ key:");
			sb.append(keyPair.getKey());
			sb.append(", value:");
			sb.append(keyPair.getValue());
			sb.append("}");
		}

		return sb.toString();
	}

	public <T> T get(Class<T> type, String key) {
		return type.cast(this.get(key));
	}
}
