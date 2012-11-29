/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.filter.compression;

import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
class EncodingStrategy {

    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";

    public static final Map<String, CompressionStreamEncoder> supportedEncodings = new ConcurrentHashMap<String, CompressionStreamEncoder>();

    public static final String ENCODING_GZIP = "gzip";

    public static final String ENCODING_X_GZIP = "x-gzip";

    public static final String ENCODING_COMPRESS = "compress";

    public static final String ENCODING_X_COMPRESS = "x-compress";

    public static final String ENCODING_DEFLATE = "deflate";

    static {
        supportedEncodings.put(ENCODING_GZIP, new CompressionStreamEncoder.GzipCompressionStream(ENCODING_GZIP, 6));
        supportedEncodings.put(ENCODING_X_GZIP, new CompressionStreamEncoder.GzipCompressionStream(ENCODING_X_GZIP, 5));
        supportedEncodings.put(ENCODING_DEFLATE, new CompressionStreamEncoder.DeflaterCompressionStream(ENCODING_DEFLATE, 4));
        supportedEncodings.put(ENCODING_COMPRESS, new CompressionStreamEncoder.ZipCompressionStream(ENCODING_COMPRESS, 3));
        supportedEncodings.put(ENCODING_X_COMPRESS, new CompressionStreamEncoder.ZipCompressionStream(ENCODING_X_COMPRESS, 2));
    }

    public static CompressionStreamEncoder determineEncoder(String acceptEncoding) {
        // check if we can support the encoding. gzip,deflate..
        // String acceptEncoding = request.getHeader("Accept-Encoding");
        if (acceptEncoding == null || acceptEncoding.equals("")) {
            return null;
        }

        // check if the contentType is of type Allowed encoding.
        acceptEncoding = acceptEncoding.trim();
        String[] encodings = null;
        if (acceptEncoding.contains(",")) {
            encodings = acceptEncoding.split(",");
        } else {
            encodings = new String[]{acceptEncoding};
        }
        final TreeSet<CompressionStreamEncoder> parsedEncoders = new TreeSet<CompressionStreamEncoder>();
        for (final String encodingString : encodings) {
            final Encoding encoding = new Encoding(encodingString);
            if (encoding.isCompressable()) {
                parsedEncoders.add(supportedEncodings.get(encoding.getEncoding()));
            }
        }
        if (parsedEncoders.isEmpty()) {
            return null;
        }
        return parsedEncoders.first();
    }

    public static class Encoding {

        private String encoding = "";

        private Float qValue = null;

        private boolean compressable = false;

        public Encoding(final String encodingString) {
            final String[] encodingParts = encodingString.split(";( )?q=( )?");
            if (encodingParts.length > 0) {
                this.encoding = encodingParts[0].trim();
            }
            if (encodingParts.length > 1) {
                try {
                    this.qValue = Float.parseFloat(encodingParts[1].trim());
                } catch (final Exception e) {
                    // ignore as the default value is 0
                }
            }
            // if we support it, and we dont have a qValue, just set it to 1,
            // this can happen where string is 'compress=1.0; gzip'
            if (supportedEncodings.containsKey(this.encoding)) {
                this.compressable = true;
                if (this.qValue == null) {
                    this.qValue = 1.0F;
                }
            } else {
                // if we don't support the encoding, but
                // the encoding is a wildcard , then set it to gzip.
                if (this.encoding.equalsIgnoreCase("*") && this.qValue != null && this.qValue > 0) {
                    this.encoding = ENCODING_GZIP;
                    this.compressable = true;
                }
            }
        }

        public boolean isCompressable() {
            return this.compressable;
        }

        public String getEncoding() {
            return this.encoding;
        }
    }

}
