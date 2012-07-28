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

import groovy.text.SimpleTemplateEngine;

import org.openmrs.module.hl7query.TemplateException;


/**
 * Groovy-based implementation of TemplateFactory
 */
public class GroovyTemplateFactory implements TemplateFactory<PreparedGroovyTemplate> {
	
	SimpleTemplateEngine templateEngine;
	
	public GroovyTemplateFactory() {
		// use this module's ModuleClassLoader
		templateEngine = new SimpleTemplateEngine(getClass().getClassLoader());
	}

	/**
	 * @see org.openmrs.module.hl7query.api.impl.TemplateFactory#prepareTemplate(java.lang.String)
     */
    @Override
    public PreparedGroovyTemplate prepareTemplate(String templateText) throws TemplateException {
    	try {
    		return new PreparedGroovyTemplate(templateEngine.createTemplate(templateText));
    	}
    	catch (Exception ex) {
    		throw new TemplateException(ex);
    	}
    }

}
