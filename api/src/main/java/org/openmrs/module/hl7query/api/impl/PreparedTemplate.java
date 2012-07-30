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
package org.openmrs.module.hl7query.api.impl;

import java.util.Map;

import org.openmrs.module.hl7query.HL7TemplateException;


/**
 * A template that may be reused for many evaluations
 */
public interface PreparedTemplate {

	/**
     * Evaluates this prepared template against the given model
     * 
     * @param bindings the model to prepare this template against
     * @return
     */
    String evaluate(Map<String, Object> bindings) throws HL7TemplateException;
	
}
