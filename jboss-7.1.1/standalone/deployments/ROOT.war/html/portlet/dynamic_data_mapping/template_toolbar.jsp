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

<%@ include file="/html/portlet/dynamic_data_mapping/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");

long groupId = ParamUtil.getLong(request, "groupId", scopeGroupId);
long structureId = ParamUtil.getLong(request, "structureId");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewTemplatesURL">
		<portlet:param name="struts_action" value="/dynamic_data_mapping/view_template" />
		<portlet:param name="structureId" value="<%= String.valueOf(structureId) %>" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewTemplatesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<%
	String message = "add";
	%>

	<c:if test="<%= DDMPermission.contains(permissionChecker, scopeGroupId, ddmResource, ActionKeys.ADD_TEMPLATE) && (Validator.isNull(templateTypeValue) || templateTypeValue.equals(DDMTemplateConstants.TEMPLATE_TYPE_DETAIL)) %>">
		<portlet:renderURL var="addTemplateURL">
			<portlet:param name="struts_action" value="/dynamic_data_mapping/edit_template" />
			<portlet:param name="redirect" value="<%= viewTemplatesURL %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" />
			<portlet:param name="structureId" value="<%= String.valueOf(structureId) %>" />
			<portlet:param name="structureAvailableFields" value='<%= renderResponse.getNamespace() + "structureAvailableFields" %>' />
		</portlet:renderURL>

		<%
		if (Validator.isNull(templateTypeValue)) {
			message = "add-detail-template";
		}
		%>

		<span class="lfr-toolbar-button add-template <%= toolbarItem.equals("add-detail-template") ? "current" : StringPool.BLANK %>">
			<a href="<%= addTemplateURL %>"><liferay-ui:message key="<%= message %>" /></a>
		</span>
	</c:if>

	<c:if test="<%= DDMPermission.contains(permissionChecker, scopeGroupId, ddmResource, ActionKeys.ADD_TEMPLATE) && (Validator.isNull(templateTypeValue) || templateTypeValue.equals(DDMTemplateConstants.TEMPLATE_TYPE_LIST)) %>">
		<portlet:renderURL var="addTemplateURL">
			<portlet:param name="struts_action" value="/dynamic_data_mapping/edit_template" />
			<portlet:param name="redirect" value="<%= viewTemplatesURL %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" />
			<portlet:param name="structureId" value="<%= String.valueOf(structureId) %>" />
			<portlet:param name="type" value="list" />
		</portlet:renderURL>

		<%
		if (Validator.isNull(templateTypeValue)) {
			message = "add-list-template";
		}
		%>

		<span class="lfr-toolbar-button view-templates <%= toolbarItem.equals("add-list-template") ? "current" : StringPool.BLANK %>">
			<a href="<%= addTemplateURL %>"><liferay-ui:message key="<%= message %>" /></a>
		</span>
	</c:if>
</div>