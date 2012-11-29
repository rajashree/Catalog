/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */


package com.sourcen.core.web.filter.compression;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 * @since 1.0
 */
abstract class CompressionStreamEncoder implements Comparable<Object> {

    public static final Logger log = LoggerFactory.getLogger(CompressionStreamEncoder.class);

    private String contentEncoding = "";

    private int encoderPriority = 0;

    protected CompressionStreamEncoder(final String contentEncoding, final int encoderPriority) {
        this.contentEncoding = contentEncoding;
        this.encoderPriority = encoderPriority;
    }

    abstract OutputStream createOutputStream(OutputStream outputStream);

    public void finish(OutputStream stream) {
        if (stream instanceof DeflaterOutputStream) {
            try {
                ((DeflaterOutputStream) stream).finish();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    String getContentEncoding() {
        return this.contentEncoding;
    }

    int getEncoderPriority() {
        return this.encoderPriority;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Object o) {
        if (o instanceof CompressionStreamEncoder) {
            if (((CompressionStreamEncoder) o).getEncoderPriority() > getEncoderPriority()) {
                return 1;
            }
        }
        return -1;
    }

    //
    // GZIP Encoder
    //

    static final class GzipCompressionStream extends CompressionStreamEncoder {

        GzipCompressionStream(final String outputEncoding, final int encoderPriority) {
            super(outputEncoding, encoderPriority);
        }

        @Override
        OutputStream createOutputStream(OutputStream outputStream) {
            try {
                return new GZIPOutputStream(outputStream);
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
            return outputStream;
        }
    }

    //
    // Deflate Encoder
    //
    static final class DeflaterCompressionStream extends CompressionStreamEncoder {

        DeflaterCompressionStream(final String outputEncoding, final int encoderPriority) {
            super(outputEncoding, encoderPriority);
        }

        @Override
        OutputStream createOutputStream(OutputStream outputStream) {
            return new DeflaterOutputStream(new BufferedOutputStream(outputStream));
        }
    }

    //
    // ZIP Encoder
    //
    static final class ZipCompressionStream extends CompressionStreamEncoder {

        ZipCompressionStream(final String outputEncoding, final int encoderPriority) {
            super(outputEncoding, encoderPriority);
        }

        @Override
        public OutputStream createOutputStream(OutputStream outputStream) {
            return new ZipOutputStream(new BufferedOutputStream(outputStream));
        }

    }

}
