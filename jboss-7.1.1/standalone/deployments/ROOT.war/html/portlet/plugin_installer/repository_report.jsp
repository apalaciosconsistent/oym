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

<%@ include file="/html/portlet/plugin_installer/init.jsp" %>

<c:if test="<%= SessionMessages.contains(renderRequest, WebKeys.PLUGIN_REPOSITORY_REPORT) %>">
	<br />

	<table class="lfr-table">

	<%
	RepositoryReport repositoryReport = (RepositoryReport)SessionMessages.get(renderRequest, WebKeys.PLUGIN_REPOSITORY_REPORT);

	Iterator itr = repositoryReport.getRepositoryURLs().iterator();

	while (itr.hasNext()) {
		String repositoryURL = (String)itr.next();

		String status = repositoryReport.getState(repositoryURL);
	%>

		<tr>
			<td>
				<%= repositoryURL %>
			</td>
			<td>
				<c:choose>
					<c:when test="<%= status.equals(RepositoryReport.SUCCESS) %>">
						<div class="portlet-msg-success">
							<liferay-ui:message key="ok" />
						</div>
					</c:when>
					<c:otherwise>
						<div class="portlet-msg-error">
							<%= status %>
						</div>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>

	<%
	}
	%>

	</table>
</c:if>