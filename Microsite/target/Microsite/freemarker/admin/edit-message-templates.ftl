 <div id="content">
<div id="jive-body-maincol">
                <h1 style="float: left;"> Message Templates </h1>
                    <br clear="all"/>
      <div class="sourcen-content-box">      
<p>
  </p><form method="post" name="emailTemplateForm" action="<@s.url action='update.email.templates' />">

    <div class="table">
    <table width="100%" cellspacing="0" cellpadding="5" border="0">
    <tbody>
    <tr>
        <td>
            <table cellspacing="0" cellpadding="2" border="0">                
                    <tr>
                        <td nowrap="nowrap">Name:</td>
                        <td>
                             <input tpe="text" name="name" value="${name!?default('')}" />
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap">Description:</td>
                        <td>
                           ${description?default('')}                            
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" nowrap="nowrap">Instructions:</td>
                        <td> <p>
                            The following tokens may be used as part of this email:<br/><br/>
           
                        </p></td>
                    </tr>
                    <tr>
                        <td id="templates" colspan="2">                              
                            <div id="template0" class="template">
                                <table cellspacing="0" cellpadding="0" border="0">
                                    <tbody><tr>
                                        <td nowrap="nowrap">Locale:</td>
                                        <td>
                                            <select name="localeCode" size="1">
                                            
                                                <option value="sq">Albanian
                                            
                                                </option><option value="sq_AL">Albanian (Albania)
                                            
                                                </option><option value="ar">Arabic
                                            
                                                </option><option value="ar_DZ">Arabic (Algeria)
                                                </option><option value="it_CH">Italian (Switzerland)
                                            
                                                </option><option value="ja">Japanese
                                            
                                                </option><option value="ja_JP">Japanese (Japan)
                                            
                                                </option><option value="ja_JP_JP">Japanese (Japan,JP)
                                            
                                                </option><option value="ko">Korean
                                            
                                                </option><option value="ko_KR">Korean (South Korea)
                                            
                                                </option><option value="lv">Latvian
                                            
                                                </option><option value="lv_LV">Latvian (Latvia)
                                            
                                                </option><option value="lt">Lithuanian
                                            
                                                </option><option value="lt_LT">Lithuanian (Lithuania)
                                                 </option></select>
                                        </td>
                                        <td rowspan="3" style="padding-left: 25px;">
                                        
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td valign="top" nowrap="nowrap">Subject:</td>
                                        <td>
                                            <textarea rows="3" style="width: 100%;" cols="80" name="subject"> ${subject?default('')}</textarea>
                                    
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td valign="top" nowrap="nowrap">Body:</td>
                                        <td>
                                            <textarea rows="20" style="width: 100%;" cols="80" name="body"> ${body?default('')}</textarea>
                                            
                                            <br/>
                                        </td>
                                    </tr>

                                </tbody></table>
                            </div>
                            
                        </td>
                    
                    </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
    <tfoot>
        <tr>
            <td>
              <input type="hidden" value="${tid?default('')}"  name="tid"/>
                <input class="submit_btn" type="button" value="Add Template" onclick="addTemplate()" name="add"/>
                <input class="submit_btn" type="submit" value="Save Changes" name="save"/>
                <input class="submit_btn" type="submit" onclick="form.onsubmit=null" value="Cancel" name="action:email.templates" id="index"/>
	     
            </td>
        </tr>
    </tfoot>
    </table>
    </div>
    </form>
    </div>
                <!-- END content box -->

            </div>
            </div>