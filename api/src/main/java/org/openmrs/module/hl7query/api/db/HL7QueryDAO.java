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
package org.openmrs.module.hl7query.api.db;

import java.util.List;

import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.HL7QueryService;

/**
 * Database methods for {@link HL7QueryService}.
 */
public interface HL7QueryDAO {
	
	/**
	 * @see HL7QueryService#getHL7Template(Integer)
	 */
	HL7Template getHL7Template(Integer id);
	
	/**
	 * @see HL7QueryService#getHL7TemplateByUuid(String)
	 */
	HL7Template getHL7TemplateByUuid(String uuid);
	
	/**
	 * @see HL7QueryService#getHL7TemplateByName(String)
	 */
	HL7Template getHL7TemplateByName(String name);
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByName(String)
	 */
	List<HL7Template> getHL7TemplatesByName(String name);
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByEntity(String)
	 */
	List<HL7Template> getHL7TemplatesByEntity(String entity);
	
	/**
	 * @see HL7QueryService#saveHL7Template(HL7Template)
	 */
	HL7Template saveHL7Template(HL7Template hl7Template);
	
	/**
	 * @see HL7QueryService#retireHL7Template(HL7Template, String)
	 */
	HL7Template retireHL7Template(HL7Template hl7Template, String reason);
	
	/**
	 * @see HL7QueryService#unretireHL7Template(HL7Template)
	 */
	HL7Template unretireHL7Template(HL7Template hl7Template);
	
	/**
	 * @see HL7QueryService#purgeHL7Template(HL7Template)
	 */
	void purgeHL7Template(HL7Template hl7Template);
	
	/**
	 * @see HL7QueryService#getHL7Templates(boolean)
	 */
	List<HL7Template> getHL7Templates(boolean includeRetired);
	
	/**
	 * @see HL7QueryService#getParentHL7Templates()
	 */
	List<HL7Template> getParentHL7Templates();
}
