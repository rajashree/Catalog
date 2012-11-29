package com.sourcen.space.dao;

import java.util.List;

import com.sourcen.space.model.Product;

public interface ProductDAO {

	public List<Product> getProductList(int pid);

	@SuppressWarnings("unchecked")
	public List<Product> getFeatureList(int pid);

	public void saveProduct(Product product);

	public void saveFeature(Product product);

	public int removeAllProdcuts();

	public int removeAllFeatures();
}
