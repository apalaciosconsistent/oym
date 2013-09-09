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

<%@ include file="/html/taglib/ui/panel/init.jsp" %>

<div class="lfr-panel <%= cssClass %>" id="<%= id %>">
	<div class="lfr-panel-titlebar">
		<div class="lfr-panel-title">
			<span>
				<liferay-ui:message key="<%= title %>" />
			</span>

			<c:if test="<%= Validator.isNotNull(helpMessage) %>">
				<liferay-ui:icon-help message="<%= helpMessage %>" />
			</c:if>
		</div>

		<c:if test="<%= collapsible && extended %>">
			<a class="lfr-panel-button" href="javascript:;" title="<liferay-ui:message key='<%= panelState.equals("open") ? "collapse" : "expand" %>' />"></a>
		</c:if>
	</div>

	<div class="lfr-panel-content">