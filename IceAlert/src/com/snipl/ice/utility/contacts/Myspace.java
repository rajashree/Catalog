package com.snipl.ice.utility.contacts;

import java.util.ArrayList;

import org.curl.*;


class Myspace implements CurlWrite {
	static StringBuffer sbResponse = new StringBuffer(16384);

	
	static String add;


	public int handleString(byte s[]) {
		sbResponse.append(new String(s));
		return 0;
	}

	public static void main(String[] args) {
		ArrayList al=new Myspace().myspaceContacts("parveen.aasia@gmail.com","sourcen1");
		String[] AlbumUrl=(String[]) al.get(0);
		String[] urlAlbumUrl=(String[]) al.get(1);
		for(int i=0;i<AlbumUrl.length;i++)
			System.out.println("AlbumUrl="+AlbumUrl[i]+"\n"+"urlAlbumUrl="+urlAlbumUrl[i]);
		
	}
	ArrayList myspaceContacts(String uname,String pwd )
	{
		String username = uname;//"parveen.aasia@gmail.com";
		String password = pwd;//"sourcen1";

		CurlGlue cg = new CurlGlue();
		String postfields = "";
		Myspace cw = null;
		cw = new Myspace();

		cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
		cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
		cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
		cg
				.setopt(
						CURL.OPT_USERAGENT,
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.1");
		cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
		cg.setopt(CURL.OPT_POST, 0);
		// takes us to the login page
		cg.setopt(CURL.OPT_URL, "http://www.myspace.com");
		cg.perform();
		postfields = null;

		
		int startIndex = sbResponse
				.indexOf("http://login.myspace.com/index.cfm?fuseaction=login.process&MyToken=");
		int endIndex = sbResponse.indexOf("\"", startIndex);

		String url = sbResponse.substring(endIndex - 36, endIndex);

		String url_s = "http://login.myspace.com/index.cfm?fuseaction=login.process&MyToken="
				+ url;
		cg.setopt(CURL.OPT_URL, url_s);
		cg.setopt(CURL.OPT_REFERER, "http://www.myspace.com");
		cg.setopt(CURL.OPT_POST, 1);

		postfields += "&email=" + username;
		postfields += "&password=" + password;

		cg.setopt(CURL.OPT_POSTFIELDS, postfields);
		sbResponse.delete(0, sbResponse.length());
		cg.perform();

		cg.setopt(CURL.OPT_URL,
				"http://home.myspace.com/index.cfm?fuseaction=user&Mytoken="
						+ url);
		sbResponse.delete(0, sbResponse.length());
		cg.perform();

		cg.setopt(CURL.OPT_URL,
				"http://messaging.myspace.com/index.cfm?fuseaction=adb&MyToken="
						+ url);
		sbResponse.delete(0, sbResponse.length());
		cg.perform();

		int start = sbResponse
				.indexOf("\"lblName\" class=\"ContactName\" title=");
		int end = sbResponse.indexOf(">", start);
		add = sbResponse.substring(start + 37, end - 1);

		String[] AlbumUrl = new String[100];

		int count = 0;
		boolean flag = true;
		int num = 1;
		
		String[] urlAlbumUrl = new String[100];
		
		
		start = sbResponse
				.indexOf("\"lblName\" class=\"ContactName\" title=");
		end=sbResponse.lastIndexOf("\"lblUserName\"  class=\"ContactName\" title=");
		while (flag) {
			num++;

			start = sbResponse
					.indexOf("\"lblName\" class=\"ContactName\" title=");

			end = sbResponse.indexOf(">", start);
			int urlstart = sbResponse.indexOf("\"lblUserName\"  class=\"ContactName\" title=");
	         int urlend = sbResponse.indexOf(">", urlstart);
			if (start < 0 )
				flag = false;
			else {
				add = sbResponse.substring(start + 37, end - 1);
				AlbumUrl[count] = add;
			}
			if(urlstart<0)
				flag = false;
			else
			{
		         String urladd = sbResponse.substring(urlstart+42, urlend-1);
		         urlAlbumUrl[count]=urladd;
			}
			count++;
			sbResponse.delete(0, urlend);
		}
		String[] dAlbumUrl=new String[count];
		String[] durlAlbumUrl=new String[count];
		for(int i=0;i<count;i++)
		{
			dAlbumUrl[i]=AlbumUrl[i];
			durlAlbumUrl[i]=urlAlbumUrl[i];
		}
		ArrayList contacts =new ArrayList();
		contacts.add(dAlbumUrl);
		contacts.add(durlAlbumUrl);
		return  contacts;
	}
}