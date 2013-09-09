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

<%@ include file="/html/portlet/message_boards/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "categories");

PortletURL tabs1URL = renderResponse.createRenderURL();

tabs1URL.setParameter("struts_action", "/message_boards/view");
tabs1URL.setParameter("tabs1", tabs1);

String tabs1Values = "categories,recent_posts,statistics";

if (themeDisplay.isSignedIn()) {
	tabs1Values = "categories,my_posts,my_subscriptions,recent_posts,statistics";

	if (MBPermission.contains(permissionChecker, scopeGroupId, ActionKeys.BAN_USER)) {
		tabs1Values += ",banned_users";
	}
}

String tabs1Names = StringUtil.replace(tabs1Values, StringPool.UNDERLINE, StringPool.DASH);
%>

<liferay-ui:tabs
	names="<%= tabs1Names %>"
	portletURL="<%= tabs1URL %>"
	tabsValues="<%= tabs1Values %>"
/>