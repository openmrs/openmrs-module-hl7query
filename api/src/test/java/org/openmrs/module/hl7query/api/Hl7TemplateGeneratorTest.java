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
package org.openmrs.module.hl7query.api;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Class to test the hl7 template segments.
 */
public class Hl7TemplateGeneratorTest extends BaseModuleContextSensitiveTest {

	HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
		service = new HL7QueryServiceImpl();
	}
	
	@Test
	public void testPV1SegmentTemplate() throws Exception {
		
		Encounter encounter = new Encounter();
		
		Date date = new Date();
		encounter.setEncounterDatetime(date);
		
		EncounterType encounterType = new EncounterType();
		encounterType.setName("ENCOUNTER TYPE NAME");
		encounter.setEncounterType(encounterType);
		
		Location location = new Location(1);
		location.setUuid("LOCATION UUID");
		location.setName("LOCATION NAME");
		encounter.setLocation(location);
		
		Person provider = new Person(1);
		provider.setUuid("PROVIDER UUID");
		provider.addName(new PersonName("PROVIDER GIVENNAME",  "PROVIDER MIDDLENAME",  "PROVIDER FAMILYNAME"));
		encounter.setProvider(provider);
		
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("org/openmrs/module/hl7query/api/templates/PV1.xml");
    	String xml = IOUtils.toString(inputStream);
    	
    	HL7Template template = new HL7Template();
    	template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    	template.setTemplate(xml);
  
        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("encounter", encounter);
        
        String evaluated = service.evaluateTemplate(template, bindings);
	    
	    Assert.assertTrue(evaluated.contains("<PL.1>LOCATION UUID</PL.1>"));
	    Assert.assertTrue(evaluated.contains("<HD.1>LOCATION NAME</HD.1>"));
	    Assert.assertTrue(evaluated.contains("<PV1.4>ENCOUNTER TYPE NAME</PV1.4>"));
	    Assert.assertTrue(evaluated.contains("<XCN.1>PROVIDER UUID</XCN.1>"));
	    Assert.assertTrue(evaluated.contains("<FN.1>PROVIDER FAMILYNAME</FN.1>"));
	    Assert.assertTrue(evaluated.contains("<XCN.3>PROVIDER GIVENNAME</XCN.3>"));
    }
	
	@Test
	public void testOBREncSegmentTemplate() throws Exception {
		
		Encounter encounter = new Encounter();
		
		Date date = new Date();
		encounter.setEncounterDatetime(date);
		encounter.setUuid("ENCOUNTER UUID");
		
		EncounterType encounterType = new EncounterType();
		encounterType.setName("ENCOUNTER TYPE NAME");
		encounter.setEncounterType(encounterType);
		
		Location location = new Location(1);
		location.setUuid("LOCATION UUID");
		location.setName("LOCATION NAME");
		encounter.setLocation(location);
		
		Person provider = new Person(1);
		provider.setUuid("PROVIDER UUID");
		provider.addName(new PersonName("PROVIDER GIVENNAME",  "PROVIDER MIDDLENAME",  "PROVIDER FAMILYNAME"));
		encounter.setProvider(provider);
		
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("org/openmrs/module/hl7query/api/templates/OBR-ENC.xml");
    	String xml = IOUtils.toString(inputStream);
    	
    	HL7Template template = new HL7Template();
    	template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    	template.setTemplate(xml);
  
        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("encounter", encounter);
        bindings.put("encounterIndex", 3);
        
        String evaluated = service.evaluateTemplate(template, bindings);
	   
        Assert.assertTrue(evaluated.contains("<OBR.1>3</OBR.1>"));
        Assert.assertTrue(evaluated.contains("<EI.1>ENCOUNTER UUID</EI.1>"));
	    Assert.assertTrue(evaluated.contains("<OBR.20>LOCATION UUID</OBR.20>"));
	    Assert.assertTrue(evaluated.contains("<OBR.21>LOCATION NAME</OBR.21>"));
	    Assert.assertTrue(evaluated.contains("<CE.2>ENCOUNTER TYPE NAME</CE.2>"));
	    Assert.assertTrue(evaluated.contains("<XCN.1>PROVIDER UUID</XCN.1>"));
	    Assert.assertTrue(evaluated.contains("<FN.1>PROVIDER FAMILYNAME</FN.1>"));
	    Assert.assertTrue(evaluated.contains("<XCN.3>PROVIDER GIVENNAME</XCN.3>"));
    }
	
	@Test
	public void testPIDSegmentTemplate() throws Exception {
		executeDataSet("templatesTestData.xml");
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/PID.xml");
    	String xml = IOUtils.toString(inputStream);
    	
    	HL7Template template = new HL7Template();
    	template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    	template.setTemplate(xml);
  
    	Patient patient = new Patient(1);
        patient.setUuid("PROVIDER UUID");
        patient.addName(new PersonName("PROVIDER GIVENNAME1", "PROVIDER MIDDLENAME1", "PROVIDER FAMILYNAME1"));
        patient.addName(new PersonName("PROVIDER GIVENNAME2", "PROVIDER MIDDLENAME2", "PROVIDER FAMILYNAME2"));
        patient.addName(new PersonName("PROVIDER GIVENNAME3", "PROVIDER MIDDLENAME3", "PROVIDER FAMILYNAME3"));

        patient.addIdentifier(new PatientIdentifier("1", new PatientIdentifierType(), new Location()));
        patient.addIdentifier(new PatientIdentifier("2", new PatientIdentifierType(), new Location()));
        patient.addIdentifier(new PatientIdentifier("3", new PatientIdentifierType(), new Location()));
       
        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("index", 4);
        bindings.put("patient", patient);
        
        String evaluated = service.evaluateTemplate(template, bindings);
	   
        Assert.assertTrue(evaluated.contains("<PID.1>4</PID.1>"));
        Assert.assertTrue(evaluated.contains("PID.3 Template"));
        Assert.assertTrue(evaluated.contains("PID.5 Template"));
	}
}
