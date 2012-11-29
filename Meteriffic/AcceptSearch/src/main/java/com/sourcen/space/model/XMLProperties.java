package com.sourcen.space.model;

import java.io.*;
import java.util.*;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLProperties implements Map {

	private File file;
	private Document doc;
	private Map<String, String> propertyCache = new HashMap<String, String>();
	private Object propLock = new Object();

	public XMLProperties(InputStream in) throws Exception {
		Reader reader = new BufferedReader(new InputStreamReader(in));
		buildDoc(reader);
	}

	private void buildDoc(Reader in) throws Exception {
		SAXReader xmlReader = new SAXReader();
		doc = xmlReader.read(in);
	}

	public XMLProperties(String fileName) throws IOException {
		this.file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException("XML properties file does not exist: " + fileName);
			
		} else {

		    if (!file.canRead()) {
				throw new IOException("XML properties file must be readable: "
						+ fileName);
			}
			if (!file.canWrite()) {
				throw new IOException("XML properties file must be writable: "
						+ fileName);
			}
			Reader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				buildDoc(reader);
			} catch (Exception e) {
				System.err.println("Error creating XML properties file " + fileName
						+ ": " + e.getMessage());
				throw new IOException(e.getMessage());
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public boolean containsKey(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsValue(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get(Object o) {
		String name = (String) o;
		String value = propertyCache.get(name);
		if (value != null) {
			return value;
		}

		String[] propName = parsePropertyName(name);
		// Search for this property by traversing down the XML heirarchy.
		Element element = doc.getRootElement();
		for (int i = 0; i < propName.length; i++) {
			element = element.element(propName[i]);
			if (element == null) {				
				return null;
			}
		}

		synchronized (propLock) {
		
			value = element.getText();
			if ("".equals(value)) {
				return null;
			} else {
				// Add to cache so that getting property next time is fast.
				value = value.trim();
				propertyCache.put(name, value);
				return value;
			}
		}
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Set keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(Map arg0) {
		// TODO Auto-generated method stub

	}

	public Object remove(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		return 0;
	}

	public Collection values() {
		// TODO Auto-generated method stub
		return null;
	}

	private String[] parsePropertyName(String name) {
		List<String> propName = new ArrayList<String>(5);
		// Use a StringTokenizer to tokenize the property name.
		StringTokenizer tokenizer = new StringTokenizer(name, ".");
		while (tokenizer.hasMoreTokens()) {
			propName.add(tokenizer.nextToken());
		}
		return propName.toArray(new String[propName.size()]);
	}

}
