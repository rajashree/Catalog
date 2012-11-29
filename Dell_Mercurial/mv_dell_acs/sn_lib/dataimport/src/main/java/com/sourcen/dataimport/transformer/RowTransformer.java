/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1745 $, $Date:: 2012-04-18 11:58:40#$
 */

/**
 RowTransformer having the abstract functionality for transforming the rows data while operation
 */
public interface RowTransformer {

    /**
     transform() is used for the transforming the rows data values at the time of the data manupulation

     @param source table row which is in the form of the java.util.Map object having the source row data
     @return table row in the form of the javal.util.Map object having the transformed row values
     @throws SQLException is thorns by this method
     */
    public Map<String, Object> transform(Map<String, Object> source) throws SQLException;

}
