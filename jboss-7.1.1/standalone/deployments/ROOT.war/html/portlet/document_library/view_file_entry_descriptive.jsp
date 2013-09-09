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
FileEntry fileEntry = (FileEntry)request.getAttribute("view_entries.jsp-fileEntry");

FileVersion latestFileVersion = fileEntry.getFileVersion();

if ((user.getUserId() == fileEntry.getUserId()) || permissionChecker.isCompanyAdmin() || permissionChecker.isGroupAdmin(scopeGroupId) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE)) {
	latestFileVersion = fileEntry.getLatestFileVersion();
}

DLFileShortcut fileShortcut = (DLFileShortcut)request.getAttribute("view_entries.jsp-fileShortcut");

PortletURL tempRowURL = (PortletURL)request.getAttribute("view_entries.jsp-tempRowURL");

boolean showCheckBox = DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.DELETE) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE);
%>

<div class="document-display-style display-descriptive <%= showCheckBox ? "selectable" : StringPool.BLANK %>" data-draggable="<%= showCheckBox ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" data-title="<%= StringUtil.shorten(fileEntry.getTitle(), 60) %>">
	<a class="document-link" data-folder="<%= Boolean.FALSE.toString() %>" href="<%= tempRowURL.toString() %>" title="<%= HtmlUtil.escapeAttribute(HtmlUtil.unescape(fileEntry.getTitle()) + " - " + HtmlUtil.unescape(fileEntry.getDescription())) %>">
		<span class="document-thumbnail">

			<%
			String thumbnailSrc = DLUtil.getThumbnailSrc(fileEntry, fileShortcut, themeDisplay);
			String thumbnailStyle = DLUtil.getThumbnailStyle();
			%>

			<img alt="" border="no" src="<%= thumbnailSrc %>" style="<%= thumbnailStyle %>" />

			<c:if test="<%= fileShortcut != null %>">
				<img alt="<liferay-ui:message key="shortcut" />" class="shortcut-icon" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_link.png">
			</c:if>

			<c:if test="<%= fileEntry.isCheckedOut() %>">
				<img alt="<liferay-ui:message key="locked" />" class="locked-icon" src="<%= themeDisplay.getPathThemeImages() %>/file_system/large/overlay_lock.png">
			</c:if>
		</span>

		<span class="entry-title">
			<%= fileEntry.getTitle() %>

			<c:if test="<%= latestFileVersion.isDraft() || latestFileVersion.isPending() %>">

				<%
				String statusLabel = WorkflowConstants.toLabel(latestFileVersion.getStatus());
				%>

				<span class="workflow-status-<%= statusLabel %>">
					(<liferay-ui:message key="<%= statusLabel %>" />)
				</span>
			</c:if>
		</span>

		<span class="document-description"><%= fileEntry.getDescription() %></span>
	</a>

	<%
	request.removeAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	%>

	<liferay-util:include page="/html/portlet/document_library/file_entry_action.jsp" />

	<c:if test="<%= showCheckBox %>">

		<%
		String rowCheckerName = FileEntry.class.getSimpleName();
		long rowCheckerId = fileEntry.getFileEntryId();

		if (fileShortcut != null) {
			rowCheckerName = DLFileShortcut.class.getSimpleName();
			rowCheckerId = fileShortcut.getFileShortcutId();
		}
		%>

		<aui:input cssClass="overlay document-selector" label="" name="<%= RowChecker.ROW_IDS + rowCheckerName %>" type="checkbox" value="<%= rowCheckerId %>" />
	</c:if>
</div>