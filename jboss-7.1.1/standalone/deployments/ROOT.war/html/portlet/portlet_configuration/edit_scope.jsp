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

<%@ include file="/html/portlet/portlet_configuration/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");
String returnToFullPageURL = ParamUtil.getString(request, "returnToFullPageURL");

PortletPreferences preferences = portletPreferences;

String scopeType = GetterUtil.getString(preferences.getValue("lfrScopeType", null));
String scopeLayoutUuid = GetterUtil.getString(preferences.getValue("lfrScopeLayoutUuid", null));

Group group = layout.getGroup();
%>

<liferay-util:include page="/html/portlet/portlet_configuration/tabs1.jsp">
	<liferay-util:param name="tabs1" value="scope" />
</liferay-util:include>

<portlet:actionURL var="editScopeURL">
	<portlet:param name="struts_action" value="/portlet_configuration/edit_scope" />
	<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.SAVE %>" />
</portlet:actionURL>

<aui:form action="<%= editScopeURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="returnToFullPageURL" type="hidden" value="<%= returnToFullPageURL %>" />
	<aui:input name="portletResource" type="hidden" value="<%= portletResource %>" />

	<aui:fieldset>
		<aui:select label="scope" name="scopeType">
			<aui:option label="default" selected="<%= Validator.isNull(scopeType) %>" value="" />
			<aui:option label="global" selected='<%= scopeType.equals("company") %>' value="company" />
			<aui:option label="select-layout" selected='<%= scopeType.equals("layout") %>' value="layout" />
		</aui:select>

		<div id="<portlet:namespace />scopeLayoutUuidContainer">
			<aui:select label="scope-layout" name="scopeLayoutUuid">
				<aui:option label='<%= LanguageUtil.get(pageContext,"current-page") + " (" + HtmlUtil.escape(layout.getName(locale)) + ")" %>' selected="<%= scopeLayoutUuid.equals(layout.getUuid()) %>" value="<%= layout.getUuid() %>" />

				<%
				for (Layout curLayout : LayoutLocalServiceUtil.getScopeGroupLayouts(layout.getGroupId(), layout.isPrivateLayout())) {
					if (curLayout.getPlid() == layout.getPlid()) {
						continue;
					}
				%>

					<aui:option label="<%= HtmlUtil.escape(curLayout.getName(locale)) %>" selected="<%= scopeLayoutUuid.equals(curLayout.getUuid()) %>" value="<%= curLayout.getUuid() %>" />

				<%
				}
				%>

			</aui:select>
		</div>
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	Liferay.Util.toggleSelectBox('<portlet:namespace />scopeType', 'layout', '<portlet:namespace />scopeLayoutUuidContainer');
</aui:script>