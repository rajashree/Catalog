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
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.exceptions.WrappedException;
import org.w3c.dom.Node;

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
 		System.out.println("Value of node ***** : "+n2 + " signer Name : "+str);
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
				System.out.println("Value of i in iterator loop***** : "+i);
			}
			
			//adding new code here...
			System.out.println("Value of i ***** : "+i);
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
 		buffer.append("</shippedPedigree>");
 		log.info("XML :"+buffer.toString());		
 		 	
 		getSignatureImage1(strName,signEmail); 
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
 			System.out.println("Pciture file returieve " + bfr );
 			byte[] rslt = helper.ReadTL(Stmt, bfr.toString());	 			  			
 			File pictFile =  new File(basedir.getAbsolutePath()+"\\xsl\\Signature.jpeg");
 			System.out.println("Pciture file Path:" + pictFile.getAbsolutePath() );
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
		// String fileout="C:/Documents and Settings/Jagadish Pampatwar/workspace/Scheduler_MD/out/DHMergedPedigreeForm.pdf";
	     /*File f = new File(fileout);
	     f.delete();*/
	     
		//merge.doIt("C:/Temp/ap-DH2135Pedigree.pdf", "C:/Temp/ap-DH2129Pedigree.pdf", "C:/Temp/out1.pdf");
     	try{
     		FileInputStream f1=new FileInputStream(file2);
     		FileInputStream f2=new FileInputStream(file1);
            merge.doIt(file2, file1, fileout);
            System.out.println("after merge");
     	}
     	catch(FileNotFoundException e){
     		System.out.println("file not found exception");
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
	
public void removeOldmergePDF() throws Exception    {
		
		AppendDoc merge = new AppendDoc(); 
		System.out.println("removeOldmergePDF");
          String fileout=getBaseDirectory()+"/out/DHMergedPedigreeForm.pdf";
		// String fileout="C:/Documents and Settings/Jagadish Pampatwar/workspace/Scheduler_MD/out/DHMergedPedigreeForm.pdf";
	      File f = new File(fileout);
	     f.delete(); 
	     
		 
	 	
	}
	
	public String getRepackageXSLString(String str,String pedigreeID,String uri)throws Exception{
    	
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
		buffer.append("</shippedPedigree>");
		log.info("XML :"+buffer.toString());
		getSignatureImage1(strName,signEmail);
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
 		 String mergeFilePath = "";
 		try {

			System.out.println(" here in sendRepackagePDF....."+str);
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
            File pdffile = new File(outDir, "repack.pdf");

           
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
               
 		       filePath[0] = pdffile.getAbsolutePath();
 			   
  		      
 		      if(!checkMan){

 		    	/* String query = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']/*:repackagedPedigree/*:previousPedigrees/*:initialPedigree ";
 		    	query = query + "return  tlsp:GetBinaryImageForServlet(binary{$i/*:altPedigree/*:data},'data')";*/
 		    	 String query = "for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree ";
 				query = query + "where  $i/*:documentInfo/*:serialNumber = '"+pedigreeID+"' ";
 				query = query + "return if(exists($i//*:initialPedigree/*:altPedigree)) then tlsp:GetBinaryImageForServlet(binary{$i//*:initialPedigree/*:altPedigree/*:data},'data') else ()";

 		        
 		    	 System.out.println("Query : "+query);
 		    	List initialStatus = queryRunner.executeQuery(query);
 		    	if(initialStatus != null && initialStatus.size()>0){
 		    	//String initialStatus = queryRunner.returnExecuteQueryStringsAsString(query);
 		    	filePath[1] = getInitialPedigreePDF(initialStatus,str);
 		    	//filePath[1] = getInitialPedigreePDF1(initialStatus,str);
 		    	 System.out.println("file path : "+filePath[1]);
 		    	 mergeFilePath=mergePDF(filePath[1],filePath[0]);
 		    	}
 		    	 		    		 
 		      }
 		      else mergeFilePath = pdffile.getAbsolutePath();
              System.out.println(" file path : "+mergeFilePath);

     } catch (Exception e) {
    	 try{
         	SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
                     "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
 			e.printStackTrace();
         	}catch(Exception ex){}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return mergeFilePath;
	}
 	
 	private File getBaseDirectory(){
 		System.out.println("getBaseDirectory");
 		
 		//String propVal = System.getProperty("com.rdta.dhforms.path");
 		String propVal = "C:\\Documents and Settings\\Rajashree\\workspace\\Scheduler_MD";
 		if( propVal == null){
 			System.out.println("Setting the FO base to current directory as the system property is missing");
 			propVal = ".";
 		}
 		File baseDirectory = new File(propVal);
 		if(!baseDirectory.exists()){
 			throw new RuntimeException("The input directory of FO files '" + baseDirectory.getAbsolutePath() + "' doesn't exist" );
 		}
 		System.out.println("getBaseDirectory"+baseDirectory);
 		
 		return baseDirectory;
 	}

	private String  createInitialPDF(String str, String pedigreeID) {

		 String filePath1="";
		System.out.println(" here in createInitialPDF.....");		
		
		try {
			String xmlString = getInitialXSLString(str,pedigreeID, getBaseDirectory().getAbsolutePath());
			
			File baseDir = getBaseDirectory();
			//File baseDir = new File("C:/Documents and Settings/Jagadish Pampatwar/workspace//Scheduler_MD");
			
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
            
            String newpedigreeID = pedigreeID.replaceAll("urn:uuid:","");
            File pdffile = new File(outDir,newpedigreeID+"1.pdf");           
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
				String[] filePath = new String[2];
 		       filePath[0] = pdffile.getAbsolutePath();
 			  /* String query = "for $i in collection('tig:///ePharma/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree[*:documentInfo/*:serialNumber = '"+pedigreeID+"']//*:initialPedigree ";
                query = query + "return  tlsp:GetBinaryImageForServlet(binary{$i/*:altPedigree/*:data},'data')";*/
 		      String query = "for $i in collection('tig:///ePharma_MD/ShippedPedigree')/*:pedigreeEnvelope/*:pedigree/*:shippedPedigree ";
 				query = query + "where  $i/*:documentInfo/*:serialNumber = '"+pedigreeID+"' ";
 				query = query + "return if(exists($i//*:initialPedigree/*:altPedigree)) then tlsp:GetBinaryImageForServlet(binary{$i//*:initialPedigree/*:altPedigree/*:data},'data') else ()";

 		        
                System.out.println("Query : "+query);
                 List initialStatus = queryRunner.executeQuery(query);
                //String initialStatus = queryRunner.returnExecuteQueryStringsAsString(query);
                String[] mergeFilePath = new String[1];
                if(initialStatus != null && initialStatus.size()>0){
                	 filePath[1] = getInitialPedigreePDF(initialStatus,str);
                	//filePath[1] = getInitialPedigreePDF1(initialStatus,str);
                	System.out.println("file path : "+filePath[1]);
                	filePath1=mergePDF(filePath[1],filePath[0]);
                }else{ mergeFilePath[0] = pdffile.getAbsolutePath();
                System.out.println(" file path : "+mergeFilePath[0]);
                filePath1=mergeFilePath[0];
                }
                           
               System.out.println(" here in sendInitialPDF....."+filePath1);
                
               
            }finally{
            	
            }                       
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try{
	        	/*SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
	                    "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
				e.printStackTrace();*/
	        	}catch(Exception ex){}
		}
		
        return filePath1;
		
	}

	 public String[] createPDF(String pedigreeID){
 		
 		String[] filePath= new String[2];
 		try {
 			
 			String query="tlsp:getRepackagedInfo_MD('"+pedigreeID+"')";
 			System.out.println("query in sendPDF method : "+query);
			String str=queryRunner.returnExecuteQueryStringsAsString(query);
			System.out.println("REsult in sendPDF method : "+str);
			
			if(str !=null){
				Node n1=XMLUtil.parse(str);
				System.out.println("-------ni is-----"+n1);

				Node n2=XMLUtil.getNode(n1,"/shippedPedigree");
				Node n3 = XMLUtil.getNode(n2,"descendant::repackagedPedigree");
					
				if(n3 != null){
 					String f=createRepackagePDF(pedigreeID,str);
 					if(f == null) throw new Exception();
 					filePath[0]=f;
 				}else{
 					filePath[0]=createInitialPDF(str,pedigreeID);
 				}

			
 			}
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			try{
 				System.out.println("IN CATCH BLOCK 343");
 	        	/*SendDHFormEmail.sendMailToSupport("greenfax@southwoodhealthcare.com","sw-epharma-support@rainingdata.com","smarthost.coxmail.com",
 	                    "Error in Create PDF",e.getMessage(),"pedigree@southwoodhealthcare.com","60empire");
 				e.printStackTrace();*/
 	        	}catch(Exception ex){}
 			e.printStackTrace();
 		}
 		
 		
 		
 		return filePath;
 	

		
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
		  	;
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
			
        } catch (Exception e) {
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
 
public boolean checkManufacturer(String str){
		
		Node n1=XMLUtil.parse(str);
		Node n2 = XMLUtil.getNode(n1,"descendant::repackagedPedigree");
		
		if((XMLUtil.getNode(n2,"descendant::receivedPedigree"))==null){
			if((XMLUtil.getNode(n2,"descendant::altPedigree"))==null){
				return true;
			} else {
				String str1 = XMLUtil.getValue(n2, "descendant::altPedigree/data");
				System.out.println("+++++++++++++++++++altPedigreeee Dataaaaaaaaa: +++++++++++++++" + str1);
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
