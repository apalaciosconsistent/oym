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
String redirect = ParamUtil.getString(request, "redirect");
String backURL = ParamUtil.getString(request, "backURL");

DDLRecord record = (DDLRecord)request.getAttribute(WebKeys.DYNAMIC_DATA_LISTS_RECORD);

long recordId = BeanParamUtil.getLong(record, request, "recordId");

long recordSetId = BeanParamUtil.getLong(record, request, "recordSetId");

long detailDDMTemplateId = ParamUtil.getLong(request, "detailDDMTemplateId");

String version = ParamUtil.getString(request, "version", DDLRecordConstants.VERSION_DEFAULT);

DDLRecordVersion recordVersion = record.getRecordVersion(version);

boolean editable = ParamUtil.getBoolean(request, "editable");

if (editable) {
	recordVersion = record.getLatestRecordVersion();
}
%>

<liferay-ui:header
	backURL="<%= backURL %>"
	title="view-record"
/>

<c:if test="<%= recordVersion != null %>">
	<aui:model-context bean="<%= recordVersion %>" model="<%= DDLRecordVersion.class %>" />

	<aui:workflow-status model="<%= DDLRecord.class %>" status="<%= recordVersion.getStatus() %>" version="<%= recordVersion.getVersion() %>" />
</c:if>

<aui:fieldset>

	<%
	DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(recordSetId);

	DDMStructure ddmStructure = recordSet.getDDMStructure(detailDDMTemplateId);

	Fields fields = null;

	if (recordVersion != null) {
		fields = StorageEngineUtil.getFields(recordVersion.getDDMStorageId());
	}
	%>

	<%= DDMXSDUtil.getHTML(pageContext, ddmStructure.getXsd(), fields, "", true, locale) %>

	<%
	boolean pending = false;

	if (recordVersion != null) {
		pending = recordVersion.isPending();
	}
	%>

	<aui:button-row>
		<aui:button href="<%= redirect %>" name="cancelButton" type="cancel" />
	</aui:button-row>
</aui:fieldset>

<%
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/dynamic_data_lists/view_record_set");
portletURL.setParameter("recordSetId", String.valueOf(recordSetId));

DDLRecordSet recordSet = DDLRecordSetLocalServiceUtil.getRecordSet(recordSetId);

PortalUtil.addPortletBreadcrumbEntry(request, recordSet.getName(locale), portletURL.toString());
PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "view-record"), currentURL);
%>