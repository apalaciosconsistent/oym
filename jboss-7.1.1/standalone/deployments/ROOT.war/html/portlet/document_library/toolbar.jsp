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

<c:if test="<%= !user.isDefaultUser() %>">
	<aui:input cssClass="select-documents aui-state-default" inline="<%= true %>" label="" name="<%= RowChecker.ALL_ROW_IDS %>" type="checkbox" />
</c:if>

<liferay-ui:icon-menu align="left" cssClass="actions-button aui-helper-hidden" direction="down" icon="" id="actionsButtonContainer" message="actions" showExpanded="<%= false %>" showWhenSingleIcon="<%= true %>">

	<%
	Group scopeGroup = themeDisplay.getScopeGroup();
	%>

	<c:if test="<%= !scopeGroup.isStaged() || scopeGroup.isStagingGroup() || !scopeGroup.isStagedPortlet(PortletKeys.DOCUMENT_LIBRARY) %>">

		<%
		String taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "editFileEntry', {action: '" + Constants.CANCEL_CHECKOUT + "'});";
		%>

		<liferay-ui:icon
			image="undo"
			message="cancel-checkout[document]"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>

		<%
		taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "editFileEntry', {action: '" + Constants.CHECKIN + "'});";
		%>

		<liferay-ui:icon
			image="unlock"
			message="checkin"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>

		<%
		taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "editFileEntry', {action: '" + Constants.CHECKOUT + "'});";
		%>

		<liferay-ui:icon
			image="lock"
			message="checkout[document]"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>

		<%
		taglibOnClick = "Liferay.fire('" + renderResponse.getNamespace() + "editFileEntry', {action: '" + Constants.MOVE + "'});";
		%>

		<liferay-ui:icon
			image="submit"
			message="move"
			onClick="<%= taglibOnClick %>"
			url="javascript:;"
		/>
	</c:if>

	<%
	String taglibUrl = "Liferay.fire('" + renderResponse.getNamespace() + "editFileEntry', {action: '" + Constants.DELETE + "'});";
	%>

	<liferay-ui:icon-delete
		confirmation="are-you-sure-you-want-to-delete-the-selected-entries"
		url="<%= taglibUrl %>"
	/>
</liferay-ui:icon-menu>

<span class="add-button" id="<portlet:namespace />addButtonContainer">
	<liferay-util:include page="/html/portlet/document_library/add_button.jsp" />
</span>

<span class="sort-button" id="<portlet:namespace />sortButtonContainer">
	<liferay-util:include page="/html/portlet/document_library/sort_button.jsp" />
</span>

<span class="manage-button">
	<c:if test="<%= !user.isDefaultUser() %>">
		<liferay-ui:icon-menu align="left" direction="down" icon="" message="manage" showExpanded="<%= false %>" showWhenSingleIcon="<%= true %>">

			<%
			String taglibUrl = "javascript:" + renderResponse.getNamespace() + "openFileEntryTypeView()";
			%>

			<liferay-ui:icon
				image="copy"
				message="document-types"
				url="<%= taglibUrl %>"
			/>

			<%
			taglibUrl = "javascript:" + renderResponse.getNamespace() + "openDDMStructureView()";
			%>

			<liferay-ui:icon
				image="copy"
				message="metadata-sets"
				url="<%= taglibUrl %>"
			/>
		</liferay-ui:icon-menu>
	</c:if>
</span>

<aui:script>
	function <portlet:namespace />openFileEntryTypeView() {
		Liferay.Util.openWindow(
			{
				dialog: {
					width:820
				},
				id: '<portlet:namespace />openFileEntryTypeView',
				title: '<%= UnicodeLanguageUtil.get(pageContext, "document-types") %>',
				uri: '<liferay-portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="struts_action" value="/document_library/view_file_entry_type" /><portlet:param name="redirect" value="<%= currentURL %>" /></liferay-portlet:renderURL>'
			}
		);
	}

	function <portlet:namespace />openDDMStructureView() {
		Liferay.Util.openDDMPortlet(
			{
				ddmResource: '<%= ddmResource %>',
				dialog: {
					width: 820
				},
				showGlobalScope: 'true',
				showManageTemplates: 'false',
				storageType: 'xml',
				structureName: 'metadata-set',
				structureType: 'com.liferay.portlet.documentlibrary.model.DLFileEntryMetadata',
				title: '<%= UnicodeLanguageUtil.get(pageContext, "metadata-sets") %>'
			}
		);
	}
</aui:script>