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

<%@ include file="/html/portlet/sites_admin/init.jsp" %>

<%
Long liveGroupId = (Long)request.getAttribute("site.liveGroupId");

LayoutSet publicLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(liveGroupId, false);

String publicVirtualHostName = publicLayoutSet.getVirtualHostname();

if (Validator.isNull(publicVirtualHostName) && Validator.isNotNull(PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME) ) {
	Group defaultGroup = GroupLocalServiceUtil.getGroup(company.getCompanyId(), PropsValues.VIRTUAL_HOSTS_DEFAULT_SITE_NAME);

	if (publicLayoutSet.getGroupId() == defaultGroup.getGroupId()) {
		publicVirtualHostName = company.getVirtualHostname();
	}
}

String defaultPublicRobots = RobotsUtil.getRobots(publicLayoutSet);

String publicRobots = ParamUtil.getString(request, "robots", defaultPublicRobots);

LayoutSet privateLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(liveGroupId, true);

String defaultPrivateRobots = RobotsUtil.getRobots(privateLayoutSet);

String privateRobots = ParamUtil.getString(request, "robots", defaultPrivateRobots);
%>

<liferay-ui:error-marker key="errorSection" value="robots" />

<aui:fieldset label="public-pages">
	<c:choose>
		<c:when test="<%= Validator.isNotNull(publicVirtualHostName) %>">
			<textarea cols="60" name="<portlet:namespace />publicRobots" rows="15"><%= HtmlUtil.escape(publicRobots) %></textarea>
		</c:when>
		<c:otherwise>
			<div class="portlet-msg-info">
				<liferay-ui:message key="please-set-the-virtual-host-before-you-set-the-robots-txt" />
			</div>
		</c:otherwise>
	</c:choose>
</aui:fieldset>

<aui:fieldset label="private-pages">
	<c:choose>
		<c:when test="<%= Validator.isNotNull(privateLayoutSet.getVirtualHostname()) %>">
			<textarea cols="60" name="<portlet:namespace />privateRobots" rows="15"><%= HtmlUtil.escape(privateRobots) %></textarea>
		</c:when>
		<c:otherwise>
			<div class="portlet-msg-info">
				<liferay-ui:message key="please-set-the-virtual-host-before-you-set-the-robots-txt" />
			</div>
		</c:otherwise>
	</c:choose>
</aui:fieldset>