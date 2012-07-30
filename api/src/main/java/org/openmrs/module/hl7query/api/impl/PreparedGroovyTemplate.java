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

import groovy.text.Template;

import java.util.Map;

import org.openmrs.module.hl7query.HL7TemplateException;


/**
 * Simple wrapper around a Groovy {@link Template} object
 */
public class PreparedGroovyTemplate implements PreparedTemplate {

	Template template;
	
	public PreparedGroovyTemplate(Template template) {
		this.template = template;
	}
	
	/**
     * @see org.openmrs.module.hl7query.api.impl.PreparedTemplate#evaluate(java.util.Map)
     */
    @Override
    public String evaluate(Map<String, Object> bindings) throws HL7TemplateException {
    	try {
    		return (bindings == null ? template.make() : template.make(bindings)).toString();
    	}
    	catch (Exception ex) {
    		throw new HL7TemplateException(ex);
    	}
    }
	
}