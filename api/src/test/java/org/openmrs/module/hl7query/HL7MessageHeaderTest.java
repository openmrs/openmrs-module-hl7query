package org.openmrs.module.hl7query;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.Assert;

public class HL7MessageHeaderTest extends MockBaseTest {

	@Test
	public void shouldEvaluateMessageHeaderTemplate() throws Exception {
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("MSH");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, null);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.4><HD.1>OPENMRS</HD.1></MSH.4>"));  //MSH-4: Source
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.6><HD.1></HD.1></MSH.6>"));  //MSH-6: Facility
		Assert.assertTrue(evaluatedTemplate.matches(".*<MSH\\.7><TS\\.1>\\d{12}\\d{2}?</TS\\.1></MSH\\.7>.*"));  //MSH-7: Date/Time
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.9><MSG.1>ORU</MSG.1><MSG.2>R01</MSG.2><MSG.3>ORU_R01</MSG.3></MSH.9>"));  //MSH-9: Message Type
		Assert.assertTrue(evaluatedTemplate.matches(".*<MSH\\.10>[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}</MSH\\.10>.*"));  //MSH-10: Message ID
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.11><PT.1>D</PT.1><PT.2>C</PT.2></MSH.11>"));  //MSH-11: Processing Info
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.12><VID.1>2.5</VID.1><VID.2><CE.1>RWA</CE.1></VID.2></MSH.12>"));  //MSH-12: Version and I18N Code
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.21><EI.1>CLSM_V0.83</EI.1></MSH.21></MSH>"));  //MSH-21: Profile
		
	}
	
}
