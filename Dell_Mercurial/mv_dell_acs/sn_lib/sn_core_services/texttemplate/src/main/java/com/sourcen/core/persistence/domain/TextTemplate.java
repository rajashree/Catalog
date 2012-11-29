/*
 * Copyright(C) 2011, Sourcen Framework and the original authors.
 * All Rights Reserved. Use is subject to license terms and conditions.
 */


package com.sourcen.core.persistence.domain;


import com.sourcen.core.persistence.domain.constructs.Dated;
import com.sourcen.core.persistence.domain.constructs.Named;
import com.sourcen.core.persistence.domain.constructs.ParentAware;
import com.sourcen.core.persistence.domain.constructs.Typed;

public interface TextTemplate extends Entity, Named, Dated, Typed<Integer>, ParentAware<String> {

    String getLocale();

    void setLocale(String locale);

    String getMessage();

    void setMessage(String message);


}
