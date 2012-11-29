/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/** @author Navin Raj Kumar G.S. */
@Table(name = "t_curation_source_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurationSourceProperty extends EntityPropertyModel {

}
