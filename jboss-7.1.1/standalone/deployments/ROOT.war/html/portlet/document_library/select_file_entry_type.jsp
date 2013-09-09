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

<liferay-util:include page="/html/portlet/document_library/file_entry_type_toolbar.jsp">
	<liferay-util:param name="strutsAction" value="/document_library/select_file_entry_type" />
</liferay-util:include>

<liferay-portlet:renderURL varImpl="portletURL">
	<portlet:param name="struts_action" value="/document_library/select_file_entry_type" />
	<portlet:param name="includeBasicFileEntryType" value="1" />
</liferay-portlet:renderURL>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<liferay-ui:header
		title="document-types"
	/>

	<liferay-ui:search-form
		page="/html/portlet/document_library/file_entry_type_search.jsp"
	/>

	<div class="separator"><!-- --></div>

	<liferay-ui:search-container
		searchContainer="<%= new StructureSearch(renderRequest, portletURL) %>"
	>
		<liferay-ui:search-container-results>
			<%@ include file="/html/portlet/document_library/file_entry_type_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.documentlibrary.model.DLFileEntryType"
			keyProperty="fileEntryTypeId"
			modelVar="fileEntryType"
		>

			<%
			StringBundler sb = new StringBundler(7);

			sb.append("javascript:Liferay.Util.getOpener().");
			sb.append(renderResponse.getNamespace());
			sb.append("selectFileEntryType('");
			sb.append(fileEntryType.getFileEntryTypeId());
			sb.append("', '");
			sb.append(HtmlUtil.escapeJS(fileEntryType.getName()));
			sb.append("', Liferay.Util.getWindow());");

			String rowHREF = sb.toString();
			%>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="name"
				value="<%= HtmlUtil.escape(fileEntryType.getName()) %>"
			/>

		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>