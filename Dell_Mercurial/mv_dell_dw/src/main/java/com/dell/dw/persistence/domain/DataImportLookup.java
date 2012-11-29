/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.dw.persistence.domain;


import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 14:56:09#$
 */
@NamedQueries({
        @NamedQuery(
                name="DataImportLookup.deleteRecord",
                query = "delete from DataImportLookup " +
                        "where srcTableId = :srcTableId and destTableName = :destTableName"
        )
})
@Entity
@Table(name = "data_import_lookup")
public class DataImportLookup extends com.sourcen.dataimport.service.support.hibernate.DataImportLookup {

}
