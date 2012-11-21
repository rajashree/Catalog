package com.xtream.xstream_ex3;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SingleValueCalendarConverter implements Converter {

	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Calendar calendar = (Calendar) source;
        writer.setValue(String.valueOf(calendar.getTime().getTime()));
		
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(Long.parseLong(reader.getValue())));
        return calendar;
		
	}

	public boolean canConvert(Class type) {
		return type.equals(GregorianCalendar.class);
		
	}

}
