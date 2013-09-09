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

<%@ include file="/html/portlet/journal/init.jsp" %>

<%
String langType = ParamUtil.getString(request, "langType");

String editorContentInputElement = ParamUtil.getString(request, "editorContentInputElement");
String editorContentOutputElement = ParamUtil.getString(request, "editorContentOutputElement");

String editorType = ParamUtil.getString(request, "editorType");

if (Validator.isNotNull(editorType)) {
	portalPreferences.setValue(PortletKeys.JOURNAL, "editor-type", editorType);
}
else {
	editorType = portalPreferences.getValue(PortletKeys.JOURNAL, "editor-type", "plain");
}

boolean useRichEditor = editorType.equals("rich");

String editorMode = "php";

if (langType.equals("css")) {
	editorMode = "css";
}
else if (langType.equals("xml") || langType.equals("xsl") || langType.equals("xsd")) {
	editorMode = "xml";
}
%>

<aui:form method="post" name="editorForm">
	<aui:fieldset>
		<aui:select name="editorType">
			<aui:option label="plain" value="plain" />
			<aui:option label="rich" selected="<%= useRichEditor %>" value="rich" />
		</aui:select>

		<div class="lfr-plain-editor <%= useRichEditor ? "aui-helper-hidden" : StringPool.BLANK %>" id="<portlet:namespace />plainEditor">
			<aui:input cssClass="lfr-template-editor" inputCssClass="lfr-editor-textarea" label="" name="plainEditorField" onKeyDown="Liferay.Util.checkTab(this); Liferay.Util.disableEsc();" type="textarea" value="" wrap="off" />
		</div>

		<div class="lfr-rich-editor <%= !useRichEditor ? "aui-helper-hidden" : StringPool.BLANK %>" id="<portlet:namespace />richEditor"></div>
	</aui:fieldset>

	<aui:button-row>
		<aui:button name="update-button" value="update" />
		<aui:button type="cancel" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-ace-editor,aui-dialog,aui-io-request">
	var editorType = '<%= HtmlUtil.escapeJS(editorType) %>';

	var openerAUI = Liferay.Util.getOpener().AUI();

	var editorContentInputElement = openerAUI.one('<%= HtmlUtil.escapeJS(editorContentInputElement) %>');
	var editorContentOutputElement = openerAUI.one('<%= HtmlUtil.escapeJS(editorContentOutputElement) %>');

	var plainEditorField = A.one('#<portlet:namespace />plainEditorField');

	var richEditor;

	var prevEditorContent;

	function getEditorContent(type) {
		var content = '';

		if (type == 'plain') {
			content = plainEditorField.val();
		}
		else {
			content = richEditor.getSession().getValue();
		}

		return content;
	}

	function setEditorContent(type, content) {
		if (type == 'plain') {
			plainEditorField.val(content);
		}
		else {
			richEditor.getSession().setValue(content);
		}

		prevEditorContent = content;
	}

	function updateEditorType(event) {
		var oldEditorType = editorType;

		var newEditorType = A.one('#<portlet:namespace />editorType').val();

		var oldEditorContent = getEditorContent(oldEditorType);

		setEditorContent(newEditorType, oldEditorContent);

		var richEditorType = (newEditorType != 'plain');

		A.one('#<portlet:namespace />plainEditor').toggle(!richEditorType);
		A.one('#<portlet:namespace />richEditor').toggle(richEditorType);

		if (richEditorType) {
			richEditor.editor.resize();
		}

		var uri = '<portlet:renderURL><portlet:param name="struts_action" value="/journal/edit_template_xsl" /></portlet:renderURL>&editorType=' + newEditorType;

		A.io.request(uri);

		editorType = newEditorType;
	}

	function updateTemplateXsl() {
		var content = getEditorContent(editorType);

		if (editorContentOutputElement) {
			var editorMode = '<%= editorMode %>';

			var encodedContent = encodeURIComponent(content);

			if (editorMode == 'xml') {
				editorContentOutputElement.val(content);
			}
			else {
				editorContentOutputElement.val(encodedContent);
			}

			var dialog = Liferay.Util.getWindow();

			if (dialog) {
				dialog.close();

				if (content != prevEditorContent) {
					if (editorMode == 'xml') {
						editorContentInputElement.val(content);
					}
					else {
						editorContentInputElement.val(encodedContent);
					}

					dialog.fire('update');
				}
			}
		}
	}

	A.on(
		'domready',
		function(event) {
			richEditor = new A.AceEditor(
				{
					boundingBox: '#<portlet:namespace />richEditor',
					width: '100%',
					height: '400',
					mode: '<%= editorMode %>'
				}
			).render();

			if (editorContentInputElement) {
				setEditorContent(editorType, decodeURIComponent(editorContentInputElement.val()));
			}

			A.one('#<portlet:namespace />editorType').on('change', updateEditorType);
			A.one('#<portlet:namespace />update-button').on('click', updateTemplateXsl);
		},
		'#<portlet:namespace />richEditor'
	);
</aui:script>