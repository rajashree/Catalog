/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.structural.adapter.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Rectangle implements Shape {

    private LegacyRectangle adaptee = new LegacyRectangle();

    public void draw(int x1, int y1, int x2, int y2) {
        adaptee.draw( Math.min(x1, x2), Math.min(y1, y2),
                      Math.abs(x2 - x1),  Math.abs(y2 - y1) );
    }

}
