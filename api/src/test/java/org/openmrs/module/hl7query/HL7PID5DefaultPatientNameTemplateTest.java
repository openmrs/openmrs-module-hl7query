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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;

import junit.framework.Assert;

public class HL7PID5DefaultPatientNameTemplateTest {
HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
	
	   service = new HL7QueryServiceImpl();
	 	 	 }
	 	
	@Test
	public void testDefaultPatientNameTemplate() throws Exception{
		Encounter encounter = new Encounter();
		
		Patient patient = new Patient();	
		patient.setUuid("PATIENT UUID");
		patient.addName(new PersonName("PATIENT GIVENNAME", "PATIENT MIDDLENAME", "PATIENT FAMILYNAME"));		
		encounter.setPatient(patient);
		
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Templates/DefaultPatientNameTemplate.xml");	 
	     String xml = IOUtils.toString(inputStream);
	     
	     HL7Template template = new HL7Template();
	     template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
	     template.setTemplate(xml);
	     
	     Map<String, Object> bindings=new HashMap<String,Object>();
	     bindings.put("encounter", encounter);
	     
	     String evaluated = service.evaluateTemplate(template, bindings);
	     
	     Assert.assertTrue(evaluated.contains("<FN.1>PATIENT FAMILYNAME</FN.1>"));
	     Assert.assertTrue(evaluated.contains("<XPN.2>PATIENT GIVENNAME</XPN.2>"));
	     Assert.assertTrue(evaluated.contains("<XPN.3>PATIENT MIDDLENAME</XPN.3>"));
	     
	     
	}

}
