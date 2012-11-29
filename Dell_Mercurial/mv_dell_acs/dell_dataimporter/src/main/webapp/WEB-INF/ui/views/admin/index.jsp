<%@include file="../includes/default.jsp"%>
<%@ page import="java.util.*" %>
<%@ page import="com.dell.acs.web.dataimport.model.admin.DataFileBean" %>
<%@ page import="com.dell.acs.web.dataimport.model.admin.DataFileGroupBean"%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Admin page</title>
<c:url value="/js/jquery-1.7.1.js" var="jqueryCore"></c:url>
<script src="${jqueryCore}"></script>
<c:url value="/admin/index.do?" var="link"/>
<c:url value="/images/opened.png" var="openedImageUrl"/>
<c:url value="/images/closed.png" var="closedImageUrl"/>
<script language="javaScript">
	$(document).ready(function() {
		$("#selectedRetailerSite").change(function() {
			redirect();
		});
		$("#host").change(function() {
			redirect();
		});
		$(".node").click(function() {
			var inner = "#" + $(this).attr("id") + "_inner";
			var image = "#" + $(this).attr("id") + "_image";
			var e = $(inner);
			var ei = $(image);
			var cls = e.attr('class');
			
			if (cls == "visible") {
				$(e).removeClass("visible").addClass("hidden");
				$(ei).attr("src", "${closedImageUrl}");
			} else {
				$(e).removeClass("hidden").addClass("visible");
				$(ei).attr("src", "${openedImageUrl}");
			}
		});
		$("#refresh").click(function() {
			redirect();
		});
		
		function redirect() {
			var level = getOpenNodes();
			var newUrl = "${link}" + "retailerSite=" + $("#selectedRetailerSite").val() + "&host=" + escape($("#host").val()) + "&openNodes=" + level;
			window.location.replace(newUrl); 
		}
	});
	
	function getOpenNodes() {
		var result = "";
		
		$(".visible").each(function(idx){
			var id = $(this).attr("id");
			var pos = id.indexOf('_');
			var epos = id.lastIndexOf('_');
			
			var nodeId = id.substring(pos+1, epos);
			
			if (nodeId != "") {
				result += id.substring(pos+1, epos) + ",";
			}
		});
		
		return result;
	}
</script>
<style>
	.visible { display : block }
	.hidden { display : none }
</style>
</head>
<body>

	<h1>Admin page</h1>
	<a href="#" id="refresh">Refresh</a>
	Retailer: <select id="selectedRetailerSite">
		<option value="-1">...select</option>
		<c:forEach var="retailerSite" items="${retailerSites}">
			<option value="${retailerSite.siteName}"
				<c:if test="${retailerSite.siteName == selectedRetailerSite}">selected</c:if>>${retailerSite.siteName}</option>
		</c:forEach>
	</select>
	Host: <select id="host">
		<option value="" <c:if test="${selectedHost.length() == 0}">selected</c:if>>...select</option>
		<c:forEach var="host" items="${hosts}">
			<option value="${host}"
				<c:if test="${host == selectedHost}">selected</c:if>>${host}</option>
		</c:forEach>
	</select>
	<hr/>			
	<c:set var="currentLevel" value="-1"/>
	<c:set var="inTable" value="false"/>
	<c:set var="currentLevel" value="${node.level}"/>
	<c:set var="inLeaf" value="false"/>
<%
/*
 <table>
		<tr>
			<th>Id</th>
			<th>Level</th>
			<th>Leaf</th>
			<th>Stat</th>
			<th>Summary</th>
			<th>Name</th>
		</tr>
	<c:forEach var="node" items="${retailerSiteFiles}">
		<tr>
			<td>${node.id}<BR/>
			${node.open}<BR/>
			</td>
			<td>${node.level}</td>
			<td>${node.leaf}</td>
			<c:if test="${node.leaf}">
				<td>${node.stat}</td>
				<td>N/A</td>
				<td>${node.data.name}</td>
			</c:if>
			<c:if test="${!node.leaf}">
				<td>N/A</td>
				<td>${node.summary}</td>
				<td>${node.group.name}</td>
			</c:if>
		</tr>
	</c:forEach>
	</table>
*/
%>
	<c:forEach var="node" items="${retailerSiteFiles}">
		<c:if test="${node.level < currentLevel}">
			<c:if test="${inTable}">
				</table>
				<c:set var="inTable" value="false"/>
			</c:if>
			<c:forEach var="i" begin="1" end="${currentLevel-node.level}">
					</li>
				</ul>		
			</c:forEach>
		</c:if>
		<c:if test="${!node.leaf}">
			<c:if test="${isleaf}">
				<c:if test="${node.level == currentLevel}">
					<!-- special case, level did not changed! -->
						</li>
					</ul>
				</c:if>
			</c:if>
			
			<c:set var="inLeaf" value="false"/>
			<c:if test="${inTable}">
				</table>
				<c:set var="inTable" value="false"/>
			</c:if>
				<ul><span id="node_${node.id}" class="node">
					<c:if test='${node.open}'>
						<img id="node_${node.id}_image" src="${openedImageUrl}"/>
						<c:set var="nodeClass" value="visible"/>
					</c:if>
					<c:if test='${!node.open}'>
						<img id="node_${node.id}_image" src="${closedImageUrl}"/>
						<c:set var="nodeClass" value="hidden"/>
					</c:if>
					${node.display}
					&nbsp;
					${node.started}
					&nbsp;
					${node.elapse}
					&nbsp;
						<c:if test="${node.processing}">
							<c:url value="/images/processRunning.gif" var="processRunningUrl"></c:url>
							<img src="${processRunningUrl}"/>
						</c:if>
						<c:if test="${node.waiting}">
							<c:url value="/images/processWaiting.png" var="processWaitingUrl"></c:url>
							<img src="${processWaitingUrl}"/>
						</c:if>
						<c:if test="${node.numErrors > 0}">
							<c:url value="/images/statusError.png" var="processErrorUrl"></c:url>
							<img src="${processErrorUrl}"/>
						</c:if>
						<c:if test="${node.done}">
							<c:url value="/images/statusDone.png" var="processDoneUrl"></c:url>
							<img src="${processDoneUrl}"/>
						</c:if>
						&nbsp;${node.numRecordsProcessed} of ${node.totalNumRecords} 
						<c:if test="${node.numErrors > 0}">
							- ${node.numErrors} error(s)
						</c:if>
					</span>
					<c:if test="${node.summary}">
						<table>
							<tr>
								<th>Start(Import)</th>
								<th>Elapse(Import)</th>
								<th>Start(Valid)</th>
								<th>Elapse(Valid)</th>
								<th>Start(Images)</th>
								<th>Elapse(Images)</th>
								<th>Start(Trans)</th>
								<th>Elapse(Trans)</th>
							</tr>
							<tr>
								<th>${node.group.startTimeImport}</th>
								<th>${node.group.elapseImport}</th>
								<th>${node.group.startTimeValid}</th>
								<th>${node.group.elapseValid}</th>
								<th>${node.group.startTimeImages}</th>
								<th>${node.group.elapseImages}</th>
								<th>${node.group.startTimeTrans}</th>
								<th>${node.group.elapseTrans}</th>
							</tr>
						</table>
						<hr/>
                    </c:if>
					<li id="node_${node.id}_inner" class="${nodeClass}">
						<table>
			<c:set var="inTable" value="true"/>
			<c:if test="${!node.summary}">
				<c:set var="inLeaf" value="false"/>
				<c:set var="currentLevel" value="${node.level}"/>
				<tr>
					<th>File</th>
					<th>Type</th>
					<th>Status</th>
					<th>Creation Date</th>
					<th>Started</th>
					<th>Elapse</th>
					<th>Step</th>
					<th>Host</th>
				</tr>
			</c:if>
			<c:if test="${node.summary}">
				<c:set var="inLeaf" value="false"/>
				<c:set var="currentLevel" value="${node.level}"/>
				<tr>
					<th>Start(Import)</th>
					<th>Elapse(Import)</th>
					<th>Start(Valid)</th>
					<th>Elapse(Valid)</th>
					<th>Start(Images)</th>
					<th>Elapse(Images)</th>
					<th>Start(Trans)</th>
					<th>Elapse(Trans)</th>
				</tr>
			</c:if>
		</c:if>

		<c:if test="${node.leaf}">
			<c:set var="inLeaf" value="true"/>
			<c:set var="currentLevel" value="${node.level}"/>
			<c:if test="${!node.stat}">
				<tr>
					<td>${node.data.name}</td>
					<td>${node.data.type}</td>
					<td>${node.data.status}</td>
					<td>${node.data.creationDate}</td>
					<td>${node.data.started}</td>
					<td>${node.data.elapse}</td>
					<td>${node.data.step}</td>
					<td>${node.data.host}</td>
				</tr>
			</c:if>
			<c:if test="${node.stat}">
				<tr>
					<td>${node.data.startTimeImport}</td>
					<td>${node.data.elapseImport}</td>
					<th>${node.data.startTimeValid}</th>
					<th>${node.data.elapseValid}</th>
					<th>${node.data.startTimeImages}</th>
					<th>${node.data.elapseImages}</th>
					<th>${node.data.startTimeTrans}</th>
					<th>${node.data.elapseTrans}</th>
				</tr>
			</c:if>
		</c:if>
	</c:forEach>
	<c:if test="${inTable}">
				</table>
			</li>
		</ul>
		<c:set var="inTable" value="false"/>
	</c:if>
	
</body>
</html>