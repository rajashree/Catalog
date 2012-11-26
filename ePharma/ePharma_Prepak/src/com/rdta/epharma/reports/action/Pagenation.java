package com.rdta.epharma.reports.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class Pagenation extends Action {
	
	
	
	 public ActionForward execute(ActionMapping mapping, ActionForm form,
			 HttpServletRequest request, HttpServletResponse response)
			 throws Exception {
		 	
		 
		
		 			String option= request.getParameter("option");
		 			String fromString = request.getParameter("from");
		 			String toString = request.getParameter("to");
		 			String totLine = request.getParameter("length");
		 		
		 		System.out.println("Total Length ="+totLine+"from String="+fromString+"toString ="+toString);
		 			
		 			
		 			int from=1;
		 			int to=16;
		 			int length=Integer.parseInt(totLine);
		 			 System.out.println("Hello I am in Pagenationg option "+option+" "+fromString+" "+toString+" "+totLine);
			 			
		 			if(option == null) option="";
		 			if(fromString == null)fromString="1";
		 			if(toString == null)toString="16";
		 		if(option.equalsIgnoreCase("0")){
		 				fromString = "1";
		 				toString="16";
		 			
				 				
		 			}else if(option.equalsIgnoreCase("1")){
		 					
		 					from= Integer.parseInt(fromString);
		 					 to = Integer.parseInt(toString);
		 					
		 					 to=to+16;
		 					 from= to-15;
		 					request.setAttribute("Hide","showAll"); 
		 					 if(to >=length ){
		 						 to=length;
		 						 request.setAttribute("Hide","Next");
		 					 }
		 			}else if (option.equalsIgnoreCase("2")){
		 				 from= Integer.parseInt(fromString);
	 					 to = Integer.parseInt(toString);
	 					 from=from-15;
	 					 to=from+15;
	 					 if(from<=1){
	 						 from=1;
	 						 request.setAttribute("Hide","Previous");
	 					 }
		 				
		 			}else if ( option.equalsIgnoreCase("3")){
		 					from=1+length-((length%20) );
		 					
		 					
		 					to=length;
		 				 System.out.println("From ="+from+"To ="+to);
		 					request.setAttribute("Hide","Next");
		 			}
		 				
		 			System.out.println("Befor setiing requests");
		 			fromString = Integer.toString(from);
		 				toString = Integer.toString(to);
		 				request.setAttribute("from",fromString);
		 				request.setAttribute("to",toString);
		 				
		 					
		 
		 				return mapping.findForward("success");
		 
	 }
		 
}
