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

package com.liferay.compat.hook.filter;

import com.liferay.portal.kernel.util.InitialThreadLocal;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatWebDAVThreadLocal {

	public static boolean isManualCheckInRequired() {
		return _manualCheckInRequired.get();
	}

	public static void setManualCheckInRequired(boolean manualCheckInRequired) {
		_manualCheckInRequired.set(manualCheckInRequired);
	}

	private static ThreadLocal<Boolean> _manualCheckInRequired =
		new InitialThreadLocal<Boolean>(
			CompatWebDAVThreadLocal.class + "._manualCheckInRequired", false);

}