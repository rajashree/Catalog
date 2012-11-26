package com.rdta.eag.epharma.repackageusecase;

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

import com.rdta.eag.epharma.activity.InsertDocToDB;
import com.rdta.eag.epharma.activity.RepackUseCaseForPrePack;
import com.rdta.eag.epharma.commons.CommonUtil;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.persistence.pool.TLConnectionPool;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.eag.epharma.pedexchange.PedEnvTransmissionHandler;
import com.rdta.eag.epharma.util.SendDHFormEmail;
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
			   
		String query2 = " clear collection 'tig:///ePharma/Repackage' ";
	    System.out.println("Query for clearing the Repackage collection : "+query2); 
	    String queryres1 = queryRunner.returnExecuteQueryStringsAsStringNew(query2,conn);
	    System.out.println("result  "+queryres1);
		
		
    Node properties = XMLUtil.parse(XmlString);
   System.out.println(" xxx "+XmlString);
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
    String invoice = "start";
    
 
    	
     if (dir.isDirectory()) {
        String[] children = dir.list();
        System.out.println("NO of Files"+ children.length);
        if(children.length>1){
         for (int i=0; i<children.length; i++) {
             if(children[i].endsWith(".FLG")){
                  System.out.println("Flag File is"+children[i]);
                 flagFileName = children[i];
                  exists=true;
                   flagFile=fileUrl+children[i];
                   
                  //flagErrorUrl=flagErrorUrl+children[i];
                  // flagProcessedUrl=flagProcessedUrl+children[i];
             }
             if(children[i].endsWith(".txt")||children[i].endsWith(".TXT")){
                  System.out.println("Text File is"+children[i]);
                 textFileName=children[i];
                  textFile1=children[i];
                 
                  fileName = textFile1;
                  fileUrl = fileUrl + textFile1;
                  texists=true;
             }
        }
         }else{
        textXmlString = "<Result><FileExists>false</FileExists></Result>";
        System.out.println("result"+textXmlString);
        return textXmlString;
         }
    } else {
    	
    	
    	
    	textXmlString = "<Result><FileExists>false</FileExists></Result>";
        System.out.println("Result"+textXmlString);
        return textXmlString;
            
    }
    if(!(exists && texists)){ 
    	textXmlString = "<Result><FileExists>false</FileExists></Result>";
        System.out.println("result"+textXmlString);
        return textXmlString;
    }
    
    else{
    String IdQuery = "import module namespace util = 'xquery:modules:util'; util:create-uuid()";
     id= queryRunner.returnExecuteQueryStringsAsStringNew(IdQuery,conn);
   	errorUrl=errorUrl+"/"+textFile1;
 	processedUrl=processedUrl+"/"+textFile1;
 	ReadBatchFile bfile=new  ReadBatchFile(fileUrl);
 	
 	List list = bfile.createRecordsForFile();
 	System.out.println("Errorxml"+list.get(0).toString());
 	System.out.println("rootxml"+list.get(1).toString());
	String errorxml=list.get(0).toString();
	String rootxml=list.get(1).toString();
	int errorcount=Integer.parseInt(list.get(2).toString());
	//int errorcount=0;
	
	if(errorcount>0){
		
			System.out.println("errorcount"+errorcount);
			System.out.println("Flag URl"+flagFile);
			
			result.append("<Result><Id>"+id+"</Id><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><ErrorFile>"+textFileName+"</ErrorFile><ErrorsExists>true</ErrorsExists>"+errorxml+"</Result>");
			
		
			
		}
	
	else{
		
		
		ReadBatchFile cfc = new ReadBatchFile(fileUrl);
		String line = cfc.getNextLine();
		
        result.append("<Result><Id>"+id+"</Id><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile><ErrorsExists>false</ErrorsExists>");
        
		StringBuffer temp = new StringBuffer("<result>");
		Map ap= cfc.prepareHeaderMapForPrepack();
		String xmlStr =null;
		int i=0;
		System.out.println("*** line before while loop *** :"+line);
		while(line!=null){
			
			if((cfc.checkforRepack(line).equalsIgnoreCase("Repack"))){
				
				System.out.println("Repack"+cfc.getLineCount());
			  System.out.println("Repack"+cfc.getLineCount());
				xmlStr = cfc.createXMLForAP(ap,"pedship",line);
				System.out.println("Repack");
				String res[]=line.split("\\|");
				System.out.println("INvoice"+res[1]);

				System.out.println("** iNvoice **"+invoice);
				if(invoice.equals("start"))
				{
					
					invoice=res[1];
					temp.append("<root>");
					temp.append("<PE>");
					temp.append("<repack>");
					temp.append(xmlStr);
				}
				else if(res[1].equals(invoice)){
					
					temp.append("</repack><repack>");
					temp.append(xmlStr);
				}
				else if(!res[1].equals(invoice) && !invoice.equals("start")){
					invoice =res[1];
					temp.append("</repack></PE></root>");
					temp.append(" <root><PE><repack>");
					temp.append(xmlStr);
				}
			
					
			}
			if((cfc.checkforRepack(line).equalsIgnoreCase("PrevProd"))){
				System.out.println("PrevProd"+cfc.getLineCount());
				  System.out.println("PrevProd"+cfc.getLineCount());
				xmlStr = cfc.createXMLForAP(ap,"pedship",line);
				System.out.println("PrevProd");
				temp.append(xmlStr);
      		}
			line=cfc.getNextLine();
		}
		temp.append("</repack></PE></root>");
		StringBuffer resxml=temp.append("</result>");
		System.out.println("temp"+temp);
		System.out.println("Temporary "+temp.toString());
		result.append("<Repackage>"+temp.toString()+"</Repackage></Result>");
		
		
	}
		
    }

    textXmlString = result.toString();
    System.out.println("Result *********"+textXmlString);
    
   StringBuffer q = new StringBuffer("let $res := tig:insert-document('tig:///ePharma/Repackage',"+textXmlString+")");
    q.append(" return <result><repackageId>"+id+"</repackageId><FileExists>true</FileExists><FlagFile>"+flagFileName+"</FlagFile><TextFile>"+textFileName+"</TextFile></result>");
    System.out.println("Query for Inserting document into Repackage collection : "+q.toString());
    String queryres = queryRunner.returnExecuteQueryStringsAsStringNew(q.toString(),conn);
    System.out.println("result"+queryres);
    conn.commit();
   
    return queryres;
        
    }catch(Exception ex){
		conn.rollback();
		throw ex;
	}
		finally{
		try {
			conn.rollback();
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

private String checkforRepack(String line) throws Exception {
	// TODO Auto-generated method stub
	if(line!=null)
	{
		String res[]=line.split("\\|");

		if( res[0]!=null && res[0].equalsIgnoreCase("Repack"))
		{
			return "Repack";
		}
		if( res[0]!=null && res[0].equalsIgnoreCase("PrevProd"))
		{
			return "PrevProd";
		}
		 

	}
	return "NA";

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

	return (t.toString()).replaceAll("&","&amp;") ;
}
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
	Ab.put(new Integer(34),"ShipToCustomerName");
	Ab.put(new Integer(35),"ShipToCustomerAddressLine1");
	Ab.put(new Integer(36),"ShipToCustomerAddressLine2");
	Ab.put(new Integer(37),"ShipToCustomerCity");
	Ab.put(new Integer(38),"ShipToState");
	Ab.put(new Integer(39),"ShipToCustomerZip");
	Ab.put(new Integer(40),"ShipToCountry");
	Ab.put(new Integer(41),"CustomerDea");
	Ab.put(new Integer(42),"BatchNumber");
	 

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
    
    if(res[0].equalsIgnoreCase("Normal")||res[0].equalsIgnoreCase("PrevProd")||res[0].equalsIgnoreCase("Repack"))
    {
    System.out.println("Prepack");

    if(res.length!=43){
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
        if(i==14 || i==26){
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




	public static void main(String args[]) throws Exception{
		
		ReadBatchFile obj = new ReadBatchFile();
		String Output = obj.ReadInputBatchFile("<properties><emailFrom>venu.gopal@sourcen.com</emailFrom><emailTo>rajashree.meganathan@sourcen.com</emailTo><emailSubject>Exception in ePharma Prepack Application - XWMS</emailSubject><emailSMTP>smtp.bizmail.yahoo.com</emailSMTP><emailUserName>testepharma@sourcen.com</emailUserName><emailPassword>sniplpass</emailPassword><fileUrl>C:/EPED_PrePack_Files/</fileUrl><errorUrl>C:/EPED_PrePack_Error_Files/</errorUrl><processedUrl>C:/EPED_PrePack_Processed_Files/</processedUrl><signerid>23836883706277927282238626987996</signerid><deaNumber>RP0300892</deaNumber><sourceRoutingCode>MDW</sourceRoutingCode></properties>");		
		System.out.println("Output_Read batch file "+Output);
	}
	
}
