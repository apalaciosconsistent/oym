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

<%@ include file="/html/portlet/layout_prototypes/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewLayoutPrototypesURL">
		<portlet:param name="struts_action" value="/layout_prototypes/view" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewLayoutPrototypesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_LAYOUT_PROTOTYPE) %>">
		<portlet:renderURL var="addLayoutPrototypeURL">
			<portlet:param name="struts_action" value="/layout_prototypes/edit_layout_prototype" />
			<portlet:param name="redirect" value="<%= viewLayoutPrototypesURL %>" />
			<portlet:param name="backURL" value="<%= viewLayoutPrototypesURL %>" />
		</portlet:renderURL>

		<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : StringPool.BLANK %>">
			<a href="<%= addLayoutPrototypeURL %>"><liferay-ui:message key="add" /></a>
		</span>
	</c:if>
</div>