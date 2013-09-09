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
Folder folder = (Folder)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FOLDER);

long folderId = ParamUtil.getLong(request, "folderId", DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);

if ((folder == null) && (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID)) {
	try {
		folder = DLAppLocalServiceUtil.getFolder(folderId);
	}
	catch (NoSuchFolderException nsfe) {
		folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
	}
}

long repositoryId = scopeGroupId;

if (folder != null) {
	repositoryId = folder.getRepositoryId();
}

boolean viewEntries = ParamUtil.getBoolean(request, "viewEntries");
boolean viewEntriesPage = ParamUtil.getBoolean(request, "viewEntriesPage");
boolean viewFolders = ParamUtil.getBoolean(request, "viewFolders");

request.setAttribute("view.jsp-folder", folder);

request.setAttribute("view.jsp-folderId", String.valueOf(folderId));

request.setAttribute("view.jsp-repositoryId", String.valueOf(repositoryId));
%>

<div>
	<c:if test="<%= viewEntries %>">

		<%
		PortalUtil.addPortletBreadcrumbEntry(request, themeDisplay.getScopeGroup().getDescriptiveName(), null);
		PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "documents-and-media"), liferayPortletResponse.createRenderURL().toString());
		%>

		<div id="<portlet:namespace />entries">
			<liferay-util:include page="/html/portlet/document_library/view_entries.jsp" />
		</div>

		<span id="<portlet:namespace />addButton">
			<liferay-util:include page="/html/portlet/document_library/add_button.jsp" />
		</span>

		<span id="<portlet:namespace />displayStyleButtons">
			<liferay-util:include page="/html/portlet/document_library/display_style_buttons.jsp" />
		</span>

		<span id="<portlet:namespace />fileEntrySearch">
			<liferay-util:include page="/html/portlet/document_library/file_entry_search.jsp" />
		</span>

		<span id="<portlet:namespace />sortButton">
			<liferay-util:include page="/html/portlet/document_library/sort_button.jsp" />
		</span>

		<span id="<portlet:namespace />breadcrumb">
			<div class="portlet-breadcrumb">
				<liferay-util:include page="/html/portlet/document_library/breadcrumb.jsp" />
			</div>

			<c:if test="<%= layout.isTypeControlPanel() %>">
				<div class="portal-breadcrumb">
					<liferay-ui:breadcrumb showCurrentGroup="<%= true %>" showCurrentPortlet="<%= true %>" showGuestGroup="<%= false %>" showLayout="<%= true %>" showParentGroups="<%= false %>" showPortletBreadcrumb="<%= true %>" />
				</div>
			</c:if>
		</span>
	</c:if>

	<c:if test="<%= viewEntriesPage %>">
		<div id="<portlet:namespace />entries">
			<liferay-util:include page="/html/portlet/document_library/view_entries.jsp" />
		</div>

		<span id="<portlet:namespace />sortButton">
			<liferay-util:include page="/html/portlet/document_library/sort_button.jsp" />
		</span>
	</c:if>

	<c:if test="<%= viewFolders %>">
		<div id="<portlet:namespace />folders">
			<liferay-util:include page="/html/portlet/document_library/view_folders.jsp" />
		</div>
	</c:if>
</div>