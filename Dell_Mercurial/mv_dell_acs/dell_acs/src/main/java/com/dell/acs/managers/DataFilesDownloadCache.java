/**
 * 
 */
package com.dell.acs.managers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shawn_Fisk
 *
 */
public class DataFilesDownloadCache {
    private static final Map<String, Map<String, DataFilesDownloadManager.FileDownLoadStatus>> downloadFile
    = new HashMap<String, Map<String, DataFilesDownloadManager.FileDownLoadStatus>>();

    public static void setDownloadFile(String retailerSite, String name, Object object) {
        Map<String, DataFilesDownloadManager.FileDownLoadStatus> fileInformation = downloadFile.get(retailerSite);
        if (fileInformation != null) {
            if (fileInformation.size() == 100) {
                fileInformation.clear();
            }
            fileInformation.put(name, (DataFilesDownloadManager.FileDownLoadStatus) object);
        } else {
            fileInformation = new HashMap<String, DataFilesDownloadManager.FileDownLoadStatus>();
        }
        downloadFile.put(retailerSite, fileInformation);
    }

    public static Map<String, DataFilesDownloadManager.FileDownLoadStatus> getDownloadFile(String retailerSite) {
        return downloadFile.get(retailerSite);
    }

    public static void clearDownloadFile() {
        downloadFile.clear();
    }
}
