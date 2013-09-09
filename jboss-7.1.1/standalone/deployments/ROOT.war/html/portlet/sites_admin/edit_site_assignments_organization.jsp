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

<%@ include file="/html/portlet/sites_admin/init.jsp" %>

<%
Group group = (Group)request.getAttribute("edit_site_assignments.jsp-group");

if (group.isOrganization()) {
	Organization organization = OrganizationLocalServiceUtil.getOrganization(group.getOrganizationId());

	int organizationUsersCount = UserLocalServiceUtil.getOrganizationUsersCount(organization.getOrganizationId());

	List<Role> roles = new ArrayList<Role>();

	roles.add(RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.ORGANIZATION_ADMINISTRATOR));
	roles.add(RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.ORGANIZATION_OWNER));
	roles.add(RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.ORGANIZATION_USER));
%>

	<aui:input name="tabs1" type="hidden" value="organizations" />

	<liferay-ui:search-container>

		<liferay-ui:search-container-results
			results="<%= roles %>"
			total="<%= roles.size() %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.Role"
			escapedModel="<%= true %>"
			keyProperty="roleId"
			modelVar="role"
		>
			<liferay-ui:search-container-column-text
				buffer="buffer"
				name="users"
			>

				<%
				LinkedHashMap<String, Object> userParams = new LinkedHashMap<String, Object>();

				userParams.put("usersOrgs", new Long(organization.getOrganizationId()));

				if (!role.getName().equals(RoleConstants.ORGANIZATION_USER)) {
					userParams.put("userGroupRole", new Long[] {new Long(group.getGroupId()), new Long(role.getRoleId())});
				}

				List<User> orgUsers = UserLocalServiceUtil.search(company.getCompanyId(), null, WorkflowConstants.STATUS_ANY, userParams, 0, 5, UsersAdminUtil.getUserOrderByComparator("firstName", "desc"));
				int orgUsersCount = UserLocalServiceUtil.searchCount(company.getCompanyId(), null, WorkflowConstants.STATUS_ANY, userParams);

				for (User orgUser : orgUsers) {
					buffer.append(orgUser.getFullName());
					buffer.append(StringPool.COMMA_AND_SPACE);
				}

				if (buffer.length() > 2) {
					buffer.delete(buffer.length() - 2, buffer.length());
				}

				if (orgUsersCount > 5) {
					buffer.append(StringPool.SPACE);
					buffer.append(LanguageUtil.format(pageContext, "and-x-more", (orgUsersCount - 5)));
				}
				%>

			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				name="organization-role"
				property="name"
			/>

			<liferay-ui:search-container-column-text
				buffer="buffer"
				name="site-roles"
			>

				<%
				Role siteMemberRole = RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.SITE_MEMBER);

				buffer.append(siteMemberRole.getTitle(user.getLocale()));

				String name = role.getName();

				if (name.equals(RoleConstants.ORGANIZATION_ADMINISTRATOR)) {
					Role siteAdministratorRole = RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.SITE_ADMINISTRATOR);

					buffer.append(StringPool.COMMA_AND_SPACE);
					buffer.append(siteAdministratorRole.getTitle(user.getLocale()));
				}
				else if (name.equals(RoleConstants.ORGANIZATION_OWNER)) {
					Role siteOwnerRole = RoleLocalServiceUtil.getRole(group.getCompanyId(), RoleConstants.SITE_OWNER);

					buffer.append(StringPool.COMMA_AND_SPACE);
					buffer.append(siteOwnerRole.getTitle(user.getLocale()));
				}
				%>

			</liferay-ui:search-container-column-text>

		</liferay-ui:search-container-row>

		<liferay-ui:panel collapsible="<%= true %>" extended="<%= false %>" persistState="<%= true %>" title='<%= LanguageUtil.format(pageContext, "users-that-belong-to-x-x", new String[] {organization.getName(), String.valueOf(organizationUsersCount)}) %>'>
			<div class="organizations-msg-info portlet-msg">
				<liferay-ui:message arguments="<%= new String[] {organization.getName(), LanguageUtil.get(pageContext, organization.getType())} %>" key="this-site-belongs-to-x-which-is-an-organization-of-type-x" />
				<liferay-ui:message arguments="<%= organization.getName() %>" key="all-users-of-x-are-automatically-members-of-the-site" />
			</div>

			<liferay-ui:search-iterator paginate="<%= false %>" />
		</liferay-ui:panel>

	</liferay-ui:search-container>

<%
}
%>