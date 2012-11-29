<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<%@include file="/WEB-INF/ui/views/includes/header-scripts.jsp" %>

<html>
<head>
<%@include file="/WEB-INF/ui/views/admin/tags/tag.jsp" %>
    <title>User Account</title>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <style type="text/css">
        body {
            font-family: Verdana, Arial, san-serif;
        }
        .revoke-status-0 {
            background: url("<c:url value="/images/key_disabled.png"/>") no-repeat center center;
            height: 24px;
            width: 39px;
            font-size: 0px;
            display: inline-block;
            /*margin-left:15px;*/
        }
        .revoke-status-1 {
            background: url("<c:url value="/images/enabled.gif"/>") no-repeat center center;
            height: 24px;
            width: 39px;
            font-size: 0px;
            display: inline-block;
            /*margin-left:15px;*/
        }
        #user-account {
            margin:20px 20px 50px 20px;
            padding: 10px;
            border: 5px solid #CCC;
            background: #FFF;"
        }


        #user-details {
              padding-left:20px;
                }
        #user-details .left {
            padding:5px;
            float:left;
            font-weight: bold;
        }
        #user-details .right {
            padding:5px;
            float:left;
        }


        #user-roles {

        }
        #user-roles-inner{
            padding-left:20px;
            padding-bottom:5px;
        }
        #key-headers {
            background: #d6d6e2;
        }

        th{
            height:25px;
        }
        #revoke-error{
            margin-bottom: 20px;
            color: red;
            font-weight: bold;

        }

        #revoke-success {
            margin-bottom: 20px;
            color: green;
            font-weight: bold;
        }
        #page {
            width: 1067px;
            margin: 0 auto;
            background: #efefef;
            border: 1px solid #ddd;
            border-radius: 10px;

            -moz-box-shadow: #fff 0 0 10px;
            -webkit-box-shadow: #fff 0 0 10px;
            box-shadow: #fff 0 0 10px;

            -moz-border-radius: 6px;
            -webkit-border-radius: 6px;
            border-radius: 6px;
        }

        #user-account {
            margin: 20px 20px 50px 20px;
            padding: 10px;
            border: 5px solid #CCC;
            background: #FFF;
            width: 956px;

        }

    </style>
    <script type="text/javascript">

        function modifyStatus(ele,key) {
            //console.log('revoke the key',key);
            var disable = $('#key-status-'+key).hasClass("revoke-status-1");
            var methodName = 'revokeAuthKey.do';
            if(!disable) {
                methodName = 'enableAuthKey.do';
            }


            $.ajax({
                url: basePath +'admin/account/user/'+methodName,
                data: {accessKey : key},
                success: function(data) {
                    if (data != null) {
                        if (data.indexOf('successfully') > -1) {
                            console.log('Successfully enabled.'+'#key-status-'+key);
                            $('#revoke-success').show().fadeOut(7000, "linear");
                            if(disable) {
                                $('#key-status-'+key).removeClass("revoke-status-1");
                                $('#key-status-'+key).addClass("revoke-status-0");
                            } else {
                                $('#key-status-'+key).removeClass("revoke-status-0");
                                $('#key-status-'+key).addClass("revoke-status-1");
                            }
                        } else {
                            console.log('Invalid Access Key found');
                            $('#key-status-'+key).removeClass("revoke-status-0");
                            $('#key-status-'+key).addClass("revoke-status-1");
                            $('#revoke-error').show().fadeOut(7000, "linear");
                        }
                    }

                }
            });
        }

        function createAuthKey(username){
           console.log('Auth Key  for ::'+username);
           $.ajax({
                url: basePath +'admin/account/user/createAuthKey.do',
                data: {username: username},
                success: function(data) {
                    if (data != null) {
                        console.log('',data);
                        if (data.indexOf('successfully') > -1) {
                            console.log('Successfully created.');
                            window.location.reload();
                        } else {
                            console.log('Unable to create AuthKeys.');

                        }
                    }

                }
            });
        }
    </script>

</head>
<body>
	
	${navCrumbs}

    <div>
        <div id="user-account">

                    <c:choose>
                        <c:when test="${ ! empty error}">
                            <div id="error-box" class="error-box" style="background-color:#ffe4e1;">
                                <div>
                                    <span class="error-icon"></span>
                                    ${error}
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>

                            <%-- User Details --%>
                            <fieldset>
                                <legend><strong>User Profile</strong></legend>

                                <div id="user-details">

                                        <div class="left">UserName: </div>
                                        <div class="right">${user.username}</div><br>

                                        <div class="left" style="clear:both;">Email: </div>
                                        <div class="right">${user.email}</div>


                                </div>

                            </fieldset>


                            <%-- User Roles --%>
                            <fieldset>
                                <legend><strong>User Roles</strong></legend>
                                <div id="user-roles">
                                    <c:choose>
                                        <c:when test="${ ! empty roles}">
                                                <c:forEach var="role" items="${roles}">
                                                       <div id="user-roles-inner">
                                                           ${role.name}
                                                       </div>
                                                </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                              No roles associated with the User.
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </fieldset>


                            <%-- User Keys --%>
                            <fieldset>
                                <legend><strong>Authorization Keys</strong></legend>
                                <div id="user-keys">
                                     <table border="1" id="sh_container" style="width:860px; margin:0 0 7px 7px;">
                                         <div id="revoke-success" style="display:none"> Successfully revoked.</div>
                                         <div id="revoke-error" style="display:none"> Could not be revoked.</div>

                                        <c:choose>
                                            <c:when test="${ ! empty keys}">
                                                <div id="authorization">
                                                <thead id="key-headers">
                                                    <th style="width: 25px;">Access Key</th>
                                                    <th style="width: 25px;">Secret Key</th>
                                                    <th style="width: 50px;">Status</th>
                                    <!-- tag functionality -->
                                    <th style="width: 40px;" >Tags</th>

                                    <th style="width: 10px;">Tag it</th>
                                                </thead>
                                                    <c:forEach var="key" items="${keys}">
                                                        <tr>
                                                            <td style="width: 25px;"> ${key.accessKey}</td>
                                                            <td style="width: 25px;"> ${key.secretKey}</td>
                                                            <td style="width: 50px;">
	                                                                <span id="key-status-${key.accessKey}" class="revoke-status-${key.status}" onclick="javascript:modifyStatus(this,'${key.accessKey}');"></span>
                                                            </td>
                                                            <td style="width: 82px; display: block;  border-bottom: 1px; border-left: 0; border-right: 0;">
                                                                <span id="tag-${key.id}" class="tag-${key.id}"><script type="text/javascript">Tagview.getTags("API_KEY",${key.id});</script></span>
                                                            </td>

                                                            <td><a href="#"
                                                                   onClick='Tagview.showTagPopup1("API_KEY",${key.id});'
                                                                   class='tag items_action'
                                                                   title="Choose Tag">Tags</a>
                                                                <input type="hidden" name="Tagtype" id="Tagtype" value="API_KEY"/>

                                                            </td>
                                                        </tr>
                                                    </div>
                                                    </c:forEach>

                                            </c:when>
                                            <c:otherwise>
                                                  <tr>There are no associated keys. <a href="<c:url value='#'/>" onclick="javascript:createAuthKey('${user.username}');"> Create New Auth </a></tr>
                                            </c:otherwise>
                                        </c:choose>
                                     </table>
                                </div>
                            </fieldset>

                            <fieldset>
                                 <legend><strong>Create New Keys</strong></legend>
                                 <div><span> <a href="<c:url value='#'/>" onclick="javascript:createAuthKey('${user.username}');"> Create New Auth </a></span> </div>
                            </fieldset>

                        </c:otherwise>
                    </c:choose>


                </div>
        </div>

</body>
</html>