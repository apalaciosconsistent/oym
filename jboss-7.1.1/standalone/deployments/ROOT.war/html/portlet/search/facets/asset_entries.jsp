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

<%@ include file="/html/portlet/search/facets/init.jsp" %>

<%
int frequencyThreshold = dataJSONObject.getInt("frequencyThreshold");

String[] values = new String[0];

if (dataJSONObject.has("values")) {
	JSONArray valuesJSONArray = dataJSONObject.getJSONArray("values");

	values = new String[valuesJSONArray.length()];

	for (int i = 0; i < valuesJSONArray.length(); i++) {
		values[i] = valuesJSONArray.getString(i);
	}
}
%>

<div class="<%= cssClass %>" data-facetFieldName="<%= facet.getFieldName() %>" id="<%= randomNamespace %>facet">
	<aui:input name="<%= facet.getFieldName() %>" type="hidden" value="<%= fieldParam %>" />

	<ul class="asset-type lfr-component">
		<li class="facet-value default <%= Validator.isNull(fieldParam) ? "current-term" : StringPool.BLANK %>">
			<a data-value="" href="javascript:;"><img alt="" src="<%= themeDisplay.getPathThemeImages() %>/common/search.png" /><liferay-ui:message key="everything" /></a>
		</li>

		<%
		List<String> assetTypes = new SortedArrayList<String>(new ModelResourceComparator(locale));

		for (String className : values) {
			if (assetTypes.contains(className)) {
				continue;
			}

			if (!ArrayUtil.contains(values, className)) {
				continue;
			}

			assetTypes.add(className);
		}

		for (String assetType : assetTypes) {
			TermCollector termCollector = facetCollector.getTermCollector(assetType);
		%>

			<c:if test="<%= fieldParam.equals(termCollector.getTerm()) %>">
				<aui:script use="liferay-token-list">
					Liferay.Search.tokenList.add(
						{
							clearFields: '<%= renderResponse.getNamespace() + facet.getFieldName() %>',
							text: '<%= HtmlUtil.escapeJS(ResourceActionsUtil.getModelResource(locale, assetType)) %>'
						}
					);
				</aui:script>
			</c:if>

		<%
			int frequency = 0;

			if (termCollector != null) {
				frequency = termCollector.getFrequency();
			}

			if (frequencyThreshold > frequency) {
				continue;
			}

			AssetRendererFactory assetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(assetType);
		%>

			<li class="facet-value <%= fieldParam.equals(termCollector.getTerm()) ? "current-term" : StringPool.BLANK %>">
				<a data-value="<%= HtmlUtil.escapeAttribute(assetType) %>" href="javascript:;"><c:if test="<%= assetRendererFactory != null %>"><img alt="" src="<%= assetRendererFactory.getIconPath(renderRequest) %>" /></c:if><%= ResourceActionsUtil.getModelResource(locale, assetType) %></a> <span class="frequency">(<%= frequency %>)</span>
			</li>

		<%
		}
		%>

	</ul>
</div>