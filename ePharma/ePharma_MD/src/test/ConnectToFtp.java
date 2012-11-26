/********************************************************************************
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
*********************************************************************************/
package test;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import com.jscape.inet.ftp.*; 

/**
 * @author Santosh Subramanya
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ConnectToFtp {
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	
	public static void main(String args[]) throws FtpException{	
		
		Ftp ftp = new Ftp("66.43.93.187", "rainingdata", "1nd1aB3$t");
		
		try{
			ftp.connect();
		}catch(Exception e){
			System.out.println("Unable to connect to the ftp !!!!......");
		}
		
		File file = new File("C:/");
		ftp.setLocalDir(file);
		
		TimeZone local = TimeZone.getDefault();
		
		String todayDate = sdf.format(new Date());
		System.out.println("***********Today's date is :"+todayDate);
		
		// This is the flag file 
		String flagFile = "EPED"+todayDate+".FLG";		
		System.out.println("********flagFile is :"+flagFile);
				
		String dir = ftp.getDir();
		System.out.println("The directory is :"+dir);
				
		String list = ftp.getDirListingAsString();
		System.out.println("The directory listing is :"+list);
		
		Enumeration e = ftp.getDirListing("Morris and Dickson files");		
		while(e.hasMoreElements()){		
			String check = (String) e.nextElement().toString();			
			//System.out.println("The file name is :"+check.indexOf(":"));
			System.out.println("The file name is :"+check.substring(check.indexOf(":")+4));
			if(check.substring(check.indexOf(":")+4).equals(flagFile)){
				System.out.println("The flag file exists............");
				System.out.println("Trying to download files from the ftp folder........");
				ftp.downloadDir("Morris and Dickson files");
				System.out.println("Download completed successfully......");				
			}
		}
		
		System.out.println("Disconnecting from ftp.......");		
		ftp.disconnect();
		System.out.println("Disconnected.......");
	}
}
