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

<%@ include file="/html/portlet/journal/init.jsp" %>

<%
boolean closeDroppableTag = GetterUtil.getBoolean((String)request.getAttribute(WebKeys.JOURNAL_STRUCTURE_CLOSE_DROPPABLE_TAG));
%>

<c:choose>
	<c:when test="<%= closeDroppableTag %>">
		</ul>
	</c:when>
	<c:otherwise>
		</span></li>
	</c:otherwise>
</c:choose>