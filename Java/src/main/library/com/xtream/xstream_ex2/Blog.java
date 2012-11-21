package com.xtream.xstream_ex2;

import java.util.*;

public class Blog {

	private Author author;
	private List entries = new ArrayList();
	
	public Blog(Author author){
		this.author = author;
	}
	
	public void add(Entry entry){
		entries.add(entry);
	}
	public List getContent(){
		return this.entries;
	}
}
