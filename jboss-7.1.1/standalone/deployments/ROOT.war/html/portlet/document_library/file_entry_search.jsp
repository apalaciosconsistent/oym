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
long repositoryId = GetterUtil.getLong((String)request.getAttribute("view.jsp-repositoryId"));

long folderId = GetterUtil.getLong((String)request.getAttribute("view.jsp-folderId"));

List<Folder> mountFolders = DLAppServiceUtil.getMountFolders(repositoryId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, QueryUtil.ALL_POS, QueryUtil.ALL_POS);
%>

<liferay-portlet:resourceURL var="searchURL">
	<portlet:param name="struts_action" value="/document_library/search" />
	<portlet:param name="repositoryId" value="<%= String.valueOf(repositoryId) %>" />
	<portlet:param name="searchRepositoryId" value="<%= String.valueOf(folderId) %>" />
	<portlet:param name="folderId" value="<%= String.valueOf(folderId) %>" />
	<portlet:param name="searchFolderId" value="<%= String.valueOf(folderId) %>" />
</liferay-portlet:resourceURL>

<div class="lfr-search-combobox search-button-container" id="<portlet:namespace />fileEntrySearchContainer">
	<aui:form action="<%= searchURL.toString() %>" method="get" name="fm1" onSubmit='<%= "event.preventDefault(); " + liferayPortletResponse.getNamespace() + "searchFileEntry();" %>'>
		<aui:input cssClass="keywords lfr-search-combobox-item" id="keywords" label="" name="keywords" type="text" />

		<%
		String taglibOnClick = "javascript:" + liferayPortletResponse.getNamespace() + "searchFileEntry();";
		%>

		<aui:button cssClass="lfr-search-combobox-item" name="search" onClick="<%= taglibOnClick %>" value="search" />
	</aui:form>
</div>

<aui:script>
	function <portlet:namespace />searchFileEntry() {
		Liferay.fire(
			'<portlet:namespace />dataRequest',
			{
				requestParams: {
					'<portlet:namespace />struts_action': '/document_library/search',
					'<portlet:namespace />repositoryId': '<%= repositoryId %>',
					'<portlet:namespace />searchRepositoryId': '<%= repositoryId %>',
					'<portlet:namespace />folderId': '<%= folderId %>',
					'<portlet:namespace />searchFolderId': '<%= folderId %>',
					'<portlet:namespace />keywords': document.<portlet:namespace />fm1.<portlet:namespace />keywords.value,
					'<portlet:namespace />searchType': <%= ((repositoryId == scopeGroupId) && (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) ? DLSearchConstants.MULTIPLE : DLSearchConstants.SINGLE %>
				},
				src: Liferay.DL_SEARCH
			}
		);

		<%
		if ((repositoryId == scopeGroupId) && (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {
			for (Folder mountFolder : mountFolders) {
			%>

				Liferay.fire(
					'<portlet:namespace />dataRequest',
					{
						requestParams: {
							'<portlet:namespace />struts_action': '/document_library/search',
							'<portlet:namespace />repositoryId': '<%= mountFolder.getRepositoryId() %>',
							'<portlet:namespace />searchRepositoryId': '<%= mountFolder.getRepositoryId() %>',
							'<portlet:namespace />folderId': '<%= mountFolder.getFolderId() %>',
							'<portlet:namespace />searchFolderId': '<%= mountFolder.getFolderId() %>',
							'<portlet:namespace />keywords': document.<portlet:namespace />fm1.<portlet:namespace />keywords.value,
							'<portlet:namespace />searchType': <%= DLSearchConstants.MULTIPLE %>
						},
						src: Liferay.DL_SEARCH
					}
				);

			<%
			}
		}
		%>

	}
</aui:script>