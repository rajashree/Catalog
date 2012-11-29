<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Create User</title>
</head>
<body>
${navCrumbs}

<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%-- using jquery validation --%>
<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.validate.js"/>"></script>
<script type="text/javascript">

    $(document).ready(function () {

        $.validator.addMethod("fieldcannothavespace", function(value, element) {
            var arr = value.split(" ");
            if(arr.length > 1){
                return false;
            }
            else
            {
                return true;
            }

        }, "Username can't contain space in between.");



        $("#createUser").validate({
            rules:{
                username:{
                    required:true,
                    remote:basePath + "admin/account/users/verifyUsernameOrEmailExists.do",
                    fieldcannothavespace: true,
                    minlength:2
                },
                firstName:{
                    required:true,
                    minlength:2
                },
                lastName:{
                    required:true,
                    minlength:1
                },
                password:"required",
                email:{// compound rule
                    required:true,
                    email:true,
                    remote:basePath + "admin/account/users/verifyUsernameOrEmailExists.do"
                },
                confirmPassword:{
                    required:true,
                    equalTo:"#password",
                    minlength:5
                }
            },
            messages:{
                username:{
                    required:jQuery.format("Username is a mandatory field."),
                    remote:jQuery.format("Username is already in use or should not contain special characters - ~ ! @ # $ % ^ , & * ( ) { } : ? ; + =")
                },
                firstName:"First Name is a mandatory field.",
                lastName:"Last Name is a mandatory field.",
                password:"Password is a mandatory field.",
                confirmPassword:"Confirm password is a mandatory field.",
                email:
                {
                    required:jQuery.format("Email field is a mandatory field."),
                    remote:jQuery.format("This email is already in use.")
                }

            }
        });
    });
</script>


<%-- end of jquery validation --%>


<form:form modelAttribute="user" id="createUser" name="createUser" method="POST" onsubmit="return resultStatus()">
    <span id="error_message"></span>

    <div class="row">
        <span>Username<b style="color:red">*</b> :</span>
        <form:input id="username" autocomplete="false" path="username"/>
        <br/><span>(Disallowed characters ~ ! @ # $ % ^ , & * ( ) { } : ? ; + =)</span>
    </div>
    <span id="error_message"></span>

    <div class="row">
        <span>First Name<b style="color:red">*</b> :</span>
        <form:input name="firstName" id="firstName" autocomplete="false" path="firstName"/>
    </div>

    <div class="row">
        <span>Last Name<b style="color:red">*</b> :</span>
        <form:input id="lastName" autocomplete="false" path="lastName"/>
    </div>

    <div class="row">
        <span>Password<b style="color:red">*</b> :</span>
        <form:password id="password" autocomplete="false" path="password"/>
    </div>

    <div class="row">
        <span>Confirm Password<b style="color:red">*</b> :</span>
        <input type="password" id="confirmPassword" name="confirmPassword">
    </div>

    <div class="row">
        <span>Email<b style="color:red">*</b> :</span>
        <form:input id="email" autocomplete="false" path="email"/>
    </div>

    <div class="row">
        <span>Facebook ID<b style="color:red"></b> :</span>
        <form:input id="facebookId" autocomplete="false" path="facebookId"/>
    </div>


    <div class="row">
        <span>Retailer<b style="color:red"></b> :</span>
        <form:select path="retailer.id">
            <form:option value="-1">Choose Retailer</form:option>
            <c:forEach var="retailercontent" items="${retailers}">
                <form:option value="${retailercontent.id}">
                    ${retailercontent.name}
                </form:option>
            </c:forEach>
        </form:select>
        <span id="facebookId_error" class="error_msg"></span>
    </div>

    <fieldset>
        <legend><Strong>User Roles</Strong></legend>
        <div id="user-roles">
            <table border="0">
                <c:forEach var="content" items="${role}">
                    <tr>
                        <td align="left">${content.name}</td>
                        <td><form:checkbox path="roles" value="${content.name}"></form:checkbox></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </fieldset>

    <input type="submit" value="Save"/>
    <a class="gbutton" href="javascript: history.back();">Cancel</a>
    <form:hidden path="enabled"/>
</form:form>
</body>
</html>