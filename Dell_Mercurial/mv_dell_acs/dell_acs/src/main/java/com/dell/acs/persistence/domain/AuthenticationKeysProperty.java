package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * User: vivek
 * Date: 7/2/12
 * Time: 3:07 PM
 *
 */
@Table(name = "t_auth_keys_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AuthenticationKeysProperty extends EntityPropertyModel {

}
