package org.openmrs.module.hl7query.web.controller;

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.AuthenticationErrorObject;
import org.openmrs.module.hl7query.util.ExceptionUtil;
import org.openmrs.module.hl7query.util.HL7QueryConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Any controller that wants to use BASIC authentication needs to extend the BaseHL7QueryController.
 * The BaseHL7QueryController used Spring Exception handling to manage exceptions automatically.
 * (This is necessary to send error messages as HTTP statuses rather than just as
 * html content, as the core web application does.)
 */
@Controller
@RequestMapping(value = "/module/hl7query/**")
public class BaseHL7QueryController {

	private int errorCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

	private String errorDetail;

	@ExceptionHandler(APIAuthenticationException.class)
	@ResponseBody
	public AuthenticationErrorObject apiAuthenticationExceptionHandler(Exception ex, HttpServletResponse response) throws Exception {
		if (Context.isAuthenticated()) {
			// user is logged in but doesn't have the relevant privilege -> 403 FORBIDDEN
			errorCode = HttpServletResponse.SC_FORBIDDEN;
			errorDetail = "User is logged in but doesn't have the relevant privilege";
		} else {
			// user is not logged in -> 401 UNAUTHORIZED
			errorCode = HttpServletResponse.SC_UNAUTHORIZED;
			errorDetail = "User is not logged in";
			response.addHeader("WWW-Authenticate", "Basic realm=\"OpenMRS at " + HL7QueryConstants.URI_PREFIX);
		}
		response.setStatus(errorCode);
		return ExceptionUtil.wrapErrorResponse(ex, errorDetail);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	private AuthenticationErrorObject handleException(Exception ex, HttpServletResponse response) throws Exception {
		ResponseStatus ann = ex.getClass().getAnnotation(ResponseStatus.class);
		if (ann != null) {
			errorCode = ann.value().value();
			if (StringUtils.isNotEmpty(ann.reason())) {
				errorDetail = ann.reason();
			}

		} else if (ExceptionUtil.hasCause(ex, APIAuthenticationException.class)) {
			return apiAuthenticationExceptionHandler(ex, response);
		} else if (ex.getClass() == HttpRequestMethodNotSupportedException.class) {
			errorCode = HttpServletResponse.SC_METHOD_NOT_ALLOWED;
		}
		response.setStatus(errorCode);
		return ExceptionUtil.wrapErrorResponse(ex, errorDetail);
	}

}