	

<html>
<head>


<script type="text/javascript">

var xmlHttp;

function showHint()
{
	var str="dfgfg";
if (str.length==0)
{ 
document.getElementById("txtHint").innerHTML=""
return
}
xmlHttp=GetXmlHttpObject()
if (xmlHttp==null)
{
alert ("Browser does not support HTTP Request")
return
} 
var url="example1_data.jsp"
url=url+"?q="+str
url=url+"&sid="+Math.random()
xmlHttp.onreadystatechange=stateChanged 
xmlHttp.open("GET",url,true)
xmlHttp.send(null)
} 


function stateChanged() 
{ 
if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete")
{ 

	 processResponse();
} 
} 








function GetXmlHttpObject()
{ 
var objXMLHttp=null
if (window.XMLHttpRequest)
{
objXMLHttp=new XMLHttpRequest()
}
else if (window.ActiveXObject)
{
objXMLHttp=new ActiveXObject("Microsoft.XMLHTTP")
}
return objXMLHttp
} 







function processResponse(){

var xmlMessage=xmlHttp.responseXml;

var nameNode = xmlHttp.responseXML.getElementsByTagName("publisher")[0];
var nameTextNode = nameNode.childNodes[0];
var publisher = nameTextNode.nodeValue;



var nameNode = xmlHttp.responseXML.getElementsByTagName("journal")[0];
var nameTextNode = nameNode.childNodes[0];
var journal = nameTextNode.nodeValue;


var nameNode = xmlHttp.responseXML.getElementsByTagName("edition")[0];
var nameTextNode = nameNode.childNodes[0];
var edition = nameTextNode.nodeValue;


var nameNode = xmlHttp.responseXML.getElementsByTagName("title")[0];
var nameTextNode = nameNode.childNodes[0];
var title = nameTextNode.nodeValue;


var nameNode = xmlHttp.responseXML.getElementsByTagName("author")[0];
var nameTextNode = nameNode.childNodes[0];
var author = nameTextNode.nodeValue;

var journalElement=document.getElementById("journal");
journalElement.value = journal;

 var publisherElement=document.getElementById("publisher");
publisherElement.value = publisher;

var editionElement=document.getElementById("edition");
editionElement.value = edition;

var titleElement=document.getElementById("title");
titleElement.value = title;

var authorElement=document.getElementById("author");
authorElement.value = author;
 }
  

</script>














</head>
<body>
<h1>Form Validation with Ajax</h1>
<form name="validationForm">
<table>
<tr><td>Catalog Id:</td><td><input type="text"
            size="20"  
              id="catalogId"
            name="catalogId"
    autocomplete="off"
         onkeyup="showHint()"></td>
         <td><div id="validationMessage"></div></td>
</tr>


<tr><td>Journal:</td><td><input type="text"
            size="20"  
              id="journal"
            name="journal"></td>
</tr>

<tr><td>Publisher:</td><td><input type="text"
            size="20"  
              id="publisher"
            name="publisher"></td>
</tr>

<tr><td>Edition:</td><td><input type="text"
            size="20"  
              id="edition"
            name="edition"></td>
</tr>
<tr><td>Title:</td><td><input type="text"
            size="20"  
              id="title"
            name="title"></td>
</tr>

<tr><td>Author:</td><td><input type="text"
            size="20"  
              id="author"
            name="author"></td>
</tr>

<tr><td><input type="submit"
           value="Create Catalog"  
              id="submitForm"
            name="submitForm"></td>
</tr>
</table>

</form>

</body>
</html>

	

		

