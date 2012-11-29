/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;


import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 533 $, $Date:: 2012-03-07 05:28:35#$
 * @since 1.0
 */
public class ObjectUtils {

    private static final int hashCodeSalt = BigInteger.probablePrime(8, new Random(new Double(Math.random()).longValue())).intValue();

    public static int hashCode(Object o) {
        return HashCodeBuilder.reflectionHashCode(o) * hashCodeSalt;
    }

    public static int hashCodeSalt() {
        return hashCodeSalt;
    }

    public static int getHashCodeSalt() {
        return hashCodeSalt;
    }

    // return hashcode for a combination of fields.
    public static int hashCode(Object... objects) {
        int hashCode = hashCodeSalt;
        for (Object o : objects) {
            if (o != null) {
                hashCode *= o.hashCode();
            }
        }
        return hashCode;
    }

    public static String toString(Object o) {
        return ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE, false, o.getClass());
    }


    public static <T> boolean equals(T o1, T o2) {
        return (o1.getClass().equals(o2.getClass()) && o1.hashCode() == o2.hashCode());
    }
}
