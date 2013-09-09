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

<%@ include file="/html/portlet/roles_admin/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Object[] objArray = (Object[])row.getObject();

Role role = (Role)objArray[0];
String target = (String)objArray[3];
Boolean supportsFilterByGroup = (Boolean)objArray[5];
List groups = (List)objArray[6];
long[] groupIdsArray = (long[])objArray[7];
List groupNames = (List)objArray[8];
%>

<aui:input name='<%= "groupIds" + target %>' type="hidden" value="<%= StringUtil.merge(groupIdsArray) %>" />
<aui:input name='<%= "groupNames" + target %>' type="hidden" value='<%= StringUtil.merge(groupNames, "@@") %>' />

<div id="<portlet:namespace />groupDiv<%= target %>">
	<span class="permission-scopes" id="<portlet:namespace />groupHTML<%= target %>">

		<%
		if (supportsFilterByGroup && !groups.isEmpty()) {
			for (int i = 0; i < groups.size(); i++) {
				Group group = (Group)groups.get(i);

				String taglibHREF = "javascript:" + renderResponse.getNamespace() + "removeGroup(" + i + ", '" + target + "');";
		%>

				<span class="lfr-token">
					<span class="lfr-token-text"><%= HtmlUtil.escape(group.getDescriptiveName(locale)) %></span>

					<aui:a cssClass="aui-icon aui-icon-close lfr-token-close" href="<%= taglibHREF %>" />
				</span>

		<%
			}
		}
		else if (role.getType() == RoleConstants.TYPE_REGULAR) {
		%>

			<%= LanguageUtil.get(pageContext, "portal") %>

		<%
		}
		%>

	</span>
</div>