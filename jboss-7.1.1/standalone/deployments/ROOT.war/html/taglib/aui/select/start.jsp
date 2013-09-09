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

<%@ include file="/html/taglib/aui/select/init.jsp" %>

<span class="<%= fieldCss %>">
	<span class="aui-field-content">
		<c:if test='<%= Validator.isNotNull(label) && !inlineLabel.equals("right") %>'>
			<label <%= AUIUtil.buildLabel(inlineLabel, true, namespace + id, false) %>>
				<liferay-ui:message key="<%= label %>" />

				<c:if test="<%= Validator.isNotNull(helpMessage) %>">
					<liferay-ui:icon-help message="<%= helpMessage %>" />
				</c:if>

				<c:if test="<%= changesContext %>">
					<span class="aui-helper-hidden-accessible">(<liferay-ui:message key="changing-the-value-of-this-field-will-reload-the-page" />)</span>
				</c:if>
			</label>
		</c:if>

		<c:if test="<%= Validator.isNotNull(prefix) %>">
			<span class="aui-prefix">
				<liferay-ui:message key="<%= prefix %>" />
			</span>
		</c:if>

		<span class='aui-field-element <%= Validator.isNotNull(label) && inlineLabel.equals("right") ? "aui-field-label-right" : StringPool.BLANK %>'>
			<select class="<%= inputCss %>" <%= disabled ? "disabled" : StringPool.BLANK %> id="<%= namespace + id %>" <%= multiple ? "multiple" : StringPool.BLANK %> name="<%= namespace + name %>" <%= Validator.isNotNull(onChange) ? "onChange=\"" + onChange + "\"" : StringPool.BLANK %> <%= Validator.isNotNull(onClick) ? "onClick=\"" + onClick + "\"" : StringPool.BLANK %> <%= Validator.isNotNull(title) ? "title=\"" + title + "\"" : StringPool.BLANK %> <%= AUIUtil.buildData(data) %> <%= InlineUtil.buildDynamicAttributes(dynamicAttributes) %>>
				<c:if test="<%= showEmptyOption %>">
					<aui:option value="<%= Validator.isNotNull(listType) ? 0 : StringPool.BLANK %>" />
				</c:if>

				<c:if test="<%= Validator.isNotNull(listType) %>">

					<%
					int listTypeId = ParamUtil.getInteger(request, name, BeanParamUtil.getInteger(bean, request, listTypeFieldName));

					List<ListType> listTypeModels = ListTypeServiceUtil.getListTypes(listType);

					for (ListType listTypeModel : listTypeModels) {
					%>

						<aui:option selected="<%= listTypeId == listTypeModel.getListTypeId() %>" value="<%= listTypeModel.getListTypeId() %>"><liferay-ui:message key="<%= listTypeModel.getName() %>" /></aui:option>

					<%
					}
					%>

				</c:if>