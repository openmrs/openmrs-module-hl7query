package org.openmrs.module.hl7query.api;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;

import junit.framework.Assert;


public class DefaultPatientIdentifierTest {
	
	HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
	
	   service = new HL7QueryServiceImpl();
	 	 	 }
	 	
	@Test
	public void testDefaultPatientIdentifierTemplate() throws Exception{
		Encounter encounter = new Encounter();
		PatientIdentifier identifier = new PatientIdentifier();
		PatientIdentifierType idtype = new PatientIdentifierType ();
		identifier.setIdentifier("ID");
		idtype.setName("ID TYPE");
		identifier.setIdentifierType(idtype);
				
		Patient patient = new Patient();	
		patient.setUuid("PATIENT UUID");
		patient.addIdentifier(identifier);
		encounter.setPatient(patient);
										
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Templates/DefaultPatientIdentifier.xml");	 
	     String xml = IOUtils.toString(inputStream);
	     
	     HL7Template template = new HL7Template();
	     template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
	     template.setTemplate(xml);
	     
	     Map<String, Object> bindings=new HashMap<String,Object>();
	     bindings.put("encounter", encounter);
	     
	     String evaluated = service.evaluateTemplate(template, bindings);
	     
	     Assert.assertTrue(evaluated.contains("<CX.1>ID</CX.1>"));
	     Assert.assertTrue(evaluated.contains("<CX.5>ID TYPE</CX.5>"));
	          
	}

}


