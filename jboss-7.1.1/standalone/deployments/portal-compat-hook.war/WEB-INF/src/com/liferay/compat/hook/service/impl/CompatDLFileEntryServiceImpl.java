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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.LockLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryService;
import com.liferay.portlet.documentlibrary.service.DLFileEntryServiceWrapper;

/**
 * @author Jonathan Lee
 */
public class CompatDLFileEntryServiceImpl extends DLFileEntryServiceWrapper {

	public CompatDLFileEntryServiceImpl(DLFileEntryService dlFileEntryService) {
		super(dlFileEntryService);
	}


	public DLFileVersion cancelCheckOut(long fileEntryId)
		throws PortalException, SystemException {

		DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.fetchDLFileEntry(
			fileEntryId);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker.isCompanyAdmin() ||
			permissionChecker.isGroupAdmin(dlFileEntry.getGroupId()) ||
			permissionChecker.isGroupOwner(dlFileEntry.getGroupId())) {

			LockLocalServiceUtil.unlock(
				DLFileEntry.class.getName(), fileEntryId);
		}

		return super.cancelCheckOut(fileEntryId);
	}

}