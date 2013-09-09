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
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author    Ryan Park
 * @generated
 */
public class ModuleSoap implements Serializable {
	public static ModuleSoap toSoapModel(Module model) {
		ModuleSoap soapModel = new ModuleSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setModuleId(model.getModuleId());
		soapModel.setAppId(model.getAppId());
		soapModel.setContextName(model.getContextName());

		return soapModel;
	}

	public static ModuleSoap[] toSoapModels(Module[] models) {
		ModuleSoap[] soapModels = new ModuleSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ModuleSoap[][] toSoapModels(Module[][] models) {
		ModuleSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ModuleSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ModuleSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ModuleSoap[] toSoapModels(List<Module> models) {
		List<ModuleSoap> soapModels = new ArrayList<ModuleSoap>(models.size());

		for (Module model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ModuleSoap[soapModels.size()]);
	}

	public ModuleSoap() {
	}

	public long getPrimaryKey() {
		return _moduleId;
	}

	public void setPrimaryKey(long pk) {
		setModuleId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getModuleId() {
		return _moduleId;
	}

	public void setModuleId(long moduleId) {
		_moduleId = moduleId;
	}

	public long getAppId() {
		return _appId;
	}

	public void setAppId(long appId) {
		_appId = appId;
	}

	public String getContextName() {
		return _contextName;
	}

	public void setContextName(String contextName) {
		_contextName = contextName;
	}

	private String _uuid;
	private long _moduleId;
	private long _appId;
	private String _contextName;
}