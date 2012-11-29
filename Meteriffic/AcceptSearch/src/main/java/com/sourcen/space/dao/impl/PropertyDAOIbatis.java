package com.sourcen.space.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.sourcen.space.dao.PropertyDAO;
import com.sourcen.space.model.Property;

public class PropertyDAOIbatis extends SqlMapClientDaoSupport implements
		PropertyDAO {

	public Property getProperty(String key) {
		return (Property) this.getSqlMapClientTemplate().queryForObject(
				"getProperty", key);
	}

	public void updateProperty(Property property) {

		getSqlMapClientTemplate().insert("updateProperty",property);
	}
	public void deleteProperty(String key) {

		getSqlMapClientTemplate().delete("deleteProperty",key);
	}

	public void saveProperty(Property property) {
		getSqlMapClientTemplate().insert("saveProperty",property);
	}

	@SuppressWarnings("unchecked")
	public List<Property> getAllProperty() {
		return getSqlMapClientTemplate().queryForList("getAllProperty");

	}

}
