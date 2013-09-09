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

<%@ include file="/html/portlet/document_library/init.jsp" %>

<%
String navigation = ParamUtil.getString(request, "navigation", "home");

long folderId = GetterUtil.getLong((String)request.getAttribute("view.jsp-folderId"));

long fileEntryTypeId = ParamUtil.getLong(request, "fileEntryTypeId", -1);

String displayStyle = ParamUtil.getString(request, "displayStyle");

if (Validator.isNull(displayStyle)) {
	displayStyle = portalPreferences.getValue(PortletKeys.DOCUMENT_LIBRARY, "display-style", PropsValues.DL_DEFAULT_DISPLAY_VIEW);
}

String keywords = ParamUtil.getString(request, "keywords");
%>

<c:if test="<%= displayViews.length > 1 %>">
	<aui:script use="aui-dialog,aui-dialog-iframe">
		var buttonRow = A.one('#<portlet:namespace />displayStyleToolbar');

		function onButtonClick(displayStyle) {
			var config = {
				'<portlet:namespace />struts_action': '<%= Validator.isNull(keywords) ? "/document_library/view" : "/document_library/search" %>',
				'<portlet:namespace />navigation': '<%= HtmlUtil.escapeJS(navigation) %>',
				'<portlet:namespace />folderId': '<%= folderId %>',
				'<portlet:namespace />displayStyle': displayStyle,
				'<portlet:namespace />viewEntries': <%= Boolean.FALSE.toString() %>,
				'<portlet:namespace />viewEntriesPage': <%= Boolean.FALSE.toString() %>,
				'<portlet:namespace />viewFolders': <%= Boolean.FALSE.toString() %>,
				'<portlet:namespace />saveDisplayStyle': <%= Boolean.TRUE.toString() %>
			};

			if (<%= Validator.isNull(keywords) %>) {
				config['<portlet:namespace />viewEntries'] = <%= Boolean.TRUE.toString() %>;
			}
			else {
				config['<portlet:namespace />keywords'] = '<%= HtmlUtil.escapeJS(keywords) %>';
			}

			if (<%= fileEntryTypeId != -1 %>) {
				config['<portlet:namespace />fileEntryTypeId'] = '<%= String.valueOf(fileEntryTypeId) %>';
			}

			updateDisplayStyle(config);
		}

		function updateDisplayStyle(config) {
			var displayStyle = config['<portlet:namespace />displayStyle'];

			<%
			for (int i = 0; i < displayViews.length; i++) {
			%>

				displayStyleToolbar.item(<%= i %>).StateInteraction.set('active', (displayStyle === '<%= displayViews[i] %>'));

			<%
			}
			%>

			Liferay.fire(
				'<portlet:namespace />dataRequest',
				{
					requestParams: config,
					src: Liferay.DL_ENTRIES_PAGINATOR
				}
			);
		}

		var displayStyleToolbarChildren = [];

		<%
		for (int i = 0; i < displayViews.length; i++) {
		%>

			displayStyleToolbarChildren.push(
				{
					handler: A.bind(onButtonClick, null, '<%= displayViews[i] %>'),
					icon: 'display-<%= displayViews[i] %>',
					title: '<%= UnicodeLanguageUtil.get(pageContext, displayViews[i] + "-view") %>'
				}
			);

		<%
		}
		%>

		var displayStyleToolbar = buttonRow.getData('displayStyleToolbar');

		if (displayStyleToolbar) {
			displayStyleToolbar.removeAll();
		}

		displayStyleToolbar = new A.Toolbar(
			{
				activeState: true,
				boundingBox: buttonRow,
				children: displayStyleToolbarChildren
			}
		).render();

		var index = 0;

		<%
		for (int i = 0; i < displayViews.length; i++) {
			if (displayStyle.equals(displayViews[i])) {
		%>

				index = <%= i %>;

		<%
				break;
			}
		}
		%>

		displayStyleToolbar.item(index).StateInteraction.set('active', true);

		buttonRow.setData('displayStyleToolbar', displayStyleToolbar);
	</aui:script>
</c:if>