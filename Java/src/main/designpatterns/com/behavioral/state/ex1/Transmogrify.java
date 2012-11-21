package com.behavioral.state.ex1;

//: c07:Transmogrify.java
//Dynamically changing the behavior of an object via composition (the State design pattern).
//From 'Thinking in Java, 3rd ed.' (c) Bruce Eckel 2002
//www.BruceEckel.com. See copyright notice in CopyRight.txt.

public class Transmogrify {
public static void main(String[] args) {
 Stage stage = new Stage();
 stage.performPlay();
 stage.change();
 stage.performPlay();
}
} ///:~


        
    