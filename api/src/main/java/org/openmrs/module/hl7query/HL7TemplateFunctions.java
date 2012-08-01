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
package org.openmrs.module.hl7query;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.api.HL7QueryService;

/**
 * This class is available to every template that is rendered. <br/>
 * This class is essentially a singleton. There is only one instance that is shared amoungst all
 * rendering templates
 * 
 * @see HL7QueryService#evaluateTemplate(HL7Template, java.util.Map)
 */
public class HL7TemplateFunctions {
	
    public static Log log = LogFactory.getLog(HL7TemplateFunctions.class);
	
	private HL7QueryService hl7queryService;
	
	private AdministrationService adminService;
	
	protected HL7QueryService getHl7queryService() {
		if (hl7queryService == null)
			hl7queryService = Context.getService(HL7QueryService.class);
		
    	return hl7queryService;
    }

	
    public void setHl7queryService(HL7QueryService hl7queryService) {
    	this.hl7queryService = hl7queryService;
    }

	
    protected AdministrationService getAdminService() {
    	if (adminService == null)
    		adminService = Context.getAdministrationService();
    	
    	return adminService;
    }

	
    public void setAdminService(AdministrationService adminService) {
    	this.adminService = adminService;
    }

	/**
	 * This method looks up another template and evaluates that and returns the product of the
	 * template.
	 * 
	 * @param templateName the unique name of the template
	 * @param bindings the data that the subtemplate will use
	 * @return string of the rendered subtemplate or empty string if no template found
	 * @see HL7QueryService#evaluateTemplate(HL7Template, java.util.Map)
	 * @should pass through to service layer evaluate method
	 * @should not fail with not found template
	 */
	public String evaluateTemplate(String templateName, Map<String, Object> bindings) {
		// what is this user thinking?
		if (templateName == null) {
			log.info("Template name is null");
			return "";
		}
		
		HL7Template template = getHl7queryService().getHL7TemplateByName(templateName);
		
		// didn't find the template, fail early.
		if (template == null) {
			log.info("Could not find a template by the name of " + templateName);
			return "";
		}
		
		return getHl7queryService().evaluateTemplate(template, bindings);
	}
	
	/**
	 * Looks up a global property by name. Useful for creating/storing/looking up constants. <br/>
	 * If the GP doesn't exist, as a convenience, a second lookup is done for "hl7query." +
	 * <code>globalPropertyName</code>
	 * 
	 * @param globalPropertyName the name of the {@link GlobalProperty} to look for
	 * @return the value of the gp or empty string if it doesn't exist
	 * @see AdministrationService#getGlobalProperty
	 * @should return empty string if gp doesn't exist
	 * @should preprend hl7query if gp doesn't exist
	 * @should return gp if gp exists
	 * @should find gp if hl7query is left off beginning
	 */
	public String getGlobalProperty(String globalPropertyName) {
		String gpValue = getAdminService().getGlobalProperty(globalPropertyName);
		
		if (gpValue == null) {
			// prepend the module id to save a little bit of space in the templates
			gpValue = getAdminService().getGlobalProperty("hl7query." + globalPropertyName);
		}
		
		// never return null
		if (gpValue == null)
			return "";
		
		return gpValue;
	}
	
}
