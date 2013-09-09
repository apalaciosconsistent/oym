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
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewSitesURL">
		<portlet:param name="struts_action" value="/sites_admin/view" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : StringPool.BLANK %>">
		<a href="<%= viewSitesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>

	<c:if test="<%= PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_COMMUNITY) %>">

		<%
		List<LayoutSetPrototype> layoutSetPrototypes = LayoutSetPrototypeServiceUtil.search(company.getCompanyId(), Boolean.TRUE, null);
		%>

		<liferay-portlet:renderURL varImpl="addSiteURL">
			<portlet:param name="struts_action" value="/sites_admin/edit_site" />
			<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.ADD %>" />
			<portlet:param name="redirect" value="<%= viewSitesURL %>" />
		</liferay-portlet:renderURL>

		<%
		boolean hasAddLayoutSetPrototypePermission = PortalPermissionUtil.contains(permissionChecker, ActionKeys.ADD_LAYOUT_SET_PROTOTYPE);
		%>

		<c:choose>
			<c:when test="<%= layoutSetPrototypes.isEmpty() && !hasAddLayoutSetPrototypePermission %>">
				<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : StringPool.BLANK %>">
					<a href="<%= addSiteURL %>"><liferay-ui:message key="add" /></a>
				</span>
			</c:when>
			<c:otherwise>
				<liferay-ui:icon-menu align="left" cssClass='<%= "lfr-toolbar-button add-button " + (toolbarItem.equals("add") ? "current" : StringPool.BLANK) %>' direction="down" extended="<%= false %>" icon='<%= themeDisplay.getPathThemeImages() + "/common/add.png" %>' message="add">

					<%
					addSiteURL.setParameter("showPrototypes", "0");
					%>

					<liferay-ui:icon
						image="site_icon"
						message="blank-site"
						method="get"
						url="<%= addSiteURL.toString() %>"
					/>

					<%
					addSiteURL.setParameter("showPrototypes", "1");

					for (LayoutSetPrototype layoutSetPrototype : layoutSetPrototypes) {
						addSiteURL.setParameter("layoutSetPrototypeId", String.valueOf(layoutSetPrototype.getLayoutSetPrototypeId()));
					%>

						<liferay-ui:icon
							image="site_icon"
							message="<%= HtmlUtil.escape(layoutSetPrototype.getName(locale)) %>"
							method="get"
							url="<%= addSiteURL.toString() %>"
						/>

					<%
					}
					%>

					<c:if test="<%= hasAddLayoutSetPrototypePermission %>">
						<liferay-portlet:renderURL portletName="<%= PortletKeys.LAYOUT_SET_PROTOTYPE %>" varImpl="manageSiteTemplateURL">
							<portlet:param name="struts_action" value="/layout_set_prototypes/view" />
							<portlet:param name="redirect" value="<%= viewSitesURL %>" />
							<portlet:param name="backURL" value="<%= viewSitesURL %>" />
						</liferay-portlet:renderURL>

						<liferay-ui:icon
							cssClass="highlited"
							image="configuration"
							message="manage-site-template"
							method="get"
							url="<%= manageSiteTemplateURL.toString() %>"
						/>
					</c:if>
				</liferay-ui:icon-menu>
			</c:otherwise>
		</c:choose>
	</c:if>
</div>