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

<%@ include file="/html/portlet/breadcrumb/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
%>

<aui:layout>
	<aui:column columnWidth="50">
		<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

		<aui:form action="<%= configurationURL %>" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

			<aui:fieldset>
				<aui:select name="preferences--displayStyle--">

					<%
					for (String displayStyleOption : PropsValues.BREADCRUMB_DISPLAY_STYLE_OPTIONS) {
					%>

						<aui:option label="<%= displayStyleOption %>" selected="<%= displayStyle.equals(displayStyleOption) %>" />

					<%
					}
					%>

				</aui:select>
			</aui:fieldset>

			<aui:fieldset cssClass="checkBoxes">
				<aui:column columnWidth="50">
					<aui:input label="show-current-site" name="preferences--showCurrentGroup--" type="checkbox" value="<%= showCurrentGroup %>" />
					<aui:input label="show-current-application" name="preferences--showCurrentPortlet--" type="checkbox" value="<%= showCurrentPortlet %>" />
					<aui:input label="show-guest-site" name="preferences--showGuestGroup--" type="checkbox" value="<%= showGuestGroup %>" />
				</aui:column>

				<aui:column columnWidth="50">
					<aui:input label="show-page" name="preferences--showLayout--" type="checkbox" value="<%= showLayout %>" />
					<aui:input label="show-parent-sites" name="preferences--showParentGroups--" type="checkbox" value="<%= showParentGroups %>" />
					<aui:input label="show-application-breadcrumb" name="preferences--showPortletBreadcrumb--" type="checkbox" value="<%= showPortletBreadcrumb %>" />
				</aui:column>
			</aui:fieldset>

			<aui:button-row>
				<aui:button type="submit" />
			</aui:button-row>
		</aui:form>
	</aui:column>
	<aui:column columnWidth="50">
		<liferay-portlet:preview
			portletName="<%= portletResource %>"
			queryString="struts_action=/breadcrumb/view"
			showBorders="<%= true %>"
		/>
	</aui:column>
</aui:layout>

<aui:script use="aui-base">
	var formNode = A.one('#<portlet:namespace />fm');

	var selectDisplayStyle = formNode.one('#<portlet:namespace />displayStyle');

	var toggleCustomFields = function() {
		var data = {
			'_<%= HtmlUtil.escapeJS(portletResource) %>_displayStyle': selectDisplayStyle.val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showCurrentGroup': formNode.one('#<portlet:namespace />showCurrentGroup').val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showCurrentPortlet': formNode.one('#<portlet:namespace />showCurrentPortlet').val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showGuestGroup': formNode.one('#<portlet:namespace />showGuestGroup').val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showLayout': formNode.one('#<portlet:namespace />showLayout').val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showParentGroups': formNode.one('#<portlet:namespace />showParentGroups').val(),
			'_<%= HtmlUtil.escapeJS(portletResource) %>_showPortletBreadcrumb': formNode.one('#<portlet:namespace />showPortletBreadcrumb').val()
		};

		Liferay.Portlet.refresh('#p_p_id_<%= HtmlUtil.escapeJS(portletResource) %>_', data);
	};

	if (selectDisplayStyle) {
		selectDisplayStyle.on('change', toggleCustomFields);

		toggleCustomFields();
	}

	A.one('.checkBoxes').delegate('change', toggleCustomFields, '.aui-field-input-choice');
</aui:script>