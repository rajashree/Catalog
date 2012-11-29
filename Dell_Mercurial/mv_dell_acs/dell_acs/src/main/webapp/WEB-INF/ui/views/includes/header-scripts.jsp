<%@ page import="com.dell.acs.managers.CampaignManager" %>
<%@ page import="com.sourcen.core.config.ConfigurationServiceImpl" %>
<%@ page import="java.util.Enumeration" %>
<link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
<link type="text/css" href="<c:url value="/css/jquery/tree_style.css"/>" rel="stylesheet"/>
<link type="text/css" href="<c:url value="/css/jquery/jquery.alerts.css"/>" rel="stylesheet"/>
<%--<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.min.js"/>"></script>--%>
<%--<script type="text/javascript" src="<c:url value="/js/jquery-ui-1.8.18.custom.min.js"/>"></script>--%>

<script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
<!-- Common for all -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.core.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.widget.js"/>"></script>
<!-- Common for all -->
<!-- dialog.js dependents START -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.mouse.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.draggable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.button.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.resizable.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.slider.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery.ui.dialog.js"/>"></script>
<!-- dialog.js dependents END -->
<!-- Auto complete and date-picker dependent Start -->
<script type="text/javascript" src="<c:url value="/js/jquery.ui.position.js"/>"></script>
<!-- Auto complete and date-picker dependent END -->

<script type="text/javascript" src="<c:url value="/js/jquery.ui.autocomplete.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js/jquery.ui.datepicker.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-timepicker-addon.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/jquery-ui-sliderAccess.js"/>"></script>

<!-- Tree -->
<script type="text/javascript" src="<c:url value="/js/tree/jquery.jstree.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/tree/jquery.hotkeys.js"/>"></script>

<!-- Ajax file upload -->
<script type="text/javascript" src="<c:url value="/js/ajaxUpload/ajaxfileupload.js"/>"></script>

<%--Jquery alert--%>
<script type="text/javascript" src="<c:url value="/js/jquery.alert.js"/>"></script>

<script type="text/javascript">

    $.ajaxSetup({
        cache: false
    });

    <%
        Integer maxDepth = ConfigurationServiceImpl.getInstance().getIntegerProperty(CampaignManager.CAMPAIGN_CATEGORY_MAX_DEPTH, 2);
    %>
    var MAX_CATEGORY_DEPTH = <%=maxDepth%>;

    var SITE_ID = -1;
</script>


<script type="text/javascript" src="<c:url value="/js/CampaignJSHelper.js"/>"></script>


<script type="text/javascript">

    /* to check the field is not null or empty, should be more than 2 characters and password should be more than 6 characters
     */
    function checkField(elementType)
    {
        var val=$("#"+elementType).val();
        var val_length = val.length;
        $("#"+elementType+"_error").empty();

        if(elementType=="email")
        {
            $("#"+elementType+"_error").empty();

            if(val!=null && val!="")
            {
                var atpos = val.indexOf("@");
                var dotpos = val.lastIndexOf(".");
                if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= val.length) {
                    $("#"+elementType+"_error").append(elementType +" is not a valid email");
                    result= false;
                }
                else {

                    $("#"+elementType+"_error").empty();
                    doAjaxPost(val, elementType, 1);
                }
            }
            else
            {
                $("#"+elementType+"_error").append("email cannot be null");
                result= false;
            }
        }
        else if (elementType=="password" && val_length < 5)
        {
                    $("#"+elementType+"_error").append(elementType+" Password should be more than 5 characters");

        }
        else
        {
            if(val_length<2 && val_length!=0 && val_length!=null)
            {
                $("#"+elementType+"_error").append(elementType+" is too short");
                result=false;
            }
            else if(val_length==0 || val_length==null)
            {
                $("#"+elementType+"_error").append(elementType+" cannot be null");
                result=false;
            }
            else
            {
            $("#"+elementType+"_error").empty();
            result=true;
            }
        }
    }

    /*
     *
     ajax script to check username and email already exits
     *
     */

    function doAjaxPost(val, elementName,type) {


        if(val!=null && val!="")
        {
            var elementType;
            if(type==0)
            {
                elementType="username=";
            }
            else
            {
                elementType="email=";
            }

            $.ajax({
                type: "POST",
                url: basePath + "admin/account/users/userCheck.do",
                data: elementType + val,
                success: function(response){

                    // we have the response
                    if(response=="nouserfound")
                    {
                        $("#"+elementName+"_error").empty();
                        result=true;

                    }
                    else
                    {
                        $("#"+elementName+"_error").empty();
                        $("#"+elementName+"_error").append("email already exits");
                        result=false;
                    }

                }

            });
        }
        else
        {
            document.getElementById(elementName + '_error').innerHTML = elementName + ' cannot be null';
            result=false;
        }
    }
</script>