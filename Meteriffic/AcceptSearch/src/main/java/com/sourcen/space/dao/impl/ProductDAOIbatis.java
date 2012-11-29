package com.sourcen.space.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.space.dao.ProductDAO;
import com.sourcen.space.model.Product;


public class ProductDAOIbatis extends SqlMapClientDaoSupport implements ProductDAO{

	@SuppressWarnings("unchecked")
	public List<Product> getProductList(int pid) {
		
		return getSqlMapClientTemplate().queryForList("getProducts",pid);
	}


	public void saveProduct(Product product) {
		 getSqlMapClientTemplate().insert("saveProduct",product);
		
	}

	public void saveFeature(Product product) {
		 getSqlMapClientTemplate().insert("saveFeature",product);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getFeatureList(int pid) {
		return getSqlMapClientTemplate().queryForList("getFeatures",pid);
	}
	public int removeAllProdcuts(){
		return getSqlMapClientTemplate().delete("removeAllProdcuts");
		
	}
	public int removeAllFeatures(){
		
		return getSqlMapClientTemplate().delete("removeAllFeatures");
	}
	

}
