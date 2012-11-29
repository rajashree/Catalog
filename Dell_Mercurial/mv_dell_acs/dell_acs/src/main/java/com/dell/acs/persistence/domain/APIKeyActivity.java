/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.IdentifiableEntityModel;
import com.sourcen.core.util.beans.Scope;
import com.sourcen.core.util.beans.Scopes;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class for "t_apikey_activity".
 *
 * @author Sandeep Heggi
 * @author $lastChangedBy : sandeep
 */

@Table(name = "t_apikey_activity")
@org.hibernate.annotations.Table(appliesTo = "t_apikey_activity",
        indexes = {@Index(name = "IX_Activity_APIKey", columnNames = "apiKey"),
                @Index(name = "IX_Activity_Username", columnNames = "username")})

@Entity
@Scopes({

        @Scope(name = "default", fields = {"id", "apiKey", "IPAddress", "requestURL", "username"}),

        @Scope(name = "minimal", fields = {"id", "apiKey", "IPAddress"})
})
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class APIKeyActivity extends IdentifiableEntityModel<Long> {

    @Column(nullable = false, length = 64)
    private String apiKey;

    @Column(nullable = false)
    private String IPAddress;

    @Column(nullable = false, columnDefinition = "nvarchar(max)")
    private String requestURL;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date accessedTime;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getAccessedTime() {
        return accessedTime;
    }

    public void setAccessedTime(Date accessedTime) {
        this.accessedTime = accessedTime;
    }
}
