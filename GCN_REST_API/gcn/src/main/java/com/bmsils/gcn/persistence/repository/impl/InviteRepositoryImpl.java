/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.persistence.domain.Invite;
import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.InviteRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("inviteRepository")
public class InviteRepositoryImpl extends RepositoryImpl implements InviteRepository  {

    public InviteRepositoryImpl(){
        super(Invite.class);
    }


    @Override
    public List<Invite> getInvites(String gcn) {
        LinkedList<Invite> invites = new LinkedList<Invite>();
        try {
            Query  query = getSession().getNamedQuery("getInviteRequests");
            query.setParameter("gcn", gcn);
            List<Object> oList = query.list();
            if (oList != null) {
                for (Object inviteObj : oList) {
                    Invite invite = (Invite) inviteObj;
                    if(invite.getInviteeGcn() !=null){
                        if(invite.getInviteeGcn().getUserGCN() != null){
                            invite.setInviteeGcn(userRepository.get(invite.getInviteeGcn().getUserGCN()));
                        }
                    }
                    if(invite.getInviterGcn() !=null){
                        if(invite.getInviterGcn().getUserGCN() != null){
                            invite.setInviterGcn(userRepository.get(invite.getInviterGcn().getUserGCN()));
                        }
                    }
                    invites.add(invite);
                }
            }
            return invites;
        }catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Invite get(String inviterGCN, String inviteeGCN) {
        try {
            Query  query = getSession().getNamedQuery("getInvite");
            query.setParameter("inviterGCN", inviterGCN);
            query.setParameter("inviteeGCN", inviteeGCN);
            Object o = query.uniqueResult();
            if (o != null) {
                return (Invite) o;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<User> getContacts(String gcn) {
        LinkedList<User> contacts = new LinkedList<User>();
        Map<String,Integer> contactGCNs = new HashMap<String, Integer>();
        try {
            Query  query = getSession().getNamedQuery("getContacts");
            query.setParameter("gcn", gcn);
            List<Object[]> oList = query.list();
            if (oList != null) {
                for (Object[] cObj : oList) {
                    if(((String)cObj[0]).equalsIgnoreCase(gcn)){
                        contactGCNs.put((String)cObj[1],(Integer)cObj[2]);
                    }else{
                        contactGCNs.put((String)cObj[0],(Integer)cObj[2]);
                    }
                }
            }
            for(String userGCN : contactGCNs.keySet()){
                User user =  userRepository.get(userGCN);
                if(user.getUserGCN().equalsIgnoreCase(gcn))
                    continue;
                if(user.getAlias() != null && user.getAlias().equalsIgnoreCase(gcn))
                    continue;
                user.setBlockedUser((contactGCNs.get(userGCN).intValue() == ApplicationConstants.SubscriptionStatus.INVITE_BLOCKED.getValue())?true:false);
                contacts.add(user);
            }

            return contacts;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /* @Override
        public Collection<User> getRecentUpdatedContacts(String gcn) {
            LinkedList<User> contacts = new LinkedList<User>();
            Set<String> contactGCNs = new HashSet<String>();
            try {
                Session session = getSession();

                Criteria criteria = session.createCriteria(Invite.class);
                List<Object> oList = criteria
                        .createAlias("inviterGcn", "inviter")
                        .createAlias("inviteeGcn", "invitee")
                        .add(Restrictions.or(
                                //Restrictions.eq("inviterGCN.userGCN", gcn),
                                //Restrictions.eq("inviteeGCN.userGCN", gcn)))
                                Restrictions.or(Restrictions.eq("inviterGcn.userGCN", gcn),Restrictions.eq("inviter.alias", gcn)),
                                Restrictions.or(Restrictions.eq("inviteeGcn.userGCN", gcn),Restrictions.eq("invitee.alias", gcn))))
                        .add(Restrictions.eq("subscriptionStatus",3))
                        .list();
                if (oList != null) {
                    for (Object iObj : oList) {
                        contactGCNs.add((((Invite) iObj).getInviterGcn()).getUserGCN());
                        contactGCNs.add((((Invite)iObj).getInviteeGcn()).getUserGCN());
                    }
                }
                if (contactGCNs != null) {
                    for (String cObj : contactGCNs) {
                        User user =  userRepository.get(cObj);
                        if(user.getUserGCN().equalsIgnoreCase(gcn) || user.getAlias().equalsIgnoreCase(gcn))
                            continue;
                        contacts.add(user);
                    }
                }
                return contacts;
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    */
    @Override
    public List<Invite> getEligibleInvitesForPN(String duration) {
        LinkedList<Invite> invites = new LinkedList<Invite>();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, - (Integer.parseInt(duration)/60000));
            List<Object> oList = getSession().createCriteria(Invite.class)
                    .add(Restrictions.ge("creationDate", calendar.getTime()))
                    .add(Restrictions.eq("subscriptionStatus", ApplicationConstants.SubscriptionStatus.INVITE_SENT.getValue()))
                    .list();
            if (oList != null) {
                for (Object inviteObj : oList) {
                    Invite invite = (Invite) inviteObj;
                    if(invite.getInviteeGcn() !=null){
                        if(invite.getInviteeGcn().getUserGCN() != null){
                            invite.setInviteeGcn(userRepository.get(invite.getInviteeGcn().getUserGCN()));
                        }
                    }
                    if(invite.getInviterGcn() !=null){
                        if(invite.getInviterGcn().getUserGCN() != null){
                            invite.setInviterGcn(userRepository.get(invite.getInviterGcn().getUserGCN()));
                        }
                    }
                    invites.add(invite);
                }
                return invites;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void blockContact(String gcn, String contactGCN) {
        try {
            Query  query = getSession().getNamedQuery("getInvite");
            query.setParameter("inviterGCN", gcn);
            query.setParameter("inviteeGCN", contactGCN);
            Object o = query.uniqueResult();
            if (o != null) {
                if(((Invite)o).getSubscriptionStatus() == 3){
                    ((Invite)o).setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_BLOCKED.getValue());
                    merge(o);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void unBlockContact(String gcn, String contactGCN) {
        try {
            Query  query = getSession().getNamedQuery("getInvite");
            query.setParameter("inviterGCN", gcn);
            query.setParameter("inviteeGCN", contactGCN);
            Object o = query.uniqueResult();
            if (o != null) {
                ((Invite)o).setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_ACCEPTED.getValue());
                merge(o);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public int getSubscriptionStatus(String senderGCN, String receiverGCN) {
        try {
            Object o;
            o =  getSession().createCriteria(Invite.class)
                    .add(Restrictions.or(Restrictions.and(Restrictions.eq("inviterGcn.userGCN", senderGCN),Restrictions.eq("inviteeGcn.userGCN", receiverGCN)),
                            Restrictions.and(Restrictions.eq("inviterGcn.userGCN", receiverGCN),Restrictions.eq("inviteeGcn.userGCN", senderGCN))))
                    .uniqueResult();
            if(o != null){
                return ((Invite)o).getSubscriptionStatus();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */

    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
