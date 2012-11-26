package com.rdta.eag.epharma.dropship;

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
	private static String poNumber = "start";

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
		
		try{	
		
		String query2 = " clear collection 'tig:///ePharma_MD/DropShip' ";
		System.out.println("Query for clearing the DropShip collection : "+query2); 
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

    	
     if (dir.isDirectory()) {
        String[] children = dir.list();
        //System.out.println("NO of Files"+ children.length);
        if(children.length>1){
         for (int i=0; i<children.length; i++) {
             if(children[i].endsWith(".FLG")){
                //  System.out.println("Flag File is"+children[i]);
                 flagFileName = children[i];
                  exists=true;
                   flagFile=fileUrl+children[i];
                   
                  //flagErrorUrl=flagErrorUrl+children[i];
                  // flagProcessedUrl=flagProcessedUrl+children[i];
             }
             if(children[i].endsWith(".txt")||children[i].endsWith(".TXT")){
                 // System.out.println("Text File is"+children[i]);
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
       // System.out.println("result"+textXmlString);
        return textXmlString;
            
    }
    if(!(exists && texists)){ 
    	textXmlString = "<result><FileExists>false</FileExists></result>";
       // System.out.println("result"+textXmlString);
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

		ReadBatchFile dropShip = new ReadBatchFile(fileUrl);
		String line = dropShip.getNextLine();
		StringBuffer temp = new StringBuffer();
        result.append("<result><Id>"+id+"</Id><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile><ErrorsExists>false</ErrorsExists>");
		//Node resultantNode = null;
		while(line!=null){

			String res[]=line.split("\\|");
			if((dropShip.checkforDS(line).equalsIgnoreCase("DROP SHIP"))){
				Map ap = dropShip.prepareHeaderMapForDropShip();
				String xmlStr = dropShip.createXMLDS(ap,line);
				//System.out.println("DS : "+xmlStr);

				if(poNumber.equals("start")||res[22].equals(poNumber)){
					if(poNumber.equals("start")){
						poNumber = res[22];
					}
					//System.out.println("poNumber"+res[22]);
					temp.append(xmlStr);
					
				}else{
					if(temp.toString()!= null && !temp.toString().equals(""))
					result.append("<DropShip>"+temp.toString()+"</DropShip>");
					temp= new StringBuffer();
					temp.append(xmlStr);
				
					//System.out.println("poNumber"+res[22]);
					poNumber = res[22];
				}
			}

     		line = dropShip.getNextLine();
		}
		result.append("<DropShip>"+temp.toString()+"</DropShip></result>");
		
		
	}
		
	
 }
    textXmlString = result.toString();
    //String query2 = " clear collection 'tig:///ePharma_MD/DropShip' ";
    //System.out.println("Query for clearing the DropShip collection : "+query2); 
   // String queryres1 = queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);
    //System.out.println("result  "+queryres1);
    StringBuffer q = new StringBuffer("let $res := tig:insert-document('tig:///ePharma_MD/DropShip',"+textXmlString+")");
    q.append(" return <result><dropShipId>"+id+"</dropShipId><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile></result>");
   // System.out.println("Query for Inserting document into DropShip collection : "+q.toString());
    String queryres = queryRunner.returnExecuteQueryStringsAsStringNew(q.toString(),conn);
  //  System.out.println("result"+queryres);
    conn.commit();
    return queryres;
     
     
}
		catch(Exception ex){
			conn.rollback();
			throw ex;
		}
		finally{
		try {
			TLConnectionPool.getTLConnectionPool().returnConnection(conn);
			/*conn.logoff();
		    conn.close();*/
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

private String checkforDS(String line) throws Exception {
	// TODO Auto-generated method stub
	if(line!=null){
		String res[]=line.split("\\|");

		if( res[0]!=null && res[0].equalsIgnoreCase("DROP SHIP")){
			return "DROP SHIP";
		}
	}
	return "NA";

}

public String createXMLDS(Map m, String line) throws Exception{

	String t="";
	if(line !=null){
		
		t=t+"<DS><LineNumber>"+getLineCount()+"</LineNumber>";
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
		t =t +"</DS>";
	}
	return t ;
}

private Map prepareHeaderMapForDropShip() throws Exception {

	 Map ab=  new LinkedHashMap();
	 ab.put(new Integer(0),"PickType");
	 ab.put(new Integer(1),"PONumber");
	 ab.put(new Integer(2),"PODate");
	 ab.put(new Integer(3),"ProductCode");
	 ab.put(new Integer(4),"ManufacturerName");
	 ab.put(new Integer(5),"DrugName");
	 ab.put(new Integer(6),"Dosage");
	 ab.put(new Integer(7),"Strength");
	 ab.put(new Integer(8),"ContainerSize");
	 ab.put(new Integer(9),"LotNo");
	 ab.put(new Integer(10),"ExpDate");
	 ab.put(new Integer(11),"Quantity");
	 ab.put(new Integer(12),"ManuAdd1");
	 ab.put(new Integer(13),"ManuAdd2");
	 ab.put(new Integer(14),"ManuCity");
	 ab.put(new Integer(15),"ManuState");
	 ab.put(new Integer(16),"ManuZip");
	 ab.put(new Integer(17),"ManuCountry");
	 ab.put(new Integer(18),"ManuDEA");
	 ab.put(new Integer(19),"ManContactName");
	 ab.put(new Integer(20),"ManContactEmail");
	 ab.put(new Integer(21),"ManContactPhone");
	 ab.put(new Integer(22),"CustPONumber");
	 ab.put(new Integer(23),"CustPODate");
	 ab.put(new Integer(24),"CustContactname");
	 ab.put(new Integer(25),"Custname");
	 ab.put(new Integer(26),"CustshipAdd1");
	 ab.put(new Integer(27),"CustShipAdd2");
	 ab.put(new Integer(28),"CustShiAddCity");
	 ab.put(new Integer(29),"CustShipAddState");
	 ab.put(new Integer(30),"CustShipzip");
	 ab.put(new Integer(31),"CustShipcountry");
	 ab.put(new Integer(32),"CustDEA");
	 ab.put(new Integer(33),"CustName");
	 ab.put(new Integer(34),"CustBillAdd1");
	 ab.put(new Integer(35),"CustBillAdd2");
	 ab.put(new Integer(36),"CustBillAddCity");
	 ab.put(new Integer(37),"CustBillState");
	 ab.put(new Integer(38),"CustBillZip");
	 ab.put(new Integer(39),"CustBillCountry");


	 return ab;
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

	    	if(res[0].equalsIgnoreCase("DROP SHIP")){
	    		System.out.println("DROPSHIP");

	    		if(res.length!=40){
	    	   		errorcount=errorcount+1;
	            	errors.append(addError("DS",getLineCount(),"Number of Columns","less"));


	         	}
	           else{
	    		Map mp = prepareHeaderMapForDropShip();
	    		for (Iterator it=mp.keySet().iterator(); it.hasNext(); ) {
	    			Object key = it.next();
	    			Object value = mp.get(key);
	    			int i = Integer.parseInt(key.toString());
	    			 res[i]=res[i].trim();
	    			 if(i==11)
	    		        {
	    		        	try{


	    			        	int Quantity = Integer.parseInt(res[i]);


	    			        	if(Quantity<0)
	    			        	{
	    			        		errorcount=errorcount+1;
	    				        	errors.append(addError("DS",getLineCount(),(String)value,"not integer"));
	    			        	}

	    			        	}
	    			        	catch (NumberFormatException e) {
	    			    			// TODO Auto-generated catch block
	    			    			errorcount=errorcount+1;
	    			    			errors.append(addError("DS",getLineCount(),(String)value,"not Integer"));
	    			    		}
	    		        }
	    			 if(i==2||i==10 ||i==23){

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
	    		    	        	errors.append(addError("DS",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));
	    		    			}
	    		    		} catch (ParseException e) {
	    		    			// TODO Auto-generated catch block
	    		    			errorcount=errorcount+1;
	    		    			errors.append(addError("DS",getLineCount(),(String)value,"Wrong Date Format(Correct Format is YYYY-MM-DD)"));

	    		    		}
	    		        }


	    			if(res[i].equalsIgnoreCase("")|| res[i]==null){
	    				errorcount=errorcount+1;
	    				errors.append(addError("DS",getLineCount(),(String)value));
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
		obj.ReadInputBatchFile("<properties><emailFrom>venu.gopal@sourcen.com</emailFrom><emailTo>rajashree.meganathan@sourcen.com</emailTo><emailSubject>Exception in ePharma Morris Dickson Application</emailSubject><emailSMTP>smtp.bizmail.yahoo.com</emailSMTP><emailUserName>testepharma@sourcen.com</emailUserName><emailPassword>sniplpass</emailPassword><fileUrl>C:/EPED_DropShip_Files/</fileUrl><errorUrl>C:/EPED_DropShip_Error_Files/</errorUrl><processedUrl>C:/EPED_DropShip_Processed_Files/</processedUrl><signerid>23836883706277927282238626987996</signerid><deaNumber>RM0314790</deaNumber><sourceRoutingCode>MDW</sourceRoutingCode></properties>");		}
	
}
