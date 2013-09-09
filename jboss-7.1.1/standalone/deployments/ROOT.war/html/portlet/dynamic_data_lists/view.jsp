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
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/dynamic_data_lists/view");
%>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />

	<liferay-util:include page="/html/portlet/dynamic_data_lists/toolbar.jsp">
		<liferay-util:param name="toolbarItem" value="view-all" />
	</liferay-util:include>

	<liferay-ui:search-container
		searchContainer="<%= new RecordSetSearch(renderRequest, portletURL) %>"
	>

		<%
		RecordSetDisplayTerms displayTerms = (RecordSetDisplayTerms)searchContainer.getDisplayTerms();
		RecordSetSearchTerms searchTerms = (RecordSetSearchTerms)searchContainer.getSearchTerms();
		%>

		<liferay-ui:search-form
			page="/html/portlet/dynamic_data_lists/record_set_search.jsp"
		/>

		<liferay-ui:search-container-results>
			<%@ include file="/html/portlet/dynamic_data_lists/record_set_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="com.liferay.portlet.dynamicdatalists.model.DDLRecordSet"
			escapedModel="<%= true %>"
			keyProperty="recordSetId"
			modelVar="recordSet"
		>
			<liferay-portlet:renderURL varImpl="rowURL">
				<portlet:param name="struts_action" value="/dynamic_data_lists/view_record_set" />
				<portlet:param name="redirect" value="<%= searchContainer.getIteratorURL().toString() %>" />
				<portlet:param name="recordSetId" value="<%= String.valueOf(recordSet.getRecordSetId()) %>" />
			</liferay-portlet:renderURL>

			<%
			if (!DDLRecordSetPermission.contains(permissionChecker, recordSet, ActionKeys.VIEW)) {
				rowURL = null;
			}
			%>

			<%@ include file="/html/portlet/dynamic_data_lists/search_columns.jspf" %>

			<liferay-ui:search-container-column-jsp
				align="right"
				path="/html/portlet/dynamic_data_lists/record_set_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<div class="separator"><!-- --></div>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>

<div class="aui-helper-hidden" id="<portlet:namespace />export-list">
	<aui:select label="file-extension" name="fileExtension">
		<aui:option value="csv">CSV</aui:option>
		<aui:option value="xml">XML</aui:option>
	</aui:select>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />exportRecordSet',
		function(url) {
			var A = AUI();

			var form = A.Node.create('<form />');

			form.setAttribute('method', 'POST');

			var content = A.one('#<portlet:namespace />export-list');

			if (content) {
				form.append(content);
				content.show();
			}

			var dialog = new A.Dialog(
				{
					align: Liferay.Util.Window.ALIGN_CENTER,
					bodyContent: form,
					buttons: [
						{
							handler: function() {
								submitForm(form, url, false);
							},
							label: '<%= UnicodeLanguageUtil.get(pageContext, "ok") %>'
						},
						{
							handler: function() {
								this.close();
							},
							label: '<%= UnicodeLanguageUtil.get(pageContext, "cancel") %>'
						}
					],
					modal: true,
					title: '<%= UnicodeLanguageUtil.get(pageContext, "export") %>',
					width: 400
				}
			).render();

		},
		['aui-dialog']
	);
</aui:script>