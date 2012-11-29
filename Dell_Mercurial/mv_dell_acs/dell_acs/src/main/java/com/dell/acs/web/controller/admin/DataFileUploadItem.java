/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 572 $, $Date:: 2012-03-08 10:26:09#$
 */
public class DataFileUploadItem {

    private Long retailerSiteId;

    private String fileType;

    private CommonsMultipartFile dataFile;

    public Long getRetailerSiteId() {
        return retailerSiteId;
    }

    public void setRetailerSiteId(Long retailerSiteId) {
        this.retailerSiteId = retailerSiteId;
    }

    public CommonsMultipartFile getDataFile() {
        return dataFile;
    }

    public void setDataFile(CommonsMultipartFile dataFile) {
        this.dataFile = dataFile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
