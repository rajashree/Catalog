package com.java.usorted;

public class Address{
	String name;
	int no;
	String add;
	Address(String n, int no, String a){
		this.name=n;
		this.no=no;
		this.add=a;
	}
	public String toString(){
		return "\tName: "+name+"\t#: "+no+"\tStreet & place: "+add ;
	}
	
	
}