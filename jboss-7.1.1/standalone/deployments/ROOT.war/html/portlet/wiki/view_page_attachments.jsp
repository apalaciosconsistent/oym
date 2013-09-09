<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/html/portlet/wiki/init.jsp" %>

<%
WikiNode node = (WikiNode)request.getAttribute(WebKeys.WIKI_NODE);
WikiPage wikiPage = (WikiPage)request.getAttribute(WebKeys.WIKI_PAGE);

String[] attachments = wikiPage.getAttachmentsFiles();

PortletURL portletURL = renderResponse.createActionURL();

portletURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
portletURL.setParameter("title", wikiPage.getTitle());

portletURL.setParameter("struts_action", "/wiki/view");

PortalUtil.addPortletBreadcrumbEntry(request, wikiPage.getTitle(), portletURL.toString());

portletURL.setParameter("struts_action", "/wiki/view_page_attachments");

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "attachments"), portletURL.toString());
%>

<liferay-util:include page="/html/portlet/wiki/top_links.jsp" />

<liferay-util:include page="/html/portlet/wiki/page_tabs.jsp">
	<liferay-util:param name="tabs1" value="attachments" />
</liferay-util:include>

<%
List<String> headerNames = new ArrayList<String>();

headerNames.add("file-name");
headerNames.add("size");
headerNames.add(StringPool.BLANK);

SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, currentURLObj, headerNames, "this-page-does-not-have-any-file-attachments");

int total = attachments.length;

searchContainer.setTotal(total);

List results = ListUtil.fromArray(attachments);

results = ListUtil.subList(results, searchContainer.getStart(), searchContainer.getEnd());

searchContainer.setResults(results);

List resultRows = searchContainer.getResultRows();

for (int i = 0; i < results.size(); i++) {
	String fileName = (String)results.get(i);

	String shortFileName = FileUtil.getShortFileName(fileName);

	long fileSize = 0;

	try {
		fileSize = DLStoreUtil.getFileSize(company.getCompanyId(), CompanyConstants.SYSTEM, fileName);
	}
	catch (NoSuchFileException nsfe) {
	}

	ResultRow row = new ResultRow(new Object[] {node, wikiPage, fileName}, fileName, i);

	PortletURL rowURL = renderResponse.createActionURL();

	rowURL.setWindowState(LiferayWindowState.EXCLUSIVE);

	rowURL.setParameter("struts_action", "/wiki/get_page_attachment");
	rowURL.setParameter("nodeId", String.valueOf(node.getNodeId()));
	rowURL.setParameter("title", wikiPage.getTitle());
	rowURL.setParameter("fileName", shortFileName);

	// File name

	StringBundler sb = new StringBundler(6);

	sb.append("<img align=\"left\" border=\"0\" src=\"");
	sb.append(themeDisplay.getPathThemeImages());
	sb.append("/file_system/small/");
	sb.append(DLUtil.getFileIcon(shortFileName));
	sb.append(".png\">&nbsp;");
	sb.append(shortFileName);

	row.addText(sb.toString(), rowURL);

	// Size

	row.addText(TextFormatter.formatKB(fileSize, locale) + "k", rowURL);

	// Action

	row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/wiki/page_attachment_action.jsp");

	// Add result row

	resultRows.add(row);
}
%>

<c:if test="<%= WikiNodePermission.contains(permissionChecker, node.getNodeId(), ActionKeys.ADD_ATTACHMENT) %>">
	<div>
		<input onClick="location.href = '<portlet:renderURL><portlet:param name="struts_action" value="/wiki/edit_page_attachment" /><portlet:param name="nodeId" value="<%= String.valueOf(node.getNodeId()) %>" /><portlet:param name="title" value="<%= wikiPage.getTitle() %>" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:renderURL>';" type="button" value="<liferay-ui:message key="add-attachments" />" />
	</div>

	<br />
</c:if>

<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />