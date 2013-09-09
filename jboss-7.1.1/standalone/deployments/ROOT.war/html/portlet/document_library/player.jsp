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

<%@ include file="/html/portlet/document_library/init.jsp" %>

<%
boolean supportedAudio = GetterUtil.getBoolean((String)request.getAttribute("view_file_entry.jsp-supportedAudio"));
boolean supportedVideo = GetterUtil.getBoolean((String)request.getAttribute("view_file_entry.jsp-supportedVideo"));

String[] previewFileURLs = (String[])request.getAttribute("view_file_entry.jsp-previewFileURLs");
String videoThumbnailURL = (String)request.getAttribute("view_file_entry.jsp-videoThumbnailURL");

String mp3PreviewFileURL = null;
String mp4PreviewFileURL = null;
String oggPreviewFileURL = null;
String ogvPreviewFileURL = null;

for (String previewFileURL : previewFileURLs) {
	if (previewFileURL.endsWith("mp3")) {
		mp3PreviewFileURL = previewFileURL;
	}
	else if (previewFileURL.endsWith("mp4")) {
		mp4PreviewFileURL = previewFileURL;
	}
	else if (previewFileURL.endsWith("ogg")) {
		oggPreviewFileURL = previewFileURL;
	}
	else if (previewFileURL.endsWith("ogv")) {
		ogvPreviewFileURL = previewFileURL;
	}
}
%>

<c:choose>
	<c:when test="<%= supportedAudio %>">
		<aui:script use="aui-audio,swfdetect">
			var audio = new A.Audio(
				{
					contentBox: '#<portlet:namespace />previewFileContent',
					fixedAttributes: {
						allowfullscreen: 'true',
						wmode: 'opaque'
					}

					<c:if test="<%= Validator.isNotNull(mp3PreviewFileURL) %>">
						, url: '<%= mp3PreviewFileURL %>'
					</c:if>
				}
			);

			<c:if test="<%= Validator.isNotNull(oggPreviewFileURL) %>">
				if (!A.UA.gecko || !A.SWFDetect.isFlashVersionAtLeast(9)) {
					audio.set('oggUrl', '<%= oggPreviewFileURL %>');
				}
			</c:if>

			audio.render();
		</aui:script>
	</c:when>
	<c:when test="<%= supportedVideo %>">
		<aui:script use="aui-base,aui-video">
			new A.Video(
				{
					contentBox: '#<portlet:namespace />previewFileContent',
					fixedAttributes: {
						allowfullscreen: 'true',
						bgColor: '#000000',
						wmode: 'opaque'
					},
					<c:if test="<%= Validator.isNotNull(ogvPreviewFileURL) %>">
						ogvUrl: '<%= ogvPreviewFileURL %>',
					</c:if>

					poster: '<%= videoThumbnailURL %>'

					<c:if test="<%= Validator.isNotNull(mp4PreviewFileURL) %>">
						, url: '<%= mp4PreviewFileURL %>'
					</c:if>
				}
			).render();
		</aui:script>
	</c:when>
</c:choose>