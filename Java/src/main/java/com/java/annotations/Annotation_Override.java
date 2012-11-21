package com.java.annotations;
//Whenever you want to override a method, declare the Override annotation type before the method:

public class Annotation_Override extends Parent {
    @Override
    public int calculate (int a, int b) {
        return (a + 1) * b;
    }
}