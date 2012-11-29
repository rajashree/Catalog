/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * The class provides methods to modify construct array based results.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2806 $, $Date:: 2012-06-01 10:40:50#$
 * @since 1.01
 */
public final class ArrayUtils extends org.apache.commons.lang.ArrayUtils {

    /**
     * the maximum depth to for the toLinearArray0 method.
     */
    private static final int MAX_DEPTH = 99;

    /**
     * creates a new ArrayUtils instance.
     */
    public ArrayUtils() {
    }


    public static int indexOf(final String[] haystack, final String needle){
        return StringUtils.indexOf(haystack ,needle);
    }

    /**
     * Returns the final converted arguments from an Object[] whose items contain Object[].
     *
     * @param args of type Object[]
     *
     * @return Object[]
     */
    public static Object[] toLinearArray(final Object[] args) {
        return ArrayUtils.toLinearArray(args, ArrayUtils.MAX_DEPTH);
    }

    /**
     * Returns the final converted arguments from an Object[] whose items contain Object[].
     *
     * @param args     of type Object[]
     * @param maxDepth of type int
     *
     * @return Object[]
     */
    public static Object[] toLinearArray(final Object[] args, final int maxDepth) {
        if (args.length > 0) {
            final List<Object> list = ArrayUtils.toLinearArray0(args, 0, maxDepth);
            final Object[] arr = new Object[list.size()];
            return list.toArray(arr);
        }
        return new Object[]{};
    }

    /**
     * helper method to convert a nested Object[] into a linear array.
     *
     * @param o            of type Object
     * @param maxDepth     of type int
     * @param currentDepth of type int
     *
     * @return List<Object> is the list of items that is converted to a linear array.
     */
    @SuppressWarnings("unchecked")
    private static List<Object> toLinearArray0(final Object o, final int currentDepth, final int maxDepth) {
        if (o.getClass().isArray()) {
            final ArrayList<Object> list = new ArrayList<Object>();
            final Object[] arr = (Object[]) o;
            for (final Object o2 : arr) {
                if (currentDepth < maxDepth) {
                    final Object res = ArrayUtils.toLinearArray0(o2, currentDepth + 1, maxDepth);
                    if (res != null) {
                        list.addAll((List<Object>) res);
                    } else {
                        list.add(o2);
                    }
                } else {
                    list.add(arr);
                }
            }
            return list;
        }
        return null;
    }



}
