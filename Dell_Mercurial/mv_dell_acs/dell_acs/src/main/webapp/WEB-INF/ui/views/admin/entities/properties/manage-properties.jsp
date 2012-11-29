<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="../../../includes/default.jsp" %>
<%@include file="../../../includes/header-scripts.jsp" %>
<%
    String type = WebUtils.getParameter(request, "type", "");
    String title = "";
    if (type.equalsIgnoreCase("campaign"))
        title = "Manage Campaign Properties";
    else if (type.equalsIgnoreCase("campaignItem"))
        title = "Manage Item Properties";
    else if (type.equalsIgnoreCase("category"))
        title = "Manage Category Properties";
    else if (type.equalsIgnoreCase("product"))
        title = "Manage Product Properties";
    request.setAttribute("title", title);
    request.setAttribute("type", type);
%>
<script type="text/javascript">

    function deleteProperty(propName) {
        var canSubmit = confirm("Do you want to delete \"" + propName + "\" property?");
        if (canSubmit) {
            $('#propName').val(propName);
            $('#propValue').val(null);
            $('#action').val('delete');
            $('#propertyForm').submit();
        }
    }
    function enableEditProperty(propName, propValue) {
        $('#propName').val(propName);
        $('#propValue').val(propValue);
        $('#action').val('edit');
        $('#actionBtn').val('Edit');
    }

    function validateExpiryDateForm() {
        var expiryDateForm = $('#item_ext_props_form');
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var valid = true;

        if (startTime == endTime && endTime == '') {
            valid = false;
        }
        if (startTime == undefined || startTime == '' || startTime == null) {
            $('#start_date_error').html("Start time can't be null");
            valid = false;
        }
        if (endTime == undefined || endTime == '' || endTime == null) {
            $('#end_date_error').html("Start time can't be null");
            valid = false;
        }

        if (valid) {
            expiryDateForm.submit();
            return true;
        } else {
            $('#form_error').html('Please enter valid information');
            return false;
        }
    }
</script>

${navCrumbs}

<% if (type.equalsIgnoreCase("campaignItem")) { %>
<fieldset style="margin-top: 30px;">
    <legend><strong>Item expiry date settings</strong></legend>
    <form action="saveItem-properties.do" method="POST" name="item_ext_props_form" id="item_ext_props_form">
        <span id="form_error" class="error_msg"></span>
        <table>
            <tbody>
            <tr>
                <td><form:label path="startTime">Start Time:</form:label></td>
                <td>
                    <%--<spring:bind path="startTime">--%>
                    <%--<input type="text" readonly="true" name="startTime" id="startTime"--%>
                    <%--value="<c:if test="${! empty startTime }">--%>
                    <%--<fmt:formatDate value="${startTime}" pattern="MM/dd/yyyy hh:mm"/> </c:if>"/>--%>
                    <%--</spring:bind>--%>
                    <input type="text" readonly="true" name="startTime" id="startTime" value="${startTime}">
                    <span id="start_date_error" class="error_msg"></span>
                </td>
            </tr>
            <tr>
                <td><form:label path="endTime">End Time:</form:label></td>
                <td>
                    <%--<spring:bind path="endTime">--%>
                    <%--<input type="text" readonly="true" name="endTime" id="endTime"--%>
                    <%--value="<c:if test="${! empty endTime }">--%>
                    <%--<fmt:formatDate value="${endTime}" pattern="MM/dd/yyyy hh:mm"/> </c:if>"/>--%>
                    <%--</spring:bind>--%>
                    <input type="text" readonly="true" name="endTime" id="endTime" value="${endTime}">
                    <span id="end_date_error" class="error_msg"></span>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="left">
                    <input type="hidden" name="id" value="${entity.id}"/>
                    <input type="hidden" name="type" value="${type}"/>
                    <input type="button" value="Save" onclick="validateExpiryDateForm();">
                </td>
            </tr>

            </tbody>
        </table>
    </form>
</fieldset>
<%}%>

<fieldset style="margin-top: 30px;">
    <legend><strong>Entity Properties</strong></legend>
    <form action="manage-properties.do" method="POST" name="propertyForm" id="propertyForm">
        <c:if test="${ ! empty error}">
            <div id="error-box" class="error-box" style="background-color:#ffe4e1;">
                <div>
                    <span class="error-icon"></span>
                        ${error}
                </div>
            </div>
        </c:if>


        <div id="container" style="padding:20px;">
            <table style="border: 1px solid #000000; width: 100%;">
                <thead style="background-color: #b8b8b8; width: 100%;">
                <tr style="height: 25px;">
                    <th width="30%" align="left">Name</th>
                    <th width="50%" align="left">Value</th>
                    <th align="center">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${properties}" var="property">
                    <tr style="border-bottom: 1px #000000 dotted;">
                        <td><span class="item_property" style="width: 250px;">${property.key}</span></td>
                        <td title="${property.value}">
                            <span class="item_property" style="width: 500px;">${property.value}</span>
                        </td>

                        <td style="text-align: center;">
                            <a style="padding-right: 25px;" title="Edit property"
                               onclick="enableEditProperty('${property.key}','${property.value}')"><span
                                    class="edit action_link"></span></a>
                            <a title="Remove property" onclick="deleteProperty('${property.key}')">
                                <span class="remove action_link"></span>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <%
                String propName = WebUtils.getParameter(request, "propName", "");
                String propValue = WebUtils.getParameter(request, "propValue", "");
            %>
            <div style="margin-top: 10px;">
                Name: <input type="text" id="propName" name="propName"/>
                Value: <input type="text" id="propValue" name="propValue"/>
                <input type="hidden" id="action" name="action" value="add">
                <input type="submit" id="actionBtn" name="actionBtn" value="Add"/>
                <input type="hidden" id="entityId" name="id" value="${entity.id}"/>
                <input type="hidden" id="entityType" name="type" value="${type}"/>
            </div>
        </div>

    </form>
</fieldset>


<script type="text/javascript">
    $('#startTime').datetimepicker({
        showOn: "button",
        buttonImage: "<c:url value="/images/cal.png"/>",
        buttonImageOnly: true,
        dateFormat: 'mm/dd/yy',
        timeFormat: 'hh:mm',
        yearRange: '-10:+10', // +/- years relative to the current year
        changeYear: true,
        onClose: function(dateText, inst) {
            var endDateTextBox = $('#endTime');
            if (endDateTextBox.val() != '') {
                var testStartDate = new Date(dateText);
                var testEndDate = new Date(endDateTextBox.val());
                if (testStartDate > testEndDate)
                    endDateTextBox.val(dateText);
            }
            else {
                endDateTextBox.val(dateText);
            }
        },
        onSelect: function (selectedDateTime) {
            var start = $(this).datetimepicker('getDate');
            $('#endTime').datetimepicker('option', 'minDate', new Date(start.getTime()));
        }
    });

    $('#endTime').datetimepicker({
        showOn: "button",
        buttonImage: "<c:url value="/images/cal.png"/>",
        buttonImageOnly: true,
        dateFormat: 'mm/dd/yy',
        timeFormat: 'hh:mm',
        yearRange: '-10:+10', // +/- years relative to the current year
        changeYear: true,
        onClose: function(dateText, inst) {
            var startDateTextBox = $('#startTime');
            if (startDateTextBox.val() != '') {
                var testStartDate = new Date(startDateTextBox.val());
                var testEndDate = new Date(dateText);
                if (testStartDate > testEndDate)
                    startDateTextBox.val(dateText);
            }
            else {
                startDateTextBox.val(dateText);
            }
        },
        onSelect: function (selectedDateTime) {
            var end = $(this).datetimepicker('getDate');
            $('#startTime').datetimepicker('option', 'maxDate', new Date(end.getTime()));
        }
    });
</script>


