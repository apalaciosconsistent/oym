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

<%@ include file="/html/portlet/dynamic_data_mapping/init.jsp" %>

<%
String closeRedirect = ParamUtil.getString(request, "closeRedirect");

DDMStructure structure = (DDMStructure)request.getAttribute(WebKeys.DYNAMIC_DATA_MAPPING_STRUCTURE);

long structureId = BeanParamUtil.getLong(structure, request, "structureId");

boolean copyDetailTemplates = ParamUtil.getBoolean(request, "copyDetailTemplates");
boolean copyListTemplates = ParamUtil.getBoolean(request, "copyListTemplates");
%>

<portlet:actionURL var="copyStructureURL">
	<portlet:param name="struts_action" value="/dynamic_data_mapping/copy_structure" />
</portlet:actionURL>

<aui:form action="<%= copyStructureURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.COPY %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="closeRedirect" type="hidden" value="<%= closeRedirect %>" />
	<aui:input name="structureId" type="hidden" value="<%= String.valueOf(structureId) %>" />
	<aui:input name="saveAndContinue" type="hidden" value="<%= true %>" />

	<liferay-ui:error exception="<%= StructureNameException.class %>" message="please-enter-a-valid-name" />

	<aui:model-context bean="<%= structure %>" model="<%= DDMStructure.class %>" />

	<aui:fieldset>
		<aui:input name="name" />

		<aui:input checked="<%= copyDetailTemplates %>" label="copy-detail-templates" name="copyDetailTemplates" type="checkbox" />

		<aui:input checked="<%= copyListTemplates %>" label="copy-list-templates" name="copyListTemplates" type="checkbox" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" value="copy" />

		<aui:button onClick="Liferay.Util.getWindow().close();" value="close" />
	</aui:button-row>
</aui:form>