/*
 * Copyright (C) 2007-2009 SourceN Inc. All rights reserved.
 *
 * This software is the proprietary information of SourceN Inc. Use is subject to license terms.
 * 
 */
package com.sourcen.meteriffic.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.meteriffic.dao.ProductDAO;
import com.sourcen.meteriffic.model.Product;

/**
 * Created by Eclipse.
 * User : rajashreem
 * Date : Jun 9, 2009
 * Time : 4:40:07 PM
 */

public class ProductDAOIbatis extends SqlMapClientDaoSupport implements ProductDAO{

	@SuppressWarnings("unchecked")
	public List<Product> getFeatureList(int pid) {
		return getSqlMapClientTemplate().queryForList("getFeatures",pid);
	}

	@SuppressWarnings("unchecked")
	public List<Product> getAllFeatureList() {
		return getSqlMapClientTemplate().queryForList("getAllFeatures");
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductList(int pid) {
		return getSqlMapClientTemplate().queryForList("getProducts",pid);
	}

	public int removeAllFeatures() {
		return getSqlMapClientTemplate().delete("removeAllFeatures");
	}

	public int removeAllProducts() {
		return  getSqlMapClientTemplate().delete("removeAllProducts");
	}

	public void saveFeature(Product product) {
		getSqlMapClientTemplate().insert("saveFeature",product);		
	}

	public void saveProduct(Product product) {
		getSqlMapClientTemplate().insert("saveProduct",product);
	}


}
