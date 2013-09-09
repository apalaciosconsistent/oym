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

<%@ include file="/html/portlet/shopping/init.jsp" %>

<%
OrderSearch searchContainer = (OrderSearch)request.getAttribute("liferay-ui:search:searchContainer");

OrderDisplayTerms displayTerms = (OrderDisplayTerms)searchContainer.getDisplayTerms();
%>

<aui:fieldset>
	<aui:column>
		<aui:input name="<%= displayTerms.NUMBER %>" size="20" type="text" value="<%= displayTerms.getNumber() %>" />

		<aui:select label="" name="<%= displayTerms.AND_OPERATOR %>">
			<aui:option label="and" selected="<%= displayTerms.isAndOperator() %>" value="1" />
			<aui:option label="or" selected="<%= !displayTerms.isAndOperator() %>" value="0" />
		</aui:select>
	</aui:column>

	<aui:column>
		<aui:select name="<%= displayTerms.STATUS %>" showEmptyOption="<%= true %>">

			<%
			for (int i = 0; i < ShoppingOrderConstants.STATUSES.length; i++) {
			%>

				<aui:option label="<%= ShoppingOrderConstants.STATUSES[i] %>" selected="<%= displayTerms.getStatus().equals(ShoppingOrderConstants.STATUSES[i]) %>" />

			<%
			}
			%>

		</aui:select>
	</aui:column>

	<aui:column>
		<aui:input name="<%= displayTerms.FIRST_NAME %>" size="20" type="text" value="<%= displayTerms.getFirstName() %>" />
	</aui:column>

	<aui:column>
		<aui:input name="<%= displayTerms.LAST_NAME %>" size="20" type="text" value="<%= displayTerms.getLastName() %>" />
	</aui:column>

	<aui:column>
		<aui:input name="<%= displayTerms.EMAIL_ADDRESS %>" size="20" type="text" value="<%= displayTerms.getEmailAddress() %>" />
	</aui:column>
</aui:fieldset>

<aui:button-row>
	<aui:button type="submit" value="search" />
</aui:button-row>

<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
	<aui:script>
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace /><%= displayTerms.NUMBER %>);
	</aui:script>
</c:if>