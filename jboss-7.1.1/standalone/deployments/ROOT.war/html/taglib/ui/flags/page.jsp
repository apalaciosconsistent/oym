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

<%@ include file="/html/taglib/init.jsp" %>

<%
String randomNamespace = PortalUtil.generateRandomKey(request, "taglib_ui_flags_page") + StringPool.UNDERLINE;

String className = (String)request.getAttribute("liferay-ui:flags:className");
long classPK = GetterUtil.getLong((String)request.getAttribute("liferay-ui:flags:classPK"));
String contentTitle = GetterUtil.getString((String)request.getAttribute("liferay-ui:flags:contentTitle"));
boolean label = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:flags:label"), true);
String message = GetterUtil.getString((String)request.getAttribute("liferay-ui:flags:message"), "flag[action]");
long reportedUserId = GetterUtil.getLong((String)request.getAttribute("liferay-ui:flags:reportedUserId"));
%>

<div class="taglib-flags" title="<liferay-ui:message key="<%= message %>" />">
	<liferay-ui:icon
		cssClass="<%= randomNamespace %>"
		image="../ratings/flagged_icon"
		imageHover="../ratings/flagged_icon_hover"
		label="<%= label %>"
		message="<%= message %>"
		url="javascript:;"
	/>
</div>

<c:choose>
	<c:when test="<%= PropsValues.FLAGS_GUEST_USERS_ENABLED || themeDisplay.isSignedIn() %>">
		<aui:script use="aui-dialog">
			var icon = A.one('.<%= randomNamespace %>');

			if (icon) {
				icon.on(
					'click',
					function() {
						var popup = new A.Dialog(
							{
								align: Liferay.Util.Window.ALIGN_CENTER,
								destroyOnClose: true,
								draggable: true,
								modal: true,
								stack: true,
								title: '<%= UnicodeLanguageUtil.get(pageContext, "report-inappropriate-content") %>',
								width: 435
							}
						).render();

						popup.plug(
							A.Plugin.IO, {
								data: {
									className: '<%= className %>',
									classPK: '<%= classPK %>',
									contentTitle: '<%= HtmlUtil.escapeJS(contentTitle) %>',
									contentURL: '<%= HtmlUtil.escapeJS(PortalUtil.getPortalURL(request) + currentURL) %>',
									reportedUserId: '<%= reportedUserId %>'
								},
								uri: '<liferay-portlet:renderURL portletName="<%= PortletKeys.FLAGS %>" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>"><portlet:param name="struts_action" value="/flags/edit_entry" /></liferay-portlet:renderURL>'
							}
						);
					}
				);
			}
		</aui:script>
	</c:when>
	<c:otherwise>
		<div id="<%= randomNamespace %>signIn" style="display:none">
			<liferay-ui:message key="please-sign-in-to-flag-this-as-inappropriate" />
		</div>

		<aui:script use="aui-dialog">
			var icon = A.one('.<%= randomNamespace %>');

			if (icon) {
				icon.on(
					'click',
					function(event) {
						var popup = new A.Dialog(
							{
								align: Liferay.Util.Window.ALIGN_CENTER,
								bodyContent: A.one('#<%= randomNamespace %>signIn').html(),
								destroyOnClose: true,
								title: '<%= UnicodeLanguageUtil.get(pageContext, "report-inappropriate-content") %>',
								modal: true,
								width: 500
							}
						).render();

						event.preventDefault();
					}
				);
			}
		</aui:script>
	</c:otherwise>
</c:choose>