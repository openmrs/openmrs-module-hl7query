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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.hl7query.Template;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.db.HL7QueryDAO;

/**
 * It is a default implementation of {@link HL7QueryService}.
 */
public class HL7QueryServiceImpl extends BaseOpenmrsService implements HL7QueryService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private HL7QueryDAO dao;
	
	// map from language name to template factory
	private Map<String, TemplateFactory<?>> templateFactories;
	
	// cache of compiled templates (by name)
	private Map<String, PreparedTemplate> templateCache;
		
	public HL7QueryServiceImpl() {
		templateFactories = new HashMap<String, TemplateFactory<?>>();
		templateFactories.put(HL7QueryService.LANGUAGE_GROOVY, new GroovyTemplateFactory());
		templateCache = new HashMap<String, PreparedTemplate>();
	}
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(HL7QueryDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @return the dao
	 */
	public HL7QueryDAO getDao() {
		return dao;
	}
	
	/**
	 * @see org.openmrs.module.hl7query.api.HL7QueryService#evaluateTemplate(org.openmrs.module.hl7query.Template, java.util.Map)
	 * @should evaluate a groovy template
	 * @should fail to evaluate a groovy template against bad input
	 * @should fail to evaluate a template of an unknown language
	 */
	@Override
	public String evaluateTemplate(Template template, Map<String, Object> bindings) {
		PreparedTemplate prepared = templateCache.get(template.getName());
		if (prepared == null) {
			TemplateFactory<?> factory = templateFactories.get(template.getLanguage());
			if (factory == null) {
				throw new APIException("Unknown template language: " + template.getLanguage());
			}
			prepared = factory.prepareTemplate(template.getTemplate());
			templateCache.put(template.getName(), prepared);
		}
		return prepared.evaluate(bindings);
	}
	
	/**
	 * Call this whenever a template is modified
	 */
	public void clearTemplateCache() {
		templateCache.clear();
	}
	
}
