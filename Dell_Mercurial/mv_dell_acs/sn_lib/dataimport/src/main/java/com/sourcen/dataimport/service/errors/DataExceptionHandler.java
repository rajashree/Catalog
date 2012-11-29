/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.errors;

import org.slf4j.Logger;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: navinr $
 @version $Revision: 2651 $, $Date:: 2012-05-28 08:27:49#$ */
public interface DataExceptionHandler {

    /* Table definition Related Exception
   * {
   *   The Exception Generated at the time of the
   *  Defining the Table structure for the Data which we are reading
   * for the Different sources and Exception Related to that process
   * are listed below
   * }
   * */

    /**
     This Method is for Exception Generated on the Table Definition class and provide help to capture the exception and
     do custom action on the Table Definition Exception

     @param e Exception or child Class of the Exception
     */
    void onTableDefinitionException(Exception e);

    /**
     This Method is for Exception Generated on the Table Definition class and provide help to capture the exception and
     do custom action on the Table Definition Exception

     @param e      Exception or child Class of the Exception
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onTableDefinitionException(Exception e, boolean bubble);

    /**
     This Method is for Exception Generated on the Table Definition class and provide help to capture the exception and
     do custom action on the Table Definition Exception

     @param e is for RuntimeException and Child of the RuntimeException
     */
    void onTableDefinitionException(RuntimeException e);

    /**
     This Method is for Exception Generated on the Table Definition class and provide help to capture the exception and
     do custom action on the Table Definition Exception

     @param e      is for RuntimeException and Child of the RuntimeException
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onTableDefinitionException(RuntimeException e, boolean bubble);


    /* Reader  Related Exception
            {
             Exception Generated at the Time of Reading the Data form the
             Files ,DataBases and Other sources these method help in trapping and
             performing custom action on the generation of the Exception during the
             operation is in progress
             }
    */

    /**
     This Method is for the Exception Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action

     @param e is the Exception object and its child Class is allowed
     */
    void onDataReaderException(Exception e);

    /**
     This Method is for the Excpetion Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action

     @param e      is the Exception object and its child Class is allowed
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataReaderException(Exception e, boolean bubble);

    /**
     This Method is for the Exception Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action

     @param e is for RuntimeException and Child of the RuntimeException
     */
    void onDataReaderException(RuntimeException e);

    /**
     This Method is for the Exception Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action

     @param e      is for RuntimeException and Child of the RuntimeException
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataReaderException(RuntimeException e, boolean bubble);

    /**
     This Method is for the Exception Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action and Allow to pass Custom Exception object which has
     custom data

     @param e DataReaderException object custom Exception object
     */
    void onDataReaderException(DataReaderException e);

    /**
     This Method is for the Exception Generated in the Reader Class While reading the Data from file Databases and other
     sources trap the exception generated and do the custom action and Allow to pass Custom Exception object which has
     custom data

     @param e      DataReaderException object custom Exception object
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataReaderException(DataReaderException e, boolean bubble);


    /* Writer Related Exception
            {
              Exception Generated at the Time of Writing the Data to the
              Files ,DataBases and Other sources these method help in trapping and
              performing custom action on the generation of the Exception during the
              operation is in progress
            }
    */

    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e is the Exception object and its child Class is allowed
     */
    void onDataWriterException(Exception e);

    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e      is the Exception object and its child Class is allowed
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataWriterException(Exception e, boolean bubble);


    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e is for RuntimeException and Child of the RuntimeException
     */
    void onDataWriterException(RuntimeException e);

    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e      is for RuntimeException and Child of the RuntimeException
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataWriterException(RuntimeException e, boolean bubble);


    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e DataWriterException object custom Exception object
     */
    void onDataWriterException(DataWriterException e);

    /**
     This method is helpful in trapping the Exception at the time of Writing the Data to Different sources and allow the
     custom action to be performed on the generation of the exception

     @param e      DataWriterException object custom Exception object
     @param bubble is for propagation of the Exception to next level of the caller method
     */
    void onDataWriterException(DataWriterException e, boolean bubble);

    void setLogger(Logger logger);

    Integer getReaderFailedCount();
    Integer getWriterFailedCount();

}
