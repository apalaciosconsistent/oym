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

<%@ include file="/html/portal/init.jsp" %>

<%
String uploadProgressId = ParamUtil.getString(request, "uploadProgressId");

String fileName = GetterUtil.getString((String)session.getAttribute(LiferayFileUpload.FILE_NAME + uploadProgressId));

if (fileName == null) {
	fileName = GetterUtil.getString((String)session.getAttribute(LiferayFileUpload.FILE_NAME));
}

Integer percent = (Integer)session.getAttribute(LiferayFileUpload.PERCENT + uploadProgressId);

if (percent == null) {
	percent = (Integer)session.getAttribute(LiferayFileUpload.PERCENT);
}
if (percent == null) {
	percent = new Integer(100);
}

if (percent.floatValue() >= 100) {
	session.removeAttribute(LiferayFileUpload.FILE_NAME);
	session.removeAttribute(LiferayFileUpload.FILE_NAME + uploadProgressId);
	session.removeAttribute(LiferayFileUpload.PERCENT);
	session.removeAttribute(LiferayFileUpload.PERCENT + uploadProgressId);
}
%>

<html>

<body>

<script type="text/javascript">
	;(function() {
		var progressId = parent['<%= HtmlUtil.escapeJS(uploadProgressId) %>'];

		if (progressId && (typeof progressId.updateBar == 'function')) {
			progressId.updateBar(<%= percent.intValue() %>, '<%= HtmlUtil.escapeJS(fileName) %>');
		}

		<c:if test="<%= percent.intValue() < 100 %>">
			setTimeout(window.location.reload, 1000);
		</c:if>
	}());
</script>

</body>

</html>