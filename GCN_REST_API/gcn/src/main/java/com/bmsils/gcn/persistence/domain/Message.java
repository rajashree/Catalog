/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import org.apache.commons.collections.list.AbstractLinkedList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 12:51 PM
 * Message is a domain object for "addresses" table
 */
@Table(name="messages")
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderGcn", nullable = false, referencedColumnName = "userGCN")
    private User senderGCN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverGcn", nullable = false,  referencedColumnName = "userGCN")
    private User receiverGCN;

    @Column(nullable = false)
    private String msgStanza;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate;

    @Column(nullable = true)
    private boolean msgRecvdFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSenderGCN() {
        return senderGCN;
    }

    public void setSenderGCN(User senderGCN) {
        this.senderGCN = senderGCN;
    }

    public User getReceiverGCN() {
        return receiverGCN;
    }

    public void setReceiverGCN(User receiverGCN) {
        this.receiverGCN = receiverGCN;
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

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderGCN=" + senderGCN +
                ", receiverGCN=" + receiverGCN +
                ", msgStanza='" + msgStanza + '\'' +
                ", creationDate=" + creationDate +
                ", msgRecvdFlag=" + msgRecvdFlag +
                '}';
    }
}
