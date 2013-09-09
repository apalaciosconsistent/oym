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

<%@ include file="/html/portlet/document_library/init.jsp" %>

<%
long folderId = GetterUtil.getLong((String)request.getAttribute("view.jsp-folderId"));

String orderByCol = ParamUtil.getString(request, "orderByCol");
String orderByType = ParamUtil.getString(request, "orderByType");

String reverseOrderByType = "asc";

if (orderByType.equals("asc")) {
	reverseOrderByType = "desc";
}
%>

<liferay-ui:icon-menu align="left" direction="down" icon="" message="sort-by" showExpanded="<%= false %>" showWhenSingleIcon="<%= false %>">

	<%
	String taglibUrl = "javascript:" + liferayPortletResponse.getNamespace() + "sortEntries('" + folderId + "', 'title','" + reverseOrderByType + "')";
	%>

	<liferay-ui:icon
		message="title"
		url="<%= taglibUrl %>"
	/>

	<%
	taglibUrl = "javascript:" + liferayPortletResponse.getNamespace() + "sortEntries('" + folderId + "', 'creationDate','" + reverseOrderByType + "')";
	%>

	<liferay-ui:icon
		message="create-date"
		url="<%= taglibUrl %>"
	/>

	<%
	taglibUrl = "javascript:" + liferayPortletResponse.getNamespace() + "sortEntries('" + folderId + "', 'modifiedDate','" + reverseOrderByType + "')";
	%>

	<liferay-ui:icon
		message="modified-date"
		url="<%= taglibUrl %>"
	/>

	<%
	taglibUrl = "javascript:" + liferayPortletResponse.getNamespace() + "sortEntries('" + folderId + "', 'downloads','" + reverseOrderByType + "')";
	%>

	<liferay-ui:icon
		message="downloads"
		url="<%= taglibUrl %>"
	/>

	<%
	taglibUrl = "javascript:" + liferayPortletResponse.getNamespace() + "sortEntries('" + folderId + "', 'size','" + reverseOrderByType + "')";
	%>

	<liferay-ui:icon
		message="size"
		url="<%= taglibUrl %>"
	/>
</liferay-ui:icon-menu>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />sortEntries',
		function(folderId, orderByCol, reverseOrderByType) {
			Liferay.fire(
				'<portlet:namespace />dataRequest',
				{
					requestParams: {
						'<portlet:namespace />folderId': folderId,
						'<portlet:namespace />struts_action': '/document_library/view',
						'<portlet:namespace />viewEntries': <%= Boolean.FALSE.toString() %>,
						'<portlet:namespace />viewEntriesPage': <%= Boolean.TRUE.toString() %>,
						'<portlet:namespace />viewFolders': <%= Boolean.FALSE.toString() %>,
						'<portlet:namespace />orderByCol': orderByCol,
						'<portlet:namespace />orderByType': reverseOrderByType,
						'<portlet:namespace />saveOrderBy': <%= Boolean.TRUE.toString() %>
					}
				}
			);
		},
		['aui-base']
	);
</aui:script>