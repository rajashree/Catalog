package com.java.util.comparator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Util {
	
    @SuppressWarnings("deprecation")
	public static List<Employee> getEmployees() {
        
        List<Employee> col = new ArrayList<Employee>();
        
        col.add(new Employee(5, "Frank", 28,(new Date("11/4/2011"))));
        col.add(new Employee(1, "Jorge", 19,(new Date("12/1/2001"))));
        col.add(new Employee(6, "Bill", 34,(new Date("1/4/2001"))));
        col.add(new Employee(3, "Michel", 10,(new Date("1/5/2001"))));
        col.add(new Employee(7, "Simpson", 8,(new Date("12/2/2008"))));
        col.add(new Employee(4, "Clerk",16 ,(new Date("5/9/2001"))));
        col.add(new Employee(8, "Lee", 40,(new Date("5/4/2004"))));
        col.add(new Employee(2, "Mark", 30,(new Date("1/4/2006"))));
        
        return col;
    }
}