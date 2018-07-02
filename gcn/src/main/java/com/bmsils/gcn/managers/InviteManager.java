/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers;

import com.bmsils.gcn.persistence.domain.Invite;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.web.beans.InviteBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 1:15 PM
 * Service to handle Invites related function calls
 */
public interface InviteManager extends Manager{

    /**
     * invite a contact
     * @param inviter
     * @param invitee
     * @param visibleProfileFields
     */
    void inviteContact(User inviter, User invitee, List<String> visibleProfileFields);

    /**
     * get invites for an user
     * @param gcn
     * @return
     */
    public List<InviteBean> getInvites(String gcn);

    /**
     * confirm the invite request sent to an user
     * @param inviter
     * @param invitee
     * @param visibleProfileFields
     * @param confirm
     * @return
     */
    int confirmInviteRequest(String inviter, String invitee, List<String> visibleProfileFields,boolean confirm);

    /**
     * establish the contact between 2 GCN's, used in QR workflow
     * @param gcn
     * @param contactGCN
     * @param gcnVisibleProfileFields
     * @param contatGCNVisibleProfileFields
     * @param fromQR
     */
    void handleQRInvite(String gcn, String contactGCN, List<String> gcnVisibleProfileFields, List<String> contatGCNVisibleProfileFields, boolean fromQR);

    /**
     * obtain the eligible invites for Push notifications
     * @return
     */
    List<Invite> getEligibleInvitesForPN();

    /**
     * block a contact
     * @param gcn
     * @param contactGCN
     */
    void blockContact(String gcn, String contactGCN);

    /**
     * unblock a contact
     * @param gcn
     * @param contactGCN
     */
    void unBlockContact(String gcn, String contactGCN);
}
