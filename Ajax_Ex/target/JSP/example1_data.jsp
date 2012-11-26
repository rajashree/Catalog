	
      <%
	        response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
	  String st="";
	   st+="<catalog><valid>false</valid><journal>abc</journal><publisher>xyz</publisher><edition>tomcat</edition><title>hah</title><author>bala</author></catalog>";
	  out.println(st);
            
        %>   
          