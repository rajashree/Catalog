/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$
 */

/**
 DataWriter specifying the abstract functionality for Writing the data
 */
public interface DataWriter extends DataAdapter {

    /**
     executeBatchUpdate() helps in the updating the data in the form of the batch

     @param batch           Collection<Map<String, Object>> batch takes collection object which have interlly
     java.util.Map<String,Object> where each row is representing in key value pair in map object
     @param batchId         which is assigne to each batch when the batch is formed
     @param recordsToInsert group of the Rows is for insertion
     @param recordsToUpdate group of the Rows is for updation
     @return the java.util.List object having the object array which have the status of the every row is sucessfully
     update or not
     */
    List<Object[]> executeBatchUpdate(Collection<Map<String, Object>> batch, String batchId,
                                      Map<Object[], Map<String, Object>> recordsToInsert,
                                      Map<Object[], Map<String, Object>> recordsToUpdate);


    /**
     processRowBeforeInsertion help in the manupulating the row data before insertion

     @param srcRecord   accept the java.util.Map<String,Object> object haing the row data in the form of the key value
     pair
     @param record
     @param recordIndex
     */
    void processRowBeforeInsertion(Map<String, Object> srcRecord, Map<String, Object> record, Integer recordIndex);


    /**
     preExecuteBatchUpdate() helps in the manupulating the data before the updating the batch

     @param batch           accetp the batch of the data in the form of the Map object
     @param batchId         accespt the batch id assigen to every batch
     @param recordsToInsert group of the data in the form of the Map object for insertion
     @param recordsToUpdate group of the data in the form of the Map object for updation
     */
    void preExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId,
                               Map<Object[], Map<String, Object>> recordsToInsert,
                               Map<Object[], Map<String, Object>> recordsToUpdate);

    /**
     @param batch           accept the batch of the data in the form of the Map object
     @param batchId         accept the batch id assign to every batch
     @param recordsToInsert group of the data in the form of the Map object for insertion
     @param recordsToUpdate group of the data in the form of the Map object for updation
     @param result          the list of the updated and inserted
     */
    void postExecuteBatchUpdate(Collection<Map<String, Object>> batch, String batchId,
                                Map<Object[], Map<String, Object>> recordsToInsert,
                                Map<Object[], Map<String, Object>> recordsToUpdate, List result);


}
