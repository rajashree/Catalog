<#macro greet>
  <font color="red">Welcome</font>
</#macro>


<#macro greetPerson1 person>
  <font color="blue">Hello ${person}!</font>
</#macro>

<#macro greetPerson2 person color="green">
  <font color="${color}">Hello ${person}!</font>
</#macro>

<#macro border>
  <table border=4 cellspacing=0 cellpadding=4><tr><td>
    <#nested>
  </tr></td></table>
</#macro>

<#macro thrice>
  <#nested><br/>
  <#nested><br/>
  <#nested><br/>
</#macro>

<#macro twise>
  <ul><li><#nested 8></li>
      <li><#nested 2></li>
     </ul>
 </#macro>
