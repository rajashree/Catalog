/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.Address;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.AddressRepository;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/9/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("addressRespository")
@Transactional
public class AddressRepositoryImpl extends RepositoryImpl implements AddressRepository{

    public AddressRepositoryImpl() {
        super(Address.class);
    }


    public Address getResidentialAddress(String userGCN) {
        try {
            Object o = getSession().createCriteria(Address.class)
                    //.add(Restrictions.eq("userGCN", userGCN))
                    .add(Restrictions.or(Restrictions.eq("userGCN", userGCN),Restrictions.eq("alias", userGCN)))
                    .add(Restrictions.eq("addressType","residential"))
                    .uniqueResult();
            if (o != null) {
                 return (Address) o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Address getOfficeAddress(String userGCN) {
        try {
            Object o = getSession().createCriteria(Address.class)
                    //.add(Restrictions.eq("userGCN", userGCN))
                    .add(Restrictions.or(Restrictions.eq("userGCN", userGCN),Restrictions.eq("alias", userGCN)))
                    .add(Restrictions.eq("addressType","office"))
                    .uniqueResult();
            if (o != null) {
                return (Address) o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Address> getAddresses(String userGCN) {
        try {
            List<Address> o = getSession().createCriteria(Address.class)
                    //.add(Restrictions.eq("user.userGCN", userGCN))
                    .createAlias("user", "u")
                    .add(Restrictions.or(Restrictions.eq("user.userGCN", userGCN),Restrictions.eq("u.alias", userGCN)))
                    .list();
            if (o != null) {
                return o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Address get(Long id) {
        try {
            Object o = getSession().createCriteria(Address.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
            if (o != null) {
                Address address = new Address();
                address.setId(id);
                address.setAddressLine1(((Address)o).getAddressLine1());
                address.setAddressLine2(((Address)o).getAddressLine2());
                address.setCity(((Address)o).getCity());
                address.setCountry(((Address)o).getCountry());
                address.setState(((Address)o).getState());
                address.setPostalCode(((Address)o).getPostalCode());
                address.setCreationDate(((Address)o).getCreationDate());
                address.setLastUpdateDate(((Address)o).getLastUpdateDate());

                return address;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

