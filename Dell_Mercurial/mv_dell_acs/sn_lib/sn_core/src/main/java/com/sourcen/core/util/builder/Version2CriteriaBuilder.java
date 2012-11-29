package com.sourcen.core.util.builder;

import com.sourcen.core.util.StringUtils;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: vivek
 * Date: 7/23/12
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Version2CriteriaBuilder implements CriteriaBuilder {

    private static final Logger logger = Logger.getLogger(Version2CriteriaBuilder.class);

    @Override
    public Criteria buildCriteria(Criteria criteria, ServiceFilterBean filterBean) {
        //Start
        criteria.setFirstResult(filterBean.getStart());
        //MaxResult or PageSize
        criteria.setMaxResults(filterBean.getPageSize());

        //Search & Specific Columns -
        if (StringUtils.isNotEmpty(filterBean.getQ()) && StringUtils.isNotEmpty(filterBean.getSearchFields())) {
            String[] searchColumnNames = filterBean.getSearchFields().split(",");
            LinkedList<Criterion> searchCriterions = new LinkedList<Criterion>();

            for (String searchColumn : searchColumnNames) {
                searchCriterions.add(Restrictions.ilike(searchColumn, "%" + filterBean.getQ() + "%"));
            }
            // finally create the master searchTerm criterion.
            Criterion masterSearchTermCriterion = searchCriterions.getFirst();
            for (Criterion criterion : searchCriterions) {
                masterSearchTermCriterion = Restrictions.or(masterSearchTermCriterion, criterion);
            }
            criteria.add(masterSearchTermCriterion);
        }


        if (StringUtils.isNotEmpty(filterBean.getOrderBy())) {
            // lets restrict the order by to the fields we have.
            String[] orderByParts = filterBean.getOrderBy().split("-");
            String orderBy = "";
            boolean isAsc = true;
            if (orderByParts.length > 1) {
                orderBy = orderByParts[0];
                isAsc = (orderByParts[1] != null && orderByParts[1].equalsIgnoreCase("asc"));
            }

            // NOTE: The criteria builder will not be responsible for validating the columns. We shall support
            // only pre-validated columns which the END Users are allowed to query.
            //If we need to validate the Table Column Names, then we would need to inject SessionFactory to achieve the same
            if (isAsc) {
                criteria.addOrder(Order.asc(orderBy));
            } else {
                criteria.addOrder(Order.desc(orderBy));
            }

        }

        return criteria;
    }

    @Override
    public Criteria buildCriteria(Criteria criteria, HttpServletRequest request) {

        //TODO: To be implemented

        /* if (StringUtils.isNotEmpty(request.getParameter("start"))) {
            criteria.setFirstResult(Integer.parseInt(request.getParameter("start")));
        }

        if (StringUtils.isNotEmpty(request.getParameter("pageSize"))) {
            criteria.setMaxResults(Integer.parseInt(request.getParameter("pageSize")));
        }*/


        return criteria;
    }

    @Override
    public String getBuilderName() {
        return Version2CriteriaBuilder.class.getName();
    }
}
