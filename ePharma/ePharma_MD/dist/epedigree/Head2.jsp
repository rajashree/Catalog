                    <% String linkname = request.getParameter("linkname");
                       if(linkname == null) linkname = "" ;
                     %>  
                           <TR bgcolor="#ccffff">			
					<% if(linkname.equals("ShipSum")) { %>				
						 		<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><A href="PedigreeDetails.do?PedigreeId=<%=pedigreeID%>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&linkname=ShipSum"><FONT color="#000099">Shipment Summary</FONT></A></STRONG></TD>
				 	<% } else { %>
				 				<TD class="type-whrite" align="center"><STRONG><A href="PedigreeDetails.do?PedigreeId=<%=pedigreeID%>&pagenm=pedigree&tp_company_nm=<%=tp_company_nm%>&linkname=ShipSum"><FONT color="#000099">Shipment Summary</FONT></A></STRONG></TD>
					
					<% }if(linkname.equals("manufacturer")) { %>				
								<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="ViewReceivingManufacturerDetail.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=manufacturer"><FONT color="#000099">Manufacturer </FONT></a></STRONG></TD>
								<% } else { %>
								<TD class="type-whrite" align="center"><STRONG><a href="ViewReceivingManufacturerDetail.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=manufacturer"><FONT color="#000099">Manufacturer </FONT></a></STRONG></TD>
					<% }if(linkname.equals("Audit")) { %>				
								<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="AuditTrail.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Audit"><FONT color="#000099">Audit Trail</FONT></a></STRONG></TD>
								<% } else { %>
								<TD class="type-whrite" align="center"><STRONG><a href="AuditTrail.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Audit"><FONT color="#000099">Audit Trail</FONT></a></STRONG></TD>
					<% }if(linkname.equals("StatusHis")) { %>				
								<TD class="type-whrite" bgcolor="gold" align="center"><STRONG><a href="pedigreeStatus.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=StatusHis"><FONT color="#000099">Status History</FONT></a></STRONG></TD>
								<% } else { %>
								<TD class="type-whrite" align="center"><STRONG><a href="pedigreeStatus.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=StatusHis"><FONT color="#000099">Status History</FONT></a></STRONG></TD>
						<% }if(linkname.equals("Attachment")) { %>				
								<TD class="type-whrite"  bgcolor="gold" align="center"><STRONG><a href="AttachementDetails.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Attachment"><FONT color="#000099">Attachment</FONT></a></STRONG></TD>
								<% } else { %>
								<TD class="type-whrite"  align="center"><STRONG><a href="AttachementDetails.do?PedigreeId=<%=pedigreeID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&linkname=Attachment"><FONT color="#000099">Attachment</FONT></a></STRONG></TD>
		     			<% } %>
							</TR>