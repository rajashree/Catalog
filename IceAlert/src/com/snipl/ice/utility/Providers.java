package com.snipl.ice.utility;
/**
* @Author Sankara Rao
*   
*/
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.snipl.ice.config.InitConfig;

public class Providers {
	Hashtable<String, TreeMap> htable;

	TreeMap<String, String> tm;

	public Providers() {
		init();
	}

	void init() {
		htable = new Hashtable<String, TreeMap>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = null;
		try {
			parser = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
		try {
			Document document = parser.parse(InitConfig.path
					+ "/config/provider.xml");//config.getServletContext().getRealPath("/")+"/config/provider.xml");
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName(root.getChildNodes()
					.item(3).getNodeName());
			for (int i = 0; i < nodes.getLength(); i++) {
				Element innernode = (Element) nodes.item(i);
				NodeList innernode2 = innernode.getElementsByTagName(innernode
						.getChildNodes().item(1).getNodeName());
				tm = new TreeMap<String, String>();
				for (int j = 0; j < innernode2.getLength(); j++) {
					Element val = (Element) innernode2.item(j);
					tm.put(innernode2.item(j).getFirstChild().getNodeValue(),
							val.getAttribute("value"));
				}
				htable.put(innernode.getAttribute("name"), tm);
			}
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public String mprovider(String str) {
		String response = "<select>";
		if (htable.containsKey(str)) {
			tm = htable.get(str);
			Set set = tm.entrySet();
			Iterator i = set.iterator();
			while (i.hasNext()) {
				Map.Entry me = (Map.Entry) i.next();
				response += "<option value='" + me.getValue() + "'>"
						+ me.getKey() + "</option>";
			}
		}
		response += "</select>";
		return response;
	}

}
