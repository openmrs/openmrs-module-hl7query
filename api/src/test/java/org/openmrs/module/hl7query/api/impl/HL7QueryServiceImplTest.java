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
    @Test(expected=HL7TemplateException.class)
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
    @Test(expected=APIException.class)
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
	    //TODO auto-generated
		Assert.fail("Not yet implemented");
    }
	
}