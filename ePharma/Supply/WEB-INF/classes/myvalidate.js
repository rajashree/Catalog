function validate2()
{
var flag=1;
for (var i = 0; i <document.myform.elements.length; i++)
 {
   if (document.myform.elements[i].value=="" && (document.myform.elements[i].type=="text" ||
	document.myform.elements[i].type=="password") && flag==1)
   {
    flag=0; 
    alert("Enter value for "+document.myform.elements[i].name+" field ");
    document.myform.elements[i].focus();	
    break;
   }
   
 }
if(flag==1)
{
document.myform.submit();
}
}
function lose(name)
{
}
function checknum(name)
{
var numb=name.value;
var check=isNaN(name.value);
if(check)
{
alert("Enter a number");
name.focus();
}
}








