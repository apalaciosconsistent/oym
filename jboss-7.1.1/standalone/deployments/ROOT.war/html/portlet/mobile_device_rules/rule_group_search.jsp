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
String chooseCallback = ParamUtil.getString(request, "chooseCallback");

RuleGroupSearch searchContainer = (RuleGroupSearch)request.getAttribute("liferay-ui:search:searchContainer");

RuleGroupDisplayTerms displayTerms = (RuleGroupDisplayTerms)searchContainer.getDisplayTerms();
RuleGroupSearchTerms searchTerms = (RuleGroupSearchTerms)searchContainer.getSearchTerms();

if (displayTerms.getGroupId() == 0) {
	displayTerms.setGroupId(groupId);
	searchTerms.setGroupId(groupId);
}
%>

<liferay-ui:search-toggle
	buttonLabel="search"
	displayTerms="<%= displayTerms %>"
	id="toggle_id_mobile_device_rules_rule_group_search"
>
	<aui:fieldset>
		<aui:input label="name" name="<%= displayTerms.NAME %>" size="20" type="text" value="<%= displayTerms.getName() %>" />

		<c:choose>
			<c:when test="<%= Validator.isNotNull(chooseCallback) && MDRPermissionUtil.contains(permissionChecker, themeDisplay.getCompanyGroupId(), ActionKeys.VIEW) %>">
				<aui:select label="scope" name="<%= displayTerms.GROUP_ID %>">
					<aui:option label="global" selected="<%= displayTerms.getGroupId() == themeDisplay.getCompanyGroupId() %>" value="<%= themeDisplay.getCompanyGroupId() %>" />

					<%
					Group group = GroupLocalServiceUtil.getGroup(groupId);
					%>

					<aui:option label="<%= HtmlUtil.escape(group.getDescriptiveName(locale)) %>" selected="<%= displayTerms.getGroupId() == groupId %>" value="<%= groupId %>" />
				</aui:select>
			</c:when>
			<c:otherwise>
				<aui:input name="<%= displayTerms.GROUP_ID %>" type="hidden" value="<%= groupId %>" />
			</c:otherwise>
		</c:choose>
	</aui:fieldset>
</liferay-ui:search-toggle>