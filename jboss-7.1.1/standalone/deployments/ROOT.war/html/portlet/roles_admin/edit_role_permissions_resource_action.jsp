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

<%@ include file="/html/portlet/roles_admin/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Object[] objArray = (Object[])row.getObject();

String target = (String)objArray[3];
Boolean supportsFilterByGroup = (Boolean)objArray[5];
%>

<c:if test="<%= supportsFilterByGroup %>">
	<portlet:renderURL var="selectCommunityURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="struts_action" value="/roles_admin/select_site" />
		<portlet:param name="target" value="<%= target %>" />
		<portlet:param name="includeCompany" value="<%= Boolean.TRUE.toString() %>" />
		<portlet:param name="includeUserPersonalSite" value="<%= Boolean.TRUE.toString() %>" />
	</portlet:renderURL>

	<%
	String limitScopeURL = "javascript:var groupWindow = window.open('" + selectCommunityURL + "', 'site', 'directories=no,height=640,location=no,menubar=no,resizable=yes,scrollbars=yes,status=no,toolbar=no,width=680'); void(''); groupWindow.focus();";
	%>

	<liferay-ui:icon
		image="add"
		label="<%= true %>"
		message="limit-scope"
		url="<%= limitScopeURL %>"
	/>
</c:if>