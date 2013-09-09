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

<%@ include file="/html/portlet/sites_admin/init.jsp" %>

<%
long groupId = GetterUtil.getLong((String)request.getAttribute("merge_alert.jsp-groupId"));
LayoutSet layoutSet = (LayoutSet)request.getAttribute("merge_alert.jsp-layoutSet");
String redirect = (String)request.getAttribute("merge_alert.jsp-redirect");

List<Layout> mergeFailFriendlyURLLayouts = SitesUtil.getMergeFailFriendlyURLLayouts(layoutSet);
%>

<c:if test="<%= !mergeFailFriendlyURLLayouts.isEmpty() %>">
	<span class="portlet-msg-alert">
		<liferay-ui:message key="some-pages-from-the-site-template-cannot-be-propagated-because-their-friendly-urls-conflict-with-the-following-pages" />

		<liferay-ui:message key="modify-the-friendly-url-of-the-pages-to-allow-their-propagation-from-the-site-template" />

		<ul>
			<liferay-portlet:renderURL portletName="<%= PortletKeys.GROUP_PAGES %>" varImpl="editLayoutsURL">
				<portlet:param name="struts_action" value="/group_pages/edit_layouts" />
				<portlet:param name="tabs1" value='<%= layoutSet.isPrivateLayout() ? "private-pages" : "public-pages" %>' />
				<portlet:param name="redirect" value="<%= redirect %>" />
				<portlet:param name="closeRedirect" value="<%= redirect %>" />
				<portlet:param name="backURL" value="<%= redirect %>" />
				<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" />
			</liferay-portlet:renderURL>

			<%
			for (Layout mergeFailFriendlyURLLayout : mergeFailFriendlyURLLayouts) {
				editLayoutsURL.setParameter("selPlid", String.valueOf(mergeFailFriendlyURLLayout.getPlid()));
			%>

				<li>
					<aui:a href="<%= editLayoutsURL.toString() %>">
						<%= mergeFailFriendlyURLLayout.getName(locale) %>
					</aui:a>
				</li>

			<%
			}
			%>

		</ul>
	</span>
</c:if>