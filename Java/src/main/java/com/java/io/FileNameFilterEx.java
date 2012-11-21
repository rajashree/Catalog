/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

/**
 * @author : Rajashree Meganathan
 * @date : 11/15/12
 */
public class FileNameFilterEx {
    public static void main(String[] args){
        File myDir = new File("C:/");
        // Define a filter for java source files beginning with F
        FilenameFilter select = new FileListFilter("Copy", "txt");

        File[] contents = myDir.listFiles(select);

        if (contents != null) {
            System.out.println("\nThe " + contents.length
                    + " matching items in the directory, " + myDir.getName()
                    + ", are:");
            for (File file : contents) {
                System.out.println(file + " is a "
                        + (file.isDirectory() ? "directory" : "file")
                        + " last modified on\n"
                        + new Date(file.lastModified()));
            }
        } else {
            System.out.println(myDir.getName() + " is not a directory");
        }
    }

}

class FileListFilter implements FilenameFilter {
    private String name;

    private String extension;

    public FileListFilter(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    public boolean accept(File directory, String filename) {
        boolean fileOK = true;

        if (name != null) {
            fileOK &= filename.startsWith(name);
        }

        if (extension != null) {
            fileOK &= filename.endsWith('.' + extension);
        }
        return fileOK;
    }
}
