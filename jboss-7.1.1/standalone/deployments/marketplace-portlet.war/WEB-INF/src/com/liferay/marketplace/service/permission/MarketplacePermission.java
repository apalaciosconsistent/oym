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

package com.liferay.marketplace.service.permission;

import com.liferay.marketplace.util.MarketplaceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author Ryan Park
 */
public class MarketplacePermission {

	public static void check(PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		if (!contains(permissionChecker)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(PermissionChecker permissionChecker)
		throws PortalException, SystemException {

		if (!permissionChecker.isOmniadmin()) {
			return false;
		}

		User user = UserLocalServiceUtil.getUserById(
			permissionChecker.getUserId());

		if (MarketplaceUtil.isMarketplaceAdmin(user)) {
			return true;
		}

		return false;
	}

}