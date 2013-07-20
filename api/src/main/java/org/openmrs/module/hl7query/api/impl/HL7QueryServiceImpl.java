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
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.HL7TemplateFunctions;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.db.HL7QueryDAO;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.PipeParser;

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
	
	// cache this so we don't have to recreate it every time
	private HL7TemplateFunctions templateFunctions = new HL7TemplateFunctions();
		
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
	 * @see org.openmrs.module.hl7query.api.HL7QueryService#evaluateTemplate(org.openmrs.module.hl7query.HL7Template, java.util.Map)
	 * @should evaluate a groovy template
	 * @should fail to evaluate a groovy template against bad input
	 * @should fail to evaluate a template of an unknown language
	 * @should add the HL7TemplateFunctions class as func to bindings
	 * @shoudl not fail if bindings is null
	 */
	@Override
	public String evaluateTemplate(HL7Template template, Map<String, Object> bindings) {
		PreparedTemplate prepared = templateCache.get(template.getName());
		if (prepared == null) {
			TemplateFactory<?> factory = templateFactories.get(template.getLanguage());
			if (factory == null) {
				throw new APIException("Unknown template language: " + template.getLanguage());
			}
			prepared = factory.prepareTemplate(template.getTemplate());
			templateCache.put(template.getName(), prepared);
		}
		
		if (bindings == null)
			bindings = new HashMap<String, Object>();
		
		bindings.put("func", templateFunctions);
		
		return prepared.evaluate(bindings);
	}
	
	/**
	 * Call this whenever a template is modified
	 */
	public void clearTemplateCache() {
		templateCache.clear();
	}

	@Override
    public HL7Template getHL7Template(Integer id) {
	    return dao.getHL7Template(id);
    }

	@Override
    public HL7Template getHL7TemplateByUuid(String uuid) {
	    return dao.getHL7TemplateByUuid(uuid);
    }

	@Override
    public HL7Template getHL7TemplateByName(String name) {
	    return dao.getHL7TemplateByName(name);
    }

	@Override
    public List<HL7Template> getHL7TemplatesByName(String name) {
	    return dao.getHL7TemplatesByName(name);
    }

	@Override
    public List<HL7Template> getHL7TemplatesByEntity(String entity) {
	    return dao.getHL7TemplatesByEntity(entity);
    }

	@Override
    public HL7Template saveHL7Template(HL7Template hl7Template) {
	    clearTemplateCache();
		return dao.saveHL7Template(hl7Template);
    }

	@Override
    public HL7Template retireHL7Template(HL7Template hl7Template, String reason) {
	    return dao.retireHL7Template(hl7Template, reason);
    }

	@Override
    public HL7Template unretireHL7Template(HL7Template hl7Template) {
	    return dao.unretireHL7Template(hl7Template);
	}

	@Override
    public void purgeHL7Template(HL7Template hl7Template) {
	    dao.purgeHL7Template(hl7Template);
    }

	@Override
    public List<HL7Template> getHL7Templates(boolean includeRetired) {
	    return dao.getHL7Templates(includeRetired);
    }
	
	/**
	* @see org.openmrs.module.hl7query.api.HL7QueryService#renderPipeDelimitedHl7(java.lang.String)
	*/
	public String renderPipeDelimitedHl7(String xml) throws Exception {
		Message message = new DefaultXMLParser().parse(xml);
	    return new PipeParser().encode(message);
	}

	@Override
    public List<HL7Template> getParentHL7Templates() {
	    return dao.getParentHL7Templates();
    }
}
