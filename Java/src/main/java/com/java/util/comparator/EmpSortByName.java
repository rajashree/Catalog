package com.java.util.comparator;

import java.util.Comparator;


public class EmpSortByName implements Comparator<Employee>{
	private boolean asc = true;
    public int compare(Employee o1, Employee o2) {
    	//return asc ? o1.getDate().compareTo(o2.getDate()) : o2.getDate().compareTo(o1.getDate());
        return o1.getName().compareTo(o2.getName());
    }
}