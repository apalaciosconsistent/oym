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
String id = (String)request.getAttribute("liferay-ui:icon:id");

IntegerWrapper iconListIconCount = (IntegerWrapper)request.getAttribute("liferay-ui:icon-list:icon-count");

if (iconListIconCount != null) {
	iconListIconCount.increment();
}

boolean iconListShowWhenSingleIcon = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon-list:showWhenSingleIcon"));

Boolean iconListSingleIcon = (Boolean)request.getAttribute("liferay-ui:icon-list:single-icon");

IntegerWrapper iconMenuIconCount = (IntegerWrapper)request.getAttribute("liferay-ui:icon-menu:icon-count");

if (iconMenuIconCount != null) {
	iconMenuIconCount.increment();
}

Boolean iconMenuSingleIcon = (Boolean)request.getAttribute("liferay-ui:icon-menu:single-icon");

boolean iconMenuShowWhenSingleIcon = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon-menu:showWhenSingleIcon"));

String image = (String)request.getAttribute("liferay-ui:icon:image");
String imageHover = (String)request.getAttribute("liferay-ui:icon:imageHover");

boolean auiImage = (image != null) && image.startsWith(_AUI_PATH);

String alt = (String)request.getAttribute("liferay-ui:icon:alt");

String message = (String)request.getAttribute("liferay-ui:icon:message");

if (message == null) {
	message = StringUtil.replace(image, StringPool.UNDERLINE, StringPool.DASH);
}

String src = (String)request.getAttribute("liferay-ui:icon:src");
String srcHover = (String)request.getAttribute("liferay-ui:icon:srcHover");

if (Validator.isNull(src)) {
	if (auiImage) {
		src = themeDisplay.getPathThemeImages().concat("/spacer.png");
	}
	else if (Validator.isNotNull(image)) {
		StringBundler sb = new StringBundler(4);

		sb.append(themeDisplay.getPathThemeImages());
		sb.append("/common/");
		sb.append(image);
		sb.append(".png");

		src = StringUtil.replace(sb.toString(), "common/../", "");
	}
}

if (Validator.isNull(srcHover) && Validator.isNotNull(imageHover)) {
	StringBundler sb = new StringBundler(4);

	sb.append(themeDisplay.getPathThemeImages());
	sb.append("/common/");
	sb.append(imageHover);
	sb.append(".png");

	srcHover = sb.toString();
}

String url = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:url"));

String method = (String)request.getAttribute("liferay-ui:icon:method");

if (Validator.isNull(method)) {
	method = "post";
}

String target = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:target"));
boolean label = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:label"));
String lang = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:lang"));
boolean localizeMessage = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:localizeMessage"));
boolean toolTip = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:icon:toolTip"));
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:cssClass"));
Map<String, Object> data = (Map<String, Object>)request.getAttribute("liferay-ui:icon:data");
String onClick = GetterUtil.getString((String)request.getAttribute("liferay-ui:icon:onClick"));

if ((iconListIconCount != null) || (iconListSingleIcon != null)) {
	label = true;
}

if ((iconMenuIconCount != null) || (iconMenuSingleIcon != null)) {
	label = true;
}

String details = null;

if (alt != null) {
	details = " alt=\"" + LanguageUtil.get(pageContext, alt) + "\"";
}
else if (label) {
	details = " alt=\"\"";
}
else {
	StringBundler sb = new StringBundler(6);

	sb.append(" alt=\"");
	sb.append(LanguageUtil.get(pageContext, message));
	sb.append("\"");

	if (toolTip) {
		sb.append(" onmouseover=\"Liferay.Portal.ToolTip.show(this, '");
		sb.append(UnicodeLanguageUtil.get(pageContext, message));
		sb.append("')\"");
	}
	else {
		sb.append(" title=\"");
		sb.append(LanguageUtil.get(pageContext, message));
		sb.append("\"");
	}

	details = sb.toString();
}
%>

<%!
private static final String _AUI_PATH = "../aui/";
%>