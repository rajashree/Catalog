<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="../../../includes/header-scripts.jsp" %>
<html>
<head>
    <meta http-equiv="Cache-Control" content="no-cache" forua="true"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>Users</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <%@include file="/WEB-INF/ui/views/admin/devmode/devmode-header.jsp" %>
     <script type="text/javascript">

        $(document).ready(function () {
                $('#sh_container').dataTable({
                    "bJQueryUI":true,
                    "bPaginate":true,
                    "sScrollX":"100%",
                    // Fix for - CS-518
                    "iDisplayLength":10,
                    "bScrollCollapse":true,
                    "sPaginationType":"full_numbers",
                    "aaSorting":[
                        [ 0, "asc" ]
                    ]
                });
        });

        function updateStatus(status, userID) {

            if(status!=null && userID!=null)
            {
                $.ajax({
                    type: "POST",
                    url: basePath+"admin/account/users/updateStatus.json",
                    data: ({status:status,userID:userID}),
                    success: function(response){
                        if(response.success){
                            if(status){
                                $('#'+userID+"-status-yes").show();
                                $('#'+userID+"-status-no").hide();
                            }else{
                                $('#'+userID+"-status-yes").hide();
                                $('#'+userID+"-status-no").show();
                            }
                        }else{
                            window.location.reload();
                        }
                    }
                });
            }
        }
     </script>

    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
        .revoke-status-0 {
            background-image: url("<c:url value="/images/key_disabled.png"/>");
            height: 24px;
            width: 24px;
            font-size: 0px;
            display: inline-block;
            margin-left:15px;
        }
        .revoke-status-1 {
            background-image: url("<c:url value="/images/enabled.gif"/>");
            height: 24px;
            width: 24px;
            font-size: 0px;
            display: inline-block;
            margin-left:15px;
        }

    </style>


</head>
<body>

${navCrumbs}

<div style="margin:20px 20px 50px 20px;padding: 10px;border: 5px solid #CCC;background: #FFF;">
    <div style="font-size: 14px;"><b>Users</b></div>
    <br/>

     <div class="sh_container">
                    <table border="1" id="sh_container">
                        <thead>
                        <th style="width: 25px;">User ID</th>
                        <th style="width: 25px;">Username</th>
                        <th style="width: 25px;">First Name</th>
                        <th style="width: 25px;">Last Name</th>
                        <th style="width: 25px;">Email</th>
                        <th style="width: 25px;">Created On</th>
                        <th style="width: 25px;">Enabled</th>
                        <th style="width: 25px;">Edit</th>
                        </thead>
                         <c:forEach var="user" items="${users}">
                                <c:if test="${ user.id > 1}">
                                    <tr>
                                        <td style="width: 25px;"> ${user.id}</td>
                                        <td style="width: 25px;"><a title="View" href="<c:url value='/admin/account/user/${user.username}.do'/>"> ${user.username}</a></td>
                                        <td style="width: 25px;">${user.firstName}</td>
                                        <td style="width: 25px;">${user.lastName}</td>
                                        <td style="width: 25px;">${user.email}</td>
                                        <td style="width: 25px;">${user.createdDate}</td>
                                        <td style="width: 25px;">
                                            <c:choose>
                                                <c:when test="${user.enabled}">
                                                    <div id="${user.id}-status-yes" class="revoke-status-1" onclick="javascript:updateStatus(false,${user.id})"></div>
                                                    <div id="${user.id}-status-no" class="revoke-status-0" onclick="javascript:updateStatus(true,${user.id})" style="display: none;"></div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div id="${user.id}-status-yes" class="revoke-status-1" onclick="javascript:updateStatus(false,${user.id})" style="display: none"></div>
                                                    <div id="${user.id}-status-no" class="revoke-status-0" onclick="javascript:updateStatus(true,${user.id})"></div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td style="width: 25px;"><a href="<c:url value='/admin/account/user/edit/${user.username}.do'/>"> <img src="<c:url value="/images/edit_clone.png"/>" /></a></td>
                                    </tr>
                                </c:if>
                        </c:forEach>
                    </table>

        </div>

    <fieldset>
        <legend><strong>Create New User</strong></legend>
        <div><span> <a href="<c:url value='/admin/account/users/create.do'/>" class="gbutton"> Create User </a></span> </div>
    </fieldset>


    <br/>
</div>

</body>
</html>