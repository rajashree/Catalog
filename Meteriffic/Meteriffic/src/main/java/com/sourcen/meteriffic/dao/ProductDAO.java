/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao;

import java.util.List;

import com.sourcen.meteriffic.model.Product;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:34:49 PM
 */

public interface ProductDAO {
	
	public List<Product> getProductList(int pId);
	
	public List<Product> getFeatureList(int pId);
	
	public void saveProduct(Product product);
	
	public void saveFeature(Product product);
	
	public int removeAllProducts();
	
	public int removeAllFeatures();

	public List<Product> getAllFeatureList();
}
