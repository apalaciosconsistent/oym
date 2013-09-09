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
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

CalEvent event = null;

boolean view = false;

if (row != null) {
	event = (CalEvent)row.getObject();
}
else {
	event = (CalEvent)request.getAttribute("view_event.jsp-event");

	view = true;
}
%>

<liferay-ui:icon-menu showExpanded="<%= view %>" showWhenSingleIcon="<%= view %>">
	<c:if test="<%= CalEventPermission.contains(permissionChecker, event, ActionKeys.UPDATE) %>">
		<portlet:renderURL var="editURL">
			<portlet:param name="struts_action" value="/calendar/edit_event" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="backURL" value="<%= currentURL %>" />
			<portlet:param name="eventId" value="<%= String.valueOf(event.getEventId()) %>" />
		</portlet:renderURL>

		<liferay-ui:icon
			image="edit"
			url="<%= editURL %>"
		/>
	</c:if>

	<c:if test="<%= CalEventPermission.contains(permissionChecker, event, ActionKeys.VIEW) %>">
		<portlet:actionURL var="exportURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="struts_action" value="/calendar/export_events" />
			<portlet:param name="redirect" value="<%= currentURL %>" />
			<portlet:param name="eventId" value="<%= String.valueOf(event.getEventId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon
			image="export"
			url="<%= exportURL %>"
		/>
	</c:if>

	<c:if test="<%= CalEventPermission.contains(permissionChecker, event, ActionKeys.PERMISSIONS) %>">
		<liferay-security:permissionsURL
			modelResource="<%= CalEvent.class.getName() %>"
			modelResourceDescription="<%= event.getTitle() %>"
			resourcePrimKey="<%= String.valueOf(event.getEventId()) %>"
			var="permissionsURL"
		/>

		<liferay-ui:icon
			image="permissions"
			url="<%= permissionsURL %>"
		/>
	</c:if>

	<c:if test="<%= CalEventPermission.contains(permissionChecker, event, ActionKeys.DELETE) %>">
		<portlet:renderURL var="redirectURL">
			<portlet:param name="struts_action" value="/calendar/view" />
		</portlet:renderURL>

		<portlet:actionURL var="deleteURL">
			<portlet:param name="struts_action" value="/calendar/edit_event" />
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
			<portlet:param name="redirect" value="<%= view ? redirectURL : currentURL %>" />
			<portlet:param name="eventId" value="<%= String.valueOf(event.getEventId()) %>" />
		</portlet:actionURL>

		<liferay-ui:icon-delete url="<%= deleteURL %>" />
	</c:if>
</liferay-ui:icon-menu>