/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.persistence.domain;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2929 $, $Date:: 2012-06-05 23:25:24#$
 */
//sfisk - CS-380
@Entity
@Table(name = "t_data_import_lookup")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataImportLookup extends com.sourcen.dataimport.service.support.hibernate.DataImportLookup {

}
