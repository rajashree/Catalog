/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.notification.ForgotPwdNotification;
import com.bmsils.gcn.persistence.domain.Address;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.domain.UserProperty;
import com.bmsils.gcn.persistence.repository.AddressRepository;
import com.bmsils.gcn.persistence.repository.ImageRepository;
import com.bmsils.gcn.persistence.repository.UserPropertyRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import com.bmsils.gcn.utils.GCNUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("userRepository")
@Transactional
public class UserRepositoryImpl extends RepositoryImpl implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User get(String userGCN, String password){
        try {
            Object o = getSession().createCriteria(User.class)
                    //.add(Restrictions.eq("userGCN", userGCN))
                    .add(Restrictions.or(Restrictions.eq("userGCN", userGCN),Restrictions.eq("alias", userGCN)))
                    .uniqueResult();
            if (o != null) {
                if(GCNUtils.md5Encoder.isPasswordValid(((User)o).getPassword(),password, GCNUtils.encoderSalt));
                    return (User) o;

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addAlias(String userGCN, String alias) {
        try {
            Object o = getSession().createCriteria(User.class)
                   .add(Restrictions.eq("userGCN", userGCN))
                   .uniqueResult();
            if (o != null) {
               ((User)o).setAlias(alias);
                merge(o);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void removeAlias(String userGCN) {
        try {
            Object o = getSession().createCriteria(User.class)
                    .add(Restrictions.eq("userGCN", userGCN))
                    .uniqueResult();
            if (o != null) {
                ((User)o).setAlias(null);
                merge(o);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public int getTotalGCNCount(String email) {
        try {
            Criteria criteria = getSession().createCriteria(User.class);
            List<Object> oList = criteria
                    .add(Restrictions.eq("emailId", email))
                    .list();
            if (oList != null) {
                return oList.size();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getUserGCN(String gcn) {
        try{
            Object o = getSession().createCriteria(User.class)
                    .add(Restrictions.or(Restrictions.eq("userGCN", gcn), Restrictions.eq("alias", gcn)))
                    .uniqueResult();
            if (o != null)
                return ((User) o).getUserGCN();

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUserAndAliases(int start, int limit) {
        LinkedList<User> users = new LinkedList<User>();
        try {
            Criteria criteria = getSession().createCriteria(User.class);
            List<Object> oList = criteria
                    .setFirstResult(start)
                    .setMaxResults(limit)
                    .list();
            if (oList != null) {
                for (Object cObj : oList) {
                    User c = (User) cObj;
                    users.add(c);
                }
                return users;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ForgotPwdNotification> getEligibleForgotPasswordRequests() {
        List<ForgotPwdNotification> result = new ArrayList<ForgotPwdNotification>();
        List<UserProperty> records = userPropertyRepository.getResetPwdRecords();
        if(records != null){
            for(UserProperty record : records){
                User user = get(record.getUserGCN().getUserGCN());
                String name = "";
                name = (GCNUtils.isNullOrEmpty(user.getFirstName())?"":user.getFirstName());
                name+=(GCNUtils.isNullOrEmpty(user.getLastName())?"":" "+user.getLastName());

                result.add(new ForgotPwdNotification(user.getEmailId(),record.getUserGCN().getUserGCN(),name,record.getValue()));
            }
            return result;
        }
        return null;
    }

    public User get(String userGCN) {
        try {
        	
        	
            Object o =  getSession().createCriteria(User.class)
                    .add(Restrictions.or(Restrictions.eq("userGCN", userGCN), Restrictions.eq("alias", userGCN)))
                    .uniqueResult();
            if (o != null) {
                User user = new User();
                user.setUserGCN(((User) o).getUserGCN());
                user.setAlias(((User) o).getAlias());
                user.setAdmin(((User)o).isAdmin());
                user.setUserName(((User) o).getUserName());
                user.setFirstName(((User) o).getFirstName());
                user.setLastName(((User) o).getLastName());
                user.setPassword(((User)o).getPassword());
                user.setPhoneNumber(((User)o).getPhoneNumber());
                user.setPasswordChangeFlag(((User)o).isPasswordChangeFlag());
                user.setProfileStatus(((User)o).getProfileStatus());
                user.setOfficeEmailId(((User)o).getOfficeEmailId());
                user.setOfficeName(((User)o).getOfficeName());
                user.setOfficePhoneNumber(((User)o).getOfficePhoneNumber());
                user.setEmailId(((User)o).getEmailId());
                user.setFacebookId(((User)o).getFacebookId());
                user.setTwitterId(((User)o).getTwitterId());
                user.setLinkedinId(((User)o).getLinkedinId());
                user.setLastSyncDevice(((User)o).getLastSyncDevice());
                user.setCreationDate(((User)o).getCreationDate());
                user.setLastUpdateDate(((User)o).getLastUpdateDate());
                user.setPresence(((User)o).getPresence());

                if(imageRepository.getDefaultImage(((User)o).getUserGCN()) != null){
                       if(ArrayUtils.isNotEmpty(imageRepository.getDefaultImage(((User)o).getUserGCN()).getImage())){
                           user.setAvatar(imageRepository.getDefaultImage(((User)o).getUserGCN()).getImage());
                       }
                }

                if(((User)o).getResidentialAddress() !=null){
                    if(((User)o).getResidentialAddress().getId() != null){

                       /* Address address = new Address();
                        address.setId(((User)o).getResidentialAddress().getId());
                        address.setAddressLine1((((User)o).getResidentialAddress()).getAddressLine1());
                        address.setAddressLine2((((User)o).getResidentialAddress()).getAddressLine2());
                        address.setCity((((User)o).getResidentialAddress()).getCity());
                        address.setCountry((((User)o).getResidentialAddress()).getCountry());
                        address.setState((((User)o).getResidentialAddress()).getState());
                        address.setPostalCode((((User)o).getResidentialAddress()).getPostalCode());
                        address.setCreationDate((((User)o).getResidentialAddress()).getCreationDate());
                        address.setLastUpdateDate((((User)o).getResidentialAddress()).getLastUpdateDate());
                        user.setResidentialAddress(address); */

                        user.setResidentialAddress(addressRepository.get(((User)o).getResidentialAddress().getId()));
                    }
                }
                if(((User)o).getOfficeAddress() !=null){
                    if(((User)o).getOfficeAddress().getId() != null){
                       /* Address address = new Address();
                        address.setId(((User)o).getOfficeAddress().getId());
                        address.setAddressLine1((((User)o).getOfficeAddress()).getAddressLine1());
                        address.setAddressLine2((((User)o).getOfficeAddress()).getAddressLine2());
                        address.setCity((((User)o).getOfficeAddress()).getCity());
                        address.setCountry((((User)o).getOfficeAddress()).getCountry());
                        address.setState((((User)o).getOfficeAddress()).getState());
                        address.setPostalCode((((User)o).getOfficeAddress()).getPostalCode());
                        address.setCreationDate((((User)o).getOfficeAddress()).getCreationDate());
                        address.setLastUpdateDate((((User)o).getOfficeAddress()).getLastUpdateDate());
                        user.setOfficeAddress(address);*/

                        user.setOfficeAddress(addressRepository.get(((User)o).getOfficeAddress().getId()));
                    }
                }
                return user;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public User getUserByEmail(String emailid) {
        try{
            Object o = getSession().createCriteria(User.class)
                    .add(Restrictions.eq("emailId", emailid))
                    .uniqueResult();
            if (o != null)
                return ((User) o);

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    AddressRepository addressRepository;

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    ImageRepository imageRepository;

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    UserPropertyRepository userPropertyRepository;

    public UserPropertyRepository getUserPropertyRepository() {
        return userPropertyRepository;
    }

    public void setUserPropertyRepository(UserPropertyRepository userPropertyRepository) {
        this.userPropertyRepository = userPropertyRepository;
    }

	@SuppressWarnings("unchecked")
	@Override
	public boolean getFromAlias(String userAlias,String Password) {
		
		System.out.println(userAlias +" "+Password);

        List<User> u =  getSession().createCriteria(User.class)
                .add(Restrictions.eq("alias", userAlias))
                .list();
        System.out.println(u.size());
        if(u.size()==0)
		   return false;
        else
        	return true;
	}
}
