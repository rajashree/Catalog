/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository.impl;

import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.repository.InviteRepository;
import com.bmsils.gcn.persistence.repository.MessageRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/12/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("messageRepository")
@Transactional
public class MessageRepositoryImpl extends RepositoryImpl implements MessageRepository {

    //TODO : Optimize the queries pertaining to Block GCN - Remove  isSenderNotBlocked() should be part of the queries

    public MessageRepositoryImpl(){
        super(Message.class);
    }


    public List<Message> getMessages(String gcn, boolean active) {
        LinkedList<Message> messages = new LinkedList<Message>();
        try {
            List<Object> oList;
            oList =  getSession().createCriteria(Message.class)
                    .createAlias("receiverGCN", "receiver")
                    .add(Restrictions.or(Restrictions.eq("receiverGCN.userGCN", gcn),Restrictions.eq("receiver.alias", gcn)))
                    .add(Restrictions.eq("msgRecvdFlag",false))
                    .list();

            if (oList != null) {
                for (Object msgObject : oList) {
                    Message msg = (Message) msgObject;
                    if(msg.getReceiverGCN() !=null){
                        if(msg.getReceiverGCN().getUserGCN() != null){
                            msg.setReceiverGCN((userRepository.get(msg.getReceiverGCN().getUserGCN())));
                        }
                    }
                    if(msg.getSenderGCN() !=null){
                        if(msg.getSenderGCN().getUserGCN() != null){
                            msg.setSenderGCN(userRepository.get(msg.getSenderGCN().getUserGCN()));
                        }
                    }

                    if(active){
                        if(msg.getSenderGCN().getPresence() == 1){
                            if(msg.getSenderGCN() != null && msg.getReceiverGCN() != null){
                                if(isSenderNotBlocked(msg.getSenderGCN().getUserGCN(), msg.getReceiverGCN().getUserGCN())){
                                    messages.add(msg);
                                }
                            }
                        }
                    }else{
                        if(msg.getSenderGCN() != null && msg.getReceiverGCN() != null){
                            if(isSenderNotBlocked(msg.getSenderGCN().getUserGCN(), msg.getReceiverGCN().getUserGCN())){
                                messages.add(msg);
                            }
                        }
                    }
                }
                return messages;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public List<Message> getMessageHistory(String gcn, String contactGCN, Date fromDate) {
        LinkedList<Message> messages = new LinkedList<Message>();
        List<Object> oList;
        try {
            if(fromDate != null){
                oList = getSession().createCriteria(Message.class)
                        .createAlias("receiverGCN", "receiver")
                        .createAlias("senderGCN", "sender")
                        .add(Restrictions.or(
                                (Restrictions.and(
                                        Restrictions.or(Restrictions.eq("receiverGCN.userGCN", gcn),Restrictions.eq("receiver.alias", gcn)),
                                        Restrictions.or(Restrictions.eq("senderGCN.userGCN", contactGCN),Restrictions.eq("sender.alias", contactGCN)))),
                                (Restrictions.and(
                                        Restrictions.or(Restrictions.eq("senderGCN.userGCN", gcn),Restrictions.eq("sender.alias", gcn)),
                                        Restrictions.or(Restrictions.eq("receiverGCN.userGCN", contactGCN),Restrictions.eq("receiver.alias", contactGCN))))))
                        .add(Restrictions.gt("creationDate", fromDate))
                        .list();
            }else{
                oList = getSession().createCriteria(Message.class)
                        .createAlias("receiverGCN", "receiver")
                        .createAlias("senderGCN", "sender")
                                //.add(Restrictions.or(Restrictions.eq("receiverGCN.userGCN", gcn),Restrictions.eq("receiver.alias", gcn)))
                                //.add(Restrictions.or(Restrictions.eq("senderGCN.userGCN", contactGCN),Restrictions.eq("sender.alias", contactGCN)))
                        .add(Restrictions.or(
                                (Restrictions.and(
                                        Restrictions.or(Restrictions.eq("receiverGCN.userGCN", gcn),Restrictions.eq("receiver.alias", gcn)),
                                        Restrictions.or(Restrictions.eq("senderGCN.userGCN", contactGCN),Restrictions.eq("sender.alias", contactGCN)))),
                                (Restrictions.and(
                                        Restrictions.or(Restrictions.eq("senderGCN.userGCN", gcn),Restrictions.eq("sender.alias", gcn)),
                                        Restrictions.or(Restrictions.eq("receiverGCN.userGCN", contactGCN),Restrictions.eq("receiver.alias", contactGCN))))))
                        .list();

            }

            if (oList != null) {
                for (Object msgObject : oList) {
                    Message msg = (Message) msgObject;
                    if(msg.getReceiverGCN() !=null){
                        if(msg.getReceiverGCN().getUserGCN() != null){
                            msg.setReceiverGCN((userRepository.get(msg.getReceiverGCN().getUserGCN())));
                        }
                    }
                    if(msg.getSenderGCN() !=null){
                        if(msg.getSenderGCN().getUserGCN() != null){
                            msg.setSenderGCN(userRepository.get(msg.getSenderGCN().getUserGCN()));
                        }
                    }
                    if(!msg.isMsgRecvdFlag()){
                        msg.setMsgRecvdFlag(true);
                        this.update(msg);
                    }
                    if(msg.getSenderGCN() != null && msg.getReceiverGCN() != null){
                        if(isSenderNotBlocked(msg.getSenderGCN().getUserGCN(), msg.getReceiverGCN().getUserGCN())){
                            messages.add(msg);
                        }
                    }
                }
                return messages;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Date getLastConnectedTime(String gcn, String contactGCN) {
        try {
            Object o = getSession().createCriteria(Message.class)
                    .createAlias("receiverGCN", "receiver")
                    .createAlias("senderGCN", "sender")
                    .add(
                            Restrictions.or(
                                    Restrictions.and(
                                            //Restrictions.eq("receiverGCN.userGCN", gcn),
                                            //Restrictions.eq("senderGCN.userGCN", contactGCN)
                                            Restrictions.or(Restrictions.eq("receiverGCN.userGCN", gcn),Restrictions.eq("receiver.userGCN", gcn)),
                                            Restrictions.or(Restrictions.eq("senderGCN.userGCN", contactGCN),Restrictions.eq("sender.userGCN", contactGCN))
                                    ),
                                    Restrictions.and(
                                            //Restrictions.eq("receiverGCN.userGCN", contactGCN),
                                            //Restrictions.eq("senderGCN.userGCN", gcn)
                                            Restrictions.or(Restrictions.eq("receiverGCN.userGCN", contactGCN),Restrictions.eq("receiver.alias", contactGCN)),
                                            Restrictions.or(Restrictions.eq("senderGCN.userGCN", gcn),Restrictions.eq("sender.alias", gcn))
                                    )
                            )
                    )
                    .addOrder(Order.desc("creationDate"))
                    .setMaxResults(1)
                    .uniqueResult();
            if (o != null) {
                return ((Message) o).getCreationDate();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getEligibleMessagesForPN(String duration) {
        LinkedList<Message> messages = new LinkedList<Message>();
        try {
            List<Object> oList;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -(Integer.parseInt(duration)/60000));
            oList =  getSession().createCriteria(Message.class)
                    .add(Restrictions.eq("msgRecvdFlag", false))
                    .add(Restrictions.ge("creationDate", calendar.getTime()))
                    .list();

            if (oList != null) {
                for (Object msgObject : oList) {
                    Message msg = (Message) msgObject;
                    if(msg.getReceiverGCN() !=null){
                        if(msg.getReceiverGCN().getUserGCN() != null){
                            msg.setReceiverGCN((userRepository.get(msg.getReceiverGCN().getUserGCN())));
                        }
                    }
                    if(msg.getSenderGCN() !=null){
                        if(msg.getSenderGCN().getUserGCN() != null){
                            msg.setSenderGCN(userRepository.get(msg.getSenderGCN().getUserGCN()));
                        }
                    }
                    if(msg.getSenderGCN() != null && msg.getReceiverGCN() != null){
                        if(isSenderNotBlocked(msg.getSenderGCN().getUserGCN(), msg.getReceiverGCN().getUserGCN())){
                            messages.add(msg);
                        }
                    }

                }
                return messages;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private boolean isSenderNotBlocked(String senderGCN, String receiverGCN){
        if(inviteRepository.getSubscriptionStatus(senderGCN, receiverGCN) == 4)
            return false;
        else
            return true;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    InviteRepository inviteRepository;

    public InviteRepository getInviteRepository() {
        return inviteRepository;
    }

    public void setInviteRepository(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }
}