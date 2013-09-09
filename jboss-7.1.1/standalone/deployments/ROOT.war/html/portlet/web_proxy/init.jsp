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

<%@ include file="/html/portlet/init.jsp" %>

<%
PortletPreferences preferences = portletPreferences;

String initUrl = preferences.getValue("initUrl", StringPool.BLANK);
String scope = preferences.getValue("scope", StringPool.BLANK);
String proxyHost = preferences.getValue("proxyHost", StringPool.BLANK);
String proxyPort = preferences.getValue("proxyPort", StringPool.BLANK);
String proxyAuthentication = preferences.getValue("proxyAuthentication", StringPool.BLANK);
String proxyAuthenticationUsername = preferences.getValue("proxyAuthenticationUsername", StringPool.BLANK);
String proxyAuthenticationPassword = preferences.getValue("proxyAuthenticationPassword", StringPool.BLANK);
String proxyAuthenticationHost = preferences.getValue("proxyAuthenticationHost", StringPool.BLANK);
String proxyAuthenticationDomain = preferences.getValue("proxyAuthenticationDomain", StringPool.BLANK);
String stylesheet = preferences.getValue("stylesheet", StringPool.BLANK);
%>

<%@ include file="/html/portlet/web_proxy/init-ext.jsp" %>