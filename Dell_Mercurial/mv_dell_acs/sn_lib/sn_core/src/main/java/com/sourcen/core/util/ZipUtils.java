/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;


/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3591 $, $Date:: 2012-06-25 10:09:29#$
 * @since 1.0
 */
public class ZipUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    public static Collection<File> extractZipFileWithStructure(File zipFile, String unzipLocation) throws IOException {
        return extractZipFileWithStructure(zipFile, unzipLocation, null);
    }

    /**
     * Extracts the zip file at the specified location and returns the collection csv of files.
     *
     * @param zipFile
     * @param unzipLocation
     *
     * @return outputFiles - optimized to return only csv files
     * @throws IOException
     */
    public static Collection<File> extractZipFileWithStructure(File zipFile, String unzipLocation, FileFilter filter) throws IOException {
        Set<File> outputFiles = new HashSet<File>();
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        Exception exception = null;
        ZipFile zip = null;

        try {
            logger.debug("File - " + FilenameUtils.getName(zipFile.getAbsolutePath()) + " is being extracted into "
                    + unzipLocation);
            zip = new ZipFile(zipFile.getAbsoluteFile());

            Enumeration zipFileEntries = zip.getEntries();

            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipArchiveEntry entry = (ZipArchiveEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destinationFile = new File(unzipLocation, currentEntry);
                File destinationParent = destinationFile.getParentFile();

                // create the parent directory structure if needed
                if(!destinationParent.exists()) {
                    Assert.isTrue(destinationParent.mkdirs(),"unable to create tree structure :="+destinationParent.getAbsolutePath());
                }
                Assert.isTrue(destinationParent.exists() && destinationParent.isDirectory(),"unable to access directory :="+destinationParent.getAbsolutePath());

                if(filter != null && !filter.accept(destinationFile)){
                    logger.debug("skipping "+destinationFile+" due to filter constraint.");
                    continue;
                }

                if (!entry.isDirectory()) {
                    if (!FilenameUtils.getExtension(entry.getName()).contains("db")) {
                        if (destinationFile.exists()) {
                            Assert.isTrue(destinationFile.delete(), "unable to delete destinationFile:=" + destinationFile.getAbsolutePath());
                            Assert.isTrue(destinationFile.createNewFile(), "unable to create destinationFile:=" + destinationFile.getAbsolutePath());
                        } else {
                            Assert.isTrue(destinationFile.createNewFile(), "unable to create destinationFile:=" + destinationFile.getAbsolutePath());
                        }

                        Assert.isTrue(destinationFile.canRead(), "unable to read destinationFile:=" + destinationFile.getAbsolutePath());
                        Assert.isTrue(destinationFile.canWrite(), "unable to write destinationFile:=" + destinationFile.getAbsolutePath());

                        // write the current file to disk
                        inputStream = new BufferedInputStream(zip.getInputStream(entry));
                        FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
                        IOUtils.copy(inputStream, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        outputFiles.add(destinationFile);
                    }
                }
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (zip != null) {
                zip.close();
            }
        }
        if (exception != null) {
            throw new IOException(exception);
        }
        return outputFiles;
    }

    /**
     * Uncompress the given GZIP file into csv at the specified location.
     *
     * @param gzipFile
     * @param unzipLocation
     *
     * @return outputFile
     * @throws IOException
     */
    public static File extractGZipFile(File gzipFile, String unzipLocation) throws IOException {

        Exception exception = null;
        GZIPInputStream gzipInputStream = null;
        OutputStream outputStream = null;
        File outputFile = null;
        try {
            gzipInputStream = new GZIPInputStream(new FileInputStream(gzipFile));
            String filename = FilenameUtils.getName(gzipFile.getAbsolutePath());
            String newFileName = filename.substring(0, filename.lastIndexOf(".")); // abc.csv.gz -> abc.csv
            if (FilenameUtils.getExtension(newFileName).equalsIgnoreCase("")) {
                newFileName += ".csv";
            }
            outputFile = new File(unzipLocation, newFileName);
            outputStream = new FileOutputStream(outputFile);
            byte[] buf = new byte[4096];
            int length;
            while ((length = gzipInputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
        } catch (Exception e) {
            exception = e;
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        if (exception != null) {
            throw new IOException(exception);
        }
        return outputFile;
    }
}
