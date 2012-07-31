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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

// TODO: change this to use mocks instead of the actual db
public class HL7TemplateFunctionsTest extends BaseModuleContextSensitiveTest {
	
	/**
	 * @see HL7TemplateFunctions#evaluateTemplate(String,Map)
	 * @verifies pass through to service layer evaluate method
	 */
	@Test
	public void evaluateTemplate_shouldPassThroughToServiceLayerEvaluateMethod() throws Exception {
		// add a simple template to our db
		HL7Template t = new HL7Template();
		t.setName("simple");
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("The value of locale.allowed.list is: ${ func.getGlobalProperty('locale.allowed.list') }");
		Context.getService(HL7QueryService.class).saveHL7Template(t);
		
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("The value of locale.allowed.list is: en", functions.evaluateTemplate("simple", null));
	}
	
	/**
	 * @see HL7TemplateFunctions#evaluateTemplate(String,Map)
	 * @verifies not fail with not found template
	 */
	@Test
	public void evaluateTemplate_shouldNotFailWithNotFoundTemplate() throws Exception {
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("", functions.evaluateTemplate("anonexistanttemplate", null));
	}
	
	/**
	 * @see HL7TemplateFunctions#getGlobalProperty(String)
	 * @verifies return empty string if gp doesn't exist
	 */
	@Test
	public void getGlobalProperty_shouldReturnEmptyStringIfGpDoesntExist() throws Exception {
		// add a simple template to our db
		HL7Template t = new HL7Template();
		t.setName("nonexistantgptemplate");
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("-${ func.getGlobalProperty('nonexistantgp') }-");
		Context.getService(HL7QueryService.class).saveHL7Template(t);
		
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("--", functions.evaluateTemplate("nonexistantgptemplate", null));
	}
	
	/**
	 * @see HL7TemplateFunctions#getGlobalProperty(String)
	 * @verifies preprend hl7query if gp doesn't exist
	 */
	@Test
	public void getGlobalProperty_shouldPreprendHl7queryIfGpDoesntExist() throws Exception {
		String GPNAME = "customgp";
		
		// add a gp to the gp table WITH "hl7query" in front of it!
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty("hl7query." + GPNAME, "acustomvalue"));
		// add a simple template to our db
		HL7Template t = new HL7Template();
		t.setName("prefixtesttemplate");
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("Value is: ${ func.getGlobalProperty('" + GPNAME + "') }");
		Context.getService(HL7QueryService.class).saveHL7Template(t);
		
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("Value is: acustomvalue", functions.evaluateTemplate("prefixtesttemplate", null));
	}
	
	/**
	 * @see HL7TemplateFunctions#getGlobalProperty(String)
	 * @verifies return gp if gp exists
	 */
	@Test
	public void getGlobalProperty_shouldReturnGpIfGpExists() throws Exception {
		String GPNAME = "customgp";
		
		// add a gp to the gp table (no hl7query prefix)
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(GPNAME, "acustomvalue"));
		// add a simple template to our db
		HL7Template t = new HL7Template();
		t.setName("customgptemplate");
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("Value is: ${ func.getGlobalProperty('" + GPNAME + "') }");
		Context.getService(HL7QueryService.class).saveHL7Template(t);
		
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("Value is: acustomvalue", functions.evaluateTemplate("customgptemplate", null));
	}
	
	/**
	 * @see HL7TemplateFunctions#getGlobalProperty(String)
	 * @verifies find gp if hl7query is left off beginning
	 */
	@Test
	public void getGlobalProperty_shouldFindGpIfHl7queryIsLeftOffBeginning() throws Exception {
		String GPNAME = "customgp";
		
		// add a gp to the gp table (no hl7query prefix)
		Context.getAdministrationService().saveGlobalProperty(new GlobalProperty(GPNAME, "acustomvalue"));
		// add a simple template to our db
		HL7Template t = new HL7Template();
		t.setName("customgp");
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("Value is: ${ func.getGlobalProperty('" + GPNAME + "') }");
		Context.getService(HL7QueryService.class).saveHL7Template(t);
		
		HL7TemplateFunctions functions = new HL7TemplateFunctions();
		
		Assert.assertEquals("Value is: acustomvalue", functions.evaluateTemplate("customgp", null));
	}
}
