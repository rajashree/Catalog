/*
* Raining Data Corp.
*
* Copyright (c) Raining Data Corp. All Rights Reserved.
*
* This software is confidential and proprietary information belonging to
* Raining Data Corp. It is the property of Raining Data Corp. and is protected
* under the Copyright Laws of the United States of America. No part of this
* software may be copied or used in any form without the prior
* written permission of Raining Data Corp.
*
*/

package com.rdta.eag.test;

import java.io.*;

/**
 * @author sanumolu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TabToXmlConverter {

      public static void main(String[] args) {
         
        try {
          FileInputStream fin = new FileInputStream("C:\\projects\\Workspace\\SouthWood\\multum\\vendor.dif");
          BufferedReader in 
           = new BufferedReader(new InputStreamReader(fin));
          
          FileOutputStream fout 
           = new FileOutputStream("C:\\projects\\Workspace\\SouthWood\\multum\\VendorList.xml");
          OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");      
          out.write("<TradingPartners>\r\n");
          String playerStats;  
          while ((playerStats = in.readLine()) != null) {
            String[] stats = splitLine(playerStats);         
            out.write("<TradingPartner>\r\n");
            out.write("    <name>"+stats[1]+"</name>\r\n");
            out.write("    <businessId>"+stats[0]+"</businessId>\r\n");
            out.write("    <deaNumber>"+stats[9]+"</deaNumber>\r\n");
            out.write("    <phone>"+stats[8]+"</phone>\r\n");
            out.write("    <address>\r\n");
            out.write("    <line1>"+stats[2]+"</line1>\r\n");
            out.write("    <line2>"+stats[3]+"</line2>\r\n");
            out.write("    <city>"+stats[4]+"</city>\r\n");
	        out.write("    <state>"+stats[5]+"</state>\r\n");
	        out.write("    <country>"+stats[7]+"</country>\r\n");
	        out.write("    <zip>"+stats[6]+"</zip>\r\n");
	        out.write("    </address>\r\n");
	        out.write("</TradingPartner>\r\n");              
          }  
          out.write("</TradingPartners>\r\n");  
          out.close();
          in.close();
        }
        catch (IOException e) {
          System.err.println(e);
        }
        

      }

      public static String[] splitLine(String playerStats) {
        
        // count the number of tabs
        int numTabs = 0;
        for (int i = 0; i < playerStats.length(); i++) {
          if (playerStats.charAt(i) == '\t') numTabs++;
        }
        int numFields = numTabs + 1;
        String[] fields = new String[numFields];
        int position = 0;
        for (int i = 0; i < numFields; i++) {
          StringBuffer field = new StringBuffer();
          while (position < playerStats.length() 
           && playerStats.charAt(position++) != '\t') {
            field.append(playerStats.charAt(position-1));
          }
          fields[i] = field.toString();
        }    
        return fields;
        
      }

}
