/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.AddressManager;
import com.bmsils.gcn.persistence.domain.Address;
import com.bmsils.gcn.persistence.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/9/12
 * Time: 1:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AddressManagerImpl implements AddressManager{
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);

    @Override
    @Transactional
    public Address getAddress(Long addressId) {
       return (Address)addressRepository.getUniqueByExample(new Address(addressId));
    }

    @Override
    @Transactional
    public Address createAddress(Address address) {
        addressRepository.insert(address);
        return address;
    }

    @Override
    @Transactional
    public Address updateAddress(Address address) {
        addressRepository.put(address);
        return address;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private AddressRepository addressRepository;

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
}
