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

<%@ include file="/html/taglib/init.jsp" %>

<c:if test="<%= layout != null %>">

	<%
	String currentLanguageId = LanguageUtil.getLanguageId(request);
	Locale defaultLocale = LocaleUtil.getDefault();
	String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

	String w3cCurrentLanguageId = LocaleUtil.toW3cLanguageId(currentLanguageId);
	String w3cDefaultLanguageId = LocaleUtil.toW3cLanguageId(defaultLanguageId);

	String metaRobots = layout.getRobots(locale, false);
	String metaRobotsLanguageId = w3cCurrentLanguageId;

	if (Validator.isNull(metaRobots)) {
		metaRobots = layout.getRobots(defaultLocale);
		metaRobotsLanguageId = w3cDefaultLanguageId;
	}
	%>

	<c:if test="<%= Validator.isNotNull(metaRobots) %>">
		<meta content="<%= HtmlUtil.escape(metaRobots) %>" lang="<%= metaRobotsLanguageId %>" name="robots" />
	</c:if>

	<%
	String metaDescription = layout.getDescription(locale, false);
	String metaDescriptionLanguageId = w3cCurrentLanguageId;

	if (Validator.isNull(metaDescription)) {
		metaDescription = layout.getDescription(defaultLocale);
		metaDescriptionLanguageId = w3cDefaultLanguageId;
	}

	String dynamicMetaDescription = (String)request.getAttribute(WebKeys.PAGE_DESCRIPTION);

	if (Validator.isNotNull(dynamicMetaDescription)) {
		if (Validator.isNotNull(metaDescription)) {
			StringBundler sb = new StringBundler(4);

			sb.append(dynamicMetaDescription);
			sb.append(StringPool.PERIOD);
			sb.append(StringPool.SPACE);
			sb.append(metaDescription);

			metaDescription = sb.toString();
		}
		else {
			metaDescription = dynamicMetaDescription;
		}
	}
	%>

	<c:if test="<%= Validator.isNotNull(metaDescription) %>">
		<meta content="<%= HtmlUtil.escape(metaDescription) %>" lang="<%= metaDescriptionLanguageId %>" name="description" />
	</c:if>

	<%
	String metaKeywords = layout.getKeywords(locale, false);
	String metaKeywordsLanguageId = w3cCurrentLanguageId;

	if (Validator.isNull(metaKeywords)) {
		metaKeywords = layout.getKeywords(defaultLocale);
		metaKeywordsLanguageId = w3cDefaultLanguageId;
	}

	List<String> dynamicMetaKeywords = (List<String>)request.getAttribute(WebKeys.PAGE_KEYWORDS);

	if (dynamicMetaKeywords != null) {
		if (Validator.isNotNull(metaKeywords)) {
			StringBundler sb = new StringBundler(4);

			sb.append(StringUtil.merge(dynamicMetaKeywords));
			sb.append(StringPool.COMMA);
			sb.append(StringPool.SPACE);
			sb.append(metaKeywords);

			metaKeywords = sb.toString();
		}
		else {
			metaKeywords = StringUtil.merge(dynamicMetaKeywords);
		}

	}
	%>

	<c:if test="<%= Validator.isNotNull(metaKeywords) %>">
		<meta content="<%= HtmlUtil.escape(metaKeywords) %>" lang="<%= metaKeywordsLanguageId %>" name="keywords" />
	</c:if>
</c:if>