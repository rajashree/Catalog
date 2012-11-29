package com.sourcen.core.util.builder;

import javax.transaction.NotSupportedException;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 7/23/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class RESTCriteriaBuilder {

    public static RESTCriteriaBuilder instance;

    static {
        instance = new RESTCriteriaBuilder();
    }

    public static synchronized CriteriaBuilder getInstance(String requestURI) throws NotSupportedException {
        if (requestURI.indexOf("v1") != -1) {
            return new Version1CriteriaBuilder();
        } else if (requestURI.indexOf("v2") != -1) {
            return new Version2CriteriaBuilder();
        }

        throw new NotSupportedException("The REST version " + requestURI + " not supported");

    }


}
