/**
 * 
 */
package com.dell.acs.managers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

/**
 * @author Shawn_Fisk
 *
 */
public class ManagerBase {

	/**
	 * 
	 */
	public ManagerBase() {
		// TODO Auto-generated constructor stub
	}

	protected DataFileStatisticService getDataFileStatisticService() {
		return this.dataFileStatistService;
	}


	/**
	 * ApplicationContext bean injection.
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Setter for applicationContext
	 */
	public void setApplicationContext(
			final ApplicationContext pApplicationContext) {
		this.applicationContext = pApplicationContext;
	}
	
	@Autowired
	private DataFileStatisticService dataFileStatistService;

	public void setDataFileStatisticService(final DataFileStatisticService pDataFileStatistService) {
		this.dataFileStatistService = pDataFileStatistService;
	}
	
	protected Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Autowired
	@Qualifier("hibernateSessionFactory")
	private SessionFactory sessionFactory;

	public void setSessionFactory(final SessionFactory pSessionFactory) {
		this.sessionFactory = pSessionFactory;
	}
}
