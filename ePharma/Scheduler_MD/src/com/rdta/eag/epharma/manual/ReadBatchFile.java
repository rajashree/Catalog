package com.rdta.eag.epharma.manual;

import java.io.BufferedReader;
import java.io.File;
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

 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rdta.eag.epharma.commons.CommonUtil;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;

 
 

public class ReadBatchFile {
	private String filePath;
	private static BufferedReader buffReader;
	static Connection conn = null;
	
	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	//private static Log log = LogFactory.getLog(ReadBatchFile.class);
	private boolean isReaderStatusClosed = false;
	private int lineCount = 0;
	private static String invoice = "start";

	static String currentLine =null;
	
	public ReadBatchFile(String filePath) throws Exception {
		this.filePath = filePath;
		buffReader = new BufferedReader(new FileReader(filePath));
	}

	public ReadBatchFile() {
		// TODO Auto-generated constructor stub
	}

	// Method to close the stream
	public void closeStream()  {
		try {
			isReaderStatusClosed = true;
			if(buffReader != null) {
				buffReader.close();
			}
		} catch(Exception e) { }
	}

	/**
	 * Returns number of lines read as of now from the input stream.
	 *
	 * @return int
	 */
	public int getLineCount() {

		return lineCount;
	}
	
	public String ReadInputBatchFile(String XmlString) throws Exception{
		
		conn = TLConnectionPool.getTLConnectionPool().borrowConnection();
		conn.setAutoCommit(false);
			
		
	String query2 = " clear collection 'tig:///ePharma_MD/ManualUseCase' ";
	System.out.println("Query for clearing the ManualUseCase collection : "+query2); 
	String queryres1 = queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);
	System.out.println("result  "+queryres1);
		
		
    Node properties = XMLUtil.parse(XmlString);
    //System.out.println(" xxx "+XmlString);
	String fileUrl= XMLUtil.getValue(properties,"fileUrl");
	String flagFile =  "";
	String	flagErrorUrl ="";
	String	flagProcessedUrl=""; 
	String flagFileName = "";
	String textFileName = "";
	
	String errorUrl =XMLUtil.getValue(properties,"errorUrl");
	String processedUrl=XMLUtil.getValue(properties,"processedUrl");;
	String	textFile1 ="";
	String fileName ="";
	boolean exists = false;
	boolean texists = false;
	StringBuffer result = new StringBuffer();
	String textXmlString =new String();
	String id="";
    File dir = new File(fileUrl);
    
    try{
    	
     if (dir.isDirectory()) {
        String[] children = dir.list();
        //System.out.println("NO of Files"+ children.length);
        if(children.length>1){
         for (int i=0; i<children.length; i++) {
             if(children[i].endsWith(".FLG")){
                 // System.out.println("Flag File is"+children[i]);
                 flagFileName = children[i];
                  exists=true;
                   flagFile=fileUrl+children[i];
                   
                  //flagErrorUrl=flagErrorUrl+children[i];
                  // flagProcessedUrl=flagProcessedUrl+children[i];
             }
             if(children[i].endsWith(".txt")||children[i].endsWith(".TXT")){
                  //System.out.println("Text File is"+children[i]);
                 textFileName=children[i];
                  textFile1=children[i];
                 
                  fileName = textFile1;
                  fileUrl = fileUrl + textFile1;
                  texists=true;
             }
        }
         }else{
        textXmlString = "<result><FileExists>false</FileExists></result>";
        //System.out.println("result"+textXmlString);
        return textXmlString;
         }
    } else {
    	
    	
    	
    	textXmlString = "<result><FileExists>false</FileExists></result>";
        //System.out.println("result"+textXmlString);
        return textXmlString;
            
    }
    if(!(exists && texists)){ 
    	textXmlString = "<result><FileExists>false</FileExists></result>";
        //System.out.println("result"+textXmlString);
        return textXmlString;
    }
    
    else{
    String IdQuery = "import module namespace util = 'xquery:modules:util'; util:create-uuid()";
     id= queryRunner.returnExecuteQueryStringsAsStringNew(IdQuery,conn);
   	errorUrl=errorUrl+"/"+textFile1;
 	processedUrl=processedUrl+"/"+textFile1;
 	ReadBatchFile bfile=new  ReadBatchFile(fileUrl);
 	List list = bfile.createRecordsForFile();
 	//System.out.println("Errorxml"+list.get(0).toString());
 	//System.out.println("rootxml"+list.get(1).toString());
	String errorxml=list.get(0).toString();
	String rootxml=list.get(1).toString();
	int errorcount=Integer.parseInt(list.get(2).toString());
	//int errorcount=0;
	
	if(errorcount>0){
		
			//System.out.println("errorcount"+errorcount);
			//System.out.println("Flag URl"+flagFile);
			
			result.append("<result><Id>"+id+"</Id><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><ErrorFile>"+textFileName+"</ErrorFile><ErrorsExists>true</ErrorsExists>"+errorxml+"</result>");
			
		/* 	ds.copyfile(fileUrl,errorUrl);
		 	ds.copyfile(flagFile,flagErrorUrl);;
			File f= new File(fileUrl);
			File flag = new File(flagFile);
			f.delete();
			flag.delete();

			SendDHFormEmail.sendMailToSupport(emailFrom, emailTo , emailSMTP,
					emailSubject, errorxml, emailUserName, emailPassword);*/
			 
			
		}
	
	else{

		ReadBatchFile manual = new ReadBatchFile(fileUrl);
		String line = manual.getNextLine();
		StringBuffer temp = new StringBuffer();
        result.append("<result><Id>"+id+"</Id><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile><ErrorsExists>false</ErrorsExists>");
		//Node resultantNode = null;
		while(line!=null){

			String res[]=line.split("\\|");
			if((manual.checkforMP(line).equalsIgnoreCase("MP"))){
				Map ap = manual.prepareHeaderMapForMP();
				String xmlStr = manual.createXMLManual(ap,line);
				//System.out.println("MP : "+xmlStr);

				if(invoice.equals("start")||res[23].equals(invoice)){
					if(invoice.equals("start")){
						invoice = res[23];
					}
					//System.out.println("invoice"+res[23]);
					temp.append(xmlStr);
					
				}else{
					if(temp.toString()!= null && !temp.toString().equals(""))
					result.append("<Manual>"+temp.toString()+"</Manual>");
					temp= new StringBuffer();
					temp.append(xmlStr);
				
					//System.out.println("invoice"+res[23]);
					invoice = res[23];
				}
			}

     		line = manual.getNextLine();
		}
		result.append("<Manual>"+temp.toString()+"</Manual></result>");
		
		
	}
		
	
 }
    textXmlString = result.toString();
    //String query2 = " clear collection 'tig:///ePharma_MD/DropShip' ";
    //System.out.println("Query for clearing the DropShip collection : "+query2); 
   // String queryres1 = queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);
    //System.out.println("result  "+queryres1);
    StringBuffer q = new StringBuffer("let $res := tig:insert-document('tig:///ePharma_MD/ManualUseCase',"+textXmlString+")");
    q.append(" return <result><manualUseCaseId>"+id+"</manualUseCaseId><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile></result>");
   // System.out.println("Query for Inserting document into ManualUseCase collection : "+q.toString());
    String queryres = queryRunner.returnExecuteQueryStringsAsStringNew(q.toString(),conn);
    //System.out.println("result"+queryres);
    conn.commit();
    return queryres;
     
     
}
    catch(Exception e){
    	
    	conn.rollback();
    	throw e;
    	
    }
		finally{
		try {
			TLConnectionPool.getTLConnectionPool().returnConnection(conn);
		
	}catch(Exception e)
	{}
}
}
	
public String getNextLine() throws Exception {
		
		String line  = null;
		if(!isReaderStatusClosed){
			line = buffReader.readLine();
			currentLine= line;
			if(line != null) {
				lineCount = lineCount + 1;
			} else {
				isReaderStatusClosed = true;
				closeStream();
			}
		}
		
		return line;
	}


public static Document createDomDocument(){
    try {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();
        return doc;
    } catch (ParserConfigurationException e) {
    }
    return null;
} 

public String addError(String RecId,int lineNo, String colName,String msg) throws Exception{
	String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is "+msg+"</Message></Error>";
	return  er;
}
public String addError(String RecId,int lineNo, String colName) throws Exception{
	String er="<Error><RecordIdentifier>"+RecId+"</RecordIdentifier><Message>"+colName+" at line "+lineNo+" is null</Message></Error>";
	return  er;
}
//Method to check for MP RecordIdentifier.
private String checkforMP(String line) throws Exception {
	// TODO Auto-generated method stub
	if(line!=null)
	{
		String res[]=line.split("\\|");

		if( res[0]!=null && res[0].equalsIgnoreCase("MP"))
		{
			return "MP";
		}

		if( res[0]!=null && res[0].equalsIgnoreCase("RecordIdentifier"))
		{
			return "RecordIdentifier";
		}

	}
	return "NA";

} 

public String createXMLManual(Map m, String line) throws Exception{

	String t="";
	if(line !=null){
		
		t=t+"<MP><LineNumber>"+getLineCount()+"</LineNumber>";
		//System.out.println("line Count "+getLineCount());
		
		String res[]=line.split("\\|");

		Map mp = m;
		for (Iterator it = mp.keySet().iterator(); it.hasNext(); ) {
			Object key = it.next();
			Object value = mp.get(key);
			int i = Integer.parseInt(key.toString());

			if(i<res.length && res[i]!=null){
				t =t +"<"+value+">"+res[i]+"</"+value+">";
			}else{
				t =t +"<"+value+"/>" ;
			}
		}
		t =t +"</MP>";
	}
	return t ;
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
         while(line !=null){
	    	String res[]=line.split("\\|");

	    	if(res[0].equalsIgnoreCase("MP")){
	    		//System.out.println("MP");

	    		if(res.length!=43){
	    	   		errorcount=errorcount+1;
	            	errors.append(addError("MP",getLineCount(),"Number of Columns","less"));


	         	}
	           else{
	    		Map mp = prepareHeaderMapForMP();
	    		for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	    			Object key = it.next();
	    			Object value = mp.get(key);
	    			int i = Integer.parseInt(key.toString());
	    			 res[i]=res[i].trim();
	    			 if(i==12)
	    		        {
	    		        	try{


	    			        	int Quantity = Integer.parseInt(res[i]);


	    			        	if(Quantity<0)
	    			        	{
	    			        		errorcount=errorcount+1;
	    				        	errors.append(addError("MP",getLineCount(),(String)value,"not integer"));
	    			        	}

	    			        	}
	    			        	catch (NumberFormatException e) {
	    			    			// TODO Auto-generated catch block
	    			    			errorcount=errorcount+1;
	    			    			errors.append(addError("MP",getLineCount(),(String)value,"not Integer"));
	    			    		}
	    		        }
	    			 if(i==2 || i==3 || i==11 || i==24){

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
	    		    				//System.out.println("Wrong DATE ");
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


	    			if(res[i].equalsIgnoreCase("")|| res[i]==null){
	    				errorcount=errorcount+1;
	    				errors.append(addError("MP",getLineCount(),(String)value));
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
	public static void main(String args[]) throws Exception{
		
		ReadBatchFile obj = new ReadBatchFile();
		obj.ReadInputBatchFile("<properties><emailFrom>venu.gopal@sourcen.com</emailFrom><emailTo>rajashree.meganathan@sourcen.com</emailTo><emailSubject>Exception in ePharma Morris Dickson Application</emailSubject><emailSMTP>smtp.bizmail.yahoo.com</emailSMTP><emailUserName>testepharma@sourcen.com</emailUserName><emailPassword>sniplpass</emailPassword><fileUrl>C:/EPED_Manual_Files/</fileUrl><errorUrl>C:/EPED_Manual_Error_Files/</errorUrl><processedUrl>C:/EPED_Manual_Processed_Files/</processedUrl><signerid>23836883706277927282238626987996</signerid><deaNumber>RM0314790</deaNumber><sourceRoutingCode>MDW</sourceRoutingCode></properties>");		}
	
}
