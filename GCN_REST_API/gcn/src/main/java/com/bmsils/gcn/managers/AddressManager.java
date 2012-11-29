/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.persistence.domain.Address;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/9/12
 * Time: 1:05 PM
 * Service to handle Addresses related function calls
 */
public interface AddressManager  extends Manager{

    /**
     * get Address
     * @param addressId
     * @return
     */
    public Address getAddress(Long addressId);

    /**
     * create Address
     * @param address
     * @return
     */
    public Address createAddress(Address address);

    /**
     * update Address
     * @param address
     * @return
     */
    public Address updateAddress(Address address);
}