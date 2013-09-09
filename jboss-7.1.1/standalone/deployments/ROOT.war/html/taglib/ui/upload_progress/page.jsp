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
String id = (String)request.getAttribute("liferay-ui:upload-progress:id");
String iframeSrc = (String)request.getAttribute("liferay-ui:upload-progress:iframe-src");
String redirect = (String)request.getAttribute("liferay-ui:upload-progress:redirect");
String message = (String)request.getAttribute("liferay-ui:upload-progress:message");
%>

<c:if test="<%= Validator.isNotNull(iframeSrc) %>">
	<div><iframe frameborder="0" id="<%= id %>-iframe" src="<%= iframeSrc %>" style="width: 100%;"></iframe></div>
</c:if>

<div><iframe frameborder="0" id="<%= id %>-poller" src="" style="height: 0; width: 0;"></iframe></div>

<div id="<%= id %>-bar-div" style="display: none; text-align: center;">
	<br />

	<c:if test="<%= Validator.isNotNull(message) %>">
		<%= LanguageUtil.get(pageContext, message) %>...<br />
	</c:if>

	<div style="background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/incomplete_middle.png) scroll repeat-x top left; margin: auto; text-align: left; width: 80%;">
		<div style="background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/incomplete_left.png) scroll no-repeat top left;">
			<div style="height: 23px; background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/incomplete_right.png) scroll no-repeat top right;">
				<div id="<%= id %>-bar" style="background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/complete_middle.png) scroll repeat-x top left; overflow: hidden; width: 0;">
					<div style="background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/complete_left.png) scroll no-repeat top left;">
						<div class="font-small" style="font-weight: bold; height: 23px; padding-top: 3px; text-align: center; background: url(<%= themeDisplay.getPathThemeImages() %>/progress_bar/complete_right.png) scroll no-repeat top right;">
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="<%= themeDisplay.getCDNHost() + themeDisplay.getPathJavaScript() %>/liferay/upload_progress.js" type="text/javascript"></script>

<aui:script>
	var <%= id %> = new UploadProgress("<%= id %>", "<%= HttpUtil.encodeURL(redirect) %>");
</aui:script>