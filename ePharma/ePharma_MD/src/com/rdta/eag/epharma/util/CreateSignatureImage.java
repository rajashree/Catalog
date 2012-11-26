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

 

 /*=======================================================================
 *
 * Filename		CreateSignatureImage.java     
 *
 * Raining Data Corp.
 * 
 * Copyright (c) Raining Data Corp. All Rights Reserved.
 * 
 * This software is confidential and proprietary information belonging to
 * Raining Data Corp. It is the property of Raining Data Corp. and is protected
 * under the Copyright Laws of the United States of America. No part of this
 * software may be copied or used in any form without the prior written 
 * permission of Raining Data Corp.
 *	
 *========================================================================*/

package com.rdta.eag.epharma.util;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rdta.Admin.Utility.Helper;
import com.rdta.Admin.servlet.RepConstants;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;

/**
 *
 *	@author     <a href="mailto:developer_email">Johnson Joseph</a>
 *
 *	Date:		Jun 1, 2006 3:22:10 PM
 *     
 *	@version    $Id :$
 *
 *	@since   	REL1.0
 */

	
public class CreateSignatureImage {
	
	/** The commons <code>logger</code> instance for this class. */
	private final static Log logger = LogFactory.getLog(CreateSignatureImage.class);

	
	/**
	 * Creates the image in \xsl folder for the given userName and eMail Id.
	 */ 

		public static void getSignatureImage(String strName,String strSignEmail)throws Exception{	
		try{
			logger.info("Inside createSignatureImage method..");
 			Helper helper =new Helper();
 			Connection Conn = helper.ConnectTL();
 			Statement Stmt = helper.getStatement(Conn);
 			StringBuffer bfr = new StringBuffer();
 			
 			bfr.append("let $userId := (for $i in collection('tig:///EAGRFID/SysUsers')");
 			bfr.append(" where $i/User/FirstName='"+strName+"'and $i/User/Email='"+strSignEmail+"'");
 			bfr.append(" return data($i/User/UserID))");
 			bfr.append("for $k in collection('tig:///EAGRFID/UserSign')/User");
 			bfr.append(" where $k/UserID = $userId");
 			bfr.append(" return $k/UserSign/binary()");
 			logger.info("Query :"+bfr.toString());
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 			  			
 			File pictFile =  new File(RepConstants.APPL_PATH+"xsl\\Signature.jpeg");			
			if(pictFile.exists()){
				pictFile.delete();
				logger.info("Deleted the exitsing image file");							
			}
 			if (rslt != null) {																					 				
 				FileOutputStream fos = new FileOutputStream(pictFile);			        			       
 				fos.write(rslt);
 				fos.flush();
 				
 			} 					
 			Thread.sleep(1750);
			}catch(Exception ie) {
				logger.error("Error in  getSignatureImage() " + ie);			 
			}		
		//return status;
	}
}

