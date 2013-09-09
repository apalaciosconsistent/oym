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

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.marketplace.util.MarketplaceConstants" %><%@
page import="com.liferay.marketplace.util.PortletKeys" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.ServerDetector" %><%@
page import="com.liferay.portal.kernel.util.StringPool" %>

<%@ page import="javax.portlet.WindowState" %>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
long appId = ParamUtil.getLong(request, "appId");

String portletId = portletDisplay.getId();

String iFrameURL = MarketplaceConstants.MARKETPLACE_URL_LOGOUT;

String referer = StringPool.BLANK;

if (portletId.equals(PortletKeys.MY_MARKETPLACE)) {
	referer = MarketplaceConstants.getPathPurchased();
}
else if (portletId.equals(PortletKeys.STORE) && (appId > 0)) {
	referer = MarketplaceConstants.getPathStore() + "/application/" + appId;
}
else {
	referer = MarketplaceConstants.getPathStore();
}
%>