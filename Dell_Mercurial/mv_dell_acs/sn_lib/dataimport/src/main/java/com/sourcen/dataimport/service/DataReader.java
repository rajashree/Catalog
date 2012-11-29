/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service;

import java.util.Collection;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$
 */

/**
 * DataReader spcefying the functionality of the Data Reading
 */
public interface DataReader extends DataAdapter {

    /**
     * getRows() helps in fetching the rows of the files or database
     *
     * @return Collection<Map<String, Object>> having the data of the csv each row in the csv file is repersenting by
     *         java.util.Map<String,Object> in key value pair and store in the Colleciton object
     */
    Collection<Map<String, Object>> getRows();

//    int getRowCount();


    /**
     * @param record
     * @param recordIndex
     */
    void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex);

}