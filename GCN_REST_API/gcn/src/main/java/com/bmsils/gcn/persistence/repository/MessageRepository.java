/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.domain.User;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/12/12
 * Time: 7:16 PM
 * MessageRepository contains data access methods pertaining to Message table related functions
 */
public interface MessageRepository extends Repository{
    public List<Message> getMessages(String gcn, boolean active);

    List<Message> getMessageHistory(String gcn, String contactGCN, Date fromDate);

    Date getLastConnectedTime(String gcn, String contactGCN);

    List<Message> getEligibleMessagesForPN(String duration);
}
