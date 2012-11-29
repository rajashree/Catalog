package com.java.util.comparator;

import java.util.Date;


public class Employee {
    private int empId;
    public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private String name;
    private int age;

    public Employee(int empId, String name, int age, Date date) {
        this.empId = empId;
        this.age = age;
        this.name = name;
        this.date = date;
    }
   private Date date;
public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}
   
}