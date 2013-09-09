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

import com.liferay.compat.portal.kernel.webdav.WebDAVUtil;
import com.liferay.portal.kernel.util.HttpUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 */
public class CompatWebDAVFilter implements Filter {


	public void destroy() {
	}


	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)servletRequest;

		String pathInfo = HttpUtil.fixPath(request.getPathInfo(), false, true);

		String strippedPathInfo = WebDAVUtil.stripManualCheckInRequiredPath(
			pathInfo);

		if (strippedPathInfo.length() != pathInfo.length()) {
			pathInfo = strippedPathInfo;

			try {
				CompatWebDAVThreadLocal.setManualCheckInRequired(true);

				filterChain.doFilter(
					new CompatHttpServletRequest(request, pathInfo),
					servletResponse);
			}
			finally {
				CompatWebDAVThreadLocal.setManualCheckInRequired(false);
			}
		}
		else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}


	public void init(FilterConfig filterConfig) {
	}

}