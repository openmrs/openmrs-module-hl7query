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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.HL7TemplateException;
import org.openmrs.module.hl7query.api.HL7QueryService;

public class HL7QueryServiceImplTest {
	
	HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
		service = new HL7QueryServiceImpl();
	}
	
	/**
	 * @see HL7QueryServiceImpl#evaluateTemplate(HL7Template,Map)
	 * @verifies evaluate a groovy template
	 */
	@Test
	public void evaluateTemplate_shouldEvaluateAGroovyTemplate() throws Exception {
		HL7Template t = new HL7Template();
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("Easy as ${ list.join(', ') }");
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("list", Arrays.asList(1, 2, 3));
		
		String evaluated = service.evaluateTemplate(t, bindings);
		Assert.assertEquals("Easy as 1, 2, 3", evaluated);
	}
	
	/**
	 * @see HL7QueryServiceImpl#evaluateTemplate(HL7Template,Map)
	 * @verifies fail to evaluate a groovy template against bad input
	 */
	@Test(expected = HL7TemplateException.class)
	public void evaluateTemplate_shouldFailToEvaluateAGroovyTemplateAgainstBadInput() throws Exception {
		HL7Template t = new HL7Template();
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("Easy as ${ list.join(', ') }");
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		
		service.evaluateTemplate(t, bindings);
	}
	
	/**
	 * @see HL7QueryServiceImpl#evaluateTemplate(HL7Template,Map)
	 * @verifies fail to evaluate a template of an unknown language
	 */
	@Test(expected = APIException.class)
	public void evaluateTemplate_shouldFailToEvaluateATemplateOfAnUnknownLanguage() throws Exception {
		HL7Template t = new HL7Template();
		t.setLanguage("C++");
		t.setTemplate("Easy as 1, 2, 3");
		
		service.evaluateTemplate(t, null);
	}
	
	/**
	 * @see HL7QueryServiceImpl#evaluateTemplate(HL7Template,Map)
	 * @verifies add the HL7TemplateFunctions class as func to bindings
	 */
	@Test
	public void evaluateTemplate_shouldAddTheHL7TemplateFunctionsClassAsFuncToBindings() throws Exception {
		
		// set up mock of Context
		
		// set up mock of Adminservice.getGlobalProperty method
		// property named "myproperty" will be called and value of "myvalue"
		
		// create new service object using that mock Context
		
		HL7Template t = new HL7Template();
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("The value of myproperty is: ${ func.getGlobalProperty('myproperty') }");
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		
		String evaluated = service.evaluateTemplate(t, bindings);
		Assert.assertEquals("The value of myproperty is: myvalue", evaluated);
	}
	
}
