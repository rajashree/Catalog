package com.java.database.pojo;

public class Student {

	private String name;
	
	private int no;
	
	private String adr;

	public Student( int no, String name,String adr) {
		super();
		this.name = name;
		this.no = no;
		this.adr = adr;
	}

	public String getAdr() {
		return adr;
	}

	public void setAdr(String adr) {
		this.adr = adr;
	}

	public String getName() {
		return name;
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
}
