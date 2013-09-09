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

package com.liferay.marketplace.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link AppService}.
 * </p>
 *
 * @author    Ryan Park
 * @see       AppService
 * @generated
 */
public class AppServiceWrapper implements AppService,
	ServiceWrapper<AppService> {
	public AppServiceWrapper(AppService appService) {
		_appService = appService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _appService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_appService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _appService.invokeMethod(name, parameterTypes, arguments);
	}

	public com.liferay.marketplace.model.App addApp(long remoteAppId,
		java.lang.String version, java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _appService.addApp(remoteAppId, version, file);
	}

	public com.liferay.marketplace.model.App deleteApp(long appId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _appService.deleteApp(appId);
	}

	public void installApp(long remoteAppId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_appService.installApp(remoteAppId);
	}

	public void uninstallApp(long remoteAppId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		_appService.uninstallApp(remoteAppId);
	}

	public com.liferay.marketplace.model.App updateApp(long appId,
		java.lang.String version, java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _appService.updateApp(appId, version, file);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public AppService getWrappedAppService() {
		return _appService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedAppService(AppService appService) {
		_appService = appService;
	}

	public AppService getWrappedService() {
		return _appService;
	}

	public void setWrappedService(AppService appService) {
		_appService = appService;
	}

	private AppService _appService;
}