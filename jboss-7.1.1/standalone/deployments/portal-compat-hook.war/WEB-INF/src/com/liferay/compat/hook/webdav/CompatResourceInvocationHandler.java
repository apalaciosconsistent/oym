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

package com.liferay.compat.hook.webdav;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.webdav.Resource;
import com.liferay.portal.kernel.webdav.WebDAVException;

import java.io.InputStream;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatResourceInvocationHandler implements InvocationHandler {

	public CompatResourceInvocationHandler(Resource resource) {
		_resource = resource;

		try {
			Class<?> clazz = resource.getClass();

			Field field = clazz.getDeclaredField("_fileEntry");

			field.setAccessible(true);

			_fileEntry = (FileEntry)field.get(resource);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}


	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		try {
			if (_fileEntry == null) {
				return method.invoke(_resource, arguments);
			}

			String methodName = method.getName();

			if (methodName.equals("getContentAsStream")) {
				return getContentAsStream();
			}
			else if (methodName.equals("getContentType")) {
				return getContentType();
			}
			else if (methodName.equals("getSize")) {
				return getSize();
			}

			return method.invoke(_resource, arguments);
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	protected InputStream getContentAsStream() throws WebDAVException {
		try {
			FileVersion fileVersion = _fileEntry.getLatestFileVersion();

			return fileVersion.getContentStream(false);
		}
		catch (Exception e) {
			throw new WebDAVException(e);
		}
	}

	protected String getContentType() {
		try {
			FileVersion fileVersion = _fileEntry.getLatestFileVersion();

			return fileVersion.getMimeType();
		}
		catch (Exception e) {
			return _fileEntry.getMimeType();
		}
	}

	protected long getSize() {
		try {
			FileVersion fileVersion = _fileEntry.getLatestFileVersion();

			return fileVersion.getSize();
		}
		catch (Exception e) {
			return _fileEntry.getSize();
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		CompatResourceInvocationHandler.class);

	private FileEntry _fileEntry;
	private Resource _resource;

}