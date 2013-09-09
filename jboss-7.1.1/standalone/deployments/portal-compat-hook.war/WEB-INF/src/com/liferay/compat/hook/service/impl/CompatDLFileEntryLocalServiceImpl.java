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

package com.liferay.compat.hook.service.impl;

import com.liferay.compat.portlet.documentlibrary.util.DLUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalService;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceWrapper;
import com.liferay.portlet.documentlibrary.service.persistence.DLFileEntryUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatDLFileEntryLocalServiceImpl
	extends DLFileEntryLocalServiceWrapper {

	public CompatDLFileEntryLocalServiceImpl(
		DLFileEntryLocalService dlFileEntryLocalService) {

		super(dlFileEntryLocalService);
	}


	public void checkInFileEntry(
			long userId, long fileEntryId, boolean majorVersion,
			String changeLog, ServiceContext serviceContext)
		throws PortalException, SystemException {

		DLFileEntry dlFileEntry = DLFileEntryUtil.findByPrimaryKey(fileEntryId);

		ServiceContext threadLocalServiceContext =
			ServiceContextThreadLocal.getServiceContext();

		boolean webDAVCheckIn = false;

		if (threadLocalServiceContext != null) {
			webDAVCheckIn = GetterUtil.getBoolean(
				serviceContext.getAttribute(DLUtil.WEBDAV_CHECK_IN_MODE));
		}
		else {
			webDAVCheckIn = GetterUtil.getBoolean(
				serviceContext.getAttribute(DLUtil.WEBDAV_CHECK_IN_MODE));
		}

		ExpandoBridge expandoBridge = dlFileEntry.getExpandoBridge();

		boolean manualCheckInRequired = GetterUtil.getBoolean(
			expandoBridge.getAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED, false));

		if (!webDAVCheckIn && manualCheckInRequired) {
			expandoBridge.setAttribute(
				DLUtil.MANUAL_CHECK_IN_REQUIRED, false, false);
		}

		super.checkInFileEntry(
			userId, fileEntryId, majorVersion, changeLog, serviceContext);
	}


	public DLFileEntry checkOutFileEntry(
			long userId, long fileEntryId, String owner, long expirationTime,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		DLFileEntry dlFileEntry = DLFileEntryUtil.findByPrimaryKey(fileEntryId);

		boolean manualCheckinMode = GetterUtil.getBoolean(
			serviceContext.getAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED));

		if (manualCheckinMode) {
			ExpandoBridge expandoBridge = dlFileEntry.getExpandoBridge();

			expandoBridge.setAttribute(
				DLUtil.MANUAL_CHECK_IN_REQUIRED, true, false);
		}

		return super.checkOutFileEntry(
			userId, fileEntryId, owner, expirationTime, serviceContext);
	}

}