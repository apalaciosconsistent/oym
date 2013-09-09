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

package com.liferay.compat.hook.repository.cmis;

import com.liferay.compat.portlet.documentlibrary.util.DLUtil;
import com.liferay.portal.NoSuchRepositoryEntryException;
import com.liferay.portal.kernel.bean.ClassLoaderBeanHandler;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.RepositoryEntry;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.persistence.RepositoryEntryUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatCMISRepositoryInvocationHandler
	extends ClassLoaderBeanHandler {

	public CompatCMISRepositoryInvocationHandler(
		Object bean, ClassLoader classLoader) {

		super(bean, classLoader);
	}


	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		try {
			String methodName = method.getName();

			if (methodName.equals("checkInFileEntry") &&
				(arguments.length == 4)) {

				clearManualCheckInRequired(
					(Long)arguments[0], (ServiceContext)arguments[3]);
			}
			else if (methodName.equals("checkOutFileEntry") &&
					 (arguments.length == 2)) {

				setManualCheckInRequired(
					(Long)arguments[0], (ServiceContext)arguments[1]);
			}

			return method.invoke(super.getBean(), arguments);
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	protected void clearManualCheckInRequired(
			long fileEntryId, ServiceContext serviceContext)
		throws NoSuchRepositoryEntryException, SystemException {

		boolean webDAVCheckInMode = GetterUtil.getBoolean(
			serviceContext.getAttribute(DLUtil.WEBDAV_CHECK_IN_MODE));

		if (webDAVCheckInMode) {
			return;
		}

		RepositoryEntry repositoryEntry = RepositoryEntryUtil.findByPrimaryKey(
			fileEntryId);

		ExpandoBridge expandoBridge = repositoryEntry.getExpandoBridge();

		boolean manualCheckInRequired = GetterUtil.getBoolean(
			expandoBridge.getAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED, false));

		if (!manualCheckInRequired) {
			return;
		}

		expandoBridge.setAttribute(
			DLUtil.MANUAL_CHECK_IN_REQUIRED, false, false);

		RepositoryEntryUtil.update(repositoryEntry, false);
	}

	protected void setManualCheckInRequired(
			long fileEntryId, ServiceContext serviceContext)
		throws NoSuchRepositoryEntryException, SystemException {

		boolean manualCheckInRequired = GetterUtil.getBoolean(
			serviceContext.getAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED));

		if (!manualCheckInRequired) {
			return;
		}

		RepositoryEntry repositoryEntry = RepositoryEntryUtil.findByPrimaryKey(
			fileEntryId);

		ExpandoBridge expandoBridge = repositoryEntry.getExpandoBridge();

		expandoBridge.setAttribute(
			DLUtil.MANUAL_CHECK_IN_REQUIRED, false, false);

		RepositoryEntryUtil.update(repositoryEntry, false);
	}

}