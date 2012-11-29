/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.servlet.resources;

import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: sameeks $
 @version $Revision: 3857 $, $Date:: 2012-07-05 11:46:15#$ */

public class FileSystemResourceServlet extends ResourceServlet {

    private static final Logger log = LoggerFactory.getLogger(FileSystemResourceServlet.class);


    private FileSystem fileSystem;

    private File servingDirectory;

    public FileSystemResourceServlet() {
        super();
    }


    @Override
    protected File getFile(HttpServletRequest request, String requestPath) {
        if (fileSystem == null) {
            try {
                String dirName = getServletConfig().getInitParameter("directory");
                fileSystem = ConfigurationServiceImpl.getInstance().getFileSystem();
                servingDirectory = fileSystem.getDirectory(StringUtils.getSimpleString(dirName));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage() + " while trying to serve request:=" + requestPath);
            }
        }
        try {
            log.info("Serving directory : " + servingDirectory);
            log.info("Requested file    : " + requestPath);
            return fileSystem.getFile(servingDirectory, requestPath, false, true);
        } catch (NullPointerException npe) {
            log.error("either filesystem was null or servingDirectory was not set in the servletConfig.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
