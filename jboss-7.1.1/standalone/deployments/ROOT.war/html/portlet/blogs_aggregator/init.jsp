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

<%@ include file="/html/portlet/init.jsp" %>

<%@ page import="com.liferay.portlet.blogs.model.BlogsEntry" %><%@
page import="com.liferay.portlet.blogs.service.BlogsEntryServiceUtil" %><%@
page import="com.liferay.portlet.blogs.service.permission.BlogsEntryPermission" %><%@
page import="com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil" %>

<%
PortletPreferences preferences = portletPreferences;

String selectionMethod = preferences.getValue("selectionMethod", "users");
long organizationId = GetterUtil.getLong(preferences.getValue("organizationId", "0"));
String displayStyle = preferences.getValue("displayStyle", "abstract");
int max = GetterUtil.getInteger(preferences.getValue("max", "20"));
boolean enableRssSubscription = GetterUtil.getBoolean(preferences.getValue("enableRssSubscription", null), true);
boolean showTags = GetterUtil.getBoolean(preferences.getValue("showTags", null), true);

if (organizationId == 0) {
	Group group = GroupLocalServiceUtil.getGroup(scopeGroupId);

	if (group.isOrganization()) {
		organizationId = group.getOrganizationId();
	}
}

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
%>

<%@ include file="/html/portlet/blogs_aggregator/init-ext.jsp" %>