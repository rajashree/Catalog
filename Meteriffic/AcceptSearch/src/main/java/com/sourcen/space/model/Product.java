package com.sourcen.space.model;

import java.io.Serializable;

public class Product implements Serializable {

	private int id;
	private String name;
	private int pid;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public String toXML(){
		
		String productXML="<tag label=\""+this.getName()+"\"   id=\""+this.getName()+"\">";
		
		return productXML;
		
		
	}
	
}
