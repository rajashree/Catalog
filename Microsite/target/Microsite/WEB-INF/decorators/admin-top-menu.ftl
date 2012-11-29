   <div class="mainmenu">
      <ul>
          <li  <#if menuIndex == 0> class="active"  </#if>><a href="<@s.url action='block' namespace="/admin" />" ><@s.text name="label.blocks"/></a><span/></li>       
      	  <li <#if menuIndex == 1> class="active"  </#if> ><a href="<@s.url action='site' namespace="/admin" />" ><@s.text name="label.sites"/></a><span/></li>       
          <li <#if menuIndex == 2> class="active"  </#if> ><a href="<@s.url action='theme' namespace="/admin" />" ><@s.text name="label.themes"/></a><span/></li>       
     	  <li <#if menuIndex == 3> class="active"  </#if> ><a href="<@s.url action='page' namespace="/admin" />" ><@s.text name="label.pages"/></a><span/></li>
     	  <li <#if menuIndex == 4> class="active"  </#if> ><a href="<@s.url action='users'  namespace="/admin/settings" />" ><@s.text name="label.settings"/></a><span/></li>
   	
       </ul>
    </div>
    
   