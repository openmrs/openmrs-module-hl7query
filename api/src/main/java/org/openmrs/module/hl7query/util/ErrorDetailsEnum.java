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

package org.openmrs.module.hl7query.util;

import org.openmrs.api.context.Context;

/**
 * The ErrorDetailsEnum class contains appopriate error message and error code pairs for each error condition which may occur while processing requests 
 *
 */

public enum ErrorDetailsEnum {
	/**
	 * The enum contains detailed error messages which are populated from the messages.properties file
	 */
	
	 MISSING_IDENTIFIERS(Context.getMessageSourceService().getMessage("hl7query.missingIdentifiers"), 400), 
	 MISSING_TEMPLATE(Context.getMessageSourceService().getMessage("hl7query.missingTemplate"), 400), 
	 MISSING_UUID(Context.getMessageSourceService().getMessage("hl7query.missingUuid"), 400),
	 MISSING_IDENTIFIER_TYPE(Context.getMessageSourceService().getMessage("hl7query.missingIdentifierType"), 400),
	 MISSING_PATIENT(Context.getMessageSourceService().getMessage("hl7query.missingPatient"), 400),
	 MULTIPLE_PATIENTS(Context.getMessageSourceService().getMessage("hl7query.multiplePatients"), 400),
	 INTERNAL_SERVER_ERROR(Context.getMessageSourceService().getMessage("hl7query.internalServerError"), 500);
	 
	 private int code;
	 private String message;
	 
	 private ErrorDetailsEnum(String message, int code) {
	   this.code = code;
	   this.message = message;
	 }
	 
	 public int getCode() {
	   return code;
	 }
	 
	 public String getMessage() {
		 return message;
	 }
}
