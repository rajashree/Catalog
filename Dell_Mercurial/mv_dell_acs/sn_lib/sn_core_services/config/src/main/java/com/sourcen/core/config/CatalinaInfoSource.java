package com.sourcen.core.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;

public class CatalinaInfoSource {

	public CatalinaInfoSource() {
		// TODO Auto-generated constructor stub
	}

	private static class FindItem implements QueryExp {
		private String _key;
		private String _value;

		public FindItem(String key, String value) {
			this._key = key;
			this._value = value;
		}

		@Override
		public boolean apply(ObjectName name)
				throws BadStringOperationException,
				BadBinaryOpValueExpException, BadAttributeValueExpException,
				InvalidApplicationException {
			return name.getKeyProperty(this._key).compareTo(this._value) == 0;
		}

		@Override
		public void setMBeanServer(MBeanServer s) {
		}
	}

	public String getApplicationUrl() {
		StringBuilder sb = new StringBuilder();
		try {
			MBeanServer server = findCatalinaDomain();
			Set<ObjectName> names = server.queryNames(null, new FindItem("type",
					"Connector"));

			if (names.size() > 0) {
				ObjectName name = names.iterator().next();
				int port = (Integer) server.getAttribute(name, "port");
				boolean secure = (Boolean) server.getAttribute(name, "secure");
				InetAddress ip = InetAddress.getLocalHost();
				sb.append(String.format("%s://%s%s/", (secure ? "https"
						: "http"), ip.getHostAddress(), (port != 80 ? ":" + port : "")));
			}
			names = server.queryNames(null, new FindItem("j2eeType", "WebModule"));
			
			for(ObjectName name : names) { 
				String encodedPath = (String) server.getAttribute(name, "encodedPath");
				if (encodedPath.length() == 0) {
					continue;
				}
				String baseName = (String) server.getAttribute(name, "baseName");
				sb.append(baseName);
			}
		} catch (AttributeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		return sb.toString();
	}

	private MBeanServer findCatalinaDomain() {
		for (MBeanServer server : MBeanServerFactory.findMBeanServer(null)) {
			String[] domains = server.getDomains();

			for (String domain : domains) {
				if (domain.compareTo("Catalina") == 0) {
					return server;
				}
			}
		}

		return null;
	}

}
