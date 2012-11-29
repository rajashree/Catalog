/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.notification;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/29/12
 * Time: 5:23 PM
 * An extension to EmailNotification
 */
public class EmailAddressNotification extends AbstractNotification implements Serializable {

    private static final long serialVersionUID = 2351425691786526136L;


    private String email;

    /**
     * Constructor for serialization use only.
     */
    public EmailAddressNotification() {
    }

    public EmailAddressNotification(String name, String coupon, String email, long shareId) {

        super.setName(name);
        super.setCoupon(coupon);
        super.setShareId(shareId);
        this.email = email;
    }

    public EmailAddressNotification(String name, String coupon, String email, long shareId, String status) {

        super.setName(name);
        super.setCoupon(coupon);
        super.setShareId(shareId);
        super.setStatus(status);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("EmailAddressNotification");
        sb.append("{email='").append(email).append('\'');
        sb.append("{name='").append(super.getName()).append('\'');
        sb.append("{coupon='").append(super.getCoupon()).append('\'');
        sb.append("{shareId='").append(super.getShareId()).append('\'');
        sb.append("{status='").append(super.getStatus()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailAddressNotification that = (EmailAddressNotification) o;

        return !(email != null ? !email.equals(that.email) : that.email != null);

    }

    public int hashCode() {
        int result = 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
