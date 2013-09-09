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

<%@ include file="/html/portlet/layout_set_prototypes/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewLayoutSetPrototypesURL">
		<portlet:param name="struts_action" value="/layout_set_prototypes/view" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewLayoutSetPrototypesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_LAYOUT_SET_PROTOTYPE) %>">
		<portlet:renderURL var="addLayoutSetPrototypeURL">
			<portlet:param name="struts_action" value="/layout_set_prototypes/edit_layout_set_prototype" />
			<portlet:param name="redirect" value="<%= viewLayoutSetPrototypesURL %>" />
			<portlet:param name="backURL" value="<%= viewLayoutSetPrototypesURL %>" />
		</portlet:renderURL>

		<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : StringPool.BLANK %>">
			<a href="<%= addLayoutSetPrototypeURL %>"><liferay-ui:message key="add" /></a>
		</span>
	</c:if>
</div>