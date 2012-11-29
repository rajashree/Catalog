package com.dell.acs.managers.model;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/20/12 4:30 PM#$ */
public class UserRoleData implements FormData{

    private Long id;
    private Long version;
    private String name;

    public UserRoleData() {
    }

    public UserRoleData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
