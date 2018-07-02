/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.managers.ConfigurationManager;
import com.bmsils.gcn.managers.MessageManager;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.MessageRepository;
import com.bmsils.gcn.persistence.repository.impl.MessageRepositoryImpl;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.web.beans.BasicProfileDataBean;
import com.bmsils.gcn.web.beans.MessageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/12/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MessageManagerImpl implements MessageManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);

    
    @Transactional
    public void sendMessage(User gcn, User contactGCN, String msg) {
        Message message = new Message();
        message.setCreationDate(new Date());
        message.setMsgRecvdFlag(false);
        message.setMsgStanza(msg);
        message.setReceiverGCN(contactGCN);
        message.setSenderGCN(gcn);
        messageRepository.put(message);
       // userManager.updateUser(gcn);
    }

    
    @Transactional
    public List<MessageBean> getMessages(String gcn, boolean active) {
        List<MessageBean> messageBeanList = new ArrayList<MessageBean>();
        List<Message> messages = messageRepository.getMessages(gcn, active);
        for(Message message : messages){
            MessageBean messageBean = new MessageBean();
            messageBean.setCreationDate(message.getCreationDate());
            messageBean.setMsgStanza(message.getMsgStanza());
            messageBean.setMsgRecvdFlag(message.isMsgRecvdFlag());
            messageBean.setReceiver(EntityToBeanMapper.getInstance().doMapping(message.getReceiverGCN(), BasicProfileDataBean.class));
            messageBean.setSender(EntityToBeanMapper.getInstance().doMapping(message.getSenderGCN(), BasicProfileDataBean.class));
            messageBeanList.add(messageBean);
        }
        return messageBeanList;
    }

    
    @Transactional
    public List<MessageBean> getMessageHistory(String gcn, String contactGCN, Date fromDate) {
        List<MessageBean> messageBeanList = new ArrayList<MessageBean>();
        List<Message> messages = messageRepository.getMessageHistory(gcn,contactGCN,fromDate);
        for(Message message : messages){
            MessageBean messageBean = new MessageBean();
            messageBean.setCreationDate(message.getCreationDate());
            messageBean.setMsgStanza(message.getMsgStanza());
            messageBean.setMsgRecvdFlag(message.isMsgRecvdFlag());
            messageBean.setReceiver(EntityToBeanMapper.getInstance().doMapping(message.getReceiverGCN(), BasicProfileDataBean.class));
            messageBean.setSender(EntityToBeanMapper.getInstance().doMapping(message.getSenderGCN(), BasicProfileDataBean.class));
            messageBeanList.add(messageBean);
        }
        return messageBeanList;
    }

    
    @Transactional
    public List<Message> getEligibleMessagesForPN() {
        return messageRepository.getEligibleMessagesForPN(configurationManager.getProperty("pushNotification.message.duration"));
    }

    @Autowired
    MessageRepository messageRepository;

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    ConfigurationManager configurationManager;

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
}
