package com.rdta.eag.epharma.activity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.job.Job;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.SendDHFormEmail;

public class PedigreeBankForMD implements Job{

	private String filePath;
	private Map sourceHeaderMap = new LinkedHashMap(); 
	private BufferedReader buffReader;
	private int lineCount = 0;
	private int colCount = 0;
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");

	
	private boolean isReaderStatusClosed = false;
	
	private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	public String getName() {
		return "MonitorActivityJobPedRcv";
	}

	public PedigreeBankForMD(){
	}

 
	public PedigreeBankForMD(String filePath) throws Exception {
	this.filePath = filePath;
	System.out.println("*************** inside parmeter constructor *************");
	buffReader = new BufferedReader(new FileReader(filePath));
		 
	}
	
	public PedigreeBankForMD(String filePath,int lineNum ) throws Exception {
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
		System.out.println(line);
		if(line != null) {
			String[] result = line.split("\\|");
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
		 
		 Ab.put(new Integer(21),"MFGDea");
		 Ab.put(new Integer(22),"CaseSerialNo");
			     
			 
		 
		 return Ab;
		 
	}
	
	
	
	private Map prepareHeaderMap(String line) throws Exception {
		
	     
	    String temp="";
	    sourceHeaderMap=  new LinkedHashMap();
		if(line != null) {
			String[] result = line.split("\\|");
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
	private Map prepareMapForAP() throws Exception {
		
	      
	    Map ApMap=  new LinkedHashMap();
	    ApMap.put(new Integer(0), "RecordIdentifier");
	    ApMap.put(new Integer(1), "PullTimeMMDDYYhhmmss");
	    ApMap.put(new Integer(2), "QtyPulled");
	    ApMap.put(new Integer(3), "NDC");
	    ApMap.put(new Integer(4), "InvoiceNo");
	    ApMap.put(new Integer(5), "InvoiceDate");
	    ApMap.put(new Integer(6), "CustomerName");
	    ApMap.put(new Integer(7), "CustomerAddressLine1");
	    ApMap.put(new Integer(8), "CustomerAddressLine2");
	    ApMap.put(new Integer(9), "CustomerCity");
	    ApMap.put(new Integer(10), "State");
	    ApMap.put(new Integer(11), "CustomerZip");
	    ApMap.put(new Integer(12), "Country");
	    ApMap.put(new Integer(13), "UniqueBoxID");
	    ApMap.put(new Integer(14), "BinLocation");
	    ApMap.put(new Integer(15), "CustomerDea");
	    	 
		 return ApMap;
		 
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
	    String temp="<pedreceive>";
	    System.out.println("Ddddddddd");
	     for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	         
	        temp=temp+"<"+value+"/>";
	    }

	    
	    temp=temp+"</pedreceive>";
	    return temp ;
	}
	
	public String createRecords() throws Exception{
	    String t ="";
	    String line=getNextLine();
	    while(line !=null)
	    {
	     t=t+"<pedreceive>";   
	    String res[]=line.split("\\|");
	    
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
	    t =t +"</pedreceive>";
	    line= getNextLine();
	   
	     if(checkforRecordIdentifier(line))
	    	 {
	    	     
	    	     prepareHeaderMap(line);
	    	 return  t;
	    	 
	    	 }
	    
	    
	     
	    }
	    return t  ;
	}
 
	public String addError(String RecId,int lineNo, String colName) throws Exception{
		String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
		return  er;
	}
	
	public List createRecordsForFile() throws Exception{
	    String t ="<root>";
	    StringBuffer errors=new StringBuffer("<Errors>");
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
	    	t=t+"<PedRcv>";  
	    
	    Map mp =prepareHeaderMapForAB();
	    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	        Object key = it.next();
	        Object value = mp.get(key);
	        int i = Integer.parseInt(key.toString());
	         
	        if(res[i].equalsIgnoreCase("")|| res[i]==null)
	        {  errorcount=errorcount+1;
	        	errors.append(addError("AB",getLineCount(),(String)value));
	        }
	        if(i<res.length && res[i]!=null){
	            t =t +"<"+value+">"+res[i].trim()+"</"+value+">";
	        }
	        else{
	            t =t +"<"+value+"/>" ;
	        }
	        
	        
	    }
	    
	    t =t +"</PedRcv>";
	    }
	    if(res[0].equalsIgnoreCase("AP"))
	    {
	    	Map mp = prepareMapForAP();
	    	
	    	
	    	if(chekForInvoiceExists.size()>0 && chekForInvoiceExists.contains(res[4]))
	    	{
	    		 
	    		String temp = "<pedship>";
			    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
			        Object key = it.next();
			        Object value = mp.get(key);
			        int i = Integer.parseInt(key.toString());
			         
			        if(res[i].equalsIgnoreCase("")|| res[i]==null)
			        {
			        	errorcount=errorcount+1;
			        	errors.append(addError("AP",getLineCount(),(String)value));
			        }
			        if(i<res.length && res[i]!=null){
			        	temp =temp +"<"+value+">"+res[i].trim()+"</"+value+">";
			        }
			        else{
			        	temp =temp +"<"+value+"/>" ;
			        }
			        
			        
			    }
			    
			    t =temp +"</pedship>";	
			    
			    temp =temp +"</pedship>";	
			    String str=XMLUtil.convertToString(pedShiprootNode,true);
			   
			    /*String qry= "tlsp:getPedShipNodes("+str+",'"+res[4]+"')";
			    List li=queryRunner.returnExecuteQueryStrings(qry);*/
			    
			    
			    //Node invNode = XMLUtil.parse(li.get(0).toString());
			    Node pedship=XMLUtil.parse(temp);
			   // System.out.println("pedShip"+XMLUtil.convertToString(pedship));
			    System.out.println("pedShiprootNode"+str);
			    
			   Iterator it=(XMLUtil.executeQuery(pedShiprootNode,"pedshipData")).iterator();
			   
			    while(it.hasNext()){
			    	Node oldpedData = (Node)it.next();
			    	Node newpedData = oldpedData;
			    	System.out.println("Hi:::"+XMLUtil.evaluate(newpedData,"//InvoiceNo='"+res[4].trim()+"'"));
			    	if(XMLUtil.evaluate(newpedData,"//InvoiceNo='"+res[4]+"'")){
			    		XMLUtil.putNode(pedShiprootNode,"/root/pedshipData[pedship/InvoiceNo='"+res[4].trim()+"']",pedship);
			    		System.out.println("fanaa\n\n\n"+XMLUtil.convertToString(pedShiprootNode)+"\n\n\n");
			    	}
			    }
			    
			    
			
	    		
	    	}
	    	else{
	    		
	    		t=t+"<pedship>";  
			    String temp ="<pedshipData><pedship>";  
			    chekForInvoiceExists.add(res[4]);
			    for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
			        Object key = it.next();
			        Object value = mp.get(key);
			        int i = Integer.parseInt(key.toString());
			         
			        if(res[i].equalsIgnoreCase("")|| res[i]==null)
			        {
			        	errorcount=errorcount+1;
			        	errors.append(addError("AP",getLineCount(),(String)value));
			        }
			        if(i<res.length && res[i]!=null){
			        	temp =temp +"<"+value+">"+res[i].trim()+"</"+value+">";
			        	t =t +"<"+value+">"+res[i].trim()+"</"+value+">";
			        }
			        else{
			        	temp =temp +"<"+value+"/>" ;
			        	t =t +"<"+value+"/>" ;
			        }
			        
			        
			    }
			    
			    
			    t =t  +"</pedship>";	
			    
			    temp=temp +"</pedship></pedshipData>";
			    System.out.println(temp);
			    
			    Node pedDatanode=(Node) XMLUtil.parse(temp);
			    XMLUtil.putNode(pedShiprootNode,"/root",pedDatanode);
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
	     list.add(pedShiprootNode);
	     
	     System.out.println("PEDSHIP"+XMLUtil.convertToString(pedShiprootNode));
	     return list;
	
	}
 
	
	public String createXMLForAP(Map m,String subrootname,String line) throws Exception{
	    String t ="";
	    
	    
	     t=t+"<"+subrootname+">";   
	    String res[]=line.split("\\|");
	    
	    Map mp = m;
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
	    t =t +"</"+subrootname+">";
	     
	  
	    
	    return t ;
	}
 
	public String createXML(Map m,String line) throws Exception{
	     String t="";
	    if(line !=null)
	    {
	     t=t+"<PedRcv>";   
	    String res[]=line.split("\\|");
	    
	    Map mp = m;
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
	    t =t +"</PedRcv>";
	     
	    }
	    return t ;
	}

	private String checkforABorAP(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
		 String res[]=line.split("\\|");
		 
		 if( res[0]!=null && res[0].equalsIgnoreCase("AB"))
	            {
			 
			 return "AB";
	            }
		 if( res[0]!=null && res[0].equalsIgnoreCase("AP"))
         {
		 
		 return "AP";
         }
		 if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
         {
		 
		 return "RecordIdentifier";
         }
	          
		}
		return "NA"; 
		
	}
	
	

	private boolean checkforRecordIdentifier(String line) throws Exception {
		// TODO Auto-generated method stub
		if(line!=null)
		{
		 String res[]=line.split("\\|");
		 
		 		if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
	            {
			 	 return true;
	            }
	         return false;
		}
		return false; 
		
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
	
	
  

private void updatePedigreeBankForAB(String xmlStr) {
	
	
	try {
		Node n = XMLUtil.parse(xmlStr);
		String ndc=XMLUtil.getValue(n,"NDC");
		String binno=XMLUtil.getValue(n,"BinLocation");
		
		String query="tlsp:BinNumberExists_MD('"+XMLUtil.getValue(n,"NDC")+"','"+XMLUtil.getValue(n,"BinLocation")+"')";
		System.out.println("check if NDC exists"+query);
		String result=queryRunner.returnExecuteQueryStringsAsString(query);
		if(result.equalsIgnoreCase("true"))
		{    String lotinfo=createLotInfo(xmlStr);
			 String q="tlsp:insertLotInfoForSameBin_MD('"+ndc+"','"+binno+"',"+lotinfo+")";
			 queryRunner.returnExecuteQueryStringsAsString(q); 
		} 
		else{
			String bininfo=createBinInfo(xmlStr);
			 String q="tlsp:insertBinInfoNodetoPedigreeBank_MD('"+ndc+"',"+bininfo+")";
			 queryRunner.returnExecuteQueryStringsAsString(q); 
		}
	} catch (PersistanceException e) {
	 
		e.printStackTrace();
	}
	 
}

private String createBinInfo(String xmlStr) throws PersistanceException {
	
	 
	String query="tlsp:createBinInfoForAB("+xmlStr+")";
	System.out.println("create bininfo"+query);
	String binInfoNode=queryRunner.returnExecuteQueryStringsAsString(query);
	// TODO Auto-generated method stub
	return binInfoNode;
}

private String createLotInfo(String xmlStr) throws PersistanceException {
	
	String lotInfoNode="";
	String query="tlsp:createLotInfoForAB("+xmlStr+")";
	System.out.println("create bininfo"+query);
	lotInfoNode=queryRunner.returnExecuteQueryStringsAsString(query);
 
	return lotInfoNode;
}

private boolean checkNDCExist(String xmlStr) throws PersistanceException {
	
	Node n = XMLUtil.parse(xmlStr);
	String query="tlsp:ndcExists_MD('"+XMLUtil.getValue(n,"NDC")+"')";
	System.out.println("check if NDC exists"+query);
	String result=queryRunner.returnExecuteQueryStringsAsString(query);
	if(result.equalsIgnoreCase("true"))
	{
		return true;
	}
	else{
		return false;
	}
	
	 
}


private static void updatePedigreeBankForAP() {
	
	
	// TODO Auto-generated method stub
	
}

private static void createPedigreeBankDocForAB(String xmlString) throws PersistanceException {
	// TODO Auto-generated method stub
	System.out.println("createPedigreeBankDocForAB");
	String n = mapToPedigreeBankDoc(xmlString); 
	String query="tlsp:InsertDocForAB("+n+")";
	String r=queryRunner.returnExecuteQueryStringsAsString(query);
}

private static String mapToPedigreeBankDoc(String xmlString) throws PersistanceException {
	 
	 String query="tlsp:InsertPedigreeBankDocForMD("+xmlString+")";
	 System.out.println("query for "+query);
	 String result= queryRunner.returnExecuteQueryStringsAsString(query);
	 System.out.println("result of pedbank"+result);
	// TODO Auto-generated method stub
	return result;
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
private static String createflagfile() {

	TimeZone local = TimeZone.getDefault();
	
	String todayDate = sdf.format(new Date());
	System.out.println("***********Today's date is :"+todayDate); 
	
	String flagFile = "EPED"+todayDate;
	return flagFile;	
}


public void performbak() {
	
	try{
	String fname = createflagfile();
	String flagFile = fname+ "PM.FLG";
	String textFile1 = fname +"AM.TXT";
	String textFile2 = fname +"PM.TXT";
	String fileUrl = "C:/EPED_Files/";
	boolean exists = (new File(fileUrl+flagFile).exists());
    if (exists) {
        System.out.println(" File or directory exists");
        
        
    	String f1=fileUrl + textFile1; 
    	String f2=fileUrl + textFile2;
    	boolean existsFile1= (new File(f1).exists());
    	boolean existsFile2= (new File(f2).exists());
    	if(existsFile1){
    		fileUrl=f1;
    	}
    	if(existsFile2){
    		fileUrl=f2;
    	}
    	PedigreeBankForMD c= new PedigreeBankForMD(fileUrl);
    	 List list=c.createRecordsForFile();
    	 System.out.println("Errorxml"+list.get(0).toString());
    	 System.out.println("rootxml"+list.get(1).toString());
    	 String errorxml=list.get(0).toString();
         String rootxml=list.get(1).toString();
         int errorcount=Integer.parseInt(list.get(2).toString());
    	 
         if(errorcount>0){	
    			
    		 System.out.println("errorcount"+errorcount);
    	
    		 
    		 SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","jagadish@sourcen.com","smtp.bizmail.yahoo.com",
    	     "test",errorxml,"jagadish@sourcen.com","");
    	                
    		 
    		 
    	 }
    	else
    	{
    	PedigreeBankForMD cfc= new PedigreeBankForMD(fileUrl);
    	Map m=cfc.prepareHeaderMapForAB();
        String line=cfc.getNextLine();
        while(line!=null){
        
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AB"))){
        	 
               String xmlStr = cfc.createXML(m,line);
               System.out.println("AB"+xmlStr);

        	   System.out.println("AB");
        	   if(cfc.checkNDCExist(xmlStr))
        	   {
        		   cfc.updatePedigreeBankForAB(xmlStr);
        		   
        	   }
        	   else{
        		 System.out.println("INside for Create PB");
        		   cfc.createPedigreeBankDocForAB(xmlStr);   
        		   
        	   }
        	   
        	   
        }
            
        if((cfc.checkforABorAP(line).equalsIgnoreCase("AP"))){
        	
        	
        	Map Ap=cfc.prepareMapForAP();
        	String xmlStr = cfc.createXMLForAP(Ap,"pedship",line);
    	    System.out.println("AP : "+xmlStr);
    	    StringBuffer buff = new StringBuffer();
    	    buff.append("tlsp:CreateShippedPedigree_MDNew('"+xmlStr+"')");
    	    queryRunner.returnExecuteQueryStringsAsString(buff.toString());
    	   
    	   
        }
        line=cfc.getNextLine();
        }
     }
    	//String recStructure= cfc.createXMLStructure();
        //String pedrec=cfc.createRecords();
        //String pedship=cfc.createRecords("pedship");
     	//String recs=cfc.createRecords();
     	//String xmlString="<root>"+pedrec+pedship+"</root>";
     //	System.out.println(xmlString);
      	//cfc.readAndSaveFile(fileUrl);
        
    }

        
    else {
   	 System.out.println(" File or directory does not exist");
       
   }
        
	}catch(Exception e){
		
		try {
			SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","jagadish@sourcen.com","smtp.bizmail.yahoo.com",
				      "test",e.getMessage(),"jagadish@sourcen.com","");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
    } 
public  void perform()   {
	
	try{
	String fname = createflagfile();
	String flagFile = fname+ "PM.FLG";
	String textFile1 = fname +"AM.TXT";
	String textFile2 = fname +"PM.TXT";
	String fileUrl = "C:/EPED_Files/";
	String errorUrl="C:/EPED_Error_Files";
	String processedUrl="C:/EPED_Processed_Files";
	
	boolean exists = (new File(fileUrl+flagFile).exists());
    if (exists) {
        System.out.println(" File or directory exists");
        
        
    	String f1=fileUrl + textFile1; 
    	String f2=fileUrl + textFile2;
    	boolean existsFile1= (new File(f1).exists());
    	boolean existsFile2= (new File(f2).exists());
    	if(existsFile1){
    		fileUrl=f1;
    		errorUrl=errorUrl+"/"+textFile1;
    		processedUrl=processedUrl+"/"+textFile1;
    	}
    	if(existsFile2){
    		fileUrl=f2;
    		errorUrl=errorUrl+"/"+textFile2;
    		processedUrl=processedUrl+"/"+textFile2;
    	}
    	PedigreeBankForMD c= new PedigreeBankForMD(fileUrl);
   	   List list=c.createRecordsForFile();
   	   System.out.println("Errorxml"+list.get(0).toString());
   	   System.out.println("rootxml"+list.get(1).toString());
   	   String errorxml=list.get(0).toString();
        String rootxml=list.get(1).toString();
        Node pedShipxml=(Node)list.get(3);
        System.out.println("pedShipxml :"+XMLUtil.convertToString((pedShipxml),true));
         int errorcount=Integer.parseInt(list.get(2).toString());
    	 
         if(errorcount>0){	
    			
    		 System.out.println("errorcount"+errorcount);
    		 
    		 FileInputStream  fis = null;
    		 FileOutputStream fos = null;
    			try
    			{
    				fis = new FileInputStream(fileUrl);
    				fos = new FileOutputStream(errorUrl);
    				int x = fis.read();

    				while(x != -1)
    				{
    					fos.write(x);
    					x = fis.read();
    				}
    				fos.flush();
    			}
    			catch(ArrayIndexOutOfBoundsException e)
    			{
    				System.out.println("Filename incorrect");
    				 
    				return;
    			}
    			catch(FileNotFoundException e)
    			{
    				System.out.println(e.getMessage());
    				return;
    			}
    			finally
    			{
    				if(fos != null)
    					fos.close();
    				if(fis != null)
    					fis.close();
    				File f = new File(fileUrl);
    				f.delete();
    			}			
    		 SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","jagadish@sourcen.com","smtp.bizmail.yahoo.com",
    	     "test",errorxml,"jagadish@sourcen.com","sniplpass");
    	                
    		 
    		 
    	 }
    	else
    	{
    	PedigreeBankForMD cfc= new PedigreeBankForMD(fileUrl);
    	Map m=cfc.prepareHeaderMapForAB();
        String line=cfc.getNextLine();
        while(line!=null){
        
         if((cfc.checkforABorAP(line).equalsIgnoreCase("AB"))){
        	 
               String xmlStr = cfc.createXML(m,line);
        	   System.out.println("AB");
        	   if(cfc.checkNDCExist(xmlStr))
        	   {
        		   cfc.updatePedigreeBankForAB(xmlStr);
        		   
        	   }
        	   else{
        		 
        		   cfc.createPedigreeBankDocForAB(xmlStr);   
        		   
        	   }
        	   
        	   
        }
            
        /*if((cfc.checkforABorAP(line).equalsIgnoreCase("AP"))){
        	
        	
        	Map Ap=cfc.prepareMapForAP();
        	String xmlStr = cfc.createXMLForAP(Ap,"pedship",line);
    	    System.out.println("AP : "+xmlStr);
    	    StringBuffer buff = new StringBuffer();
    	    buff.append("tlsp:PEDSHIP_MD('"+xmlStr+"')");
    	    queryRunner.returnExecuteQueryStringsAsString(buff.toString());
    	   
    	   
        }*/
        line=cfc.getNextLine();
        }
        String xmlString = XMLUtil.convertToString((pedShipxml),true);
        System.out.println("xmlString : "+xmlString);
        StringBuffer buff = new StringBuffer();
        buff.append("tlsp:PEDSHIP_MD('"+xmlString+"')");
        queryRunner.returnExecuteQueryStringsAsString(buff.toString());
        FileInputStream  fis = null;
		FileOutputStream fos = null;
			try
			{
				fis = new FileInputStream(fileUrl);
				fos = new FileOutputStream(processedUrl);
				int x = fis.read();

				while(x != -1)
				{
					fos.write(x);
					x = fis.read();
				}
				fos.flush();
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Filename incorrect");
				 
				return;
			}
			catch(FileNotFoundException e)
			{
				System.out.println(e.getMessage());
				return;
			}
			finally
			{
				if(fos != null)
					fos.close();
				    
				if(fis != null)
					fis.close();
				    }			
			File f = new File(fileUrl);
			f.delete();
    	}
    	
    
    }
    else {
   	 System.out.println(" File or directory does not exist");
       
   }
        
	}catch(Exception e){
		
		try {
			 
			
			SendDHFormEmail.sendMailToSupport("jagadish@sourcen.com","jagadish@sourcen.com","smtp.bizmail.yahoo.com",
				      "test",e.getMessage(),"jagadish@sourcen.com","");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
    } 


public static void main(String args[])
	{
	    
	    PedigreeBankForMD m = new PedigreeBankForMD ();
	    m.perform();
	}



}
