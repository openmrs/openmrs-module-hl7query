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

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.hl7query.TemplateException;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.db.HL7QueryDAO;

/**
 * It is a default implementation of {@link HL7QueryService}.
 */
public class HL7QueryServiceImpl extends BaseOpenmrsService implements HL7QueryService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private HL7QueryDAO dao;
	
	private TemplateEngine groovyTemplateEngine;
	
	public HL7QueryServiceImpl() {
		// use this module's classloader
		groovyTemplateEngine = new SimpleTemplateEngine(getClass().getClassLoader());
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
	 * Compiles the given templateText into a groovy template.
	 * This operation is relatively expensive, so you should cache and reuse the returned template. 
	 * 
	 * @param templateText
	 * @return
	 * @throws TemplateException
	 */
	public Template prepareGroovyTemplate(String templateText) throws TemplateException {
		try {
			return groovyTemplateEngine.createTemplate(templateText);
		}
		catch (Exception ex) {
			throw new TemplateException(ex);
		}
	}
	
	/**
	 * Evaluates the given groovy template with the given bindings (which are allowed to be null)
	 * 
	 * @param template
	 * @param bindings
	 * @return
	 * @throws TemplateException
	 */
	public String evaluate(Template template, Map<String, Object> bindings) throws TemplateException {
		try {
			Writable boundTemplate = bindings == null ? template.make() : template.make(bindings);
			return boundTemplate.toString();
		}
		catch (Exception ex) {
			throw new TemplateException(ex);
		}
	}
}
