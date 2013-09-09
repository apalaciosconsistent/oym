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
String bulletStyle = ((String)request.getAttribute("liferay-ui:navigation:bulletStyle")).toLowerCase();
String displayStyle = (String)request.getAttribute("liferay-ui:navigation:displayStyle");

String headerType = null;
String rootLayoutType = null;
int rootLayoutLevel = 0;
String includedLayouts = null;
boolean nestedChildren = true;

String[] displayStyleDefinition = _getDisplayStyleDefinition(displayStyle);

if ((displayStyleDefinition != null) && (displayStyleDefinition.length != 0)) {
	headerType = displayStyleDefinition[0];
	rootLayoutType = displayStyleDefinition[1];
	rootLayoutLevel = GetterUtil.getInteger(displayStyleDefinition[2]);
	includedLayouts = displayStyleDefinition[3];

	if (displayStyleDefinition.length > 4) {
		nestedChildren = GetterUtil.getBoolean(displayStyleDefinition[4]);
	}
}
else {
	headerType = (String)request.getAttribute("liferay-ui:navigation:headerType");
	rootLayoutType = (String)request.getAttribute("liferay-ui:navigation:rootLayoutType");
	rootLayoutLevel = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:navigation:rootLayoutLevel"));
	includedLayouts = (String)request.getAttribute("liferay-ui:navigation:includedLayouts");
	nestedChildren = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:navigation:nestedChildren"));
}
%>

<%!
private String[] _getDisplayStyleDefinition(String displayStyle) {
	return PropsUtil.getArray("navigation.display.style", new Filter(displayStyle));
}
%>