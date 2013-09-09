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

package com.liferay.compat.hook.events;

import com.liferay.compat.portlet.documentlibrary.util.DLUtil;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

/**
 * @author Brian Wing Shun Chan
 * @author Jonathan Lee
 */
public class StartupAction extends SimpleAction {


	public void run(String[] ids) throws ActionException {
		try {
			for (String id : ids) {
				doRun(GetterUtil.getLong(id));
			}
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	protected void doRun(long companyId) throws Exception {
		ExpandoBridge expandoBridge =
			ExpandoBridgeFactoryUtil.getExpandoBridge(
				companyId, DLFileEntry.class.getName());

		if (!expandoBridge.hasAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED)) {
			expandoBridge.addAttribute(
				DLUtil.MANUAL_CHECK_IN_REQUIRED, ExpandoColumnConstants.BOOLEAN,
				false);

			UnicodeProperties properties = expandoBridge.getAttributeProperties(
				DLUtil.MANUAL_CHECK_IN_REQUIRED);

			properties.setProperty("hidden", Boolean.toString(true));

			expandoBridge.setAttributeProperties(
				DLUtil.MANUAL_CHECK_IN_REQUIRED, properties, false);
		}
	}

}