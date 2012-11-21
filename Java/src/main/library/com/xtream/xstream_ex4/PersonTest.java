package com.xtream.xstream_ex4;

import com.thoughtworks.xstream.XStream;

public class PersonTest {
	
	public static void main(String args[]){
		Person per = new Person();
		per.setName("Rajashree");
		XStream xstream = new XStream();
		xstream.registerConverter(new PersonConverter());
		xstream.alias("person", Person.class);
		
		System.out.println(xstream.toXML(per));
		
	}
}
