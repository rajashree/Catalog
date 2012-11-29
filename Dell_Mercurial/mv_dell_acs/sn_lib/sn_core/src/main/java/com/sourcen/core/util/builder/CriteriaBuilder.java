package com.sourcen.core.util.builder;

import com.sourcen.core.util.beans.ServiceFilterBean;
import org.hibernate.Criteria;

import javax.servlet.http.HttpServletRequest;

/**
 * User: vivek
 * Date: 7/23/12
 * Time: 7:08 PM
 */
public interface CriteriaBuilder {

    Criteria buildCriteria(Criteria criteria, ServiceFilterBean filterBean);

    Criteria buildCriteria(Criteria criteria, HttpServletRequest request);

    String getBuilderName();
}
