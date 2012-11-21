package com.xtream.xstream_ex2;

import com.thoughtworks.xstream.XStream;

public class Sample {

	public static void main(String args[]){
		Blog blog = new Blog(new Author("Rajashree"));
		blog.add(new Entry("title1","description 1"));
		blog.add(new Entry("title2","description 2"));
		
		XStream xstream = new XStream();
		xstream.alias("Blog",Blog.class);
		xstream.alias("Entry",Entry.class);
		xstream.addImplicitCollection(Blog.class,"entries");
		xstream.useAttributeFor(Blog.class, "author");
		xstream.registerConverter(new AuthorConverter()); 
		System.out.println(xstream.toXML(blog));
	}
}
