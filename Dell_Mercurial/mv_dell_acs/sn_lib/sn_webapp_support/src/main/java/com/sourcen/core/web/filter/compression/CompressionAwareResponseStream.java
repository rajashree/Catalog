/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.filter.compression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 3030 $, $Date:: 2012-06-08 10:22:36#$
 */
public class CompressionAwareResponseStream extends ServletOutputStream {

    private static final Logger log = LoggerFactory.getLogger(CompressionAwareResponseStream.class);

    protected boolean compressionEnabled = false;
    protected boolean compressionTriggered = false;
    protected boolean closed = false;
    protected boolean outputStarted = false;


    protected int compressionThreshold = 0;
    protected boolean hasExceededThreshold = false;
    protected int totalBytesWritten = 0;

    protected OutputStream selectedStream = null;
    protected HttpServletResponse originalResponse = null;
    protected ServletOutputStream output = null;

    CompressionStreamEncoder encoder;

    private ByteArrayOutputStream buffer;
    private int originalContentLength = -1;

    public CompressionAwareResponseStream(HttpServletResponse response, CompressionStreamEncoder encoder) throws IOException {
        super();
        this.originalResponse = getOriginalResponse(response);
        this.encoder = encoder;
        reset();
    }

    protected HttpServletResponse getOriginalResponse(ServletResponse response) {
        while (response instanceof HttpServletResponseWrapper) {
            ServletResponse servletResponse = getOriginalResponse(((HttpServletResponseWrapper) response).getResponse());
            if (servletResponse instanceof HttpServletResponse) {
                return (HttpServletResponse) servletResponse;
            }
        }
        return (HttpServletResponse) response;
    }


    public void reset() {
        this.compressionEnabled = false;
        this.compressionTriggered = false;
        this.hasExceededThreshold = false;
        this.buffer = null;
        this.closed = false;
        if (compressionThreshold != 0) {
            this.initializeBuffer();
        }
    }

    public void resetBuffer() {
        if (outputStarted) {
            throw new IllegalStateException("Cannot reset buffer as compression has already started.");
        }
        if (buffer != null) {
            buffer.reset();
        }
    }

    private void initializeBuffer() {
        if (log.isDebugEnabled()) {
            log.debug("new buffer size is :=" + compressionThreshold);
        }
        buffer = new ByteArrayOutputStream(compressionThreshold);
    }


    protected void setCompressionThreshold(int threshold) {
        if (outputStarted) {
            throw new IllegalStateException("Buffer has already been committed, we cannot reset the threshold at this time");
        }
        compressionThreshold = threshold;
        if (buffer == null || buffer.size() == 0) {
            initializeBuffer();
        }
    }


    public void write(int b) throws IOException {
        checkIfWritable();
        buffer.write(b);
        flushIfRequired();
    }

    public void write(byte b[], int off, int len) throws IOException {
        checkIfWritable();
        if (len == 0) return;
        buffer.write(b, off, len);
        flushIfRequired();
    }


    protected void flushIfRequired() throws IOException {
        if (buffer.size() >= compressionThreshold) {
            flushStream();
        }
    }

    public void flushStream() throws IOException {
        // flush anything that was in our byteArray.
        int bufferSize = buffer.size();
        if (bufferSize > 0) {
            if (log.isDebugEnabled()) {
                log.debug("flusing remaining " + bufferSize + " bytes to compressor.");
            }
            getSelectedOutputStream().write(buffer.toByteArray(), 0, bufferSize);
            totalBytesWritten += bufferSize;
            buffer.reset();
        }
    }

    public OutputStream getSelectedOutputStream() {
        if (selectedStream == null) {
            outputStarted = true;
            try {
                this.output = originalResponse.getOutputStream();
            } catch (Exception e) {
                throw new RuntimeException("Unable to retrive the outputstream from the originalResponse", e);
            }
            if (compressionEnabled && totalBytesWritten + buffer.size() >= compressionThreshold) {
                if (log.isDebugEnabled()) {
                    log.debug("Creating a new compressionStream of encoding:=" + encoder.getContentEncoding());
                }
                compressionTriggered = true;
                if (log.isDebugEnabled()) {
                    log.debug("stream was compressed, setting contentEncoding to:=" + encoder.getContentEncoding());
                }
                originalResponse.setHeader(CompressionAwareResponseWrapper.HEADER_CONTENT_ENCODING, encoder.getContentEncoding());
                selectedStream = encoder.createOutputStream(output);
            } else {
                selectedStream = output;
            }
        }
        return selectedStream;
    }

    public void close() throws IOException {

        if (log.isDebugEnabled()) {
            log.debug("closing CompressionResponseStream and releasing buffers.");
        }
        checkIfWritable();
        flushStream();
        if (compressionTriggered) {
            selectedStream.close();
            output.close();
        } else {
            if (originalContentLength == -1) {
                if (log.isDebugEnabled()) {
                    log.debug("stream was not compressed, hence setting totalBytesWritten :=" + totalBytesWritten);
                }
                originalResponse.setContentLength(totalBytesWritten);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("stream was not compressed, hence setting originalContentLength :=" + originalContentLength);
                }
                originalResponse.setContentLength(originalContentLength);
            }
            if (selectedStream != null) {
                selectedStream.close();
            }
        }
        selectedStream = null;
        output = null;
        closed = true;
    }


    public void flush() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("flusing stream.");
        }
        checkIfWritable();
        flushIfRequired();
    }

    protected void checkIfWritable() throws IOException {
        if (closed) {
            throw new IOException("Cannot write into a closed output stream");
        }
    }


    //
    //
    //
    public void determineIfCompressionAllowed(String contentType) {
        if (outputStarted) {
            throw new IllegalStateException("The output has already been committed, cannot change the contentType now.");
        }
        compressionEnabled = CompressionFilter.getSupportedContentTypes().contains(contentType);
    }

    public boolean isOutputStarted() {
        return outputStarted;
    }

    public void setOriginalContentLength(int originalContentLength) {
        this.originalContentLength = originalContentLength;
    }
}
