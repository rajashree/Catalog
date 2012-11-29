/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.Invite;
import com.bmsils.gcn.persistence.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 1:13 PM
 * InviteRepository contains data access methods pertaining to Invite table related functions
 */
public interface InviteRepository extends Repository{

    Invite get(String inviterGCN, String inviteeGCN);

    List<Invite> getInvites(String gcn);

    Collection<User> getContacts(String gcn);

 //   Collection<User> getRecentUpdatedContacts(String gcn);

    List<Invite> getEligibleInvitesForPN(String duration);

    void blockContact(String gcn, String contactGCN);

    void unBlockContact(String gcn, String contactGCN);

    int getSubscriptionStatus(String senderGCN, String receiverGCN);
}