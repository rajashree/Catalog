package com.sourcen.space.service;

import java.io.File;
import java.util.List;

import com.sourcen.space.model.Product;

public interface ProductManager extends ServiceManager {

	public List<Product> getProductList(int pid);

	public void saveProduct(Product product);

	public void saveFeature(Product product);

	public String getProductListAsXML();

	public void saveProductXML(File productXML);

	public void saveFeatureXML(File featureXML);

	public String getFeatureListAsXML();

}
