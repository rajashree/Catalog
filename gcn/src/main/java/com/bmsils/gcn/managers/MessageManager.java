/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.web.beans.MessageBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/12/12
 * Time: 7:12 PM
 * Service to handle Messages related function calls
 */
@Service
public interface MessageManager extends Manager{
    /**
     * send a message to a contact
     * @param gcn
     * @param contactGCN
     * @param message
     */
    void sendMessage(User gcn, User contactGCN, String message);

    /**
     * get messages (from active contacts (or) from all contacts)
     * @param gcn
     * @param active
     * @return
     */
    List<MessageBean> getMessages(String gcn, boolean active);

    /**
     * get message history based on the duration
     * @param gcn
     * @param contactGCN
     * @param fromDate
     * @return
     */
    List<MessageBean> getMessageHistory(String gcn, String contactGCN, Date fromDate);

    /**
     * get eligible messages for push notifications
     * @return
     */
    List<Message> getEligibleMessagesForPN();
}
