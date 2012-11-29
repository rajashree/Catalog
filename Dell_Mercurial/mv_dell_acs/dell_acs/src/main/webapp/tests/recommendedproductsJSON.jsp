<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div id="page">
    <div id="wrapper">
        <h1>Product Recommendations</h1>

        <form name="recommendform" method="post" action="<c:url value="/rest/api/json/v1/recommendproductsJSON"/>">
            <table style="width:100%;">
                <tr>
                    <td>Zipcode</td>
                    <td><input type="text" name="Zipcode" id="Zipcode" value="54858"></td>
                </tr>

                <tr>
                    <td>Max Products</td>
                    <td><input type="text" name="Max_Products" id="Max_Products" value="100"></td>
                </tr>
                <tr>
                    <td>Category Depth</td>
                    <td><input type="text" name="Category_Depth" id="Category_Depth" value="1"></td>
                </tr>
                <tr>
                    <td>Max Categories</td>
                    <td><input type="text" name="Max_Categories" id="Max_Categories" value="3,4"></td>
                </tr>
                <tr>
                    <td>Max Related</td>
                    <td><input type="text" name="Max_Related" id="Max_Related" value="10"></td>
                </tr>
                <tr>
                    <td>Advertiser ID</td>
                    <td><input type="text" name="Advertiser_ID" id="Advertiser_ID" value="2"><i>(Used)</i></td>
                </tr>
                <tr>
                    <td>Ad Category ID</td>
                    <td><input type="text" name="adCategoryID" id="adCategoryID" value="1000"></td>
                </tr>
                <tr>
                    <td>Referral Site</td>
                    <td><input type="text" name="Referral_Site" id="Referral_Site" value="http://dell.com"></td>
                </tr>
                <tr>
                    <td>Search Term</td>
                    <td><input type="text" name="SearchTerms" id="SearchTerms" value="product"><i>(Used)</i></td>
                </tr>
                <tr>
                    <td>Resolution</td>
                    <td><input type="text" name="Resolution" id="Resolution" value="100x200"></td>
                </tr>
                <tr>
                    <td>User Cookie Id</td>
                    <td><input type="text" name="User_Cookie_Id" id="User_Cookie_Id" value="100"></td>
                </tr>
                <tr>
                    <td>Product ID's</td>
                    <td><input type="text" name="Product_IDs" id="Product_IDs" value="1"><i>(Comma Seprated
                        Values)</i><i>(Used)</i></td>
                </tr>
                <tr>
                    <td>Product Category</td>
                    <td><input type="text" name="Product_Categories" id="Product_Categories" value="men"><i>(Used)</i>
                    </td>
                </tr>
                <tr>
                    <td>Gender::</td>
                    <td><input type="text" name="Gender" id="Gender" value="men"></td>
                </tr>
                <tr>
                    <td>Birthday</td>
                    <td><input type="text" name="Birthday" id="Birthday" value="15/12/2015"></td>
                </tr>
                <tr>
                    <td>ContentType</td>
                    <td><input type="text" name="ContentType" id="ContentType" value="0"></td>
                </tr>
                <tr>
                    <td>Recommend</td>
                    <td><input type="text" name="Recommend" id="Recommend" value="0"></td>
                </tr>
                <tr>
                    <td>IP Address</td>
                    <td><input type="text" name="IP_Address" id="IP_Address" value="255.255.0.1"></td>
                </tr>
                <tr>
                    <td>Browser</td>
                    <td><input type="text" name="Browser" id="Browser" value="chrome"></td>
                </tr>
                <tr>
                    <td>Operating System</td>
                    <td><input type="text" name="Operating_System" id="Operating_System" value="windows"></td>
                </tr>
            </table>


            <input type="submit" name="submit_btn" id="submit_btn"
                   value="GetRecommendedProducts">
        </form>
    </div>
</div>
</body>
</html>