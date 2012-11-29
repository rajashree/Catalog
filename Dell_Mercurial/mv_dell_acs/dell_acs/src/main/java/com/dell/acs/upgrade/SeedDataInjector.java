/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.upgrade;

import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.domain.UserRole;
import com.dell.acs.persistence.repository.RetailerRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.dell.acs.persistence.repository.UserRepository;
import com.dell.acs.persistence.repository.UserRoleRepository;
import com.sourcen.core.upgrade.UpgradeTask;
import com.sourcen.core.util.StringUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2764 $, $Date:: 2012-05-29 23:24:47#$
 */

public class SeedDataInjector implements UpgradeTask {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// ignored.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void run() {

		// first create user and authorize as admin.
		UserRole roleUser;
		UserRole roleAdmin = null;
		if (userRoleRepository.size() == 0) {
			roleAdmin = new UserRole("ROLE_ADMIN");
			userRoleRepository.insert(roleAdmin);

			roleUser = new UserRole("ROLE_USER");
			userRoleRepository.insert(roleUser);

			userRoleRepository.insert(new UserRole("ROLE_FACEBOOK_USER"));
			userRoleRepository.insert(new UserRole("ROLE_AD_PUBLISHER"));
		}
		User admin = null;
		if (userRepository.size() == 0) {

			User anonymousUser = new User();
			anonymousUser.setFirstName("anonymous");
			anonymousUser.setLastName("");
			anonymousUser.setEmail("anonymous@localhost.com");
			anonymousUser.setCreatedDate(new Date());
			anonymousUser.setModifiedDate(new Date());
			anonymousUser.setUsername("anonymous");
			anonymousUser.setPassword(StringUtils.MD5Hash("anonymous")
					+ Math.random());
			anonymousUser.setEnabled(true);
			userRepository.insert(anonymousUser);

			admin = new User();
			admin.setFirstName("admin");
			admin.setLastName("");
			admin.setEmail("admin@localhost.com");
			admin.setCreatedDate(new Date());
			admin.setModifiedDate(new Date());
			admin.setUsername("admin");
			admin.setPassword(StringUtils.MD5Hash("admin"));
			admin.setEnabled(true);
			admin.setRoles(new HashSet<UserRole>(userRoleRepository.getAll()));
			userRepository.insert(admin);

			User marketvineManager = new User();
			marketvineManager.setFirstName("Marketvine Manager");
			marketvineManager.setLastName("");
			marketvineManager.setEmail("marketvineManager@localhost.com");
			marketvineManager.setCreatedDate(new Date());
			marketvineManager.setModifiedDate(new Date());
			marketvineManager.setUsername("marketvine");
			marketvineManager.setPassword(StringUtils.MD5Hash("m@rK3tv!n#"));
			marketvineManager.setEnabled(true);
			HashSet<UserRole> marketvineRoles = new HashSet<UserRole>();
			marketvineRoles.add(roleAdmin);
			marketvineManager.setRoles(marketvineRoles);
			userRepository.insert(marketvineManager);

			User dellMPPManager = new User();
			dellMPPManager.setFirstName("Dell MPP Manager");
			dellMPPManager.setLastName("");
			dellMPPManager.setEmail("dellmppmgr@dellmppmgr.com");
			dellMPPManager.setCreatedDate(new Date());
			dellMPPManager.setModifiedDate(new Date());
			dellMPPManager.setUsername("dellmppmgr");
			dellMPPManager.setPassword(StringUtils.MD5Hash("de!1M55mg^"));
			dellMPPManager.setEnabled(true);
			HashSet<UserRole> dellMPPRoles = new HashSet<UserRole>();
			dellMPPManager.setRoles(dellMPPRoles);
			userRepository.insert(dellMPPManager);
		}

		// TODO - enable RunAs function so the seedData is executed by admin.
		// Authentication adminAuthentication = null;
		// SecurityContextHolder.getContext().setAuthentication(adminAuthentication);

		if (retailerRepository.size() == 0
				&& retailerSiteRepository.size() == 0) {
			Retailer dellRetailer = new Retailer();
			dellRetailer.setName("dell");
			dellRetailer.setDescription("Dell Retailer");
			dellRetailer.setUrl("dell.com");
			dellRetailer.setCreatedDate(new Date());
			dellRetailer.setModifiedDate(new Date());
			dellRetailer.setActive(true);
			retailerManager.createRetailer(dellRetailer);

			RetailerSite dellRetailerSite = new RetailerSite();
			dellRetailerSite.setRetailer(dellRetailer);
			dellRetailerSite.setSiteName("dell");
			dellRetailerSite.setSiteUrl("dell.com");
			dellRetailerSite.setCreatedDate(new Date());
			dellRetailerSite.setModifiedDate(new Date());
			dellRetailerSite.setActive(true);
			retailerManager.createRetailerSite(dellRetailerSite);
		}

	}

	/**
	 * RetailerRepository bean injection.
	 */
	@Autowired
	private RetailerRepository retailerRepository;

	/**
	 * RetailerSiteRepository bean injection.
	 */
	@Autowired
	private RetailerSiteRepository retailerSiteRepository;

	/**
	 * UserRepository bean injection.
	 */
	@Autowired
	private UserRepository userRepository;

	/**
	 * UserRoleRepository bean injection.
	 */
	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private RetailerManager retailerManager;

	public void setRetailerManager(final RetailerManager retailerManager) {
		this.retailerManager = retailerManager;
	}

	/**
	 * Setter for retailerRepository.
	 */
	public void setRetailerRepository(RetailerRepository retailerRepository) {
		this.retailerRepository = retailerRepository;
	}

	/**
	 * Setter for retailerSiteRepository.
	 */
	public void setRetailerSiteRepository(
			RetailerSiteRepository retailerSiteRepository) {
		this.retailerSiteRepository = retailerSiteRepository;
	}

	/**
	 * Setter for userRepository.
	 */
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Setter for userRoleRepository.
	 */
	public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}
}
