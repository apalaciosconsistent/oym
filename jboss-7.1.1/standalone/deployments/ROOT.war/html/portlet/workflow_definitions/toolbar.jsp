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

<%@ include file="/html/portlet/workflow_definitions/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewDefinitionsURL">
		<portlet:param name="struts_action" value="/workflow_definitions/view" />
		<portlet:param name="tabs1" value="workflow-definitions" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewDefinitionsURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<portlet:renderURL var="addWorkflowDefinitionURL">
		<portlet:param name="struts_action" value="/workflow_definitions/edit_workflow_definition" />
		<portlet:param name="tabs1" value="workflow-definitions" />
		<portlet:param name="redirect" value="<%= viewDefinitionsURL %>" />
		<portlet:param name="backURL" value="<%= viewDefinitionsURL %>" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : StringPool.BLANK %>">
		<a href="<%= addWorkflowDefinitionURL %>"><liferay-ui:message key="add" /></a>
	</span>
</div>