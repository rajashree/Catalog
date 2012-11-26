package com.rdta.dhforms;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.rmi.server.UID;

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
import org.pdfbox.examples.persistence.AppendDoc;
import org.pdfbox.examples.persistence.CopyDoc;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.exceptions.WrappedException;
import org.w3c.dom.Node;

import com.Ostermiller.util.StringTokenizer;
import com.rdta.eag.epharma.api.PortalIntegration;
import com.rdta.eag.epharma.commons.CommonUtil;
import com.rdta.eag.epharma.commons.persistence.PersistanceException;
import com.rdta.eag.epharma.commons.persistence.QueryRunner;
import com.rdta.eag.epharma.commons.persistence.QueryRunnerFactory;
import com.rdta.eag.epharma.commons.xml.XMLUtil;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;


public class CreatePDF {
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	static final Log log = LogFactory.getLog(CreatePDF.class);
	List finalFilePath = new ArrayList();
	String MergedPDF;
	static int count = 0;
	String recipeintId="";
	String authenticatorId="";
	private static final String PROPS_CONFIG = "Prop.properties";
	
	public String getInitialXSLString(String str,String pedigreeID,String uri)throws Exception{
     	
     	log.info("*******Inside getInitialXSLString********");
     	
     	StringBuffer  buffer = new StringBuffer("<shippedPedigree>");
     	
     	//Changes to Obtain Recipient Info and Authenticator Info from Properties file : START
     	
     	java.util.Properties properties = new java.util.Properties();
		InputStream inputstream = CreatePDF.class.getResourceAsStream(PROPS_CONFIG);

		properties.load(inputstream);
		recipeintId = properties.getProperty("recipeintId");
		log.info("Recipient Id"+recipeintId);
		authenticatorId= properties.getProperty("authenticatorId");
		log.info("Authenticator Id"+authenticatorId);
     	
		String qry1="for $i in collection('tig:///EAGRFID/SysUsers')/User where $i/UserID='"+recipeintId;
		qry1=qry1+"'return <recipientInfo><recipientName>{concat($i/FirstName,$i/LastName)}</recipientName>";
		qry1=qry1+"<recipientSignature>{concat($i/FirstName,$i/LastName)}</recipientSignature></recipientInfo>"; 
		String recipientInfo=queryRunner.returnExecuteQueryStringsAsString(qry1);
     	Node recipeintInfoNode = XMLUtil.parse(recipientInfo);
     	
    
     	
		String qry2="for $i in collection('tig:///EAGRFID/SysUsers')/User where $i/UserID='"+authenticatorId;
		qry2=qry2+"'return <authenticatorInfo><authenticatorName>{concat($i/FirstName,$i/LastName)}</authenticatorName>" +
			"<authenticatorSignature>{concat($i/FirstName,$i/LastName)}</authenticatorSignature>" +
			"<authenticatorTelephone>{data($i/Phone)}</authenticatorTelephone>" +
			"<authenticatorEmail>{data($i/Email)}</authenticatorEmail></authenticatorInfo>"; 
		String authenticatorInfo=queryRunner.returnExecuteQueryStringsAsString(qry2);
		Node authenticatorInfoNode = XMLUtil.parse(authenticatorInfo);
	
		
     	//Changes to Obtain Recipient Info and Authenticator Info from Properties file : END
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
 		log.info("Value of node ***** : "+n2 + " signer Name : "+str);
		if((XMLUtil.getNode(n2,"descendant::initialPedigree/altPedigree"))!=null){
					buffer.append("<altPedigree>yes</altPedigree>");
				}
				
 		int i=0;
 		List ls= new ArrayList();
			Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
			//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
			while(it.hasNext()){
				
				Node n4= (Node)it.next();
				
				buffer.append(XMLUtil.convertToString(n4,true));
				i++;
				log.info("Value of i in iterator loop***** : "+i);
			}
			
			//adding new code here...
			log.info("Value of i ***** : "+i);
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
			
			/*if(n3!=null && n5 != null){
				
				
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
			}*/
			
		
			buffer.append("<custodyChain>");
			String serialNumber="";
			Node intialShipped = XMLUtil.getNode(n2,"descendant-or-self::*[initialPedigree]");
			
			
			if(intialShipped!=null){
				buffer.append("<transactionInfo>");
				buffer.append("<recipientSignPath>"+uri+"</recipientSignPath>");
				buffer.append("<authenticatorSignPath>"+uri+"</authenticatorSignPath>");
			
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(recipeintInfoNode,"recipientName"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(recipeintInfoNode,"recipientSignature"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorName"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorSignature"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorTelephone"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorEmail"),true));
				
				boolean flag = compare(intialShipped);
				
				serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/businessAddress"),true));
				
				//Setting the business Address
				/*buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionDate"),true));
				*/
				
				if(n5!=null){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifier"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifierType"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionDate"),true));
					
				}else{
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifier"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifierType"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionDate"),true));
				}
				
				
				if(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo")!=null)
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/recipientInfo"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"signatureInfo/signerInfo"),true));
				
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/shippingAddress"),true));
				}
				/*if((XMLUtil.getNode(n2,"descendant::initialPedigree/transactionInfo"))!=null){
					buffer.append("<checkPedigree>yes</checkPedigree>");
				}*/
				if((XMLUtil.getNode(n2,"descendant::initialPedigree/altPedigree"))!=null){
					buffer.append("<altPedigree>yes</altPedigree>");
				}
				
				
				buffer.append("</transactionInfo>");
				
			}
			
			//intialTransaction information inside the custody chain
			
			/*
			if(n5!=null){
				
				buffer.append("<transactionInfo>");
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n3,"transactionInfo/transactionDate"),true));
				
				buffer.append("</transactionInfo>");
				
				
			}
			*/
			
			if(!(pedigreeID.equals(serialNumber))){
				
				Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
				Node testNode = intialShipped;
				while(true){
				
				//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
				
				if(shippedNode!=null){
					
					buffer.append("<transactionInfo>");
					boolean flag = compare(shippedNode);
					
					serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/businessAddress"),true));
					//Setting the business Address
					/*buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
					*/
					
					if(n5!=null){
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionIdentifier/identifier"),true));
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionIdentifier/identifierType"),true));
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionDate"),true));
						
					}else{
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
					}
					
					
					
					if(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo")!=null)
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/recipientInfo"),true));
					
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"signatureInfo/signerInfo"),true));
				
					
					
					//Setting the Shipping Address
					
					if(!flag){
						buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/shippingAddress"),true));
					}
					
					/*if((XMLUtil.getNode(n2,"descendant::initialPedigree/transactionInfo"))!=null){
						buffer.append("<checkPedigree>yes</checkPedigree>");
					}*/
					testNode = shippedNode;
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
			buffer.append("<recipientSignPath>"+uri+"</recipientSignPath>");
			buffer.append("<authenticatorSignPath>"+uri+"</authenticatorSignPath>");
 		buffer.append("</shippedPedigree>");
 		log.info("XML :"+buffer.toString());		
 		 	
 		getSignatureImage1(strName,signEmail); 
 		getAuthenticatorSignatureImage();
 		getRecipientSignatureImage();
 		return buffer.toString();
 		
     	
     }
	
	 
	public void getSignatureImage1(String strName,String strSignEmail)throws Exception{	
		try{
			
 			Helper helper =new Helper();
 			Connection Conn = helper.ConnectTL();
 			Statement Stmt = helper.getStatement(Conn);
 			StringBuffer bfr = new StringBuffer();
 			File basedir = getBaseDirectory();
 			bfr.append("let $userId := (for $i in collection('tig:///EAGRFID/SysUsers')/User");
 			bfr.append(" where concat(data($i/FirstName),' ',data($i/LastName))= '" +strName+"' and $i/Email='"+strSignEmail+"'");
 			bfr.append(" return data($i/UserID))");
 			bfr.append("for $k in collection('tig:///EAGRFID/UserSign')/User");
 			bfr.append(" where $k/UserID = $userId");
 			bfr.append(" return $k/UserSign/binary()");
 			log.info("Query "+bfr.toString());
 			log.info("Picture file retrieve " + bfr );
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 		
 			log.info("Picture file Path:" + basedir.getAbsolutePath()+"\\xsl\\Signature.jpeg" );
 			File pictFile =  new File(basedir.getAbsolutePath()+"\\xsl\\Signature.jpeg");
 			log.info("Picture file Path:" + pictFile.getAbsolutePath() );
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

	

	 
	public void getAuthenticatorSignatureImage()throws Exception{	
		try{
			
 			Helper helper =new Helper();
 			Connection Conn = helper.ConnectTL();
 			Statement Stmt = helper.getStatement(Conn);
 			StringBuffer bfr = new StringBuffer();
 			File basedir = getBaseDirectory();
 			
 			bfr.append("for $k in collection('tig:///EAGRFID/UserSign')/User");
 			bfr.append(" where $k/UserID = '"+authenticatorId+"'");
 			bfr.append(" return $k/UserSign/binary()");
 			log.info("Query "+bfr.toString());
 			log.info("Authenticator Signture file retrieve " + bfr );
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 		
 			log.info("Picture file Path:" + basedir.getAbsolutePath()+"\\xsl\\AuthenticatorSignature.jpeg" );
 			File pictFile =  new File(basedir.getAbsolutePath()+"\\xsl\\AuthenticatorSignature.jpeg");
 			log.info("AuthenticatorSignature file Path:" + pictFile.getAbsolutePath() );
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
	

	 
	public void getRecipientSignatureImage()throws Exception{	
		try{
			
 			Helper helper =new Helper();
 			Connection Conn = helper.ConnectTL();
 			Statement Stmt = helper.getStatement(Conn);
 			StringBuffer bfr = new StringBuffer();
 			File basedir = getBaseDirectory();
 			bfr.append("for $k in collection('tig:///EAGRFID/UserSign')/User");
 			bfr.append(" where $k/UserID = '"+recipeintId+"'");
 			bfr.append(" return $k/UserSign/binary()");
 			log.info("Query "+bfr.toString());
 			log.info("Picture file retrieve " + bfr );
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 		
 			log.info("Picture file Path:" + basedir.getAbsolutePath()+"\\xsl\\RecipientSignature.jpeg" );
 			File pictFile =  new File(basedir.getAbsolutePath()+"\\xsl\\RecipientSignature.jpeg");
 			log.info("Recipient Signature file Path:" + pictFile.getAbsolutePath() );
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
	public String mergePDF(String file1,String file2) throws Exception    {
		
		AppendDoc merge = new AppendDoc(); 
		
            String fileout=getBaseDirectory()+"/out/DHMergedPedigreeForm.pdf";
	     File f = new File(fileout);
	     f.delete();
	     
		//merge.doIt("C:/Temp/ap-DH2135Pedigree.pdf", "C:/Temp/ap-DH2129Pedigree.pdf", "C:/Temp/out1.pdf");
     	try{
     		FileInputStream f1=new FileInputStream(file2);
     		FileInputStream f2=new FileInputStream(file1);
            merge.doIt(file2, file1, fileout);
            log.info("after merge");
     	}
     	catch(FileNotFoundException e){
     		log.info("file not found exception");
     		try{
             	/*SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                         "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
     			e.printStackTrace();*/
     			fileout= null;
     			
     			throw new Exception();
             	}catch(Exception ex){throw new Exception();}
     	}
     	catch(Exception ex){ 
     		try{
             	/*SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                         "Error in Merge PDF",ex.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
     			ex.printStackTrace();*/
     			fileout= null;
     			throw new Exception();
             	}catch(Exception e1){throw new Exception();}
             	
     	}
	 	return fileout;
	 	
	}
	
	public String getRepackageXSLString(String str,String pedigreeID,String uri)throws Exception{
    	
		StringBuffer  buffer = new StringBuffer("<shippedPedigree>");
    	
		java.util.Properties properties = new java.util.Properties();
		InputStream inputstream = CreatePDF.class.getResourceAsStream(PROPS_CONFIG);

		properties.load(inputstream);
		recipeintId = properties.getProperty("recipeintId");
		log.info("Recipient Id"+recipeintId);
		authenticatorId= properties.getProperty("authenticatorId");
		log.info("Authenticator Id"+authenticatorId);
     	
		String qry1="for $i in collection('tig:///EAGRFID/SysUsers')/User where $i/UserID='"+recipeintId;
		qry1=qry1+"'return <recipientInfo><recipientName>{concat($i/FirstName,$i/LastName)}</recipientName>";
		qry1=qry1+"<recipientSignature>{concat($i/FirstName,$i/LastName)}</recipientSignature></recipientInfo>"; 
		String recipientInfo=queryRunner.returnExecuteQueryStringsAsString(qry1);
     	Node recipeintInfoNode = XMLUtil.parse(recipientInfo);
     	
    
     	
		String qry2="for $i in collection('tig:///EAGRFID/SysUsers')/User where $i/UserID='"+authenticatorId;
		qry2=qry2+"'return <authenticatorInfo><authenticatorName>{concat($i/FirstName,$i/LastName)}</authenticatorName>" +
			"<authenticatorSignature>{concat($i/FirstName,$i/LastName)}</authenticatorSignature>" +
			"<authenticatorTelephone>{data($i/Phone)}</authenticatorTelephone>" +
			"<authenticatorEmail>{data($i/Email)}</authenticatorEmail></authenticatorInfo>"; 
		String authenticatorInfo=queryRunner.returnExecuteQueryStringsAsString(qry2);
		Node authenticatorInfoNode = XMLUtil.parse(authenticatorInfo);
	
		
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
		if((XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/manufacturer"))!=null){
		 buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"descendant::repackagedPedigree/productInfo/manufacturer"),true));
		}
		buffer.append(XMLUtil.convertToString(XMLUtil.getNode(n2,"signatureInfo/signerInfo"),true));
		
		Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
		//to check if any altPedigree exists
		if((XMLUtil.getNode(n3,"descendant::altPedigree"))!=null){
			buffer.append("<altPedigree>yes</altPedigree>");
		}
		
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
		
		Node repackTransactionInfo =  getTransactionInfo(n3);
		
		if(intialShipped!=null){
			buffer.append("<transactionInfo>");
			boolean flag = compare(intialShipped);
			buffer.append("<recipientSignPath>"+uri+"</recipientSignPath>");
			buffer.append("<authenticatorSignPath>"+uri+"</authenticatorSignPath>");
			
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(recipeintInfoNode,"recipientName"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(recipeintInfoNode,"recipientSignature"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorName"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorSignature"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorTelephone"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(authenticatorInfoNode,"authenticatorEmail"),true));
			
			
			serialNumber =  XMLUtil.getValue(intialShipped,"documentInfo/serialNumber");
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/businessAddress"),true));
			//Setting the business Address
			/*buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifier"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionIdentifier/identifierType"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/transactionDate"),true));
			*/
			if(repackTransactionInfo!=null){
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(repackTransactionInfo,"transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(repackTransactionInfo,"transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(repackTransactionInfo,"transactionDate"),true));
			}
			
			if((XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"))!=null){
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"),true));
			}
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/recipientInfo"),true));
			buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"signatureInfo/signerInfo"),true));
			
			
			
			if(!flag){
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/shippingAddress"),true));
			}
			buffer.append("</transactionInfo>");
		}
	
		if(!(pedigreeID.equals(serialNumber))){
			
			Node shippedNode = XMLUtil.getNode(intialShipped,"ancestor::shippedPedigree");
			Node testNode = intialShipped;
		while(true){
			
			//Node shippedNode = XMLUtil.getNode(n2,".[descendant-or-self::*/shippedPedigree/documentInfo/serialNumber='"+serialNumber+"']");
			
			if(shippedNode!=null){
				
				buffer.append("<transactionInfo>");
				boolean flag = compare(shippedNode);
				
				serialNumber =  XMLUtil.getValue(shippedNode,"documentInfo/serialNumber");
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/businessAddress"),true));
				//Setting the business Address
				/*buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/transactionDate"),true));
				*/
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionIdentifier/identifier"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionIdentifier/identifierType"),true));
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(testNode,"transactionInfo/transactionDate"),true));
				
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/recipientInfo"),true));
				
				buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"signatureInfo/signerInfo"),true));
			
				//Setting the Shipping Address
				
				if(!flag){
					buffer.append(XMLUtil.convertToString(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/shippingAddress"),true));
				}
				
				testNode = shippedNode;
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
		buffer.append("<recipientSignPath>"+uri+"</recipientSignPath>");
		buffer.append("<authenticatorSignPath>"+uri+"</authenticatorSignPath>");
		buffer.append("</shippedPedigree>");
		log.info("XML :"+buffer.toString());
		getSignatureImage1(strName,signEmail);
		getAuthenticatorSignatureImage();
 		getRecipientSignatureImage();
		log.info("AftergetSignatureImage");
		return buffer.toString();
    	
    }
	
 	public boolean compare(Node n1){
		
		Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
		Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
		
		if(businessAddress!=null && shippingAddress != null){
			/*if(XMLUtil.getValue(businessAddress,"businessName").equals(XMLUtil.getValue(shippingAddress,"businessName"))){
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
			}*/
			
			if(check(XMLUtil.getValue(businessAddress,"businessName"),(XMLUtil.getValue(shippingAddress,"businessName")))){
				if(check(XMLUtil.getValue(businessAddress,"street1"),(XMLUtil.getValue(shippingAddress,"street1")))){
					if(check(XMLUtil.getValue(businessAddress,"street2"),(XMLUtil.getValue(shippingAddress,"street2")))){
						if(check(XMLUtil.getValue(businessAddress,"city"),(XMLUtil.getValue(shippingAddress,"city")))){
							if(check(XMLUtil.getValue(businessAddress,"stateOrRegion"),(XMLUtil.getValue(shippingAddress,"stateOrRegion")))){
								if(check(XMLUtil.getValue(businessAddress,"postalCode"),(XMLUtil.getValue(shippingAddress,"postalCode")))){
									if(check(XMLUtil.getValue(businessAddress,"country"),(XMLUtil.getValue(shippingAddress,"country")))){
										return true;
									}
								}
								
							}
						}
					}
				}
			}
			
		}if(shippingAddress == null){
			return true;
		}
		return false;
	}
 	public boolean check(String str1, String str2){
		if(str1!=null && str2!=null){
			if(str1.equals(str2)){
				return true;
			}
		}else if(str1 == null && str2 == null){
			return true;
		}
		
		
		return false;
	}

 	private String createRepackagePDF(String pedigreeID, String str) {
		// TODO Auto-generated method stub
 		 String[] filePath = new String[2];
 		List initialStatus = new ArrayList();
 		
 		List altPedigreesFilePaths = new ArrayList();
 		 String mergeFilePath = "";
 		try {

			log.info(" here in sendRepackagePDF....."+str);
			String xmlString = getRepackageXSLString(str,pedigreeID,getBaseDirectory().getAbsolutePath());
		
			boolean checkMan = checkManufacturer(str);

			
			 			
			
            File baseDir = getBaseDirectory();
            
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
           File xsltfile = null;
            if(checkMan){
            	xsltfile = new File(baseDir, "/xsl/repackFromManufaturer.fo");
            }
            else{
            	xsltfile = new File(baseDir, "/xsl/repackFromWholesaler.fo");	
            }
            File pdffile = new File(outDir, pedigreeID.substring(9)+"repack.pdf");

           
            // Construct fop with desired output format
            Fop fop = new Fop(MimeConstants.MIME_PDF);
            
          // Setup output
            log.info("The pdf PATH ________ "+pdffile.getAbsolutePath());
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
               
 		       filePath[0] = pdffile.getAbsolutePath();
 		       
// New Code - for Handling RepackageUseCases for Prepak : START 
 		       
 			   finalFilePath.add(filePath[0]);

 		
		   String newquery1 =  "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree return count($i/*:previousPedigrees)";
		   String newcount = queryRunner.returnExecuteQueryStringsAsString(newquery1);
		   for (int i=0;i<Integer.parseInt(newcount);i++){ 	      
 		    String repackPrevPed = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree/*:previousPedigrees["+(i+1)+"]  return $i";
			
 		    log.info("New"+repackPrevPed);
 		    String  prevPeds = queryRunner.returnExecuteQueryStringsAsString(repackPrevPed);
 		     
 		    Node prevPed =XMLUtil.parse(prevPeds);
 		    
 	 	
 	 		
 	 		Node altPed=XMLUtil.getNode(prevPed,"/previousPedigrees/altPedigree");
 	 		Node initialPed=XMLUtil.getNode(prevPed,"/previousPedigrees/initialPedigree");
 	 		Node recvdPed = XMLUtil.getNode(prevPed,"/previousPedigrees/pedigree/receivedPedigree");
 	 		Node shippedPed = XMLUtil.getNode(prevPed,"/previousPedigrees/pedigree/shippedPedigree");
 	 		
 	 		if(altPed != null){
 	 			String query1 = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree/*:previousPedigrees["+(i+1)+"]";
 		    	query1 = query1 + " return if(exists($i//*:altPedigree)) then tlsp:GetBinaryImageForServlet(binary{$i//*:altPedigree/*:data},'data') else ()";
 		    	log.info("Query : "+query1);
 		    	initialStatus=queryRunner.executeQuery(query1);
		    	 
 		    	if(initialStatus != null && initialStatus.size()>0){
 	 		    	
 	 		    	String tempFilePath = getInitialPedigreePDF(initialStatus,str);
 	 		    	finalFilePath.add(tempFilePath);
				}
 		    	MergedPDF =mergeListOfPDFs(finalFilePath);
				
				
 	 		}
 	 		else if(initialPed !=null){
 	 			
 	 			String query2 = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree/*:previousPedigrees["+(i+1)+"]";
 		    	query2 = query2 + " return if(exists($i//*:initialPedigree/*:altPedigree)) then tlsp:GetBinaryImageForServlet(binary{$i//*:initialPedigree/*:altPedigree/*:data},'data') else ()";
 		    	log.info("Query : "+query2);
 		    	initialStatus=queryRunner.executeQuery(query2);
 		    	 
 		    	if(initialStatus != null && initialStatus.size()>0){
 	 		    	

 	 		    	String tempFilePath = getInitialPedigreePDF(initialStatus,str);
 	 		    		finalFilePath.add(tempFilePath);
 		    	
							
				}
 		    	MergedPDF =mergeListOfPDFs(finalFilePath);
 	 		}
 	 		
 	 		else if(recvdPed != null){
 	 			

 			  
 			   	      
 	 		    String repakPrevpeds = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree/*:previousPedigrees["+(i+1)+"]/*:pedigree return $i/*:receivedPedigree/*:pedigree/*:shippedPedigree";
 	 		    log.info("New"+repakPrevpeds);
 	 		    String  prvPed = queryRunner.returnExecuteQueryStringsAsString(repakPrevpeds);
 	 		     
 	 		    Node new1=XMLUtil.parse(prvPed);
 	 	 		Node new2=XMLUtil.getNode(new1,"/shippedPedigree");
 	 	 		
 	 	 	
 	 	 		if(new2 != null){
 	 	 				String serialNumber =XMLUtil.getValue(new2,"documentInfo/serialNumber");
 	 	 				log.info("The new ShippedPed Id "+serialNumber);
 	 	 				createInitialPDF(prvPed,serialNumber);
 	 	 		}
 	 		    
 	 

 			}
 	 		else if(shippedPed != null){
 	 			 
 	 			String serialNumber =XMLUtil.getValue(prevPed,"/previousPedigrees/pedigree/shippedPedigree/documentInfo/serialNumber");
 	 	 		log.info("The new ShippedPed Id "+serialNumber);
 	 	 		createInitialPDF(XMLUtil.convertToString(shippedPed),serialNumber);
 	 	 			
 	 	 		
 	 		}
 	 		    

 	 	}
 	 		
 	 		
             
              

     } catch (Exception e) {
    	 try{
         	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                     "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
 			e.printStackTrace();
         	}catch(Exception ex){}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
        return MergedPDF;
	}

//	    New Code - for Handling RepackageUseCases for Prepak : END
 	
 	
 	public File getBaseDirectory(){
 		String propVal = System.getProperty("com.rdta.dhforms.path");
 	
 		String[] a = propVal.split("%20");
    	String value = a[0];	
    	for(int i=1;i<a.length;i++){
    			value+=" "+a[i];
    	}
    	propVal = value;
    	log.info("baseDirectory "+propVal);
 		if( propVal == null){
 			log.info("Setting the FO base to current directory as the system property is missing");
 			propVal = ".";
 		}
 		File baseDirectory = new File(propVal);
 		
 		if(!baseDirectory.exists()){
 			throw new RuntimeException("The input directory of FO files '" + baseDirectory.getAbsolutePath() + "' doesn't exist" );
 		}
 		return baseDirectory;
 	}

	private String  createInitialPDF(String str, String pedigreeID) {
		
		 String filePath1="";
		log.info(" here in createInitialPDF.....");		
		
		try {
			log.info("pedigreeID"+pedigreeID);
			String xmlString = getInitialXSLString(str,pedigreeID, getBaseDirectory().getAbsolutePath() );
			
			File baseDir = getBaseDirectory();
			log.info("Inside Create InitialPDF");
			
            File outDir = new File(baseDir, "out");            
            outDir.mkdirs();
           
            // Setup input and output files                                                      
            File xmlfile = File.createTempFile("User", ".xml");   
            log.info("The Temp file location"+xmlfile.getAbsolutePath());
            
            // Delete temp file when program exits.
            //xmlfile.deleteOnExit();  
            
            // Write to temp file
            BufferedWriter bw = new BufferedWriter(new FileWriter(xmlfile)); 
            
            bw.write(xmlString);
            bw.close();
      
            File xsltfile = new File(baseDir, "/xsl/initial.fo");
            //File pdffile = File.createTempFile("initial", ".pdf");
            
            File pdffile = new File(outDir, pedigreeID.substring(11)+"1.pdf");           
            // Construct fop with desired output format
            log.info("b4");
            Fop fop = new Fop(MimeConstants.MIME_PDF);
            log.info("****AFTER FOP ********");
           
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
				String[] filePath = new String[2];
 		        filePath[0] = pdffile.getAbsolutePath();
 		        
//  New Code -for Handling RepackageUseCases for Prepak : START
                
 		        finalFilePath.add(filePath[0]);
		        
 		        Node ShippedPed = XMLUtil.parse(str);
 		        String serialNumber = XMLUtil.getValue(ShippedPed,"/shippedPedigree/documentInfo/serialNumber");
 		       
 	 	 		Node initialPed=XMLUtil.getNode(ShippedPed,"/shippedPedigree/initialPedigree");
 	 	 		Node recvdPed = XMLUtil.getNode(ShippedPed,"/shippedPedigree/pedigree/receivedPedigree");
 	 	 		Node shippedPed = XMLUtil.getNode(ShippedPed,"/shippedPedigree/pedigree/shippedPedigree");
 	 	 		
 	 	 		 if(initialPed !=null){
 	 	 			
 	 	 			String query2 = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree//*:shippedPedigree[*:documentInfo/*:serialNumber = '"+serialNumber+"']";
 	 		    	query2 = query2 + " return if(exists($i//*:initialPedigree/*:altPedigree)) then tlsp:GetBinaryImageForServlet(binary{$i//*:initialPedigree/*:altPedigree/*:data},'data') else ()";
 	 		    	log.info("Query : "+query2);
 	 		    	List initialStatus=queryRunner.executeQuery(query2);
 	 		    	 
 	 		    	if(initialStatus != null && initialStatus.size()>0){
 	 	 		    	

 	 	 		    	String tempFilePath = getInitialPedigreePDF(initialStatus,str);
 	 	 		    		finalFilePath.add(tempFilePath);
 	 		    	
 								
 					}
 	 		    	log.info("Size"+finalFilePath.size());
 	 		    	MergedPDF =mergeListOfPDFs(finalFilePath);
 	 		    	return MergedPDF;
 	 	 		
 	 	 		 }else if(recvdPed != null){
 		        
 		        Node new11=XMLUtil.parse(str);
		        Node new12=XMLUtil.getNode(new11,"/shippedPedigree/pedigree/receivedPedigree/pedigree/shippedPedigree");
		        if(new12 != null){
 	 	 		
 	 	 			 		    
 	 	 				String serialNumber1 =XMLUtil.getValue(new12,"documentInfo/serialNumber");
 	 	 				log.info("The Interior ShippedPed Id "+serialNumber1);
 	 	 				log.info("Interior SP "+new12);
 	 	 				createInitialPDF(XMLUtil.convertToString(new12),serialNumber1);
 	 	 		}
		         
 	 	 		}
 		       
 		      
                           
               log.info(" here in sendInitialPDF....."+filePath1);
                MergedPDF=mergeListOfPDFs(finalFilePath);
               
            }finally{
            	
            }                       
        } catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println();
			e.printStackTrace();
			try{
	        	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
	                    "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
				e.printStackTrace();
	        	}catch(Exception ex){}
		}
		
      
        return MergedPDF;
		
	}
	
	
//	    New Code - for Handling RepackageUseCases for Prepak : END
	
	
	 public String createPDF(String pedigreeID) throws Exception, Exception{
 		
 		String[] filePath= new String[2];
 		finalFilePath.removeAll(finalFilePath);
 		try {
 			
 			log.info("*********"+pedigreeID+"***********");
 			String query="tlsp:getRepackagedInfo('"+pedigreeID+"')";
 			
			String str=queryRunner.returnExecuteQueryStringsAsString(query);
			log.info("REsult in sendPDF method : "+str.length());
			
			if(str !=null){
				Node n1=XMLUtil.parse(str);
				log.info("-------ni is-----"+n1);

				Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
				log.info("-------ni is-----");

				Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
				log.info("The node here");	
				if(n3 != null){
					MergedPDF=createRepackagePDF(pedigreeID,str);
 					if(MergedPDF == null) throw new Exception();
 					filePath[0]=MergedPDF;
 				}else{
 					MergedPDF=createInitialPDF(str,pedigreeID);
 				}

			
 			}
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			try{
 				log.info("IN CATCH BLOCK 343");
 	        	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
 	                    "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
 				e.printStackTrace();
 	        	}catch(Exception ex){}
 			e.printStackTrace();
 		}
 		
 		
 		
 		//return filePath;
 	log.info("MERGED PDF"+MergedPDF);
 	
 		
 		return MergedPDF;

		
	}

	
 	private String getInitialPedigreePDF(List data,String pedigreeID) {
		// TODO Auto-generated method stub
		try {
			
			
			
			log.info("The no of Previous Pedigrees having AltPedigrees "+data.size());
			
			log.info("Result : "
					+ data.get(0).getClass().getName());
			
			InputStream stream = (ByteArrayInputStream) data.get(0);
			
		   byte[] data1 = new byte[128];
		   
		   File file = new File(getBaseDirectory()+"/out/InitialPedigree.pdf");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			
			DataOutputStream dos = new DataOutputStream(fos);
			int x = stream.read(data1);
			while (x != -1) {
				 //byte[] decoded = Base64.decode(data1);
							
				 dos.write(data1);
				 data1= new byte[128];
				 x = stream.read(data1);
			}
			stream.close();
			dos.close();
			fos.close();
			return file.getAbsolutePath();
        }
			
			catch (Exception e) {
			// TODO Auto-generated catch block
        	try{
            	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                        "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
    			e.printStackTrace();
            	}catch(Exception ex){}
			e.printStackTrace();
			return null;
		}
        
         
	}
 
/* 	
 	private String getInitialPedigreePDFList(List data,String pedigreeID) {
		// TODO Auto-generated method stub
		try {
			
			List altPedigreePdfPaths = new ArrayList();
			Iterator it = altPedigreePdfPaths.iterator();
			
			//StringBuffer buff = new StringBuffer();
			//buff.append("for $i in collection('tig:///ePharma/PaperPedigree')/initialPedigree[DocumentInfo/serialNumber = '31393843945529672246633950583774'] return $i/altPedigree/data/string()");
			//List data = queryRunner.executeQuery(buff.toString());
			log.info("The no of Previous Pedigrees having AltPedigrees "+data.size());
			
			
			for(int i=0;i<data.size();i++){
			log.info("Result : "
					+ data.get(i).getClass().getName());
			
			InputStream stream = (ByteArrayInputStream) data.get(0);
			
			byte b[]=data.get(i).toString().getBytes();
			
			InputStream stream =   new ByteArrayInputStream(b);
			
			
		   byte[] data1 = new byte[128];
		   
		   File file = new File(getBaseDirectory()+"/out/InitialPedigree"+i+".pdf");
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			
			DataOutputStream dos = new DataOutputStream(fos);
			int x = stream.read(data1);
			while (x != -1) {
				 
				 dos.write(data1);
				 data1= new byte[128];
				 x = stream.read(data1);
			}
			stream.close();
			dos.close();
			fos.close();
			log.info("The alt Pedigree of pedigree "+i+" is"+file.getAbsolutePath());
			
			altPedigreePdfPaths.add(file.getAbsolutePath());
			
       
			}
			log.info(altPedigreePdfPaths.size());
			return mergeAltPDFs(altPedigreePdfPaths);
			
			
        }
			
			catch (Exception e) {
			// TODO Auto-generated catch block
        	try{
            	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                        "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
    			e.printStackTrace();
            	}catch(Exception ex){}
			e.printStackTrace();
			return null;
		}
        
         
	}
 	
 	
*/
 	
 //New Code : To handle REpackage Usecase for PrePak : START
 	
 	public String mergeListOfPDFs(List pdfFilePaths) throws COSVisitorException, IOException{
		
 		AppendDoc merge = new AppendDoc(); 
 		CopyDoc copyFile =new CopyDoc();
 		CreatePDF form = new CreatePDF();
 		File baseDir =form.getBaseDirectory();
 		File outDir = new File(baseDir, "out");            
 	    outDir.mkdirs();
 	    count++;
 	    File outputFile = new File(outDir, "FinalMergedPDF"+count+".pdf"); 
 	    outputFile.createNewFile();
 	    String fileout = outputFile.getAbsolutePath();

 		ListIterator it=pdfFilePaths.listIterator();
 		 
 		if(pdfFilePaths.size()== 1 ){
 			copyFile.doIt(it.next().toString(),outputFile.getAbsolutePath());
 			
 		}else{
 	      while(!it.hasPrevious()){
 	    	
 	    	  String x =it.next().toString();
 	    	  merge.doIt(x,it.next().toString(),fileout);    	  
 	    	  	  
 	      }
 	      while(it.hasNext()){
 	    	  
 	    	  
 	    	  String file1= it.next().toString();
 	  
 	           	merge.doIt(fileout,file1,fileout);
 	      }
 		}
 		log.info("PDF merged...");
 		return fileout;

 	}

// New Code : To handle REpackage Usecase for PrePak : END
 	
public boolean checkManufacturer(String str){
		
		Node n1=XMLUtil.parse(str);
		Node n2 = XMLUtil.getNode(n1,"descendant::repackagedPedigree");
		
		if((XMLUtil.getNode(n2,"descendant::receivedPedigree"))==null){
			if((XMLUtil.getNode(n2,"descendant::altPedigree"))==null){
				return true;
			} else {
				String str1 = XMLUtil.getValue(n2, "descendant::altPedigree/data");
				log.info("+++++++++++++++++++altPedigreeee Dataaaaaaaaa: +++++++++++++++" + str1);
				if(str1 == null || str1.trim().equals("")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public Node getTransactionInfo(Node n){
		
		
		if(XMLUtil.getNode(n,"descendant::receivedPedigree")!=null){
			return XMLUtil.getNode(n,"descendant::shippedPedigree/transactionInfo");
		}else{
			return XMLUtil.getNode(n,"descendant::initialPedigree/transactionInfo");
		}
	}
 	public static void main(String args[]){
 		try{
 		CreatePDF dh = new CreatePDF();
 		
    	dh.createPDF("fff36b69-0339-13c0-c001-eff841af394a");
    			//fff36b97-2f37-1780-c001-eff841af394a
    	
 		
 		}catch(Exception e){
 			
 		}
 	}
}
