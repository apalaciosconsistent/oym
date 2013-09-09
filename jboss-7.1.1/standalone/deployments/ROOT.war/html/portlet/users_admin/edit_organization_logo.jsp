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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
long groupId = ParamUtil.getLong(request, "groupId");
long publicLayoutSetId = ParamUtil.getLong(request, "publicLayoutSetId");
%>

<c:choose>
	<c:when test='<%= SessionMessages.contains(renderRequest, "request_processed") %>'>

		<%
		String logoURL = StringPool.BLANK;

		if (publicLayoutSetId != 0) {
			LayoutSet publicLayoutSet = LayoutSetLocalServiceUtil.getLayoutSet(publicLayoutSetId);

			logoURL = themeDisplay.getPathImage() + "/organization_logo?img_id=" + publicLayoutSet.getLogoId() + "&t=" + WebServerServletTokenUtil.getToken(publicLayoutSet.getLogoId());
		}
		%>

		<aui:script>
			window.close();
			opener.<portlet:namespace />changeLogo('<%= logoURL %>');
		</aui:script>
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="editOrganizationLogoURL">
			<portlet:param name="struts_action" value="/users_admin/edit_organization_logo" />
		</portlet:actionURL>

		<aui:form action="<%= editOrganizationLogoURL %>" enctype="multipart/form-data" method="post" name="fm">
			<aui:input name="groupId" type="hidden" value="<%= groupId %>" />
			<aui:input name="publicLayoutSetId" type="hidden" value="<%= publicLayoutSetId %>" />

			<liferay-ui:error exception="<%= ImageTypeException.class %>" message="please-enter-a-file-with-a-valid-file-type" />
			<liferay-ui:error exception="<%= UploadException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />

			<aui:fieldset>
				<aui:input label="upload-a-logo-for-the-organization-pages-that-will-be-used-instead-of-the-default-enterprise-logo-in-both-public-and-private-pages" name="fileName" size="50" type="file" />

				<aui:button-row>
					<aui:button type="submit" />

					<aui:button onClick="window.close();" type="cancel" value="close" />
				</aui:button-row>
			</aui:fieldset>
		</aui:form>

		<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
			<aui:script>
				Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />fileName);
			</aui:script>
		</c:if>
	</c:otherwise>
</c:choose>