package com.dell.acs.web.resource.servlet;

import com.dell.acs.persistence.domain.RetailerSite;
import com.sourcen.core.util.FileSystem;
import com.sourcen.core.util.StringUtils;
import com.sourcen.core.web.servlet.resources.FileSystemResourceServlet;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 Created by IntelliJ IDEA. User: Mahalakshmi Date: 6/14/12 Time: 2:03 PM To change this template use File | Settings |
 File Templates.
 */
public class ErrorFileDownloadServlet extends FileSystemResourceServlet {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final int BUFSIZE = 6096;

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        if (this.isAuthenticated()) {

            final Map<String, String[]> requestParamNames = request.getParameterMap();
            if (requestParamNames.containsKey("errorFilePath")) {
                getErrorFile(request, response);
            } else if (requestParamNames.containsKey("logFilePath")) {
                getLogFile(request, response);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }

    }

    private HttpServletResponse getErrorFile(HttpServletRequest request, HttpServletResponse response) throws
            IOException {

        log.info("request path" + request.getParameter("errorFilePath"));
        final String filePath = request.getParameter("errorFilePath");
        final String extension = FilenameUtils.getExtension(filePath);
        final String errorFilePath =
                filePath.substring(0, filePath.length() - extension.length() - 1) + "_errors." + extension;
        File errorFile = FileSystem.getDefault().getFile(errorFilePath, false, false);

        log.info("Error File path" + errorFilePath);
        log.info("File file " + errorFile);

        if (errorFile.exists()) {
            ServletOutputStream outStream = response.getOutputStream();
            ServletContext context = getServletConfig().getServletContext();
            String mimetype = context.getMimeType(errorFilePath);

            // sets response content type
            if (mimetype == null) {
                mimetype = "application/octet-stream";
            }

            response.setContentType(mimetype);
            response.setContentLength((int) errorFile.length());
            String errorFileName = (new File(errorFilePath)).getName();
            // sets HTTP header
            response.setHeader("Content-Disposition", "attachment; filename=\"" + errorFileName + "\"");

            byte[] byteBuffer = new byte[BUFSIZE];
            DataInputStream in = new DataInputStream(new FileInputStream(errorFile));
            int length = 0;
            // reads the file's bytes and writes them to the response stream
            while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
                outStream.write(byteBuffer, 0, length);
            }

            in.close();
            outStream.close();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        return response;
    }

    private HttpServletResponse getLogFile(HttpServletRequest request, HttpServletResponse response) throws
            IOException {

        log.info("request path" + request.getParameter("logFilePath"));
        final String filePath = request.getParameter("logFilePath");
        final String logFilePath = getPathForLogFileName(filePath);

        File logFile = FileSystem.getDefault().getFile(logFilePath, false, false);
        log.info("logfile path" + logFile);

        if (logFile.exists()) {
            ServletOutputStream outStream = response.getOutputStream();
            ServletContext context = getServletConfig().getServletContext();
            String mimetype = context.getMimeType(logFilePath);

            // sets response content type
            if (mimetype == null) {
                mimetype = "application/octet-stream";
            }

            response.setContentType(mimetype);
            response.setContentLength((int) logFile.length());
            String errorFileName = (new File(logFilePath)).getName();
            // sets HTTP header
            response.setHeader("Content-Disposition", "attachment; filename=\"" + errorFileName + "\"");

            byte[] byteBuffer = new byte[BUFSIZE];
            DataInputStream in = new DataInputStream(new FileInputStream(logFile));
            int length = 0;
            // reads the file's bytes and writes them to the response stream
            while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
                outStream.write(byteBuffer, 0, length);
            }
            request.setAttribute("logFileStatus",true);
            in.close();
            outStream.close();

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        return response;
    }

    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.isAuthenticated();
    }

    private String getPathForLogFileName(String filePath) {

        String searchString = "/feeds";
        String replacementString =
                StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR + "logs" + StringUtils.CDN_RESOURCE_FILE_PATH_SEPARATOR +
                        "dataimportcom_dell_acs_dataimport_dataFile_logs_";
        String logFilePath = StringUtils.replace(filePath, searchString, replacementString);
        String logFileName = StringUtils.remove(logFilePath, "-").replace(".", "_").concat(".log");
        log.info("logFileName" + logFileName);
        return logFileName;
    }

}
