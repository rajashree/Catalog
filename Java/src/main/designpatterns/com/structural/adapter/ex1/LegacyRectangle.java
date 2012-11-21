/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.structural.adapter.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class LegacyRectangle {

    public void draw(int x, int y, int w, int h) {
        System.out.println("rectangle at (" + x + ',' + y + ") with width " + w + " and height " + h);
    }

}
