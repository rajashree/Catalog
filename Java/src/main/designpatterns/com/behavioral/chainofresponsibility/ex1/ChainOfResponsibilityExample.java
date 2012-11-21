package com.behavioral.chainofresponsibility.ex1;


public class ChainOfResponsibilityExample {
 
    public static void main(String[] args) {
        // Build the chain of responsibility
        Logger l,l1;
        l1 = l = new StdoutLogger(Logger.DEBUG);
        l1 = l1.setNext(new EmailLogger(Logger.NOTICE));
        l1 = l1.setNext(new StderrLogger(Logger.ERR));
 
        // Handled by StdoutLogger
        l.message("Entering function y.", Logger.DEBUG);
 
        // Handled by StdoutLogger and EmailLogger
        l.message("Step1 completed.", Logger.NOTICE);
 
        // Handled by all three loggers
        l.message("An error has occurred.", Logger.ERR);
    }
 
}
