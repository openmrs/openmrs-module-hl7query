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
 * The HL7ErrorConstants class contains detailed error message for all potential error conditions which may occur during execution
 */

public final class HL7ErrorConstants {

	public static final String MISSING_IDENTIFIERS = "Both Patient identifier and encounter uuid cannot be blank";
	
	public static final String MISSING_TEMPLATE = "Cannot find template with name :";
	
	public static final String MISSING_UUID = "Cannot find an encounter with uuid :";
	
	public static final String MISSING_IDENTIFIER_TYPE = "Cannot find a patient identifier type with uuid :";
	
	public static final String MISSING_PATIENT = "Could not find a matching patient :";
	
	public static final String MULTIPLE_PATIENTS = "Found multiple matching patients :";
	
	public static final String INTERNAL_SERVER_ERROR = "Internal Server error";
}
