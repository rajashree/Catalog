/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/20/12
 * Time: 3:42 PM
 * MessageBean is a UI bean object being used to map Message Entity
 */
public class MessageBean {
    private BasicProfileDataBean receiver;
    private BasicProfileDataBean sender;
    private String msgStanza;
    private Date creationDate;
    private boolean msgRecvdFlag;

    public BasicProfileDataBean getReceiver() {
        return receiver;
    }

    public void setReceiver(BasicProfileDataBean receiver) {
        this.receiver = receiver;
    }

    public BasicProfileDataBean getSender() {
        return sender;
    }

    public void setSender(BasicProfileDataBean sender) {
        this.sender = sender;
    }

    public String getMsgStanza() {
        return msgStanza;
    }

    public void setMsgStanza(String msgStanza) {
        this.msgStanza = msgStanza;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isMsgRecvdFlag() {
        return msgRecvdFlag;
    }

    public void setMsgRecvdFlag(boolean msgRecvdFlag) {
        this.msgRecvdFlag = msgRecvdFlag;
    }
}
