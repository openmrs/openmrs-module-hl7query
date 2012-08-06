package org.openmrs.module.hl7query;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;

public class HL7PID3DefaultPatientIdentifierTemplateTest {
HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
	
	   service = new HL7QueryServiceImpl();
	 	 	 }
	
	@Test
    public void testPID3SegmentTemplate() throws Exception {

            PatientIdentifier patientIdentifier = new PatientIdentifier();
            patientIdentifier.setIdentifier("PATIENT IDENTIFIER");
            
            PatientIdentifierType patientIdentifierType= new PatientIdentifierType();
            patientIdentifierType.setName("IDENTIFIER TYPE");  
            patientIdentifier.setIdentifierType(patientIdentifierType);
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/DefaultPatientIdentifier.xml");
    String xml = IOUtils.toString(inputStream);

    HL7Template template = new HL7Template();
    template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    template.setTemplate(xml);

    Map<String, Object> bindings = new HashMap<String, Object>();
    bindings.put("patientIdentifier", patientIdentifier);
    
    String evaluated = service.evaluateTemplate(template, bindings);
    
    Assert.assertTrue(evaluated.contains("<CX.1>PATIENT IDENTIFIER</CX.1>"));
    Assert.assertTrue(evaluated.contains("<CX.5>IDENTIFIER TYPE</CX.5>"));


}

}
