package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
// sfisk - CS-380
@Table(name = "t_coupon_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CouponProperty extends EntityPropertyModel {

}
