/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service;

import java.util.List;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1734 $, $Date:: 2012-04-18 11:53:09#$
 */

/**
 DataImportLookupService having the abstract functionality for he lookup for the related values likes reference value in
 database tables
 */
public interface DataImportLookupService {

    /**
     getKey() enable to get the realted keys for the provided key

     @param tableName from whiere the the related reference key is to search
     @param srcKey    realted source key in the master table
     @return the related reference key for the provided source key
     */
    Long getKey(String tableName, String srcKey);

    /**
     putKeys() enable to put the group of the keys

     @param keys list of the keys to be put
     */
    void putKeys(List<Object[]> keys);

    /**
     putKeys() enable to put the group of the keys

     @param key in the form of hte object arrays
     */
    void putKey(Object[] key);


    /**
     getTableCache() getting the keys from the table catch which helps in the performance and avoid round trup to
     database for getting the reference values or keys

     @param tableName related table name for search of the forigen key
     @return the keys in the form of hte Map object
     */
    Map<String, Long> getTableCache(String tableName);
}
