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

package com.liferay.marketplace.model;

import com.liferay.portal.model.PersistedModel;

/**
 * The extended model interface for the App service. Represents a row in the &quot;Marketplace_App&quot; database table, with each column mapped to a property of this class.
 *
 * @author Ryan Park
 * @see AppModel
 * @see com.liferay.marketplace.model.impl.AppImpl
 * @see com.liferay.marketplace.model.impl.AppModelImpl
 * @generated
 */
public interface App extends AppModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.marketplace.model.impl.AppImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public java.lang.String getFileDir();

	public java.lang.String getFileName();

	public java.lang.String getFilePath();

	public boolean isDownloaded()
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException;

	public boolean isInstalled()
		throws com.liferay.portal.kernel.exception.SystemException;
}