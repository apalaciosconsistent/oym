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

<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>

<%
String cssClasses = ParamUtil.getString(request, "cssClasses");
String editorImpl = ParamUtil.getString(request, "editorImpl");
String initMethod = ParamUtil.getString(request, "initMethod");
String name = ParamUtil.getString(request, "name");
String onChangeMethod = ParamUtil.getString(request, "onChangeMethod");
boolean skipEditorLoading = ParamUtil.getBoolean(request, "skipEditorLoading");
String toolbarSet = ParamUtil.getString(request, "toolbarSet");
%>

<liferay-ui:input-editor
	cssClass="<%= cssClasses %>"
	editorImpl="<%= editorImpl %>"
	initMethod="<%= initMethod %>"
	name="<%= name %>"
	onChangeMethod="<%= onChangeMethod %>"
	skipEditorLoading="<%= skipEditorLoading %>"
	toolbarSet="<%= toolbarSet %>"
/>