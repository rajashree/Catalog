<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@include file="/WEB-INF/ui/views/includes/default.jsp" %>


    <title>Change Password</title>
    <style type="text/css">
        .error_message
        {
            color: red;
            margin-left: 154px;
            padding-bottom: 18px;
        }
    </style>

    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.validate.js"/>"></script>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#changePassword").validate({

                rules:{
                    oldpassword:{
                        required:true,
                        minlength:5
                    },

                    password:{
                        required:true,
                        minlength:5
                    },

                    confirmPassword:{
                        required:true,
                        equalTo:"#password",
                        minlength:5

                    }
                },
                messages:{
                    oldpassword:"Previous password is required",
                    password:"New password is required",
                    confirmPassword:"Confirm password required and should match"
                }
            });
        });
    </script>



</head>
<body>
${navCrumbs}

<fieldset>
    <legend><strong>Change Password</strong></legend>
    <div id="error_message" class="error_message">${message}</div>

    <form action="changepassword.do" name="changePassword" id="changePassword" method="POST">
        <div class="row">
            <span>Existing Password<b style="color:red"> *</b> :</span>
            <input type="password" name="oldpassword" id="oldpassword"/>
            <span id="oldpassword_error" class="error_msg"></span>
        </div>
        <div class="row">
            <span>New Password<b style="color:red"> *</b> :</span>
            <input type="password" name="password" id="password"/>
            <span id="password_error" class="error_msg"></span>
        </div>
        <div class="row">
            <span>Confirm Password<b style="color:red"> *</b> :</span>
            <input type="password" name="confirmPassword" id="confirmPassword"/>
            <span id="confirmPassword_error" class="error_msg"></span>
        </div>
        <input type="submit" name="submit" value="Save"/>
        <a class="gbutton" href="<c:url value='/admin/account/user/edit/${username}.do'/>">Cancel</a>
        <input type="hidden" name="username" value="${username}"/>
    </form>
</fieldset>


</body>
</html>