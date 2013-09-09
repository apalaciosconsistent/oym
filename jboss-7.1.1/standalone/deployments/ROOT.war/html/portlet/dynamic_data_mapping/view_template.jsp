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
String tabs1 = ParamUtil.getString(request, "tabs1", "templates");

long structureId = ParamUtil.getLong(request, "structureId");

DDMStructure structure = null;

if (structureId > 0) {
	structure = DDMStructureServiceUtil.getStructure(structureId);
}

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/dynamic_data_mapping/view_template");
portletURL.setParameter("tabs1", tabs1);
portletURL.setParameter("structureId", String.valueOf(structureId));
%>

<c:if test="<%= (structure != null) %>">
	<portlet:renderURL var="viewRecordsURL">
		<portlet:param name="struts_action" value="/dynamic_data_mapping/view" />
	</portlet:renderURL>

	<liferay-ui:header
		backURL="<%= viewRecordsURL %>"
		title='<%= LanguageUtil.format(pageContext, (Validator.isNull(templateHeaderTitle) ? "templates-for-structure-x" : templateHeaderTitle), structure.getName(locale), false) %>'
	/>
</c:if>

<liferay-util:include page="/html/portlet/dynamic_data_mapping/template_toolbar.jsp">
	<liferay-util:param name="structureId" value="<%= String.valueOf(structureId) %>" />
</liferay-util:include>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<liferay-ui:search-form
		page="/html/portlet/dynamic_data_mapping/template_search.jsp"
	/>
</aui:form>

<div class="separator"></div>

<liferay-ui:search-container
	searchContainer="<%= new TemplateSearch(renderRequest, portletURL) %>"
>
	<liferay-ui:search-container-results>
		<%@ include file="/html/portlet/dynamic_data_mapping/template_search_results.jspf" %>
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.dynamicdatamapping.model.DDMTemplate"
		keyProperty="templateId"
		modelVar="template"
	>

		<%
		String rowHREF = null;

		if (Validator.isNotNull(chooseCallback)) {
			StringBundler sb = new StringBundler(7);

			sb.append("javascript:Liferay.Util.getOpener()['");
			sb.append(HtmlUtil.escapeJS(chooseCallback));
			sb.append("']('");
			sb.append(template.getTemplateId());
			sb.append("', '");
			sb.append(HtmlUtil.escapeJS(template.getName(locale)));
			sb.append("', Liferay.Util.getWindow());");

			rowHREF = sb.toString();
		}
		%>

		<liferay-ui:search-container-column-text
			href="<%= rowHREF %>"
			name="id"
			property="templateId"
		/>

		<liferay-ui:search-container-column-text
			href="<%= rowHREF %>"
			name="name"
			value="<%= HtmlUtil.escape(LanguageUtil.get(pageContext, template.getName(locale))) %>"
		/>

		<c:if test="<%= Validator.isNull(templateTypeValue) %>">
			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="type"
				value="<%= LanguageUtil.get(pageContext, template.getType()) %>"
			/>
		</c:if>

		<liferay-ui:search-container-column-text
			href="<%= rowHREF %>"
			name="mode"
			value="<%= LanguageUtil.get(pageContext, template.getMode()) %>"
		/>

		<liferay-ui:search-container-column-text
			href="<%= rowHREF %>"
			name="language"
			value="<%= LanguageUtil.get(pageContext, template.getLanguage()) %>"
		/>

		<liferay-ui:search-container-column-text
			buffer="buffer"
			href="<%= rowHREF %>"
			name="modified-date"
		>

			<%
			buffer.append(dateFormatDateTime.format(template.getModifiedDate()));
			%>

		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-jsp
			align="right"
			path="/html/portlet/dynamic_data_mapping/template_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>