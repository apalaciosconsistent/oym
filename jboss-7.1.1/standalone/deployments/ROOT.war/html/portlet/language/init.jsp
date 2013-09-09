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

<%@ page import="com.liferay.taglib.ui.LanguageTag" %>

<%
PortletPreferences preferences = portletPreferences;

Locale[] availableLocales = LanguageUtil.getAvailableLocales();
String[] availableLanguageIds = LocaleUtil.toLanguageIds(availableLocales);

String[] languageIds = StringUtil.split(preferences.getValue("languageIds", StringUtil.merge(availableLanguageIds)));
int displayStyle = GetterUtil.getInteger(preferences.getValue("displayStyle", StringPool.BLANK));
%>

<%@ include file="/html/portlet/language/init-ext.jsp" %>