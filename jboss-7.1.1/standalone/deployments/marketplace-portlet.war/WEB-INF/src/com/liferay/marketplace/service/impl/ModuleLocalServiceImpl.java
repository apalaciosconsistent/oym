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

package com.liferay.marketplace.service.impl;

import com.liferay.marketplace.model.Module;
import com.liferay.marketplace.service.base.ModuleLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.SystemException;

import java.util.List;

/**
 * @author Ryan Park
 */
public class ModuleLocalServiceImpl extends ModuleLocalServiceBaseImpl {

	public Module addModule(long userId, long appId, String contextName)
		throws SystemException {

		long moduleId = counterLocalService.increment();

		Module module = modulePersistence.create(moduleId);

		module.setModuleId(moduleId);
		module.setAppId(appId);
		module.setContextName(contextName);

		modulePersistence.update(module, false);

		return module;
	}

	public Module fetchModule(long appId, String contextName)
		throws SystemException {

		return modulePersistence.fetchByA_C(appId, contextName);
	}

	public List<Module> getModules(long appId) throws SystemException {
		return modulePersistence.findByAppId(appId);
	}

}