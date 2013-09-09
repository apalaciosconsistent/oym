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

import com.liferay.portal.kernel.repository.BaseRepository;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.repository.proxy.BaseRepositoryProxyBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatCMISRepositoryFactoryInvocationHandler
	implements InvocationHandler {

	public CompatCMISRepositoryFactoryInvocationHandler(
		Object repositoryFactory) {

		_repositoryFactory = repositoryFactory;
	}

	public Object getRepositoryFactory() {
		return _repositoryFactory;
	}


	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		try {
			String methodName = method.getName();

			Object value = method.invoke(_repositoryFactory, arguments);

			if (methodName.equals("getInstance")) {
				ClassLoader classLoader =
					PortalClassLoaderUtil.getClassLoader();

				BaseRepository baseRepository =
					(BaseRepository)ProxyUtil.newProxyInstance(
						classLoader, new Class<?>[] {BaseRepository.class},
						new CompatCMISRepositoryInvocationHandler(
							value, classLoader));

				value = new BaseRepositoryProxyBean(
					baseRepository, classLoader);
			}

			return value;
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	private Object _repositoryFactory;

}