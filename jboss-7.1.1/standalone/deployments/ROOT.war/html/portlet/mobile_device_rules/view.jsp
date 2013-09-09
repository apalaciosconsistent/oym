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
String className = ParamUtil.getString(request, "className");
long classPK = ParamUtil.getLong(request, "classPK");
String chooseCallback = ParamUtil.getString(request, "chooseCallback");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/mobile_device_rules/view");
portletURL.setParameter("groupId", String.valueOf(groupId));
portletURL.setParameter("chooseCallback", chooseCallback);
%>

<c:if test="<%= Validator.isNotNull(chooseCallback) %>">
	<liferay-ui:header
		title="rule-groups"
	/>
</c:if>

<liferay-util:include page="/html/portlet/mobile_device_rules/toolbar.jsp">
	<liferay-util:param name="toolbarItem" value="view" />
</liferay-util:include>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">

	<%
	RuleGroupSearch ruleGroupSearch = new RuleGroupSearch(liferayPortletRequest, portletURL);
	%>

	<liferay-ui:search-container
		searchContainer="<%= ruleGroupSearch %>"
	>
		<liferay-ui:search-form
			page="/html/portlet/mobile_device_rules/rule_group_search.jsp"
		/>

		<%
		RuleGroupSearchTerms searchTerms = (RuleGroupSearchTerms)searchContainer.getSearchTerms();
		%>

		<liferay-ui:search-container-results>
			<%@ include file="/html/portlet/mobile_device_rules/rule_group_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup"
			escapedModel="<%= true %>"
			keyProperty="ruleGroupId"
			modelVar="ruleGroup"
		>

			<%
			String rowHREF = null;

			if (Validator.isNull(chooseCallback)) {
			%>

				<liferay-portlet:renderURL var="editURL">
					<portlet:param name="struts_action" value="/mobile_device_rules/edit_rule_group" />
					<portlet:param name="redirect" value="<%= portletURL.toString() %>" />
					<portlet:param name="ruleGroupId" value="<%= String.valueOf(ruleGroup.getRuleGroupId()) %>" />
				</liferay-portlet:renderURL>

			<%
				rowHREF = editURL;
			}
			else {
				MDRRuleGroupInstance ruleGroupInstance = MDRRuleGroupInstanceLocalServiceUtil.fetchRuleGroupInstance(className, classPK, ruleGroup.getRuleGroupId());

				if (ruleGroupInstance == null) {
					StringBundler sb = new StringBundler(7);

					sb.append("javascript:Liferay.Util.getOpener()['");
					sb.append(HtmlUtil.escapeJS(chooseCallback));
					sb.append("'](");
					sb.append(ruleGroup.getRuleGroupId());
					sb.append(",'");
					sb.append(ruleGroup.getName(locale));
					sb.append("', Liferay.Util.getWindow());");

					rowHREF = sb.toString();
				}
			}
			%>

			<%@ include file="/html/portlet/mobile_device_rules/rule_group_columns.jspf" %>
		</liferay-ui:search-container-row>

		<div class="separator"><!-- --></div>

		<liferay-ui:search-iterator type="more" />
	</liferay-ui:search-container>
</aui:form>