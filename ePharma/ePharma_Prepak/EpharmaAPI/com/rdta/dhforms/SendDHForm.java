package com.rdta.dhforms;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Node;




//import com.Ostermiller.util.Base64;
import com.Ostermiller.util.Base64;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;


public class SendDHForm {
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static final Log log = LogFactory.getLog(SendDHForm.class);
	
	
	public String sendPDF(String pedigreeID){
		
		
		//String pedigreeID = "fff36a77-284f-1040-c001-e75f0d664277"; 
		
		try {
			String query="tlsp:getRepackagedInfo('"+pedigreeID+"')";
			
			log.info("Query"+query);
			System.out.println("-------query is-----"+query);
			
			String str=queryRunner.returnExecuteQueryStringsAsString(query);
			log.info("QueryResult"+str);
			System.out.println("-------str is-----"+str);
	
			if(str !=null){
				Node n1=XMLUtil.parse(str);
				System.out.println("-------ni is-----"+n1);

				Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
				Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
					
				if(n3 != null){
					sendRepackagePDF(str,pedigreeID);
				}else{
					sendInitialPDF(str,pedigreeID);
				}
			}
		} catch (PersistanceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}


	private void sendRepackagePDF(String pedigreeID, String str) {
		// TODO Auto-generated method stub
		try {

			System.out.println(" here in sendRepackagePDF....."+str);
			String xmlString = getRepackageXSLString(pedigreeID,str,".");
			
			
			SendDHFormEmail mail=new SendDHFormEmail();						
			
            File baseDir = new File(".");
            File outDir = new File(baseDir, "out");
            
            outDir.mkdirs();
           
            // Setup input and output files                                                      
            File xmlfile = File.createTempFile("User", ".xml");   
            //xmlfile.createNewFile();
            // Delete temp file when program exits.
            xmlfile.deleteOnExit();        
            // Write to temp file
            BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile));           
            bw.write(xmlString);
            bw.close();
      
            File xsltfile = new File(baseDir, "/xsl/repack.fo");
            File pdffile = new File(outDir, "1.pdf");
          
            // Construct fop with desired output format
            Fop fop = new Fop(MimeConstants.MIME_PDF);
            
          // Setup output
            OutputStream out = new java.io.FileOutputStream(pdffile);
            out = new java.io.BufferedOutputStream(out);
          
          
                fop.setOutputStream(out);
    
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");
                
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
                
           
                out.close();
                
               String[] filePath = new String[2];
 			   filePath[0] = pdffile.getAbsolutePath();
 			   
 			   
//String result =SendDHFormEmail.sendDHFormEmailAttachement("manish@rainingdata.com","epharma-support@rainingdata.com","smtp.rainingdata.com","Your DH2135 form for Invoice #1124566","Please find the Florida form 2135 attached alongwith.Incase of question feel free to contact us at 888-888-8888.","mgambhir","119714",pdffile.getAbsolutePath());
                String query = "for $i in collection('tig:///ePharma/ShippedPedigree')/pedigreeEnvelope/pedigree/shippedPedigree[documentInfo/serialNumber = '"+str+"']/repackagedPedigree/previousPedigrees/initialPedigree ";
                query = query + "return $i/altPedigree/data/string()";
                System.out.println("Query : "+query);
                List initialStatus = queryRunner.executeQuery(query);
                System.out.println("&&&&&&&  "+initialStatus.size());
                if(initialStatus != null && initialStatus.size()>0){
                	filePath[1] = getInitialPedigreePDF(initialStatus,str);
                	System.out.println("file path : "+filePath[1]);
                }
                
 			  String result =SendDHFormEmail.sendDHFormEmailMutipleAttachement("venu.gopal@sourcen.com","venu.gopal@sourcen.com","smtp.bizmail.yahoo.com",
                        "DHFORMS","Please find the DH form attached.","testepharma@sourcen.com","sniplpass",filePath);


		System.out.println(" here in sendRepackagePDF....."+result);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void sendInitialPDF(String pedigreeID, String str) {

		System.out.println(" here in sendInitialPDF.....");		
		try {
			String xmlString = getInitialXSLString(str,pedigreeID,".");
			
			File baseDir = new File(".");
			
            File outDir = new File(baseDir, "out");            
            outDir.mkdirs();
           
            // Setup input and output files                                                      
            File xmlfile = File.createTempFile("User", ".xml");   
            
            // Delete temp file when program exits.
            xmlfile.deleteOnExit();  
            
            // Write to temp file
            BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile)); 
            
            bw.write(xmlString);
            bw.close();
      
            File xsltfile = new File(baseDir, "/xsl/initial.fo");
            //File pdffile = File.createTempFile("initial", ".pdf");
            
            File pdffile = new File(outDir, "1.pdf");           
            // Construct fop with desired output format
            
            Fop fop = new Fop(MimeConstants.MIME_PDF);
            
           
            // Setup output
            OutputStream out = new java.io.FileOutputStream(pdffile);
            out = new java.io.BufferedOutputStream(out);
          
            try {
                fop.setOutputStream(out);
    
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
                
                
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("versionParam", "2.0");
                
                // Setup input for XSLT transformation
                Source src = new StreamSource(xmlfile);
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing
                transformer.transform(src, res);
             
                out.close();                              
                
                //MessageResources messageResources = getResources(request);
                
                //String result =SendDHFormEmail.sendDHFormEmailAttachement("manish@rainingdata.com","manish@rainingdata.com","smtp.bizmail.yahoo.com",
                        //"DHFORMS","Please find the DH form attached.","testepharma@sourcen.com","sniplpass",pdffile.getAbsolutePath());


		    String result =SendDHFormEmail.sendDHFormEmailAttachement("venu.gopal@sourcen.com","venu.gopal@sourcen.com","smtp.bizmail.yahoo.com",
                        "DHFORMS","Please find the DH form attached.","testepharma@sourcen.com","sniplpass",pdffile.getAbsolutePath());
                            
               System.out.println(" here in sendInitialPDF....."+result);
                
               
            }finally{
            	
            }                       
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	
	
	
	public String getInitialXSLString(String str,String pedigreeID,String uri)throws Exception{
     	
     	
     	StringBuffer  buffer = new StringBuffer("<shippedPedigree>");
 	
     	Node n1=XMLUtil.parse(str);
 		Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
 		
 		String strName =XMLUtil.getValue(n2,"signatureInfo/signerInfo/name"); 
 		String signEmail=XMLUtil.getValue(n2,"signatureInfo/signerInfo/email");  		 		
 		//adding business name
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/senderInfo/businessAddress/businessName"),true));
 		
 		
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/drugName"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/strength"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/dosageForm"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/containerSize"),true));
 		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::initialPedigree/productInfo/productCode"),true));
 		
 		Node n3 = XMLUtil.getNode(n2,"descendant::initialPedigree");
 		
 		int i=0;
 		List ls= new ArrayList();
			Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
			//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
			while(it.hasNext()){
				
				Node n4= (Node)it.next();
				
				buffer.append(XMLUtil.convertToString(n4,true));
				i++;
				
			}
			
			//adding new code here...
			
			if(i==0){
				Node shippedNode = XMLUtil.getNode(n2,"child::*/child::shippedPedigree");
				
				while(true){
					
					
					if(shippedNode!=null){
						Iterator itemIterator = XMLUtil.executeQuery(shippedNode,"itemInfo").iterator();
						//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
						while(it.hasNext()){
							
							Node itemNode= (Node)itemIterator.next();
							
							buffer.append(XMLUtil.convertToString(itemNode,true));
							i++;
							
						}
						shippedNode = XMLUtil.getNode(shippedNode,"child::*/child::shippedPedigree");
					}
					
					if(i>0 || shippedNode == null)break;
				}
				
				if(i==0){
					
					
					Iterator initialItemInfoIt=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					while(initialItemInfoIt.hasNext()){
						
						Node n4= (Node)initialItemInfoIt.next();
						
						buffer.append(XMLUtil.convertToString(n4,true));
					}
					
					
				}
			}
 		
 		
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionDate"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifier"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifierType"),true));
 		
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"productInfo/manufacturer"),true));
			
			
			Node n5 = XMLUtil.getNode(n3,"descendant::transactionInfo");
			
			if(n3!=null && n5 != null){
				
				
				boolean flag = compare(n3);
				
				buffer.append("<initialTransaction>");
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionDate"),true));
				
				//buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/recipientInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/contactInfo"),true));
				
				
				
				
				//Setting the Shipping Address
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/senderInfo/shippingAddress"),true));
					
				}
				buffer.append("</initialTransaction>");
			}
			
		
			buffer.append("<custodyChain>");
			String serialNumber="";
			Node intialShipped = XMLUtil.getNode(n2,"descendant-or-self::*[initialPedigree]");
			
			
			if(intialShipped!=null){
				buffer.append("<transactionInfo>");
				boolean flag = compare(intialShipped);
				
				serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
				
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionDate"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/recipientInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"signatureInfo/signerInfo"),true));
			
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/shippingAddress"),true));
				}
				buffer.append("</transactionInfo>");
			}
			
			
			if(!(pedigreeID.equals(serialNumber))){
				
				Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
				while(true){
				
				//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
				
				if(shippedNode!=null){
					
					buffer.append("<transactionInfo>");
					boolean flag = compare(shippedNode);
					
					serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/businessAddress"),true));
					//Setting the business Address
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/recipientInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"signatureInfo/signerInfo"),true));
				
					//Setting the Shipping Address
					
					if(!flag){
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/shippingAddress"),true));
					}
					
					shippedNode = XMLUtil.getNode(shippedNode,"ancestor::shippedPedigree");
					
					buffer.append("</transactionInfo>");
				}
				
				if(pedigreeID.equals(serialNumber) || shippedNode==null)break;
			}
			}
			
			buffer.append("</custodyChain>");
			
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"signatureInfo/signerInfo"),true));
			buffer.append("<date>"+CommonUtil.dateToString(new Date())+"</date>");
			//buffer.append("<path>C:\\Documents and Settings\\Ajay Reddy\\workspace\\ePharma_SW\\xsl\\logo.gif</path>");
			buffer.append("<path>"+uri+"</path>");
			buffer.append("<signPath>"+uri+"</signPath>");
 		buffer.append("</shippedPedigree>");
 			
 		getSignatureImage(strName,signEmail); 		
 		return buffer.toString();
     	
     }
	
	public static void getSignatureImage(String strName,String strSignEmail)throws Exception{	
		try{
			
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
 			
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 			  			
 			File pictFile =  new File("xsl\\Signature.jpeg");			
			if(pictFile.exists()){
				pictFile.delete();
											
			}
 			if (rslt != null) {																					 				
 				FileOutputStream fos = new FileOutputStream(pictFile);			        			       
 				fos.write(rslt);
 				fos.flush();
 				
 			} 					
 			Thread.sleep(1750);
			}catch(Exception ie) {
							 
			}		
		//return status;
	}

	
	public String getRepackageXSLString(String str,String pedigreeID,String uri)throws Exception{
    	
    	System.out.println("Str::::::::\n\n\n"+str+"\n\n\n");
    	StringBuffer  buffer = new StringBuffer("<shippedPedigree>");
    	
    	Node n1=XMLUtil.parse(str);
		Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
		
		String strName =XMLUtil.getValue(n2,"signatureInfo/signerInfo/name"); 
 		String signEmail=XMLUtil.getValue(n2,"signatureInfo/signerInfo/email"); 
 				
		//adding business name
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/senderInfo/businessAddress/businessName"),true));
		
		
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/drugName"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/strength"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/dosageForm"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/containerSize"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/productCode"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/manufacturer"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"signatureInfo/signerInfo"),true));
		
		Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
		Node initialNode = XMLUtil.getNode(n2,"descendant::initialPedigree");
		int i=0;
		List ls= new ArrayList();
		Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
		//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
		while(it.hasNext()){
			
			Node n4= (Node)it.next();
			
			buffer.append(XMLUtil.convertToString(n4,true));
			i++;
			
		}
		
		//adding new code here...
		
		if(i==0){
			Node shippedNode = XMLUtil.getNode(n2,"child::*/child::shippedPedigree");
			
			while(true){
				
				
				if(shippedNode!=null){
					Iterator itemIterator = XMLUtil.executeQuery(shippedNode,"itemInfo").iterator();
					//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
					while(it.hasNext()){
						
						Node itemNode= (Node)itemIterator.next();
						
						buffer.append(XMLUtil.convertToString(itemNode,true));
						i++;
						
					}
					shippedNode = XMLUtil.getNode(shippedNode,"child::*/child::shippedPedigree");
				}
				
				if(i>0 || shippedNode == null)break;
				
				
			}if(i==0){
				
				
				Iterator initialItemInfoIt=XMLUtil.executeQuery(n3,"itemInfo").iterator();
				//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
				while(initialItemInfoIt.hasNext()){
					
					Node n4= (Node)initialItemInfoIt.next();
					
					buffer.append(XMLUtil.convertToString(n4,true));
				}
			}
			
			
		}
		
		
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionDate"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifier"),true));
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"transactionInfo/transactionIdentifier/identifierType"),true));
		
		Iterator it1=XMLUtil.executeQuery(n2,"descendant::repackagedPedigree/previousProducts").iterator();
		while(it1.hasNext()){
			Node n4= (Node)it1.next();
			buffer.append(XMLUtil.convertToString(n4,true));
		}
		
		buffer.append("<custodyChain>");
		String serialNumber="";
		Node intialShipped = XMLUtil.getNode(n2,"descendant-or-self::*[repackagedPedigree]");
		
		
		if(intialShipped!=null){
			buffer.append("<transactionInfo>");
			boolean flag = compare(intialShipped);
			
			serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/businessAddress"),true));
			
			//Setting the business Address
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(initialNode,"transactionInfo/transactionIdentifier/identifier"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(initialNode,"transactionInfo/transactionIdentifier/identifierType"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(initialNode,"transactionInfo/transactionDate"),true));
			
			
			
			
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"),true));
			
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/recipientInfo"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"signatureInfo/signerInfo"),true));
			
			
			
			if(!flag){
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/shippingAddress"),true));
			}
			buffer.append("</transactionInfo>");
		}
	
		if(!(pedigreeID.equals(serialNumber))){
			
			Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
		
		while(true){
			
			//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
			
			if(shippedNode!=null){
				
				buffer.append("<transactionInfo>");
				boolean flag = compare(shippedNode);
				
				serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/recipientInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"signatureInfo/signerInfo"),true));
			
				//Setting the Shipping Address
				
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/shippingAddress"),true));
				}
				
				shippedNode = XMLUtil.getNode(shippedNode,"ancestor::shippedPedigree");
				
				buffer.append("</transactionInfo>");
			}
			
			if(pedigreeID.equals(serialNumber) || shippedNode==null)break;
		}
		}
	
		buffer.append("</custodyChain>");
		
		
		buffer.append("<date>"+CommonUtil.dateToString(new Date())+"</date>");
		buffer.append("<path>"+uri+"</path>");
		buffer.append("<signPath>"+uri+"</signPath>");
		buffer.append("</shippedPedigree>");
		
		getSignatureImage(strName,signEmail);
		return buffer.toString();
    	
    }
	
 	public boolean compare(Node n1){
		
		Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
		Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
/*
		
		if(businessAddress!=null && shippingAddress != null){
			if(XMLUtil.getValue(businessAddress,"businessName").equals(XMLUtil.getValue(shippingAddress,"businessName"))){
				if(XMLUtil.getValue(businessAddress,"street1").equals(XMLUtil.getValue(shippingAddress,"street1"))){
					if(XMLUtil.getValue(businessAddress,"street2").equals(XMLUtil.getValue(shippingAddress,"street2"))){
						if(XMLUtil.getValue(businessAddress,"city").equals(XMLUtil.getValue(shippingAddress,"city"))){
							if(XMLUtil.getValue(businessAddress,"stateOrRegion").equals(XMLUtil.getValue(shippingAddress,"stateOrRegion"))){
								if(XMLUtil.getValue(businessAddress,"postalCode").equals(XMLUtil.getValue(shippingAddress,"postalCode"))){
									if(XMLUtil.getValue(businessAddress,"country").equals(XMLUtil.getValue(shippingAddress,"country"))){
										return true;
									}
								}
								
							}
						}
					}
				}
			}
			
		}
		return false;*/return true;
	}
 	private String getInitialPedigreePDF(List data,String pedigreeID) {
		// TODO Auto-generated method stub
		try {
			
			//StringBuffer buff = new StringBuffer();
			//buff.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = '31393843945529672246633950583774'] return $i/altPedigree/data/string()");
			//List data = queryRunner.executeQuery(buff.toString());
			System.out.println("Result : "
					+ data.get(0).getClass().getName());
			InputStream stream = (ByteArrayInputStream) data.get(0);
			
		   byte[] data1 = new byte[128];
		  	
		   File file = new File("c:\\temp\\InitialPedigree"+pedigreeID+".pdf");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			
			DataOutputStream dos = new DataOutputStream(fos);
			int x = stream.read(data1);
			while (x != -1) {
				 byte[] decoded = Base64.decode(data1);
							
				 dos.write(decoded);
				 data1= new byte[128];
				 x = stream.read(data1);
			}
			stream.close();
			dos.close();
			fos.close();
			File pdffile = new File("c:\\temp\\InitialPedigree"+pedigreeID+".pdf");
					
			return pdffile.getAbsolutePath();
			
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	
			e.printStackTrace();
			return null;
		}
	}
 	public static void main(String args[]){
 		try{
 		SendDHForm dh = new SendDHForm();
 		dh.sendPDF("fff36a85-42e6-1900-c001-7bfc20b432d7");
 		}catch(Exception e){
 			
 		}
 	}
}
