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

<%@ include file="/html/portlet/message_boards/init.jsp" %>

<%
MBCategory category = (MBCategory)request.getAttribute(WebKeys.MESSAGE_BOARDS_CATEGORY);

long categoryId = MBUtil.getCategoryId(request, category);

MBCategoryDisplay categoryDisplay = new MBCategoryDisplayImpl(scopeGroupId, categoryId);

if (category != null) {
	MBUtil.addPortletBreadcrumbEntries(category, request, renderResponse);
}
%>

<aui:form method="post" name="fm">
	<liferay-ui:header
		title="message-boards-home"
	/>

	<liferay-ui:breadcrumb showGuestGroup="<%= false %>" showLayout="<%= false %>" showParentGroups="<%= false %>" />

	<%
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/message_boards/select_category");
	portletURL.setParameter("mbCategoryId", String.valueOf(categoryId));

	List<String> headerNames = new ArrayList<String>();

	headerNames.add("category");
	headerNames.add("num-of-categories");
	headerNames.add("num-of-threads");
	headerNames.add("num-of-posts");
	headerNames.add(StringPool.BLANK);

	SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, null);

	int total = MBCategoryServiceUtil.getCategoriesCount(scopeGroupId, categoryId);

	searchContainer.setTotal(total);

	List results = MBCategoryServiceUtil.getCategories(scopeGroupId, categoryId, searchContainer.getStart(), searchContainer.getEnd());

	searchContainer.setResults(results);

	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		MBCategory curCategory = (MBCategory)results.get(i);

		curCategory = curCategory.toEscapedModel();

		ResultRow row = new ResultRow(curCategory, curCategory.getCategoryId(), i);

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("struts_action", "/message_boards/select_category");
		rowURL.setParameter("mbCategoryId", String.valueOf(curCategory.getCategoryId()));

		// Name and description

		if (Validator.isNotNull(curCategory.getDescription())) {
			row.addText(curCategory.getName().concat("<br />").concat(curCategory.getDescription()), rowURL);
		}
		else {
			row.addText(curCategory.getName(), rowURL);
		}

		// Statistics

		int categoriesCount = categoryDisplay.getSubcategoriesCount(curCategory);
		int threadsCount = categoryDisplay.getSubcategoriesThreadsCount(curCategory);
		int messagesCount = categoryDisplay.getSubcategoriesMessagesCount(curCategory);

		row.addText(String.valueOf(categoriesCount), rowURL);
		row.addText(String.valueOf(threadsCount), rowURL);
		row.addText(String.valueOf(messagesCount), rowURL);

		// Action

		StringBundler sb = new StringBundler(7);

		sb.append("opener.");
		sb.append(renderResponse.getNamespace());
		sb.append("selectCategory('");
		sb.append(curCategory.getCategoryId());
		sb.append("', '");
		sb.append(UnicodeFormatter.toString(curCategory.getName()));
		sb.append("'); window.close();");

		row.addButton("right", SearchEntry.DEFAULT_VALIGN, LanguageUtil.get(pageContext, "choose"), sb.toString());

		// Add result row

		resultRows.add(row);
	}
	%>

	<aui:button-row>

		<%
		String taglibSelectOnClick = "opener." + renderResponse.getNamespace() + "selectCategory('" + categoryId + "','" + ((category != null) ? category.getName() : LanguageUtil.get(pageContext, "message-boards-home")) + "');window.close();";
		%>

		<aui:button onClick="<%= taglibSelectOnClick %>" value="choose-this-category" />
	</aui:button-row>

	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
</aui:form>