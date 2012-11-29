<td style="border: 1px solid rgb(192, 192, 192); width: 100%; vertical-align: top; text-align: center;">
    <div align="center" style="min-height: 400px;">
    
      <form action="/tudu-dwr/secure/admin/administration.action" method="post" name="administrationForm">
      <p>
       <input type="hidden" value="users" name="page"/>
       <input type="hidden" value="cancel" name="method"/>
       <input type="hidden" value="" name="login"/>
       Number of users : <b>2</b>
       <br/><br/>
       Search for users with login starting with :
       <input type="text" value="" size="30" maxlength="200" name="loginStart"/>
       <input type="submit" onclick="document.forms[0].elements['method'].value='searchUser';" value="Submit"/>
      </p>
      
       <p>
        
        </p><table class="list">
         <tbody><tr>
          <th>
           Login
          </th>
          <th>
           First name
          </th>
          <th>
           Last name
          </th>
          <th>
           Enabled
          </th>
          <th>
           Actions
          </th>
         </tr>
        
         
         
         <tr class="odd">
          <td>
           admin
          </td>
          <td>
           Albert
          </td>
          <td>
           Dmin
          </td>
          <td style="text-align: center;">
           true
          </td>
          <td style="text-align: center;">
           
            <input type="submit" onclick="document.forms[0].elements['method'].value='disableUser';document.forms[0].elements['login'].value='admin';" value="Disable"/>
           
           
          </td>
         </tr>
        
         
         
         <tr class="even">
          <td>
           user
          </td>
          <td>
           
          </td>
          <td>
           
          </td>
          <td style="text-align: center;">
           true
          </td>
          <td style="text-align: center;">
           
            <input type="submit" onclick="document.forms[0].elements['method'].value='disableUser';document.forms[0].elements['login'].value='user';" value="Disable"/>
           
           
          </td>
         </tr>
        
        </tbody></table>      
      
     </form> 
  
   </div>
   </td>