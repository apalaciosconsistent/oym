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

<%@ include file="/html/portlet/message_boards/init.jsp" %>

<%
MBMessage message = (MBMessage)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE);

String body = StringPool.BLANK;

if (message.isFormatBBCode()) {
	body = BBCodeTranslatorUtil.getHTML(message.getBody());
	body = StringUtil.replace(body, "@theme_images_path@/emoticons", themeDisplay.getPathThemeImages() + "/emoticons");
}
else {
	body = message.getBody();
}
%>

<%= body %>

<liferay-ui:custom-attributes-available className="<%= MBMessage.class.getName() %>">
	<liferay-ui:custom-attribute-list
		className="<%= MBMessage.class.getName() %>"
		classPK="<%= (message != null) ? message.getMessageId() : 0 %>"
		editable="<%= false %>"
		label="<%= true %>"
	/>
</liferay-ui:custom-attributes-available>