package com.java.applet;

import java.applet.*;
import java.awt.*;

public class Myapplet extends Applet{
	String str;
	public void init(){
		str = "Hello!!! This is my first applet";
	}
	public void paint(Graphics g){
		g.drawString(str, 12,50);
	}
}