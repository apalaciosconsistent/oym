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

<%@ include file="/html/taglib/init.jsp" %>

<%
boolean collapsible = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel:collapsible"));
String cssClass = (String)request.getAttribute("liferay-ui:panel:cssClass");
String defaultState = (String)request.getAttribute("liferay-ui:panel:defaultState");
boolean extended = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel:extended"));
String helpMessage = (String)request.getAttribute("liferay-ui:panel:helpMessage");
String id = (String)request.getAttribute("liferay-ui:panel:id");
String parentId = (String)request.getAttribute("liferay-ui:panel:parentId");
boolean persistState = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel:persistState"));
String title = (String)request.getAttribute("liferay-ui:panel:title");

IntegerWrapper panelCount = (IntegerWrapper)request.getAttribute("liferay-ui:panel-container:panelCount" + parentId);

if (panelCount != null) {
	panelCount.increment();

	Boolean panelContainerExtended = (Boolean)request.getAttribute("liferay-ui:panel-container:extended");

	if (panelContainerExtended != null) {
		extended = panelContainerExtended.booleanValue();
	}
}

String panelState = GetterUtil.getString(SessionClicks.get(request, id, null), defaultState);

if (collapsible) {
	cssClass += " lfr-collapsible";
}

if (!panelState.equals("open")) {
	cssClass += " lfr-collapsed";
}

if (extended) {
	cssClass += " lfr-extended";
}
else {
	cssClass += " lfr-panel-basic";
}
%>