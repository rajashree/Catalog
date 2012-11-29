/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.filter.compression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public class CompressionAwareResponseWrapper extends HttpServletResponseWrapper {


    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final Logger log = LoggerFactory.getLogger(CompressionAwareResponseWrapper.class);

    private HttpServletRequest originalRequest;
    private HttpServletResponse originalResponse;
    private CompressionStreamEncoder encoder;
    private Integer compressionThreshold = 0;

    private Integer contentLength = 0;
    private String contentType = null;

    private ServletOutputStream stream = null;
    private PrintWriter writer = null;


    public CompressionAwareResponseWrapper(HttpServletRequest request, HttpServletResponse response, CompressionStreamEncoder encoder) {
        super(response);
        this.encoder = encoder;
        this.originalRequest = request;
        this.originalResponse = response;
    }

    //
    // Content Type and content-Length stuff.
    //
    protected CompressionAwareResponseStream getCompressionAwareResponseStream() {
        if (stream != null) {
            return (CompressionAwareResponseStream) stream;
        }
        return null;
    }

    public void setContentType(String contentType) {
        if (log.isDebugEnabled()) {
            log.debug("setting contentType to " + contentType);
        }
        if (stream != null) {
            if (getCompressionAwareResponseStream().isOutputStarted()) {
                throw new IllegalArgumentException("output has already been sent.");
            } else {
                getCompressionAwareResponseStream().determineIfCompressionAllowed(contentType);
            }
        }
        this.contentType = contentType;
        originalResponse.setContentType(contentType);
    }

    public void setContentLength(int length) {
        // ignore this, and save it for later use.
        this.contentLength = length;
        if (stream != null) {
            getCompressionAwareResponseStream().setOriginalContentLength(length);
        }
    }

    public void setCompressionThreshold(Integer threshold) {
        this.compressionThreshold = threshold;
    }


    //
    // headers
    //

    private boolean isEditableHeader(String header, Object value) {
        if (header == null) {
            return true;
        }

        if (header.equalsIgnoreCase(HEADER_CONTENT_ENCODING)) {
            return false;
        }

        if (header.equalsIgnoreCase(HEADER_CONTENT_LENGTH)) {
            if (value instanceof Integer) {
                setContentLength((Integer) value);
            } else if (value == null) {
                // ignore
            } else {
                setContentLength(Integer.valueOf(value.toString()));
            }
            return false;
        }

        return true;
    }


    @Override
    public void setHeader(String name, String value) {
        if (isEditableHeader(name, value)) {
            super.setHeader(name, value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        if (isEditableHeader(name, value)) {
            super.addHeader(name, value);
        }
    }

    @Override
    public void setIntHeader(String name, int value) {
        if (isEditableHeader(name, value)) {
            super.setIntHeader(name, value);
        }
    }

    @Override
    public void addIntHeader(String name, int value) {
        if (isEditableHeader(name, value)) {
            super.addIntHeader(name, value);
        }
    }

    //
    // Streams.
    //

    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        if (stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called.");
        }
        stream = createOutputStream();
        String characterEncoding = originalResponse.getCharacterEncoding();
        if (characterEncoding != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, characterEncoding));
        } else {
            writer = new PrintWriter(stream);
        }
        return writer;

    }


    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called.");
        }
        if (stream == null) {
            stream = createOutputStream();
        }
        if (log.isDebugEnabled()) {
            log.debug("setting stream to :=" + stream);
        }
        return stream;
    }

    public ServletOutputStream createOutputStream() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("creating a new CompressionAwareResponseStream.");
        }
        CompressionAwareResponseStream stream = new CompressionAwareResponseStream(originalResponse, encoder);
        stream.setCompressionThreshold(this.compressionThreshold);
        if (contentType != null) {
            stream.determineIfCompressionAllowed(contentType);
        }
        return stream;
    }


    public void flushBuffer() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("flusing buffer");
        }
        if (stream != null) {
            ((CompressionAwareResponseStream) stream).flush();
        }
    }

    public void finish() {
        try {
            if (writer != null) {
                writer.close();
            } else if (stream != null && !getCompressionAwareResponseStream().closed) {
                stream.close();
            }
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public void resetBuffer() {
        if (stream != null) {
            getCompressionAwareResponseStream().resetBuffer();
        }
        super.resetBuffer();
    }

    @Override
    public void reset() {
        if (stream != null) {
            getCompressionAwareResponseStream().reset();
        }
        stream = null;
        writer = null;
        super.reset();
    }
}
