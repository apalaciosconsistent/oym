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

package com.liferay.marketplace.service.impl;

import com.liferay.marketplace.model.App;
import com.liferay.marketplace.service.base.AppServiceBaseImpl;
import com.liferay.marketplace.service.permission.MarketplacePermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import java.io.File;

/**
 * @author Ryan Park
 */
public class AppServiceImpl extends AppServiceBaseImpl {

	public App addApp(long remoteAppId, String version, File file)
		throws PortalException, SystemException {

		MarketplacePermission.check(getPermissionChecker());

		return appLocalService.addApp(getUserId(), remoteAppId, version, file);
	}

	public App deleteApp(long appId) throws PortalException, SystemException {
		MarketplacePermission.check(getPermissionChecker());

		return appLocalService.deleteApp(appId);
	}

	public void installApp(long remoteAppId)
		throws PortalException, SystemException {

		MarketplacePermission.check(getPermissionChecker());

		appLocalService.installApp(remoteAppId);
	}

	public void uninstallApp(long remoteAppId)
		throws PortalException, SystemException {

		MarketplacePermission.check(getPermissionChecker());

		appLocalService.uninstallApp(remoteAppId);
	}

	public App updateApp(long appId, String version, File file)
		throws PortalException, SystemException {

		MarketplacePermission.check(getPermissionChecker());

		return appLocalService.updateApp(appId, version, file);
	}

}