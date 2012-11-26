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

 
package com.rdta.catalog;
import java.io.*;
import java.util.*;
import java.util.List;
import org.w3c.dom.Node;

import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
 


public class CVSTOXMLConvertor {
		
	
	
	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap(); 
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	

	
	private boolean isReaderStatusClosed = false;
	
	 
	  private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public CVSTOXMLConvertor(String filePath) throws Exception {
		this.filePath = filePath;
		 
		buffReader = new BufferedReader(new FileReader(filePath));
		 

 
		 
	}
	
	public CVSTOXMLConvertor(String filePath,int lineNum ) throws Exception {
		this(filePath);
		skipNumberOfLines(lineNum);
	}
	
	
	private void skipNumberOfLines(int lineNum) throws Exception {
		
		while(lineCount < lineNum) {
			getNextLine();
		}
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
	public void LineDecrement() throws Exception {
		lineCount=lineCount-1;
	}
	
	private Map prepareHeaderMap() throws Exception {
		
	     
	    String temp="";
		String line = getNextLine();
		if(line != null) {
			String[] result = line.split(",");
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				token = result[i];
				System.out.println("Header Name: "+i + token);
				sourceHeaderMap.put(new Integer(i), token);
			     
			} 
		} 
		 return sourceHeaderMap;
		 
	}
	
	/**
	 * Returns number of columns found in input file. 
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return colCount;
	}
	
	/**
	 * Returns number of lines read as of now from the input stream.
	 * 
	 * @return int
	 */
	public int getLineCount() {
		
		return lineCount;
	}
	
	/**
	 * Returns Header information.
	 * Which contains Column Index as Key and Value has DataElementNode
	 * 
	 * @return
	 */
	public Map getHeaderMap() {
	
		return sourceHeaderMap;
	}
	
	
	public boolean isStreamClosed() {
		return isReaderStatusClosed;
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
	
	
	public void finalzie() {
		try {
			
			if(buffReader != null) {
				buffReader.close();
			}
			
		}catch(Exception e) {}
	}
	
	public String createXMLStructure(){
	    Map mp= getHeaderMap();
	    String temp="<recordStructure>";
	    System.out.println("Ddddddddd");
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	         
	        temp=temp+"<"+value+"/>";
	    }

	    
	    temp=temp+"</recordStructure>";
	    return temp ;
	}
	
	public String createRecords() throws Exception{
	    String t ="";
	    String line=getNextLine();
	    while(line !=null)
	    {
	     t=t+"<recordStructure>";   
	    String res[]=line.split(",");
	    
	    Map mp = sourceHeaderMap;
	    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	         
	        if(i<res.length && res[i]!=null){
	            t =t +"<"+value+">"+res[i]+"</"+value+">";
	        }
	        else{
	            t =t +"<"+value+"/>" ;
	        }
	        
	        
	    }
	    t =t +"</recordStructure>";
	    line= getNextLine();
	     
	    }
	    return t  ;
	}
 

	
	
	public void readAndSaveFile(String url) throws IOException, PersistanceException{
	    String fileUrl = url;
	    BufferedReader buffer = new BufferedReader(new FileReader(fileUrl));
	    String data = buffer.readLine();
	    System.out.println("vvvvv"+data);
	    FileInputStream fis = null;
		try
		{
		fis = new FileInputStream(fileUrl);
	    System.out.println(fis.getClass().getName());
	    StringBuffer b= new StringBuffer("declare binary-encoding none;");
	    b.append("let $binData := binary{$1}");
	    b.append("let $i := <Doc><csv>{$binData}</csv></Doc> return ");
	    b.append("tig:insert-document('tig:///ePharma_MD/PedigreeBank',$i)");
        queryRunner.executeQueryWithStream(b.toString(),fis);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("File Name Missing");
			return;
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			return;
		}
		finally
		{
			if(fis != null)
				fis.close();
		}
	    
	    
	}
	 
	
	/**
	 * @param lineCount The lineCount to set.
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
   public static void main(String[] args)  throws Exception {
		
		String fileUrl = "C:\\Documents and Settings\\Jagadish Pampatwar\\Desktop\\MorrisandDickson\\SampleBank.csv";
		
		CVSTOXMLConvertor cfc= new CVSTOXMLConvertor(fileUrl);
	    Map m=cfc.prepareHeaderMap();
		String recStructure= cfc.createXMLStructure();
	 	String recs=cfc.createRecords();
	 	String xmlString="<root>"+recs+"</root>";
	  	cfc.readAndSaveFile(fileUrl);
	    
	}
}

