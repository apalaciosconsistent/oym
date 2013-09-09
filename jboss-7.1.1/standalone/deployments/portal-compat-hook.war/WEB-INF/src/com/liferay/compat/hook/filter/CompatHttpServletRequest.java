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

package com.liferay.compat.hook.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatHttpServletRequest extends HttpServletRequestWrapper {

	public CompatHttpServletRequest(
		HttpServletRequest request, String pathInfo) {

		super(request);

		_pathInfo = pathInfo;
	}


	public String getPathInfo() {
		return _pathInfo;
	}

	private String _pathInfo;

}