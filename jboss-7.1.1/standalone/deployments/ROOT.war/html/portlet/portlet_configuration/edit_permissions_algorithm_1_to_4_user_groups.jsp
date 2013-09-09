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

<%@ include file="/html/portlet/portlet_configuration/init.jsp" %>

<%
String tabs3 = (String)request.getAttribute("edit_permissions_algorithm_1_to_4.jsp-tabs3");

portletResource = (String)request.getAttribute("edit_permissions_algorithm_1_to_4.jsp-portletResource");
String modelResource = (String)request.getAttribute("edit_permissions_algorithm_1_to_4.jsp-modelResource");
Resource resource = (Resource)request.getAttribute("edit_permissions_algorithm_1_to_4.jsp-resource");

PortletURL portletURL = (PortletURL)request.getAttribute("edit_permissions_algorithm_1_to_4.jsp-portletURL");

String userGroupIds = ParamUtil.getString(request, "userGroupIds");
long[] userGroupIdsArray = StringUtil.split(userGroupIds, 0L);
int userGroupIdsPos = ParamUtil.getInteger(request, "userGroupIdsPos");
%>

<aui:input name="userGroupIds" type="hidden" value="<%= userGroupIds %>" />
<aui:input name="userGroupIdsPos" type="hidden" value="<%= userGroupIdsPos %>" />
<aui:input name="userGroupIdsPosValue" type="hidden" />
<aui:input name="userGroupIdActionIds" type="hidden" />

<c:choose>
	<c:when test="<%= userGroupIdsArray.length == 0 %>">
		<liferay-ui:tabs
			names="current,available"
			param="tabs3"
			url="<%= portletURL.toString() %>"
		/>

		<liferay-ui:search-container
			rowChecker="<%= new RowChecker(renderResponse) %>"
			searchContainer="<%= new UserGroupSearch(renderRequest, portletURL) %>"
		>
			<liferay-ui:search-form
				page="/html/portlet/users_admin/user_group_search.jsp"
			/>

			<%
			UserGroupSearchTerms searchTerms = (UserGroupSearchTerms)searchContainer.getSearchTerms();

			LinkedHashMap userGroupParams = new LinkedHashMap();

			if (tabs3.equals("current")) {
				userGroupParams.put("permissionsResourceId", new Long(resource.getResourceId()));
			}
			%>

			<liferay-ui:search-container-results
				results="<%= UserGroupLocalServiceUtil.search(company.getCompanyId(), searchTerms.getKeywords(), userGroupParams, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator()) %>"
				total="<%= UserGroupLocalServiceUtil.searchCount(company.getCompanyId(), searchTerms.getKeywords(), userGroupParams) %>"
			/>

			<liferay-ui:search-container-row
				className="com.liferay.portal.model.UserGroup"
				escapedModel="<%= true %>"
				keyProperty="userGroupId"
				modelVar="userGroup"
			>
				<liferay-ui:search-container-column-text
					name="name"
					orderable="<%= true %>"
					property="name"
				/>

				<liferay-ui:search-container-column-text
					name="description"
					orderable="<%= true %>"
					property="description"
				/>

				<liferay-ui:search-container-column-text
					buffer="buffer"
					name="permissions"
				>

					<%
					List permissions = PermissionLocalServiceUtil.getGroupPermissions(userGroup.getGroup().getGroupId(), resource.getResourceId());

					List actions = ResourceActionsUtil.getActions(permissions);
					List actionsNames = ResourceActionsUtil.getActionsNames(pageContext, actions);

					buffer.append(StringUtil.merge(actionsNames, ", "));
					%>

				</liferay-ui:search-container-column-text>
			</liferay-ui:search-container-row>

			<div class="separator"><!-- --></div>

			<aui:button onClick='<%= renderResponse.getNamespace() + "updateUserGroupPermissions();" %>' value="update-permissions" />

			<br /><br />

			<liferay-ui:search-iterator />
		</liferay-ui:search-container>
	</c:when>
	<c:otherwise>

		<%
		UserGroup userGroup = UserGroupLocalServiceUtil.getUserGroup(userGroupIdsArray[userGroupIdsPos]);
		%>

		<liferay-ui:header
			localizeTitle="<%= false %>"
			title="<%= userGroup.getName() %>"
		/>

		<%
		List permissions = PermissionLocalServiceUtil.getGroupPermissions(userGroup.getGroup().getGroupId(), resource.getResourceId());

		List actions1 = ResourceActionsUtil.getResourceActions(portletResource, modelResource);
		List actions2 = ResourceActionsUtil.getActions(permissions);

		// Left list

		List leftList = new ArrayList();

		for (int i = 0; i < actions2.size(); i++) {
			String actionId = (String)actions2.get(i);

			leftList.add(new KeyValuePair(actionId, ResourceActionsUtil.getAction(pageContext, actionId)));
		}

		leftList = ListUtil.sort(leftList, new KeyValuePairComparator(false, true));

		// Right list

		List rightList = new ArrayList();

		for (int i = 0; i < actions1.size(); i++) {
			String actionId = (String)actions1.get(i);

			if (!actions2.contains(actionId)) {
				rightList.add(new KeyValuePair(actionId, ResourceActionsUtil.getAction(pageContext, actionId)));
			}
		}

		rightList = ListUtil.sort(rightList, new KeyValuePairComparator(false, true));
		%>

		<div class="assign-permissions">
			<liferay-ui:input-move-boxes
				leftBoxName="current_actions"
				leftList="<%= leftList %>"
				leftTitle="what-they-can-do"
				rightBoxName="available_actions"
				rightList="<%= rightList %>"
				rightTitle="what-they-cant-do"
			/>

			<aui:button-row>

				<%
				String taglibPreviousOnClick = renderResponse.getNamespace() + "saveUserGroupPermissions(" + (userGroupIdsPos - 1) + ", '" + userGroupIdsArray[userGroupIdsPos] + "');";
				String taglibNextOnClick = renderResponse.getNamespace() + "saveUserGroupPermissions(" + (userGroupIdsPos + 1) + ", '" + userGroupIdsArray[userGroupIdsPos] + "');";
				String taglibFinishedOnClick = renderResponse.getNamespace() + "saveUserGroupPermissions(-1, '"+ userGroupIdsArray[userGroupIdsPos] + "');";
				%>

				<aui:button cssClass="previous" disabled="<%= userGroupIdsPos <= 0 %>" onClick="<%= taglibPreviousOnClick %>" value="previous" />

				<aui:button cssClass="next" disabled="<%= userGroupIdsPos + 1 >= userGroupIdsArray.length %>" onClick="<%= taglibNextOnClick %>" value="next" />

				<aui:button cssClass="finished" onClick="<%= taglibFinishedOnClick %>" value="finished" />
			</aui:button-row>
		</div>
	</c:otherwise>
</c:choose>