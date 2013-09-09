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

<%@ page import="com.liferay.portal.kernel.search.Document" %><%@
page import="com.liferay.portal.kernel.search.Hits" %><%@
page import="com.liferay.portal.kernel.search.Indexer" %><%@
page import="com.liferay.portal.kernel.search.IndexerRegistryUtil" %><%@
page import="com.liferay.portal.kernel.search.SearchContext" %><%@
page import="com.liferay.portal.kernel.search.SearchContextFactory" %><%@
page import="com.liferay.portlet.journal.model.JournalArticle" %><%@
page import="com.liferay.portlet.journal.model.JournalArticleConstants" %><%@
page import="com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil" %><%@
page import="com.liferay.portlet.journalcontentsearch.util.ContentHits" %>

<%
PortletPreferences preferences = portletPreferences;

boolean showListedDefault = true;

if (portletName.equals(PortletKeys.JOURNAL_CONTENT_SEARCH)) {
	showListedDefault = PropsValues.JOURNAL_CONTENT_SEARCH_SHOW_LISTED;
}

boolean showListed = PrefsParamUtil.getBoolean(preferences, request, "showListed", showListedDefault);

String targetPortletId = StringPool.BLANK;

if (!showListed) {
	targetPortletId = PrefsParamUtil.getString(preferences, request, "targetPortletId", StringPool.BLANK);
}

String type = PrefsParamUtil.getString(preferences, request, "type", StringPool.BLANK);
%>

<%@ include file="/html/portlet/journal_content_search/init-ext.jsp" %>