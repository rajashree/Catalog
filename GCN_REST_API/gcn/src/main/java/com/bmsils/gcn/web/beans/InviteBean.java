/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.beans;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/20/12
 * Time: 3:28 PM
 * InviteBean is a UI bean object being used to map Invite Entity
 */
public class InviteBean {
    private BasicProfileDataBean inviter;
    private BasicProfileDataBean invitee;
    private boolean fromQRFlag;

    public BasicProfileDataBean getInviter() {
        return inviter;
    }

    public void setInviter(BasicProfileDataBean inviter) {
        this.inviter = inviter;
    }

    public BasicProfileDataBean getInvitee() {
        return invitee;
    }

    public void setInvitee(BasicProfileDataBean invitee) {
        this.invitee = invitee;
    }

    public boolean isFromQRFlag() {
        return fromQRFlag;
    }

    public void setFromQRFlag(boolean fromQRFlag) {
        this.fromQRFlag = fromQRFlag;
    }
}
