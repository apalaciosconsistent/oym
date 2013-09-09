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

<aui:form method="post" name="fm">
	<liferay-ui:header
		title="sites"
	/>

	<%
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/document_library/select_group");
	%>

	<liferay-ui:search-container
		searchContainer="<%= new GroupSearch(renderRequest, portletURL) %>"
	>
		<liferay-ui:search-form
			page="/html/portlet/users_admin/group_search.jsp"
			searchContainer="<%= searchContainer %>"
		/>

		<div class="separator"><!-- --></div>

		<%
		List<Group> mySites = user.getMySites();

		if (PortalUtil.isCompanyControlPanelPortlet(portletId, themeDisplay)) {
			mySites = ListUtil.copy(mySites);

			mySites.add(0, GroupLocalServiceUtil.getGroup(themeDisplay.getCompanyGroupId()));
		}
		%>

		<liferay-ui:search-container-results
			results="<%= mySites %>"
			total="<%= mySites.size() %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.portal.model.Group"
			escapedModel="<%= true %>"
			keyProperty="groupId"
			modelVar="group"
			rowIdProperty="friendlyURL"
		>

			<%
			String groupName = HtmlUtil.escape(group.getDescriptiveName(locale));

			if (group.isUser()) {
				groupName = LanguageUtil.get(pageContext, "my-site");
			}

			StringBundler sb = new StringBundler(7);

			sb.append("javascript:opener.");
			sb.append(renderResponse.getNamespace());
			sb.append("selectGroup('");
			sb.append(group.getGroupId());
			sb.append("', '");
			sb.append(HtmlUtil.escapeJS(groupName));
			sb.append("'); window.close();");

			String rowHREF = sb.toString();
			%>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="name"
				value="<%= groupName %>"
			/>

			<liferay-ui:search-container-column-text
				href="<%= rowHREF %>"
				name="type"
				value="<%= LanguageUtil.get(pageContext, group.getTypeLabel()) %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator />
	</liferay-ui:search-container>
</aui:form>