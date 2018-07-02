/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/5/12
 * Time: 12:57 PM
 * Invite is a domain object for "addresses" table
 */

@NamedQueries(
        {
                @NamedQuery(name="getContacts",
                        query="SELECT inviterGcn.userGCN,inviteeGcn.userGCN, subscriptionStatus from Invite " +
                                "where (subscriptionStatus=3 OR subscriptionStatus=4) " +
                                "and (inviterGcn.userGCN = :gcn OR inviteeGcn.userGCN = :gcn OR inviterGcn.alias = :gcn OR inviteeGcn.alias = :gcn)"),

                @NamedQuery(name="getInviteRequests",
                        query="SELECT inv from Invite inv " +
                                "where subscriptionStatus=1 " +
                                "and (inviteeGcn.userGCN = :gcn OR inviteeGcn.alias = :gcn)"),

                @NamedQuery(name="getInvite",
                        query="SELECT inv from Invite inv " +
                                "where (inviterGcn.userGCN = :inviterGCN OR inviterGcn.alias = :inviterGCN) " +
                                "and (inviteeGcn.userGCN = :inviteeGCN OR inviteeGcn.alias = :inviteeGCN)")
        }
)

@Table(name="invites")
@Entity
public class Invite implements Serializable {

    @Id
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "inviterGcn", nullable = false)
    private User inviterGcn;

    @Id
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "inviteeGcn", nullable = false)
    private User inviteeGcn;

    @Column(nullable = true)
    private int subscriptionStatus;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date creationDate;

    @Column(nullable = true)
    private boolean fromQRFlag;

    public User getInviterGcn() {
        return inviterGcn;
    }

    public void setInviterGcn(User inviterGcn) {
        this.inviterGcn = inviterGcn;
    }

    public User getInviteeGcn() {
        return inviteeGcn;
    }

    public void setInviteeGcn(User inviteeGcn) {
        this.inviteeGcn = inviteeGcn;
    }

    public int getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(int subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFromQRFlag() {
        return fromQRFlag;
    }

    public void setFromQRFlag(boolean fromQRFlag) {
        this.fromQRFlag = fromQRFlag;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "inviterGcn=" + inviterGcn +
                ", inviteeGcn=" + inviteeGcn +
                ", subscriptionStatus=" + subscriptionStatus +
                ", creationDate=" + creationDate +
                ", fromQRFlag=" + fromQRFlag +
                '}';
    }
}
