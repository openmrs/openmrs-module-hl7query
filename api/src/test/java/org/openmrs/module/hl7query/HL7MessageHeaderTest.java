package org.openmrs.module.hl7query;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.Assert;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;

public class HL7MessageHeaderTest extends MockBaseTest {

	@Test
	public void shouldEvaluateMessageHeaderTemplate() throws Exception {
		Map<String, Object> bindings = new HashMap<String, Object>();
		
		bindings.put("hl7query.facility", "testFacility");
		
		AdministrationService adminService = Context.getAdministrationService();
		
		adminService.saveGlobalProperty(new GlobalProperty("hl7query.messageSource", "OPENMRS"));
		
		List<GlobalProperty> props = adminService.getAllGlobalProperties();
		
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic MSH");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		Assert.assertTrue(evaluatedTemplate.contains("testFacility"));
		Assert.assertTrue(!evaluatedTemplate.contains("facilty fail!!!"));
		Assert.assertTrue(evaluatedTemplate.contains("OPENMRS"));
	}
	
}
