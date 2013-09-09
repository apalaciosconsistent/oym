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

<%@ include file="/html/portlet/workflow_tasks/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "assigned-to-me");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="assignedToMeURL">
		<portlet:param name="struts_action" value="/workflow_tasks/view" />
		<portlet:param name="toolbarItem" value="assigned-to-me" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button assigned-to-me <%= toolbarItem.equals("assigned-to-me") ? "current" : StringPool.BLANK %>">
		<a href="<%= assignedToMeURL %>"><liferay-ui:message key="assigned-to-me" /></a>
	</span>

	<portlet:renderURL var="assignedToMyRolesURL">
		<portlet:param name="struts_action" value="/workflow_tasks/view" />
		<portlet:param name="toolbarItem" value="assigned-to-my-roles" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button assigned-to-my-role <%= toolbarItem.equals("assigned-to-my-roles") ? "current" : StringPool.BLANK %>">
		<a href="<%= assignedToMyRolesURL %>"><liferay-ui:message key="assigned-to-my-roles" /></a>
	</span>

	<portlet:renderURL var="completedURL">
		<portlet:param name="struts_action" value="/workflow_tasks/view" />
		<portlet:param name="toolbarItem" value="my-completed-tasks" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button completed-button <%= toolbarItem.equals("my-completed-tasks") ? "current" : StringPool.BLANK %>">
		<a href="<%= completedURL %>"><liferay-ui:message key="my-completed-tasks" /></a>
	</span>
</div>