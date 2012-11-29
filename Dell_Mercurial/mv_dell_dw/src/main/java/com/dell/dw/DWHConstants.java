package com.dell.dw;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 8/2/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class DWHConstants {

    public static enum EndpointType {

        CS(1L), EXTERNAL(2L);

        private long value;

        EndpointType(final long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

    }
}
