/*
 * Copyright (c) Rajashree Meganathan 2012
 * All rights reserved.
 */

package com.creational.prototype.ex2;

/**
 * @author : Rajashree Meganathan
 * @date : 11/21/12
 */
public class RunPrototypePattern {
  public static void main(String[] arguments) {
    System.out.println("Example for Prototype pattern");
    System.out.println();
    System.out.println("This example will create an Address object,");
    System.out.println(" which it will then duplicate by calling the");
    System.out.println(" object's clone method.");
    System.out.println();

    System.out.println("Creating first address.");
    Address address1 = new Address("8445 Silverado Trail", "Rutherford",
        "CA", "91734");
    System.out.println("First address created.");
    System.out.println("    Hash code = " + address1.hashCode());
    System.out.println(address1);
    System.out.println();

    System.out.println("Creating second address using the clone() method.");
    Address address2 = (Address) address1.copy();
    System.out.println("Second address created.");
    System.out.println("    Hash code = " + address2.hashCode());
    System.out.println(address2);
    System.out.println();

  }
}

interface Copyable {
  public Object copy();
}

class Address implements Copyable {
  private String type;

  private String street;

  private String city;

  private String state;

  private String zipCode;

  public static final String EOL_STRING = System
      .getProperty("line.separator");

  public static final String COMMA = ",";

  public static final String HOME = "home";

  public static final String WORK = "work";

  public Address(String initType, String initStreet, String initCity,
      String initState, String initZip) {
    type = initType;
    street = initStreet;
    city = initCity;
    state = initState;
    zipCode = initZip;
  }

  public Address(String initStreet, String initCity, String initState,
      String initZip) {
    this(WORK, initStreet, initCity, initState, initZip);
  }

  public Address(String initType) {
    type = initType;
  }

  public Address() {
  }

  public String getType() {
    return type;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getState() {
    return state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setType(String newType) {
    type = newType;
  }

  public void setStreet(String newStreet) {
    street = newStreet;
  }

  public void setCity(String newCity) {
    city = newCity;
  }

  public void setState(String newState) {
    state = newState;
  }

  public void setZipCode(String newZip) {
    zipCode = newZip;
  }

  public Object copy() {
    return new com.creational.prototype.ex2.Address(street, city, state, zipCode);
  }

  public String toString() {
    return "\t" + street + COMMA + " " + EOL_STRING + "\t" + city + COMMA
        + " " + state + " " + zipCode;
  }
}