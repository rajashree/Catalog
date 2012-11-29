<%@ page import="com.sourcen.core.config.ConfigurationServiceImpl" %>
<%@include file="includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Facebook Login</title>
    <link type="text/css" rel="stylesheet" href="/css/normalize.css"/>
    <link type="text/css" rel="stylesheet" href="/css/style.css"/>
    <style type="text/css">
        #fb-auth {
            border: none;
            width: 165px;
            height: 50px;
            background: white url('./images/f-connect.png');
        }
    </style>
</head>
<body>

<div id="fb-root"></div>
<div id="fb-status"></div>
<div align="center"><input type="button" id="fb-auth"/></div>
<%--<div class="fb-login-button" scope="email,user_checkins">
        Login with Facebook
</div>--%>
<%--<div id="fb-status" id="fbloginStatus" style="display:none;">Trying to login with Facebook <img src="./images/loading_animation.gif"/></div>--%>
<form id="fb-post-form" name="fb-post-form" method="post" action="<c:url value="/fb-login-process.do"/>">
    <input type="hidden" id="fbAccessToken" name="fbAccessToken"/>
    <input type="hidden" id="adPublisherWebsite" name="adPublisherWebsite"
           value="<%= request.getParameter("adPublisherWebsite")%>"/>
</form>

<script type="text/javascript">
    /*var statusDiv = document.getElementById("fb-status");*/
    window.fbAsyncInit = function () {

        var button = document.getElementById('fb-auth');

        FB.init({ appId:'<%=ConfigurationServiceImpl.getInstance().getProperty("fbconnect.appId","281783948559678") %>',
            status:true,
            cookie:true,
            xfbml:true,
            oauth:true});


        button.onclick = function() {

            FB.login(function (response) {
                if (response.authResponse) {
                    document.getElementById("fbAccessToken").value = response.authResponse.accessToken;
                    document.forms["fb-post-form"].submit();
                }
            }, {scope:'email,publish_stream,user_about_me,offline_access'});


        }


    };
    /*        button.onclick = function() {
     if (response.authResponse) {
     document.getElementById("fbAccessToken").value = response.authResponse.accessToken;
     document.forms["fb-post-form"].submit();
     } else {
     FB.login(function (response) {
     document.getElementById("fbAccessToken").value = response.authResponse.accessToken;
     document.forms["fb-post-form"].submit();
     }, {scope:'email'});
     }

     }

     };*/

    //        // load Facebook scripts.
    (function () {
        var e = document.createElement('script');
        e.async = true;
        e.src = document.location.protocol
                + '//connect.facebook.net/en_US/all.js';
        document.getElementById('fb-root').appendChild(e);
    }());

    /*   function showLoader(loaderStatus){

     if(loaderStatus){
     document.getElementById('fbloginStatus').style.display="block";
     }else{
     document.getElementById('fbloginStatus').style.display="none";
     }
     }*/
</script>

</body>
</html>