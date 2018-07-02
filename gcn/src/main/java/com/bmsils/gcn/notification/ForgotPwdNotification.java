/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.notification;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 5/9/12
 * Time: 12:24 PM
 * ForgotPwdNotification is bean object used for forgot password email
 */
public class ForgotPwdNotification implements Serializable {

    private static final long serialVersionUID = -3918232520754145222L;
    
    private String email;
    private String gcn;
    private String username;
    private String passwordToken;


    public ForgotPwdNotification(String email, String gcn, String username, String passwordToken) {
        this.email = email;
        this.gcn = gcn;
        this.username = username;
        this.passwordToken = passwordToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcn() {
        return gcn;
    }

    public void setGcn(String gcn) {
        this.gcn = gcn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(String passwordToken) {
        this.passwordToken = passwordToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForgotPwdNotification that = (ForgotPwdNotification) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (gcn != null ? !gcn.equals(that.gcn) : that.gcn != null) return false;
        if (passwordToken != null ? !passwordToken.equals(that.passwordToken) : that.passwordToken != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (gcn != null ? gcn.hashCode() : 0);
        result = 31 * result + (passwordToken != null ? passwordToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ForgotPwdNotification{" +
                "email='" + email + '\'' +
                ", gcn='" + gcn + '\'' +
                ", passwordToken='" + passwordToken + '\'' +
                '}';
    }
}
