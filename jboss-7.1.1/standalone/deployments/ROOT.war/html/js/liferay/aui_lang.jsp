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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ page import="com.liferay.portal.kernel.servlet.HttpHeaders" %>
<%@ page import="com.liferay.portal.kernel.util.CalendarUtil" %>
<%@ page import="com.liferay.portal.kernel.util.ContentTypes" %>
<%@ page import="com.liferay.portal.kernel.util.DateUtil" %>
<%@ page import="com.liferay.portal.kernel.util.LocaleUtil" %>
<%@ page import="com.liferay.util.JS" %>

<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.Locale" %>

<%
response.addHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.TEXT_JAVASCRIPT);

String languageId = LanguageUtil.getLanguageId(request);

Locale locale = LocaleUtil.fromLanguageId(languageId);

String dateFormatPattern = ((SimpleDateFormat)(DateFormat.getDateInstance(DateFormat.SHORT, locale))).toPattern();

String dateFormatOrder = _DATE_FORMAT_ORDER_MDY;

if (dateFormatPattern.indexOf("y") == 0) {
	dateFormatOrder = _DATE_FORMAT_ORDER_YMD;
}
else if (dateFormatPattern.indexOf("d") == 0) {
	dateFormatOrder = _DATE_FORMAT_ORDER_DMY;
}

Date selectedDate = new Date();

Calendar cal = new GregorianCalendar();

cal.setTime(selectedDate);
%>

AUI.add(
	'portal-aui-lang',
	function(A) {
		A.DataType.Date.Locale['<%= languageId %>'] = A.merge(
			A.DataType.Date.Locale['<%= languageId %>'], {
				a: <%= JS.toScript(CalendarUtil.getDays(locale, "EEE")) %>,
				A: <%= JS.toScript(CalendarUtil.getDays(locale)) %>,
				b: <%= JS.toScript(CalendarUtil.getMonths(locale, "MMM")) %>,
				B: <%= JS.toScript(CalendarUtil.getMonths(locale)) %>,

				<c:choose>
					<c:when test="<%= dateFormatOrder.equals(_DATE_FORMAT_ORDER_MDY) %>">
						c: '%d %b %a %Y %T %Z',
						x: '%m/%d/%y',
					</c:when>
					<c:when test="<%= dateFormatOrder.equals(_DATE_FORMAT_ORDER_YMD) %>">
						c: '%Y %d %b %a %T %Z',
						x: '%y/%m/%d',
					</c:when>
					<c:otherwise>
						c: '%a %d %b %Y %T %Z',
						x: '%d/%m/%y',
					</c:otherwise>
				</c:choose>

				<c:choose>
				 	<c:when test="<%= DateUtil.isFormatAmPm(locale) %>">
						p: ['AM', 'PM'],
						P: ['am', 'pm'],
						r: '%I:%M:%S %p',
					</c:when>
					<c:otherwise>
						r: '%H:%M:%S',
					</c:otherwise>
				</c:choose>

				X: '%T'
			}
		);
	},
	'',
	{
		requires: ['aui-calendar']
	}
);

<%!
private static final String _DATE_FORMAT_ORDER_DMY = "[\\'d\\', \\'m\\', \\'y\\']";

private static final String _DATE_FORMAT_ORDER_MDY = "[\\'m\\', \\'d\\', \\'y\\']";

private static final String _DATE_FORMAT_ORDER_YMD = "[\\'y\\', \\'m\\', \\'d\\']";
%>