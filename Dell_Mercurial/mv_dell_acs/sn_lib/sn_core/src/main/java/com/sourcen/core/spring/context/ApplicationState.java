/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.spring.context;


import com.sourcen.core.InvalidArgumentException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2766 $, $Date:: 2012-05-30 02:19:10#$
 */
public enum ApplicationState {

    INITIALIZING(0, "INITIALIZING"),
    INITIALIZED(1, "INITIALIZED"),
    TERMINATING(-1, "TERMINATING"),
    TERMINATED(-10, "TERMINATED"),

    // these are application startup states.
    SETUP(1000, "SETUP"),
    UPGRADE(2000, "UPGRADE"),
    COMPLETE(3000, "COMPLETE"),

    // all errors above 10,000
    SETUP_ERROR(11000, "SETUP_ERROR"),
    UPGRADE_INCOMPLETE(12001, "UPGRADE_INCOMPLETE"),
    UPGRADE_ERROR(12000, "UPGRADE_ERROR"),
    APPLICATION_ERROR(13000, "APPLICATION_ERROR");

    public static final String APPLICATION_STATE_KEY = "application.state";
    public static final Integer ERROR_INDEX = 10000;
    public static final Integer SAVED_STATE_INDEX = 1000;
    private Integer id;
    private String name;
    private Boolean errorState = false;

    ApplicationState(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public Boolean isErrorState() {
        return (this.id >= ERROR_INDEX);
    }
    public Boolean canSaveState() {
        return (this.id >= SAVED_STATE_INDEX);
    }

    public static ApplicationState get(Integer id) {
        for (ApplicationState state : ApplicationState.values()) {
            if (state.getId().equals(id)) {
                return state;
            }
        }
        throw new InvalidArgumentException("id", id);
    }

    public static ApplicationState get(String name) {
        for (ApplicationState state : ApplicationState.values()) {
            if (state.getName().equalsIgnoreCase(name)) {
                return state;
            }
        }
        throw new InvalidArgumentException("name", name);
    }


}
