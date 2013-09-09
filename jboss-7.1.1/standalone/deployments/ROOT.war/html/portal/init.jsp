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

<%@ include file="/html/common/init.jsp" %>

<%@ page import="com.liferay.portal.DuplicateUserEmailAddressException" %><%@
page import="com.liferay.portal.LayoutFriendlyURLException" %><%@
page import="com.liferay.portal.LayoutPermissionException" %><%@
page import="com.liferay.portal.NoSuchResourceException" %><%@
page import="com.liferay.portal.PortletActiveException" %><%@
page import="com.liferay.portal.RequiredLayoutException" %><%@
page import="com.liferay.portal.RequiredRoleException" %><%@
page import="com.liferay.portal.ReservedUserEmailAddressException" %><%@
page import="com.liferay.portal.UserActiveException" %><%@
page import="com.liferay.portal.UserEmailAddressException" %><%@
page import="com.liferay.portal.UserLockoutException" %><%@
page import="com.liferay.portal.UserPasswordException" %><%@
page import="com.liferay.portal.UserReminderQueryException" %><%@
page import="com.liferay.portal.kernel.templateparser.TransformException" %><%@
page import="com.liferay.portal.service.permission.OrganizationPermissionUtil" %><%@
page import="com.liferay.portal.struts.PortletRequestProcessor" %><%@
page import="com.liferay.portal.upload.LiferayFileUpload" %><%@
page import="com.liferay.portal.util.LayoutLister" %><%@
page import="com.liferay.portal.util.LayoutView" %><%@
page import="com.liferay.portlet.journalcontent.util.JournalContentUtil" %><%@
page import="com.liferay.portlet.layoutconfiguration.util.RuntimePortletUtil" %><%@
page import="com.liferay.portlet.usersadmin.util.UsersAdminUtil" %>

<%@ page import="org.apache.struts.action.ActionMapping" %><%@
page import="org.apache.struts.taglib.tiles.ComponentConstants" %><%@
page import="org.apache.struts.tiles.ComponentDefinition" %><%@
page import="org.apache.struts.tiles.TilesUtil" %>