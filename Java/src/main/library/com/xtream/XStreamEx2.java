package com.xtream;

import java.awt.List;
import java.util.ArrayList;



import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;


class Entry{
	private String title, description;
	public Entry(String title, String description){
		this.title = title;
		this.description = description;
	}
}

class Blog{
	private String author;
	private ArrayList entries = new ArrayList();
	
	public Blog(String author){
		this.author = author;
	}
	public void add(Entry entry){
		entries.add(entry);
	}
	public ArrayList getContent(){
		return entries;
	}
	public String getAuthor(){
		return author;
	}
}



public class XStreamEx2 {
	public static void main(String arg[]){
		Blog teamblog = new Blog("Dev");
		teamblog.add(new Entry("First","This is my first blog"));
		teamblog.add(new Entry("Second","This is my Second blog"));
		XStream xs = new XStream();
		
		xs.alias("blog", Blog.class);
		xs.alias("entry", Entry.class);
		
		xs.addImplicitCollection(Blog.class, "entries");
		
		xs.useAttributeFor(Blog.class,"author");
		String str =xs.toXML(teamblog);
		System.out.println("from object to xml "+str);
        System.out.println("from xml to object "+((Blog)xs.fromXML(str)).getAuthor());
	
		
	}

}
