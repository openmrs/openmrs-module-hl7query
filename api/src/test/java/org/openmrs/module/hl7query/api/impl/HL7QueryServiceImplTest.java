package org.openmrs.module.hl7query.api.impl;


import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.APIException;
import org.openmrs.module.hl7query.Template;
import org.openmrs.module.hl7query.TemplateException;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;

public class HL7QueryServiceImplTest extends BaseModuleContextSensitiveTest {
	
	HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
		service = new HL7QueryServiceImpl();
	}

	/**
     * @see HL7QueryServiceImpl#evaluateTemplate(Template,Map)
     * @verifies evaluate a groovy template
     */
    @Test
    public void evaluateTemplate_shouldEvaluateAGroovyTemplate() throws Exception {
    	Template t = new Template();
    	t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    	t.setTemplate("Easy as ${ list.join(', ') }");
    	
    	Map<String, Object> bindings = new HashMap<String, Object>();
    	bindings.put("list", Arrays.asList(1, 2, 3));
    	
	    String evaluated = service.evaluateTemplate(t, bindings);
	    Assert.assertEquals("Easy as 1, 2, 3", evaluated);
    }

	/**
     * @see HL7QueryServiceImpl#evaluateTemplate(Template,Map)
     * @verifies fail to evaluate a groovy template against bad input
     */
    @Test(expected=TemplateException.class)
    public void evaluateTemplate_shouldFailToEvaluateAGroovyTemplateAgainstBadInput() throws Exception {
    	Template t = new Template();
    	t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    	t.setTemplate("Easy as ${ list.join(', ') }");
    	
    	Map<String, Object> bindings = new HashMap<String, Object>();
    	
	    service.evaluateTemplate(t, bindings);
    }

	/**
     * @see HL7QueryServiceImpl#evaluateTemplate(Template,Map)
     * @verifies fail to evaluate a template of an unknown language
     */
    @Test(expected=APIException.class)
    public void evaluateTemplate_shouldFailToEvaluateATemplateOfAnUnknownLanguage() throws Exception {
    	Template t = new Template();
    	t.setLanguage("C++");
    	t.setTemplate("Easy as 1, 2, 3");
    	
	    service.evaluateTemplate(t, null);
    }
	
    /**
     * @see {@link HL7QueryService#renderPipeDelimitedORUR01(String)}
     */
    @Test
    @Verifies(value = "should return pipe delimited hl7 message", method = "renderPipeDelimitedORUR01(String)")
    public void renderPipeDelimitedORUR01_shouldReturnPipeDelimitedHl7Message() throws Exception {
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("org/openmrs/module/hl7query/api/impl/sample-hl7.xml");
    	String xml = IOUtils.toString(inputStream);
    	String output = service.renderPipeDelimitedORUR01(xml);
    	Assert.assertNotNull(output);
    }
}