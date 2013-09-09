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
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/document_library/view_file_entry_type");
%>

<liferay-util:include page="/html/portlet/document_library/file_entry_type_toolbar.jsp" />

<liferay-ui:error exception="<%= RequiredStructureException.class %>" message="cannot-delete-a-document-type-that-is-presently-used-by-one-or-more-documents" />

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<liferay-ui:search-form
		page="/html/portlet/document_library/file_entry_type_search.jsp"
	/>
</aui:form>

<div class="separator"></div>

<liferay-ui:search-container
	searchContainer="<%= new StructureSearch(renderRequest, portletURL) %>"
>
	<liferay-ui:search-container-results>
		<%@ include file="/html/portlet/document_library/file_entry_type_search_results.jspf" %>
	</liferay-ui:search-container-results>

	<liferay-ui:search-container-row
		className="com.liferay.portlet.documentlibrary.model.DLFileEntryType"
		escapedModel="<%= true %>"
		keyProperty="fileEntryTypeId"
		modelVar="fileEntryType"
	>
		<liferay-ui:search-container-column-text
			name="name"
			property="name"
		/>

		<liferay-ui:search-container-column-text
			buffer="buffer"
			name="modified-date"
		>

			<%
			buffer.append(dateFormatDateTime.format(fileEntryType.getModifiedDate()));
			%>

		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-jsp
			align="right"
			path="/html/portlet/document_library/file_entry_type_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator />
</liferay-ui:search-container>