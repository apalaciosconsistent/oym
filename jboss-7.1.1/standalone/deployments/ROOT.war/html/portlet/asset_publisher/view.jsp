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

<%@ include file="/html/portlet/asset_publisher/init.jsp" %>

<%
if (mergeUrlTags || mergeLayoutTags) {
	String[] compilerTagNames = new String[0];

	if (mergeUrlTags) {
		compilerTagNames = (String[])request.getAttribute(WebKeys.TAGS_COMPILER_ENTRIES);
	}

	if (mergeLayoutTags) {
		Set<String> layoutTagNames = AssetUtil.getLayoutTagNames(request);

		if (!layoutTagNames.isEmpty()) {
			compilerTagNames = ArrayUtil.append(compilerTagNames, layoutTagNames.toArray(new String[layoutTagNames.size()]));
		}
	}

	String titleEntry = null;

	if ((compilerTagNames != null) && (compilerTagNames.length > 0)) {
		String[] newAssetTagNames = ArrayUtil.append(allAssetTagNames, compilerTagNames);

		allAssetTagNames = ArrayUtil.distinct(newAssetTagNames, new StringComparator());

		long[] allAssetTagIds = AssetTagLocalServiceUtil.getTagIds(scopeGroupId, allAssetTagNames);

		assetEntryQuery.setAllTagIds(allAssetTagIds);

		titleEntry = compilerTagNames[compilerTagNames.length - 1];
	}

	String portletTitle = HtmlUtil.unescape(portletDisplay.getTitle());

	portletTitle = AssetUtil.substituteTagPropertyVariables(scopeGroupId, titleEntry, portletTitle);

	renderResponse.setTitle(portletTitle);
}
else {
	allAssetTagNames = ArrayUtil.distinct(allAssetTagNames, new StringComparator());
}

for (String curAssetTagName : allAssetTagNames) {
	try {
		AssetTag assetTag = AssetTagLocalServiceUtil.getTag(scopeGroupId, curAssetTagName);

		AssetTagProperty journalTemplateIdProperty = AssetTagPropertyLocalServiceUtil.getTagProperty(assetTag.getTagId(), "journal-template-id");

		String journalTemplateId = journalTemplateIdProperty.getValue();

		request.setAttribute(WebKeys.JOURNAL_TEMPLATE_ID, journalTemplateId);

		break;
	}
	catch (NoSuchTagException nste) {
	}
	catch (NoSuchTagPropertyException nstpe) {
	}
}

if (enableTagBasedNavigation && selectionStyle.equals("manual") && ((assetEntryQuery.getAllCategoryIds().length > 0) || (assetEntryQuery.getAllTagIds().length > 0))) {
	selectionStyle = "dynamic";
}

Group scopeGroup = themeDisplay.getScopeGroup();
%>

<c:if test="<%= (scopeGroup != null) && (!scopeGroup.hasStagingGroup() || scopeGroup.isStagingGroup()) && !portletName.equals(PortletKeys.RELATED_ASSETS) %>">

	<%
	for (long groupId : groupIds) {
	%>

		<div class="lfr-meta-actions add-asset-selector">
			<%@ include file="/html/portlet/asset_publisher/add_asset.jspf" %>
		</div>

	<%
	}
	%>

</c:if>

<%
PortletURL portletURL = renderResponse.createRenderURL();

SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, delta, portletURL, null, null);

if (!paginationType.equals("none")) {
	searchContainer.setDelta(delta);
	searchContainer.setDeltaConfigurable(false);
}
%>

<c:if test="<%= showMetadataDescriptions %>">
	<liferay-ui:categorization-filter
		assetType="content"
		portletURL="<%= portletURL %>"
	/>
</c:if>

<c:choose>
	<c:when test='<%= selectionStyle.equals("dynamic") %>'>
		<%@ include file="/html/portlet/asset_publisher/view_dynamic_list.jspf" %>
	</c:when>
	<c:when test='<%= selectionStyle.equals("manual") %>'>
		<%@ include file="/html/portlet/asset_publisher/view_manual.jspf" %>
	</c:when>
</c:choose>

<c:if test='<%= !paginationType.equals("none") && (searchContainer.getTotal() > searchContainer.getResults().size()) %>'>
	<liferay-ui:search-paginator searchContainer="<%= searchContainer %>" type="<%= paginationType %>" />
</c:if>

<c:if test="<%= enableRSS %>">
	<portlet:resourceURL var="rssURL">
		<portlet:param name="struts_action" value="/asset_publisher/rss" />
	</portlet:resourceURL>

	<div class="subscribe">
		<liferay-ui:icon
			image="rss"
			label="<%= true %>"
			method="get"
			target="_blank"
			url="<%= rssURL %>"
		/>
	</div>

	<liferay-util:html-top>
		<link href="<%= HtmlUtil.escape(rssURL) %>" rel="alternate" title="RSS" type="application/rss+xml" />
	</liferay-util:html-top>
</c:if>

<%!
private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.asset_publisher.view_jsp");
%>