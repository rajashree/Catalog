package com.snipl.ice.utility;

import java.sql.SQLException;
import java.util.Random;

import com.mysql.jdbc.ResultSet;
import com.snipl.ice.security.Dao;

/**
* @Author Kamalakar Challa
*   
*/
public class GeneralUtility {

	/**
	 * @param args
	 */
	Dao d;
	private static char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f',
	    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't',
	    'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7',
	    '8' };
	
    public static boolean isEmail(String mail) {
	boolean retVal = false;
	if (mail.matches("(^\\S+)@(\\w+\\.)(\\w+)(\\.\\w+)*")) {
	    retVal = true;
	}
	return retVal;
    }

    public static String getRandomString(int length) {
	String retVal = "";
	Random random = new Random();
	for (int i = 0; i < length; i++) {
	    int j = random.nextInt(chars.length);
	    retVal += chars[j] + "";
	}
	return retVal;
    }

	public String getFullCountry(String country){
		String global_country_name=null;
		
		if(country.equals("0"))
		{
			global_country_name = "";
		}
		else if(country.equals("1"))
		{
			global_country_name = "Afghanistan";
		}
		else if(country.equals("2"))
		{
			global_country_name = "Albania";
		}
		else if(country.equals("3"))
		{
			global_country_name = "Algeria";
		}
		else if(country.equals("4"))
		{
			global_country_name = "Argentina";
		}
		else if(country.equals("5"))
		{
			global_country_name = "Australia";
		}
		else if(country.equals("6"))
		{
			global_country_name = "Antarctica";
		}
		else if(country.equals("7"))
		{
			global_country_name = "Austria";
		}
		else if(country.equals("8"))
		{
			global_country_name = "Bahrain";
		}
		else if(country.equals("9"))
		{
			global_country_name = "Bangladesh";
		}
		else if(country.equals("10"))
		{
			global_country_name = "Belgium";
		}
		else if(country.equals("11"))
		{
			global_country_name = "Bhutan";
		}
		else if(country.equals("12"))
		{
			global_country_name = "Bolivia";
		}
		else if(country.equals("13"))
		{
			global_country_name = "Bosnia &  Herzegovina";
		}
		else if(country.equals("14"))
		{
			global_country_name = "Botswana";
		}
		else if(country.equals("15"))
		{
			global_country_name = "Brazil";
		}
		else if(country.equals("16"))
		{
			global_country_name = "Bulgaria";
		}
		else if(country.equals("17"))
		{
			global_country_name = "Burkina Faso";
		}
		else if(country.equals("18"))
		{
			global_country_name = "Cambodia";
		}		
		else if(country.equals("19"))
		{
			global_country_name = "Canada";
		}
		else if(country.equals("20"))
		{
			global_country_name = "Chile";
		}
		else if(country.equals("21"))
		{
			global_country_name = "China";
		}
		else if(country.equals("co"))
		{
			global_country_name = "Colombia";
		}
		else if(country.equals("23"))
		{
			global_country_name = "Congo";
		}		
		else if(country.equals("24"))
		{
			global_country_name = "Cuba";
		}
		else if(country.equals("25"))
		{
			global_country_name = "Czech Republic";
		}		
		else if(country.equals("dj"))
		{
			global_country_name = "Djibouti";
		}
		else if(country.equals("dm"))
		{
			global_country_name = "Dominica";
		}
		else if(country.equals("do"))
		{
			global_country_name = "Dominican Republic";
		}		
		else if(country.equals("tp"))
		{
			global_country_name = "East Timor";
		}
		else if(country.equals("gq"))
		{
			global_country_name = "Equatorial Guinea";
		}
		else if(country.equals("er"))
		{
			global_country_name = "Eritrea";
		}		
		else if(country.equals("ee"))
		{
			global_country_name = "Estonia";
		}
		else if(country.equals("et"))
		{
			global_country_name = "Ethiopia";
		}
		else if(country.equals("fk"))
		{
			global_country_name = "Falkland Islands";
		}		
		else if(country.equals("fo"))
		{
			global_country_name = "Faroe Islands";
		}
		else if(country.equals("fj"))
		{
			global_country_name = "Fiji";
		}
		else if(country.equals("gf"))
		{
			global_country_name = "French Guiana";
		}		
		else if(country.equals("ge"))
		{
			global_country_name = "Georgia";
		}
		else if(country.equals("gh"))
		{
			global_country_name = "ghana";
		}
		else if(country.equals("gi"))
		{
			global_country_name = "Gibraltar";
		}		
		else if(country.equals("gl"))
		{
			global_country_name = "Greenland";
		}
		else if(country.equals("gd"))
		{
			global_country_name = "Grenada";
		}
		else if(country.equals("gp"))
		{
			global_country_name = "Guadeloupe";
		}		
		else if(country.equals("gu"))
		{
			global_country_name = "Guam";
		}
		else if(country.equals("gt"))
		{
			global_country_name = "Guatemala";
		}
		else if(country.equals("gn"))
		{
			global_country_name = "Guinea";
		}
		else if(country.equals("gy"))
		{
			global_country_name = "Guyana";
		}
		else if(country.equals("ht"))
		{
			global_country_name = "Haiti";
		}
		else if(country.equals("hn"))
		{
			global_country_name = "Honduras";
		}
		else if(country.equals("hk"))
		{
			global_country_name = "hong kong";
		}
		else if(country.equals("is"))
		{
			global_country_name = "Iceland";
		}
		else if(country.equals("in"))
		{
			global_country_name = "India";
		}
		else if(country.equals("ir"))
		{
			global_country_name = "Iran";
		}
		else if(country.equals("iq"))
		{
			global_country_name = "Iraq";
		}
		else if(country.equals("jm"))
		{
			global_country_name = "Jamaica";
		}
		else if(country.equals("kz"))
		{
			global_country_name = "Kazakhstan";
		}
		else if(country.equals("ke"))
		{
			global_country_name = "Kenya";
		}
		else if(country.equals("kw"))
		{
			global_country_name = "Kuwait";
		}
		else if(country.equals("ly"))
		{
			global_country_name = "Libya";
		}
		else if(country.equals("mu"))
		{
			global_country_name = "Mauritius";
		}
		else if(country.equals("me"))
		{
			global_country_name = "Mexico";
		}
		else if(country.equals("mn"))
		{
			global_country_name = "Mongolia";
		}
		else if(country.equals("mm"))
		{
			global_country_name = "Myanmar";
		}
		else if(country.equals("na"))
		{
			global_country_name = "Namibia";
		}
		else if(country.equals("np"))
		{
			global_country_name = "Nepal";
		}
		else if(country.equals("ng"))
		{
			global_country_name = "Nigeria";
		}
		else if(country.equals("pa"))
		{
			global_country_name = "Panama";
		}	
		else if(country.equals("lk"))
		{
			global_country_name = "Sri Lanka";
		}
		else if(country.equals("sy"))
		{
			global_country_name = "Syria";
		}
		else if(country.equals("si"))
		{
			global_country_name = "singapore";
		}
		else if(country.equals("tj"))
		{
			global_country_name = "Tajikistan";
		}
		else if(country.equals("tz"))
		{
			global_country_name = "Tanzania";
		}
		else if(country.equals("ug"))
		{
			global_country_name = "Uganda";
		}
		else if(country.equals("uk"))
		{
			global_country_name = "united kingdom";
		}
		else if(country.equals("us"))
		{
			global_country_name = "United States of America";
		}
		else if(country.equals("vn"))
		{
			global_country_name = "Vietnam";
		}
		else if(country.equals("zw"))
		{
			global_country_name = "Zimbabwe";
		}
		else
		{
			global_country_name = null;
		}
		return global_country_name;	
	}
	
	
	public int getOwnCommunityCount(int userid){
	
		ResultSet rs=null;
        int count=0;
        try
		{
        	d=new Dao();
            rs=d.executeQuery("select * from community_details where owner="+userid);
            while(rs.next())
            {
            	count++;
            }
		}
        catch(SQLException e)
        {
        	System.out.println("SqlException caught"+e.getMessage());
        }
        finally
        {
	        d.close();
        } 
        return count;
	}
	
	public String getCountryCode(String country){
		String ccode=null;
		
		if(country.equals("0"))
		{
			ccode = "";
		}
		else if(country.equals("1"))
		{
			ccode = "+93";
		}
		else if(country.equals("2"))
		{
			ccode = "+355";
		}
		else if(country.equals("3"))
		{
			ccode = "+213";
		}
		else if(country.equals("4"))
		{
			ccode = "+54";
		}
		else if(country.equals("5"))
		{
			ccode = "+61";
		}
		else if(country.equals("6"))
		{
			ccode = "+672";
		}
		else if(country.equals("7"))
		{
			ccode = "+43";
		}
		else if(country.equals("8"))
		{
			ccode = "+973";
		}
		else if(country.equals("9"))
		{
			ccode = "+880";
		}
		else if(country.equals("10"))
		{
			ccode = "+32";
		}
		else if(country.equals("11"))
		{
			ccode = "+975";
		}
		else if(country.equals("12"))
		{
			ccode = "+591";
		}
		else if(country.equals("13"))
		{
			ccode = "+387";
		}
		else if(country.equals("14"))
		{
			ccode = "+267";
		}
		else if(country.equals("15"))
		{
			ccode = "+55";
		}
		else if(country.equals("16"))
		{
			ccode = "+359";
		}
		else if(country.equals("17"))
		{
			ccode = "+226";
		}
		else if(country.equals("18"))
		{
			ccode = "+855";
		}		
		else if(country.equals("19"))
		{
			ccode = "+1";
		}
		else if(country.equals("20"))
		{
			ccode = "+56";
		}
		else if(country.equals("21"))
		{
			ccode = "+86";
		}
		else if(country.equals("co"))
		{
			ccode = "+57";
		}
		else if(country.equals("23"))
		{
			ccode = "+242";
		}		
		else if(country.equals("24"))
		{
			ccode = "+53";
		}
		else if(country.equals("25"))
		{
			ccode = "+420";
		}		
		else if(country.equals("dj"))
		{
			ccode = "+253";
		}
		else if(country.equals("dm"))
		{
			ccode = "";//change
		}
		else if(country.equals("do"))
		{
			ccode = "";//change
		}		
		else if(country.equals("tp"))
		{
			ccode = "+670";
		}
		else if(country.equals("gq"))
		{
			ccode = "+240";
		}
		else if(country.equals("er"))
		{
			ccode = "+291";
		}		
		else if(country.equals("ee"))
		{
			ccode = "+372";
		}
		else if(country.equals("et"))
		{
			ccode = "+251";
		}
		else if(country.equals("fk"))
		{
			ccode = "+500";
		}		
		else if(country.equals("fo"))
		{
			ccode = "+298";
		}
		else if(country.equals("fj"))
		{
			ccode = "+679";
		}
		else if(country.equals("gf"))
		{
			ccode = "+594";
		}		
		else if(country.equals("ge"))
		{
			ccode = "+995";
		}
		else if(country.equals("gh"))
		{
			ccode = "+233";
		}
		else if(country.equals("gi"))
		{
			ccode = "+350";
		}		
		else if(country.equals("gl"))
		{
			ccode = "+299";
		}
		else if(country.equals("gd"))
		{
			ccode = "";//Change
		}
		else if(country.equals("gp"))
		{
			ccode = "+590";
		}		
		else if(country.equals("gu"))
		{
			ccode = "";//Change
		}
		else if(country.equals("gt"))
		{
			ccode = "+502";
		}
		else if(country.equals("gn"))
		{
			ccode = "+224";
		}
		else if(country.equals("gy"))
		{
			ccode = "+592";
		}
		else if(country.equals("ht"))
		{
			ccode = "+509";
		}
		else if(country.equals("hn"))
		{
			ccode = "+504";
		}
		else if(country.equals("hk"))
		{
			ccode = "+852";
		}
		else if(country.equals("is"))
		{
			ccode = "+354";
		}
		else if(country.equals("in"))
		{
			ccode = "+91";
		}
		else if(country.equals("ir"))
		{
			ccode = "+98";
		}
		else if(country.equals("iq"))
		{
			ccode = "+964";
		}
		else if(country.equals("jm"))
		{
			ccode = "";//Change
		}
		else if(country.equals("kz"))
		{
			ccode = "+7";
		}
		else if(country.equals("ke"))
		{
			ccode = "+254";
		}
		else if(country.equals("kw"))
		{
			ccode = "+965";
		}
		else if(country.equals("ly"))
		{
			ccode = "+218";
		}
		else if(country.equals("mu"))
		{
			ccode = "+230";
		}
		else if(country.equals("me"))
		{
			ccode = "+52";
		}
		else if(country.equals("mn"))
		{
			ccode = "+976";
		}
		else if(country.equals("mm"))
		{
			ccode = "+95";
		}
		else if(country.equals("na"))
		{
			ccode = "+264";
		}
		else if(country.equals("np"))
		{
			ccode = "+977";
		}
		else if(country.equals("ng"))
		{
			ccode = "+234";
		}
		else if(country.equals("pa"))
		{
			ccode = "+507";
		}
		
		else if(country.equals("lk"))
		{
			ccode = "+94";
		}
		else if(country.equals("sy"))
		{
			ccode = "+963";
		}
		else if(country.equals("si"))
		{
			ccode = "+65";
		}
		else if(country.equals("tj"))
		{
			ccode = "+992";
		}
		else if(country.equals("tz"))
		{
			ccode = "+255";
		}
		else if(country.equals("ug"))
		{
			ccode = "+256";
		}
		else if(country.equals("uk"))
		{
			ccode = "+44";
		}
		else if(country.equals("us"))
		{
			ccode = "+1";
		}
		else if(country.equals("vn"))
		{
			ccode = "+84";
		}
		else if(country.equals("zw"))
		{
			ccode = "+263";
		}
		else
		{
			ccode = null;
		}
		return ccode;	
	}
}
