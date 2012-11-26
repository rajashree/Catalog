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
package com.rdta.eag.epharma.activity;


import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import com.rdta.eag.epharma.util.SendDHFormEmail;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;

import com.rdta.eag.epharma.commons.xml.XMLUtil;



public class FindErrorsInTextFile {

	private String filePath;
	private String errorFileUrl;
	private Map sourceHeaderMap = new LinkedHashMap();
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;

	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");


	private boolean isReaderStatusClosed = false;

	private static Log log = LogFactory.getLog(FindErrorsInTextFile.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public FindErrorsInTextFile(String filePath,String errorFileUrl) throws Exception {
	this.filePath = filePath;
	this.errorFileUrl = errorFileUrl;
	buffReader = new BufferedReader(new FileReader(filePath));
	}
	public static Document createDomDocument() {
	    try {
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.newDocument();
	        return doc;
	    } catch (ParserConfigurationException e) {
	    }
	    return null;
	}
	public String getNextLine() throws Exception {
		String line  = null;
		if(!isReaderStatusClosed){
			line = buffReader.readLine();
			if(line != null) {
				lineCount = lineCount + 1;
			} else {
				isReaderStatusClosed = true;
				closeStream();
			}
		}

		return line;
	}

	/**
	 * Close the open file stream
	 *
	 */
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}

   public int getLineCount() {

		return lineCount;
	}

   public String addError(String RecId,int lineNo, String colName,String msg) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is "+msg+"</Message></Error>";
		return  er;
	}
   public String addError(String RecId,int lineNo, String colName) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
		return  er;
	}



	private Map prepareMapForAP() throws Exception {


	    Map ApMap=  new LinkedHashMap();
	    ApMap.put(new Integer(0), "RecordIdentifier");
	    ApMap.put(new Integer(1), "PullTimeMMDDYYhhmmss");
	    ApMap.put(new Integer(2), "QtyPulled");
	    ApMap.put(new Integer(3), "NDC");
	    ApMap.put(new Integer(4), "InvoiceNo");
	    ApMap.put(new Integer(5), "InvoiceDate");
	    ApMap.put(new Integer(6), "PONumber");
	    ApMap.put(new Integer(7), "CustomerName");
	    ApMap.put(new Integer(8), "CustomerAddressLine1");
	    ApMap.put(new Integer(9), "CustomerAddressLine2");
	    ApMap.put(new Integer(10), "CustomerCity");
	    ApMap.put(new Integer(11), "State");
	    ApMap.put(new Integer(12), "CustomerZip");
	    ApMap.put(new Integer(13), "Country");
	    ApMap.put(new Integer(14), "ShipToCustomerName");
	    ApMap.put(new Integer(15), "ShipToCustomerAddressLine1");
	    ApMap.put(new Integer(16), "ShipToCustomerAddressLine2");
	    ApMap.put(new Integer(17), "ShipToCustomerCity");
	    ApMap.put(new Integer(18), "ShipToState");
	    ApMap.put(new Integer(19), "ShipToCustomerZip");
	    ApMap.put(new Integer(20), "ShipToCountry");
	    ApMap.put(new Integer(21), "UniqueBoxID");
	    ApMap.put(new Integer(22), "BinLocation");
	    ApMap.put(new Integer(23), "CustomerDea");

		 return ApMap;

	}

	private Map prepareHeaderMapForAB() throws Exception {

		 Map Ab=  new LinkedHashMap();


		 Ab.put(new Integer(0),"RecordIdentifier");
		 Ab.put(new Integer(1),"PONo");
		 Ab.put(new Integer(2),"PODateMMDDYY");
		 Ab.put(new Integer(3),"RecDateMMDDYY");
		 Ab.put(new Integer(4),"BankTimeMMDDYYhhmmss");
		 Ab.put(new Integer(5),"NDC");
		 Ab.put(new Integer(6),"MFGName");
		 Ab.put(new Integer(7),"DrugLabel");
		 Ab.put(new Integer(8),"Form");
		 Ab.put(new Integer(9),"Strength");
		 Ab.put(new Integer(10),"ContainerSize");
		 Ab.put(new Integer(11),"LotNo");
		 Ab.put(new Integer(12),"ExpMMYY");
		 Ab.put(new Integer(13),"BinLocation");
		 Ab.put(new Integer(14),"Quantity");
		 Ab.put(new Integer(15),"MFGAddressLine1");
		 Ab.put(new Integer(16),"MFGAddressLine2");
		 Ab.put(new Integer(17),"MFGCity");
		 Ab.put(new Integer(18),"State");
		 Ab.put(new Integer(19),"MFGZip");
		 Ab.put(new Integer(20),"Country");
		 Ab.put(new Integer(21), "MFGContactName");
		 Ab.put(new Integer(22), "MFGContactTelephone");
		 Ab.put(new Integer(23), "MFGContactEMail");
		 Ab.put(new Integer(24),"MFGDea");
		 Ab.put(new Integer(25),"CaseSerialNo");



		 return Ab;

	}


	private Map prepareHeaderMapForMP() throws Exception {

		Map Ab=  new LinkedHashMap();
		Ab.put(new Integer(0),"RecordIdentifier");
		Ab.put(new Integer(1),"PONo");
		Ab.put(new Integer(2),"PODateYYYYMMDD");
		Ab.put(new Integer(3),"RecDateYYYYMMDD");
		Ab.put(new Integer(4),"NDC");
		Ab.put(new Integer(5),"MFGName");
		Ab.put(new Integer(6),"DrugLabel");
		Ab.put(new Integer(7),"Form");
		Ab.put(new Integer(8),"Strength");
		Ab.put(new Integer(9),"ContainerSize");
		Ab.put(new Integer(10),"LotNo");
		Ab.put(new Integer(11),"ExpYYYYMMDD");
		Ab.put(new Integer(12),"QuantityPulled");
		Ab.put(new Integer(13),"MFGAddressLine1");
		Ab.put(new Integer(14),"MFGAddressLine2");
		Ab.put(new Integer(15),"MFGCity");
		Ab.put(new Integer(16),"State");
		Ab.put(new Integer(17),"MFGZip");
		Ab.put(new Integer(18),"Country");
		Ab.put(new Integer(19),"MFGContactName");
		Ab.put(new Integer(20),"MFGContactTelephone");
		Ab.put(new Integer(21),"MFGContactEMail");
		Ab.put(new Integer(22),"MFGDea");
		Ab.put(new Integer(23),"InvoiceNo");
		Ab.put(new Integer(24),"InvoiceDateYYYYMMDD");
		Ab.put(new Integer(25),"PONumber");
		Ab.put(new Integer(26),"CustomerName");
		Ab.put(new Integer(27),"CustomerAddressLine1");
		Ab.put(new Integer(28),"CustomerAddressLine2");


		Ab.put(new Integer(29),"CustomerCity");
		Ab.put(new Integer(30),"CustomerState");
		Ab.put(new Integer(31),"CustomerZip");

		Ab.put(new Integer(32),"CustomerCountry");
		Ab.put(new Integer(33),"ShipToCustomerName");
		Ab.put(new Integer(34),"ShipToCustomerAddressLine1");

		Ab.put(new Integer(35),"ShipToCustomerAddressLine2");
		Ab.put(new Integer(36),"ShipToCustomerCity");
		Ab.put(new Integer(37),"ShipToState");

		Ab.put(new Integer(38),"ShipToCustomerZip");
		Ab.put(new Integer(39),"ShipToCountry");
		Ab.put(new Integer(40),"UniqueBoxID");
		Ab.put(new Integer(41),"CustomerDea");
		Ab.put(new Integer(42),"CaseSerialNo");


		return Ab;

	}

	private Map prepareHeaderMapForDeleteAndInsert() throws Exception {

		Map Ab=  new LinkedHashMap();
		Ab.put(new Integer(0),"RecordIdentifier");
		Ab.put(new Integer(1),"PONo");
		Ab.put(new Integer(2),"PODateYYYYMMDD");
		Ab.put(new Integer(3),"RecDateYYYYMMDD");
		Ab.put(new Integer(4),"NDC");
		Ab.put(new Integer(5),"MFGName");
		Ab.put(new Integer(6),"DrugLabel");
		Ab.put(new Integer(7),"Form");
		Ab.put(new Integer(8),"Strength");
		Ab.put(new Integer(9),"ContainerSize");
		Ab.put(new Integer(10),"LotNo");
		Ab.put(new Integer(11),"ExpYYYYMMDD");
		Ab.put(new Integer(12),"QuantityPulled");
		Ab.put(new Integer(13),"MFGAddressLine1");
		Ab.put(new Integer(14),"MFGAddressLine2");
		Ab.put(new Integer(15),"MFGCity");
		Ab.put(new Integer(16),"State");
		Ab.put(new Integer(17),"MFGZip");
		Ab.put(new Integer(18),"Country");
		Ab.put(new Integer(19),"MFGContactName");
		Ab.put(new Integer(20),"MFGContactTelephone");
		Ab.put(new Integer(21),"MFGContactEMail");
		Ab.put(new Integer(22),"MFGDea");
		Ab.put(new Integer(23),"InvoiceNo");
		Ab.put(new Integer(24),"InvoiceDateYYYYMMDD");
		Ab.put(new Integer(25),"PONumber");
		Ab.put(new Integer(26),"CustomerName");
		Ab.put(new Integer(27),"CustomerAddressLine1");
		Ab.put(new Integer(28),"CustomerAddressLine2");


		Ab.put(new Integer(29),"CustomerCity");
		Ab.put(new Integer(30),"CustomerState");
		Ab.put(new Integer(31),"CustomerZip");

		Ab.put(new Integer(32),"CustomerCountry");
		Ab.put(new Integer(33),"ShipToCustomerName");
		Ab.put(new Integer(34),"ShipToCustomerAddressLine1");

		Ab.put(new Integer(35),"ShipToCustomerAddressLine2");
		Ab.put(new Integer(36),"ShipToCustomerCity");
		Ab.put(new Integer(37),"ShipToState");

		Ab.put(new Integer(38),"ShipToCustomerZip");
		Ab.put(new Integer(39),"ShipToCountry");
		Ab.put(new Integer(40),"UniqueBoxID");
		Ab.put(new Integer(41),"CustomerDea");
		Ab.put(new Integer(42),"CaseSerialNo");


		return Ab;

	}

	
	
	public List createRecordsForFile() throws Exception{
	    String t ="<root>";
	   
	    StringBuffer errors=new StringBuffer();
	    errors.append("\nComplete file "+this.filePath+" was not processed and was moved to "+this.errorFileUrl+" folder. Please rectify the error and reload the file.  Please find the corresponding Error Xml \n ");
	    
	   
	    errors.append("\n<Errors>");
	    
	    int errorcount=0;
	    String line=getNextLine();
	    String pedShipXML="<root></root>";
      	Node pedShiprootNode=XMLUtil.parse(pedShipXML);
      	Document doc = createDomDocument();

      	Element element = doc.createElement("root");
        doc.appendChild(element);

      	List chekForInvoiceExists= new ArrayList();
	    while(line !=null)
	    {
       String res[]=line.split("\\|");
	    if(res[0].equalsIgnoreCase("AB"))
	    {
	   	log.info("AB");

	     if(res.length!=26){
		   		errorcount=errorcount+1;
	        	errors.append(addError("AB",getLineCount(),"Number of Columns","less"));
	        	

	   	}
	    else{

	    Map mp =prepareHeaderMapForAB();
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        res[i] = res[i].trim();
	        //Check Quantity is integer or not
	        if(i==14)
	        {
	        	try{


	        	int Quantity = Integer.parseInt(res[i]);


	        	if(Quantity<0)
	        	{
	        		errorcount=errorcount+1;
		        	errors.append(addError("AB",getLineCount(),(String)value,"not integer"));
	        	}

	        	}
	        	catch (NumberFormatException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("AB",getLineCount(),(String)value,"not Integer"));
	    		}


	        }
	        if(i==4)
	        {
	        	String date = res[i];

	    		SimpleDateFormat format = new SimpleDateFormat();

	        	try {
	    			
	    			if (date.length() == 19) {
	    				format.setLenient(false);
	    				format.applyPattern("yyyy-MM-dd");
	    			 	Date d = format.parse(date.substring(0,10));
	    				format.applyPattern("HH:mm:ss");
	    	        	Date t1 = format.parse(date.substring(11,19));
	    				
	    			}

	    			else {
	    				errorcount=errorcount+1;
	    	        	errors.append(addError("AB",getLineCount(),(String)value,"Wrong Date Time Format(Correct Format is YYYY-MM-DD HH:mm:ss)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block

	    			errorcount=errorcount+1;
    	        	errors.append(addError("AB",getLineCount(),(String)value,"Wrong Date Time Format(Correct Format is YYYY-MM-DD HH:mm:ss)"));
	    		}

	        }
	         if(i==2 || i==3 || i== 12)
	          {
	        	String date = res[i];

	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    		try {
	    			if (date.length() == 10) {
	    				format.setLenient(false);

	    				format.applyPattern("yyyy-MM-dd");
	    				Date d = format.parse(date);
	    				
	    			}

	    			else {
	    				
	    				String s ="Wrong Date Format";
	    				errorcount=errorcount+1;
	    	        	errors.append(addError("AB",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("AB",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		}



	        }
	        if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("AB",getLineCount(),(String)value));
	        }


	    }}

	    }
	    if(res[0].equalsIgnoreCase("AP"))
	    {
	    log.info("AP");

	    if(res.length!=24){
	   		errorcount=errorcount+1;
        	errors.append(addError("AP",getLineCount(),"Number of Columns","less"));


     	}
       else{
	    Map mp =prepareMapForAP();
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        res[i] = res[i].trim();
	        if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("AP",getLineCount(),(String)value));
	        }
	        if(i==2)
	        {
	        	try{


		        	int Quantity = Integer.parseInt(res[i]);


		        	if(Quantity<0)
		        	{
		        		errorcount=errorcount+1;
			        	errors.append(addError("AP",getLineCount(),(String)value,"not integer"));
		        	}

		        	}
		        	catch (NumberFormatException e) {
		    			// TODO Auto-generated catch block
		    			errorcount=errorcount+1;
		    			errors.append(addError("AP",getLineCount(),(String)value,"not Integer"));
		    		}
	        }
	        if(i==5){

	        	String date = res[i];

	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    		try {
	    			if (date.length() == 10) {
	    				format.setLenient(false);

	    				format.applyPattern("yyyy-MM-dd");
	    				Date d = format.parse(date);
	    				
	    			}

	    			else {
	    				//S//ystem.out.println("Wrong DATE ");
	    				String s ="Wrong Date Format";
	    				errorcount=errorcount+1;
	    	        	errors.append(addError("AP",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("AP",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		}
	        }

	    }
	    }
	    }
	    if(res[0].equalsIgnoreCase("MP"))
	    {
	    log.info("MP");

	    if(res.length!=43){
	   		errorcount=errorcount+1;
        	errors.append(addError("MP",getLineCount(),"Number of Columns","less"));


     	}

       else{
	     Map mp =prepareHeaderMapForMP();
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        res[i] = res[i].trim();
	        if(i==2 || i==3 || i==11 || i==24)
	        {
	        	String date = res[i];

	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    		try {
	    			if (date.length() == 10) {
	    				format.setLenient(false);

	    				format.applyPattern("yyyy-MM-dd");
	    				Date d = format.parse(date);
	    				//System.out.println("Correct DATE ");
	    			}

	    			else {
	    				System.out.println("Wrong DATE ");
	    				String s ="Wrong Date Format";
	    				errorcount=errorcount+1;
	    	        	errors.append(addError("MP",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("MP",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		}

	        }
	        if(i==12){
	        	try{


		        	int Quantity = Integer.parseInt(res[i]);


		        	if(Quantity<0)
		        	{
		        		errorcount=errorcount+1;
			        	errors.append(addError("AP",getLineCount(),(String)value,"not integer"));
		        	}

		        	}
		        	catch (NumberFormatException e) {
		    			// TODO Auto-generated catch block
		    			errorcount=errorcount+1;
		    			errors.append(addError("AP",getLineCount(),(String)value,"not Integer"));
		    		}

	        }
	         if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("MP",getLineCount(),(String)value));
	        }

	    }

	    }
	    }
	    if(res[0].equalsIgnoreCase("DELETE_PEDIGREE"))
	    {
	    log.info("DELETE_PEDIGREE");

	    if(res.length!=43){
	   		errorcount=errorcount+1;
        	errors.append(addError("DELETE_PEDIGREE",getLineCount(),"Number of Columns","less"));


     	}

       else{
	     Map mp =prepareHeaderMapForDeleteAndInsert();
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        res[i] = res[i].trim();
	        if(i==2 || i==3 || i==11 || i==24)
	        {
	        	String date = res[i];

	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	    		try {
	    			if (date.length() == 10) {
	    				format.setLenient(false);

	    				format.applyPattern("yyyy-MM-dd");
	    				Date d = format.parse(date);
	    				//System.out.println("Correct DATE ");
	    			}

	    			else {
	    				System.out.println("Wrong DATE ");
	    				String s ="Wrong Date Format";
	    				errorcount=errorcount+1;
	    	        	errors.append(addError("DELETE_PEDIGREE",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("DELETE_PEDIGREE",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		}

	        }
	        if(i==12){
	        	try{


		        	int Quantity = Integer.parseInt(res[i]);


		        	if(Quantity<0)
		        	{
		        		errorcount=errorcount+1;
			        	errors.append(addError("AP",getLineCount(),(String)value,"not integer"));
		        	}

		        	}
		        	catch (NumberFormatException e) {
		    			// TODO Auto-generated catch block
		    			errorcount=errorcount+1;
		    			errors.append(addError("AP",getLineCount(),(String)value,"not Integer"));
		    		}

	        }
	         if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("DELETE_PEDIGREE",getLineCount(),(String)value));
	        }

	    }

	    }
	    }
	    line= getNextLine();
	    }
	    errors.append("</Errors>");
	    String E=errors.toString();
	     t=t+"</root>" ;
	     List list = new ArrayList();
	     list.add(E);
	     list.add(t);
	     list.add(""+errorcount);
	     return list;

	}
	public void finalzie() {
		try {

			if(buffReader != null) {
				buffReader.close();
			}

		}catch(Exception e) {}
	}


	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String fileUrl = "C:/EPED_Delete_Files/EPED_Delete_20070131_210042.txt";
		String errorFileUrl = "C:/EPED_Delete_Error_Files/";
		FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl,errorFileUrl);
		List list= fetf.createRecordsForFile();

		int errorcount=Integer.parseInt(list.get(2).toString());
         if(errorcount>0 ){
        	 String errorxml=list.get(0).toString();
        	 log.info("Null values  in the file");

        	 log.info(errorxml);

             SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","jagadish@sourcen.com","smtp.bizmail.yahoo.com",
      	    "test",errorxml,"jagadish@sourcen.com","");

         }
	}

}
