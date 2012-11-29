package com.java.usorted;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
/*
 * 
 Transient: The transient modifier applies to variable only 
and it is not stored as part of it's objects persistent 
state.Transient variables are not serialized.
 
 
 Volatile: Volatile modifier applies to variables only and 
it tells the compiler that the variable modified by 
volatile can be changed unexpectedly by other parts of the 
program.

  */
public class Transient {
  public static void main(String[] args) throws Exception {
    User a = new User("A", "B");
    System.out.println("logon a = " + a);
    ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("User.out"));
    o.writeObject(a);
    o.close();

    Thread.sleep(1000); // Delay for 1 second

    ObjectInputStream in = new ObjectInputStream(new FileInputStream("User.out"));
    System.out.println("Recovering object at " + new Date());
    a = (User) in.readObject();
    System.out.println("logon a = " + a);
  }

}

class User implements Serializable {
  private Date date = new Date();

  private String username;

  private transient String password;

  public User(String name, String pwd) {
    username = name;
    password = pwd;
  }

  public String toString() {
    String pwd = (password == null) ? "(n/a)" : password;
    return "logon info: \n   username: " + username + "\n   date: " + date + "\n   password: "
        + pwd;
  }
}