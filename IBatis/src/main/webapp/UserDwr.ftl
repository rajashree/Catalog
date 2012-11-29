<html>
<head>
<script type="text/javascript" src="/IBatis_Ex1/dwr/interface/userDwrService.js"></script>
<script type="text/javascript" src="/IBatis_Ex1/dwr/engine.js"></script>
<script type="text/javascript" src="/IBatis_Ex1/dwr/util.js"></script>
</head>
<body>
<script>
function getVersion(){
userDwrService.getApplicationVersion("true",handleAppplicationVersion);
}
function getUser(){
var username = dwr.util.getValue("username");
alert(username);
userDwrService.getUser(username,handleUser);
}
function handleAppplicationVersion(data){
alert(data);
}
function handleUser(data){
alert(data.lastName);
}
</script>
<input type="button" value="Get Version" onclick="getVersion()" />
<input type="text" id="username"/>
<input type="button" value="Get User" onclick="getUser()"/>
</body>
</html>