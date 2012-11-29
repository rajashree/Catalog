/**
 * 
 */
package com.dell.acs.web.dataimport.model.admin;


/**
 * @author Shawn_Fisk
 * 
 */
public abstract class DataFileLeaf {

	private Long _id;

	/**
	 * Constructor
	 */
	public DataFileLeaf(Long id) {
		this._id = id;
	}

	public Long getId() {
		return this._id;
	}
	
	public abstract String getName();

}
