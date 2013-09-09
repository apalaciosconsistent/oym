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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
String usersListView = (String)request.getAttribute("view.jsp-usersListView");

PortletURL portletURL = (PortletURL)request.getAttribute("view.jsp-portletURL");

LinkedHashMap<String, Object> organizationParams = new LinkedHashMap<String, Object>();

boolean showList = true;

if (filterManageableOrganizations) {
	List<Organization> userOrganizations = user.getOrganizations(true);

	if (userOrganizations.isEmpty()) {
		showList = false;
	}
	else {
		organizationParams.put("organizationsTree", userOrganizations);
	}
}
%>

<c:choose>
	<c:when test="<%= showList %>">
		<liferay-ui:search-container
			rowChecker="<%= new RowChecker(renderResponse) %>"
			searchContainer="<%= new OrganizationSearch(renderRequest, portletURL) %>"
		>
			<aui:input name="deleteOrganizationIds" type="hidden" />
			<aui:input disabled="<%= true %>" name="organizationsRedirect" type="hidden" value="<%= portletURL.toString() %>" />

			<liferay-ui:search-form
				page="/html/portlet/users_admin/organization_search.jsp"
			/>

			<%
			OrganizationSearchTerms searchTerms = (OrganizationSearchTerms)searchContainer.getSearchTerms();

			long parentOrganizationId = ParamUtil.getLong(request, "parentOrganizationId", OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID);

			if (parentOrganizationId <= 0) {
				parentOrganizationId = OrganizationConstants.ANY_PARENT_ORGANIZATION_ID;
			}
			%>

			<liferay-ui:search-container-results>
				<c:choose>
					<c:when test="<%= PropsValues.ORGANIZATIONS_INDEXER_ENABLED && PropsValues.ORGANIZATIONS_SEARCH_WITH_INDEX %>">
						<%@ include file="/html/portlet/users_admin/organization_search_results_index.jspf" %>
					</c:when>
					<c:otherwise>
						<%@ include file="/html/portlet/users_admin/organization_search_results_database.jspf" %>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-results>

			<liferay-ui:search-container-row
				className="com.liferay.portal.model.Organization"
				escapedModel="<%= true %>"
				keyProperty="organizationId"
				modelVar="organization"
			>
				<liferay-portlet:renderURL varImpl="rowURL">
					<portlet:param name="struts_action" value="/users_admin/view" />
					<portlet:param name="redirect" value="<%= searchContainer.getIteratorURL().toString() %>" />
					<portlet:param name="organizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />
					<portlet:param name="usersListView" value="<%= UserConstants.LIST_VIEW_TREE %>" />
				</liferay-portlet:renderURL>

				<%
				if (!OrganizationPermissionUtil.contains(permissionChecker, organization.getOrganizationId(), ActionKeys.VIEW)) {
					rowURL = null;
				}
				%>

				<%@ include file="/html/portlet/users_admin/organization/search_columns.jspf" %>

				<liferay-ui:search-container-column-jsp
					align="right"
					path="/html/portlet/users_admin/organization_action.jsp"
				/>
			</liferay-ui:search-container-row>

			<c:if test="<%= !results.isEmpty() %>">
				<div class="separator"><!-- --></div>

				<aui:button onClick='<%= renderResponse.getNamespace() + "deleteOrganizations();" %>' value="delete" />
			</c:if>

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>
	</c:when>
	<c:otherwise>
		<div class="portlet-msg-info">
			<liferay-ui:message key="you-do-not-belong-to-an-organization-and-are-not-allowed-to-view-other-organizations" />
		</div>
	</c:otherwise>
</c:choose>