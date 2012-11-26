<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.rdta.commons.persistence.PersistanceException"%>
<%@ page import="com.rdta.commons.persistence.QueryRunner"%>
<%@ page import="com.rdta.commons.persistence.QueryRunnerFactory"%>
<%@ page import="com.rdta.eag.commons.xml.*"%>
<%@ page import="org.w3c.dom.Node"%>
<%!
ArrayList executeQuery(String xQuery) {

        QueryRunner queryRunner = QueryRunnerFactory.getInstance()
                .getDefaultQueryRunner();
        ArrayList resultList = null;
        try {
            resultList = (ArrayList) queryRunner
                    .executeQuery(xQuery.toString());
        } catch (PersistanceException e) {
            e.printStackTrace();
        }
        System.out.println("length" + resultList.size());

        return resultList;
    }

    String buildXQuery(String ndc, String lot) {
        System.out.println(ndc + lot);
        StringBuffer sbXQuery = new StringBuffer();
        sbXQuery.append("for $i in collection('tig:///ePharma/PedigreeBank') where ");
        sbXQuery.append(" $i/*:Pedigree/*:Products/*:Product/*:NDC = '");
        sbXQuery.append(ndc + "' ");
        if (lot != null || !lot.equals("")) {
            sbXQuery.append(" and ");
            sbXQuery.append(" $i/*:Pedigree/*:Products/*:Product/*:LotNumber = '");
            sbXQuery.append(lot + "' ");
            bLotNumberSpecified = true;
        }
        sbXQuery.append(" return $i/*:Pedigree ");
        System.out.println(sbXQuery.toString());
        return sbXQuery.toString();
    }
    ArrayList searchResults;
    String ndc, lot;
    boolean bLotNumberSpecified = false;


    %>
<%

        String servIP = request.getServerName();
        String clientIP = request.getRemoteAddr();
        String sessionID = "";//request.getParameter("sessionID");
        String pagenm = request.getParameter("pagenm");
        String tp_company_nm = request.getParameter("tp_company_nm");

        ndc = request.getParameter("ndc");
        lot = request.getParameter("lot");
        String xQuery = buildXQuery(ndc, lot);

        searchResults = executeQuery(xQuery);
        for (int i = 0; i < searchResults.size(); i++) {
              Object obj = searchResults.get(i);      
              System.out.println(obj.getClass());
        }
        %>
<html>
<head>
    <title>Raining Data ePharma - Pedigree Bank</title>
    <link href="../../assets/epedigree1.css" rel="stylesheet"
    type="text/css">
    <script language="JavaScript" type="text/JavaScript">
    <!--
    function MM_goToURL() { //v3.0
        var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
        for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
    }
    
    function MM_openBrWindow(theURL,winName,features) { //v2.0
        window.open(theURL,winName,features);
    }
    //-->
    </script>
</head>
    <body>
<%@include file='../epedigree/topMenu.jsp'%>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width="1%" valign="middle" class="td-rightmenu"><img
            src="../assets/images/space.gif" width="10" height="10"></td>
            <!-- Messaging -->
            <td width="99%" valign="middle" class="td-rightmenu">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td align="left"></td>
                        <td align="right"><!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
                            <img
                    src="../assets/images/space.gif" width="5"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>
                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td><img src="../../assets/images/space.gif" width="30"
                    height="12"></td>
                        <td rowspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td><!-- info goes here -->
                            <table width="100%" id="Table1" cellSpacing="1"
                    cellPadding="1" align="left" border="0"
                    bgcolor="white">
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>
                                        <table width="100%" border="0" cellspacing="0"
                            cellpadding="0">
                                            <tr>
                                                <td><img
                                    src="../../assets/images/space.gif"
                                    width="30" height="12"></td>
                                                <td rowspan="2">&nbsp;</td>
                                            </tr>
                                            <!-- Breadcrumb -->
                                            <!-- <tr> 
                            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> - </span><a href="#" class="typegray1-link">ePedigree</a></td>
                          </tr> -->
                                            <tr>
                                                <td class="td-typeblack">NDC 3333-444-77</td>
                                                <td align="right" class="td-typegray"><!-- Displaying 1 - 25 of 141 <a href="#" class="typegray-link">Prev</a> | <a href="#" class="typegray-link">Next </a>|<a href="#" class="typegray-link"> View All</a> --></td>
                                            </tr>
                                            <tr class="td-typeblack">
                                                <td colspan="2" align="center">
                                                    <input
                                    type="radio" name="choice" checked>
                                                    View As List 
                                                    <input type="radio"
                                    name="choice">
                                                    View As Graph</td>
                                            </tr>

                                            <tr>
                                                <td align="left">
                                                    <table id="Table12" cellSpacing="1"
                                    cellPadding="0" width="100%"
                                    align="center" border="0">
                                                        <tr class="tableRow_Header">
                                                            <td class="type-whrite" noWrap
                                            align="center">NDC</td>
                                                            <td class="type-whrite" noWrap
                                            align="center">Regular Inventory</td>
                                                            <TD class="type-whrite" noWrap
                                            align="center">Returned Inventory</TD>
                                                            <TD class="type-whrite"
                                            align="center">Total</TD>
                                                        </tr>
                                                        <!--
                                                        <TR class="tableRow_On">
                                                            <TD class="td-content"
                                            align="center">1/7/2005</TD>
                                                            <TD class="td-content"
                                            align="center">Amerisource Bergen</TD>
                                                            <TD class="td-content"
                                            align="center">Tylenol Extra Strength</TD>
                                                            <TD class="td-content"
                                            align="center">100</TD>
                                                        </TR>
                                                        <TR class="tableRow_Off">
                                                            <TD class="td-content"
                                            align="center">1/7/2005</TD>
                                                            <TD class="td-content"
                                            align="center">Amerisource Bergen</TD>
                                                            <TD class="td-content"
                                            align="center">Tylenol Extra Strength</TD>
                                                            <TD class="td-content"
                                            align="center">50</TD>
                                                        </TR>
                                                        -->
                                    <%
                                    //should use iterator; but no time
        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println(i);
            Node nd = XMLUtil.parse((InputStream)searchResults.get(i));

            List lProducts = XMLUtil.executeQuery(nd, "Products/Product");
            //get the ndc number and see that's what we're looking for
            Iterator itProducts = lProducts.iterator();
            while (itProducts.hasNext()) {
                Node node = (Node)itProducts.next();
                String ndcValue = XMLUtil.getValue(node, "NDC");
                String lotValue = XMLUtil.getValue(node, "LotNumber");
                String strReturned = XMLUtil.getValue(node, "Returned");
                System.out.println("returned " + strReturned);
                boolean bRender = false;
                if (bLotNumberSpecified) {
                    if (ndcValue.equals(ndc) && lotValue.equals(lot)) {
                        bRender = true;
                    }
                } else {
                    if (ndcValue.equals(ndc)) {
                        bRender = true;
                    }                
                }
                if (bRender) {
                %>
                <tr class="tableRow_Off">
                <td class="td-content" noWrap align="center"><%=ndcValue%></td>
                <td class="td-content" noWrap align="center">Regular Inventory</td>
                <TD class="td-content" noWrap align="center">Returned Inventory</TD>
                <TD class="td-content" align="center">Total</TD>
                </tr>
                
                <%
                
                
                }                
            }
            //System.out.println(XMLUtil.convertToString(nd) + val);            
        }
        %>

                                
                                                    </table>

                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td align="left"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <DIV>
                </DIV>
                <div id="footer" class="td-menu">
                    <table width="100%" height="24" border="0" cellpadding="0"
            cellspacing="0">
                        <tr>
                            <td width="100%" height="24" bgcolor="#d0d0ff"
                    class="td-menu" align="left">&nbsp;&nbsp;Copyright 2005. Raining Data.</td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </table>
    </div>
    </body>
</html>
