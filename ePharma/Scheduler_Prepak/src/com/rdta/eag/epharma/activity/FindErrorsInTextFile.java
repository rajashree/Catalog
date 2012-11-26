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
	private Map sourceHeaderMap = new LinkedHashMap();
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;

	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");


	private boolean isReaderStatusClosed = false;

	private static Log log = LogFactory.getLog(FindErrorsInTextFile.class);
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public FindErrorsInTextFile(String filePath) throws Exception {
	this.filePath = filePath;
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



// Preparing the headers for Batch file values.
	private Map prepareHeaderMapForPrepack() throws Exception {

		Map Ab=  new LinkedHashMap();
		Ab.put(new Integer(0),"PickType");
		Ab.put(new Integer(1),"Invoice");
		Ab.put(new Integer(2),"PODateYYYYMMDD");
		Ab.put(new Integer(3),"RecDateYYYYMMDD");
		Ab.put(new Integer(4),"PedigreeId");
		Ab.put(new Integer(5),"NDC");
		Ab.put(new Integer(6),"MFGName");
		Ab.put(new Integer(7),"LegendDrugName");
		Ab.put(new Integer(8),"Dosage");
		Ab.put(new Integer(9),"Strength");
		Ab.put(new Integer(10),"ContainerSize");
		Ab.put(new Integer(11),"LotNo");
		Ab.put(new Integer(12),"ExpYYYYMMDD");
		Ab.put(new Integer(13),"QuantityPulled");
		Ab.put(new Integer(14),"MfgStreet1");
		Ab.put(new Integer(15),"MfgStreet2");
		Ab.put(new Integer(16),"MfgCity");
		Ab.put(new Integer(17),"MfgState");
		Ab.put(new Integer(18),"MfgPostalCode");
		Ab.put(new Integer(19),"MfgCountry");
		Ab.put(new Integer(20),"MfgContactName");
		Ab.put(new Integer(21),"MfgContactTitle");
		Ab.put(new Integer(22),"MfgContactEMail");
		Ab.put(new Integer(23),"MfgDEANumber");
		Ab.put(new Integer(24),"LotNumberInitial");
		Ab.put(new Integer(25),"ExpiryDate");
		Ab.put(new Integer(26),"Quantity");
		Ab.put(new Integer(27),"CustomerName");
		Ab.put(new Integer(28),"CustomerAddressStreet1");
		Ab.put(new Integer(29),"CustomerAddressStreet2");
		Ab.put(new Integer(30),"CustomerCity");
		Ab.put(new Integer(31),"CustomerState");
		Ab.put(new Integer(32),"CustomerPostalCode");
		Ab.put(new Integer(33),"CustomerCountry");
		//Ab.put(new Integer(34),"ShipToCustomerName");
		Ab.put(new Integer(34),"ShipToCustomerAddressLine1");
		Ab.put(new Integer(35),"ShipToCustomerAddressLine2");
		Ab.put(new Integer(36),"ShipToCustomerCity");
		Ab.put(new Integer(37),"ShipToState");
		Ab.put(new Integer(38),"ShipToCustomerZip");
		Ab.put(new Integer(39),"ShipToCountry");
		Ab.put(new Integer(40),"CustomerDea");
		Ab.put(new Integer(41),"BatchNumber");
		 

		return Ab;

	}

	public List createRecordsForFile() throws Exception{
	    String t ="<root>";
	    StringBuffer errors=new StringBuffer();
	    errors.append("\nComplete file "+this.filePath+" was not processed and was moved to EPED_Error_Files folder. Please rectify the error and reload the file.  Please find the corresponding Error Xml \n ");
	    
	   
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
	    
	    if(res[0].equalsIgnoreCase("Normal")||res[0].equalsIgnoreCase("PrevProd")||res[0].equalsIgnoreCase("RepackProd"))
	    {
	    log.info("Prepack");

	    if(res.length!=42){
	   		errorcount=errorcount+1;
        	errors.append(addError("Prepack",getLineCount(),"Number of Columns","less"));


     	}

       else{
	     Map mp =prepareHeaderMapForPrepack();
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        res[i] = res[i].trim();
	        if(i==2 || i==3 || i==12 || i==25)
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
	    	        	errors.append(addError("Prepack",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    			}
	    		} catch (ParseException e) {
	    			// TODO Auto-generated catch block
	    			errorcount=errorcount+1;
	    			errors.append(addError("Prepack",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		}

	        }
	        if(i==13 || i==26){
	        	try{


		        	int Quantity = Integer.parseInt(res[i]);
		        	
		        	
		        	


		        	if(Quantity<0)
		        	{
		        		errorcount=errorcount+1;
			        	errors.append(addError("Prepack",getLineCount(),(String)value,"not integer"));
		        	}

		        	}
		        	catch (NumberFormatException e) {
		    			// TODO Auto-generated catch block
		    			errorcount=errorcount+1;
		    			errors.append(addError("Prepack",getLineCount(),(String)value,"not Integer"));
		    		}

	        }
	         if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("Prepack",getLineCount(),(String)value));
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

		String fileUrl = "C:/EPED_Files/TestCase1.TXT";
		FindErrorsInTextFile fetf= new FindErrorsInTextFile(fileUrl);
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
