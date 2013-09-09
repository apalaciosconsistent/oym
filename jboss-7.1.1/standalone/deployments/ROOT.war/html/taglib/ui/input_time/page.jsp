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
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-time:cssClass"));
String hourParam = namespace + request.getAttribute("liferay-ui:input-time:hourParam");
int hourValue = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:input-time:hourValue"));
boolean hourNullable = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-time:hourNullable"));
String minuteParam = namespace + request.getAttribute("liferay-ui:input-time:minuteParam");
int minuteValue = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:input-time:minuteValue"));
int minuteInterval = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:input-time:minuteInterval"));
boolean minuteNullable = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-time:minuteNullable"));
String amPmParam = namespace + request.getAttribute("liferay-ui:input-time:amPmParam");
int amPmValue = GetterUtil.getInteger((String)request.getAttribute("liferay-ui:input-time:amPmValue"));
boolean amPmNullable = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-time:amPmNullable"));
boolean disabled = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-time:disabled"));

NumberFormat numberFormat = NumberFormat.getInstance(locale);

numberFormat.setMinimumIntegerDigits(2);
%>

<div class="lfr-input-time <%= cssClass %>">
	<select <%= disabled ? "disabled=\"disabled\"" : "" %> name="<%= hourParam %>">
		<c:if test="<%= hourNullable %>">
			<option value=""></option>
		</c:if>

		<%
		for (int i = 0; i < (DateUtil.isFormatAmPm(locale) ? 12 : 24); i++) {
			String hourString = String.valueOf(i);

			if (DateUtil.isFormatAmPm(locale) && (i == 0)) {
				hourString = "12";
			}
		%>

			<option <%= (hourValue == i) ? "selected" : "" %> value="<%= i %>"><%= hourString %></option>

		<%
		}
		%>

	</select>

	<select <%= disabled ? "disabled=\"disabled\"" : "" %> name="<%= minuteParam %>">
		<c:if test="<%= minuteNullable %>">
			<option value=""></option>
		</c:if>

		<%
		for (int i = 0; i < 60; i++) {
			String minute = numberFormat.format(i);
		%>

			<c:if test="<%= (minuteInterval == 0) || ((i % minuteInterval) == 0) %>">
				<option <%= (minuteValue == i) ? "selected" : "" %> value="<%= i %>">:<%= minute %></option>
			</c:if>

		<%
		}
		%>

	</select>

	<c:choose>
		<c:when test="<%= !DateUtil.isFormatAmPm(locale) %>">
			<input name="<%= amPmParam %>" type="hidden" value="<%= Calendar.AM %>" />
		</c:when>
		<c:otherwise>
			<select <%= disabled ? "disabled=\"disabled\"" : "" %> name="<%= amPmParam %>">
				<c:if test="<%= amPmNullable %>">
					<option value=""></option>
				</c:if>

				<option <%= (amPmValue == Calendar.AM) ? "selected" : "" %> value="<%= Calendar.AM %>"><liferay-ui:message key="am" /></option>
				<option <%= (amPmValue == Calendar.PM) ? "selected" : "" %> value="<%= Calendar.PM %>"><liferay-ui:message key="pm" /></option>
			</select>
		</c:otherwise>
	</c:choose>
</div>