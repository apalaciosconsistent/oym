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

<%
boolean accordion = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel-floating-container:accordion"));
String cssClass = (String)request.getAttribute("liferay-ui:panel-floating-container:cssClass");
String id = (String)request.getAttribute("liferay-ui:panel-floating-container:id");
boolean paging = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel-floating-container:paging"));
String pagingElements = (String)request.getAttribute("liferay-ui:panel-floating-container:pagingElements");
boolean persistState = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:panel-floating-container:persistState"));
int resultsPerPage = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:panel-floating-container:resultsPerPage"));
String trigger = (String)request.getAttribute("liferay-ui:panel-floating-container:trigger");
int width = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:panel-floating-container:width"));
%>