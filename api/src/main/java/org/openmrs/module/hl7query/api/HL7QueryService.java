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

import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.hl7query.HL7Template;
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
	 * @param hl7Template
	 * @param bindings
	 * @return the result of evaluating the given template against the given bindings
	 */
	String evaluateTemplate(HL7Template hl7Template, Map<String, Object> bindings);
	
	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param id
	 * @return
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	HL7Template getHL7Template(Integer id);
	
	@Transactional(readOnly = true)
	HL7Template getHL7TemplateByUuid(String uuid);
	
	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param name
	 * @return
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	HL7Template getHL7TemplateByName(String name);
	
	@Transactional(readOnly = true)
	List<HL7Template> getHL7TemplatesByName(String name);
	
	@Transactional(readOnly = true)
	List<HL7Template> getHL7TemplatesByEntity(String entity);
	
	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param hl7Template
	 * @return
	 * @should save template
	 */
	HL7Template saveHL7Template(HL7Template hl7Template);
	
	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param hl7Template
	 * @param reason
	 * @return
	 * @should retire template
	 */
	HL7Template retireHL7Template(HL7Template hl7Template, String reason);

	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param hl7Template
	 * @return
	 * @should unretire template
	 */
	HL7Template unretireHL7Template(HL7Template hl7Template);
	
	/**
	 * 
	 * Auto generated method comment
	 * 
	 * @param hl7Template
	 * @should purge template
	 */
	void purgeHL7Template(HL7Template hl7Template);
}