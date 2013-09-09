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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.marketplace.service.http.AppServiceSoap}.
 *
 * @author    Ryan Park
 * @see       com.liferay.marketplace.service.http.AppServiceSoap
 * @generated
 */
public class AppSoap implements Serializable {
	public static AppSoap toSoapModel(App model) {
		AppSoap soapModel = new AppSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setAppId(model.getAppId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setRemoteAppId(model.getRemoteAppId());
		soapModel.setVersion(model.getVersion());

		return soapModel;
	}

	public static AppSoap[] toSoapModels(App[] models) {
		AppSoap[] soapModels = new AppSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static AppSoap[][] toSoapModels(App[][] models) {
		AppSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new AppSoap[models.length][models[0].length];
		}
		else {
			soapModels = new AppSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static AppSoap[] toSoapModels(List<App> models) {
		List<AppSoap> soapModels = new ArrayList<AppSoap>(models.size());

		for (App model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new AppSoap[soapModels.size()]);
	}

	public AppSoap() {
	}

	public long getPrimaryKey() {
		return _appId;
	}

	public void setPrimaryKey(long pk) {
		setAppId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getAppId() {
		return _appId;
	}

	public void setAppId(long appId) {
		_appId = appId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public long getRemoteAppId() {
		return _remoteAppId;
	}

	public void setRemoteAppId(long remoteAppId) {
		_remoteAppId = remoteAppId;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	private String _uuid;
	private long _appId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private long _remoteAppId;
	private String _version;
}