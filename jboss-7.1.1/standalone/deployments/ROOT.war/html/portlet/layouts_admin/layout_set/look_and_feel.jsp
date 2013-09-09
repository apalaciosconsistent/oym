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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%
long groupId = ((Long)request.getAttribute("edit_pages.jsp-groupId")).longValue();
long liveGroupId = ((Long)request.getAttribute("edit_pages.jsp-liveGroupId")).longValue();
boolean privateLayout = ((Boolean)request.getAttribute("edit_pages.jsp-privateLayout")).booleanValue();
LayoutSet layoutSet = ((LayoutSet)request.getAttribute("edit_pages.jsp-selLayoutSet"));

Theme selTheme = layoutSet.getTheme();
ColorScheme selColorScheme = layoutSet.getColorScheme();

Theme selWapTheme = layoutSet.getWapTheme();
ColorScheme selWapColorScheme = layoutSet.getWapColorScheme();
%>

<liferay-ui:error-marker key="errorSection" value="look-and-feel" />

<aui:model-context bean="<%= layoutSet %>" model="<%= Layout.class %>" />

<h3><liferay-ui:message key="look-and-feel" /></h3>

<aui:fieldset>
	<aui:input name="devices" type="hidden" value="regular,wap" />

	<liferay-ui:tabs
		names="regular-browsers,mobile-devices"
		refresh="<%= false %>"
	>
		<liferay-ui:section>

			<%
			List<Theme> themes = ThemeLocalServiceUtil.getThemes(company.getCompanyId(), liveGroupId, user.getUserId(), false);
			List<ColorScheme> colorSchemes = selTheme.getColorSchemes();

			request.setAttribute("edit_pages.jsp-themes", themes);
			request.setAttribute("edit_pages.jsp-colorSchemes", colorSchemes);
			request.setAttribute("edit_pages.jsp-selTheme", selTheme);
			request.setAttribute("edit_pages.jsp-selColorScheme", selColorScheme);
			request.setAttribute("edit_pages.jsp-device", "regular");
			request.setAttribute("edit_pages.jsp-editable", true);
			%>

			<liferay-util:include page="/html/portlet/layouts_admin/look_and_feel_themes.jsp" />

			<h3><liferay-ui:message key="css" /></h3>

			<aui:input label="insert-custom-css-that-will-be-loaded-after-the-theme" name="regularCss" type="textarea" value="<%= layoutSet.getCss() %>">
				<%@ include file="/html/taglib/aui/input/validator_max_length_oracle.jspf" %>
			</aui:input>
		</liferay-ui:section>

		<liferay-ui:section>

			<%
			List<Theme> themes = ThemeLocalServiceUtil.getThemes(company.getCompanyId(), liveGroupId, user.getUserId(), true);
			List<ColorScheme> colorSchemes = selWapTheme.getColorSchemes();

			request.setAttribute("edit_pages.jsp-themes", themes);
			request.setAttribute("edit_pages.jsp-colorSchemes", colorSchemes);
			request.setAttribute("edit_pages.jsp-selTheme", selWapTheme);
			request.setAttribute("edit_pages.jsp-selColorScheme", selWapColorScheme);
			request.setAttribute("edit_pages.jsp-device", "wap");
			request.setAttribute("edit_pages.jsp-editable", true);
			%>

			<liferay-util:include page="/html/portlet/layouts_admin/look_and_feel_themes.jsp" />
		</liferay-ui:section>
	</liferay-ui:tabs>
</aui:fieldset>