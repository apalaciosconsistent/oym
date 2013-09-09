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

<%@ include file="/html/portlet/dynamic_data_lists/init.jsp" %>

<%
DDLRecord record = (DDLRecord)request.getAttribute(WebKeys.DYNAMIC_DATA_LISTS_RECORD);

long detailDDMTemplateId = ParamUtil.getLong(request, "detailDDMTemplateId");
%>

<div class="record-toolbar" id="<portlet:namespace />recordToolbar"></div>

<aui:script use="aui-toolbar,aui-dialog-iframe,liferay-util-window">
	var permissionPopUp = null;

	var toolbarChildren = [
		<c:if test="<%= record != null %>">
			<portlet:renderURL var="viewHistoryURL">
				<portlet:param name="struts_action" value="/dynamic_data_lists/view_record_history" />
				<portlet:param name="backURL" value="<%= currentURL %>" />
				<portlet:param name="recordId" value="<%= String.valueOf(record.getRecordId()) %>" />
				<portlet:param name="detailDDMTemplateId" value="<%= String.valueOf(detailDDMTemplateId) %>" />
			</portlet:renderURL>

			{
				handler: function(event) {
					window.location = '<%= viewHistoryURL %>';
				},
				icon: 'clock',
				label: '<%= UnicodeLanguageUtil.get(pageContext, "view-history") %>'
			}
		</c:if>
	];

	new A.Toolbar(
		{
			activeState: false,
			boundingBox: '#<portlet:namespace />recordToolbar',
			children: toolbarChildren
		}
	).render();
</aui:script>