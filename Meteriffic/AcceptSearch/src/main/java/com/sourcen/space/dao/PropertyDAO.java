package com.sourcen.space.dao;

import java.util.List;

import com.sourcen.space.model.Property;

public interface PropertyDAO {

	/**
	 * Find a property by key.
	 * 
	 * @param key
	 *            The property key
	 * @return The property
	 */
	Property getProperty(String key);

	/**
	 * Update a property.
	 * 
	 * @param property
	 *            The property to update
	 */
	public void updateProperty(Property property);

	/**
	 * Save a property.
	 * 
	 * @param property
	 *            The property to save
	 */
	void saveProperty(Property property);

	public List<Property> getAllProperty();

	public void deleteProperty(String key);

	

}
