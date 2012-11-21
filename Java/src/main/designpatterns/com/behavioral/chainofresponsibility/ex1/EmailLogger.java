/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.behavioral.chainofresponsibility.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class EmailLogger extends Logger {

    public EmailLogger(int mask) {
        this.mask = mask;
    }

    protected void writeMessage(String msg) {
        System.out.println("Sending via email: " + msg);
    }

}
