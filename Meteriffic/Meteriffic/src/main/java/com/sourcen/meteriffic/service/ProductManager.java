/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.service;

import java.io.File;
import java.util.List;

import com.sourcen.meteriffic.model.Product;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 8:58:15 PM
 */

public interface ProductManager extends ServiceManager{
	
	public List<Product> getProductList(int pid);
	
	public void saveProduct(Product product);
	
	public void saveFeature(Product product);
	
	public void saveProductXML(File productXML);
	
	public void saveFeatureXML(File featureXML);
	
}
