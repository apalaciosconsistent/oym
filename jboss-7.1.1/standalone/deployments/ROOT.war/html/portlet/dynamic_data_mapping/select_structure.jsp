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
long groupId = ParamUtil.getLong(request, "groupId", scopeGroupId);
%>

<c:if test="<%= showToolbar %>">
	<liferay-util:include page="/html/portlet/dynamic_data_mapping/structure_toolbar.jsp">
		<liferay-util:param name="toolbarItem" value="view-all" />
	</liferay-util:include>
</c:if>

<liferay-portlet:renderURL varImpl="portletURL">
	<portlet:param name="struts_action" value="/dynamic_data_mapping/select_structure" />
</liferay-portlet:renderURL>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">

	<c:if test="<%= !showToolbar %>">
		<liferay-ui:header
			localizeTitle="<%= false %>"
			title="<%= scopeStructureName %>"
		/>
	</c:if>

	<liferay-ui:search-form
		page="/html/portlet/dynamic_data_mapping/structure_search.jsp"
	/>

	<div class="separator"><!-- --></div>

	<liferay-ui:search-container
		searchContainer="<%= new StructureSearch(renderRequest, portletURL) %>"
	>
		<liferay-ui:search-container-results>
			<%@ include file="/html/portlet/dynamic_data_mapping/structure_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.dynamicdatamapping.model.DDMStructure"
			keyProperty="structureId"
			modelVar="structure"
		>

			<%
			StringBundler sb = new StringBundler(7);

			sb.append("javascript:Liferay.Util.getOpener().");
			sb.append(saveCallback);
			sb.append("('");
			sb.append(structure.getStructureId());
			sb.append("', '");
			sb.append(HtmlUtil.escapeJS(structure.getName(locale)));
			sb.append("', Liferay.Util.getWindow());");

			String rowHREF = sb.toString();
			%>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="id"
				value="<%= String.valueOf(structure.getStructureId()) %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="name"
				value="<%= HtmlUtil.escape(structure.getName(locale)) %>"
			/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />searchStructureId);
</aui:script>