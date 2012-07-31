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
package org.openmrs.module.hl7query.api;

import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.hl7query.Template;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(HL7QueryService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface HL7QueryService extends OpenmrsService {
	
	static final String LANGUAGE_GROOVY = "groovy";

	/**
	 * Evaluates the given template against the given bindings, returning the text result (which should be XML) 
	 * 
	 * @param template
	 * @param bindings
	 * @return the result of evaluating the given template against the given bindings
	 */
	String evaluateTemplate(Template template, Map<String, Object> bindings);

	/**
	 * Converts an xml to pipe delimited hl7 message
	 * 
	 * @param xml the hl7 message in xml format
	 * @return the pipe delimited hl7 message
	 * @throws Exception
	 * @should return pipe delimited hl7 message
	 */
	public String renderPipeDelimitedHl7(String xml) throws Exception;
}