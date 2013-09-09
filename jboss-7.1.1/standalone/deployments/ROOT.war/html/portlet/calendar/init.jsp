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

<%@ page import="com.liferay.portal.kernel.cal.DayAndPosition" %><%@
page import="com.liferay.portlet.asset.model.AssetEntry" %><%@
page import="com.liferay.portlet.asset.model.AssetTag" %><%@
page import="com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil" %><%@
page import="com.liferay.portlet.asset.service.AssetTagLocalServiceUtil" %><%@
page import="com.liferay.portlet.calendar.EventDurationException" %><%@
page import="com.liferay.portlet.calendar.EventEndDateException" %><%@
page import="com.liferay.portlet.calendar.EventStartDateException" %><%@
page import="com.liferay.portlet.calendar.EventTitleException" %><%@
page import="com.liferay.portlet.calendar.ImportEventsException" %><%@
page import="com.liferay.portlet.calendar.NoSuchEventException" %><%@
page import="com.liferay.portlet.calendar.model.CalEvent" %><%@
page import="com.liferay.portlet.calendar.model.CalEventConstants" %><%@
page import="com.liferay.portlet.calendar.service.CalEventServiceUtil" %><%@
page import="com.liferay.portlet.calendar.service.permission.CalEventPermission" %><%@
page import="com.liferay.portlet.calendar.service.permission.CalendarPermission" %><%@
page import="com.liferay.portlet.calendar.util.CalUtil" %><%@
page import="com.liferay.portlet.calendar.util.comparator.EventTimeComparator" %>

<%
PortletPreferences preferences = portletPreferences;

String tabs1Names = "summary,day,week,month,year,events";

if (CalendarPermission.contains(permissionChecker, scopeGroupId, ActionKeys.EXPORT_ALL_EVENTS) || CalendarPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_EVENT)) {
	tabs1Names += ",export-import";
}

String[] tabs1NamesArray = StringUtil.split(tabs1Names);

String tabs1Default = preferences.getValue("tabs1Default", tabs1NamesArray[0]);

String summaryTabOrientation = preferences.getValue("summaryTabOrientation", "horizontal");
boolean summaryTabShowMiniMonth = GetterUtil.getBoolean(preferences.getValue("summaryTabShowMiniMonth", "true"));
boolean summaryTabShowTodaysEvents = GetterUtil.getBoolean(preferences.getValue("summaryTabShowTodaysEvents", "true"));
boolean enableRelatedAssets = GetterUtil.getBoolean(preferences.getValue("enableRelatedAssets", null), true);
boolean enableRatings = PropsValues.CALENDAR_EVENT_RATINGS_ENABLED && GetterUtil.getBoolean(preferences.getValue("enableRatings", null), true);
boolean enableComments = PropsValues.CALENDAR_EVENT_COMMENTS_ENABLED && GetterUtil.getBoolean(preferences.getValue("enableComments", null), true);

String monthParam = request.getParameter("month");
String dayParam = request.getParameter("day");
String yearParam = request.getParameter("year");

Calendar selCal = CalendarFactoryUtil.getCalendar(timeZone, locale);

try {
	selCal.set(Calendar.YEAR, Integer.parseInt(yearParam));
}
catch (NumberFormatException nfe) {
}

try {
	if (dayParam != null) {
		selCal.set(Calendar.DATE, 1);
	}

	selCal.set(Calendar.MONTH, Integer.parseInt(monthParam));
}
catch (NumberFormatException nfe) {
}

try {
	int maxDayOfMonth = selCal.getActualMaximum(Calendar.DATE);

	int dayParamInt = Integer.parseInt(dayParam);

	if (dayParamInt > maxDayOfMonth) {
		dayParamInt = maxDayOfMonth;
	}

	selCal.set(Calendar.DATE, dayParamInt);
}
catch (NumberFormatException nfe) {
}

int selMonth = selCal.get(Calendar.MONTH);
int selDay = selCal.get(Calendar.DATE);
int selYear = selCal.get(Calendar.YEAR);

Calendar curCal = CalendarFactoryUtil.getCalendar(timeZone, locale);

int curMonth = curCal.get(Calendar.MONTH);
int curDay = curCal.get(Calendar.DATE);
int curYear = curCal.get(Calendar.YEAR);

String[] months = CalendarUtil.getMonths(locale);

String[] days = CalendarUtil.getDays(locale);

Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale);
Format dateFormatTime = FastDateFormatFactoryUtil.getTime(locale);
DateFormat dateFormatISO8601 = DateUtil.getISO8601Format();
%>

<%@ include file="/html/portlet/calendar/init-ext.jsp" %>