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

String eventType = ParamUtil.getString(request, "eventType");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/calendar/view");
portletURL.setParameter("tabs1", tabs1);

String[] urlArray = PortalUtil.stripURLAnchor(portletURL.toString(), "&#");

String urlWithoutAnchor = urlArray[0];
String urlAnchor = urlArray[1];
%>

<aui:form method="post" name="fm">
	<liferay-util:include page="/html/portlet/calendar/tabs1.jsp" />

	<c:choose>
		<c:when test='<%= tabs1.equals("summary") %>'>
			<%@ include file="/html/portlet/calendar/summary.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("day") %>'>
			<%@ include file="/html/portlet/calendar/day.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("week") %>'>
			<%@ include file="/html/portlet/calendar/week.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("month") %>'>
			<%@ include file="/html/portlet/calendar/month.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("year") %>'>
			<%@ include file="/html/portlet/calendar/year.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("events") %>'>
			<%@ include file="/html/portlet/calendar/events.jspf" %>
		</c:when>
		<c:when test='<%= tabs1.equals("export-import") %>'>
			<%@ include file="/html/portlet/calendar/export_import.jspf" %>
		</c:when>
	</c:choose>
</aui:form>

<%
if (!tabs1.equals("summary")) {
	PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, tabs1), currentURL);
}
%>