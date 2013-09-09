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
if (termCollectors.isEmpty()) {
	return;
}

int frequencyThreshold = dataJSONObject.getInt("frequencyThreshold");
JSONArray rangesJSONArray = dataJSONObject.getJSONArray("ranges");
%>

<div class="<%= cssClass %>" data-facetFieldName="<%= facet.getFieldName() %>" id="<%= randomNamespace %>facet">
	<aui:input name="<%= facet.getFieldName() %>" type="hidden" value="<%= fieldParam %>" />

	<ul class="lfr-component range">
		<li class="facet-value default <%= Validator.isNull(fieldParam) ? "current-term" : StringPool.BLANK %>">
			<a data-value="" href="javascript:;"><liferay-ui:message key="any-range" /></a>
		</li>

		<%
		for (int i = 0; i < rangesJSONArray.length(); i++) {
			JSONObject rangeJSONObject = rangesJSONArray.getJSONObject(i);

			String label = rangeJSONObject.getString("label");
			String range = rangeJSONObject.getString("range");

			TermCollector termCollector = facetCollector.getTermCollector(range);
		%>

			<c:if test="<%= fieldParam.equals(range) %>">
				<aui:script use="liferay-token-list">
					Liferay.Search.tokenList.add(
						{
							clearFields: '<%= renderResponse.getNamespace() + facet.getFieldName() %>',
							text: '<%= UnicodeLanguageUtil.get(pageContext, HtmlUtil.escape(label)) %>'
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
		%>

			<li class="facet-value <%= fieldParam.equals(range) ? "current-term" : StringPool.BLANK %>">
				<a data-value="<%= HtmlUtil.escapeAttribute(range) %>" href="javascript:;"><liferay-ui:message key="<%= label %>" /></a> <span class="frequency">(<%= frequency %>)</span>
			</li>

		<%
		}
		%>

	</ul>
</div>