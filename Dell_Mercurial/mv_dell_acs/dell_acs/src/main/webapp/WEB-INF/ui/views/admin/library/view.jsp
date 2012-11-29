<%@ page import="com.sourcen.core.util.WebUtils" %>
<%@include file="/WEB-INF/ui/views/includes/default.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Library Page</title>
    <link type="text/css" href="<c:url value="/css/jquery/jquery-ui-1.8.18.custom.css"/>" rel="stylesheet"/>
    <style type="text/css">
        .ui-widget-header {
            border: none !important;
            background: none !important;
        }

        .ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited {
            color: rgb(28, 148, 196);
            text-decoration: none;
        }

        .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default {
            background: rgb(209, 206, 206);
            font-weight: bold;
            color: rgb(28, 148, 196);

        }

        .ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus {
            background: rgb(209, 206, 206);
            font-weight: bold;
            color: rgb(28, 148, 196);
        }

        /* overrides for ui-tab styles */
        #widget-docs ul.ui-tabs-nav {
            padding: 0 0 0 8px;
        }

        #widget-docs .ui-tabs-nav li {
            margin: 5px 5px 0 0;
        }

        #widget-docs .ui-tabs-nav li a:link,
        #widget-docs .ui-tabs-nav li a:visited,
        #widget-docs .ui-tabs-nav li a:hover,
        #widget-docs .ui-tabs-nav li a:active {
            font-size: 14px;
            padding: 4px 1.2em 3px;
            color: #fff;
        }

        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:link,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:visited,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:hover,
        #widget-docs .ui-tabs-nav li.ui-tabs-selected a:active {
            color: #e6820E;
        }

        #widget-docs .ui-tabs-panel {
            padding: 20px 9px;
            font-size: 12px;
            line-height: 1.4;
            color: #000;
        }

        #widget-docs .ui-widget-content a:link,
        #widget-docs .ui-widget-content a:visited {
            color: #1b75bb;
            text-decoration: none;
        }

        #widget-docs .ui-widget-content a:hover,
        #widget-docs .ui-widget-content a:active {
            color: rgba(240, 240, 240, 0.52);
        }
    </style>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.7.1.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.core.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.widget.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.ui.tabs.js"/>"></script>
    <script type="text/javascript">
        $(function () {
            $("#tabs").tabs();
        });
    </script>
</head>

<body>

${navCrumbs}

<div id="tabs">
    <ul>
        <li><a href="#documents">Documents</a></li>
        <li><a href="#coupons">Coupons</a></li>
        <li><a href="#videos">Videos</a></li>
        <li><a href="#images">Images</a></li>
        <li><a href="#links">Links</a></li>
        <li><a href="#events">Events</a></li>
        <li><a href="#articles">Articles</a></li>
    </ul>

    <div id="documents" style="border-top: 1px solid rgb(204, 204, 204);">
        <c:if test="${fn:length(documents) gt 0}">
            <div class="sh_container">
                <table class="datatable">
                    <thead>
                    <tr>
                        <th class="title textAlign">Title</th>
                        <th class="audit textAlign">Modified Date</th>
                        <th class="audit textAlign">Modified By</th>
                        <th class="audit textAlign">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${documents}" var="document">
                        <tr style="border-bottom: 1px dotted #000000;">
                            <td class="title textAlign"><span class="document">${document.name}</span></td>
                            <td class="audit textAlign"><fmt:formatDate value="${document.modifiedDate}"
                                                              pattern="MM/dd/yyyy hh:mm"/></td>
                            <td class="audit textAlign">
                                <c:if test="${! empty document.modifiedBy.username}">
                                    ${document.modifiedBy.username}
                                </c:if>
                                <c:if test="${empty document.modifiedBy.username}">
                                    admin
                                </c:if>
                            </td>
                            <td>
                                <a title="Edit Document"
                                   href="<c:url value="/admin/document/edit.do?id=${document.id}"/>"><span
                                    class="edit action_link"></span></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </c:if>
        <span>
            <a href="<c:url value='/admin/document/add.do?siteID='/>${retailerSite.id}"
               class="gbutton">Add New Document</a>
        </span>
    </div>

    <div id="coupons" style="border-top: 1px solid rgb(204, 204, 204);">
        Will be available in the Upcoming Releases
    </div>

    <div id="videos" style="border-top: 1px solid rgb(204, 204, 204);">
        <c:if test="${fn:length(videos) gt 0}">
            <div class="sh_container">
                <table class="datatable">
                    <thead>
                    <tr>
                        <th class="title textAlign">Title</th>
                        <th class="audit textAlign">Description</th>
                        <th class="audit textAlign">URL</th>
                        <th class="audit textAlign">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${videos}" var="video">
                        <tr style="border-bottom: 1px dotted #000000;">
                            <td class="title textAlign"><span class="video">${video.name}</span></td>
                            <td class="title textAlign"><span >${video.description}</span></td>
                            <td class="title textAlign"><span >${video.url}</span></td>
                            <td>
                                                <span>
                                                    <a title="Edit Video"
                                                       href="<c:url value="/admin/videos/edit.do?id=${video.id}"/>"><span
                                                        class="edit action_link"></span></a>
                                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
            <span>
                <a href="<c:url value='/admin/videos/add.do?siteID='/>${retailerSite.id}"
                   class="gbutton">Add New Video</a>
            </span>
    </div>

    <div id="images" style="border-top: 1px solid rgb(204, 204, 204);">
        <c:if test="${fn:length(images) gt 0}">
            <div class="sh_container">
                <table class="datatable">
                    <thead>
                    <tr>
                        <th class="title textAlign">Title</th>
                        <th class="audit textAlign">Description</th>
                        <th class="audit textAlign">URL</th>
                        <th class="audit textAlign">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${images}" var="image">
                        <tr style="border-bottom: 1px dotted #000000;">
                            <td class="title textAlign"><span class="image">${image.name}</span></td>
                            <td class="title textAlign"><span >${image.description}</span></td>
                            <td class="title textAlign"><span >${image.url}</span></td>
                            <td>
                                                <span>
                                                    <a title="Edit Image"
                                                       href="<c:url value="/admin/images/edit.do?id=${image.id}"/>"><span
                                                        class="edit action_link"></span></a>
                                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
            <span>
                <a href="<c:url value='/admin/images/add.do?siteID='/>${retailerSite.id}"
                   class="gbutton">Add New Image</a>
            </span>
    </div>
    <div id="links" style="border-top: 1px solid rgb(204, 204, 204);">
         <c:if test="${fn:length(links) gt 0}">
             <div class="sh_container">
                 <table class="datatable">
                     <thead>
                     <tr>
                         <th class="title textAlign">Title</th>
                         <th class="audit textAlign">Description</th>
                         <th class="audit textAlign">URL</th>
                         <th class="audit textAlign">Action</th>
                     </tr>
                     </thead>
                     <tbody>
                     <c:forEach items="${links}" var="link">
                         <tr style="border-bottom: 1px dotted #000000;">
                             <td class="title textAlign"><span class="link">${link.name}</span></td>
                             <td class="title textAlign"><span >${link.description}</span></td>
                             <td class="title textAlign"><span >${link.url}</span></td>
                             <td>
                                                 <span>
                                                     <a title="Edit Link"
                                                        href="<c:url value="/admin/link/edit.do?id=${link.id}"/>"><span
                                                         class="edit action_link"></span></a>
                                                 </span>
                             </td>
                         </tr>
                     </c:forEach>
                     </tbody>
                 </table>
             </div>
         </c:if>
             <span>
                 <a href="<c:url value='/admin/link/add.do?siteID='/>${retailerSite.id}"
                    class="gbutton">Add New Link</a>
             </span>
     </div>

    <div id="events" style="border-top: 1px solid rgb(204, 204, 204);">
        <c:if test="${fn:length(events) gt 0}">
            <div class="sh_container">
                <table class="datatable">
                    <thead>
                    <tr>
                        <th class="title textAlign">Title</th>
                        <th class="audit textAlign">Modified Date</th>
                        <th class="audit textAlign">Modified By</th>
                        <th class="audit textAlign">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${events}" var="event">
                        <tr style="border-bottom: 1px dotted #000000;">
                            <td class="title textAlign"><span class="event">${event.name}</span></td>
                            <td><span class="audit textAlign"><fmt:formatDate value="${event.modifiedDate}"
                                                                    pattern="MM/dd/yyyy hh:mm"/></span></td>
                            <td><span class="audit textAlign">
                                            <c:if test="${! empty event.modifiedBy.username}">
                                                ${event.modifiedBy.username}
                                            </c:if>
                                            <c:if test="${empty event.modifiedBy.username}">
                                                admin
                                            </c:if>
                                        </span></td>
                            <td>
                                            <span>
                                                <a title="Edit Event"
                                                   href="<c:url value="/admin/events/edit.do?id=${event.id}&siteId=${event.retailerSite.id}"/>"><span
                                                    class="edit action_link"></span></a>
                                            </span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <span>
            <a href="<c:url value='/admin/events/add.do?siteID='/>${retailerSite.id}"
               class="gbutton">Add New Event</a>
        </span>
    </div>


    <div id="articles" style="border-top: 1px solid rgb(204, 204, 204);">
        <c:if test="${fn:length(articles) gt 0}">
            <div class="sh_container">
                <table class="datatable">
                    <thead>
                    <tr>
                        <th class="title textAlign">Title</th>
                        <th class="audit textAlign">Modified Date</th>
                        <th class="audit textAlign">Modified By</th>
                        <th class="audit textAlign">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${articles}" var="article">
                        <tr style="border-bottom: 1px dotted #000000;">
                            <td class="title textAlign"><span class="document">${article.name}</span></td>
                            <td><span class="audit textAlign"><fmt:formatDate value="${article.modifiedDate}"
                                                                    pattern="MM/dd/yyyy hh:mm"/></span></td>
                            <td><span class="audit textAlign">
                                            <c:if test="${! empty article.modifiedBy.username}">
                                                ${article.modifiedBy.username}
                                            </c:if>
                                            <c:if test="${empty article.modifiedBy.username}">
                                                admin
                                            </c:if>
                                        </span></td>
                            <td>
                                <a title="Edit Article"
                                   href="<c:url value="/admin/article/edit.do?id=${article.id}"/>"><span
                                    class="edit action_link"></span></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
        <span>
            <a href="<c:url value='/admin/article/add.do?siteID='/>${retailerSite.id}"
               class="gbutton">Add New Article</a>
        </span>
    </div>
</div>
</body>
</html>