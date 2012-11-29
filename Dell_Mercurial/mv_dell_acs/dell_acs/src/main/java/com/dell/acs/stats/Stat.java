/**
 * 
 */
package com.dell.acs.stats;

/**
 * @author srfisk
 *
 */
public abstract class Stat {
	private String _name;
	
	/**
	 * 
	 */
	public Stat(String name) {
		this._name = name;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return _name;
	}
	
	public abstract void dumpHeader(StringBuilder sb);
	public abstract void dumpValues(StringBuilder sb);

}
