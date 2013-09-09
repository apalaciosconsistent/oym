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

<%@ include file="/html/portal/api/jsonws/init.jsp" %>

<style>
	<%@ include file="/html/portal/api/jsonws/css.jspf" %>
</style>

<div id="wrapper">
	<header id="banner" role="banner">
		<hgroup id="heading">
			<h1 class="site-title">
				<a class="logo" href="<%= contextPath %>/api/jsonws" title="JSONWS API">
					<img alt="JSONWS API" height="<%= themeDisplay.getCompanyLogoHeight() %>" src="<%= HtmlUtil.escape(themeDisplay.getCompanyLogo()) %>" width="<%= themeDisplay.getCompanyLogoWidth() %>" />
				</a>

				<span class="site-name">
					JSONWS API
				</span>
			</h1>
		</hgroup>
	</header>

	<div id="content">
		<div id="main-content">
			<aui:layout>
				<aui:column columnWidth="25" cssClass="lfr-api-navigation">
					<liferay-util:include page="/html/portal/api/jsonws/actions.jsp" />
				</aui:column>
				<aui:column columnWidth="75" cssClass="lfr-api-details">
					<liferay-util:include page="/html/portal/api/jsonws/action.jsp" />
				</aui:column>
			</aui:layout>
		</div>
	</div>

	<footer id="footer" role="contentinfo">
		<p class="powered-by">
			<liferay-ui:message key="powered-by" /> <a href="http://www.liferay.com" rel="external">Liferay</a>
		</p>
	</footer>
</div>