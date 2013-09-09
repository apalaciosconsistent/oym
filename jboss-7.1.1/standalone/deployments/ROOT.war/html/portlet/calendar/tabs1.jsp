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

<%@ include file="/html/portlet/calendar/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", tabs1Default);

PortletURL tabs1URL = renderResponse.createRenderURL();

tabs1URL.setParameter("struts_action", "/calendar/view");
tabs1URL.setParameter("month", String.valueOf(selMonth));
tabs1URL.setParameter("day", String.valueOf(selDay));
tabs1URL.setParameter("year", String.valueOf(selYear));
%>

<liferay-ui:tabs
	names="<%= tabs1Names %>"
	url="<%= tabs1URL.toString() %>"
	value="<%= tabs1 %>"
/>