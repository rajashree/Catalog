package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Adarsh
 * Date: 7/18/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "t_curation_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurationProperty extends EntityPropertyModel {

}
