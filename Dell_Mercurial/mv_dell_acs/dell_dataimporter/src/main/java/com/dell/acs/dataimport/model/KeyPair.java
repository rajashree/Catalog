/**
 * 
 */
package com.dell.acs.dataimport.model;

/**
 * @author Shawn_Fisk
 * 
 */
public class KeyPair {
	String _key;
	Object _value;

	public KeyPair(String key, Object value) {
		this._key = key;
		this._value = value;
	}

	public String getKey() {
		return this._key;
	}

	public Object getValue() {
		return this._value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this._key.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof KeyPair) {
			return this._key.compareTo(((KeyPair) other)._key) == 0;
		}

		return false;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KeyPair {").append("key:").append(this._key).append(",value:").append(this._value).append("}");
        return sb.toString();
    }
}
