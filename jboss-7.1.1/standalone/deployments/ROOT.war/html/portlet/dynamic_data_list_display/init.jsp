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

<%@ page import="com.liferay.portlet.dynamicdatalists.NoSuchRecordSetException" %><%@
page import="com.liferay.portlet.dynamicdatalists.model.DDLRecordSet" %><%@
page import="com.liferay.portlet.dynamicdatalists.model.DDLRecordSetConstants" %><%@
page import="com.liferay.portlet.dynamicdatalists.search.RecordSetSearch" %><%@
page import="com.liferay.portlet.dynamicdatalists.search.RecordSetSearchTerms" %><%@
page import="com.liferay.portlet.dynamicdatalists.service.DDLRecordSetLocalServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatalists.service.permission.DDLPermission" %><%@
page import="com.liferay.portlet.dynamicdatalists.util.DDLUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMTemplate" %><%@
page import="com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.permission.DDMPermission" %><%@
page import="com.liferay.portlet.dynamicdatamapping.service.permission.DDMTemplatePermission" %>

<%
PortletPreferences preferences = portletPreferences;

String portletResource = ParamUtil.getString(request, "portletResource");

long recordSetId = GetterUtil.getLong(preferences.getValue("recordSetId", StringPool.BLANK));

long detailDDMTemplateId = GetterUtil.getLong(preferences.getValue("detailDDMTemplateId", StringPool.BLANK));
long listDDMTemplateId = GetterUtil.getLong(preferences.getValue("listDDMTemplateId", StringPool.BLANK));

boolean editable = DDLUtil.isEditable(preferences, portletDisplay.getId(), themeDisplay.getScopeGroupId());

boolean spreadsheet = GetterUtil.getBoolean(preferences.getValue("spreadsheet", Boolean.FALSE.toString()));

String ddmResource = portletConfig.getInitParameter("ddm-resource");

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
%>

<%@ include file="/html/portlet/dynamic_data_list_display/init-ext.jsp" %>