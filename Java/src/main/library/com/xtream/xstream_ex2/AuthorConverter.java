package com.xtream.xstream_ex2;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class AuthorConverter implements SingleValueConverter{

	public Object fromString(String name) {
		return new Author(name);
		
	}

	public String toString(Object author) {
		return ((Author)author).getName();
	}

	public boolean canConvert(Class type) {
		return type.equals(Author.class);
	}

}
