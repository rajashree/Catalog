/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 5/8/12
 * Time: 5:24 PM
 * UserProperty is a domain object for "addresses" table
 */
@Table(name = "user_property")
@Entity
public class UserProperty implements Serializable {

    @Id
    @Column(nullable = false)
    private String name;

    @Id
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userGCN", nullable = false)
    private User userGCN;

    @Column(nullable = true)
    private String value;

    public User getUserGCN() {
        return userGCN;
    }

    public void setUserGCN(User userGCN) {
        this.userGCN = userGCN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
