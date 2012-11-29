/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.persistence.repository;

import com.bmsils.gcn.persistence.domain.ProfileVisibility;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 3:03 PM
 * ProfileVisibilityRepository contains data access methods pertaining to ProfileVisibility table related functions
 */
public interface ProfileVisibilityRepository extends Repository {
    ProfileVisibility getContactProfileDetailsVisiblity(String gcn, String contactGCN);
    void updateProfileFieldUpdates(String gcn, String profileFieldsUpdated);
    void updateProfileFieldUpdates(String gcn, String contactGCN, String profileFieldsUpdated);
    Map<String,Object> getRecentUpdates(String gcn);
}
