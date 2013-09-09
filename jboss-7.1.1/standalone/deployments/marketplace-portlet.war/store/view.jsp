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

<%@ include file="/init.jsp" %>

<div class="loading-animation">
	<iframe class="aui-helper-hidden-accessible" frameborder="0" id="<portlet:namespace />frame" name="<portlet:namespace />frame" scrolling="no" src="about:blank"></iframe>
</div>

<form action="<%= iFrameURL %>" id="<portlet:namespace />fm" method="post" target="<portlet:namespace />frame">
<input name="referer" type="hidden" value="<%= referer %>" />
<input name="mpClientURL" type="hidden" value="<%= themeDisplay.getPortalURL() + themeDisplay.getURLCurrent() %>" />
</form>

<div class="aui-helper-hidden time-out-message portlet-msg-error">
	<liferay-ui:message key="could-not-connect-to-the-liferay-marketplace" />
</div>

<aui:script use="aui-base,aui-io,liferay-marketplace-messenger">
	var frame = A.one('#<portlet:namespace />frame');

	var timeout = setTimeout(
		function() {
			frame.ancestor().removeClass('loading-animation');
			A.one('.time-out-message').show();
		},
		120000
	);

	Liferay.MarketplaceMessenger.init(
		{
			targetFrame: frame
		}
	);

	Liferay.MarketplaceMessenger.receiveMessage(
		function(event) {
			var response = event.responseData;

			if (!response.cmd) {
				return;
			}

			if (response.cmd == 'init') {
				clearTimeout(timeout);

				frame.removeClass('aui-helper-hidden-accessible');

				frame.ancestor().removeClass('loading-animation');

				Liferay.MarketplaceMessenger.setTargetURI(response.serverURL);

				if (response.height) {
					frame.height(response.height + 50);
				}

				if (response.width) {
					frame.width(response.width);
				}

				Liferay.MarketplaceMessenger.postMessage(
					{
						message: 'success',
						supportsHotDeploy: <%= ServerDetector.isSupportsHotDeploy() %>
					}
				);
			}
			else if (response.cmd == 'goto') {
				var url = null;

				if (response.panel === "control-panel") {
					url = '<%= themeDisplay.getURLControlPanel() %>';
				}
				else {
					url = '<liferay-portlet:renderURL doAsGroupId="<%= themeDisplay.getScopeGroupId() %>" portletName="<%= portletId.equals(PortletKeys.STORE) ? PortletKeys.MY_MARKETPLACE : PortletKeys.STORE %>" windowState="<%= WindowState.MAXIMIZED.toString() %>" />';

					if (response.appId) {
						url = Liferay.Util.addParams('appId=' + response.appId, url);
					}
				}

				window.location = url;
			}
			else if (response.cmd == 'resize') {
				if (response.height) {
					frame.height(response.height + 50);
				}

				if (response.width) {
					frame.width(response.width);
				}
			}
			else {
				A.io.request(
					'<portlet:actionURL />',
					{
						data: response,
						dataType: 'JSON',
						method: 'POST',
						on: {
							success: function(event, id, obj) {
								var response = this.get('responseData');

								Liferay.MarketplaceMessenger.postMessage(response);
							}
						}
					}
				);
			}
		},
		A.Lang.emptyFnTrue
	);

	A.one('#<portlet:namespace />fm').submit();
</aui:script>