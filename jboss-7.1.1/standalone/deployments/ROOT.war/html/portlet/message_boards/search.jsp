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
String redirect = ParamUtil.getString(request, "redirect");

long breadcrumbsCategoryId = ParamUtil.getLong(request, "breadcrumbsCategoryId");
long breadcrumbsMessageId = ParamUtil.getLong(request, "breadcrumbsMessageId");

long searchCategoryId = ParamUtil.getLong(request, "searchCategoryId");

long[] categoryIdsArray = null;

List categoryIds = new ArrayList();

categoryIds.add(new Long(searchCategoryId));

MBCategoryServiceUtil.getSubcategoryIds(categoryIds, scopeGroupId, searchCategoryId);

categoryIdsArray = StringUtil.split(StringUtil.merge(categoryIds), 0L);

long threadId = ParamUtil.getLong(request, "threadId");
String keywords = ParamUtil.getString(request, "keywords");
%>

<liferay-portlet:renderURL varImpl="searchURL">
	<portlet:param name="struts_action" value="/message_boards/search" />
</liferay-portlet:renderURL>

<aui:form action="<%= searchURL %>" method="get" name="fm">
	<liferay-portlet:renderURLParams varImpl="searchURL" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="breadcrumbsCategoryId" type="hidden" value="<%= breadcrumbsCategoryId %>" />
	<aui:input name="breadcrumbsMessageId" type="hidden" value="<%= breadcrumbsMessageId %>" />
	<aui:input name="searchCategoryId" type="hidden" value="<%= searchCategoryId %>" />
	<aui:input name="threadId" type="hidden" value="<%= threadId %>" />

	<liferay-ui:header
		backURL="<%= redirect %>"
		title="search"
	/>

	<%
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/message_boards/search");
	portletURL.setParameter("redirect", redirect);
	portletURL.setParameter("breadcrumbsCategoryId", String.valueOf(breadcrumbsCategoryId));
	portletURL.setParameter("breadcrumbsMessageId", String.valueOf(breadcrumbsMessageId));
	portletURL.setParameter("searchCategoryId", String.valueOf(searchCategoryId));
	portletURL.setParameter("threadId", String.valueOf(threadId));
	portletURL.setParameter("keywords", keywords);

	List<String> headerNames = new ArrayList<String>();

	headerNames.add("#");
	headerNames.add("category");
	headerNames.add("message");
	headerNames.add("thread-posts");
	headerNames.add("thread-views");

	SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, LanguageUtil.format(pageContext, "no-messages-were-found-that-matched-the-keywords-x", "<strong>" + HtmlUtil.escape(keywords) + "</strong>"));

	try {
		Indexer indexer = IndexerRegistryUtil.getIndexer(MBMessage.class);

		SearchContext searchContext = SearchContextFactory.getInstance(request);

		searchContext.setAttribute("paginationType", "more");
		searchContext.setCategoryIds(categoryIdsArray);
		searchContext.setEnd(searchContainer.getEnd());
		searchContext.setKeywords(keywords);
		searchContext.setStart(searchContainer.getStart());

		Hits results = indexer.search(searchContext);

		int total = results.getLength();

		searchContainer.setTotal(total);

		List resultRows = searchContainer.getResultRows();

		for (int i = 0; i < results.getDocs().length; i++) {
			Document doc = results.doc(i);

			ResultRow row = new ResultRow(doc, i, i);

			// Position

			row.addText(searchContainer.getStart() + i + 1 + StringPool.PERIOD);

			// Category

			long categoryId = GetterUtil.getLong(doc.get("categoryId"));

			MBCategory category = null;

			try {
				category = MBCategoryLocalServiceUtil.getCategory(categoryId);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Message boards search index is stale and contains category " + categoryId);
				}

				continue;
			}

			PortletURL categoryUrl = renderResponse.createRenderURL();

			categoryUrl.setParameter("struts_action", "/message_boards/view");
			categoryUrl.setParameter("redirect", currentURL);
			categoryUrl.setParameter("mbCategoryId", String.valueOf(categoryId));

			row.addText(HtmlUtil.escape(category.getName()), categoryUrl);

			// Thread and message

			long curThreadId = GetterUtil.getLong(doc.get("threadId"));
			long messageId = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

			MBThread thread = null;

			try {
				thread = MBThreadLocalServiceUtil.getThread(curThreadId);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Message boards search index is stale and contains thread " + curThreadId);
				}

				continue;
			}

			MBMessage message = null;

			try {
				message = MBMessageLocalServiceUtil.getMessage(messageId);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Message boards search index is stale and contains message " + messageId);
				}

				continue;
			}

			PortletURL rowURL = renderResponse.createRenderURL();

			rowURL.setParameter("struts_action", "/message_boards/view_message");
			rowURL.setParameter("redirect", currentURL);
			rowURL.setParameter("messageId", String.valueOf(messageId));

			row.addText(HtmlUtil.escape(message.getSubject()), rowURL);
			row.addText(String.valueOf(thread.getMessageCount()), rowURL);
			row.addText(String.valueOf(thread.getViewCount()), rowURL);

			// Add result row

			resultRows.add(row);
		}
	%>

		<span class="aui-search-bar">
			<aui:input inlineField="<%= true %>" label="" name="keywords" size="30" title="search-messages" type="text" value="<%= keywords %>" />

			<aui:button type="submit" value="search" />
		</span>

		<br /><br />

		<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" type="more" />

	<%
	}
	catch (Exception e) {
		_log.error(e.getMessage());
	}
	%>

</aui:form>

<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) && !themeDisplay.isFacebook() %>">
	<aui:script>
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />keywords);
	</aui:script>
</c:if>

<%
if (breadcrumbsCategoryId > 0) {
	MBUtil.addPortletBreadcrumbEntries(breadcrumbsCategoryId, request, renderResponse);
}

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(pageContext, "search") + ": " + keywords, currentURL);
%>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.message_boards.search_jsp");
%>