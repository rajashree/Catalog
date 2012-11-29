/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.util;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 1746 $, $Date:: 2012-04-18 11:59:01#$
 */

/**
 DataTypeUtil is the utility class for getting the Data type of the provided argument objects
 */
public class DataTypeUtil {


    public static Long getLong(Object obj) {
        Long result = -1L;
        if (obj instanceof Integer) {
            result = ((Integer) obj).longValue();
        } else if (obj instanceof Long) {
            result = (Long) obj;
        } else if (obj instanceof Double) {
            result = ((Double) obj).longValue();
        } else {
            result = new Double(Double.parseDouble(obj.toString())).longValue();
        }
        return result;
    }

}
