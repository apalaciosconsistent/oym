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

<%@ page import="com.liferay.portal.kernel.sanitizer.Sanitizer" %><%@
page import="com.liferay.portal.kernel.sanitizer.SanitizerException" %><%@
page import="com.liferay.portal.kernel.sanitizer.SanitizerUtil" %><%@
page import="com.liferay.portlet.journal.action.EditArticleAction" %><%@
page import="com.liferay.portlet.journal.model.JournalArticle" %><%@
page import="com.liferay.portlet.journal.search.ArticleSearch" %><%@
page import="com.liferay.portlet.journal.search.ArticleSearchTerms" %><%@
page import="com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil" %><%@
page import="com.liferay.portlet.journal.service.JournalArticleServiceUtil" %><%@
page import="com.liferay.portlet.rss.util.RSSUtil" %>

<%@ page import="com.sun.syndication.feed.synd.SyndContent" %><%@
page import="com.sun.syndication.feed.synd.SyndEnclosure" %><%@
page import="com.sun.syndication.feed.synd.SyndEntry" %><%@
page import="com.sun.syndication.feed.synd.SyndFeed" %><%@
page import="com.sun.syndication.feed.synd.SyndImage" %>

<%
PortletPreferences preferences = portletPreferences;

String portletResource = ParamUtil.getString(request, "portletResource");

String[] urls = preferences.getValues("urls", new String[0]);
String[] titles = preferences.getValues("titles", new String[0]);
int entriesPerFeed = GetterUtil.getInteger(preferences.getValue("entriesPerFeed", "8"));
int expandedEntriesPerFeed = GetterUtil.getInteger(preferences.getValue("expandedEntriesPerFeed", "1"));
boolean showFeedTitle = GetterUtil.getBoolean(preferences.getValue("showFeedTitle", Boolean.TRUE.toString()));
boolean showFeedPublishedDate = GetterUtil.getBoolean(preferences.getValue("showFeedPublishedDate", Boolean.TRUE.toString()));
boolean showFeedDescription = GetterUtil.getBoolean(preferences.getValue("showFeedDescription", Boolean.TRUE.toString()));
boolean showFeedImage = GetterUtil.getBoolean(preferences.getValue("showFeedImage", Boolean.TRUE.toString()));
String feedImageAlignment = preferences.getValue("feedImageAlignment", "right");
boolean showFeedItemAuthor = GetterUtil.getBoolean(preferences.getValue("showFeedItemAuthor", Boolean.TRUE.toString()));

String[] headerArticleValues = preferences.getValues("headerArticleValues", new String[] {"0", ""});

long headerArticleGroupId = GetterUtil.getLong(headerArticleValues[0]);
String headerArticleId = headerArticleValues[1];

String[] footerArticleValues = preferences.getValues("footerArticleValues", new String[] {"0", ""});

long footerArticleGroupId = GetterUtil.getLong(footerArticleValues[0]);
String footerArticleId = footerArticleValues[1];

Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
%>

<%@ include file="/html/portlet/rss/init-ext.jsp" %>