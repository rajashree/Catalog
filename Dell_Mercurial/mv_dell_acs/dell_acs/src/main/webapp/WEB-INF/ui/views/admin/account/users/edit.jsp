<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
    <title>Edit User</title>

    <%-- using jquery validation --%>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.validate.js"/>"></script>
    <script type="text/javascript">
    result = true;

        $(document).ready(function () {
            $("#editUser").validate({
                rules:{
                    firstName:{
                        required:true,
                        minlength:2
                    },

                    lastName:{
                        required:true,
                        minlength:1
                    },

                    email:{// compound rule
                        required:true,
                        email:true,
                        remote: {
                            url:basePath + "admin/account/users/verifyUsernameOrEmailExists.do",
                            data: {
                                type: $("#previousemail").val(),
                                id: ${user.id}
                            }
                        }
                    },
                    confirmPassword:{
                        required:true,
                        equalTo:"#password",
                        minlength:5
                    }
                },
                messages:{
                    firstName:"First Name is a mandatory field.",
                    lastName:"Last Name is a mandatory field.",
                    email:
                    {
                        required:jQuery.format(" Email field is a mandatory field."),
                        remote:jQuery.format(" This email is already in use.")
                    }

                }
            });
        });
    </script>

    <style type="text/css">
        .statusmessage{
            color: red;
            margin-left: 156px;
            padding-bottom:21px;
        }
    </style>

</head>
<body>

${navCrumbs}
<div class="statusmessage"><c:out value="${message}"/></div>

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>

<form:form modelAttribute="user" id="editUser" name="editUser" action="update.do" method="POST">
    <div class="row">
        <span>User Name<b style="color:red">*</b> :</span>
        ${user.username}
    </div>

    <div class="row">
        <span>First Name<b style="color:red">*</b> :</span>
        <form:input id="firstName" name="firstName" autocomplete="false" path="firstName"/>
    </div>


    <div class="row">
        <span>Last Name<b style="color:red">*</b> :</span>
        <form:input id="lastName" name="lastName" autocomplete="false" path="lastName"/>
    </div>


    <div class="row">
        <span>Email<b style="color:red">*</b> :</span>
        <form:input id="email" autocomplete="false" path="email" name="email"/>
    </div>

    <div class="row">
        <span>Facebook ID<b style="color:red"></b> :</span>
        <form:input id="facebookId" autocomplete="false" path="facebookId"/>
    </div>

    <div class="row">
    <span>Retailer<b style="color:red"></b> :</span>
         <spring:bind path="retailer.id">
             <select name="retailer.id" id="retailer.id">
                 <option value="-1">Choose Retailer</option>
                 <c:forEach var="retailer" items="${retailers}">
                     <option value="${retailer.id}" <c:if test="${retailer.id ==  retailerID}">selected="yes"</c:if> >${retailer.name}</option>
                 </c:forEach>
             </select>
         </spring:bind>
    </div>

    <div class="row">
        <span>Change Password<b style="color:red"></b> :</span>
        <a href="<c:url value='/admin/account/user/changePassword/${user.username}.do'/>">Change Password</a>
    </div>

    <div class="row">
            <fieldset>
                <legend><Strong>User Roles</Strong></legend>
                <div id="user-roles">
                    <c:choose>
                        <c:when test="${ ! empty allRoles}">
                            <table>
                                <c:forEach var="role" items="${allRoles}">
                                    <tr>
                                        <c:choose>
                                            <c:when test="${fn:contains(assignedRoles, role)}">
                                                <td>${role.name}</td>
                                                <td>
                                                    <spring:bind path="roles">
                                                        <input type="checkbox" name="roles" value="${role.name}" checked="yes"/>
                                                    </spring:bind>
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>${role.name}</td>
                                                <td>
                                                    <spring:bind path="roles">
                                                        <input type="checkbox" name="roles" value="${role.name}"/>
                                                    </spring:bind>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:when>
                    </c:choose>
                </div>
            </fieldset>

            <form:hidden path="username" id="username"></form:hidden>
            <form:hidden path="password" id="password"></form:hidden>
            <form:hidden path="enabled"/>
            <input type="hidden" name="previousemail" id="previousemail" value="${user.email}">

            <div style="padding-top: 20px;">
                <input type="submit" value="Save"/>
                <a class="gbutton" href="<c:url value='/admin/account/users/list.do'/>">Cancel</a>
            </div>

</form:form>

</body>
</html>