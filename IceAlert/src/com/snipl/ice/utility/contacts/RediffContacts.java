package com.snipl.ice.utility.contacts;

import java.util.ArrayList;
import java.util.List;

import org.curl.CURL;
import org.curl.CurlGlue;
import org.curl.CurlWrite;
public class RediffContacts implements CurlWrite
{
	static StringBuffer sbResponse =new StringBuffer(16384);
	static StringBuffer dummysbResponse =new StringBuffer(16384);

	public int handleString(byte s[])
	{
		sbResponse.append(new String(s));
		return 0;
	}
	
	public ArrayList getRediffContacts(String uname,String passwd)
	{
		ArrayList list_al=null;
		String Redirect_url=null;
	    CurlGlue cg = new CurlGlue();
	    String postfields="";
	    Integer status=0;
	    RediffContacts cw = null;
	    
	    List con_name=new ArrayList();
	    List con_email=new ArrayList();
		
	    cw = new RediffContacts();
		
		cg.setopt(CURL.OPT_WRITEFUNCTION, cw);
		cg.setopt(CURL.OPT_COOKIEJAR, "/tmp/cookiejar-$randnum");
    	cg.setopt(CURL.OPT_COOKIEFILE, "/tmp/cookiejar-$randnum");
    	cg.setopt(CURL.OPT_USERAGENT, "YahooSeeker-Testing/v3.9 (compatible; Mozilla 4.0; MSIE 5.5; http://search.yahoo.com/)");
    	cg.setopt(CURL.OPT_FOLLOWLOCATION, 1);
    	cg.setopt(CURL.OPT_SSL_VERIFYHOST, 2);
    	cg.setopt(CURL.OPT_SSL_VERIFYPEER, 0);
    	cg.setopt(CURL.OPT_POST, 0);
		cg.setopt(CURL.OPT_URL, "http://www.rediff.com");
		cg.perform();
		sbResponse.delete(0, sbResponse.length());
        
    	cg.setopt(CURL.OPT_URL,"http://mail.rediff.com/cgi-bin/login.cgi");
    	postfields = "login=" + uname;
  		postfields += "&passwd="+passwd;
  		postfields += "&FormName="+"existing";
  		cg.setopt(CURL.OPT_POSTFIELDS,postfields);
  		 
	    cg.perform();
	    if(sbResponse.indexOf("Your login failed.")==-1)
	    {
		    int v_ind_st=sbResponse.indexOf("http://f");
		    String versionStr=sbResponse.substring(v_ind_st+11,v_ind_st+12);
	    	
		    int st_index=sbResponse.indexOf("/bn/address.cgi?"),end_index;
		    boolean flag=false;
		    if(st_index==-1)
		    {
		    	v_ind_st=sbResponse.indexOf("http://f");
			    versionStr=sbResponse.substring(v_ind_st+8,v_ind_st+9);
			    st_index=sbResponse.indexOf("/bn/toggle.cgi?");
			    end_index=sbResponse.indexOf("\"",st_index);
			    Redirect_url="http://f"+versionStr+"plus.rediff.com/"+sbResponse.substring(st_index, end_index);
			    cg.setopt(CURL.OPT_URL, Redirect_url);
			    sbResponse.delete(0,sbResponse.length());
			    cg.perform();
			    dummysbResponse.delete(0, dummysbResponse.length());
			    dummysbResponse.append(sbResponse.toString());
			    flag=true;
		    }
		    st_index=sbResponse.indexOf("/bn/address.cgi?");
		    end_index=sbResponse.indexOf("\"",st_index);
		    String contacts_url="http://f"+versionStr+"mail.rediff.com/"+sbResponse.substring(st_index, end_index);
		    boolean next_flag=false;
		    do
		    {
			    cg.setopt(CURL.OPT_URL, contacts_url);
			    sbResponse.delete(0,sbResponse.length());
			    cg.perform();
			    int add_st_ind=sbResponse.indexOf("<input type=checkbox");
			    if(add_st_ind!=-1)
			    {
				    add_st_ind=sbResponse.lastIndexOf("<TR",add_st_ind);
				    int add_lst_ind=sbResponse.lastIndexOf("<input type=checkbox");
				    //next
				    int nxt_index_st=sbResponse.lastIndexOf("/bn/address.cgi?");
				    int nxt_index_end=sbResponse.indexOf("start=", nxt_index_st);
				    if((nxt_index_end+6!=sbResponse.indexOf("\"",nxt_index_end)))
				    {
				    	if(sbResponse.lastIndexOf("Next")!=-1)
				    	{
				    	contacts_url="http://f"+versionStr+"mail.rediff.com/"+sbResponse.substring(nxt_index_st, sbResponse.indexOf("\"",nxt_index_end));
				    	next_flag=true;
				    	}
				    	else
					    	next_flag=false;
				    }
				    
				    //next
				    add_lst_ind=sbResponse.indexOf("</TR>",add_lst_ind);
				    sbResponse.replace(0, sbResponse.length(), sbResponse.substring(add_st_ind, add_lst_ind+5));
				    int con_st=sbResponse.indexOf("<TR"),con_end;
				    
				    StringBuffer dummy =new StringBuffer(16384);
				    while(con_st<sbResponse.length()&&con_st!=-1)
				    {
				    	dummy.delete(0, dummy.length());
				    	con_end=sbResponse.indexOf("</TR>",con_st);
				    	dummy.append( sbResponse.substring(con_st, con_end));
				    	int name_index_st=dummy.indexOf("</TD");
				    	name_index_st=dummy.indexOf("<TD",name_index_st);
				    	int name_index_end=dummy.indexOf("</TD",name_index_st);
				    	if(con_st!=-1&&con_end!=-1&&name_index_st!=-1&&name_index_end!=-1)
				    	{
				    		con_name.add(dummy.substring(dummy.indexOf(";",dummy.indexOf(";")+1)+1,name_index_end));
					    	
					    	name_index_st=dummy.indexOf("</TD",name_index_end);
					    	name_index_st=dummy.indexOf("<TD",name_index_st);
					    	name_index_st=dummy.indexOf("</TD",name_index_st);
					    	name_index_st=dummy.indexOf("<TD",name_index_st);
					    	name_index_end=dummy.indexOf("</TD",name_index_st);
					    	con_email.add(dummy.substring(dummy.indexOf(">",name_index_st)+1, name_index_end));
				    	}
				    	con_st=sbResponse.indexOf("</TR",con_end);
				    	con_st=sbResponse.indexOf("<TR",con_st);
				    }
			    }
			    else
			    {
			    	status=2;
			    	break;
			    }
		    }while(next_flag);
		    if(flag)
		    {
		    	st_index=dummysbResponse.indexOf("/bn/toggle.cgi?");
			    end_index=dummysbResponse.indexOf("\"",st_index);
			    Redirect_url="http://f"+versionStr+"mail.rediff.com/"+dummysbResponse.substring(st_index, end_index);
			    cg.setopt(CURL.OPT_URL, Redirect_url);
			    sbResponse.delete(0,sbResponse.length());
			    cg.perform();
		    }
	    }
	    else
	    	status=1;
	    list_al=new ArrayList();
		list_al.add(0, status.toString());
	    if(status==0)
	    {
	    	list_al.add(1, con_name);
	    	list_al.add(2, con_email);
	    }
	    return list_al;
	}
}