<html>
	<head>
		<title><@s.text name="account.createNewAccount.title" /></title>
    </head>
	<body>
		<form name="user" method="post" enctype="multipart/form-data" action="<@s.url action='taxonomy' />" >
			<div id="wrapper">
	    		<div id="content">
	 				<#include "/freemarker/global/form-message.ftl" />
			        <h2 class="block-title">Taxonomy (Product/Feature) Update</h2>
	        		<div class="content">
	        			<div class="taxonomy_form">
	        				<ul>
	             				<li><div id="error_div"  class="errormsg"></div></li>
	  							<li>
			         				<ul id="check" class="sec1"> 
						    			<li class="label-group">Metric</li>	         
								        <li>
								            <label><input type="radio"  name="product" value="false" checked="checked" id="RadioGroup1_0"/>Feature Taxonomy</label>
								        </li>
								        <li>
								            <label><input type="radio" name="product" value="true" id="RadioGroup1_1"/>Product Taxonomy</label>
						    			</li>
									</ul>
								</li>
	        				    <li><label><@s.text name="Upload Taxonomy File(XML)"/>:</label><input type="file"  name="upload" /></li>
	           		      </ul>
	         			  <input type="submit" value="Update"  class="submit_btn"/>
	        			</div>
	        		</div>
			    </div>
	  		</div>
	 	</form> 
	 	<div id="leftbar">
			<div class="actions">
				<h2><@s.text name="label.actions"/></h2>
				<div class="content">
					<ul>
						<li><a <#if tab == 1> class="active"</#if> href="<@s.url action='index' namespace='/admin'><@s.param name='tab' value='1' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.general"/></a></li>
						<li><a <#if tab == 2> class="active"</#if> href="<@s.url action='taxonomy!input' namespace='/admin'><@s.param name='tab' value='2' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.taxonomy.update"/></a></li>
						<li><a <#if tab == 3> class="active"</#if> href="<@s.url action='index!listUser' namespace='/admin'><@s.param name='tab' value='3' /><@s.param name='tabIndex' value='1' /></@s.url>"><@s.text name="label.user.management"/></a></li>
						
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>