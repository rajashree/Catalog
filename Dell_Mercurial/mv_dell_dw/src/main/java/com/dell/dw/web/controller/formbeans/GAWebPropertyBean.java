package com.dell.dw.web.controller.formbeans;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 6/15/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAWebPropertyBean {
    private Long accountId;
    private String accountName;
    private String webPropertyId;
    private Long profileId;
    private String profileName;
    private Boolean active;
    private Date initializationDate;
    private String timezone;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getWebPropertyId() {
        return webPropertyId;
    }

    public void setWebPropertyId(String webPropertyId) {
        this.webPropertyId = webPropertyId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public void setInitializationDate(Date initializationDate) {
        this.initializationDate = initializationDate;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
