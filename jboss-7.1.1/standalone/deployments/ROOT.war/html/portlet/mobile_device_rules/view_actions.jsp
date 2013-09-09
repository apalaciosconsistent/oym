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

<%@ include file="/html/portlet/mobile_device_rules/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

long ruleGroupInstanceId = ParamUtil.getLong(request, "ruleGroupInstanceId");

MDRRuleGroupInstance ruleGroupInstance = MDRRuleGroupInstanceLocalServiceUtil.getRuleGroupInstance(ruleGroupInstanceId);

MDRRuleGroup ruleGroup = ruleGroupInstance.getRuleGroup();

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/mobile_device_rules/view_actions");
portletURL.setParameter("redirect", redirect);
portletURL.setParameter("ruleGroupInstanceId", String.valueOf(ruleGroupInstanceId));
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	localizeTitle="<%= false %>"
	title='<%= LanguageUtil.format(pageContext, "actions-for-x", ruleGroup.getName(locale), false) %>'
/>

<c:if test="<%= MDRPermissionUtil.contains(permissionChecker, groupId, ActionKeys.ADD_RULE_GROUP) %>">
	<liferay-portlet:renderURL var="addURL">
		<portlet:param name="struts_action" value="/mobile_device_rules/edit_action" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="ruleGroupInstanceId" value="<%= String.valueOf(ruleGroupInstanceId) %>" />
	</liferay-portlet:renderURL>

	<div class="lfr-portlet-toolbar">
		<span class="add-button lfr-toolbar-button">
			<a href="<%= addURL %>">
				<liferay-ui:message key="add-action" />
			</a>
		</span>
	</div>
</c:if>

<div class="separator"><!-- --></div>

<liferay-ui:search-container
	delta="<%= 5 %>"
	deltaConfigurable="<%= false %>"
	emptyResultsMessage="no-actions-are-configured-for-this-rule-group-instance"
	headerNames="name,description,type"
	iteratorURL="<%= portletURL %>"
>
	<liferay-ui:search-container-results
		results="<%= MDRActionLocalServiceUtil.getActions(ruleGroupInstanceId, searchContainer.getStart(), searchContainer.getEnd()) %>"
		total="<%= MDRActionLocalServiceUtil.getActionsCount(ruleGroupInstanceId) %>"
	/>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.mobiledevicerules.model.MDRAction"
		escapedModel="<%= true %>"
		keyProperty="actionId"
		modelVar="action"
	>
		<liferay-portlet:renderURL var="rowURL">
			<portlet:param name="struts_action" value="/mobile_device_rules/edit_action" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="actionId" value="<%= String.valueOf(action.getActionId()) %>" />
		</liferay-portlet:renderURL>

		<%@ include file="/html/portlet/mobile_device_rules/action_columns.jspf" %>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator type="more" />
</liferay-ui:search-container>