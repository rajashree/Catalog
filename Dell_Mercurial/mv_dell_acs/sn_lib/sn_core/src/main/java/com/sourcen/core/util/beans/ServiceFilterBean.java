package com.sourcen.core.util.beans;

/**
 * A value object which is used in the v2 REST endpoints.
 * This acts as a default holder for various common parameters -
 * start, pageSize, orderBy, searchFields, q, filterBy, scope -
 * across different Service endpoints on the HttpRequest.
 *
 * Helps us define common request parameters across services.
 *
 * The end user can easily over-ride any of the default value, by
 * passing the parameter & value like any other HttpRequest param.
 *
 * For Ex: An API request on this URL http://iads.marketvine.com:8080/api/v2/rest/ProductService/getPagedProductReviews.json?productId=1
 * The response would have 10 reviews, as the 'pageSize=10', if there are 10 reviews present on the content server.
 *
 * However, the end user can over-ride the default values on - start, pageSize, etc - by passing these parameters explicitly on the REQUEST.
 *
 * http://iads.marketvine.com:8080/api/v2/rest/ProductService/getPagedProductReviews.json?productId=1&start=0&pageSize=5
 *
 * The following API call gives an error "ResultSet may only be accessed in a forward direction." currently
 * http://iads.marketvine.com:8080/api/v2/rest/ProductService/getPagedProductReviews.json?productId=1&start=1&pageSize=5
 *
 * Here is the reference article from JTDS documentation - http://jtds.sourceforge.net/faq.html#forwardOnlyResultSet
 *
 * The paginated issue has been resolved by using 2008 SQLServer dialect.
 *
 *
 * User: vivek
 * Date: 7/23/12
 * Time: 3:27 PM
 *
 */
public class ServiceFilterBean {

    private int start = 0;
    private int pageSize = 10;
    private String orderBy = "id-asc";
    private String searchFields = "";
    private String q = "";
    private String filterBy = "";
    private String scope = "standard";


    public String getQ() {
        return q;
    }


    public void setQ(String q) {
        this.q = q;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }
}
