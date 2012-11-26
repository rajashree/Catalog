
 
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

 

package com.rdta.eag.epharma.sendmail;

import java.beans.PersistenceDelegate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.Node;

import com.rdta.Admin.servlet.RepConstants;
import com.rdta.commons.CommonUtil;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.eag.epharma.util.CreateSignatureImage;
import com.rdta.eag.epharma.util.SendDHFormEmail;

/**
 *
 *	@author     <a href="mailto:developer_email">Johnson Joseph</a>
 *
 *	Date:		May 26, 2006 11:48:26 AM
 *     
 *	@version    $Id :$
 *
 *	@since   	REL1.0
 */

public class Send2135Action extends Action{

	/** The commons <code>logger</code> instance for this class. */
	private final static Log logger = LogFactory.getLog(Send2135Action.class);
	static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
	

	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		
		try {
			logger.info("Insdie Send2135Action execute()");
			
			SendDHFormEmail mail=new SendDHFormEmail();						
			String pedigreeID = request.getParameter("PedigreeId");
            String str=queryRunner.returnExecuteQueryStringsAsString("tlsp:getRepackagedInfo('"+pedigreeID+"')");
			
            String xmlString=getXSLString(str,pedigreeID,getServlet().getServletContext().getRealPath("/"));
            			
			boolean checkMan = checkManufacturer(str);
			System.out.print("Manufacturer or Not "+ checkMan);
			
            File baseDir = new File( getServlet().getServletContext().getRealPath("/") );
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
                MessageResources messageResources = getResources(request);                                
                String result =SendDHFormEmail.sendDHFormEmailAttachement(messageResources.getMessage("eMail.From"),messageResources.getMessage("eMail.To"),messageResources.getMessage("eMail.SMTP.Server"),
                messageResources.getMessage("eMail.Subject"),messageResources.getMessage("eMail.Message"),messageResources.getMessage("eMail.Username"),messageResources.getMessage("eMail.Password"),pdffile.getAbsolutePath());
                 
                
                if(("Success").equalsIgnoreCase(result))
                    request.setAttribute("emailStatus",result);
                    else                
                    request.setAttribute("emailStatus","failure"); 
                
               
            }finally{
            	 
            }
                       
        } catch (PersistanceException p) {
        	request.setAttribute("pagenm","pedigree");
            throw new PersistanceException(p); 
            
        }
		
	
		request.setAttribute("form","2129");
		
		return mapping.findForward("success");
	}
	
	
	public String getXSLString(String str,String pedigreeID,String uri)throws Exception{
    	
    	
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
		
		
		StringBuffer buffer1 = new StringBuffer();
 		buffer1.append("<Processes><ProcessId>14983</ProcessId>");
 		buffer1.append("<PEDSHIP><messageNm>PEDSHIP</messageNm>");
 		buffer1.append("<signerId>23802960038774853494053340184062</signerId>");
 		buffer1.append("<TransactionInfo>Some data</TransactionInfo>");
 		buffer1.append("<ProductInfo><drugName>LACTULOSE</drugName>");
 		buffer1.append("<productCode>58016-5455-01</productCode>");
 		buffer1.append("<dosageForm>SOLUTION</dosageForm><strength>10GM/15ML</strength>");
 		buffer1.append("<physicalSize>120ML</physicalSize><containerSize>1</containerSize>");
 		buffer1.append("<UseVendorLot>N</UseVendorLot><Manufacturer>SOUTHWOOD PHARMACEUTICALS, INC.</Manufacturer>");
 		buffer1.append("<LotInfo><lot>AV530</lot><quantity>2</quantity><ExpirationDate>2007-10-31T00:00:00</ExpirationDate>");
 		buffer1.append("<Comment>This lot number has been in our inventory since 1st June 2006</Comment></LotInfo>");
 		buffer1.append("</ProductInfo><ProductInfo><drugName>LACTULOSE</drugName><productCode>58016-5455-01</productCode>");
 		buffer1.append("<dosageForm>SOLUTION</dosageForm><strength>10GM/15ML</strength>");
 		buffer1.append("<physicalSize>120ML</physicalSize><containerSize>1</containerSize>");
 		buffer1.append("<UseVendorLot>Y</UseVendorLot><Manufacturer>SOUTHWOOD PHARMACEUTICALS, INC.</Manufacturer>");
 		buffer1.append("<LotInfo><lot>BR899</lot><quantity>100</quantity>");
 		buffer1.append("<ExpirationDate>2007-10-31T00:00:00</ExpirationDate>");
 		buffer1.append("<Comment>This is the second comment</Comment></LotInfo></ProductInfo></PEDSHIP></Processes>");
 		
 		List lotMessages = getLotInfo(buffer1.toString());
		
 		System.out.println("No of elements in the list"+lotMessages.size());
 		Iterator lotMessageIterator = lotMessages.iterator();
		
		
		
		int i=0;
		List ls= new ArrayList();
		Iterator it=XMLUtil.executeQuery(n2,"itemInfo").iterator();
		//Iterator it=XMLUtil.executeQuery(n3,"itemInfo").iterator();
		while(it.hasNext()){
			
			Node n4= (Node)it.next();
			
			buffer.append(XMLUtil.convertToString(n4,true));
			i++;
			
			if(i==1){
				String lotNumber = XMLUtil.getValue(n4,"lot");
				
				while(lotMessageIterator.hasNext()){
					
					Node lotInfoNode = (Node)lotMessageIterator.next();
					String lotNum = XMLUtil.getValue(lotInfoNode,"lot");
					if(lotNum!=null){
						if(lotNum.equals(lotNumber)){
							buffer.append(XMLUtil.convertToString(XMLUtil.getNode(lotInfoNode,"Comment"),true));
							break;
						}
				}
			}
			break;
			}
			
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
						
						if(i==1){
							String lotNumber = XMLUtil.getValue(itemNode,"lot");
							
							while(lotMessageIterator.hasNext()){
								
								Node lotInfoNode = (Node)lotMessageIterator.next();
								String lotNum = XMLUtil.getValue(lotInfoNode,"lot");
								if(lotNum!=null){
									if(lotNum.equals(lotNumber)){
										buffer.append(XMLUtil.convertToString(XMLUtil.getNode(lotInfoNode,"Comment"),true));
										break;
									}
							}
						}
						break;
						}
						
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
					
					i++;
					if(i==1){
						String lotNumber = XMLUtil.getValue(n4,"lot");
						
						while(lotMessageIterator.hasNext()){
							
							Node lotInfoNode = (Node)lotMessageIterator.next();
							String lotNum = XMLUtil.getValue(lotInfoNode,"lot");
							if(lotNum!=null){
								if(lotNum.equals(lotNumber)){
									buffer.append(XMLUtil.convertToString(XMLUtil.getNode(lotInfoNode,"Comment"),true));
									break;
								}
						}
					}
					break;
					}
					
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
			
			if((XMLUtil.getNode(intialShipped,"transactionInfo/senderInfo/contactInfo"))!=null)
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
				
				if(XMLUtil.getNode(shippedNode,"transactionInfo/senderInfo/contactInfo")!=null)
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
		
		
		
		
		CreateSignatureImage.getSignatureImage(strName,signEmail);
		return buffer.toString();
    	
    }
   
/*	public boolean compare(Node n1){
	
	Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
	Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
	
	
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
	return false;
}
	*/
	
 	public boolean compare(Node n1){
		
		Node businessAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/businessAddress");
		Node shippingAddress = XMLUtil.getNode(n1,"transactionInfo/senderInfo/shippingAddress");
		
		if(businessAddress!=null && shippingAddress != null){
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

	/*
	 * To check whether the repackager got from manufacturer or wholesaler
	 */
	public boolean checkManufacturer(String str){
		
		Node n1=XMLUtil.parse(str);
		Node n2 = XMLUtil.getNode(n1,"descendant::repackagedPedigree");
		
		if((XMLUtil.getNode(n2,"descendant::receivedPedigree"))==null){
			if((XMLUtil.getNode(n2,"descendant::altPedigree"))==null){
				return true;
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
	
	
	
	public List getLotInfo(String pedShipMessage) throws PersistanceException{
 		
 		System.out.println("pedShip Node" +pedShipMessage);
 		String query = "tlsp:ReturnLotInfo("+pedShipMessage+")";
 		String result = queryRunner.returnExecuteQueryStringsAsString(query);
 		Node n1= XMLUtil.parse(result);
 		List lotInfo = XMLUtil.executeQuery(n1,"LotInfo");

 		System.out.println("List size is :"+lotInfo.size());

 		return lotInfo; 
 	}
	
	
	
	
}

