/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.java.lang;

/**
 * @author : Rajashree Meganathan
 * @date : 11/14/12
 */
public class EnumEx {

    public static enum DocStatus {
        DRAFT(100),
        PUBLISHED(101);


        private int code;

        private DocStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static DocStatus getDocStatusByCode(int code) {
            for (DocStatus obj : values()) {
                if (obj.getCode() == code) {
                    return obj;
                }

            }
            throw new IllegalArgumentException("Invalid DocStatus Code");
        }

        public static void main(String[] args){
            System.out.println("DOC STATUS :: "+(EnumEx.DocStatus.getDocStatusByCode(300)).name());
        }

    }

}
