package com.xtream.xstream_ex3;

import com.thoughtworks.xstream.XStream;

public class Tutorial {

	public static void main(String args[]){
		XStream xstream = new XStream();
		//xstream.processAnnotations(Message.class);
		//xstream.autodetectAnnotations(true);
		System.out.println(xstream.toXML(new Message(15,"firstname","lastname","middlename","test")));
	}
}
