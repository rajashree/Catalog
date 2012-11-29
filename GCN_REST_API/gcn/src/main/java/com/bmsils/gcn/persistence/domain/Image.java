/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/24/12
 * Time: 5:43 PM
 * Image is a domain object for "addresses" table
 */
@Table(name = "images")
@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "userGCN", nullable = false)
    private User user;

    @Column(nullable = true)
    private boolean isDefault;

    @Lob
    @Column(nullable = true, columnDefinition = "BLOB")
    public byte[] image;

    public Image() {
    }

    public Image(User user, boolean isDefault, byte[] image) {
        this.user = user;
        this.isDefault = isDefault;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}




