                                                             <TR bgcolor="#ccffff">			
									<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><A href="PedigreeDetails.do?PedigreeId=<%=pedigreeID%>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>"><FONT color="#000099">ShippedPedigree</FONT></A></STRONG></TD>
									<TD class="type-whrite" align="center"><STRONG><a href="ReceivedPedigreeDetails.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">ReceivedPedigree</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Trail.jsp?pedid=""&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Audit Trail</FONT></a></STRONG>
														</TD>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Status.jsp?pedid=&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Status History</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ProductList.do?pedid=""&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Attachment</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Returns.jsp?pedid=""&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Returns</FONT></a></STRONG>
														</TD>
														
													</TR>