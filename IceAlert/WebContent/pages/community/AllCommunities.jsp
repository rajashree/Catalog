<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/community.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>ICE Alert</title>
</head>
<body style="background:#FFFFFF">
<div class="communities" style="overflow:hidden; width:515px;">
  <div class="form_com">
    <div class="button"><input type="button" value="Add" onclick="document.location='./AddCommunity.ice'"/></div>
      <div class="button">
      	<logic:present name="Ownlist">
      		<html:button property="Invite" onclick="document.location='./InviteCommunity.ice'" disabled="false">Invite</html:button>
      	</logic:present>
      	<logic:notPresent name="Ownlist">
      		<html:button property="Invite" style="background:#cccccc; border:#bbbbbb 1px solid; color:#999999;cursor:default;" disabled="disabled">Invite</html:button>
      	</logic:notPresent>
      </div>
      <div class="search_go">
      	<form action="./SearchCommunityDB.ice" method="post" onsubmit="return validateSearch();">
      	<div class="search">Search</div>
          <div class="t_box">
          	<html:text styleClass="text_box" styleId="id_search" property="id_search" value=""/>
          </div>
          <div class="button">
          	<input type="submit" value="Go" />
          </div>
          </form>
      </div>
  </div>
  <!-- Body -->
  <div class="communities_page" style="padding:35px 0px 0px 20px;">
    <logic:present name="Ownlist">
                    		<div class="cp">
                            	<div class="text">
                            		<logic:notPresent name="alert_community">
                            			owned communities
                            		</logic:notPresent>
                            		<logic:present name="alert_community">
                            			Select One of the owned communities
                            		</logic:present>
                            	</div>
                            	<div class="count">
							    	<div class="no">
							    		<logic:present name="comm_count">
							    			<logic:equal name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					1/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					1/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:equal>
							    			<logic:notEqual name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:notEqual>
							    		</logic:present>
							    	</div>
							    </div>
                       	 	</div>
				<logic:iterate id="COwnbean" name="Ownlist" indexId="a">
					<div class="photo">
                                          <div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.Ownlist[a].community_imgurl}" /></div>
                                          <div class="user_name">
                                          	<html:link styleClass="text" page="/ViewCommunity.ice" paramId="communityid" paramName="COwnbean" paramProperty="community_id">
								<bean:write name="COwnbean" property="community_name"/>
							</html:link>
                                          </div>
                                      </div>													
				</logic:iterate>	
				
				<div class="prev_next">
					<html:form action="CommunityView_Owned.ice">
					<logic:present name="previous">
						<logic:notEqual name="previous" value="0">
							<div class="prev">
								<html-el:hidden property="community_pre" value="${requestScope.previous}" />
								<html:submit property="previous"  styleClass="but_link">« Previous</html:submit>
							</div>
						</logic:notEqual>
					</logic:present>

					<logic:present name="alert_community">	
						<html:hidden property="alert" value="true" />
					</logic:present>
					<logic:notPresent name="alert_community">	
						<html:hidden property="alert" value="false" />
					</logic:notPresent>
					
																	
					<logic:present name="next">
						<logic:notEqual name="next" value="0">
							<div class="next">
				    			<html-el:hidden property="community_next" value="${requestScope.next}" />
				    			<html:submit property="next"  styleClass="but_link">Next »</html:submit>
				    		</div>												    			
						</logic:notEqual>
					</logic:present>
					</html:form>	
                                     </div>	                                            											
			</logic:present>
			
			<logic:present name="Sublist">
				<div class="cp">
                                        	<div class="text">subscribed communities</div>
                                        	<div class="count">
							    	<div class="no">
							    		<logic:present name="comm_count">
							    			<logic:equal name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					1/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					1/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:equal>
							    			<logic:notEqual name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:notEqual>
							    		</logic:present>
							    	</div>
							    </div>
                                    	</div>
                                    	<logic:iterate id="CSubbean" name="Sublist" indexId="a">
					<div class="photo">
                                          <div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.Sublist[a].community_imgurl}" /></div>
                                          <div class="user_name">
                                          	<html:link styleClass="text" page="/ViewCommunity.ice" paramId="communityid" paramName="CSubbean" paramProperty="community_id">
								<bean:write name="CSubbean" property="community_name"/>
							</html:link>
                                          </div>
                                      </div>													
				</logic:iterate>											
				
				<div class="prev_next">
					<html:form action="CommunityView_Subscribed.ice" style="overflow:hidden;">													
					<logic:present name="previous">
						<logic:notEqual name="previous" value="0">
							<div class="prev">
								<html-el:hidden property="community_pre" value="${requestScope.previous}" />
								<html:submit property="previous"  styleClass="but_link">« Previous</html:submit>
							</div>
						</logic:notEqual>
					</logic:present>													
					<logic:present name="next">
						<logic:notEqual name="next" value="0">
							<div class="next">
				    			<html-el:hidden property="community_next" value="${requestScope.next}" />												    			
				    			<html:submit property="next"  styleClass="but_link">Next »</html:submit>
				    		</div>
						</logic:notEqual>
					</logic:present>
					</html:form>
                                     </div>													
				
			</logic:present>
			
			<logic:present name="searchkey">
				<div class="cp">
                                        	<div class="text" style="float:left;width: 100%" >search results for "<bean:write name="searchkey"/>"</div>
                                        	<div class="count">
							    	<div class="no">
							    		<logic:present name="comm_count">
							    			<logic:equal name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					1/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					1/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:equal>
							    			<logic:notEqual name="previous" value="0">
							    				<logic:equal name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="comm_count"/> of <bean:write name="comm_count"/>
							    				</logic:equal>
							    				<logic:notEqual name="next" value="0">
							    					<bean:write name="previous"/>/<bean:write name="next"/> of <bean:write name="comm_count"/>
							    				</logic:notEqual>
							    			</logic:notEqual>
							    		</logic:present>
							    	</div>
							    </div>
                                    	</div>
			</logic:present>
			<logic:present name="searchkey">
				<logic:notPresent name="Searchlist">
					<div class="body">No Communities found.</div>
				</logic:notPresent>
			</logic:present>
			<logic:present name="Searchlist">
				
                                    	<logic:iterate id="CSearchbean" name="Searchlist" indexId="a">
					<div class="photo">
                                          <div class="image"><html-el:img width="74px" height="74px"  src="${requestScope.Searchlist[a].community_imgurl}" /></div>
                                          <div class="user_name">
                                          	<html:link styleClass="text" page="/ViewCommunity.ice" paramId="communityid" paramName="CSearchbean" paramProperty="community_id">
								<bean:write name="CSearchbean" property="community_name"/>
							</html:link>
                                          </div>
                                      </div>													
				</logic:iterate>											
				
				<div class="prev_next">
					<html:form action="SearchCommunityDB.ice"  style="overflow:hidden;">
						<html-el:hidden property="id_search" value="${requestScope.searchkey}" />
						<logic:present name="previous">
							<logic:notEqual name="previous" value="0">
								<div class="prev">
									<html-el:hidden property="community_pre" value="${requestScope.previous}" />
									<html:submit property="previous"  styleClass="but_link">« Previous</html:submit>
								</div>
							</logic:notEqual>
						</logic:present>													
						<logic:present name="next">
							<logic:notEqual name="next" value="0">
								<div class="next">
					    			<html-el:hidden property="community_next" value="${requestScope.next}" />
					    			<html:submit property="next"  styleClass="but_link">Next »</html:submit>
					    		</div>
							</logic:notEqual>
						</logic:present>
					</html:form>
                                     </div>													
				
			</logic:present>
  </div>
</div>
</body>
</html>