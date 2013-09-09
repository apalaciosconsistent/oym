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

<%@ include file="/html/portlet/portlet_configuration/init.jsp" %>

<c:choose>
	<c:when test="<%= (PropsValues.PERMISSIONS_USER_CHECK_ALGORITHM == 5) || (PropsValues.PERMISSIONS_USER_CHECK_ALGORITHM == 6) %>">
		<liferay-util:include page="/html/portlet/portlet_configuration/edit_permissions_algorithm_5_to_6.jsp" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/html/portlet/portlet_configuration/edit_permissions_algorithm_1_to_4.jsp" />
	</c:otherwise>
</c:choose>