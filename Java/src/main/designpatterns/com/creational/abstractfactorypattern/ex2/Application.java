/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.creational.abstractfactorypattern.ex2;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Application {
    public Application(GUIFactory factory){
        Button button = factory.createButton();
        button.paint();
    }
}
