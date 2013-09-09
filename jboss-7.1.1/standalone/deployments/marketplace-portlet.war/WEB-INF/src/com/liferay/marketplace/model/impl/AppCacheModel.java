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

package com.liferay.marketplace.model.impl;

import com.liferay.marketplace.model.App;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import java.io.Serializable;

import java.util.Date;

/**
 * The cache model class for representing App in entity cache.
 *
 * @author Ryan Park
 * @see App
 * @generated
 */
public class AppCacheModel implements CacheModel<App>, Serializable {

	public String toString() {
		StringBundler sb = new StringBundler(19);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", appId=");
		sb.append(appId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", remoteAppId=");
		sb.append(remoteAppId);
		sb.append(", version=");
		sb.append(version);
		sb.append("}");

		return sb.toString();
	}

	public App toEntityModel() {
		AppImpl appImpl = new AppImpl();

		if (uuid == null) {
			appImpl.setUuid(StringPool.BLANK);
		}
		else {
			appImpl.setUuid(uuid);
		}

		appImpl.setAppId(appId);
		appImpl.setCompanyId(companyId);
		appImpl.setUserId(userId);

		if (userName == null) {
			appImpl.setUserName(StringPool.BLANK);
		}
		else {
			appImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			appImpl.setCreateDate(null);
		}
		else {
			appImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			appImpl.setModifiedDate(null);
		}
		else {
			appImpl.setModifiedDate(new Date(modifiedDate));
		}

		appImpl.setRemoteAppId(remoteAppId);

		if (version == null) {
			appImpl.setVersion(StringPool.BLANK);
		}
		else {
			appImpl.setVersion(version);
		}

		appImpl.resetOriginalValues();

		return appImpl;
	}

	public String uuid;
	public long appId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public long remoteAppId;
	public String version;
}