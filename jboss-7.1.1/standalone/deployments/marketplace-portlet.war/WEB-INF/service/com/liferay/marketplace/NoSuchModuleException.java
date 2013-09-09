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

package com.liferay.marketplace;

import com.liferay.portal.NoSuchModelException;

/**
 * @author Ryan Park
 */
public class NoSuchModuleException extends NoSuchModelException {

	public NoSuchModuleException() {
		super();
	}

	public NoSuchModuleException(String msg) {
		super(msg);
	}

	public NoSuchModuleException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NoSuchModuleException(Throwable cause) {
		super(cause);
	}

}