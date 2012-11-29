<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.sourcen.core.util.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<html>
<head>
    <title>Products</title>
    <style type="text/css" title="currentStyle">
        @import "<c:url value="/scripts/libs/jquery/dataTables/css/demo_table_jui.css"/>";
        @import "<c:url value="/scripts/libs/jquery/dataTables/themes/smoothness/jquery-ui-1.8.4.custom.css"/>";
        #product_images span{
            display: block;
            width: 350px;
        }
    </style>
    <script type="text/javascript" language="javascript"
            src="<c:url value="/scripts/libs/jquery/jquery-1.7.2.min.js"/>"></script>
    <script type="text/javascript" language="javascript"
            src="<c:url value="/scripts/libs/jquery/dataTables/jquery.dataTables-1.9.1.min.js"/>"></script>
</head>
<body>

${navCrumbs}

<div class="products">
    <c:if test="${fn:length(products) > 0}">
        <div><b>Products</b></div>
            <div>
                <table border="1" id="products">
                    <thead>
                    <tr>
                        <td>ID</td>
                        <td>Title</td>
                        <td style="width: 150px;">Images</td>
                        <td style="width: 25px;">List Price</td>
                        <td style="width: 25px;">Price</td>
                        <td style="width: 200px;">Url</td>
			<td style="width: 200px;">Enabled</td>
                    </tr>
                    </thead>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.title}</td>
                            <td style="width: 150px;">
                                <c:set var="imageLen" value="${fn:length(product.images)}" />
                                <c:forEach var="image" items="${product.images}" varStatus="status">
                                    <div id="product_images"
                                         <c:if test="${imageLen > 1 && status.index < imageLen-1}">
                                             style="border-bottom: 1px dotted #001422; padding-bottom: 5px;"
                                         </c:if>>
                                        <span><strong>Name: </strong>${image.imageName}</span>
                                        <span><strong>Type:</strong> ${image.imageType}</span>
                                        <span><strong>URL:</strong><a href="${image.imageURL}" title="${image.imageURL}">Image URL</a></span>
                                        <span><strong>SrcImage:</strong><a href="${image.srcImageURL}" title="${image.srcImageURL}">Source Image URL</a></span>
                                        <span>
                                            <strong>Source Image:</strong><img width="50" height="50" src="${image.srcImageURL}" alt="No Image">
                                            <strong>CDN Image:</strong><img width="50" height="50" src="${image.imageURL}" alt="No Image">
                                        </span>
                                    </div>
                                </c:forEach>
                            </td>
                            <td style="width: 25px;">${product.listPrice}</td>
                            <td style="width: 25px;">${product.price}</td>
                            <td style="width: 25px;"><a href="${product.url}" title="${product.url}">Product URL</a></td>
			    <td style="width: 25px;">
                            	<input type="text" name="${product.id}_status" id="${product.id}_status" disabled="true"
                                	value="<c:if test="${product.enabled!=null && product.enabled==false}">false</c:if><c:if test="${product.enabled==null || product.enabled==true}">true</c:if>">
                            	<input type="button" name="btn1" id="btn1" onclick="changeStatus(${product.id});" value="change">

                             </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
    </c:if>
    <c:if test="${fn:length(products) == 0}">
        No products are available for merchant <c:out value="- ${merchantName}" />
    </c:if>
</div>


<script type="text/javascript">

    $(document).ready(function () {
        if ($('#products').length > 0) {
            $('#products').dataTable({
                "bJQueryUI":true,
                "bPaginate":true,
                "sScrollX":"100%",
                "iDisplayLength":10,
                "bScrollCollapse":true,
                "sPaginationType":"full_numbers",
                "aoPreSearchCols": [1, 2],
                "aaSorting":[
                    [ 1, "asc" ]
                ]
            });
        }
    });

    function changeStatus(productId) {
        var key = "#" + productId + "_status";
        var value = $(key).val();
        var status = true;
        if(value == 'true'){
            status = false;
        }
        if(value == 'false'){
            status = true;
        }
        $.ajax({
            //url:'http://localhost:8080' + basePath + 'api/v1/rest/ProductService/updateProductStatus.json',
            //url:'api/v1/rest/ProductService/updateProductStatus.json',
            url:basePath +'api/v1/rest/ProductService/updateProductStatus.json',
            data:{productId:productId, status:status},
            dataType:"json",
            //type:'GET',
            success:function (data, status) {
                if (data.success) {
                   window.location.reload();
                }
            }
        });
    }
</script>
</body>
</html>