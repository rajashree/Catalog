/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sourcen.dataimport.service.DataImportLookupService;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1720 $, $Date:: 2012-04-18 11:45:08#$
 */

/**
 * {@inheritDoc}
 */
public final class DataImportLookupServiceImpl implements InitializingBean,
		DataImportLookupService {

	/**
	 * logger class
	 */
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(DataImportLookupServiceImpl.class);

	private static final Map<String, Map<String, Long>> tableCaches = new ConcurrentHashMap<String, Map<String, Long>>();

	private Class<DataImportLookup> dataImportLookupClass;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataImportLookupClass,
				"dataImportLookupClass cannot be null");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long getKey(final String tableName, final String srcKey) {
		if (srcKey == null) {
			logger.error("srcKey cannot be null for table:=" + tableName);
			return null;
		}
		try {
			Criteria criteria = getSession().createCriteria(
					dataImportLookupClass);
			criteria.add(Restrictions.eq("pk.srcTableName", tableName)).add(
					Restrictions.eq("pk.srcTableId", srcKey));
			List<DataImportLookup> list = criteria.list();
			if (!list.isEmpty()) {
				return list.iterator().next().getDestTableId();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void putKeys(List<Object[]> keys) {
		if (keys == null || keys.isEmpty()) {
			throw new RuntimeException("keys cannot be empty.");
		}
		
		
		// 0=srcTableName, 1= srcKey, 2=destTable, 3=destKey});
		String srcTableName = keys.get(0)[0].toString();
		Assert.notNull(srcTableName, "Source tableName must not be null");
		Assert.notNull(keys.get(0)[2],
				"destination table name must not be null");
		Map<String, Long> tableCache = getTableCache(srcTableName);

		for (Object[] keyObj : keys) {
			try {
				if (!(keyObj[1] instanceof String)) {
					// Do not cache because it is a special key to by pass dataImportLookup table.
					continue; 
				}
				
				DataImportLookup entity = (DataImportLookup) dataImportLookupClass
						.newInstance();
				entity.setSrcTableName((String) keyObj[0]);
				entity.setSrcTableId((String) keyObj[1]);
				entity.setDestTableName((String) keyObj[2]);
				entity.setDestTableId((Long) keyObj[3]);
				getSession().saveOrUpdate(entity);
				tableCache.put(entity.getSrcTableId(), entity.getDestTableId());
			} catch (NonUniqueObjectException nuoe) {
				logger.warn(nuoe.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		try {
			getSession().flush();
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putKey(Object[] keyObj) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public synchronized Map<String, Long> getTableCache(final String tableName) {
		Map<String, Long> result = tableCaches.get(tableName);
		if (result == null) {
			result = new HashMap<String, Long>();
			tableCaches.put(tableName, result);
		}
		try {
			Criteria criteria = getSession().createCriteria(
					dataImportLookupClass);
			criteria.add(Restrictions.eq("pk.srcTableName", tableName));
			List rows = criteria.list();
			for (Object rowObj : rows) {
				DataImportLookup row = (DataImportLookup) rowObj;
				result.put(row.getSrcTableId(), row.getDestTableId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getMessage());
		}
		return result;
	}

	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDataImportLookupClass(
			Class<DataImportLookup> dataImportLookupClass) {
		this.dataImportLookupClass = dataImportLookupClass;
	}
}
