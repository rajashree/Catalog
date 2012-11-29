/**
 * 
 */
package com.dell.acs.stats.thread;

import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dell.acs.stats.Stat;
import com.dell.acs.stats.StatManager;
import com.dell.acs.stats.StatUtil;
import com.mchange.v2.c3p0.PooledDataSource;

/**
 * @author srfisk
 * 
 */
public class StatsHeartBeatThread implements InitializingBean, Runnable,
		DisposableBean {
	private static final long SECOND = 1000l;
	private static final long DEFAULT_PERIOD = 30 * SECOND;
	private Logger _logger;
	private Logger _loggerAnalytics;
	private long _period;
	private boolean _running;
	private Thread _thread;
	private StatManager _manager;
	private Date startTime = new Date();

	/**
	 * Constructor
	 */
	public StatsHeartBeatThread() {
		_manager = StatUtil.getInstance();
		_thread = new Thread(this, "HeartBeatThread");
		_thread.setDaemon(true);
		_running = true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		_thread.start();
	}

	public void stop() {
	}

	@Override
	public void run() {
		StringBuilder sb = new StringBuilder();
		if (this._logger.isInfoEnabled()) {
			dumpHeader(sb);
			sb.delete(0, sb.length());

			while (_running) {
				if (_thread.isInterrupted()) {
					break;
				}

				try {
					dump(sb);
					sb.delete(0, sb.length());
					dumpAnalytics(sb);
					sb.delete(0, sb.length());
				} catch (Exception e) {
					// Ignore
				}

				synchronized (this) {
					try {
						this.wait(_period);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		}
	}

	private void dumpHeader(StringBuilder sb) {
		sb.append("tick");
		sb.append(",");
		sb.append("# threads");
		sb.append(",");
		sb.append("free memory");
		sb.append(",");
		sb.append("max memory");
		sb.append(",");

		dumpHeaderHibernateStatistic(sb);
		dumpHeaderDataSourceStatistic(sb);

		for (Stat stats : _manager.getStats()) {
			stats.dumpHeader(sb);
		}

		write(sb.toString());
	}

	private void dump(StringBuilder sb) {
		sb.append(System.currentTimeMillis());
		sb.append(",");
		sb.append(Thread.activeCount());
		sb.append(",");
		sb.append(Runtime.getRuntime().freeMemory());
		sb.append(",");
		sb.append(Runtime.getRuntime().maxMemory());
		sb.append(",");

		dumpHibernateStatistic(sb);
		dumpDataSourceStatistic(sb);

		for (Stat stats : _manager.getStats()) {
			stats.dumpValues(sb);
		}

		write(sb.toString());
	}

	private void write(String msg) {
		if (_logger.isInfoEnabled()) {
			_logger.info(msg);
		}
	}

	private void dumpAnalytics(StringBuilder sb) {
		sb.append(System.currentTimeMillis());
		sb.append(",");
		sb.append(Thread.activeCount());
		sb.append(",");
		sb.append(Runtime.getRuntime().freeMemory());
		sb.append(",");
		sb.append(Runtime.getRuntime().maxMemory());
		sb.append(",");
		if (this.dataSource != null) {
			PooledDataSource pds = (PooledDataSource) this.dataSource;
			try {
				sb.append(pds.getNumConnectionsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			Date now = new Date();
			sb.append(now.getTime() - this.startTime.getTime());
			sb.append(",");
		}
		
		writeAnalytics(sb.toString());
	}

	private void writeAnalytics(String msg) {
		if (_loggerAnalytics.isInfoEnabled()) {
			_loggerAnalytics.info(msg);
		}
	}

	private void dumpHeaderHibernateStatistic(StringBuilder sb) {
		Statistics stats = this.sessionFactory.getStatistics();
		// Bug in hibernate, if statistics is enabled then it throw null pointer
		// exception
		// related to ehcache being enabled.
		// stats.setStatisticsEnabled(true);

		if (stats.isStatisticsEnabled()) {
			// All the names here.
			sb.append("Hibernate.CloseStatementCount");
			sb.append(",");
			sb.append("Hibernate.CollectionFetchCount");
			sb.append(",");
			sb.append("Hibernate.CollectionLoadCount");
			sb.append(",");
			sb.append("Hibernate.CollectionRecreateCount");
			sb.append(",");
			sb.append("Hibernate.CollectionRemoveCount");
			sb.append(",");
			sb.append("Hibernate.CollectionUpdateCount");
			sb.append(",");
			sb.append("Hibernate.ConnectCount");
			sb.append(",");
			sb.append("Hibernate.EntityDeleteCount");
			sb.append(",");
			sb.append("Hibernate.EntityFetchCount");
			sb.append(",");
			sb.append("Hibernate.EntityInsertCount");
			sb.append(",");
			sb.append("Hibernate.EntityLoadCount");
			sb.append(",");
			sb.append("Hibernate.EntityUpdateCount");
			sb.append(",");
			sb.append("Hibernate.FlushCount");
			sb.append(",");
			sb.append("Hibernate.NaturalIdCacheHitCount");
			sb.append(",");
			sb.append("Hibernate.NaturalIdCacheMissCount");
			sb.append(",");
			sb.append("Hibernate.NaturalIdCachePutCount");
			sb.append(",");
			sb.append("Hibernate.NaturalIdQueryExecutionCount");
			sb.append(",");
			sb.append("Hibernate.QueryExecutionMaxTimeQueryString");
			sb.append(",");
			sb.append("Hibernate.SecondLevelCacheHitCount");
			sb.append(",");
			sb.append("Hibernate.SecondLevelCacheMissCount");
			sb.append(",");
			sb.append("Hibernate.SecondLevelCachePutCount");
			sb.append(",");
			sb.append("Hibernate.SessionCloseCount");
			sb.append(",");
			sb.append("Hibernate.SessionOpenCount");
			sb.append(",");
			sb.append("Hibernate.SuccessfulTransactionCount");
			sb.append(",");
			sb.append("Hibernate.TransactionCount");
			sb.append(",");
			sb.append("Hibernate.UpdateTimestampsCacheHitCount");
			sb.append(",");
			sb.append("Hibernate.UpdateTimestampsCacheMissCount");
			sb.append(",");
			sb.append("Hibernate.UpdateTimestampsCachePutCount");
			sb.append(",");
		}
	}

	private void dumpHibernateStatistic(StringBuilder sb) {
		Statistics stats = this.sessionFactory.getStatistics();

		if (stats.isStatisticsEnabled()) {
			// All the values here.
			sb.append(stats.getCloseStatementCount());
			sb.append(",");
			sb.append(stats.getCollectionFetchCount());
			sb.append(",");
			sb.append(stats.getCollectionLoadCount());
			sb.append(",");
			sb.append(stats.getCollectionRecreateCount());
			sb.append(",");
			sb.append(stats.getCollectionRemoveCount());
			sb.append(",");
			sb.append(stats.getCollectionUpdateCount());
			sb.append(",");
			sb.append(stats.getConnectCount());
			sb.append(",");
			sb.append(stats.getEntityDeleteCount());
			sb.append(",");
			sb.append(stats.getEntityFetchCount());
			sb.append(",");
			sb.append(stats.getEntityInsertCount());
			sb.append(",");
			sb.append(stats.getEntityLoadCount());
			sb.append(",");
			sb.append(stats.getEntityUpdateCount());
			sb.append(",");
			sb.append(stats.getFlushCount());
			sb.append(",");
			sb.append(stats.getNaturalIdCacheHitCount());
			sb.append(",");
			sb.append(stats.getNaturalIdCacheMissCount());
			sb.append(",");
			sb.append(stats.getNaturalIdCachePutCount());
			sb.append(",");
			sb.append(stats.getNaturalIdQueryExecutionCount());
			sb.append(",");
			sb.append(stats.getQueryExecutionMaxTimeQueryString());
			sb.append(",");
			sb.append(stats.getSecondLevelCacheHitCount());
			sb.append(",");
			sb.append(stats.getSecondLevelCacheMissCount());
			sb.append(",");
			sb.append(stats.getSecondLevelCachePutCount());
			sb.append(",");
			sb.append(stats.getSessionCloseCount());
			sb.append(",");
			sb.append(stats.getSessionOpenCount());
			sb.append(",");
			sb.append(stats.getSuccessfulTransactionCount());
			sb.append(",");
			sb.append(stats.getTransactionCount());
			sb.append(",");
			sb.append(stats.getUpdateTimestampsCacheHitCount());
			sb.append(",");
			sb.append(stats.getUpdateTimestampsCacheMissCount());
			sb.append(",");
			sb.append(stats.getUpdateTimestampsCachePutCount());
			sb.append(",");
		}
	}

	private void dumpHeaderDataSourceStatistic(StringBuilder sb) {
		if (this.dataSource != null) {
			sb.append("DataSource.NumBusyConnectionsAllUsers");
			sb.append(",");
			sb.append("DataSource.NumBusyConnectionsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumConnectionsAllUsers");
			sb.append(",");
			sb.append("DataSource.NumConnectionsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumFailedCheckinsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumFailedCheckoutsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumHelperThreads");
			sb.append(",");
			sb.append("DataSource.NumIdleConnectionsAllUsers");
			sb.append(",");
			sb.append("DataSource.NumIdleConnectionsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumThreadsAwaitingCheckoutDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumUnclosedOrphanedConnectionsAllUsers");
			sb.append(",");
			sb.append("DataSource.NumUnclosedOrphanedConnectionsDefaultUser");
			sb.append(",");
			sb.append("DataSource.NumUserPools");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumCheckedOutDefaultUser");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumCheckedOutStatementsAllUsers");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumConnectionsWithCachedStatementsAllUsers");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumConnectionsWithCachedStatementsDefaultUser");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumStatementsAllUsers");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumStatementsDefaultUser");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumCheckedOutStatementsAllUsers");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumConnectionsWithCachedStatementsDefaultUser");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumStatementsAllUsers");
			sb.append(",");
			sb.append("DataSource.StatementCacheNumStatementsDefaultUser");
			sb.append(",");
			sb.append("DataSource.ThreadPoolNumActiveThreads");
			sb.append(",");
			sb.append("DataSource.ThreadPoolNumIdleThreads");
			sb.append(",");
			sb.append("DataSource.ThreadPoolNumTasksPending");
			sb.append(",");
			sb.append("DataSource.ThreadPoolSize");
			sb.append(",");
			sb.append("DataSource.UpTimeMillisDefaultUser");
			sb.append(",");
		}
	}

	private void dumpDataSourceStatistic(StringBuilder sb) {
		if (this.dataSource != null) {
			PooledDataSource pds = (PooledDataSource) this.dataSource;

			try {
				sb.append(pds.getNumBusyConnectionsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumBusyConnectionsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumConnectionsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumConnectionsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumFailedCheckinsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumFailedCheckoutsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumHelperThreads());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumIdleConnectionsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumIdleConnectionsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumThreadsAwaitingCheckoutDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumUnclosedOrphanedConnectionsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumUnclosedOrphanedConnectionsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getNumUserPools());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getStatementCacheNumCheckedOutDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds
						.getStatementCacheNumCheckedOutStatementsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds
						.getStatementCacheNumConnectionsWithCachedStatementsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds
						.getStatementCacheNumConnectionsWithCachedStatementsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getStatementCacheNumStatementsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getStatementCacheNumStatementsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds
						.getStatementCacheNumCheckedOutStatementsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds
						.getStatementCacheNumConnectionsWithCachedStatementsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getStatementCacheNumStatementsAllUsers());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getStatementCacheNumStatementsDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getThreadPoolNumActiveThreads());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getThreadPoolNumIdleThreads());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getThreadPoolNumTasksPending());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getThreadPoolSize());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
			try {
				sb.append(pds.getUpTimeMillisDefaultUser());
			} catch (SQLException e) {
				sb.append(-1);
			}
			sb.append(",");
		}
	}

	public void setLogger(String loggerName) {
		_logger = Logger.getLogger(loggerName);
	}

	public void setLoggerAnalytics(String loggerName) {
		_loggerAnalytics = Logger.getLogger(loggerName);
	}

	public void setPeriod(String periodValue) {
		try {
			_period = Long.parseLong(periodValue);
		} catch (Exception e) {
			_period = DEFAULT_PERIOD;
		}
	}

	@Override
	public void destroy() throws Exception {
		_running = false;
		_thread.interrupt();
	}

	protected SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected DataSource dataSource;

	@Autowired
	@Qualifier("dataSource")
	public void setDataSource(DataSource pDataSource) {
		this.dataSource = pDataSource;
	}
}
