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
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewRecordsURL">
		<portlet:param name="struts_action" value="/dynamic_data_lists/view" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewRecordsURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= DDLPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_RECORD_SET) %>">
		<portlet:renderURL var="addRecordSetURL">
			<portlet:param name="struts_action" value="/dynamic_data_lists/edit_record_set" />
			<portlet:param name="redirect" value="<%= viewRecordsURL %>" />
			<portlet:param name="backURL" value="<%= viewRecordsURL %>" />
		</portlet:renderURL>

		<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : StringPool.BLANK %>">
			<a href="<%= addRecordSetURL %>"><liferay-ui:message key="add" /></a>
		</span>

		<span class="lfr-toolbar-button view-structures <%= toolbarItem.equals("manage-data-definitions") ? "current" : StringPool.BLANK %>">
			<a href="javascript:void(0);" id="<portlet:namespace />manageDDMStructuresLink"><liferay-ui:message key="manage-data-definitions" /></a>
		</span>
	</c:if>
</div>

<c:if test="<%= DDLPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_RECORD_SET) %>">
	<aui:script use="aui-base">
			A.one('#<portlet:namespace />manageDDMStructuresLink').on('click', function() {
				Liferay.Util.openDDMPortlet(
					{
						ddmResource: '<%= ddmResource %>',
						dialog: {
							width:820
						},
						storageType: '<%= PropsValues.DYNAMIC_DATA_LISTS_STORAGE_TYPE %>',
						structureName: 'data-definition',
						structureType: 'com.liferay.portlet.dynamicdatalists.model.DDLRecordSet',
						title: '<%= UnicodeLanguageUtil.get(pageContext, "data-definitions") %>'
					}
				);
			});
	</aui:script>
</c:if>