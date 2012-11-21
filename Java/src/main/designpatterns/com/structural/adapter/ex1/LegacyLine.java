/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.structural.adapter.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */ //Adaptee shld not change (or) cannot be changed due to closed source or something..
class LegacyLine {

    public void draw(int x1, int y1, int x2, int y2) {
        System.out.println("line from (" + x1 + ',' + y1 + ") to (" + x2 + ',' + y2 + ')');
    }

}
