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

<%@ include file="/html/portlet/journal_content/init.jsp" %>

<%
int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM);

String redirect = ParamUtil.getString(request, "redirect");

JournalArticle article = null;

groupId = ParamUtil.getLong(request, "groupId", themeDisplay.getScopeGroupId());

String type = ParamUtil.getString(request, "type");

try {
	if (Validator.isNotNull(articleId)) {
		article = JournalArticleLocalServiceUtil.getLatestArticle(groupId, articleId);

		article = article.toEscapedModel();

		groupId = article.getGroupId();
		type = article.getType();
	}
}
catch (NoSuchArticleException nsae) {
}
%>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />
<liferay-portlet:renderURL portletConfiguration="true" varImpl="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm1">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="redirect" type="hidden" value='<%= configurationRenderURL + StringPool.AMPERSAND + renderResponse.getNamespace() + "cur=" + cur %>' />

	<liferay-ui:error exception="<%= NoSuchArticleException.class %>" message="the-web-content-could-not-be-found" />

	<div class="portlet-msg-info">
		<span class="displaying-help-message-holder <%= article == null ? StringPool.BLANK : "aui-helper-hidden" %>">
			<liferay-ui:message key="please-select-a-web-content-from-the-list-below" />
		</span>

		<span class="displaying-article-id-holder <%= article == null ? "aui-helper-hidden" : StringPool.BLANK %>">
			<liferay-ui:message key="displaying-content" />: <span class="displaying-article-id"><%= article != null ? article.getTitle(locale) : StringPool.BLANK %></span>
		</span>
	</div>

	<c:if test="<%= article != null %>">

		<%
		String structureId = article.getStructureId();

		JournalStructure structure = null;

		long structureGroupId = groupId;

		if (Validator.isNotNull(structureId)) {
			try {
				structure = JournalStructureLocalServiceUtil.getStructure(groupId, structureId, true);

				structureGroupId = structure.getGroupId();
			}
			catch (NoSuchStructureException nsse) {
			}

			List<JournalTemplate> templates = new ArrayList<JournalTemplate>();

			templates.addAll(JournalTemplateLocalServiceUtil.getStructureTemplates(structureGroupId, structureId));

			if (groupId != structureGroupId) {
				templates.addAll(JournalTemplateLocalServiceUtil.getStructureTemplates(groupId, structureId));
			}

			if (!templates.isEmpty()) {
				if (Validator.isNull(templateId)) {
					templateId = article.getTemplateId();
				}
		%>

				<aui:fieldset>
					<liferay-ui:message key="override-default-template" />

					<liferay-ui:table-iterator
						list="<%= templates %>"
						listType="com.liferay.portlet.journal.model.JournalTemplate"
						rowLength="3"
						rowPadding="30"
					>

						<%
						boolean templateChecked = false;

						if (templateId.equals(tableIteratorObj.getTemplateId())) {
							templateChecked = true;
						}

						if ((tableIteratorPos.intValue() == 0) && Validator.isNull(templateId)) {
							templateChecked = true;
						}
						%>

						<liferay-portlet:renderURL portletName="<%= PortletKeys.JOURNAL %>" var="editTemplateURL">
							<portlet:param name="struts_action" value="/journal/edit_template" />
							<portlet:param name="redirect" value="<%= currentURL %>" />
							<portlet:param name="groupId" value="<%= String.valueOf(tableIteratorObj.getGroupId()) %>" />
							<portlet:param name="templateId" value="<%= tableIteratorObj.getTemplateId() %>" />
						</liferay-portlet:renderURL>

						<liferay-util:buffer var="linkContent">
							<aui:a href="<%= editTemplateURL %>" id="tableIteratorObjName"><%= tableIteratorObj.getName() %></aui:a>
						</liferay-util:buffer>

						<aui:input checked="<%= templateChecked %>" label="<%= linkContent %>" name="overideTemplateId" onChange='<%= "if (this.checked) {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "templateId.value = this.value;}" %>' type="radio" value="<%= tableIteratorObj.getTemplateId() %>" />

						<c:if test="<%= tableIteratorObj.isSmallImage() %>">
							<br />

							<img border="0" hspace="0" src="<%= Validator.isNotNull(tableIteratorObj.getSmallImageURL()) ? tableIteratorObj.getSmallImageURL() : themeDisplay.getPathImage() + "/journal/template?img_id=" + tableIteratorObj.getSmallImageId() + "&t=" + WebServerServletTokenUtil.getToken(tableIteratorObj.getSmallImageId()) %>" vspace="0" />
						</c:if>
					</liferay-ui:table-iterator>

					<br />
				</aui:fieldset>

		<%
			}
		}
		%>

	</c:if>

	<%
	DynamicRenderRequest dynamicRenderRequest = new DynamicRenderRequest(renderRequest);

	dynamicRenderRequest.setParameter("type", type);
	dynamicRenderRequest.setParameter("groupId", String.valueOf(groupId));

	ArticleSearch searchContainer = new ArticleSearch(dynamicRenderRequest, configurationRenderURL);

	List<String> headerNames = searchContainer.getHeaderNames();

	headerNames.clear();

	headerNames.add("id");
	headerNames.add("title");
	headerNames.add("modified-date");
	headerNames.add("display-date");
	headerNames.add("author");
	%>

	<liferay-ui:search-form
		page="/html/portlet/journal/article_search.jsp"
		searchContainer="<%= searchContainer %>"
	>
		<liferay-ui:param name="groupId" value="<%= String.valueOf(groupId) %>" />
		<liferay-ui:param name="type" value="<%= HtmlUtil.escape(type) %>" />
	</liferay-ui:search-form>

	<br />

	<%
	ArticleSearchTerms searchTerms = (ArticleSearchTerms)searchContainer.getSearchTerms();

	searchTerms.setVersion(-1);

	List<JournalArticle> results = null;
	%>

	<%@ include file="/html/portlet/journal/article_search_results.jspf" %>

	<%
	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		JournalArticle curArticle = results.get(i);

		curArticle = curArticle.toEscapedModel();

		ResultRow row = new ResultRow(null, curArticle.getArticleId() + EditArticleAction.VERSION_SEPARATOR + curArticle.getVersion(), i);

		StringBundler sb = new StringBundler(7);

		sb.append("javascript:");
		sb.append(renderResponse.getNamespace());
		sb.append("selectArticle('");
		sb.append(curArticle.getArticleId());
		sb.append("','");
		sb.append(curArticle.getTitle(locale));
		sb.append("');");

		String rowHREF = sb.toString();

		// Article id

		row.addText(curArticle.getArticleId(), rowHREF);

		// Title

		row.addText(curArticle.getTitle(locale), rowHREF);

		// Modified date

		row.addText(dateFormatDate.format(curArticle.getModifiedDate()), rowHREF);

		// Display date

		row.addText(dateFormatDate.format(curArticle.getDisplayDate()), rowHREF);

		// Author

		row.addText(PortalUtil.getUserName(curArticle), rowHREF);

		// Add result row

		resultRows.add(row);
	}
	%>

	<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
</aui:form>

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value='<%= configurationRenderURL + StringPool.AMPERSAND + renderResponse.getNamespace() + "cur" + cur %>' />
	<aui:input name="preferences--groupId--" type="hidden" value="<%= groupId %>" />
	<aui:input name="preferences--articleId--" type="hidden" value="<%= articleId %>" />
	<aui:input name="preferences--templateId--" type="hidden" value="<%= templateId %>" />

	<aui:fieldset cssClass="aui-helper-hidden">
		<aui:field-wrapper label="portlet-id">
			<%= HtmlUtil.escape(portletResource) %>
		</aui:field-wrapper>
	</aui:fieldset>

	<br />

	<aui:fieldset>
		<aui:input name="preferences--showAvailableLocales--" type="checkbox" value="<%= showAvailableLocales %>" />

		<aui:field-wrapper helpMessage='<%= !openOfficeServerEnabled ? "enabling-openoffice-integration-provides-document-conversion-functionality" : StringPool.BLANK %>' label="enable-conversion-to">

			<%
			for (String conversion : conversions) {
			%>

				<aui:field-wrapper inlineField="<%= true %>" inlineLabel="left" label="<%= conversion.toUpperCase() %>">
					<input <%= ArrayUtil.contains(extensions, conversion) ? "checked": "" %> <%= !openOfficeServerEnabled ? "disabled" : "" %> name="<portlet:namespace />extensions" type="checkbox" value="<%= conversion %>" />
				</aui:field-wrapper>

			<%
			}
			%>

		</aui:field-wrapper>

		<aui:input name="preferences--enablePrint--" type="checkbox" value="<%= enablePrint %>" />

		<aui:input name="preferences--enableRelatedAssets--" type="checkbox" value="<%= enableRelatedAssets %>" />

		<aui:input name="preferences--enableRatings--" type="checkbox" value="<%= enableRatings %>" />

		<c:if test="<%= PropsValues.JOURNAL_ARTICLE_COMMENTS_ENABLED %>">
			<aui:input name="preferences--enableComments--" type="checkbox" value="<%= enableComments %>" />

			<aui:input name="preferences--enableCommentRatings--" type="checkbox" value="<%= enableCommentRatings %>" />
		</c:if>

		<aui:input name="preferences--enableViewCountIncrement--" type="checkbox" value="<%= enableViewCountIncrement %>" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />selectArticle',
		function(articleId, articletTitle) {
			var A = AUI();

			document.<portlet:namespace />fm.<portlet:namespace />articleId.value = articleId;
			document.<portlet:namespace />fm.<portlet:namespace />templateId.value = "";

			A.one('.displaying-article-id-holder').show();
			A.one('.displaying-help-message-holder').hide();

			var displayArticleId = A.one('.displaying-article-id');

			displayArticleId.set('innerHTML', articletTitle + ' (<%= UnicodeLanguageUtil.get(pageContext, "modified") %>)');
			displayArticleId.addClass('modified');
		},
		['aui-base']
	);
</aui:script>