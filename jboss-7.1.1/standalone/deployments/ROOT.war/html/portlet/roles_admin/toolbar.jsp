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

<%@ include file="/html/portlet/roles_admin/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewRolesURL">
		<portlet:param name="struts_action" value="/roles_admin/view" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewRolesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_ROLE) %>">
		<portlet:renderURL var="viewRolesURL">
			<portlet:param name="struts_action" value="/roles_admin/view" />
		</portlet:renderURL>

		<liferay-portlet:renderURL varImpl="addRoleURL">
			<portlet:param name="struts_action" value="/roles_admin/edit_role" />
			<portlet:param name="redirect" value="<%= viewRolesURL %>" />
		</liferay-portlet:renderURL>

		<liferay-ui:icon-menu align="left" cssClass='<%= "lfr-toolbar-button add-button " + (toolbarItem.equals("add") ? "current" : StringPool.BLANK) %>' direction="down" extended="<%= false %>" icon='<%= themeDisplay.getPathThemeImages() + "/common/add.png" %>' message="add">

			<%
			addRoleURL.setParameter("type", String.valueOf(RoleConstants.TYPE_REGULAR));
			%>

			<liferay-ui:icon
				image="user_icon"
				message="regular-role"
				method="get"
				url="<%= addRoleURL.toString() %>"
			/>

			<%
			addRoleURL.setParameter("type", String.valueOf(RoleConstants.TYPE_SITE));
			%>

			<liferay-ui:icon
				image="site_icon"
				message="site-role"
				method="get"
				url="<%= addRoleURL.toString() %>"
			/>

			<%
			addRoleURL.setParameter("type", String.valueOf(RoleConstants.TYPE_ORGANIZATION));
			%>

			<liferay-ui:icon
				image="organization_icon"
				message="organization-role"
				method="get"
				url="<%= addRoleURL.toString() %>"
			/>
		</liferay-ui:icon-menu>
	</c:if>
</div>