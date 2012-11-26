<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.BufferedInputStream" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Properties" %>
<%@ page import="org.xml.sax.InputSource" %>
<%@ page import="com.rdta.tlapi.xql.*" %>
<%@ page import="com.rdta.util.io.StreamHelper" %>
<%@ page import="com.rdta.tlapi.xql.Connection" %>
<%@ page import="com.rdta.tlapi.xql.Statement" %>
<%@ page import="com.rdta.tlapi.xql.DataSourceProperties" %>
<%@ page import="com.rdta.tlapi.xql.DataSourceFactory" %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory,javax.xml.parsers.DocumentBuilder,org.w3c.dom.*" %>

<%!

public com.rdta.tlapi.xql.Connection ConnectTL() {
	com.rdta.tlapi.xql.Connection connection = null;
	
	String hostname = "localhost";
	
	try {
		// Create database connection
		DataSourceProperties properties = DataSourceFactory.getDataSourceProperties();
		properties.getProperty(DataSourceProperties.REMOTE_HOST).setValue( hostname );
		properties.getProperty(DataSourceProperties.REMOTE_PORT).setValue( "3408" );
		properties.getProperty(DataSourceProperties.LISTEN_PORT).setValue( "3444" );
	
		DataSource dataSource = DataSourceFactory.getDataSource(properties);
		connection = dataSource.getConnection("admin", "admin");
		return connection;
	}
	catch(com.rdta.tlapi.xql.XQLException ex) { 
		System.out.print(ex); 
		return connection;
	}

}
%>


<%! 
public String getSessionByClientIP(com.rdta.tlapi.xql.Statement statement, String clientIP) {
	
	byte[] xmlResult = null;
	String usrSessionID = "";
	String query = "";
	query = "for $b in collection('tig:///EAGRFID/SysSessions') ";
	query = query + "where  $b/session/userip = '"+clientIP+"' ";
	query = query + "return data($b/session/sessionid)";
	xmlResult = ReadTL(statement, query);
	
	if(xmlResult != null) {
		usrSessionID = new String(xmlResult);
	}
	return usrSessionID;
}

%>

<%!
public com.rdta.tlapi.xql.Statement getStatement(com.rdta.tlapi.xql.Connection connection) {

	com.rdta.tlapi.xql.Statement statement = null;
	try {
		statement = connection.createStatement();
		return statement;
	} catch(com.rdta.tlapi.xql.XQLException ex) { 
			System.out.print(ex); 
			return statement;
	}

}

%>

<%!
public void CloseConnectionTL(com.rdta.tlapi.xql.Connection connection) {
	try { connection.logoff();
               connection.close(); } catch(XQLException e)   {}
}

%>

<%!
public String ValidateUserSession(com.rdta.tlapi.xql.Statement statement, String sessionID, String clientIP) {

String sessStatus = "";
byte[] xmlResults = null;
String strResult = "";

String xQuery = "for $s in collection('tig://root/EAGRFID/SysSessions/') ";
xQuery = xQuery + "where $s/session/sessionid = '"+sessionID+"' and $s/session/userip = '"+clientIP+"' ";
xQuery = xQuery + "return fn:get-minutes-from-dayTimeDuration ";
xQuery = xQuery + "(xs:dateTime(fn:current-dateTime())- xs:dateTime($s/session/lastuse)) ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	strResult = new String(xmlResults);

	if(Integer.parseInt(strResult)<15) { //IF LAST USED LESS THAN 15 MINUTES SESSION IS OK

		//MAKE SESSION ID
		SimpleDateFormat df = new SimpleDateFormat();

		//SESSION TIME FORMAT: 2000-01-20T12:00:00 
		df.applyPattern("yyyy-MM-dd");
		String screenEnteredDate = df.format(new java.util.Date());
		df.applyPattern("hh:mm:ss");
		String screenEnteredTime = df.format(new java.util.Date());
		String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;

		//UPDATE SESSION ENTRY - REFRESH LAST USE ELEMENT
		xQuery = "for $d in collection('tig://root/EAGRFID/SysSessions/') ";
		xQuery = xQuery+"where $d/session[sessionid/text() = '"+sessionID+"'] ";
		xQuery = xQuery+"return ";
		xQuery = xQuery+"let $newDoc := ( ";
		xQuery = xQuery+"<session> ";
		xQuery = xQuery+"  {$d/session/*:*[ . << $d//lastuse]} ";
		xQuery = xQuery+"  <lastuse>"+screenEnteredDT+"</lastuse> ";
		xQuery = xQuery+"  {$d/session/*:*[ . >> $d//lastuse]} ";
		xQuery = xQuery+"</session> ) ";
		xQuery = xQuery+"return tig:replace-document(document-uri( $d ), $newDoc) ";

		xmlResults = ReadTL(statement, xQuery);
		sessStatus = "VALID";
	} else { //SESSION EXPIRED - RELOGIN
		sessStatus = "EXPIRED";
	}
} else { sessStatus = "EXPIRED"; }

return sessStatus;
}
%>

<%!

public byte[] ReadTL(com.rdta.tlapi.xql.Statement statement, String xQuery) {

	byte[] data = null;

	try {
		com.rdta.tlapi.xql.ResultSet resultSet;

		   resultSet = statement.execute(xQuery);
		   if ( resultSet.next() )
		   {
		       InputStream resultStream = resultSet.getBinaryStream();
		       
		       data = StreamHelper.copy(resultStream);  // get the data

		       resultStream.close();
		       resultSet.close();

		   }
		   else
		   {
		       System.out.println( "Query returned no result." );
		       System.out.println("Query: "+xQuery);
		   }

	}
        catch(com.rdta.tlapi.xql.XQLException ex) { 
        	System.out.print(ex); 
        	System.out.print(xQuery);
        	//data = ex.toString().getBytes(); 
        }
        
        catch(java.io.IOException ioex) {  
        	System.out.print(ioex); 
        	System.out.print(xQuery);
        	//data = ioex.toString().getBytes();	
        }
 	
 	
 	return data;       
}

%>

<%!

public void executeStatementStream(com.rdta.tlapi.xql.Statement statement, String query,  ByteArrayInputStream stream) throws Exception
        {
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            InputStream resultStream = null;

            resultSet = statement.execute(query, stream);

            if ( resultSet.next() )
            {
                // get the data
                //data = StreamHelper.copy(resultStream);
                //resultStream.close();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
                System.out.println("Query: "+query);
            }
            //java.io.InputStream isResult = new ByteArrayInputStream(data);
            
        }
 %>

<%!

public String ParseUsersForViewUserScreen(byte[] xmlResults, String sessionID) {

	String nodeName = "";
	String userID = "";
	String firstName = "";
	String lastName = "";
	String accessLevel = "";
	String Name = "";
	String ACode = "";
	String Department = "";
	String Facility = "";
	String HTMLROW = "";
	String xmlResultString = "";
		
	try {
		java.io.InputStream isResult = new ByteArrayInputStream(xmlResults);

		//build dom
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbb = dbf.newDocumentBuilder();

		org.w3c.dom.Document doc = dbb.parse(isResult);
		NodeList foundusers = doc.getElementsByTagName("user");

		Node usernode;
		Node userchildnode;

		NodeList userchildnodes;
		int numOfResults = foundusers.getLength();

		if(numOfResults>0) {
			for(int i=0;i<numOfResults;i++)  {
				usernode = foundusers.item(i); //get user Node
				nodeName = usernode.getNodeName();

				if(nodeName.equals("user")) {

					userchildnodes=usernode.getChildNodes();
					for(int j=0;j<userchildnodes.getLength();j++)
					{
						userchildnode =  userchildnodes.item(j);
						if (userchildnode != null) {

							nodeName = userchildnode.getNodeName();

							if(nodeName.equals("id")) {
								userID  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("firstname")) {
								firstName  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("lastname")) {
								lastName  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("accesslevel")) {
								accessLevel  = userchildnode.getFirstChild().getNodeValue();
							}

						} // END IF USER CHILD NODE NOT NULL
					} //END FOR USER CHILD NODES
				}  //END IF USER NODE

				//BUILD HTML ROW
				HTMLROW = HTMLROW +"<TR bgcolor=D3E5ED>";
				HTMLROW = HTMLROW + "<TD><A href='NewUser.jsp?userid="+userID+"&sessionID="+sessionID+"'>"+firstName+" "+lastName+"</A></TD>";
				HTMLROW = HTMLROW + "<TD>"+accessLevel+"</TD>";
				HTMLROW = HTMLROW + "</TR>";

			} //END FOR USER NODES

			if( (!userID.equals("")) & (!firstName.equals("")) & (!lastName.equals("")) & (!accessLevel.equals("")) ) {
				// GOT VALID USER INFO, PROCEED TO MAIN PAGE
				xmlResultString = "userID = "+userID+", firstName = "+firstName+", lastName = "+lastName+", accessLevel = "+accessLevel;
			} else {
				// DIDNT GET USER INFO, REDIRECT TO LOGIN FAILED
				xmlResultString = "Parsed but got bad data.";
			}

		} else {
			//redirect URL to LOGIN FAILED
			xmlResultString = "Got not data from TL.";
		}
		
		return HTMLROW;
		
	} catch (javax.xml.parsers.ParserConfigurationException pce) { System.out.print(pce); return HTMLROW; }
		catch (org.xml.sax.SAXException pce) { System.out.print(pce); return HTMLROW; }
		catch (java.io.IOException ioe) { System.out.print(ioe); return HTMLROW; }
	
}
%>


<%!
    public boolean writePicToTL(InputStream fileIS, String locID)
    {
        try {

        String insXML = "<root>";
        insXML = insXML +"<document>{binary {$1}}</document>";
        insXML = insXML +"<locationID>"+ locID + "</locationID>";
        insXML = insXML +"</root>";

        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/LocationImage/', "+insXML+" )";
        //executeStatementStreamPic(FullQuery, fileIS);

         return true;

        }    catch(Exception e) {
                    System.err.println(e);
                    return false;
        }
    }
%>


<%!
        public void executeStatementStreamPic(String query,  InputStream stream) throws Exception
        {
        
            Connection connection = ConnectTL();
        	Statement statement = getStatement(connection);
        	
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            InputStream resultStream = null;
            QueryOption qo = QueryOption.XMLSPACE;
            statement.setQueryOption(qo, "preserve");
            resultSet = statement.execute(query, stream);

            if ( resultSet.next() )
            {
                // get the data
                data = StreamHelper.copy(resultStream);
                resultStream.close();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
            }
        }
%>

<%!
      public String replaceString(String s, String one, String another) {
      // In a string replace one substring with another
      if (s.equals("")) return "";
      String res = "";
      int i = s.indexOf(one,0);
      int lastpos = 0;
      while (i != -1) {
        res += s.substring(lastpos,i) + another;
        lastpos = i + one.length();
        i = s.indexOf(one,lastpos);
      }
      res += s.substring(lastpos);  // the rest
      return res;
      }
%>


<%!
      public String getUniqueID(String clientIP) {
     		SimpleDateFormat df = new SimpleDateFormat();
     		df.applyPattern("yyyyMMdd");
     		String idDate = df.format(new java.util.Date());
     		df.applyPattern("hhmmss");
     		String idTime = df.format(new java.util.Date());
		String newID = replaceString(clientIP,".","")+idDate+idTime;
      return newID;
      }
%>