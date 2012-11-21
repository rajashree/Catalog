/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.File;
import java.io.FilePermission;
import java.security.AccessController;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FilePermissionEx {
    public static void main(String[] args) {
        FilePermission fp = new FilePermission(new File("C:\\1.txt").getPath(), "read");

        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(fp);
        }


        AccessController.checkPermission(fp);
    }
}
