package com.xtream.xstream_ex3;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("message")
public class Message {
	//@XStreamAlias("type")
	//@XStreamAsAttribute
	@XStreamOmitField
	private int messageType;
	
	@XStreamImplicit(itemFieldName="part")
	private List<String> content;
	
	@XStreamConverter(SingleValueCalendarConverter.class)
	private Calendar created = new GregorianCalendar();
	private String Val="Valueeeeeeeeee";
	
	public  Message(int messageType,String ... content ){
		this.messageType = messageType;
		this.content = Arrays.asList(content); 
	}
}

