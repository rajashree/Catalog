<%

String servIP=  request.getParameter("servIP");

String sessionID =  request.getParameter("sessionID");

String tp_company_nm = request.getParameter("tp_company_nm");

String pagenm = request.getParameter("pagenm");

 

if(session == null) {

 

            System.out.println(" Inside session is nulll ");

            session = request.getSession(true);

}

 

 

if(sessionID != null && tp_company_nm != null && pagenm != null) {

 

                        System.out.println(" These are attributes set in Session : " + sessionID + " " + tp_company_nm + " " +  pagenm);

                        session.setAttribute("r_sessionID",sessionID);

                        session.setAttribute("r_tp_company_nm",tp_company_nm);

                        session.setAttribute("r_pagenm",pagenm);

} else {

 

            sessionID = (String)session.getAttribute("r_sessionID");

            tp_company_nm  = (String)session.getAttribute("r_tp_company_nm");

            pagenm =  (String)session.getAttribute("r_pagenm");

}

 

%>

