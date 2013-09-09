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

package com.liferay.compat.hook.action;

import com.liferay.compat.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.servlet.NoRedirectServletResponse;
import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jonathan Lee
 */
public class CompatUpdatePasswordAction extends BaseStrutsAction {


	public String execute(
			StrutsAction originalStrutsAction, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		NoRedirectServletResponse noRedirectServletResponse =
			new NoRedirectServletResponse(response);

		String forward = originalStrutsAction.execute(
			request, noRedirectServletResponse);

		String location = noRedirectServletResponse.getRedirectLocation();

		if (Validator.isNotNull(location)) {
			String redirect = ParamUtil.getString(request, WebKeys.REFERER);

			if (Validator.isNull(redirect)) {
				redirect = location;
			}

			response.sendRedirect(redirect);
		}

		return forward;
	}

}