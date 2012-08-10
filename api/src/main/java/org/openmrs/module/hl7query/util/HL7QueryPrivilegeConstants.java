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

import org.openmrs.annotation.AddOnStartup;
/**
Contains classes for privileges used in this module
 */

public class HL7QueryPrivilegeConstants {
	@AddOnStartup(description = "Ability to Add HL7 templates")
	public static final String ADD_HL7_TEMPLATE = "Add HL7 Template";

	@AddOnStartup(description = "Ability to Edit HL7 templates")
	public static final String EDIT_HL7_TEMPLATE = "Edit HL7 Template";
	
	@AddOnStartup(description = "Ability to Add HL7 templates")
	public static final String VIEW_HL7_TEMPLATE = "View HL7 Templates";
}
