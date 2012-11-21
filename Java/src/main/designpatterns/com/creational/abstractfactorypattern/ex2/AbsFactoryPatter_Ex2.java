package com.creational.abstractfactorypattern.ex2;

/* GUIFactory example -- */


public class AbsFactoryPatter_Ex2 {
    public static void main(String[] args) {
        new Application(createOsSpecificFactory());
    }
 
    public static GUIFactory createOsSpecificFactory() {
        int sys = 0;
        if (sys == 0) {
            return new WinFactory();
        } else {
            return new OSXFactory();
        }
    }
}
