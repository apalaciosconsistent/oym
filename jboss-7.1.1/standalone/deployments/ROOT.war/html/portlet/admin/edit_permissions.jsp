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

<%@ include file="/html/portlet/admin/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "merge-redundant-roles");
String tabs2 = ParamUtil.getString(request, "tabs2", "organizations");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/admin_server/edit_permissions");
portletURL.setParameter("tabs1", tabs1);
portletURL.setParameter("tabs2", tabs2);

request.setAttribute("edit_permissions.jsp-tabs2", tabs2);

request.setAttribute("edit_permissions.jsp-portletURL", portletURL);
%>

<aui:form method="post" name="fm">
	<liferay-ui:tabs
		names="merge-redundant-roles,reassign-to-system-role"
		param="tabs1"
		url="<%= portletURL.toString() %>"
	/>

	<liferay-ui:tabs
		names="organizations,sites,users"
		param="tabs2"
		url="<%= portletURL.toString() %>"
	/>

	<c:choose>
		<c:when test='<%= tabs1.equals("merge-redundant-roles") %>'>
			<liferay-util:include page="/html/portlet/admin/edit_permissions_merge.jsp" />
		</c:when>
		<c:otherwise>
			<liferay-util:include page="/html/portlet/admin/edit_permissions_reassign.jsp" />
		</c:otherwise>
	</c:choose>
</aui:form>

<aui:script>
	function <portlet:namespace />invoke(link) {
		submitForm(document.<portlet:namespace />fm, link);
	}
</aui:script>