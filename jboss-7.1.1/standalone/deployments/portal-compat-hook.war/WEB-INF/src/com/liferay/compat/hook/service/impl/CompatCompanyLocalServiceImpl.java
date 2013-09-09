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
import com.liferay.portal.model.Company;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyLocalServiceWrapper;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.util.ExpandoBridgeFactoryUtil;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatCompanyLocalServiceImpl extends CompanyLocalServiceWrapper {

	public CompatCompanyLocalServiceImpl(
		CompanyLocalService companyLocalService) {

		super(companyLocalService);
	}


	public Company checkCompany(String webId)
		throws PortalException, SystemException {

		Company company = super.checkCompany(webId);

		ExpandoBridge expandoBridge = ExpandoBridgeFactoryUtil.getExpandoBridge(
			company.getCompanyId(), DLFileEntry.class.getName());

		if (!expandoBridge.hasAttribute(DLUtil.MANUAL_CHECK_IN_REQUIRED)) {
			expandoBridge.addAttribute(
				DLUtil.MANUAL_CHECK_IN_REQUIRED, ExpandoColumnConstants.BOOLEAN,
				false);
		}

		return company;
	}

}