/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;

import com.sourcen.core.App;
import com.sourcen.core.config.ConfigurationService;
import org.apache.commons.io.FilenameUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: bhaskara $
 * @version $Revision: 2890 $, $Date:: 2012-06-04 14:00:26#$
 * @since 1.0
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static ConfigurationService configurationService = App.getService(ConfigurationService.class);

    public static String allowedDocumentExtensions =
            configurationService.getProperty("supported.doc.extensions", "pdf,doc,docx,xls,xlsx,ppt,pptx,txt");

    public static String allowedImageExtensions = configurationService.getProperty("supported.image.extensions",
            "jpg,jpeg,jpe,jfif,gif,png");

    public static List<String> docExtList = Arrays.asList(allowedDocumentExtensions.split(","));

    public static List<String> imageExtList = Arrays.asList(allowedImageExtensions.split(","));


    /**
     * Gives the character encoding of the given file.
     *
     * @param fileObject for which the character encoding is to be determined
     * @return encoding of the file as a String
     * @throws IOException
     */
    public static String getCharacterEncoding(File fileObject) throws IOException {
        String encoding = "";

        if (fileObject == null) {
            throw new NullPointerException("fileObject was null");
        }
        if (!fileObject.canRead()) {
            throw new IOException("Unable to read the file");
        }

        byte[] fileData = new byte[4096];

        FileInputStream fis = new FileInputStream(fileObject);
        UniversalDetector detector = new UniversalDetector(null);
        int bytesRead = 0;
        while ((bytesRead = fis.read(fileData)) > 0 && !detector.isDone()) {
            detector.handleData(fileData, 0, bytesRead);
        }
        encoding = detector.getDetectedCharset();
        logger.debug(fileObject.getPath() + "  is of encoding " + encoding);
        detector.dataEnd();
        detector.reset();
        if (encoding == null) {
            throw new UnsupportedEncodingException("Character Encoding not found.");
        }
        return encoding;
    }

    /**
     * Generates a time_stamped file name for a file.
     * Example: abc.zip to abc-2012_12_02-12_15_45.zip
     *
     * @param originalFilename in String format.
     * @return returns a time_stamped file name as a String
     * @throws IOException
     */
    public static String generateTimestampFilename(String originalFilename) throws IOException {
        return FilenameUtils.getBaseName(originalFilename) + "-" + generateTimestamp() + "." + FilenameUtils.getExtension(originalFilename);
    }


    /**
     * Generates a timestamop replacing ":" with "_".
     * Example : 2012:12:02-12:15:45 to 2012_12_02-12_15_45.
     *
     * @return converted date format as a string
     */
    public static String generateTimestamp() {
        return DateUtils.TIMESTAMP_DATEFORMAT.format(new Date()).replaceAll(":", "_");
    }

    public static URL loadResource(final String fileName) {
        final ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if (standardClassloader != null) {
            url = standardClassloader.getResource(fileName);
        }

        if (url == null) {
            url = FileUtils.class.getResource(fileName);
        }
        return url;
    }

    public static InputStream loadStream(final String fileName) {
        final ClassLoader standardClassloader = Thread.currentThread().getContextClassLoader();
        InputStream stream = standardClassloader.getResourceAsStream(fileName);
        if (stream != null) {
            return stream;
        }
        stream = FileUtils.class.getResourceAsStream(fileName);
        if (stream != null) {
            return stream;
        }

            URL url = loadResource(fileName);
            if (url != null) {
                try {
                    return url.openStream();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        throw new RuntimeException("Unable to load stream :=" + fileName);
    }

    public static boolean isFileExtensionAllowed(String fileName, String fileType) {
        boolean allowed = false;
        Assert.notNull(fileType, "Please provide the file type [eg : document or image]");
        String ext = FilenameUtils.getExtension(fileName).toLowerCase();
        if (fileType.equals("document")) {
            if (docExtList.contains(ext)) {
                allowed = true;
            }
        } else if (fileType.equals("image")) {
            if (imageExtList.contains(ext)) {
                allowed = true;
            }        }
        return allowed;
    }

}
