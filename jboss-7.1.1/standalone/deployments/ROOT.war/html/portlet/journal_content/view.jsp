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
JournalArticle article = (JournalArticle)request.getAttribute(WebKeys.JOURNAL_ARTICLE);
JournalArticleDisplay articleDisplay = (JournalArticleDisplay)request.getAttribute(WebKeys.JOURNAL_ARTICLE_DISPLAY);

boolean print = ParamUtil.getString(request, "viewMode").equals(Constants.PRINT);

boolean hasViewPermission = true;

String title = StringPool.BLANK;
boolean approved = false;
boolean expired = true;
%>

<c:choose>
	<c:when test="<%= article == null %>">
		<c:choose>
			<c:when test="<%= Validator.isNull(articleId) %>">

				<%
				renderRequest.setAttribute(WebKeys.PORTLET_CONFIGURATOR_VISIBILITY, Boolean.TRUE);
				%>

				<div class="portlet-msg-info">
					<liferay-ui:message key="select-existing-web-content-or-add-some-web-content-to-be-displayed-in-this-portlet" />
				</div>
			</c:when>
			<c:otherwise>
				<div class="portlet-msg-error">
					<%= LanguageUtil.get(pageContext, "the-selected-web-content-no-longer-exists") %>
				</div>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>

		<%
		hasViewPermission = JournalArticlePermission.contains(permissionChecker, article.getGroupId(), article.getArticleId(), ActionKeys.VIEW);

		title = article.getTitle(locale);
		approved = article.isApproved();
		expired = article.isExpired();

		if (!expired) {
			Date expirationDate = article.getExpirationDate();

			if ((expirationDate != null) && expirationDate.before(new Date())) {
				expired = true;
			}
		}
		%>

		<c:choose>
			<c:when test="<%= (articleDisplay != null) && !expired && hasViewPermission %>">

				<%
				if (enableViewCountIncrement) {
					AssetEntryServiceUtil.incrementViewCounter(JournalArticle.class.getName(), articleDisplay.getResourcePrimKey());
				}

				if (themeDisplay.isStateExclusive()) {
					out.print(RuntimePortletUtil.processXML(application, request, response, renderRequest, renderResponse, articleDisplay.getContent()));

					return;
				}

				PortletURL portletURL = renderResponse.createRenderURL();
				%>

				<c:if test="<%= enableConversions || enablePrint || (showAvailableLocales && (articleDisplay.getAvailableLocales().length > 1)) %>">
					<div class="user-actions">
						<c:if test="<%= enablePrint %>">
							<c:choose>
								<c:when test="<%= print %>">
									<div class="print-action">
										<liferay-ui:icon
											image="print"
											label="<%= true %>"
											message='<%= LanguageUtil.format(pageContext, "print-x-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle()}) %>'
											url="javascript:print();"
										/>
									</div>

									<aui:script>
										print();
									</aui:script>
								</c:when>
								<c:otherwise>

									<%
									PortletURL printPageURL = renderResponse.createRenderURL();

									printPageURL.setParameter("struts_action", "/journal_content/view");
									printPageURL.setParameter("groupId", String.valueOf(articleDisplay.getGroupId()));
									printPageURL.setParameter("articleId", articleDisplay.getArticleId());
									printPageURL.setParameter("viewMode", Constants.PRINT);
									printPageURL.setWindowState(LiferayWindowState.POP_UP);
									%>

									<div class="print-action">
										<liferay-ui:icon
											image="print"
											label="<%= true %>"
											message='<%= LanguageUtil.format(pageContext, "print-x-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle()}) %>'
											url='<%= "javascript:" + renderResponse.getNamespace() + "printPage();" %>'
										/>
									</div>

									<aui:script>
										function <portlet:namespace />printPage() {
											window.open('<%= printPageURL %>', '', "directories=0,height=480,left=80,location=1,menubar=1,resizable=1,scrollbars=yes,status=0,toolbar=0,top=180,width=640");
										}
									</aui:script>
								</c:otherwise>
							</c:choose>
						</c:if>

						<c:if test="<%= enableConversions && !print %>">

							<%
							PortletURL exportArticleURL = renderResponse.createActionURL();

							exportArticleURL.setParameter("struts_action", "/journal_content/export_article");
							exportArticleURL.setParameter("groupId", String.valueOf(articleDisplay.getGroupId()));
							exportArticleURL.setParameter("articleId", articleDisplay.getArticleId());
							exportArticleURL.setWindowState(LiferayWindowState.EXCLUSIVE);
							%>

							<div class="export-actions">
								<liferay-ui:icon-list>

									<%
									for (String extension : extensions) {
										exportArticleURL.setParameter("targetExtension", extension);
									%>

										<liferay-ui:icon
											image='<%= "../file_system/small/" + extension %>'
											label="<%= true %>"
											message='<%= LanguageUtil.format(pageContext, "x-convert-x-to-x", new Object[] {"aui-helper-hidden-accessible", articleDisplay.getTitle(), extension.toUpperCase()}) %>'
											method="get"
											url="<%= exportArticleURL.toString() %>"
										/>

									<%
									}
									%>

								</liferay-ui:icon-list>
							</div>
						</c:if>

						<c:if test="<%= showAvailableLocales && !print %>">

							<%
							String[] availableLocales = articleDisplay.getAvailableLocales();
							%>

							<c:if test="<%= availableLocales.length > 1 %>">
								<c:if test="<%= enableConversions || enablePrint %>">
									<div class="locale-separator"> </div>
								</c:if>

								<div class="locale-actions">
									<liferay-ui:language displayStyle="<%= 0 %>" languageIds="<%= availableLocales %>" />
								</div>
							</c:if>
						</c:if>
					</div>
				</c:if>

				<div class="journal-content-article">
					<%= RuntimePortletUtil.processXML(application, request, response, renderRequest, renderResponse, articleDisplay.getContent()) %>
				</div>

				<c:if test="<%= articleDisplay.isPaginate() %>">
					<liferay-ui:page-iterator
						cur="<%= articleDisplay.getCurrentPage() %>"
						curParam='<%= "page" %>'
						delta="<%= 1 %>"
						id="articleDisplayPages"
						maxPages="<%= 25 %>"
						total="<%= articleDisplay.getNumberOfPages() %>"
						type="article"
						url="<%= portletURL.toString() %>"
					/>

					<br />
				</c:if>
			</c:when>
			<c:otherwise>

				<%
				renderRequest.setAttribute(WebKeys.PORTLET_CONFIGURATOR_VISIBILITY, Boolean.TRUE);
				%>

				<br />

				<c:if test="<%= hasViewPermission %>">
					<c:choose>
						<c:when test="<%= Validator.isNull(articleId) %>">
						</c:when>
						<c:otherwise>

							<%
							if (expired) {
							%>

								<div class="portlet-msg-alert">
									<%= LanguageUtil.format(pageContext, "x-is-expired", title) %>
								</div>

							<%
							}
							else if (!approved) {
							%>

								<c:choose>
									<c:when test="<%= JournalArticlePermission.contains(permissionChecker, article.getGroupId(), article.getArticleId(), ActionKeys.UPDATE) %>">
										<liferay-portlet:renderURL portletName="<%= PortletKeys.JOURNAL %>" var="editURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
											<portlet:param name="struts_action" value="/journal/edit_article" />
											<portlet:param name="redirect" value="<%= currentURL %>" />
											<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
											<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
											<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
										</liferay-portlet:renderURL>

										<div class="portlet-msg-alert">
											<a href="<%= editURL %>">
												<%= LanguageUtil.format(pageContext, "x-is-not-approved", HtmlUtil.escape(title)) %>
											</a>
										</div>
									</c:when>
									<c:otherwise>
										<div class="portlet-msg-alert">
											<%= LanguageUtil.format(pageContext, "x-is-not-approved", HtmlUtil.escape(title)) %>
										</div>
									</c:otherwise>
								</c:choose>

							<%
							}
							%>

						</c:otherwise>
					</c:choose>
				</c:if>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<%
JournalArticle latestArticle = null;

try {
	if (articleDisplay != null) {
		latestArticle = JournalArticleLocalServiceUtil.getLatestArticle(articleDisplay.getGroupId(), articleDisplay.getArticleId(), WorkflowConstants.STATUS_ANY);
	}
}
catch (NoSuchArticleException nsae) {
}

JournalTemplate template = null;

if ((articleDisplay != null) && Validator.isNotNull(articleDisplay.getTemplateId())) {
	template = JournalTemplateLocalServiceUtil.getTemplate(articleDisplay.getGroupId(), articleDisplay.getTemplateId(), true);
}

boolean showEditArticleIcon = (latestArticle != null) && JournalArticlePermission.contains(permissionChecker, latestArticle.getGroupId(), latestArticle.getArticleId(), ActionKeys.UPDATE);
boolean showEditTemplateIcon = (template != null) && JournalTemplatePermission.contains(permissionChecker, template.getGroupId(), template.getTemplateId(), ActionKeys.UPDATE);
boolean showSelectArticleIcon = PortletPermissionUtil.contains(permissionChecker, layout, portletDisplay.getId(), ActionKeys.CONFIGURATION);
boolean showAddArticleIcon = showSelectArticleIcon && JournalPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_ARTICLE);
boolean showIconsActions = themeDisplay.isSignedIn() && !layout.isLayoutPrototypeLinkActive() && (showEditArticleIcon || showEditTemplateIcon || showSelectArticleIcon || showAddArticleIcon);
%>

<c:if test="<%= showIconsActions && !print && hasViewPermission %>">
	<div class="lfr-meta-actions icons-container">
		<div class="icon-actions">
			<c:if test="<%= showEditArticleIcon %>">
				<liferay-portlet:renderURL portletName="<%= PortletKeys.JOURNAL %>" var="editURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
					<portlet:param name="struts_action" value="/journal/edit_article" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="groupId" value="<%= String.valueOf(latestArticle.getGroupId()) %>" />
					<portlet:param name="articleId" value="<%= latestArticle.getArticleId() %>" />
					<portlet:param name="version" value="<%= String.valueOf(latestArticle.getVersion()) %>" />
				</liferay-portlet:renderURL>

				<liferay-ui:icon
					image="edit"
					message="edit-web-content"
					url="<%= editURL %>"
				/>
			</c:if>

			<c:if test="<%= showEditTemplateIcon %>">
				<liferay-portlet:renderURL portletName="<%= PortletKeys.JOURNAL %>" var="editTemplateURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
					<portlet:param name="struts_action" value="/journal/edit_template" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="groupId" value="<%= String.valueOf(template.getGroupId()) %>" />
					<portlet:param name="templateId" value="<%= template.getTemplateId() %>" />
				</liferay-portlet:renderURL>

				<liferay-ui:icon
					image="../file_system/small/xml"
					message="edit-template"
					url="<%= editTemplateURL %>"
				/>
			</c:if>

			<c:if test="<%= showSelectArticleIcon %>">
				<liferay-ui:icon
					cssClass="portlet-configuration"
					image="configuration"
					message="select-web-content"
					method="get"
					onClick="<%= portletDisplay.getURLConfigurationJS() %>"
					url="<%= portletDisplay.getURLConfiguration() %>"
				/>
			</c:if>

			<c:if test="<%= showAddArticleIcon %>">
				<liferay-portlet:renderURL portletName="<%= PortletKeys.JOURNAL %>" var="addArticleURL" windowState="<%= WindowState.MAXIMIZED.toString() %>">
					<portlet:param name="struts_action" value="/journal/edit_article" />
					<portlet:param name="redirect" value="<%= currentURL %>" />
					<portlet:param name="portletResource" value="<%= portletDisplay.getId() %>" />
					<portlet:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
				</liferay-portlet:renderURL>

				<liferay-ui:icon
					image="add_article"
					message="add-web-content"
					url="<%= addArticleURL %>"
				/>
			</c:if>
		</div>
	</div>
</c:if>

<c:if test="<%= (articleDisplay != null) && hasViewPermission %>">
	<c:if test="<%= enableRelatedAssets %>">
		<div class="entry-links">
			<liferay-ui:asset-links
				className="<%= JournalArticle.class.getName() %>"
				classPK="<%= articleDisplay.getResourcePrimKey() %>"
			/>
		</div>
	</c:if>

	<c:if test="<%= enableRatings && !print %>">
		<div class="taglib-ratings-wrapper">
			<liferay-ui:ratings
				className="<%= JournalArticle.class.getName() %>"
				classPK="<%= articleDisplay.getResourcePrimKey() %>"
			/>
		</div>
	</c:if>

	<c:if test="<%= enableComments %>">

		<%
		int discussionMessagesCount = MBMessageLocalServiceUtil.getDiscussionMessagesCount(PortalUtil.getClassNameId(JournalArticle.class.getName()), articleDisplay.getResourcePrimKey(), WorkflowConstants.STATUS_APPROVED);
		%>

		<c:if test="<%= discussionMessagesCount > 0 %>">
			<liferay-ui:header
				title="comments"
			/>
		</c:if>

		<portlet:actionURL var="discussionURL">
			<portlet:param name="struts_action" value="/journal_content/edit_article_discussion" />
		</portlet:actionURL>

		<liferay-ui:discussion
			className="<%= JournalArticle.class.getName() %>"
			classPK="<%= articleDisplay.getResourcePrimKey() %>"
			formAction="<%= discussionURL %>"
			hideControls="<%= print %>"
			ratingsEnabled="<%= enableCommentRatings && !print %>"
			redirect="<%= currentURL %>"
			subject="<%= articleDisplay.getTitle() %>"
			userId="<%= articleDisplay.getUserId() %>"
		/>
	</c:if>
</c:if>