package com.rdta.grandfather;
import java.io.BufferedReader;
 
import java.io.FileReader;
 

 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
 
import java.util.Map;
   
 
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
 
import com.rdta.eag.epharma.util.SendDHFormEmail;
 
public class CreateGrandFatherXML {

	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap(); 
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	
	
	
	private boolean isReaderStatusClosed = false;
	
	 
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public CreateGrandFatherXML(String filePath) throws Exception {
	this.filePath = filePath;
	buffReader = new BufferedReader(new FileReader(filePath));
		 
	}
	
	public CreateGrandFatherXML(String filePath,int lineNum ) throws Exception {
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
		
	     
	 
		String line = getNextLine();
		System.out.println(line);
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

	
	
	private Map prepareHeaderMap(String line) throws Exception {
		
	  
	    sourceHeaderMap=  new LinkedHashMap();
		if(line != null) {
			String[] result = line.split(",");
			System.out.println(line);
			colCount = result.length - 1;
			String token = null;
			for(int i = 0; i<result.length; i++) {
				 if(result[i]!=null){
				 token = result[i];
				 System.out.println("Header Name: "+i + token);
				 sourceHeaderMap.put(new Integer(i), token);
				 }
			     
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
	
	
	
	public List createXML(Map m,String line) throws Exception{
	     String t="";
	     String flag="false";
	     String error="";
	    if(line !=null)
	    {
	     t=t+"<GrandFatherXML>";   
	     error = "<error>";
	    String res[]=line.split(",");
	    System.out.println(res[3]);
	    
	    Map mp = m;
	    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	        
	        if(i<res.length && res[i].trim()!=null && !res[i].trim().equals("")){
	        	if(value.equals("VenInvDate")|| value.equals("ExpDate")){
	        		String ch = changeDateFormat(res[i].trim());
	        		t =t +"<"+value+">"+ch+"</"+value+">";
	              	}
	        	else if(value.equals("VenInvType")){
	        		String ch = changeInvType(res[i].trim());
	        		t =t +"<"+value+">"+ch+"</"+value+">";
	              	}
	        	else{
	            t =t +"<"+value+">"+res[i].trim()+"</"+value+">";
	        	}
	        }
	        else{
	        	flag="true";
	        	error= error+"<"+value+"/>" ;
	            t =t +"<"+value+"/>" ;
	        }
	    }
	    t =t +"</GrandFatherXML>";
	    error= error+"</error>";
	    }
	    List list = new ArrayList();
	    list.add(t);
	    list.add(flag);
	    list.add(error);
	    return list ;
	}


	private String changeInvType(String s) {
		String inv="";
		if(s.equals("INVOICE")){
			inv="InvoiceNumber";
		}

		if(s.equals("PURCHASEORDER")){
			inv="PurchaseOrderNumber";
		}

		return inv;
	}

	private String chekdaymonth(String str) {
		
		int t = str.length();
		if(t==1)
		{
		return "0"+str;	
		}
		return str;
	}
	
	
	private String changeDateFormat(String str) {
		
		String d[]=str.split("/");
		String data = d[2]+"-"+chekdaymonth(d[1])+"-"+chekdaymonth(d[0]);
		return data;
	}

	/**
	 * @param lineCount The lineCount to set.
	 */
	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	
	

public static void main(String[] args)   {
	
	
	try{
	
        String  fileUrl="C://Documents and Settings//Jagadish Pampatwar//Desktop//GrandFatherTest//Test of Grandfather data from SW.csv";
    	CreateGrandFatherXML cfc= new CreateGrandFatherXML(fileUrl);
    	
        String line=cfc.getNextLine();
        
        Map m =cfc.prepareHeaderMap(line);
        line = cfc.getNextLine();
        while(line!=null){
        List xml= cfc.createXML(m,line);
        System.out.println("GrandFather Message for line"+cfc.getLineCount()+""+ xml.get(0));
      /*  if(xml.get(1).toString().equals("true")){
        	
        	System.out.println("Error on line: "+cfc.getLineCount()+ " "+xml.get(2).toString());
        	
        }
        else{
          */
          StringBuffer b= new StringBuffer(); 
          b.append("tlsp:GrandFatherXMLFormat("+xml.get(0)+")");
          String queryResult = queryRunner.returnExecuteQueryStringsAsString(b.toString());
          System.out.println("Result"+queryResult);
          System.out.println("insert query");
          b= new StringBuffer("tlsp:InsertInitialPedigreeForGFXML("+xml.get(0)+")");
          String result = queryRunner.returnExecuteQueryStringsAsString(b.toString());
          System.out.println("insert result"+result);
          b= new StringBuffer("");
          b.append("tlsp:GrandFatherIn("+queryResult+")");
          System.out.println("query"+b.toString());
       //   List li= queryRunner.executeQuery(b.toString());
       //   System.out.println(li.size());;
       // }
      //  System.out.println("xmlString : "+cfc.getLineCount()+ " "+xml); 
        line=cfc.getNextLine();
        }
       
         
	}catch(Exception e){
		
		try {
			 
			e.printStackTrace();
			/*SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","testepharma@sourcen.com","smtp.bizmail.yahoo.com",
				      "test",e.getMessage(),"testepharma@sourcen.com","");*/
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
    } 



}
