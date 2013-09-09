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
String target = ParamUtil.getString(request, "target");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/users_admin/select_organization");

if (Validator.isNotNull(target)) {
	portletURL.setParameter("target", target);
}
%>

<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
	<liferay-ui:header
		title="organizations"
	/>

	<liferay-ui:search-container
		searchContainer="<%= new OrganizationSearch(renderRequest, portletURL) %>"
	>
		<liferay-ui:search-form
			page="/html/portlet/users_admin/organization_search.jsp"
		/>

		<%
		OrganizationSearchTerms searchTerms = (OrganizationSearchTerms)searchContainer.getSearchTerms();

		long parentOrganizationId = OrganizationConstants.ANY_PARENT_ORGANIZATION_ID;

		LinkedHashMap<String, Object> organizationParams = new LinkedHashMap<String, Object>();

		if (filterManageableOrganizations) {
			organizationParams.put("organizationsTree", user.getOrganizations());
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

			<%
			String rowHREF = null;

			if (OrganizationPermissionUtil.contains(permissionChecker, organization.getOrganizationId(), ActionKeys.ASSIGN_MEMBERS)) {
				StringBundler sb = new StringBundler(10);

				sb.append("javascript:opener.");
				sb.append(renderResponse.getNamespace());
				sb.append("selectOrganization('");
				sb.append(organization.getOrganizationId());
				sb.append("', '");
				sb.append(organization.getGroup().getGroupId());
				sb.append("', '");
				sb.append(UnicodeFormatter.toString(organization.getName()));
				sb.append("', '");
				sb.append(UnicodeLanguageUtil.get(pageContext, organization.getType()));
				sb.append("', '");
				sb.append(target);
				sb.append("');");
				sb.append("window.close();");

				rowHREF = sb.toString();
			}
			%>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="name"
				orderable="<%= true %>"
				property="name"
			/>

			<liferay-ui:search-container-column-text
				buffer="buffer"
				href="<%= rowHREF %>"
				name="parent-organization"
			>

				<%
				String parentOrganizationName = StringPool.BLANK;

				if (organization.getParentOrganizationId() > 0) {
					try {
						Organization parentOrganization = OrganizationLocalServiceUtil.getOrganization(organization.getParentOrganizationId());

						parentOrganizationName = parentOrganization.getName();
					}
					catch (Exception e) {
					}
				}

				buffer.append(HtmlUtil.escape(parentOrganizationName));
				%>

			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="type"
				orderable="<%= true %>"
				value="<%= LanguageUtil.get(pageContext, organization.getType()) %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="city"
				property="address.city"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="region"
				property="address.region.name"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="country"
				property="address.country.name"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />name);
</aui:script>