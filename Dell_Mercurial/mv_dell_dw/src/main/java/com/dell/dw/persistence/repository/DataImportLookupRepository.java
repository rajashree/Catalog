package com.dell.dw.persistence.repository;

import com.dell.dw.persistence.domain.DataImportLookup;
import com.sourcen.core.persistence.repository.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/13/12
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataImportLookupRepository extends Repository<DataImportLookup> {

    Boolean deleteRecord(String srcTableId, String destTableName);

}

