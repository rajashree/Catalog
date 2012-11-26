<%@include file='../includes/jspinclude.jsp'%>

<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String userName = request.getParameter("uname");
String userPassword = request.getParameter("password");

String xmlResultString = "";
String redirURL = "LoginFailed.html";
String sessQuery = "";
String tp_company_nm = "";
String tp_company_id = "";

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);
              
String xQuery = "<results> {";
xQuery = xQuery + " for $b in collection('tig://root/EAGRFID/SysUsers') ";
xQuery = xQuery + " return $b/User[UserName='"+userName+"' and Password = '"+userPassword+"'] ";
xQuery = xQuery + "} </results> ";

byte[] xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	java.io.InputStream isResult = new ByteArrayInputStream(xmlResults);

	//build dom
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder dbb = dbf.newDocumentBuilder();

	org.w3c.dom.Document doc = dbb.parse(isResult);
	NodeList foundusers = doc.getElementsByTagName("User");

	Node usernode;
	Node userchildnode;
	String nodeName = "";
	String userID = "";
	String firstName = "";
	String lastName = "";
	String accessLevel = "";
	NodeList userchildnodes;
	int numOfResults = foundusers.getLength();

if(numOfResults>0) {
	for(int i=0;i<numOfResults;i++)  {
		usernode = foundusers.item(i); //get user Node
		nodeName = usernode.getNodeName();
		
		if(nodeName.equals("User")) {
		
			userchildnodes=usernode.getChildNodes();
			for(int j=0;j<userchildnodes.getLength();j++)
	      	   	{
				userchildnode =  userchildnodes.item(j);
				if (userchildnode != null) {
				 	     
	      		      		nodeName = userchildnode.getNodeName();
	      		      
					if(nodeName.equals("UserID")) {
						userID  = userchildnode.getFirstChild().getNodeValue();
						
					}
					
					if(nodeName.equals("FirstName")) {
						firstName  = userchildnode.getFirstChild().getNodeValue();
						
					}
					
					if(nodeName.equals("LastName")) {
						lastName  = userchildnode.getFirstChild().getNodeValue();
						
					}
					
					if(nodeName.equals("AccessLevel")) {
						accessLevel  = userchildnode.getFirstChild().getNodeValue();
						
					}
					
				} // END IF USER CHILD NODE NOT NULL
			} //END FOR USER CHILD NODES
		}  //END IF USER NODE
	} //END FOR USER NODES

		if( (!userID.equals("")) & (!firstName.equals("")) & (!lastName.equals(""))) {

			xQuery = "for $b in collection('tig://root/EAGRFID/SysUsers') ";
			xQuery = xQuery + " where $b/User[UserID = '"+userID+"'] ";
			xQuery = xQuery + " return data($b/User/BelongsToCompany) ";

			xmlResults = ReadTL(statement, xQuery);
			if(xmlResults != null) {
				tp_company_nm = new String(xmlResults);
				if(!tp_company_nm.equals("")) {
					xQuery = "for $b in collection('tig://root/CatalogManager/TradingPartner')/TradingPartner ";
					xQuery = xQuery + " where $b/name = '"+tp_company_nm+"' ";
					xQuery = xQuery + " return data($b/businessId) ";

					xmlResults = ReadTL(statement, xQuery);
					if(xmlResults != null) {
						tp_company_id = new String(xmlResults);
					}
				}
			}

			//MAKE SESSION ID
			SimpleDateFormat df = new SimpleDateFormat();
			df.applyPattern("yyyyMMdd");
			String idDate = df.format(new java.util.Date());
			df.applyPattern("hhmmss");
			String idTime = df.format(new java.util.Date());
			String sessionID = replaceString(clientIP, ".", "")+idDate+idTime;
			session.setAttribute("sessionID",sessionID);

			//SESSION TIME FORMAT: 2000-01-20T12:00:00 
			df.applyPattern("yyyy-MM-dd");
			String screenEnteredDate = df.format(new java.util.Date());
			df.applyPattern("hh:mm:ss");
			String screenEnteredTime = df.format(new java.util.Date());
			String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;

			//CREATE SESSION ENTRY AND INSERT
			String sessXML = "<session>";
			sessXML = sessXML +"<sessionid>"+sessionID+"</sessionid>";
			sessXML = sessXML +"<userid>"+userID+"</userid>";
			sessXML = sessXML +"<username>"+firstName+" "+lastName+"</username>";
			sessXML = sessXML +"<accesslevel>epedigree</accesslevel>";
			sessXML = sessXML +"<sessionstart>"+screenEnteredDT+"</sessionstart>";
			sessXML = sessXML +"<userip>"+clientIP+"</userip>";
			sessXML = sessXML +"<lastuse>"+screenEnteredDT+"</lastuse>";
			sessXML = sessXML +"<tp_company_nm>"+tp_company_nm+"</tp_company_nm>";
			sessXML = sessXML +"<tp_company_id>"+tp_company_id+"</tp_company_id>";
			sessXML = sessXML +"</session>";

			sessQuery = "for $b in collection('tig:///EAGRFID/SysSessions') ";
			sessQuery = sessQuery + "where  $b/session/userip = '"+clientIP+"' ";
			sessQuery = sessQuery + "return tig:delete-document(document-uri( $b ))";
			System.out.println("Query for deleting Syssessions doc : "+sessQuery);
			xmlResults = ReadTL(statement, sessQuery);

			sessQuery = "tig:insert-document( 'tig:///EAGRFID/SysSessions/', "+sessXML+")";
			System.out.println("Query for inserting Syssessions doc : "+sessQuery);
			xmlResults = ReadTL(statement, sessQuery);

			session.setAttribute("sessionID",sessionID);
			request.setAttribute("tp_company_nm",tp_company_nm);
			redirURL = "epedigree/epedigree.do?linkName=eped";  
			
			request.getSession(true);

		} else {
			// DIDNT GET USER INFO, REDIRECT TO LOGIN FAILED
			xmlResultString = "Parsed but got bad data.";
			redirURL = "LoginFailed.html";
		}

	} else {
		//redirect URL to LOGIN FAILED
		xmlResultString = "Got not data from TL.";
		redirURL = "LoginFailed.html";
	}
	
} //got data for user 

else {

		//redirect URL to LOGIN FAILED
		xmlResultString = "Got not data from TL.";
		redirURL = "LoginFailed.html";
	}

	CloseConnectionTL(connection);
	

%>

<%
String getCustURL = redirURL;
response.setContentType("text/html; charset=ISO-8859-1");
response.setHeader("Location", getCustURL);
response.setStatus(303);
%>

