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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.GlobalProperty;
import org.openmrs.Obs;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.util.OpenmrsConstants;

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
	
	/**
	 * Returns the implementation id of the implementation site. This is an
	 * hl7concept source, which is used in creating the MSH segment.
	 * 
	 * @return the implementation id
	 */
	public String getImplementationId(){
		//If the Implementation id is set, return it
		if(getAdminService().getImplementationId().getImplementationId() != null)
			return getAdminService().getImplementationId().getImplementationId();
		else //If the Implementation id  is missing, then return null
			return null;
	}
	
	/**
	 * Returns a String representation of the date object to be included in the hl7 
	 * message
	 * The user may specify the date string format by passing it in as 
	 * a method parameter.
	 * If the user does not specify a parameter, then the default format 
	 * ('yyyyMMddHHmmss') will be used.
	 * If the user does not specify a date, then a new Date object would be used
	 * instead.
	 * 
	 * @param date the date
	 * 		The date to be parsed to String
	 * @param format the format
	 * 		The string used to format the date string
	 * @return the string
	 * 		The string representation of the given date object after it is formatted
	 */
	public String formatDate(Date date, String format){
		String dateString;
		SimpleDateFormat dateFormat = null;
		if(date == null)
			date = new Date();
		
		if(format != null){
			dateFormat = new SimpleDateFormat(format);
		}else{
			dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 	
		}
		dateString = dateFormat.format(date);
		return dateString;
	}
	
	/**
	 * Gets the list of obs groups in a given encounter.
	 * 
	 * @param encounter the encounter.
	 * @return the list of obs groups.
	 */
	public Set<Obs> getObsGroups(Encounter encounter) {
		Set<Obs> obsGroups = new LinkedHashSet<Obs>();
		
		for (Obs obs : encounter.getObs()) {
			if (obs.getObsGroup() != null) {
				obsGroups.add(obs.getObsGroup());
			}
		}
		
		return obsGroups;
	}
	
	/**
	 * Gets the concept for medical record observations. The one pointed to the
	 * <code>OpenmrsConstants.GLOBAL_PROPERTY_MEDICAL_RECORD_OBSERVATIONS</code> global property.
	 * 
	 * @return the concept object for medical record observations.
	 */
	public Concept getMedicalRecordObservationsConcept() {
		Concept concept = null;
		String conceptId = Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GLOBAL_PROPERTY_MEDICAL_RECORD_OBSERVATIONS);
		if (conceptId != null && StringUtils.isNumeric(conceptId)) {
			concept = Context.getConceptService().getConcept(Integer.parseInt(conceptId));
		}
		
		return concept;
	}
}
