/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.managers.DataImportManager.FileStatus;
import com.dell.acs.managers.DataImportManager.ImportType;
import com.dell.acs.persistence.domain.DataFile;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.DataFileRepository;
import com.sourcen.core.persistence.domain.constructs.ThreadLockAware;
import com.sourcen.core.persistence.repository.impl.hibernate.IdentifiableEntityRepositoryImpl;
import com.sourcen.core.util.DateUtils;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: ashish $
 * @version $Revision: 3621 $, $Date:: 2012-06-26 10:36:58#$
 */
@Repository
public class DataFileRepositoryImpl extends
		IdentifiableEntityRepositoryImpl<Long, DataFile> implements
		DataFileRepository {

	/**
	 * Constructor
	 */
	public DataFileRepositoryImpl() {
		super(DataFile.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataFile> getFiles(final RetailerSite retailerSite) {
		return getSession().createCriteria(DataFile.class)
				.add(Restrictions.eq("retailerSite.id", retailerSite.getId()))
				.addOrder(Order.desc("modifiedDate")).list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DataFile> getDataFilesWithSrcPath(
			final Set<String> availableFiles) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.in("srcFile", availableFiles));
		return (List<DataFile>) criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// patch for https://jira.marketvine.com/browse/CS-330
	// making locks atomic on the repository rather than manager.
	@Transactional(readOnly = true)
	public DataFile getLatestImportedFile() {

		// select d.* from data_files d where status =0
		// AND (select count(id) from data_files WHERE status=1) =0 order by
		// priority desc, id asc LIMIT 1;

		List activeRetailerIds = getSession().createCriteria(DataFile.class)
				.add(Restrictions.eq("status", 1))
				.setProjection(Projections.property("retailerSite.id")).list();

		Criteria criteria = getSession().createCriteria(DataFile.class);
		if (activeRetailerIds != null && !activeRetailerIds.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("retailerSite.id",
					activeRetailerIds)));
		}
		criteria.add(Restrictions.eq("status", 0))
				.addOrder(Order.desc("priority")).addOrder(Order.asc("id"))
				.setMaxResults(1).list();

		List result = criteria.list();
		if (result.isEmpty()) {
			return null;
		}
		return (DataFile) result.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getLatestNonImagesDataFile(java.util.Collection, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public DataFile getLatestNonImagesDataFile(
			Collection<Long> retailerSiteIds, Integer currentState,
			Integer nextState) {
		Session session = this.getSession();
		DataFile dataFile = null;

		// select d.* from data_files d where status =0
		// AND (select count(id) from data_files WHERE status=1) =0 order by
		// priority desc, id asc LIMIT 1;

		Criteria runningCriteria = session.createCriteria(DataFile.class)
				.add(Restrictions.eq("status", FileStatus.PROCESSING))
				.add(Restrictions.ne("importType", "images"));
		if (retailerSiteIds.size() > 0) {
			runningCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}

		@SuppressWarnings("unchecked")
		Collection<DataFile> runningSet = runningCriteria.list();
		Collection<RetailerSite> runningRetailerSiteSet = new ArrayList<RetailerSite>();
		for (DataFile running : runningSet) {
			runningRetailerSiteSet.add(running.getRetailerSite());
		}

		// Get the set of waiting processes not in the running set but still
		// part of the retailer site set.
		Criteria nonRunningCriteria = session.createCriteria(DataFile.class)
				.add(Restrictions.eq("status", FileStatus.IN_QUEUE))
				.add(Restrictions.ne("importType", "images"))
				.addOrder(Order.desc("priority")).addOrder(Order.asc("id"));
		if (runningRetailerSiteSet.size() > 0) {
			nonRunningCriteria.add(Restrictions.not(Restrictions.in(
					"retailerSite", runningRetailerSiteSet)));
		}
		if (retailerSiteIds.size() > 0) {
			nonRunningCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}

		@SuppressWarnings("unchecked")
		Collection<DataFile> nonRunningSet = nonRunningCriteria.list();

		if (nonRunningSet.size() == 0) {
			// No retailer site current not running, so look for another running
			// at equal priority
			DataFile nextDataFile = null;
			for (DataFile running : runningSet) {
				Criteria criteria = session
						.createCriteria(DataFile.class)
						.add(Restrictions.eq("retailerSite",
								running.getRetailerSite()))
						.add(Restrictions.eq("status", FileStatus.IN_QUEUE))
						.add(Restrictions.ne("importType", "images"))
						.add(Restrictions.eq("priority", running.getPriority()))
						.setMaxResults(1);

				@SuppressWarnings("rawtypes")
				List result = criteria.list();
				if (result.isEmpty()) {
					continue;
				} else {
					nextDataFile = (DataFile) result.iterator().next();
					break;
				}
			}

			dataFile = nextDataFile;
		} else {
			dataFile = nonRunningSet.iterator().next();
		}

		if (dataFile != null) {
			dataFile = this.acquireLock(dataFile, currentState, nextState);
		}

		return dataFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getLatestNonImagesDataFile(java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = true)
	public DataFile getLatestImagesDataFile(Collection<Long> retailerSiteIds) {

		// select d.* from data_files d where status =0
		// AND (select count(id) from data_files WHERE status=1) =0 order by
		// priority desc, id asc LIMIT 1;

		// Get distinct set of retailers current processing request and skip
		// them.

		Criteria processingRetailer = getSession().createCriteria(
				DataFile.class).add(
				Restrictions.eq("status", FileStatus.PROCESSING));
		if (retailerSiteIds.size() > 0) {
			processingRetailer.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}
		processingRetailer.setProjection(
				Projections.distinct(Projections.property("retailerSite.id")))
				.list();

		@SuppressWarnings("rawtypes")
		List activeRetailerIds = processingRetailer.list();

		Criteria criteria = getSession().createCriteria(DataFile.class);
		if (activeRetailerIds != null && !activeRetailerIds.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("retailerSite.id",
					activeRetailerIds)));
		}
		criteria.add(Restrictions.eq("status", FileStatus.IN_QUEUE))
				.add(Restrictions.eq("importType", "images"))
				.addOrder(Order.desc("priority")).addOrder(Order.asc("id"))
				.setMaxResults(1).list();

		@SuppressWarnings("rawtypes")
		List result = criteria.list();
		if (result.isEmpty()) {
			return null;
		}
		return (DataFile) result.iterator().next();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return
	 */
	@Override
	public DataFile getPendingFileForExport(RetailerSite retailerSite) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("outputStatus", 0));
		// status is Done, Error Read
		Criterion criterion1 = Restrictions.or(Restrictions.eq("status", 2),
				Restrictions.eq("status", 3));
		// status is Error Write
		// Commenting out the Error Write condition as this will not have any
		// products
		// criteria.add(Restrictions.or(criterion1,Restrictions.eq("status",10)));
		criteria.add(Restrictions.eq("importType",
				"com.dell.acs.persistence.domain.Product"));
		criteria.add(Restrictions.eq("retailerSite.id", retailerSite.getId()));
		criteria.addOrder(Order.desc("createdDate"));
		criteria.setMaxResults(1);

		List result = criteria.list();

		if (result.isEmpty()) {
			return null;
		}
		return (DataFile) result.iterator().next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// patch for https://jira.marketvine.com/browse/CS-330
	// making locks atomic on the repository rather than manager.
	@Transactional(readOnly = true)
	public DataFile getLatestImageFile() {
		List dbList = getSession().createCriteria(DataFile.class)
				.add(Restrictions.eq("importType", "images"))
				.add(Restrictions.eq("status", 6)).setMaxResults(1).list();
		if (dbList == null || dbList.isEmpty()) {
			return null;
		}
		return (DataFile) dbList.iterator().next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataFile> getAllDataFilesInProcessingStatus() {
		List<DataFile> dataFileList = getSession()
				.createCriteria(DataFile.class)
				.add(Restrictions.or(Restrictions.eq("status", 1),
						Restrictions.eq("status", 7))).list();
		return dataFileList;
	}

	@Override
	public Collection<String> getDownLoadedDataFiles(RetailerSite retailerSite,
			int maxResultSize) {
		// sfisk - CS-330, SQL Server requires order by fields to be in the
		// projection set for a distinct query.
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("srcFile"));
		pl.add(Projections.property("modifiedDate"));
		ArrayList dataFileList = (ArrayList) getSession()
				.createCriteria(DataFile.class)
				.add(Restrictions.eq("retailerSite.id", retailerSite.getId()))
				.setProjection(Projections.distinct(pl))
				.addOrder(Order.desc("modifiedDate"))
				.setFetchSize(maxResultSize).list();

		for (Object o : dataFileList) {
			logger.info("Files for retailer site ==>"
					+ retailerSite.getSiteName() + "::" + o.toString());
		}
		return dataFileList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getLatestPreprocessFile(java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = true)
	public DataFile getLatestPreprocessFile(Collection<Long> retailerSiteIds) {
		Session session = this.getSession();
		// select d.* from data_files d where status =
		// FileStatus.PREPROCESS_QUEUE(11)

		// Get the set of running processes for retailer site set.
		Criteria runningCriteria = session.createCriteria(DataFile.class).add(
				Restrictions.eq("status", FileStatus.PREPROCESS_RUNNING));
		if (retailerSiteIds.size() > 0) {
			runningCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}
		runningCriteria.setProjection(Projections.property("retailerSite.id"));

		List runningRetailerIds = runningCriteria.list();

		// Get the set of waiting processes not in the running set but still
		// part of the retailer site set.
		Criteria nonRunningCriteria = session.createCriteria(DataFile.class)
				.add(Restrictions.eq("status", FileStatus.PREPROCESS_QUEUE));
		if (runningRetailerIds.size() > 0) {
			nonRunningCriteria.add(Restrictions.not(Restrictions.in(
					"retailerSite.id", runningRetailerIds)));
		}
		if (retailerSiteIds.size() > 0) {
			nonRunningCriteria.add(Restrictions.in("retailerSite.id",
					retailerSiteIds));
		}
		nonRunningCriteria.add(Restrictions.eq("status",
				FileStatus.PREPROCESS_QUEUE));
		nonRunningCriteria.addOrder(Order.desc("priority")).addOrder(
				Order.asc("id"));

		List nonRunning = nonRunningCriteria.list();

		if (nonRunning.isEmpty()) {
			Criteria alreadyRunningCriteria = session.createCriteria(
					DataFile.class).add(
					Restrictions.eq("status", FileStatus.PREPROCESS_QUEUE));
			if (retailerSiteIds.size() > 0) {
				alreadyRunningCriteria.add(Restrictions.in("retailerSite.id",
						retailerSiteIds));
			}
			alreadyRunningCriteria.add(Restrictions.eq("status",
					FileStatus.PREPROCESS_QUEUE));
			alreadyRunningCriteria.addOrder(Order.desc("priority")).addOrder(
					Order.asc("id"));
			alreadyRunningCriteria.setMaxResults(1);

			List alreadyRunning = alreadyRunningCriteria.list();

			if (alreadyRunning.isEmpty()) {
				return null;
			}

			return (DataFile) alreadyRunning.iterator().next();
		} else {
			if (nonRunning.isEmpty()) {
				return null;
			}

			return (DataFile) nonRunning.iterator().next();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.DataFileRepository#getDataFileForFilePath
	 * (java.lang.String)
	 */
	@Override
	public DataFile getDataFileForFilePath(String fileName) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("filePath", fileName)).setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<DataFile> result = (List<DataFile>) criteria.list();
		if (result.isEmpty()) {
			return null;
		}
		return result.iterator().next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getDataFilesByRetailerSite(com.dell.acs.persistence.domain.RetailerSite)
	 */
	@Override
	public Collection<DataFile> getDataFilesByRetailerSite(RetailerSite rs) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("retailerSite.id", rs.getId()));
		@SuppressWarnings("unchecked")
		List<DataFile> result = (List<DataFile>) criteria.list();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public void insert(DataFile dataFile) {
		super.insert(dataFile);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> getHosts() {
		return (List<String>) getSession()
				.createCriteria(DataFile.class)
				.add(Restrictions.isNotNull("host"))
				.setProjection(
						Projections.distinct(Projections.property("host")))
				.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getDataFilesByRetailerSiteAndHost
	 * (com.dell.acs.persistence.domain.RetailerSite, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Collection<DataFile> getDataFilesByRetailerSiteAndHost(
			RetailerSite rs, String host) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("retailerSite.id", rs.getId()));
		criteria.add(Restrictions.eq("host", host));
		@SuppressWarnings("unchecked")
		List<DataFile> result = (List<DataFile>) criteria.list();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dell.acs.persistence.repository.DataFileRepository#
	 * getRecoverPreprocessFile(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Collection<DataFile> getRecoverPreprocessFiles(String host) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("status", FileStatus.PREPROCESS_RUNNING))
				.add(Restrictions.eq("host", host));

		return (Collection<DataFile>) criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.DataFileRepository#getRecoverDataFiles
	 * (java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Collection<DataFile> getRecoverDataFiles(String host) {
		Criteria criteria = getSession().createCriteria(DataFile.class);
		criteria.add(Restrictions.eq("status", FileStatus.PROCESSING)).add(
				Restrictions.eq("host", host));

		return (Collection<DataFile>) criteria.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.DataFileRepository#atomicUpdateAll
	 * (java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = false)
	public DataFile atomicUpdate(DataFile dataFile) {
		Session session = getSession();
		DataFile df = (DataFile) session.get(DataFile.class, dataFile.getId());
		df.setCurrentRow(dataFile.getCurrentRow());
		df.setEndDate(dataFile.getEndDate());
		df.setEndTime(dataFile.getEndTime());
		df.setHost(dataFile.getHost());
		df.setStartDate(dataFile.getStartDate());
		df.setStartTime(dataFile.getStartTime());
		df.setNumRows(dataFile.getNumRows());
		df.setNumErrorRows(dataFile.getNumErrorRows());
		session.update(df);
		session.flush();
		return df;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dell.acs.persistence.repository.DataFileRepository#acquireLock(com
	 * .dell.acs.persistence.domain.DataFile, java.lang.Integer,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	public DataFile acquireLock(DataFile dataFile, Integer currentStatus,
			Integer nextStatus, String host) {
		// TODO sfisk 2012/08/23 - Need to clean this update, add support for
		// StatusAware to include host.
		// TODO - replace with HQL so that we can do update dataFile SET
		// status=? WHERE id=? AND status=?
		// so that we get an absolute DB level lock. This will still work as we
		// are using VersionAware.
		try {
			Session session = getSession();
			DataFile freshObj = (DataFile) session.get(dataFile.getClass(),
					dataFile.getId());

			session.buildLockRequest(LockOptions.UPGRADE)
					.setLockMode(LockMode.PESSIMISTIC_WRITE).setTimeOut(10000)
					.lock(freshObj);
			if (freshObj.getStatus().equals(currentStatus)) {
				logger.debug("Updating status for id:=" + dataFile.getId()
						+ " oldStatus :=" + currentStatus + " newStatus:="
						+ nextStatus);
				freshObj.setHost(host);
				freshObj.setStatus(nextStatus);
				if (freshObj instanceof ThreadLockAware) {
					String threadId = DateUtils.JVM_START_TIME_UTC + "-"
							+ Thread.currentThread().getId();
					((ThreadLockAware) freshObj).setLockedThread(threadId);
				}
				session.update(freshObj);
				session.flush();
				return onFindForObject(freshObj);
			}
			return null;
		} catch (Exception e) {
			logger.error("unable to lock dataFile :=" + dataFile.getId());
		}
		return null;
	}
}
