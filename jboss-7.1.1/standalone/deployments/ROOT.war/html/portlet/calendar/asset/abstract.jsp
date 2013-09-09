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
int abstractLength = (Integer)request.getAttribute(WebKeys.ASSET_PUBLISHER_ABSTRACT_LENGTH);

CalEvent event = (CalEvent)request.getAttribute(WebKeys.CALENDAR_EVENT);
%>

<%= StringUtil.shorten(HtmlUtil.extractText(HtmlUtil.escape(event.getDescription())), abstractLength) %><br />

<liferay-ui:icon
	image="../common/calendar"
/>

<liferay-ui:message key="start-date" />:

<c:choose>
	<c:when test="<%= event.isTimeZoneSensitive() %>">
		<%= dateFormatDate.format(Time.getDate(event.getStartDate(), timeZone)) %>
	</c:when>
	<c:otherwise>
		<%= dateFormatDate.format(event.getStartDate()) %>
	</c:otherwise>
</c:choose>