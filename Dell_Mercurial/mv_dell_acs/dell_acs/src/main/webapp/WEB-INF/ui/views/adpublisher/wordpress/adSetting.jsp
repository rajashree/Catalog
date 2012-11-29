<%@ page import="com.sourcen.core.util.StringUtils" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    Logger logger = LoggerFactory.getLogger("com.dell.acs.views.adpublisher.wordpress.adSetting");
%>
<html>
<head>
    <title>Add Customization</title>

    <link href="<c:url value="/css/dellacs_style.css"/>" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js"></script>
    <script type="text/javascript" src="<c:url value="/js/dellacs_function.js"/>"></script>
    <script type="text/javascript">
        $(document).ready(function init() {
            $('.addeep').draggable({ containment:['#content_top'],revert:true });
            $('.condeep').droppable({ accept:'.addeep',drop:handleDropEvent });
        });
    </script>
</head>
<body>
<div class="wrap">
<div id="adminLogout" align="right"><a href="<c:url value="/logout.do"/>"><b style="color:black">Sign Out</b></a></div>
<div class="dellacs_bordersolid"><h2>Customize your Ads</h2></div>

<div class="dellacs_instruction">Choose the type of page on which
    you want to display ads, then drag and drop the ad formats that you want
    to install on your blog. please click on the ads which you had droped in
    content area to select Ad types.
</div>

<div class="dellacs_mtop15">
    Post Type
    <form id="postTypeSelector" name="postTypeSelector" method="GET"
          action="<c:url value="/adpublisher/wordpress/adSetting.do"/>">
        <select name="dellacs_posttype" id="dellacs_posttype" class="dellacs_post_dropdown"
                onchange="dellacs_setParameter(this.value);">
            <%
                String post = (String) request.getAttribute("dellacs_post_type");
                //logger.debug("Post type: " + post);
                if (!StringUtils.isEmpty(post)) {

            %>
            <c:if test="${dellacs_post_type=='page'}">
                <option value="page" selected>Page</option>
            </c:if>
            <c:if test="${dellacs_post_type!='page'}">
                <option value="page">Page</option>
            </c:if>
            <c:if test="${dellacs_post_type=='post'}">
                <option value="post" selected>Post</option>
            </c:if>
            <c:if test="${dellacs_post_type!='post'}">
                <option value="post">Post</option>
            </c:if>
            <%--
            <c:if test="${dellacs_post_type=='home'}">
                <option value="home" selected>Home</option>
            </c:if>
            <c:if test="${dellacs_post_type!='home'}">
                <option value="home" >Home</option>
            </c:if>

            <c:if test="${dellacs_post_type=='search'}">
                <option value="search" style="display:none" selected>Search</option>
            </c:if>
            <c:if test="${dellacs_post_type!='search'}">
                <option value="search" style="display:none" >Search</option>
            </c:if>
            --%>
            <%
            } else {

            %>
            <option value="page">Page</option>
            <option value="post">Post</option>
            <%--
         <option value="home" style="display:none" >Home</option>
         <option value="search" style="display:none" >Search</option>--%>
            <%
                }
            %>
        </select>
        <input type="hidden" name="adPublisherId" id="adPublisherId_1"
               value="<%=request.getParameter("adPublisherId")%>">
    </form>
</div>

<br/>

<div id="dellacs_ad_main">
<div id="dellacs_ad_one">
<p class="dellacs_para1">Page:&nbsp;
            <span id="dellacs_parag2" class="dellacs_para2">
               <%
                   String postType = (String) request.getAttribute("dellacs_post_type");
                   if (!StringUtils.isEmpty(postType)) {
               %>
               <c:if test="${dellacs_post_type=='page'}">
                   page
               </c:if>
                <c:if test="${dellacs_post_type=='post'}">
                    post
                </c:if>
                <c:if test="${dellacs_post_type=='home'}">
                    home
                </c:if>
                <c:if test="${dellacs_post_type=='search'}">
                    search
                </c:if>
                <%
                } else {
                %>
                page
                <%
                    }
                %>
            </span>
</p>

<div id="dellacs_pagename">
<div class="dellacs_phead">Page Layout:</div>
<b id="top_header">Top of Page:</b>

<div id="content_top" class="condeep" style="display:block;">
    <%--Top Area2  for Customize Ad page--%>
    <%
        String dellacs_adpos_ct1 = "0";
    %>
    <c:if test="${dellacs_ct1!=null}">
        <%
            String dellacs_ct1_class = "";
            String dellacs_ct_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_ct1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_ct1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_ct_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_ct1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_ct_1: " + dellacs_ct_1 + ",dellacs_adpos_ct1: " + dellacs_adpos_ct1);
                } else {
                    dellacs_ct1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_ct_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_ct1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_ct_1: " + dellacs_ct_1 + ",dellacs_adpos_ct1: " + dellacs_adpos_ct1);
                }
                request.setAttribute("dellacs_adpos_ct1", dellacs_adpos_ct1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_ct1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_ct1", "");
                }

                //logger.debug("Name: " + dellacs_ct_1 + ", Type: " + dellacs_adpos_ct1);
            } catch (Exception e) {
                //logger.debug("error at dellacs_ct1=>" + e.getMessage());
            }

        %>
        <div id="ct1" onclick="dellacs_settings('ct1','top');" title="<%=dellacs_ct1_class%>"
             style="float:left;" class="<%=dellacs_ct1_class%>">
            <p><%=dellacs_ct_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_ct1==null}">
        <div id="ct1" onclick="dellacs_settings('ct1','top');" style="float:left;"><p></p></div>
    </c:if>
</div>

<%
    String dellacs_adpos_cm1 = "0";
    String dellacs_adpos_cm2 = "0";
    String dellacs_adpos_cmb1 = "0";
    String dellacs_adpos_cmb2 = "0";
    String posTyp = (String) request.getAttribute("dellacs_post_type");
    if (posTyp == null) {

%>
<b id="middle_header">Content Area:</b>

<div id="content_middle" class="condeep" style="display:block;">
    <%--Middle Area12  for Customize Ad page--%>
    <c:if test="${dellacs_cm1!=null}">
        <%
            String dellacs_cm1_class = "";
            String dellacs_cm_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cm1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cm1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_1: " + dellacs_cm_1 + ",dellacs_adpos_cm1: " + dellacs_adpos_cm1);
                } else {
                    dellacs_cm1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_1: " + dellacs_cm_1 + ",dellacs_adpos_cm1: " + dellacs_adpos_cm1);
                }
                request.setAttribute("dellacs_adpos_cm1", dellacs_adpos_cm1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cm1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cm1", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cm_1 - " + e.getMessage());
            }
        %>
        <div id="cm1" onclick="dellacs_settings('cm1','middle');" title="<%=dellacs_cm1_class%>" style="float:left;"
             class="<%=dellacs_cm1_class%>">
            <p><%=dellacs_cm_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cm1==null}">
        <div id="cm1" onclick="dellacs_settings('cm1','middle');" style="float:left;"><p></p></div>
    </c:if>
    <%--Middle Area2  for Customize Ad page--%>

    <c:if test="${dellacs_cm2!=null}">

        <%
            String dellacs_cm2_class = "";
            String dellacs_cm_2 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cm2");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cm2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm2 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_2: " + dellacs_cm_2 + ",dellacs_adpos_cm2: " + dellacs_adpos_cm2);
                } else {
                    dellacs_cm2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm2 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_2: " + dellacs_cm_2 + ",dellacs_adpos_cm2: " + dellacs_adpos_cm2);
                }
                request.setAttribute("dellacs_adpos_cm2", dellacs_adpos_cm2);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cm2", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cm2", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cm_2 - " + e.getMessage());
            }

        %>
        <div id="cm2" onclick="dellacs_settings('cm2','middle');" title="<%=dellacs_cm2_class%>" style="float:left;"
             class="<%=dellacs_cm2_class%>">
            <p><%=dellacs_cm_2%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cm2==null}">
        <div id="cm2" onclick="dellacs_settings('cm2','middle');" style="float:right;"><p></p></div>
    </c:if>
    Morbi at quam dui, id pharetra enim. Maecenas purus orci, convallis vel faucibus sit amet, sollicitudin nec
    nulla. Etiam id nisl non mauris semper convallis. Phasellus et nibh lorem. Praesent rhoncus porta lorem
    vitae feugiat. Morbi aliquet pulvinar vehicula. Suspendisse potenti. Maecenas vitae augue lorem, non dictum
    nisi. In at nisl at urna viverra congue. Ut rhoncus eleifend elementum. In facilisis sapien sit amet metus
    suscipit auctor. Sed pulvinar tempus vulputate. Fusce.Morbi at quam dui, id pharetra enim. Maecenas purus
    orci, convallis vel faucibus sit amet, sollicitudin nec nulla. Etiam id nisl non mauris semper convallis.
    Phasellus et nibh lorem. Praesent rhoncus porta lorem vitae feugiat. Morbi aliquet pulvinar vehicula.
    Suspendisse potenti. Maecenas vitae augue lorem, non dictum nisi. In at nisl at urna viverra congue. Ut
    rhoncus eleifend elementum. In facilisis sapien sit amet metus suscipit auctor. Sed pulvinar tempus
    vulputate. Fusce.Morbi at quam dui, id pharetra enim.Maecenas vitae augue lorem, non dictum nisi. In at nisl
    at urna viverra congue.
</div>
<%--</c:if>--%>
<%--Middle Bottom Area  for Customize Ad page--%>
<div id="content_middlebottom" class="condeep" style="display:block;">

    <%--Middle Bottom Area 1 for Customize Ad page--%>
    <c:if test="${dellacs_cmb1!=null}">

        <%
            String dellacs_cmb1_class = "";
            String dellacs_cmb_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cmb1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cmb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_1: " + dellacs_cmb_1 + ",dellacs_adpos_cmb1: " + dellacs_adpos_cmb1);
                } else {
                    dellacs_cmb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_1: " + dellacs_cmb_1 + ",dellacs_adpos_cmb1: " + dellacs_adpos_cmb1);
                }
                request.setAttribute("dellacs_adpos_cmb1", dellacs_adpos_cmb1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cmb1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cmb1", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cmb_1 - " + e.getMessage());
            }

        %>
        <div id="cmb1" onclick="dellacs_settings('cmb1','middlebottom');" title="<%=dellacs_cmb1_class%>"
             style="float:left;" class="<%=dellacs_cmb1_class%>">
            <p><%=dellacs_cmb_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cmb1==null}">

        <div id="cmb1" onclick="dellacs_settings('cmb1','middlebottom');" style="float:left;"><p></p></div>
    </c:if>
    <c:if test="${dellacs_cmb2!=null}">
        <%
            String dellacs_cmb2_class = "";
            String dellacs_cmb_2 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cmb2");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cmb2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb2 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_2: " + dellacs_cmb_2 + ",dellacs_adpos_cmb2: " + dellacs_adpos_cmb2);
                } else {
                    dellacs_cmb2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb2 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_2: " + dellacs_cmb_2 + ",dellacs_adpos_cmb2: " + dellacs_adpos_cmb2);
                }
                request.setAttribute("dellacs_adpos_cmb2", dellacs_adpos_cmb2);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cmb2", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cmb2", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cmb_2 - " + e.getMessage());
            }

        %>
        <div id="cmb2" onclick="dellacs_settings('cmb2','middlebottom');" title="<%=dellacs_cmb2_class%>"
             style="float:right;"
             class="<%=dellacs_cmb2_class%>">
            <p><%=dellacs_cmb_2%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cmb2==null}">
        <div id="cmb2" onclick="dellacs_settings('cmb2','middlebottom');" style="float:right;"><p></p></div>
    </c:if>
</div>
<%
} else if (posTyp.equals("page") | posTyp.equals("post")) {
%>
<b id="middle_header">Content Area:</b>

<div id="content_middle" class="condeep" style="display:block;">
    <%--Middle Area12  for Customize Ad page--%>
    <c:if test="${dellacs_cm1!=null}">
        <%
            String dellacs_cm1_class = "";
            String dellacs_cm_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cm1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cm1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_1: " + dellacs_cm_1 + ",dellacs_adpos_cm1: " + dellacs_adpos_cm1);
                } else {
                    dellacs_cm1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_1: " + dellacs_cm_1 + ",dellacs_adpos_cm1: " + dellacs_adpos_cm1);
                }
                request.setAttribute("dellacs_adpos_cm1", dellacs_adpos_cm1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cm1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cm1", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cm_1 - " + e.getMessage());
            }

        %>
        <div id="cm1" onclick="dellacs_settings('cm1','middle');" title="<%=dellacs_cm1_class%>" style="float:left;"
             class="<%=dellacs_cm1_class%>">
            <p><%=dellacs_cm_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cm1==null}">
        <div id="cm1" onclick="dellacs_settings('cm1','middle');" style="float:left;"><p></p></div>
    </c:if>
    <%--Middle Area2  for Customize Ad page--%>

    <c:if test="${dellacs_cm2!=null}">
        <%
            String dellacs_cm2_class = "";
            String dellacs_cm_2 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cm2");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cm2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm2 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_2: " + dellacs_cm_2 + ",dellacs_adpos_cm2: " + dellacs_adpos_cm2);
                } else {
                    dellacs_cm2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cm_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cm2 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cm_2: " + dellacs_cm_2 + ",dellacs_adpos_cm2: " + dellacs_adpos_cm2);
                }
                request.setAttribute("dellacs_adpos_cm2", dellacs_adpos_cm2);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cm2", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cm2", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cm_2 - " + e.getMessage());
            }

        %>
        <div id="cm2" onclick="dellacs_settings('cm2','middle');" title="<%=dellacs_cm2_class%>" style="float:right;"
             class="<%=dellacs_cm2_class%>">
            <p><%=dellacs_cm_2%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cm2==null}">
        <div id="cm2" onclick="dellacs_settings('cm2','middle');" style="float:right;"><p></p></div>
    </c:if>
    Morbi at quam dui, id pharetra enim. Maecenas purus orci, convallis vel faucibus sit amet, sollicitudin nec
    nulla. Etiam id nisl non mauris semper convallis. Phasellus et nibh lorem. Praesent rhoncus porta lorem
    vitae feugiat. Morbi aliquet pulvinar vehicula. Suspendisse potenti. Maecenas vitae augue lorem, non dictum
    nisi. In at nisl at urna viverra congue. Ut rhoncus eleifend elementum. In facilisis sapien sit amet metus
    suscipit auctor. Sed pulvinar tempus vulputate. Fusce.Morbi at quam dui, id pharetra enim. Maecenas purus
    orci, convallis vel faucibus sit amet, sollicitudin nec nulla. Etiam id nisl non mauris semper convallis.
    Phasellus et nibh lorem. Praesent rhoncus porta lorem vitae feugiat. Morbi aliquet pulvinar vehicula.
    Suspendisse potenti. Maecenas vitae augue lorem, non dictum nisi. In at nisl at urna viverra congue. Ut
    rhoncus eleifend elementum. In facilisis sapien sit amet metus suscipit auctor. Sed pulvinar tempus
    vulputate. Fusce.Morbi at quam dui, id pharetra enim.Maecenas vitae augue lorem, non dictum nisi. In at nisl
    at urna viverra congue.
</div>
<%--</c:if>--%>
<%--Middle Bottom Area  for Customize Ad page--%>
<div id="content_middlebottom" class="condeep" style="display:block;">

    <%--Middle Bottom Area 1 for Customize Ad page--%>
    <c:if test="${dellacs_cmb1!=null}">
        <%
            String dellacs_cmb1_class = "";
            String dellacs_cmb_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cmb1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cmb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_1: " + dellacs_cmb_1 + ",dellacs_adpos_cmb1: " + dellacs_adpos_cmb1);
                } else {
                    dellacs_cmb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_1: " + dellacs_cmb_1 + ",dellacs_adpos_cmb1: " + dellacs_adpos_cmb1);
                }
                request.setAttribute("dellacs_adpos_cmb1", dellacs_adpos_cmb1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cmb1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cmb1", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cmb_1 - " + e.getMessage());
            }
        %>
        <div id="cmb1" onclick="dellacs_settings('cmb1','middlebottom');" title="<%=dellacs_cmb1_class%>"
             style="float:left;" class="<%=dellacs_cmb1_class%>">

            <p><%=dellacs_cmb_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cmb1==null}">
        <div id="cmb1" onclick="dellacs_settings('cmb1','middlebottom');" style="float:left;"><p></p></div>
    </c:if>

    <c:if test="${dellacs_cmb2!=null}">
        <%
            String dellacs_cmb2_class = "";
            String dellacs_cmb_2 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cmb2");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cmb2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb2 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_2: " + dellacs_cmb_2 + ",dellacs_adpos_cmb2: " + dellacs_adpos_cmb2);
                } else {
                    dellacs_cmb2_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cmb_2 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cmb2 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cmb_2: " + dellacs_cmb_2 + ",dellacs_adpos_cmb2: " + dellacs_adpos_cmb2);
                }
                request.setAttribute("dellacs_adpos_cmb2", dellacs_adpos_cmb2);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cmb2", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cmb2", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cmb_2 - " + e.getMessage());
            }

        %>
        <div id="cmb2" onclick="dellacs_settings('cmb2','middlebottom');" title="<%=dellacs_cmb2_class%>"
             style="float:right;"
             class="<%=dellacs_cmb2_class%>">
            <p><%=dellacs_cmb_2%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cmb2==null}">
        <div id="cmb2" onclick="dellacs_settings('cmb2','middlebottom');" style="float:right;"><p></p></div>
    </c:if>
</div>
<%--Bottom Area for Customize Ad page--%>

<%
    }
%>
<b id="bottom_header">Bottom of Page:</b>

<div id="content_bottom" class="condeep" style="display:block;">
    <%
        String dellacs_adpos_cb1 = "0";
    %>
    <c:if test="${dellacs_cb1!=null}">

        <%
            String dellacs_cb1_class = "";
            String dellacs_cb_1 = "";
            try {
                String modelKeyValue = (String) request.getAttribute("dellacs_cb1");
                String[] nameType = modelKeyValue.split("#");
                if (nameType.length < 2) {
                    dellacs_cb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cb1 = "0";
                    //logger.debug("adSetting.jsp[123]=> dellacs_cb_1: " + dellacs_cb_1 + ",dellacs_adpos_cb1: " + dellacs_adpos_cb1);
                } else {
                    dellacs_cb1_class = nameType[0];
                    int j = nameType[0].indexOf('_');
                    dellacs_cb_1 = (nameType[0].substring(j + 1, nameType[0].length())).replace('_', 'X');
                    dellacs_adpos_cb1 = nameType[1];
                    //logger.debug("adSetting.jsp[123]=> dellacs_cb_1: " + dellacs_cb_1 + ",dellacs_adpos_cb1: " + dellacs_adpos_cb1);
                }
                request.setAttribute("dellacs_adpos_cb1", dellacs_adpos_cb1);
                if (!StringUtils.isEmpty(nameType[0])) {
                    request.setAttribute("dellacs_banpos_cb1", nameType[0]);
                } else {
                    request.setAttribute("dellacs_banpos_cb1", "");
                }
            } catch (Exception e) {
                //logger.debug("error at dellacs_cb_1 - " + e.getMessage());
            }

        %>
        <div id="cb1" onclick="dellacs_settings('cb1','bottom');" style="float:left;" title="<%=dellacs_cb1_class%>"
             class="<%=dellacs_cb1_class%>">
            <p><%=dellacs_cb_1%>
            </p>
        </div>
    </c:if>
    <c:if test="${dellacs_cb1==null}">
        <div id="cb1" onclick="dellacs_settings('cb1','bottom');" style="float:left;"><p></p></div>
    </c:if>
</div>
</div>
</div>
<div id="dellacs_ad_two">
    <div id="dellacs_adtype">
        <div id="b_950_90" class="addeep b_950_90">
            <p>950 X 90</p>
        </div>
        <br/>

        <div id="b_728_90" class="addeep b_728_90">
            <p>728 X 90</p>
        </div>
        <br/>

        <div id="b_300_250" class="addeep b_300_250">
            <p>300 X 250</p>
        </div>
        <div id="bfs_300_250" class="addeep bfs_300_250">
            <p>300 X 250fs</p>
        </div>
        <div class='cboth'></div>
        <div id="b_160_600" class="addeep b_160_600">
            <p>160 X 600</p>
        </div>
    </div>
</div>
</div>
<div class='cboth'></div>
<div class='dellacs_savebanner_div'>
    <div class="dellacs_add_cat">
        <table width="100%">
            <tr>
                <td colspan="2">Selected Banner&nbsp;&nbsp;<span
                        id="dellacs_selected_adspan"></span> <input type="hidden" value=""
                                                                    name="dellacs_selected_pos"
                                                                    id="dellacs_selected_pos"></td>
            </tr>
            <tr>
                <td colspan="2">Position On Page :&nbsp;&nbsp;<span
                        id="dellacs_selected_posspan"></span></td>
            </tr>
            <tr>
                <td align="left" class="dellacs_cat_td">Category:&nbsp;</td>
                <td width="15%" align="left">
                    <select name="dellacs_ad_category[]" onchange="dellacs_setbanner_adtype(this.value);"
                            style="width: 130px;">
                        <option selected value="0">--Select Category--</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="right"><input type="button" value="Remove"
                                                     class='dellacs_save_btnc' onclick="clear_content('','');"></td>
            </tr>
        </table>
    </div>
    <form id="dellacs_frmaddata" name="dellacs_frmaddata" method="POST"
          action="<c:url value="/adpublisher/wordpress/save-adSetting.do"/>">
        <input type="submit" value="Submit" style="background-color: #CFECEC;">

        <%--**Banner id at various postion in page Starts--%>
        <input type="hidden" name="dellacs_banpos_ct1" id="dellacs_banpos_ct1" value="${dellacs_banpos_ct1}">
        <input type="hidden" name="dellacs_banpos_cm1" id="dellacs_banpos_cm1" value="${dellacs_banpos_cm1}">
        <input type="hidden" name="dellacs_banpos_cm2" id="dellacs_banpos_cm2" value="${dellacs_banpos_cm2}">
        <input type="hidden" name="dellacs_banpos_cmb1" id="dellacs_banpos_cmb1" value="${dellacs_banpos_cmb1}">
        <input type="hidden" name="dellacs_banpos_cmb2" id="dellacs_banpos_cmb2" value="${dellacs_banpos_cmb2}">
        <input type="hidden" name="dellacs_banpos_cb1" id="dellacs_banpos_cb1" value="${dellacs_banpos_cb1}">
        <%--**Banner id at various postion in page Ends--%>
        <%--**Ad Type at various postion in page Starts--%>
        <input type="hidden" name="dellacs_adpos_ct1" id="dellacs_adpos_ct1" value="<%=dellacs_adpos_ct1%>"/>
        <input type="hidden" name="dellacs_adpos_cm1" id="dellacs_adpos_cm1" value="<%=dellacs_adpos_cm1%>">
        <input type="hidden" name="dellacs_adpos_cm2" id="dellacs_adpos_cm2" value="<%=dellacs_adpos_cm2%>">
        <input type="hidden" name="dellacs_adpos_cmb1" id="dellacs_adpos_cmb1" value="<%=dellacs_adpos_cmb1%>">
        <input type="hidden" name="dellacs_adpos_cmb2" id="dellacs_adpos_cmb2" value="<%=dellacs_adpos_cmb2%>">
        <input type="hidden" name="dellacs_adpos_cb1" id="dellacs_adpos_cb1" value="<%=dellacs_adpos_cb1%>">

        <%--**Ad Type at various postion in page Ends--%>

        <input type="hidden" name="dellacs_post_type" id="dellacs_post_type" value="${dellacs_post_type}">
        <%--<input type="hidden" name="dellacs_flag_save" id="dellacs_flag_save" value="">--%>
        <input type="hidden" name="adPublisherId" id="adPublisherId_2"
               value="<%=request.getParameter("adPublisherId")%>">

    </form>
</div>
</div>

</body>
</html>