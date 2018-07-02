<!--
ImageUpload.jsp is used only for testing purpose - works only when Basic Auth is turned off
-->

<html>
<body>
<form id="test" method="POST" action="http://192.168.1.248:8080/gcn/api/v1/rest/updateProfileDetails.json"
           enctype="multipart/form-data">


    <input type="text" name="gcn" value="aaaplay"/>
    <input type="text" name="firstName" value="test"/>
    <input type="text" name="lastName" value="test"/>
    <input type="text" name="userName" value="test"/>
    <input type="text" name="emailId" value="test"/>
    <input type="text" name="facebookId" value="test"/>
    <input type="text" name="linkedinId" value="test"/>
    <input type="text" name="officeEmailId" value="test"/>
    <input type="text" name="officeName" value="test"/>
    <input type="text" name="officePhoneNumber" value="123"/>
    <input type="text" name="phoneNumber" value="123"/>
    <input type="text" name="residentialAddAddressLine1" value="test"/>
    <input type="text" name="residentialAddAddressLine2" value="test"/>
    <input type="text" name="residentialAddCity" value="test"/>
    <input type="text" name="residentialAddState" value="test"/>
    <input type="text" name="residentialAddCountry" value="test"/>
    <input type="text" name="residentialAddPostalCode" value="test"/>
    <input type="text" name="officeName" value="test"/>
    <input type="text" name="officeAddAddressLine1" value="test"/>
    <input type="text" name="officeAddAddressLine2" value="test"/>
    <input type="text" name="officeAddCity" value="test"/>
    <input type="text" name="officeAddState" value="test"/>
    <input type="text" name="officeAddCountry" value="test"/>
    <input type="text" name="officeAddPostalCode" value="test"/>
    <input type="text" name="profileStatus" value="test"/>
    <input type="text" name="profileUpdateFlag" value="true"/>
    <input type="text" name="profileFieldsUpdated" value="emailId,faceboodId"/>
    <input type="text" name="imageId"/>


    <input type="file" name="avatar" />
    <input type="submit" value="upload" />

</form>

</body>
</html>