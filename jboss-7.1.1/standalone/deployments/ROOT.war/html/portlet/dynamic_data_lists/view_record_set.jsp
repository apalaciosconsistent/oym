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

DDLRecordSet recordSet = (DDLRecordSet)request.getAttribute(WebKeys.DYNAMIC_DATA_LISTS_RECORD_SET);

long recordSetId = BeanParamUtil.getLong(recordSet, request, "recordSetId");

long detailDDMTemplateId = ParamUtil.getLong(request, "detailDDMTemplateId");
long listDDMTemplateId = ParamUtil.getLong(request, "listDDMTemplateId");

boolean editable = ParamUtil.getBoolean(request, "editable", true);

if (portletName.equals(PortletKeys.DYNAMIC_DATA_LISTS)) {
	editable = true;
}

boolean spreadsheet = ParamUtil.getBoolean(request, "spreadsheet");
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	localizeTitle="<%= false %>"
	title="<%= recordSet.getName(locale) %>"
/>

<portlet:actionURL var="editRecordSetURL">
	<portlet:param name="struts_action" value="/dynamic_data_lists/edit_record_set" />
</portlet:actionURL>

<aui:form action="<%= editRecordSetURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveRecordSet();" %>'>
	<c:if test="<%= DDLRecordSetPermission.contains(permissionChecker, recordSetId, ActionKeys.ADD_RECORD) && !spreadsheet && editable %>">
		<aui:button onClick='<%= renderResponse.getNamespace() + "addRecord();" %>' value="add-record" />

		<div class="separator"><!-- --></div>
	</c:if>

	<c:choose>
		<c:when test="<%= listDDMTemplateId > 0 %>">
			<%= DDLUtil.getTemplateContent(listDDMTemplateId, recordSet, themeDisplay, renderRequest, renderResponse) %>
		</c:when>
		<c:when test="<%= spreadsheet %>">
			<liferay-util:include page="/html/portlet/dynamic_data_lists/view_spreadsheet_records.jsp" />
		</c:when>
		<c:otherwise>
			<liferay-util:include page="/html/portlet/dynamic_data_lists/view_records.jsp" />
		</c:otherwise>
	</c:choose>

</aui:form>

<aui:script>
	function <portlet:namespace />addRecord() {
		submitForm(document.<portlet:namespace />fm, '<liferay-portlet:renderURL windowState="<%= WindowState.MAXIMIZED.toString() %>"><portlet:param name="struts_action" value="/dynamic_data_lists/edit_record" /><portlet:param name="redirect" value="<%= currentURL %>" /><portlet:param name="backURL" value="<%= currentURL %>" /><portlet:param name="recordSetId" value="<%= String.valueOf(recordSetId) %>" /><portlet:param name="detailDDMTemplateId" value="<%= String.valueOf(detailDDMTemplateId) %>" /></liferay-portlet:renderURL>');
	}
</aui:script>

<%
PortalUtil.setPageSubtitle(recordSet.getName(locale), request);
PortalUtil.setPageDescription(recordSet.getDescription(locale), request);

PortalUtil.addPortletBreadcrumbEntry(request, recordSet.getName(locale), currentURL);
%>