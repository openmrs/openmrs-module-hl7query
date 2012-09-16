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

/**
 * The ErrorDetailsEnum class contains appopriate error message and error code pairs for each error condition which may occur while processing requests 
 *
 */

public enum ErrorDetailsEnum {
	 MISSING_IDENTIFIERS(HL7ErrorConstants.MISSING_IDENTIFIERS, 400), 
	 MISSING_TEMPLATE(HL7ErrorConstants.MISSING_TEMPLATE, 400), 
	 MISSING_UUID(HL7ErrorConstants.MISSING_UUID, 400),
	 MISSING_IDENTIFIER_TYPE(HL7ErrorConstants.MISSING_IDENTIFIER_TYPE, 400),
	 MISSING_PATIENT(HL7ErrorConstants.MISSING_PATIENT, 400),
	 MULTIPLE_PATIENTS(HL7ErrorConstants.MULTIPLE_PATIENTS, 400),
	 INTERNAL_SERVER_ERROR(HL7ErrorConstants.INTERNAL_SERVER_ERROR, 500);
	 
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
