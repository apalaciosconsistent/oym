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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%
themeDisplay.setIncludeServiceJs(true);

Layout selLayout = (Layout)request.getAttribute("edit_pages.jsp-selLayout");

long groupId = selLayout.getGroupId();
String className = Layout.class.getName();
long classPK = selLayout.getPlid();

String redirect = null;
%>

<%@ include file="/html/portlet/layouts_admin/layout/mobile_rule_groups_header.jspf" %>

<%
String rootNodeName = (String)request.getAttribute("edit_pages.jsp-rootNodeName");

PortletURL redirectURL = (PortletURL)request.getAttribute("edit_pages.jsp-redirectURL");

int mdrRuleGroupInstancesCount = MDRRuleGroupInstanceServiceUtil.getRuleGroupInstancesCount(className, classPK);
%>

<aui:input checked="<%= mdrRuleGroupInstancesCount == 0 %>" disabled="<%= mdrRuleGroupInstancesCount > 0 %>" id="inheritRuleGroupInstances" label='<%= LanguageUtil.format(pageContext, "use-the-same-mobile-device-rule-groups-of-the-x-x", new String[] {HtmlUtil.escape(rootNodeName), redirectURL.toString()}) %>' name="inheritRuleGroupInstances" type="radio" value="<%= true %>" />

<aui:input checked="<%= mdrRuleGroupInstancesCount > 0 %>" id="uniqueRuleGroupInstances" label="define-specific-mobile-rule-groups-for-this-page" name="inheritRuleGroupInstances" type="radio" value="<%= false %>" />

<div class="<%= (mdrRuleGroupInstancesCount == 0) ? StringPool.BLANK : "aui-helper-hidden" %>" id="<portlet:namespace />inheritRuleGroupInstancesContainer">
	<div class="portlet-msg-info">
		<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(rootNodeName), redirectURL.toString()} %>" key="mobile-device-rule-groups-will-be-inhertited-from-x-x" />
	</div>
</div>

<div class="<%= (mdrRuleGroupInstancesCount > 0) ? StringPool.BLANK : "aui-helper-hidden" %>" id="<portlet:namespace />uniqueRuleGroupInstancesContainer">
	<%@ include file="/html/portlet/layouts_admin/layout/mobile_rule_groups_toolbar.jspf" %>

	<%@ include file="/html/portlet/layouts_admin/layout/mobile_rule_groups_rule_group_instances.jspf" %>
</div>

<aui:script>
	Liferay.Util.toggleRadio('<portlet:namespace />inheritRuleGroupInstances', '<portlet:namespace />inheritRuleGroupInstancesContainer', '<portlet:namespace />uniqueRuleGroupInstancesContainer');
	Liferay.Util.toggleRadio('<portlet:namespace />uniqueRuleGroupInstances', '<portlet:namespace />uniqueRuleGroupInstancesContainer', '<portlet:namespace />inheritRuleGroupInstancesContainer');
</aui:script>