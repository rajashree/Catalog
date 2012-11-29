package com.dell.acs.persistence.domain;

import com.sourcen.core.persistence.domain.impl.jpa.EntityPropertyModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 * @version $Revision: 3707 $, $Date:: 2012-07-13 2:54 PM#$
 */
@Deprecated
@Table(name = "t_article_properties")
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArticleProperty extends EntityPropertyModel {
}
