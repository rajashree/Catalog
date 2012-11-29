package com.dell.dw.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/1/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name = "lead_properties")
@Entity
public class LeadProperty extends EntityPropertyModel {
}
