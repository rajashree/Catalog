/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.Address;
import com.bmsils.gcn.persistence.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/9/12
 * Time: 12:55 PM
 * AddressRepository contains data access methods pertaining to Address table related functions
 */
public interface AddressRepository  extends Repository{
    public Address getResidentialAddress(String userGCN);
    public Address getOfficeAddress(String userGCN);
    public List<Address> getAddresses(String userGCN);
    public Address get(Long id);
}