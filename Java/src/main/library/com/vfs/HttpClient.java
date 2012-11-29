package com.vfs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.http.HttpFileObject;
import org.apache.commons.vfs2.provider.http.HttpFileProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 9/11/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */

public class HttpClient {
    public static void main(String[] args){
        try {
            DefaultFileSystemManager fileSystemManager =  new DefaultFileSystemManager();
            HttpFileObject httpFileObject = null;
            FileObject localFile = null;
            fileSystemManager.init();
            fileSystemManager.addProvider("http", new HttpFileProvider());
            StandardFileSystemManager obj = new StandardFileSystemManager();

            obj.init();
            httpFileObject = (HttpFileObject) obj.resolveFile("http://www.exinfm.com/excel%20files/capbudg.xls");
            File file = new File("/home/sourcen/test.xls");
            FileUtils.copyInputStreamToFile(httpFileObject.getInputStream(), file);
            localFile = obj.resolveFile(file.getAbsolutePath());
        } catch (FileSystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
