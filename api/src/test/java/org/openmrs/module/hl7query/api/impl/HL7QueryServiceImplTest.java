package org.openmrs.module.hl7query.api.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.hl7query.TemplateException;

public class HL7QueryServiceImplTest {
	
	HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
		service = new HL7QueryServiceImpl();
	}
	
	/**
	 * Integration test for evaluating Groovy templates
	 * @see HL7QueryServiceImpl#prepareGroovyTemplate(String)
	 * @see HL7QueryServiceImpl#evaluateGroovyTemplate(String,Map)
	 * @verifies evaluate a template with null bindings
	 */
	@Test
	public void test_groovyTemplateIntegration1() throws Exception {
		String evaluated = service.evaluate(service.prepareGroovyTemplate("Easy as ${ [ 1, 2, 3 ].join(', ') }"), null);
		Assert.assertEquals("Easy as 1, 2, 3", evaluated);
	}
	
	/**
	 * Integration test for evaluating Groovy templates
	 * @see HL7QueryServiceImpl#prepareGroovyTemplate(String)
	 * @see HL7QueryServiceImpl#evaluateGroovyTemplate(String,Map)
	 * @verifies evaluate a template with bindings
	 */
	@Test
	public void test_groovyTemplateIntegration2() throws Exception {
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("list", Arrays.asList("1", "2", "3"));
		String evaluated = service.evaluate(service.prepareGroovyTemplate("Easy as ${ list.join(', ') }"), bindings);
		Assert.assertEquals("Easy as 1, 2, 3", evaluated);
	}
	
	/**
	 * Integration test for evaluating Groovy templates
	 * @see HL7QueryServiceImpl#prepareGroovyTemplate(String)
	 * @see HL7QueryServiceImpl#evaluateGroovyTemplate(String,Map)
	 * @verifies fail to evaluate a template that references a variable not provided by bindings
	 */
	@Test(expected=TemplateException.class)
	public void test_groovyTemplateIntegration3()
	    throws Exception {
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("list", Arrays.asList("1", "2", "3"));
		service.evaluate(service.prepareGroovyTemplate("Easy as ${ wrongName.join(', ') }"), bindings);
	}
}