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
String topLink = ParamUtil.getString(request, "topLink", "message-boards-home");

MBCategory category = (MBCategory)request.getAttribute(WebKeys.MESSAGE_BOARDS_CATEGORY);

long categoryId = MBUtil.getCategoryId(request, category);

boolean viewCategory = GetterUtil.getBoolean((String)request.getAttribute("view.jsp-viewCategory"));

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/message_boards/view");
%>

<div class="top-links-container">
	<div class="top-links">
		<ul class="top-links-navigation">
			<li class="top-link first">

				<%
				portletURL.setParameter("topLink", "message-boards-home");
				portletURL.setParameter("tag", StringPool.BLANK);
				%>

				<liferay-ui:icon
					image="../aui/home"
					label="<%= true %>"
					message="message-boards-home"
					url='<%= (topLink.equals("message-boards-home") && categoryId == 0 && viewCategory) ? StringPool.BLANK : portletURL.toString() %>'
				/>
			</li>

			<li class="top-link">

				<%
				portletURL.setParameter("topLink", "recent-posts");
				%>

				<liferay-ui:icon
					image="../aui/clock"
					label="<%= true %>"
					message="recent-posts"
					url='<%= topLink.equals("recent-posts") ? StringPool.BLANK : portletURL.toString() %>'
				/>
			</li>

			<c:if test="<%= themeDisplay.isSignedIn() %>">
				<li class="top-link">

					<%
					portletURL.setParameter("topLink", "my-posts");
					%>

					<liferay-ui:icon
						image="../aui/person"
						label="<%= true %>"
						message="my-posts"
						url='<%= topLink.equals("my-posts") ? StringPool.BLANK : portletURL.toString() %>'
					/>
				 </li>

				 <li class="top-link">

					 <%
					portletURL.setParameter("topLink", "my-subscriptions");
					%>

					<liferay-ui:icon
						image="../aui/signal-diag"
						label="<%= true %>"
						message="my-subscriptions"
						url='<%= topLink.equals("my-subscriptions") ? StringPool.BLANK : portletURL.toString() %>'
					/>
				 </li>
			</c:if>

			<li class="top-link <%= MBPermission.contains(permissionChecker, scopeGroupId, ActionKeys.BAN_USER) ? StringPool.BLANK : "last" %>">

				<%
				portletURL.setParameter("topLink", "statistics");
				%>

				<liferay-ui:icon
					image="../aui/clipboard" label="<%= true %>"
					message="statistics"
					url='<%= topLink.equals("statistics") ? StringPool.BLANK : portletURL.toString() %>'
				/>
			</li>

			<c:if test="<%= MBPermission.contains(permissionChecker, scopeGroupId, ActionKeys.BAN_USER) %>">
				<li class="top-link last">

					<%
					portletURL.setParameter("topLink", "banned-users");
					%>

					<liferay-ui:icon
						image="../aui/alert" label="<%= true %>"
						message="banned-users"
						url='<%= topLink.equals("banned-users") ? StringPool.BLANK : portletURL.toString() %>'
					/>
				</li>
			</c:if>
		</ul>

		<c:if test="<%= showSearch %>">
			<liferay-portlet:renderURL varImpl="searchURL">
				<portlet:param name="struts_action" value="/message_boards/search" />
			</liferay-portlet:renderURL>

			<div class="category-search">
				<aui:form action="<%= searchURL %>" method="get" name="searchFm">
					<liferay-portlet:renderURLParams varImpl="searchURL" />
					<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
					<aui:input name="breadcrumbsCategoryId" type="hidden" value="<%= categoryId %>" />
					<aui:input name="searchCategoryId" type="hidden" value="<%= categoryId %>" />

					<span class="aui-search-bar">
						<aui:input id="keywords1" inlineField="<%= true %>" label="" name="keywords" size="30" title="search-messages" type="text" />

						<aui:button type="submit" value="search" />
					</span>
				</aui:form>
			</div>

			<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) && !themeDisplay.isFacebook() %>">
				<aui:script>
					Liferay.Util.focusFormField(document.<portlet:namespace />searchFm.<portlet:namespace />keywords);
				</aui:script>
			</c:if>
		</c:if>
	</div>
</div>