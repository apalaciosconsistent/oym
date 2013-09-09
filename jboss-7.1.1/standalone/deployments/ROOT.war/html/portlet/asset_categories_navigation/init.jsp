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

<%@ include file="/html/portlet/init.jsp" %>

<%@ page import="com.liferay.portlet.asset.NoSuchVocabularyException" %><%@
page import="com.liferay.portlet.asset.model.AssetVocabulary" %><%@
page import="com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil" %><%@
page import="com.liferay.portlet.asset.service.AssetVocabularyServiceUtil" %>

<%
PortletPreferences preferences = portletPreferences;

List<AssetVocabulary> vocabularies = AssetVocabularyServiceUtil.getGroupsVocabularies(new long[] {scopeGroupId, themeDisplay.getCompanyGroupId()});

long[] availableAssetVocabularyIds = new long[vocabularies.size()];

for (int i = 0; i < vocabularies.size(); i++) {
	AssetVocabulary vocabulary = vocabularies.get(i);

	availableAssetVocabularyIds[i] = vocabulary.getVocabularyId();
}

boolean allAssetVocabularies = GetterUtil.getBoolean(preferences.getValue("allAssetVocabularies", Boolean.TRUE.toString()));

long[] assetVocabularyIds = availableAssetVocabularyIds;

if (!allAssetVocabularies && (preferences.getValues("assetVocabularyIds", null) != null)) {
	assetVocabularyIds = StringUtil.split(preferences.getValue("assetVocabularyIds", null), 0L);
}
%>

<%@ include file="/html/portlet/asset_categories_navigation/init-ext.jsp" %>