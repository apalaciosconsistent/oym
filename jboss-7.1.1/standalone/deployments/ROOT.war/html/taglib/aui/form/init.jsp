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

@generated
--%>

<%@ include file="/html/taglib/taglib-init.jsp" %>

<%
Map<String, Object> dynamicAttributes = (Map<String, Object>)request.getAttribute("aui:form:dynamicAttributes");
Map<String, Object> scopedAttributes = (Map<String, Object>)request.getAttribute("aui:form:scopedAttributes");
CustomAttributes customAttributes = (CustomAttributes)request.getAttribute("aui:form:customAttributes");

Map<String, Object> _options = new HashMap<String, Object>();

if ((scopedAttributes != null) && !scopedAttributes.isEmpty()) {
	_options.putAll(scopedAttributes);
}

if ((dynamicAttributes != null) && !dynamicAttributes.isEmpty()) {
	_options.putAll(dynamicAttributes);
}

java.lang.String action = GetterUtil.getString((java.lang.String)request.getAttribute("aui:form:action"));
java.lang.String cssClass = GetterUtil.getString((java.lang.String)request.getAttribute("aui:form:cssClass"));
boolean escapeXml = GetterUtil.getBoolean(String.valueOf(request.getAttribute("aui:form:escapeXml")), true);
boolean inlineLabels = GetterUtil.getBoolean(String.valueOf(request.getAttribute("aui:form:inlineLabels")));
java.lang.String method = GetterUtil.getString((java.lang.String)request.getAttribute("aui:form:method"), "post");
java.lang.String name = GetterUtil.getString((java.lang.String)request.getAttribute("aui:form:name"), "fm");
java.lang.String onSubmit = GetterUtil.getString((java.lang.String)request.getAttribute("aui:form:onSubmit"));
boolean useNamespace = GetterUtil.getBoolean(String.valueOf(request.getAttribute("aui:form:useNamespace")), true);

_updateOptions(_options, "action", action);
_updateOptions(_options, "cssClass", cssClass);
_updateOptions(_options, "escapeXml", escapeXml);
_updateOptions(_options, "inlineLabels", inlineLabels);
_updateOptions(_options, "method", method);
_updateOptions(_options, "name", name);
_updateOptions(_options, "onSubmit", onSubmit);
_updateOptions(_options, "useNamespace", useNamespace);
%>

<%@ include file="/html/taglib/aui/form/init-ext.jspf" %>

<%!
private static final String _NAMESPACE = "aui:form:";
%>