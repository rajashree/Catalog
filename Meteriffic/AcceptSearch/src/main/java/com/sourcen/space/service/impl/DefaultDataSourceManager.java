package com.sourcen.space.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import javassist.NotFoundException;

import org.apache.commons.dbcp.BasicDataSource;

import com.sourcen.space.service.ApplicationManager;
import com.sourcen.space.service.DataSourceManager;

public class DefaultDataSourceManager extends BasicDataSource implements DataSourceManager {

	private ApplicationManager applicationManager=null;
	
	public DefaultDataSourceManager(ApplicationManager applicationManager ){
		this.applicationManager=applicationManager;		
		
	}
	public void init() {
		try {
			this.driverClassName=applicationManager.getXMLProperty("driverClassName");
			this.url=applicationManager.getXMLProperty("url");
			this.username=applicationManager.getXMLProperty("username");
		  } catch (NotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public boolean isEnabled() {
		
		return false;
	}

	public void restart() {
		
		
	}

	public void start() {
		
		
	}

	public void stop() {
		
		
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}
	
	
	public static void main(String args[]){
		ApplicationManager applicationManager = new DefaultApplicationManager();
		applicationManager.setConfigFile("c:/wtp/setup.xml");
		
		applicationManager.init();
		try {
			System.out.println(applicationManager.getXMLProperty("driverClassName"));
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DefaultDataSourceManager defaultDataSourceManager= new DefaultDataSourceManager(applicationManager);
		defaultDataSourceManager.init();
		try {
			DataSource dataSource=defaultDataSourceManager.createDataSource();
			Connection con=dataSource.getConnection();
			Statement st=con.createStatement();
			st.execute("select 1");
			ResultSet rs=st.getResultSet();
			rs.next();
		System.out.println(rs.getInt(1));	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
