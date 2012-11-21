/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.structural.adapter.ex1;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
class Line implements Shape {

    private LegacyLine adaptee = new LegacyLine();

    public void draw(int x1, int y1, int x2, int y2) {
        adaptee.draw(x1, y1, x2, y2);
    }

}
