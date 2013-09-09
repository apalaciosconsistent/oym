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

<%@ include file="/html/portlet/journal/init.jsp" %>

<%
String strutsAction = ParamUtil.getString(request, "struts_action");

long groupId = ParamUtil.getLong(request, "groupId", scopeGroupId);

StructureSearch searchContainer = (StructureSearch)request.getAttribute("liferay-ui:search:searchContainer");

StructureDisplayTerms displayTerms = (StructureDisplayTerms)searchContainer.getDisplayTerms();
%>

<liferay-ui:search-toggle
	buttonLabel="search"
	displayTerms="<%= displayTerms %>"
	id="toggle_id_journal_structure_search"
>
	<aui:fieldset>
		<aui:input label="id" name="<%= displayTerms.STRUCTURE_ID %>" size="20" type="text" value="<%= displayTerms.getStructureId() %>" />

		<aui:input name="<%= displayTerms.NAME %>" size="20" type="text" value="<%= displayTerms.getName() %>" />

		<aui:input name="<%= displayTerms.DESCRIPTION %>" size="20" type="text" value="<%= displayTerms.getDescription() %>" />

		<c:if test='<%= strutsAction.equalsIgnoreCase("/journal/select_structure") %>'>
			<aui:select label="my-sites" name="<%= displayTerms.GROUP_IDS %>">
				<c:if test="<%= themeDisplay.getCompanyGroupId() != scopeGroupId %>">
					<aui:option label="" value="<%= displayTerms.getGroupIds(renderRequest) %>" />
					<aui:option label="global" selected="<%= displayTerms.getGroupId() == themeDisplay.getCompanyGroupId() %>" value="<%= themeDisplay.getCompanyGroupId() %>" />
				</c:if>

				<aui:option label="<%= themeDisplay.getParentGroupName() %>" selected="<%= displayTerms.getGroupId() == themeDisplay.getParentGroupId() %>" value="<%= themeDisplay.getParentGroupId() %>" />

				<%
				Layout scopeLayout = themeDisplay.getScopeLayout();
				%>

				<c:if test="<%= scopeLayout != null %>">

					<%
					Group scopeGroup = scopeLayout.getScopeGroup();
					%>

					<aui:option label='<%= LanguageUtil.get(pageContext, "current-page") + " (" + HtmlUtil.escape(scopeLayout.getName(locale)) + ")" %>' selected="<%= (displayTerms.getGroupIds().length == 1) && (displayTerms.getGroupIds()[0] == scopeGroup.getGroupId()) %>" value="<%= scopeGroup.getGroupId() %>" />
				</c:if>
			</aui:select>
		</c:if>
	</aui:fieldset>
</liferay-ui:search-toggle>

<%
boolean showAddStructureButton = false;
boolean showPermissionsButton = false;

if (portletName.equals(PortletKeys.JOURNAL)) {
	showAddStructureButton = JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_STRUCTURE);
	showPermissionsButton = JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.PERMISSIONS);
}
%>

<c:if test="<%= showAddStructureButton || showPermissionsButton %>">
	<aui:button-row>
		<c:if test="<%= showAddStructureButton %>">
			<aui:button onClick='<%= renderResponse.getNamespace() + "addStructure();" %>' value="add-structure" />
		</c:if>

		<c:if test="<%= showPermissionsButton %>">
			<liferay-security:permissionsURL
				modelResource="com.liferay.portlet.journal"
				modelResourceDescription="<%= HtmlUtil.escape(themeDisplay.getScopeGroupName()) %>"
				resourcePrimKey="<%= String.valueOf(scopeGroupId) %>"
				var="permissionsURL"
			/>

			<aui:button href="<%= permissionsURL %>" value="permissions" />
		</c:if>
	</aui:button-row>
</c:if>

<aui:script>
	function <portlet:namespace />addStructure() {
		var url = '<portlet:renderURL><portlet:param name="struts_action" value="/journal/edit_structure" /><portlet:param name="redirect" value="<%= currentURL %>" /><portlet:param name="groupId" value="<%= String.valueOf(groupId) %>" /></portlet:renderURL>';

		if (toggle_id_journal_structure_searchcurClickValue == 'basic') {
			url += '&<portlet:namespace /><%= displayTerms.NAME %>=' + document.<portlet:namespace />fm.<portlet:namespace /><%= displayTerms.KEYWORDS %>.value;

			submitForm(document.hrefFm, url);
		}
		else {
			document.<portlet:namespace />fm.method = 'post';
			submitForm(document.<portlet:namespace />fm, url);
		}
	}

	<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) || windowState.equals(LiferayWindowState.POP_UP) %>">
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace /><%= displayTerms.STRUCTURE_ID %>);
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace /><%= displayTerms.KEYWORDS %>);
	</c:if>
</aui:script>