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

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.OpenmrsMetadata;

/**
 * A template that describes how to generate an HL7 message, or a fragment of one.
 * 
 * This object defines a template and the language it should be evaluated in.
 */
public class Template extends BaseOpenmrsMetadata implements OpenmrsMetadata {
	
	private Integer templateId;

	private String hl7Entity;
	
	private String language;
	
	private String template;
	
	/**
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	@Override
	public Integer getId() {
		return getTemplateId();
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		setTemplateId(id);
	}
	
	/**
	 * @return the templateId
	 */
	public Integer getTemplateId() {
		return templateId;
	}
	
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
		
	/**
	 * @return the hl7Entity
	 */
	public String getHl7Entity() {
		return hl7Entity;
	}
	
	/**
	 * @param hl7Entity the hl7Entity to set
	 */
	public void setHl7Entity(String hl7Entity) {
		this.hl7Entity = hl7Entity;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	
}
