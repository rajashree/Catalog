package com.dell.dw.web.controller.formbeans;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 7/31/12
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppHealthBean {
    private String appName;
    private Long pageViewsToday = 0L;
    private Double avgDailyPageViews = 0.0;
    private Double loadTime = 0.0;
    private Double avgLoadTime = 0.0;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getPageViewsToday() {
        return pageViewsToday;
    }

    public void setPageViewsToday(Long pageViewsToday) {
        this.pageViewsToday = pageViewsToday;
    }

    public Double getAvgDailyPageViews() {
        return avgDailyPageViews;
    }

    public void setAvgDailyPageViews(Double avgDailyPageViews) {
        this.avgDailyPageViews = avgDailyPageViews;
    }

    public Double getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Double loadTime) {
        this.loadTime = loadTime;
    }

    public Double getAvgLoadTime() {
        return avgLoadTime;
    }

    public void setAvgLoadTime(Double avgLoadTime) {
        this.avgLoadTime = avgLoadTime;
    }
}
