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

<%@ include file="/html/portlet/layouts_admin/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Layout curLayout = (Layout)row.getObject();
%>

<div class="layout">

	<%
	String taglibHref = "javascript:Liferay.LayoutExporter.details({toggle: '#" + renderResponse.getNamespace() + "_detail_" + curLayout.getPlid() + "_toggle img', detail: '#_detail_" + curLayout.getPlid() + "'});";
	%>

	<em class='<%= curLayout.getAncestors().isEmpty() ? "aui-helper-hidden" : StringPool.BLANK %>' id="<portlet:namespace /><%= curLayout.getPlid() %>includeAncestor"><liferay-ui:message key="include-ancestor-pages-if-necessary" /></em>

	<em class="aui-helper-hidden" id="<portlet:namespace /><%= curLayout.getPlid() %>deleteLivePage"><liferay-ui:message key="delete-live-page" /></em>

	<em class='<%= curLayout.getChildren().isEmpty() ? "aui-helper-hidden" : StringPool.BLANK %>' id="<portlet:namespace /><%= curLayout.getPlid() %>includeChildren"><liferay-ui:message key="include-all-descendent-pages" /></em>

	<liferay-ui:icon cssClass="nobr" id='<%= "_detail_" + curLayout.getPlid() + "_toggle" %>' image="../arrows/01_plus" label="<%= true %>" message="change" target="_self" toolTip="options" url="<%= taglibHref %>" />
</div>

<div class="aui-helper-hidden export-layout-detail" id="_detail_<%= curLayout.getPlid() %>" style="border-top: 1px solid #CCC; margin-top: 4px; padding-top: 4px; width: 95%;">
	<aui:input label="delete-live-page" name='<%= "delete_" + curLayout.getPlid() %>' type="radio" value="<%= true %>" />

	<aui:input checked="<%= true %>" label="publish" name='<%= "delete_" + curLayout.getPlid() %>' type="radio" value="<%= false %>" />

	<div id="<portlet:namespace />publishChangesOptions_<%= curLayout.getPlid() %>" style="margin-left: 2em;">
		<c:if test="<%= !curLayout.getAncestors().isEmpty() %>">
			<aui:input checked="<%= true %>" disabled="<%= true %>" label="include-ancestor-pages-if-necessary" name='<%= "includeAncestors_" + curLayout.getPlid() %>' type="checkbox" value="1" />
		</c:if>

		<c:if test="<%= !curLayout.getChildren().isEmpty() %>">
			<aui:input checked="<%= true %>" label="include-all-descendent-pages" name='<%= "includeChildren_" + curLayout.getPlid() %>' type="checkbox" value="1" />
		</c:if>
	</div>
</div>

<aui:script use="aui-base">
	var ancestorsMsg = A.one('#<portlet:namespace /><%= curLayout.getPlid() %>includeAncestor');
	var childrenMsg = A.one('#<portlet:namespace /><%= curLayout.getPlid() %>includeChildren');
	var deleteMsg = A.one('#<portlet:namespace /><%= curLayout.getPlid() %>deleteLivePage');
	var publishOptions = A.one('#<portlet:namespace />publishChangesOptions_<%= curLayout.getPlid() %>');

	var ancestorsCheckbox = A.one('#<portlet:namespace />includeAncestors_<%= curLayout.getPlid() %>Checkbox');
	var childrenCheckbox = A.one('#<portlet:namespace />includeChildren_<%= curLayout.getPlid() %>Checkbox');

	var radioButtons = A.all('#_detail_<%= curLayout.getPlid() %> input[type=radio]');

	<c:if test="<%= !curLayout.getAncestors().isEmpty() %>">
		ancestorsCheckbox.on(
			'change',
			function(event) {
				ancestorsMsg.toggle();
			}
		);
	</c:if>

	<c:if test="<%= !curLayout.getChildren().isEmpty() %>">
		childrenCheckbox.on(
			'change',
			function(event) {
				childrenMsg.toggle();
			}
		);
	</c:if>

	radioButtons.on(
		'change',
		function(event) {
			deleteMsg.toggle();

			if (event.currentTarget.get('value') == 'true') {
				childrenMsg.hide();
				ancestorsMsg.hide();
			}
			else {
				if (ancestorsCheckbox && ancestorsCheckbox.get('checked')) {
					ancestorsMsg.show();
				}

				if (childrenCheckbox && childrenCheckbox.get('checked')) {
					childrenMsg.show();
				}
			}

			publishOptions.toggle();
		}
	);
</aui:script>