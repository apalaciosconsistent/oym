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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewUsersURL">
		<portlet:param name="struts_action" value="/users_admin/view" />
		<portlet:param name="usersListView" value="<%= UserConstants.LIST_VIEW_TREE %>" />
		<portlet:param name="saveUsersListView" value="<%= Boolean.TRUE.toString() %>" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewUsersURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<%
	boolean hasAddOrganizationPermission = PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_ORGANIZATION);
	boolean hasAddUserPermission = PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_USER);
	%>

	<c:if test="<%= hasAddOrganizationPermission || hasAddUserPermission %>">
		<liferay-ui:icon-menu align="left" cssClass='<%= "lfr-toolbar-button add-button " + (toolbarItem.equals("add") ? "current" : StringPool.BLANK) %>' direction="down" extended="<%= false %>" icon='<%= themeDisplay.getPathThemeImages() + "/common/add.png" %>' message="add" showWhenSingleIcon="<%= true %>">
			<c:if test="<%= hasAddUserPermission %>">
				<portlet:renderURL var="addUserURL">
					<portlet:param name="struts_action" value="/users_admin/edit_user" />
					<portlet:param name="redirect" value="<%= viewUsersURL %>" />
				</portlet:renderURL>

				<liferay-ui:icon
					image="user_icon"
					message="user"
					url="<%= addUserURL %>"
				/>
			</c:if>

			<c:if test="<%= hasAddOrganizationPermission %>">

				<%
				for (String organizationType : PropsValues.ORGANIZATIONS_TYPES) {
				%>

					<portlet:renderURL var="addOrganizationURL">
						<portlet:param name="struts_action" value="/users_admin/edit_organization" />
						<portlet:param name="redirect" value="<%= viewUsersURL %>" />
						<portlet:param name="type" value="<%= organizationType %>" />
					</portlet:renderURL>

					<liferay-ui:icon
						image="add_location"
						message="<%= LanguageUtil.get(pageContext, organizationType) %>"
						url="<%= addOrganizationURL %>"
					/>

				<%
				}
				%>

			</c:if>
		</liferay-ui:icon-menu>
	</c:if>

	<c:choose>
		<c:when test="<%= PortalPermissionUtil.contains(permissionChecker, ActionKeys.EXPORT_USER) %>">
			<span class="lfr-toolbar-button export-button"><a href="javascript:<portlet:namespace />exportUsers();"><liferay-ui:message key="export-users" /></a></span>
		</c:when>
		<c:when test="<%= PortletPermissionUtil.contains(permissionChecker, PortletKeys.USERS_ADMIN, ActionKeys.EXPORT_USER) %>">
			<span class="lfr-toolbar-button export-button"><a href="javascript:<portlet:namespace />exportUsers();"><liferay-ui:message key="export-organization-users" /></a></span>
		</c:when>
	</c:choose>
</div>

<aui:script>
	function <portlet:namespace />exportUsers() {
		document.<portlet:namespace />fm.method = "post";
		submitForm(document.<portlet:namespace />fm, "<portlet:actionURL><portlet:param name="struts_action" value="/users_admin/export_users" /></portlet:actionURL>&compress=0&etag=0&strip=0", false);
	}
</aui:script>