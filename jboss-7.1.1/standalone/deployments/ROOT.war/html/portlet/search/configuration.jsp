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

<%@ include file="/html/portlet/search/init.jsp" %>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />
<liferay-portlet:renderURL portletConfiguration="true" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<aui:fieldset label="display-settings">
		<div class="configuration-style" id="<portlet:namespace />configurationStyle">
			<aui:field-wrapper>
				<aui:input checked="<%= !advancedConfiguration %>" id="basic" label="basic" name="preferences--advancedConfiguration--" type="radio" value="false" />
				<aui:input checked="<%= advancedConfiguration %>" id="advanced" label="advanced" name="preferences--advancedConfiguration--" type="radio" value="true" />
			</aui:field-wrapper>
		</div>

		<div class="basic-configuration <%= (advancedConfiguration ? "aui-helper-hidden" : "") %>" id="<portlet:namespace />basicConfiguration">
			<aui:input name="preferences--displayAssetTypeFacet--" type="checkbox" value="<%= displayAssetTypeFacet %>" />

			<aui:input name="preferences--displayAssetTagsFacet--" type="checkbox" value="<%= displayAssetTagsFacet %>" />

			<aui:input name="preferences--displayAssetCategoriesFacet--" type="checkbox" value="<%= displayAssetCategoriesFacet %>" />

			<aui:input name="preferences--displayModifiedRangeFacet--" type="checkbox" value="<%= displayModifiedRangeFacet %>" />
		</div>

		<div class="advanced-configuration <%= (!advancedConfiguration ? "aui-helper-hidden" : "") %>" id="<portlet:namespace />advancedConfiguration">

			<%
			JSONObject searchConfigurationJSONObject = JSONFactoryUtil.createJSONObject(searchConfiguration);
			%>

			<aui:input helpMessage="search-configuration-help" inputCssClass="search-configuration-text" name="preferences--searchConfiguration--" type="textarea" value="<%= searchConfigurationJSONObject.toString(4) %>" />
		</div>
	</aui:fieldset>

	<br />

	<aui:fieldset label="other-settings">
		<c:if test="<%= permissionChecker.isCompanyAdmin() %>">
			<aui:input helpMessage="display-results-in-document-form-help" name="preferences--displayResultsInDocumentForm--" type="checkbox" value="<%= displayResultsInDocumentForm %>" />
		</c:if>

		<aui:input name="preferences--viewInContext--" type="checkbox" value="<%= viewInContext %>" />

		<aui:input helpMessage="display-main-query-help" name="preferences--displayMainQuery--" type="checkbox" value="<%= displayMainQuery %>" />

		<aui:input helpMessage="display-open-search-results-help" name="preferences--displayOpenSearchResults--" type="checkbox" value="<%= displayOpenSearchResults %>" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-base">
	var basicConfiguration = A.one('#<portlet:namespace />basicConfiguration');
	var advancedConfiguration = A.one('#<portlet:namespace />advancedConfiguration');

	var configurationStyles = A.all('#<portlet:namespace />configurationStyle input');

	configurationStyles.on(
		'change',
		function(event) {
			var value = event.currentTarget.val();

			basicConfiguration.toggle(value != 'true');

			advancedConfiguration.toggle(value == 'true');
		}
	);
</aui:script>