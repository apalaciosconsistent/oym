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

<%@ page import="com.liferay.portal.kernel.util.Diff" %>
<%@ page import="com.liferay.portal.kernel.util.DiffResult" %>

<%
String sourceName = (String)request.getAttribute("liferay-ui:diff:sourceName");
String targetName = (String)request.getAttribute("liferay-ui:diff:targetName");
List<DiffResult>[] diffResults = (List<DiffResult>[])request.getAttribute("liferay-ui:diff:diffResults");

List sourceResults = diffResults[0];
List targetResults = diffResults[1];
%>

<c:choose>
	<c:when test="<%= !sourceResults.isEmpty() %>">
		<table class="taglib-search-iterator" id="taglib-diff-results">
		<tr>
			<td>
				<%= sourceName %>
			</td>
			<td>
				<%= targetName %>
			</td>
		</tr>

		<%
		Iterator<DiffResult> sourceItr = sourceResults.iterator();
		Iterator<DiffResult> targetItr = targetResults.iterator();

		while (sourceItr.hasNext()) {
			DiffResult sourceResult = sourceItr.next();
			DiffResult targetResult = targetItr.next();
		%>

			<tr class="portlet-section-header results-header">
				<th>
					<liferay-ui:message key="line" /> <%= sourceResult.getLineNumber() %>
				</th>
				<th>
					<liferay-ui:message key="line" /> <%= targetResult.getLineNumber() %>
				</th>
			</tr>
			<tr>
				<td class="lfr-top" width="50%">
					<table class="taglib-diff-table">

					<%
					Iterator<String> itr = sourceResult.getChangedLines().iterator();

					while (itr.hasNext()) {
						String changedLine = itr.next();
					%>

						<tr class="lfr-top">
							<%= _processColumn(changedLine) %>
						</tr>

					<%
					}
					%>

					</table>
				</td>
				<td class="lfr-top" width="50%">
					<table class="taglib-diff-table">

					<%
					itr = targetResult.getChangedLines().iterator();

					while (itr.hasNext()) {
						String changedLine = itr.next();
					%>

						<tr class="lfr-top">
							<%= _processColumn(changedLine) %>
						</tr>

					<%
					}
					%>

					</table>
				</td>
			</tr>

		<%
		}
		%>

		</table>
	</c:when>
	<c:otherwise>
		<%= LanguageUtil.format(pageContext, "there-are-no-differences-between-x-and-x", new Object[] {sourceName, targetName}) %>
	</c:otherwise>
</c:choose>

<%!
private static String _processColumn(String changedLine) {
	changedLine = changedLine.replaceAll(" ", "&nbsp;");
	changedLine = changedLine.replaceAll("\t", "&nbsp;&nbsp;&nbsp;");

	String column = "<td>" + changedLine + "</td>";

	if (changedLine.equals(StringPool.BLANK)) {
		return "<td>&nbsp;</td>";
	}
	else if (changedLine.equals(Diff.CONTEXT_LINE)) {
		return "<td class=\"taglib-diff-context\">&nbsp;</td>";
	}
	else if (changedLine.equals(Diff.OPEN_INS + Diff.CLOSE_INS)) {
		return "<td class=\"taglib-diff-addedline\">&nbsp;</td>";
	}
	else if (changedLine.equals(Diff.OPEN_DEL + Diff.CLOSE_DEL)) {
		return "<td class=\"taglib-diff-deletedline\">&nbsp;</td>";
	}

	return column;
}
%>