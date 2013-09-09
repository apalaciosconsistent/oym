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

<%@ include file="/html/portlet/roles_admin/init.jsp" %>

<%
Role role = (Role)request.getAttribute("edit_role_permissions.jsp-role");

String portletResource = (String)request.getAttribute("edit_role_permissions.jsp-portletResource");

String curPortletResource = (String)request.getAttribute("edit_role_permissions.jsp-curPortletResource");
String curModelResource = (String)request.getAttribute("edit_role_permissions.jsp-curModelResource");
String curModelResourceName = (String)request.getAttribute("edit_role_permissions.jsp-curModelResourceName");

List curActions = ResourceActionsUtil.getResourceActions(curPortletResource, curModelResource);

curActions = ListUtil.sort(curActions, new ActionComparator(locale));

List guestUnsupportedActions = ResourceActionsUtil.getResourceGuestUnsupportedActions(curPortletResource, curModelResource);

List<String> headerNames = new ArrayList<String>();

headerNames.add("action");

if (role.getType() == RoleConstants.TYPE_REGULAR) {
	headerNames.add("scope");
	headerNames.add(StringPool.BLANK);
}

SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, renderResponse.createRenderURL(), headerNames, "there-are-no-actions");

searchContainer.setRowChecker(new ResourceActionRowChecker(renderResponse));

int total = curActions.size();

searchContainer.setTotal(total);

List results = curActions;

searchContainer.setResults(results);

List resultRows = searchContainer.getResultRows();

for (int i = 0; i < results.size(); i++) {
	String actionId = (String)results.get(i);

	if (role.getName().equals(RoleConstants.GUEST) && guestUnsupportedActions.contains(actionId)) {
		continue;
	}

	String curResource = null;

	if (Validator.isNull(curModelResource)) {
		curResource = curPortletResource;
	}
	else {
		curResource = curModelResource;
	}

	String target = curResource + actionId;
	int scope = ResourceConstants.SCOPE_COMPANY;
	boolean supportsFilterByGroup = false;
	List<Group> groups = Collections.emptyList();
	String groupIds = ParamUtil.getString(request, "groupIds" + target, null);
	long[] groupIdsArray = StringUtil.split(groupIds, 0L);
	List<String> groupNames = new ArrayList<String>();

	if (role.getType() == RoleConstants.TYPE_REGULAR) {
		if (Validator.isNotNull(portletResource)) {
			Portlet portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), portletResource);

			if (Validator.isNotNull(portlet.getControlPanelEntryCategory()) && portlet.getControlPanelEntryCategory().equals(PortletCategoryKeys.CONTENT)) {
				supportsFilterByGroup = true;
			}
		}

		if (!supportsFilterByGroup && !ResourceActionsUtil.isPortalModelResource(curResource) && !portletResource.equals(PortletKeys.PORTAL)) {
			supportsFilterByGroup = true;
		}

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<String, Object>();

		List<Object> rolePermissions = new ArrayList<Object>();

		rolePermissions.add(curResource);
		rolePermissions.add(new Integer(ResourceConstants.SCOPE_GROUP));
		rolePermissions.add(actionId);
		rolePermissions.add(new Long(role.getRoleId()));

		groupParams.put("rolePermissions", rolePermissions);

		groups = GroupLocalServiceUtil.search(company.getCompanyId(), new long[] {PortalUtil.getClassNameId(Company.class), PortalUtil.getClassNameId(Group.class), PortalUtil.getClassNameId(Organization.class), PortalUtil.getClassNameId(UserPersonalSite.class)}, null, null, groupParams, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		groupIdsArray = new long[groups.size()];

		for (int j = 0; j < groups.size(); j++) {
			Group group = (Group)groups.get(j);

			groupIdsArray[j] = group.getGroupId();

			groupNames.add(HtmlUtil.escape(group.getDescriptiveName(locale)));
		}

		if (!groups.isEmpty()) {
			scope = ResourceConstants.SCOPE_GROUP;
		}
	}
	else {
		scope = ResourceConstants.SCOPE_GROUP_TEMPLATE;
	}

	ResultRow row = new ResultRow(new Object[] {role, actionId, curResource, target, scope, supportsFilterByGroup, groups, groupIdsArray, groupNames}, target, i);

	row.addText(ResourceActionsUtil.getAction(pageContext, actionId));

	if (role.getType() == RoleConstants.TYPE_REGULAR) {
		row.addJSP("/html/portlet/roles_admin/edit_role_permissions_resource_scope.jsp");

		row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/roles_admin/edit_role_permissions_resource_action.jsp");
	}

	resultRows.add(row);
}
%>

<liferay-ui:search-iterator paginate="<%= false %>" searchContainer="<%= searchContainer %>" />