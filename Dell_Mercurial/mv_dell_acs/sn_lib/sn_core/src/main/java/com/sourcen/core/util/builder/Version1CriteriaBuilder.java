package com.sourcen.core.util.builder;

import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * This could be used for building hibernate Criteria generically.
 * TODO: Implement support for /api/v1/rest
 * User: vivek
 *
 */
public class Version1CriteriaBuilder implements CriteriaBuilder {

    public static final Logger logger = Logger.getLogger(Version1CriteriaBuilder.class);

    @Override
    public Criteria buildCriteria(Criteria criteria, HttpServletRequest request) {
        return criteria;
    }

    @Override
    public Criteria buildCriteria(Criteria criteria, ServiceFilterBean filterBean) {
        return  criteria;
    }

    /**
     *
     * @return
     */
    public String getBuilderName(){
        return Version1CriteriaBuilder.class.getName();
    }


}
