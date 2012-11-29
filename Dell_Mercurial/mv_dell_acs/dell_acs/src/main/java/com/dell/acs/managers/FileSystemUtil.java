package com.dell.acs.managers;

import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.util.FileSystem;

/**
 * Created by IntelliJ IDEA.
 * User: Adarsh
 * Date: 3/28/12
 * Time: 7:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemUtil {

    public static String getPath(RetailerSite retailerSite, String dir) {
        return FileSystem.getSimpleFilename("/" + dir + "/" + retailerSite.getRetailer().getId()
                + "_" + retailerSite.getRetailer().getName().toLowerCase()
                + "/" + retailerSite.getId() + "_" + retailerSite.getSiteName().toLowerCase()) + "/";
    }

    public static String getConfigDir(RetailerSite retailerSite) {
        return FileSystem.getSimpleFilename("/config/retailers/" + retailerSite.getRetailer().getName().toLowerCase()
                + "/" + retailerSite.getSiteName().toLowerCase()) + "/";
    }

    public static String getDataExportDir(RetailerSite retailerSite) {
        return FileSystem.getSimpleFilename("/data_export/" + retailerSite.getRetailer().getName().toLowerCase()
                + "/" + retailerSite.getSiteName().toLowerCase()) + "/";
    }



}
