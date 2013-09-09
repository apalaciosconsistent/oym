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

	</div>
</div>

<c:if test="<%= collapsible && (panelCount == null) %>">
	<aui:script use="liferay-panel">
		var panel = new Liferay.Panel(
			{
				collapsible: <%= collapsible %>,
				panel: '#<%= id %>',
				persistState: <%= persistState %>
			}
		);

		Liferay.Panel.register('<%= id %>', panel);
	</aui:script>
</c:if>