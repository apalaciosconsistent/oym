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

<%@ include file="/html/portlet/layout_set_prototypes/init.jsp" %>

<%
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/layout_set_prototypes/view");
%>

<liferay-ui:error exception="<%= RequiredLayoutSetPrototypeException.class %>" message="you-cannot-delete-site-templates-that-are-used-by-a-site" />

<liferay-util:include page="/html/portlet/layout_set_prototypes/toolbar.jsp">
	<liferay-util:param name="toolbarItem" value="view-all" />
</liferay-util:include>

<aui:form action="<%= portletURL.toString() %>" method="get" name="fm">
	<liferay-portlet:renderURLParams varImpl="portletURL" />
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />

	<liferay-ui:search-container
		headerNames="name"
		searchContainer='<%= new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, null, LanguageUtil.get(pageContext, "no-site-templates-were-found")) %>'
	>
		<aui:input name="deleteLayoutSetPrototypesIds" type="hidden" />

		<liferay-ui:search-container-results
			results="<%= LayoutSetPrototypeLocalServiceUtil.search(company.getCompanyId(), null, searchContainer.getStart(), searchContainer.getEnd(), null) %>"
			total="<%= LayoutSetPrototypeLocalServiceUtil.searchCount(company.getCompanyId(), null) %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.LayoutSetPrototype"
			escapedModel="<%= true %>"
			keyProperty="layoutSetPrototypeId"
			modelVar="layoutSetPrototype"
		>
			<liferay-portlet:renderURL varImpl="rowURL">
				<portlet:param name="struts_action" value="/layout_set_prototypes/edit_layout_set_prototype" />
				<portlet:param name="redirect" value="<%= searchContainer.getIteratorURL().toString() %>" />
				<portlet:param name="backURL" value="<%= searchContainer.getIteratorURL().toString() %>" />
				<portlet:param name="layoutSetPrototypeId" value="<%= String.valueOf(layoutSetPrototype.getLayoutSetPrototypeId()) %>" />
			</liferay-portlet:renderURL>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="name"
				orderable="<%= true %>"
				value="<%= layoutSetPrototype.getName(locale) %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="active"
			>
				<%= LanguageUtil.get(pageContext, layoutSetPrototype.isActive()? "yes" : "no") %>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-jsp
				align="right"
				path="/html/portlet/layout_set_prototypes/layout_set_prototype_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>

<aui:script use="aui-base,aui-dialog">
	A.getBody().delegate(
		'click',
		function(event) {
			event.preventDefault();

			var link = event.currentTarget;
			var title = link.get('text');

			Liferay.Util.openWindow(
				{
					dialog:
						{
							centered: true,
							constrain: true,
							modal: true,
							width: 600
						},
					id: '<portlet:namespace />' + title,
					title: title,
					uri: link.attr('href')
				}
			);
		},
		'.layoutset-prototype-action a'
	);
</aui:script>