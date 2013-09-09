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
Layout selLayout = (Layout)request.getAttribute("edit_pages.jsp-selLayout");

LayoutTypePortlet selLayoutTypePortlet = null;

Theme selTheme = null;

if (selLayout != null) {
	selLayoutTypePortlet = (LayoutTypePortlet)selLayout.getLayoutType();

	selTheme = selLayout.getTheme();
}

String layoutTemplateId = StringPool.BLANK;

if (selLayoutTypePortlet != null) {
	layoutTemplateId = selLayoutTypePortlet.getLayoutTemplateId();
}

List<LayoutTemplate> layoutTemplates = LayoutTemplateLocalServiceUtil.getLayoutTemplates(selTheme.getThemeId());
%>

<liferay-ui:error-marker key="errorSection" value="layout" />

<aui:model-context bean="<%= selLayout %>" model="<%= Layout.class %>" />

<h3><liferay-ui:message key="layout" /></h3>

<%@ include file="/html/portlet/layouts_admin/layout/layout_templates.jspf" %>