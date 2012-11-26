/*
 * Created on Sep 29, 2005
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

 package com.rdta.epharma.epedigree.action;

/**
 * @author vijayalakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
   import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    
    
    import org.apache.struts.action.Action;
    import org.apache.struts.action.ActionForm;
    import org.apache.struts.action.ActionForward;
    import org.apache.struts.action.ActionMapping;

   

    import java.util.List;
    import com.rdta.commons.persistence.QueryRunner;
    import com.rdta.commons.persistence.QueryRunnerFactory;
    
    /**
     *
     *
     *
     */public class SearchePedigreeAction extends Action{
        
    	private static final QueryRunner  queryRunner = QueryRunnerFactory.getInstance().getDefaultQueryRunner();
    	
        public ActionForward execute(ActionMapping mapping, ActionForm form,
    								 HttpServletRequest request, HttpServletResponse response)
    								 throws Exception {
        	System.out.println("WE ARE HERE");
        	List list=null;
        	String refNumber = request.getParameter("pedigreeNum");
        	//=request.getParameter("refnum");
        	/*String xQuery="for $i in collection('tig:///ePharma/APN')/APN"+
                   " return data($i/DocumentId)";
    		 List list=  queryRunner.executeQuery(xQuery);*/ 
    		 if(refNumber == null){
    		     System.out.println("please enter the Reference number");
    		 }
    		 else
    		 {	   
 				
 		        String buff="for $i in collection('tig:///ePharma/DespatchAdvice')/DespatchAdvice"+
 			
 					  " where $i/ID='"+refNumber+"'"+        		
 					  " return data($i/DespatchLine/Item/SellersItemIdentification) ";

 		        try{
 		            list = queryRunner.returnExecuteQueryStrings(buff.toString());
 		        }catch(Exception e){
 		            e.printStackTrace();
 		        }
 				
 				
 				
 				String NDC=(String)list.get(0);
 				System.out.println("Comment===================="+NDC);
 				buff ="for $i in collection('tig:///ePharma/APN')/APN"+
 					  " where $i/Pedigrees/Pedigree/Products/Product/NDC='"+NDC+"'"+
 					  " return data($i/DocumentId)";
 					  
 				
 				List list1 =  queryRunner.returnExecuteQueryStrings(buff.toString());
 				request.setAttribute("APNResultSet",list1);
 				
 				buff="for $b in collection('tig:///ePharma/APN')/APN/Pedigrees/Pedigree"+
 				" return data($b/Custody/TransactionNumber)";
 				List list2 =  queryRunner.returnExecuteQueryStrings(buff.toString());
 				request.setAttribute("OrderResultSet",list2);
 				
 				buff="for $b in collection('tig:///ePharma/APN')/APN"+
                  " return data($b/DateTime)";
 				List list3 =  queryRunner.returnExecuteQueryStrings(buff.toString());
 				request.setAttribute("Date ReceivedResultSet",list3);
 				
 				
 				buff="for $b in collection('tig:///ePharma/APN')/APN/Pedigrees/Pedigree"+
                      " return data($b/Products/Product/LegendDrugName)";
 				List list4 =  queryRunner.returnExecuteQueryStrings(buff.toString());
 				request.setAttribute("ProductResultSet",list4);
 				buff=" for $b in collection('tig:///ePharma/APN')/APN/Pedigrees/Pedigree"+
 				" return data($b/Products/Product/Quantity) ";
 				List list5 =  queryRunner.returnExecuteQueryStrings(buff.toString());
 				request.setAttribute("QuantityResultSet",list5);
 				
 				
 				
    		 }
 		
    
    				
    		return mapping.findForward("success");
    	
           

        
        }

   
}
