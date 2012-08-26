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

import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.util.HL7QueryPrivilegeConstants;
import org.openmrs.util.PrivilegeConstants;
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
	 * Gets HL7Template by ID.
	 * 
	 * @param id
	 * @return HL7Template or null
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	HL7Template getHL7Template(Integer id);
	
	/**
	 * Gets HL7Template by UUID.
	 * 
	 * @param uuid
	 * @return HL7Template or null
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	HL7Template getHL7TemplateByUuid(String uuid);
	
	/**
	 * Gets HL7Template by unique name.
	 * 
	 * @param name
	 * @return HL7Template or null
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	HL7Template getHL7TemplateByName(String name);
	
	/**
	 * Gets HL7Templates by partial name.
	 * 
	 * @param name
	 * @return HL7Templates or empty list
	 * @should return matching templates
	 * @should return empty list if no matches
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	List<HL7Template> getHL7TemplatesByName(String name);
	
	/**
	 * Gets HL7Templates by entity.
	 * 
	 * @param entity
	 * @return HL7Templates or empty list
	 * @should return matching templates
	 * @should return empty list if no matches
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	List<HL7Template> getHL7TemplatesByEntity(String entity);
	
	/**
	 * Saves or updates the template.
	 * 
	 * @param hl7Template
	 * @return HL7Template
	 * @should save template
	 */
	@Authorized({HL7QueryPrivilegeConstants.MANAGE_HL7_TEMPLATES})
	HL7Template saveHL7Template(HL7Template hl7Template);
	
	/**
	 * Retires the template.
	 * 
	 * @param hl7Template
	 * @param reason
	 * @return HL7Template
	 * @should retire template
	 */
	@Authorized({HL7QueryPrivilegeConstants.MANAGE_HL7_TEMPLATES})
	HL7Template retireHL7Template(HL7Template hl7Template, String reason);

	/**
	 * Unretires the template.
	 * 
	 * @param hl7Template
	 * @return HL7Template
	 * @should unretire template
	 */
	@Authorized({HL7QueryPrivilegeConstants.MANAGE_HL7_TEMPLATES})
	HL7Template unretireHL7Template(HL7Template hl7Template);
	
	/**
	 * Purges the template.
	 * 
	 * @param hl7Template
	 * @should purge template
	 */
	@Authorized({HL7QueryPrivilegeConstants.MANAGE_HL7_TEMPLATES})
	void purgeHL7Template(HL7Template hl7Template);
	
	/**
	 * Gets all HL7 templates. 
	 * TODO add/require view privileges and update manageTemplates.jsp to
	 * require them
	 * 
	 * @param includeRetired specified whether retired templates should be included or not
	 * @return a list of {@link HL7Template}s
	 * @should get all templates
	 * @should exclude retired templates if include retired is set to false
	 */
	@Transactional(readOnly = true)
	@Authorized({HL7QueryPrivilegeConstants.GET_HL7_TEMPLATES})
	List<HL7Template> getHL7Templates(boolean includeRetired);
	
	/**
	* Converts an xml to pipe delimited hl7 message
	*
	* @param xml the hl7 message in xml format
	* @return the pipe delimited hl7 message
	* @throws Exception
	* @should return pipe delimited hl7 message
	*/
	@Transactional(readOnly = true)
	public String renderPipeDelimitedHl7(String xml) throws Exception;
}