/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.persistence.domain;


public interface SystemProperty extends IdentifiableEntity<String> {

    String getValue();

    void setValue(String value);

}
