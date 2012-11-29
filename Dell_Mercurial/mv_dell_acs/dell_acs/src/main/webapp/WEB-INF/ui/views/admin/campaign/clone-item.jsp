
<%@include file="../../includes/default.jsp" %>
<%@include file="../../includes/header-scripts.jsp" %>
<%@include file="../../includes/form-messages.jsp" %>
<script type="text/javascript">
    function validateField(ele){
        var jqID = '#'.concat(ele.id);
        var val = $(jqID).val();
        if(parseFloat(val) < 0 || val.length == 0){
            $(jqID.concat("_error")).show();
            $(jqID.concat("_error")).html("Please enter a valid value");
            return false;
        }else{
            $(jqID.concat("_error")).hide();
        }
    }

    function validateAndSubmit(){

        // Check for availability
    }

</script>

${navCrumbs}

<fieldset style="padding: 10px;">
    <legend><strong>Product Cloning</strong></legend>
        <%-- http://jqueryui.com/demos/autocomplete/#multiple-remote --%>
    <form id="clone_form" method="POST" action="clone-item.do">
        <table>
            <tbody>
            <tr>
                <td>Title:</td>
                <td><input type="text" autocomplete="false" name="title" id="title" value="<c:out value='${cloneObject["title"]}' />"></td>
            </tr>

            <tr>
                <td>Product ID:</td>
                <td><input name="productID" id="productID" type="text" readonly="true" value="<c:out value='${cloneObject["productID"]}' />"></td>
            </tr>

            <tr>
                <td>Description:</td>
                <td>
                    <textarea name="description" id="description"><c:out value='${cloneObject["description"]}' /></textarea>
                </td>
            </tr>

            <tr>
                <td>Specifications: </td>
                <td><textarea id="specifications" name="specifications"><c:out value='${cloneObject["specifications"]}' /></textarea></td>
            </tr>

            <tr>
                <td>Market Value:</td>
                <td><input type="text"  autocomplete="false" name="price"
                           id="price" value="<c:out value='${cloneObject["price"]}' />" onblur="validateField(this);">
                    <span id="price_error" style="display: none; color: red;"></span>
                </td>
            </tr>

            <tr>
                <td>List Price:</td>
                <td><input type="text"  autocomplete="false" name="listPrice"
                           id="listPrice" value="<c:out value='${cloneObject["listPrice"]}' />" onblur="validateField(this);">
                    <span id="listPrice_error" style="display: none; color: red;"></span>
                </td>
            </tr>

            <tr>
                <td>Availability:</td>
                <td>
                    <select name="availability" id="availability">
                        <option value="">Choose the availability</option>
                        <option value="0" <c:if test='${cloneObject["availability"] == 0}'><c:out value='selected' /></c:if>>OUT_OF_STOCK</option>
                        <option value="1" <c:if test='${cloneObject["availability"] == 1}'><c:out value='selected' /></c:if>>IN_STOCK</option>
                        <option value="2" <c:if test='${cloneObject["availability"] == 2}'><c:out value='selected' /></c:if>>AVAILABLE_FOR_ORDER</option>
                        <option value="3" <c:if test='${cloneObject["availability"] == 3}'><c:out value='selected' /></c:if>>PRE-ORDER</option>
                    </select>
                </td>
            </tr>

            <tr>
                <td>Promotional:</td>
                <td><textarea id="promotional" name="promotional"><c:out value='${cloneObject["promotional"]}' /></textarea>
                </td>
            </tr>

            <tr>
                <td>Shipping Promotion:</td>
                <td><textarea name="shippingPromotion" id="shippingPromotion"><c:out value='${cloneObject["shippingPromotion"]}' /></textarea></td>
            </tr>
            </tbody>
        </table>

        <% String id = request.getParameter("id"); %>
        <% String productID = request.getParameter("productID"); %>

        <input type="hidden" name="id" value="<%=id%>"/>
        <input type="hidden" name="itemID" value='${cloneObject["itemID"]}'/>
        <input type="hidden" name="campaignID" value="${campaign.id}"/>
        <input type="hidden" name="originalProductID" value="<%=productID%>"/>
        <input type="submit" value="Save"/>
        <a class="gbutton" href="<c:url value='/admin/campaign/manage-items.do?campaignID='/>${campaign.id}">Cancel</a>
    </form>
</fieldset>