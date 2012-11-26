/*
 * Created on Aug 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

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

package com.rdta.epharma.reports.action;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rdta.catalog.trading.action.LocationAction;
import com.rdta.commons.persistence.PersistanceException;
import com.rdta.commons.persistence.QueryRunner;
import com.rdta.commons.persistence.QueryRunnerFactory;
import com.rdta.commons.persistence.TLQueryRunner;
import com.rdta.commons.xml.XMLUtil;
import com.rdta.epharma.reports.form.OutPutFieldPath;
import com.rdta.epharma.reports.form.PedigreeReportForm;
import com.rdta.epharma.reports.form.ShowOutPutFieldForm;
import com.rdta.epharma.reports.util.FieldMappingBean;
import com.rdta.epharma.reports.util.ReportDAO;

/**
 * @author mgawbhir
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SubmitReportFilters extends Action
{
    private static Log log=LogFactory.getLog(SubmitReportFilters.class);
    private static final QueryRunner queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    
    private static String shippedQuery =   "declare function local:getItemInfo($node as node()){"+
	"if( exists($node/*:shippedPedigree/*:itemInfo)) then" +
	" $node/*:shippedPedigree/*:itemInfo "+
	"else if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then"+ 
	"( $node/*:shippedPedigree/*:repackagedPedigree/*:itemInfo )"+
	"else if( exists($node/*:shippedPedigree/*:initialPedigree/*:itemInfo) )then"+
	"( $node/*:shippedPedigree/*:initialPedigree/*:itemInfo) else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getItemInfo($node/*:shippedPedigree/*:pedigree) else local:getItemInfo($node/*:receivedPedigree/*:pedigree)"+

	" }; declare function local:getProductInfo($node as node()){"+
	"if (exists($node/*:shippedPedigree/*:repackagedPedigree ))then"+ 
	" $node/*:shippedPedigree/*:repackagedPedigree/*:productInfo "+ 
	" else if( exists($node/*:shippedPedigree/*:initialPedigree) )then"+
	" $node/*:shippedPedigree/*:initialPedigree/*:productInfo else if(exists($node/*:shippedPedigree/*:pedigree))then  local:getProductInfo($node/*:shippedPedigree/*:pedigree) else local:getProductInfo($node/*:receivedPedigree/*:pedigree) };";
	

	
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws Exception {
    	System.out.println(".....coming to action here SubmitReportFilters.....");
    	
    	String offset = request.getParameter("offset");
		if(offset != null){
			int off = Integer.parseInt(offset);
			System.out.println("The value of offset in PedigreeSearchAction is :"+off);
		}else{
			offset="0";
		}
		
				System.out.println("Pagenation "+request.getParameter("pageNumber"));
				request.setAttribute("offset", offset);
		
		try{
		log.info("Control at execute() of SubmitReportFilters Class");
		HttpSession session = request.getSession(false);
		Collection coll = new ArrayList();
		Collection showReport = new ArrayList();
		
		/*
		if(pedigreeReportForm==null ||pedigreeReportForm.getOutputFieldList()==null)
			throw new Exception("ReportForm is not in session");
		String[] output = pedigreeReportForm.getOutputFieldList();
		
		String cubeName = pedigreeReportForm.getCubeName();
		
		
		String xQuery = ""; */
		
		String xQuery ="";
		String returnClause = (String)session.getAttribute("returnclause");
		String cubeName = (String )session.getAttribute("cubeName");
		//xQuery = makeFilterCondition(request,cubeName);
		
		log.info(" calling the makeOutputCondition method to form the complete Query for generation Report");
		xQuery = xQuery + " "+makeOutputCondition(request,returnClause,cubeName);
		
		
		
		List showFields = queryRunner.returnExecuteQueryStrings(xQuery);
		
		log.info("In the execute() method of SubmitReportFilters  ");
		log.info("Parsing the Data Based on the Selected OutPutFields");
		int showLength = showFields.size();
		String [] fields =(String[]) session.getAttribute("fields");
		int fileldLength = fields.length;
		showReport.add(fields);
		
		//The below code will parse the node returned by the stored Procedure
		//The code will handle the multiple itemInfo node also
	
		
		
		for (int i=0;i<showLength;i++){
			
		log.info("Parsing the individual nodes returned by the storedProcedure");
			Node node= XMLUtil.parse((String)showFields.get(i));
		log.info("Checking the existence of  multiple itemInfo Nodes ");
			Node tempNode = XMLUtil.getNode(node,"itemInfo");
			NodeList itemNode = tempNode.getChildNodes();
			int itemLength = itemNode.getLength();
			
			
		for(int j=0;j<itemLength;j++){
			boolean itemflag = false;
			ShowOutPutFieldForm showForm = new ShowOutPutFieldForm();
			String[] temp= new String[fileldLength];
			for(int k=0;k<fileldLength;k++){
			
				String path = (String)OutPutFieldPath.pathMap.get(fields[k]);
				log.info("Path is "+path);
				String value=null;
				if((path.indexOf("itemInfo")!=-1)){
						value = XMLUtil.getValue(itemNode.item(j),path.substring(path.lastIndexOf("/")+1,path.length()));	
				itemflag=true;
				}else{
				value = XMLUtil.getValue(node,path);
				
				}
				if(value != null){
				temp[k]=value; }else{
					temp[k]=new String("N/A");
					System.out.println(" came here");
				}
				
			}
		
			showReport.add(temp);
			showForm.setFields(temp);
			
		
			coll.add(showForm);
			if(!itemflag){
				j=itemLength;
			}
		}
		}
		
		HttpSession sess = request.getSession(false);
		sess.setAttribute("showOutFields",coll);
		sess.removeAttribute("showReport");
		sess.setAttribute("showReport",showReport);
	//	request.setAttribute("showOutFields",coll);
	//	request.setAttribute("showReport",showReport);
		}catch(PersistanceException e)
		{
			log.error("Error in   SubmitReportFilters.execute()" + e);
			throw new PersistanceException(e);	
		}
		
		
		return mapping.findForward("success");
   }
    
    
    
    public String createXQuery(String cubeName)
    {
     	String xQuery = "";
    	xQuery = "for $i in collection(tig://ePharma/Pedigree) ";
    	xQuery = xQuery+ "tlsp:getReportOuputFields('"+cubeName+"')";

     	return xQuery;
    }
    
    
    
    
    public String makeOutputCondition(HttpServletRequest request,String clause,String cubeName) {
    	
    		
    	
    	
    	log.info("Executing  the makeOutputCondition( ) of SubmitReportFilters  .....") ;
    	
    		String [] textFields = request.getParameterValues("key");
    		String [] elements = request.getParameterValues("elements");
    		String [] status = request.getParameterValues("select");
    		String from= request.getParameter("from");
    		String to= request.getParameter("to");
    		String where =" where ";
    		String select="";
    		String selectStatus=request.getParameter("selectstatus");
    		String collection="";
    		
    		String [] selectoptions = request.getParameterValues("select");
    		System.out.println("selectlength"+selectoptions.length);
    		
    		log.info("Befor Selecting the Collection name and where clasue");
    		//Based on the cube name It will select the Collection Name and Condition
    		
    		if(cubeName.equals("shippedPedigreeByTradingPartnerPerDateSpan")||cubeName.equals("failedPedigreeByTradingPartnerPerDateSpan")||cubeName.equals("pedigreeByStatusPerTradingPartner")){
    			select = "$i/*:transactionInfo/*:recipientInfo/*:businessAddress/*:businessName = '";
    			collection="ShippedPedigree";
    		}else{
    			select = " $i/*:transactionInfo/*:senderInfo/*:businessAddress/*:businessName = '";
    			collection="ReceivedPedigree";
    		}
    			
    		//Based on the filters entered by the use it will the complet where clause
    		
    		
    		
    		if(!(textFields[0].length() == 0)){
    				where=where+select+textFields[0]+"' ";
    				if ((from != null && from.length()!= 0)&& (to !=null && to.length() !=0 )){
    					where = where + status[0]+" ( ($i/*:transactionInfo/*:transactionDate/text() >= '"+request.getParameter("from")+"' ) and"; 
    					where = where +" ($i/*:transactionInfo/*:transactionDate/text()  <= '"+request.getParameter("to")+"' ) )";
    					if(selectStatus != null&& selectStatus.length()!= 0){
    						where = where +status[1]+"( for $i in collection('tig:///ePharma/PedigreeStatus' )/PedigreeStatus[PedigreeID = data($i/*:documentInfo/*:serialNumber)]"+ 
    						" let $x := $i/TimeStamp "+ 
    						" return for $p in $i/Status [ StatusChangedOn = $x ]"+
    						" return if($p/StatusChangedTo = '"+selectStatus+"') then true() else false() )";
    					}
    					
    				}else{
    					
    					if(selectStatus != null&& selectStatus.length()!= 0){
    						where = where +status[0]+" (( for $i in collection('tig:///ePharma/PedigreeStatus' )/PedigreeStatus[PedigreeID = data($i/*:documentInfo/*:serialNumber)]"+ 
" let $x := $i/TimeStamp "+ 
" return for $p in $i/Status [ StatusChangedOn = $x ]"+
" return if($p/StatusChangedTo = '"+selectStatus+"') then true() else false() ) = true() ) ";
    					}
    				}
    				
    			}else if ((from != null && from.length()!= 0)&&(to !=null && to.length() !=0 )){
    					where = where +" ( ($i/*:transactionInfo/*:transactionDate/text() >= '"+request.getParameter("from")+"' ) and"; 
    					where = where +"  ($i/*:transactionInfo/*:transactionDate/text() <= '"+request.getParameter("to")+"' ) )";
    					if(selectStatus != null && selectStatus.length()!= 0){
    						where = where +status[1]+" ((  for $i in collection('tig:///ePharma/PedigreeStatus' )/PedigreeStatus[PedigreeID = data($i/*:documentInfo/*:serialNumber)]"+ 
    						" let $x := $i/TimeStamp "+ 
    						" return for $p in $i/Status [ StatusChangedOn = $x ]"+
    						" return if($p/StatusChangedTo = '"+selectStatus+"') then true() else false() ) = true()) ";
    					}
    				
    			}else{
					if(selectStatus != null && selectStatus.length()!= 0){
						where =where + " ( ( for $i in collection('tig:///ePharma/PedigreeStatus' )/PedigreeStatus[PedigreeID = data($i/*:documentInfo/*:serialNumber)]"+ 
						" let $x := $i/TimeStamp "+ 
						" return for $p in $i/Status [ StatusChangedOn = $x ]"+
						" return if($p/StatusChangedTo = '"+selectStatus+"') then true() else false() ) = true() ) ";					}
				}
    			
    		String collectionInf = " let $data := (for $i in collection ('tig:///ePharma/"+collection+"')/*:pedigreeEnvelope/*:pedigree//*:shippedPedigree "+
    		" let $pedenv := $i/.."+
    		" let $pedenvhome := $pedenv/.."+
    		" let $x := $pedenvhome/..";
    //		" let $reportStatus := tlsp:getreportStatus(data($i/documentInfo/serialNumber)cast as xs:string,'"+selectStatus+"')/text()"+
    //		" where ";
    		
    		if(where.equals(" where "))
    			where="";
    		String query=shippedQuery+collectionInf+where+" return  <out><pedigreeId>{data($i/*:documentInfo/*:serialNumber)}</pedigreeId><transactionInfo>{$i/*:transactionInfo/*:*}</transactionInfo>" +
    				"<productInfo>{local:getProductInfo($i/parent::*:*)/*:*}</productInfo><itemInfo>{local:getItemInfo($i/parent::*:*)}</itemInfo>"+
    				"<envolopeId>{data( $x/*:pedigreeEnvelope/*:serialNumber)}</envolopeId></out> ) return $data ";

    		log.info("Where Clause is "+where);
    		log.info("\n\n Finla Query is "+query);
    		log.info("Returning from the makeOutputCondition( ) of SubmitReportFilters ") ;
    		
    	
    	
    	return query;
    }
    
    
    
    
    public String makeFilterCondition(HttpServletRequest request,String cubeName)
       {
    	HashMap filterMap = new ReportDAO().getMap("filter",cubeName);
    	ArrayList filterMappingList = (ArrayList)filterMap.get(cubeName);
		String xQuery = "";
		xQuery = "for $apn at $count in collection('tig:///ePharma/APN')/APN/Pedigrees/Pedigree where ";
		
		//form the where clause
		Iterator it  = filterMappingList.iterator();
		String[] operator = request.getParameterValues("selectAndOr");
		int i=-1;
		String op = "";
		
		while(it.hasNext())
		{
			FieldMappingBean bean = (FieldMappingBean)it.next();
			String nextEle = bean.getKey();
			String eleValue = request.getParameter(nextEle);
			i++;
			if(nextEle.equals("dateRange"))
			{
				String dateFrom = request.getParameter("fromDtReceived");
				String dateTo = request.getParameter("toDtReceived");
				if(dateFrom!=null && !dateFrom.equals("") && dateTo!=null && !dateTo.equals(""))
				{
					xQuery = xQuery+"$apn/"+bean.getXPath()+ " >= '"+ dateFrom +"' ";
					xQuery = xQuery+" and $apn/"+bean.getXPath()+" <= '"+ dateTo+"' ";
					xQuery = xQuery+" "+operator[i]+" ";
					op = operator[i];
				}
			}else
			if(nextEle.equals("address"))
			{
				String address = request.getParameter("address");
				
				if(address!=null && !address.equals(""))
				{
					xQuery = xQuery+"contains($apn/"+bean.getXPath()+ " , '"+ address +"') ";
					xQuery = xQuery+" "+operator[i]+" ";
					op = operator[i];
				}
			}
			else{
				System.out.println("----element name-1----- "+nextEle+" -------------");
				System.out.println("----element value------ "+eleValue+" -------------");
				System.out.println("----operator value------ "+operator[i]+" -------------");	

				if(eleValue!=null && !eleValue.equals(""))
				{
					xQuery = xQuery+"$apn/"+bean.getXPath()+"= '"+eleValue+"' ";
					xQuery = xQuery+" "+operator[i]+" ";
					op = operator[i];
				}
				
			}
		}
		System.out.println("xQuery1111 "+xQuery);
		int index = xQuery.lastIndexOf(op);
		xQuery = xQuery.substring(0,index);
		System.out.println("xQuery 222 "+xQuery);
		return xQuery;
    }
    public String makeOutputCondition(HttpServletRequest request,String[]output,String cubeName)
    {
    	String xQuery = "";
		xQuery = makeFilterCondition(request,cubeName);

    	HashMap outputMap =  new ReportDAO().getMap("output",cubeName);
		ArrayList outputMappingList = (ArrayList)outputMap.get(cubeName);
		Iterator it = outputMappingList.iterator();
		xQuery = xQuery + " return if($count mod 2 = 0) then <TR  class='tableRow_On'>";
		String tableTop = "<tr class='tableRow_Header'> ";
		String clause = "";
		for(int a=0;a<output.length;a++)
		{
			System.out.println("----filedName------ "+ output[a]+" -------------");
			
			it  = outputMappingList.iterator();
			
			while(it.hasNext())
			{
				FieldMappingBean bean = (FieldMappingBean)it.next();
				String nextEle = bean.getKey();
				System.out.println("----output here from bean is "+nextEle+"--x path---"+bean.getXPath());
				if(nextEle.equals(output[a]) )
				{
					clause = clause+ "  <TD class='td-menu'> {data($apn/"+bean.getXPath()+" )} </TD> ";
					//xQuery = xQuery + "  <TD class='td-menu'> {data($apn/"+bean.getXPath()+" )} </TD> ";
					tableTop = tableTop+" <TD class='type-whrite' align='center'> "+ bean.getFieldName()+" </TD>";
				}
				
			}
		}
		System.out.println("clause is-------- "+clause);
		xQuery = xQuery + clause+ " </TR> else <TR> "+clause+" </TR>";
		tableTop = tableTop+"</TR>";
		StringBuffer str= new StringBuffer();
		try{

			TLQueryRunner queryRunner = new TLQueryRunner();
			List list = queryRunner.returnExecuteQueryStrings(xQuery);
			
			if(list!=null && list.size()>0)
			{
				
				for(int i=0;i<list.size();i++)
				{
					str = str.append(list.get(i));
				}
				
			}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			tableTop = tableTop+"  "+str.toString()+" </TABLE>";
			
			System.out.println("the final output for jsp is "+tableTop);
			request.setAttribute("htmlString",tableTop);
			HttpSession session = request.getSession();
			session.setAttribute("query",tableTop);
		
		
		return xQuery;
    }
    
}
