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

<%@ include file="/html/portlet/layout_configuration/init.jsp" %>

<%
PortletCategory portletCategory = (PortletCategory)request.getAttribute(WebKeys.PORTLET_CATEGORY);

int portletCategoryIndex = GetterUtil.getInteger((String)request.getAttribute(WebKeys.PORTLET_CATEGORY_INDEX));

String oldCategoryPath = (String)request.getAttribute(WebKeys.PORTLET_CATEGORY_PATH);

String newCategoryPath = LanguageUtil.get(pageContext, portletCategory.getName());

Pattern pattern = Pattern.compile(".*");

Matcher matcher = pattern.matcher(newCategoryPath);

StringBundler divId = new StringBundler();

while (matcher.find()) {
	divId.append(matcher.group());
}

newCategoryPath = divId.toString();

if (Validator.isNotNull(oldCategoryPath)) {
	newCategoryPath = oldCategoryPath + ":" + newCategoryPath;
}

List categories = ListUtil.fromCollection(portletCategory.getCategories());

categories = ListUtil.sort(categories, new PortletCategoryComparator(locale));

List portlets = new ArrayList();

Iterator itr = portletCategory.getPortletIds().iterator();

String externalPortletCategory = null;

while (itr.hasNext()) {
	String portletId = (String)itr.next();

	Portlet portlet = PortletLocalServiceUtil.getPortletById(user.getCompanyId(), portletId);

	if ((portlet != null) && PortletPermissionUtil.contains(permissionChecker, layout, portlet, ActionKeys.ADD_TO_PAGE)) {
		portlets.add(portlet);

		PortletApp portletApp = portlet.getPortletApp();

		if (portletApp.isWARFile() && Validator.isNull(externalPortletCategory)) {
			PortletConfig curPortletConfig = PortletConfigFactoryUtil.create(portlet, application);

			ResourceBundle resourceBundle = curPortletConfig.getResourceBundle(locale);

			externalPortletCategory = ResourceBundleUtil.getString(resourceBundle, portletCategory.getName());
		}
	}
}

portlets = ListUtil.sort(portlets, new PortletTitleComparator(application, locale));

if (!categories.isEmpty() || !portlets.isEmpty()) {
%>

	<div class="lfr-add-content <%= layout.isTypePortlet() ? "collapsed" : "" %>" id="<portlet:namespace />portletCategory<%= portletCategoryIndex %>">
		<h2>
			<span><%= Validator.isNotNull(externalPortletCategory) ? externalPortletCategory : LanguageUtil.get(pageContext, portletCategory.getName()) %></span>
		</h2>

		<div class="lfr-content-category <%= layout.isTypePortlet() ? "aui-helper-hidden" : "" %>">

			<%
			itr = categories.iterator();

			while (itr.hasNext()) {
				request.setAttribute(WebKeys.PORTLET_CATEGORY, itr.next());
				request.setAttribute(WebKeys.PORTLET_CATEGORY_INDEX, String.valueOf(portletCategoryIndex));
				request.setAttribute(WebKeys.PORTLET_CATEGORY_PATH, newCategoryPath);
			%>

				<liferay-util:include page="/html/portlet/layout_configuration/view_category.jsp" />

			<%
				request.setAttribute(WebKeys.PORTLET_CATEGORY_PATH, oldCategoryPath);

				portletCategoryIndex++;
			}

			String[] runtimePortletIds = StringUtil.split(ParamUtil.getString(request, "runtimePortletIds"));

			itr = portlets.iterator();

			while (itr.hasNext()) {
				Portlet portlet = (Portlet)itr.next();

				divId.setIndex(0);

				divId.append(newCategoryPath);
				divId.append(":");

				matcher = pattern.matcher(PortalUtil.getPortletTitle(portlet, application, locale));

				while (matcher.find()) {
					divId.append(matcher.group());
				}

				boolean portletInstanceable = portlet.isInstanceable();

				boolean portletUsed = layoutTypePortlet.hasPortletId(portlet.getPortletId());

				for (String runtimePortletId : runtimePortletIds) {
					String portletId = portlet.getPortletId();

					if (runtimePortletId.equals(portletId) ||
						runtimePortletId.startsWith(portletId.concat(PortletConstants.INSTANCE_SEPARATOR))) {

						portletUsed = true;
					}
				}

				boolean portletLocked = (!portletInstanceable && portletUsed);

				if (portletInstanceable && layout.isTypePanel()) {
					continue;
				}
			%>

				<c:choose>
					<c:when test="<%= layout.isTypePortlet() %>">
						<div
							class="lfr-portlet-item <c:if test="<%= portletLocked %>">lfr-portlet-used</c:if> <c:if test="<%= portletInstanceable %>">lfr-instanceable</c:if>"
							id="<portlet:namespace />portletItem<%= portlet.getPortletId() %>"
							instanceable="<%= portletInstanceable %>"
							plid="<%= plid %>"
							portletId="<%= portlet.getPortletId() %>"
							title="<%= PortalUtil.getPortletTitle(portlet, application, locale) %>"
						>
							<p><%= PortalUtil.getPortletTitle(portlet, application, locale) %> <a href="javascript:;"><liferay-ui:message key="add" /></a></p>
						</div>

						<input id="<portlet:namespace />portletItem<%= portlet.getPortletId() %>CategoryPath" type="hidden" value="<%= divId.toString().replace(':', '-') %>" />

						<%
						List<PortletItem> portletItems = PortletItemLocalServiceUtil.getPortletItems(themeDisplay.getScopeGroupId(), portlet.getPortletId(), com.liferay.portal.model.PortletPreferences.class.getName());

						for (PortletItem portletItem : portletItems) {
							divId.setIndex(0);

							divId.append(newCategoryPath);
							divId.append(":");
							divId.append(PortalUtil.getPortletTitle(portlet, application, locale));
							divId.append(":");

							matcher = pattern.matcher(HtmlUtil.escape(portletItem.getName()));

							while (matcher.find()) {
								divId.append(matcher.group());
							}
						%>

							<div
								class="lfr-portlet-item lfr-archived-setup"
								id="<portlet:namespace />portletItem<%= portletItem.getPortletItemId() %>"
								instanceable="<%= portletInstanceable %>"
								plid="<%= plid %>"
								portletId="<%= portlet.getPortletId() %>"
								portletItemId="<%= portletItem.getPortletItemId() %>"
								title="<%= HtmlUtil.escape(portletItem.getName()) %>"
							>
								<p><%= HtmlUtil.escape(portletItem.getName()) %> <a href="javascript:;"><liferay-ui:message key="add" /></a></p>
							</div>

							<input id="<portlet:namespace />portletItem<%= portletItem.getPortletItemId() %>CategoryPath" type="hidden" value="<%= divId.toString().replace(':', '-') %>" />

						<%
						}
						%>

					</c:when>
					<c:otherwise>
						<div>
							<a href="<liferay-portlet:renderURL portletName="<%= portlet.getRootPortletId() %>" windowState="<%= WindowState.MAXIMIZED.toString() %>"></liferay-portlet:renderURL>"><%= PortalUtil.getPortletTitle(portlet, application, locale) %></a>
						</div>
					</c:otherwise>
				</c:choose>

			<%
			}
			%>

		</div>
	</div>

	<input id="<portlet:namespace />portletCategory<%= portletCategoryIndex %>CategoryPath" type="hidden" value="<%= newCategoryPath.replace(':', '-') %>" />

<%
}
%>