/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.service.support.hibernate;

import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1877 $, $Date:: 2012-04-20 21:41:17#$
 */

/**
 BeanProcessor is the utility interface which help the implementing Bean class to trap the state before inserting the
 values to the Bean and before the bean values is persist to the database
 */
public interface BeanProcessor {

    /**
     @param clazz is the Class type args which takes the current beans as the args and enable the functionality of
     trapping its state
     @return true if the current bean is configured for state capturing or false if bean is not register for state
     capturing
     */
    boolean supportsBean(Class clazz);

    /**
     @param row accept the java.util.Map<String,Object> object which is having the data representing the one row of the
     file or database
     @return it return's the ava.util.Map<String,Object> object after the processing or manipulating the row data like
     changing the values like data format or some values which is dependent on the other table like reference
     keys or encryption of the data all necessary steps which is required before the data persists
     */
    Map<String, Object> preProcessBeanValues(Map<String, Object> row);


    /**
     @param bean object which is used for storing the row data and that bean object is having the data of the row after
     transformation
     @param row  accept the java.util.Map<String,Object> object which is having the data representing the one row of the
     file or database
     @return bean object which is now ready for the persistto databse
     */
    Object preProcessBeforePersist(Object bean, Map<String, Object> row);

    Object postProcessAfterPersist(Object bean, Map<String,Object> row);
}
