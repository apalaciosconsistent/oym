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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Module}.
 * </p>
 *
 * @author    Ryan Park
 * @see       Module
 * @generated
 */
public class ModuleWrapper implements Module, ModelWrapper<Module> {
	public ModuleWrapper(Module module) {
		_module = module;
	}

	public Class<?> getModelClass() {
		return Module.class;
	}

	public String getModelClassName() {
		return Module.class.getName();
	}

	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("moduleId", getModuleId());
		attributes.put("appId", getAppId());
		attributes.put("contextName", getContextName());

		return attributes;
	}

	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long moduleId = (Long)attributes.get("moduleId");

		if (moduleId != null) {
			setModuleId(moduleId);
		}

		Long appId = (Long)attributes.get("appId");

		if (appId != null) {
			setAppId(appId);
		}

		String contextName = (String)attributes.get("contextName");

		if (contextName != null) {
			setContextName(contextName);
		}
	}

	/**
	* Returns the primary key of this module.
	*
	* @return the primary key of this module
	*/
	public long getPrimaryKey() {
		return _module.getPrimaryKey();
	}

	/**
	* Sets the primary key of this module.
	*
	* @param primaryKey the primary key of this module
	*/
	public void setPrimaryKey(long primaryKey) {
		_module.setPrimaryKey(primaryKey);
	}

	/**
	* Returns the uuid of this module.
	*
	* @return the uuid of this module
	*/
	public java.lang.String getUuid() {
		return _module.getUuid();
	}

	/**
	* Sets the uuid of this module.
	*
	* @param uuid the uuid of this module
	*/
	public void setUuid(java.lang.String uuid) {
		_module.setUuid(uuid);
	}

	/**
	* Returns the module ID of this module.
	*
	* @return the module ID of this module
	*/
	public long getModuleId() {
		return _module.getModuleId();
	}

	/**
	* Sets the module ID of this module.
	*
	* @param moduleId the module ID of this module
	*/
	public void setModuleId(long moduleId) {
		_module.setModuleId(moduleId);
	}

	/**
	* Returns the app ID of this module.
	*
	* @return the app ID of this module
	*/
	public long getAppId() {
		return _module.getAppId();
	}

	/**
	* Sets the app ID of this module.
	*
	* @param appId the app ID of this module
	*/
	public void setAppId(long appId) {
		_module.setAppId(appId);
	}

	/**
	* Returns the context name of this module.
	*
	* @return the context name of this module
	*/
	public java.lang.String getContextName() {
		return _module.getContextName();
	}

	/**
	* Sets the context name of this module.
	*
	* @param contextName the context name of this module
	*/
	public void setContextName(java.lang.String contextName) {
		_module.setContextName(contextName);
	}

	public boolean isNew() {
		return _module.isNew();
	}

	public void setNew(boolean n) {
		_module.setNew(n);
	}

	public boolean isCachedModel() {
		return _module.isCachedModel();
	}

	public void setCachedModel(boolean cachedModel) {
		_module.setCachedModel(cachedModel);
	}

	public boolean isEscapedModel() {
		return _module.isEscapedModel();
	}

	public java.io.Serializable getPrimaryKeyObj() {
		return _module.getPrimaryKeyObj();
	}

	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_module.setPrimaryKeyObj(primaryKeyObj);
	}

	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _module.getExpandoBridge();
	}

	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_module.setExpandoBridgeAttributes(serviceContext);
	}


	public java.lang.Object clone() {
		return new ModuleWrapper((Module)_module.clone());
	}

	public int compareTo(com.liferay.marketplace.model.Module module) {
		return _module.compareTo(module);
	}


	public int hashCode() {
		return _module.hashCode();
	}

	public com.liferay.portal.model.CacheModel<com.liferay.marketplace.model.Module> toCacheModel() {
		return _module.toCacheModel();
	}

	public com.liferay.marketplace.model.Module toEscapedModel() {
		return new ModuleWrapper(_module.toEscapedModel());
	}

	public com.liferay.marketplace.model.Module toUnescapedModel() {
		return new ModuleWrapper(_module.toUnescapedModel());
	}


	public java.lang.String toString() {
		return _module.toString();
	}

	public java.lang.String toXmlString() {
		return _module.toXmlString();
	}

	public void persist()
		throws com.liferay.portal.kernel.exception.SystemException {
		_module.persist();
	}


	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ModuleWrapper)) {
			return false;
		}

		ModuleWrapper moduleWrapper = (ModuleWrapper)obj;

		if (Validator.equals(_module, moduleWrapper._module)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedModel}
	 */
	public Module getWrappedModule() {
		return _module;
	}

	public Module getWrappedModel() {
		return _module;
	}

	public void resetOriginalValues() {
		_module.resetOriginalValues();
	}

	private Module _module;
}