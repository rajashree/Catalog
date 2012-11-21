package com.structural.composite.ex1;

/** Client */
public class Program {
 
    public static void main(String[] args) {
        //Initialize four ellipses
        Ellipse ellipse1 = new Ellipse();
        Ellipse ellipse2 = new Ellipse();
        Ellipse ellipse3 = new Ellipse();
        Ellipse ellipse4 = new Ellipse();
        Ellipse ellipse5 = new Ellipse();
        
        //Initialize three composite graphics
        CompositeGraphic graphic = new CompositeGraphic();
        CompositeGraphic graphic1 = new CompositeGraphic();
        CompositeGraphic graphic2 = new CompositeGraphic();
 
        //Composes the graphics
        graphic1.add(ellipse1);
        graphic1.add(ellipse2);
        graphic1.add(ellipse3);
 
        graphic2.add(ellipse4);
 
        graphic.add(graphic1);
        graphic.add(graphic2);
 
        //Prints the complete graphic (four times the string "Ellipse").
        System.out.println("COMPOSITE OBJECT PRINT");
        graphic.print();
        System.out.println("INDIVIDUAL OBJECT PRINT");
        ellipse5.print();
        
    }
}
