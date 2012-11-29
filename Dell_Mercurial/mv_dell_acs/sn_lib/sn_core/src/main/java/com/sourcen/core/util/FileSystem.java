/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 1356 $, $Date:: 2012-04-10 11:32:26#$
 */
public class FileSystem {

    private static final Logger logger = LoggerFactory.getLogger(FileSystem.class);

    public static FileSystem defaultFileSystem;

    static {

    }

    public static void setDefaultFileSystem(FileSystem defaultFileSystem) {
        FileSystem.defaultFileSystem = defaultFileSystem;
    }

    public static FileSystem getDefault() {
        if (defaultFileSystem == null) {
            try {
                defaultFileSystem = new FileSystem("/app_filesystem");
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return defaultFileSystem;
    }

    private String fileSystemPath = null;

    public FileSystem(String fileSystemPath) throws IOException {
        this.fileSystemPath = fileSystemPath;
        File file = getFile(null, fileSystemPath, true, true, true);
    }

    public File getFileSystem() throws IOException {
        return verifyAccessToFile(new File(getFileSystemAsString()));
    }

    public File getTempDirectory() throws IOException {
        File tempDir = new File(getFileSystem(), "temp");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return verifyAccessToFile(tempDir);
    }

    public File getFile(String childPath) throws IOException {
        return getFile(childPath, false, false, true);
    }

    public File getFile(String childPath, boolean create, boolean verify) throws IOException {
        return getFile(childPath, false, create, verify);
    }

    public File getDirectory(String childPath) throws IOException {
        return getFile(childPath, true, false, true);
    }

    public File getDirectory(String childPath, boolean create) throws IOException {
        return getFile(childPath, true, create, true);
    }
    public File getDirectory(String childPath, boolean create, boolean verify) throws IOException {
        return getFile(childPath, true, create, verify);
    }


    public File getFile(String childPath, boolean isDirectory, boolean create, boolean verify) throws IOException {
        return getFile(getFileSystem(), childPath, isDirectory, create, verify);
    }

    public File getFile(File parent, String childPath) throws IOException {
        return getFile(parent, childPath, false, true);
    }
    public File getFile(File parent, String childPath, boolean create, boolean verify) throws IOException {
        return getFile(parent, childPath, false, create, verify);
    }

    public File getFile(File parent, String childPath, boolean isDirectory, boolean create, boolean verify) throws IOException {
        File file;
        if (parent != null) {
            file = new File(parent, FilenameUtils.normalize(childPath));
        } else {
            file = new File(FilenameUtils.normalize(childPath));
        }
        if (create) {
            if (!file.exists()) {
                if (isDirectory) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
            }
        }
        if (verify) {
            return verifyAccessToFile(file);
        }
        return file;
    }

    public String getFileSystemAsString() {
        return this.fileSystemPath;
    }

    public File verifyAccessToFile(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("Unable to access filesystem :=" + file.getPath());
        }
        if (!file.canRead()) {
            throw new IOException("Unable to read filesystem :=" + file.getPath());
        }
        if (!file.canWrite()) {
            throw new IOException("Unable to write filesystem :=" + file.getPath());
        }
        return file;
    }

    /**
     * normalizes the string so that we can have a clean filename.
     * example : hello.world._file+name_etc.zip
     *
     * @param filename
     *
     * @return String
     */
    public static String getSimpleFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9\\s\\.\\/\\_]+", "").replaceAll("[\\s\\.]+", "_");
    }


}
