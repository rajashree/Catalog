/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.structural.bridge.ex2;

import java.util.ArrayList;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class RunBridgePattern_Ex1 {
    public static void main(String [] arguments){
        System.out.println("Example for the Bridge pattern");
        System.out.println();
        System.out.println("This example divides complex behavior among two");
        System.out.println(" classes - the abstraction and the implementation.");
        System.out.println();
        System.out.println("In this case, there are two classes which can provide the");
        System.out.println(" abstraction - BaseList and OrnamentedList. The BaseList");
        System.out.println(" provides core funtionality, while the OrnamentedList");
        System.out.println(" expands on the model by adding a list character.");
        System.out.println();
        System.out.println("The OrderedListImpl class provides the underlying storage");
        System.out.println(" capability for the list, and can be flexibly paired with");
        System.out.println(" either of the classes which provide the abstraction.");

        System.out.println("Creating the OrderedListImpl object.");
        ListImpl implementation = new OrderedListImpl();

        System.out.println("Creating the BaseList object.");
        BaseList listOne = new BaseList();
        listOne.setImplementor(implementation);
        System.out.println();

        System.out.println("Adding elements to the list.");
        listOne.add("One");
        listOne.add("Two");
        listOne.add("Three");
        listOne.add("Four");
        System.out.println();

        System.out.println("Creating an OrnamentedList object.");
        OrnamentedList listTwo = new OrnamentedList();
        listTwo.setImplementor(implementation);
        listTwo.setItemType('+');
        System.out.println();

        System.out.println("Creating an NumberedList object.");
        NumberedList listThree = new NumberedList();
        listThree.setImplementor(implementation);
        System.out.println();

        System.out.println("Printing out first list (BaseList)");
        for (int i = 0; i < listOne.count(); i++){
            System.out.println("\t" + listOne.get(i));
        }
        System.out.println();

        System.out.println("Printing out second list (OrnamentedList)");
        for (int i = 0; i < listTwo.count(); i++){
            System.out.println("\t" + listTwo.get(i));
        }
        System.out.println();

        System.out.println("Printing our third list (NumberedList)");
        for (int i = 0; i < listThree.count(); i++){
            System.out.println("\t" + listThree.get(i));
        }
    }
}

interface ListImpl{
    public void addItem(String item);
    public void addItem(String item, int position);
    public void removeItem(String item);
    public int getNumberOfItems();
    public String getItem(int index);
    public boolean supportsOrdering();
}

class BaseList{
    protected ListImpl implementor;

    public void setImplementor(ListImpl impl){
        implementor = impl;
    }

    public void add(String item){
        implementor.addItem(item);
    }
    public void add(String item, int position){
        if (implementor.supportsOrdering()){
            implementor.addItem(item, position);
        }
    }

    public void remove(String item){
        implementor.removeItem(item);
    }

    public String get(int index){
        return implementor.getItem(index);
    }

    public int count(){
        return implementor.getNumberOfItems();
    }
}

class NumberedList extends BaseList{
    public String get(int index){
        return (index + 1) + ". " + super.get(index);
    }
}

class OrderedListImpl implements ListImpl{
    private ArrayList items = new ArrayList();

    public void addItem(String item){
        if (!items.contains(item)){
            items.add(item);
        }
    }
    public void addItem(String item, int position){
        if (!items.contains(item)){
            items.add(position, item);
        }
    }

    public void removeItem(String item){
        if (items.contains(item)){
            items.remove(items.indexOf(item));
        }
    }

    public boolean supportsOrdering(){
        return true;
    }

    public int getNumberOfItems(){
        return items.size();
    }

    public String getItem(int index){
        if (index < items.size()){
            return (String)items.get(index);
        }
        return null;
    }
}

class OrnamentedList extends BaseList{
    private char itemType;

    public char getItemType(){ return itemType; }
    public void setItemType(char newItemType){
        if (newItemType > ' '){
            itemType = newItemType;
        }
    }

    public String get(int index){
        return itemType + " " + super.get(index);
    }
}