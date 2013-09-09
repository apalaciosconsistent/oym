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

<%@ include file="/html/portal/init.jsp" %>

<%
Portlet portlet = (Portlet)request.getAttribute(WebKeys.RENDER_PORTLET);

String portletId = portlet.getPortletId();
%>

<c:choose>
	<c:when test="<%= portlet.getRenderWeight() >= 1 %>">
		[$TEMPLATE_PORTLET_<%= portletId %>$]
	</c:when>
	<c:otherwise>

		<%
		portletDisplay.setId(portletId);
		portletDisplay.setNamespace(PortalUtil.getPortletNamespace(portletId));

		String url = PortletURLUtil.getRefreshURL(request, themeDisplay);
		%>

		<div class="loading-animation" id="p_load<%= portletDisplay.getNamespace() %>"></div>

		<aui:script use="aui-base">
			var ns = '<%= portletDisplay.getNamespace() %>';

			Liferay.Portlet.addHTML(
				{
					onComplete: function(portlet, portletId) {
						portlet.refreshURL = '<%= HtmlUtil.escapeJS(url) %>';
					},
					placeHolder: A.one('#p_load' + ns),
					url: '<%= HtmlUtil.escapeJS(url) %>'
				}
			);
		</aui:script>
	</c:otherwise>
</c:choose>