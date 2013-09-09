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

package com.liferay.marketplace.util;

import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringPool;

/**
 * @author Ryan Park
 */
public class MarketplaceConstants {

	public static final String MARKETPLACE_URL_LOGOUT =
		"https://mp.liferay.com/c/portal/logout";

	public static String getPathPurchased() {
		if (_pathPurchased == null) {
			_pathPurchased =
				_MARKETPLACE_PATH_PURCHASED + _MARKETPLACE_CLIENT_BUILD +
					StringPool.SLASH;
		}

		return _pathPurchased + ReleaseInfo.getBuildNumber();
	}

	public static String getPathStore() {
		if (_pathStore == null) {
			_pathStore =
				_MARKETPLACE_PATH_STORE + _MARKETPLACE_CLIENT_BUILD +
					StringPool.SLASH;
		}

		return _pathStore + ReleaseInfo.getBuildNumber();
	}

	private static final String _MARKETPLACE_CLIENT_BUILD = "1";

	private static final String _MARKETPLACE_PATH_PURCHASED =
		"/widget/web/guest/mpserver/-/mp_server/purchased/";

	private static final String _MARKETPLACE_PATH_STORE =
		"/widget/web/guest/mpserver/-/mp_server/store/";

	private static String _pathPurchased;
	private static String _pathStore;

}