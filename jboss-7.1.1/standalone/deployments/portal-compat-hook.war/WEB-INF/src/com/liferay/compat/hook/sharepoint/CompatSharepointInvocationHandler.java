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

package com.liferay.compat.hook.sharepoint;

import com.liferay.compat.portal.kernel.webdav.WebDAVUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatSharepointInvocationHandler implements InvocationHandler {

	public CompatSharepointInvocationHandler(Object sharepointMethod) {
		_sharepointMethod = sharepointMethod;
	}

	public Object getSharepointMethod() {
		return _sharepointMethod;
	}


	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		try {
			String methodName = method.getName();

			Object value = method.invoke(_sharepointMethod, arguments);

			if (methodName.equals("getRootPath")) {
				String rootPath = (String)value;

				rootPath = WebDAVUtil.stripManualCheckInRequiredPath(rootPath);

				value = rootPath;
			}

			return value;
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	private Object _sharepointMethod;

}