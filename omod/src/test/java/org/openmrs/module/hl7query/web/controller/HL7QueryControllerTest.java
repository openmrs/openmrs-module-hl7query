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
package org.openmrs.module.hl7query.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.Verifies;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Contains tests for the {@link HL7QueryController}
 */
public class HL7QueryControllerTest extends BaseModuleContextSensitiveTest {
	
	private static final String ENCOUNTER_1_UUID = "6519d653-393b-4118-9c83-a3715b82d4ac";
	
	private static final String MODULE_TEST_DATA_XML = "moduleTestData.xml";
	
	private HL7QueryService service;
	
	@Before
	public void before() throws Exception {
		service = Context.getService(HL7QueryService.class);
		executeDataSet(MODULE_TEST_DATA_XML);
		
		//Set the templates text for all the templates by loading them form the test template files
		HL7Template oruro1Template = service.getHL7TemplateByName("Generic ORUR01");
		oruro1Template.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
			"complete_orur01.xml")));
		
		HL7Template MSHTemplate = service.getHL7TemplateByName("MSH");
		MSHTemplate.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/MSH.xml")));
		
		HL7Template patientTemplate = service.getHL7TemplateByName("Generic Patient");
		patientTemplate.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/patient_orur01.xml")));
		
		HL7Template PIDTemplate = service.getHL7TemplateByName("Generic PID");
		PIDTemplate.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/PID.xml")));
		
		HL7Template patientNameTemplate = service.getHL7TemplateByName("Default Patient Name");
		patientNameTemplate.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/DefaultPatientNameTemplate.xml")));
		
		HL7Template patientIdTemplate = service.getHL7TemplateByName("Default Patient Identifier");
		patientIdTemplate.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/DefaultPatientIdentifier.xml")));
		
		//Why is this template located in a different places from the others
		HL7Template PV1Template = service.getHL7TemplateByName("Generic PV1");
		PV1Template.setTemplate(IOUtils.toString(HL7QueryControllerTest.class.getClassLoader().getResourceAsStream(
		    "templates/PV1.xml")));
		
		//TODO Set the template text for the template in https://tickets.openmrs.org/browse/HLQRY-38
		service.saveHL7Template(oruro1Template);
		service.saveHL7Template(MSHTemplate);
		service.saveHL7Template(patientTemplate);
		service.saveHL7Template(PIDTemplate);
		service.saveHL7Template(patientNameTemplate);
		service.saveHL7Template(patientIdTemplate);
		service.saveHL7Template(PV1Template);
	}
	
	/**
	 * @see {@link HL7QueryController#getEncounters(String,String,String,Date,Date,HttpServletRequest)}
	 */
	@Test
	@Verifies(value = "should return the expected hl7 output as xml if the xml header exists", method = "getEncounters(String,String,String,Date,Date,HttpServletRequest)")
	public void getEncounters_shouldReturnTheExpectedHl7OutputAsXmlIfTheXmlHeaderExists() throws Exception {
		//TODO Add the ORU_R01.ORDER_OBSERVATION tags(obs) to the test file 'expectedORUR01Output.xml'
		//When https://tickets.openmrs.org/browse/HLQRY-38 is completed
		String expectedOutput = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(
		    "expectedORUR01XmlOutput.xml"));
		expectedOutput = StringUtils.deleteWhitespace(expectedOutput);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/xml");
		String hl7Output = new HL7QueryController().getEncounters(null, null, ENCOUNTER_1_UUID, null, null, request)
		        .toString();
		hl7Output = StringUtils.deleteWhitespace(hl7Output);
		//Ignore timestamp by removing it
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<TS.1>", "</TS.1>"), "");
		//Ignore the uuid of the message
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<MSH.10>", "</MSH.10>"), "");
		
		Assert.assertEquals(expectedOutput, hl7Output);
	}
	
	/**
	 * @see {@link HL7QueryController#getEncounters(String,String,String,Date,Date,HttpServletRequest)}
	 */
	@Test
	@Verifies(value = "should return the expected hl7 in the format that matches the accept header value", method = "getEncounters(String,String,String,Date,Date,HttpServletRequest)")
	public void getEncounters_shouldReturnTheExpectedHl7InTheFormatThatMatchesTheAcceptHeaderValue() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		String hl7Output = new HL7QueryController().getEncounters(null, null, ENCOUNTER_1_UUID, null, null, request)
		        .toString();
		hl7Output = StringUtils.deleteWhitespace(hl7Output);
		
		//TODO Find a better way to test this
		//Check it contains series of pipes
		Assert.assertTrue(hl7Output.contains("||||||"));
		Assert.assertFalse(hl7Output.contains("<ORU_R01 xmlns=\"urn:hl7-org:v2xml\">"));
		Assert.assertFalse(hl7Output.contains("</ORU_R01>"));
	}
	
	/**
	 * @see {@link HL7QueryController#getEncounters(String,String,String,Date,Date,HttpServletRequest)}
	 */
	@Test
	@Verifies(value = "should return the patient encounters given the patient identifier and id type", method = "getEncounters(String,String,String,Date,Date,HttpServletRequest)")
	public void getEncounters_shouldReturnThePatientEncountersGivenThePatientIdentifierAndIdType() throws Exception {
		//TODO Add the ORU_R01.ORDER_OBSERVATION tags(obs) to the test file 'expectedOutput_encountersByPatient.xml'
		//When https://tickets.openmrs.org/browse/HLQRY-38 is completed
		String expectedOutput = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(
		    "expectedOutput_encountersByPatient.xml"));
		expectedOutput = StringUtils.deleteWhitespace(expectedOutput);
		
		final String identifier = "6TS-4";
		final String identifierTypeUuid = "1a339fe9-38bc-4ab3-b180-320988c0b968";
		final int expectedEncounterCount = 3;
		Patient patient = Context.getPatientService().getPatient(7);
		//sanity checks
		Assert.assertEquals(identifier, patient.getPatientIdentifier().getIdentifier());
		Assert.assertEquals(identifierTypeUuid, patient.getPatientIdentifier().getIdentifierType().getUuid());
		Assert.assertEquals(expectedEncounterCount, Context.getEncounterService().getEncountersByPatient(patient).size());
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/xml");
		String hl7Output = new HL7QueryController().getEncounters(identifier, identifierTypeUuid, null, null, null, request)
		        .toString();
		
		hl7Output = StringUtils.deleteWhitespace(hl7Output);
		//Ignore timestamp by removing it
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<TS.1>", "</TS.1>"), "");
		//Ignore the uuid of the message
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<MSH.10>", "</MSH.10>"), "");
		
		Assert.assertEquals(expectedOutput, hl7Output);
	}
	
	/**
	 * @see {@link HL7QueryController#getEncounters(String,String,String,Date,Date,HttpServletRequest)}
	 */
	@Test
	@Verifies(value = "should return the patient encounters matching specified start and end encounter dates", method = "getEncounters(String,String,String,Date,Date,HttpServletRequest)")
	public void getEncounters_shouldReturnThePatientEncountersMatchingSpecifiedStartAndEndEncounterDates() throws Exception {
		//TODO Add the ORU_R01.ORDER_OBSERVATION tags(obs) to the test file 'expectedOutput_encountersByStartAndEndDate.xml'
		//When https://tickets.openmrs.org/browse/HLQRY-38 is completed
		String expectedOutput = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(
		    "expectedOutput_encountersByStartAndEndDate.xml"));
		expectedOutput = StringUtils.deleteWhitespace(expectedOutput);
		
		final String identifier = "6TS-4";
		final String identifierTypeUuid = "1a339fe9-38bc-4ab3-b180-320988c0b968";
		Patient patient = Context.getPatientService().getPatient(7);
		//sanity checks
		Assert.assertEquals(identifier, patient.getPatientIdentifier().getIdentifier());
		Assert.assertEquals(identifierTypeUuid, patient.getPatientIdentifier().getIdentifierType().getUuid());
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Accept", "text/xml");
		Encounter expectedEncounter = Context.getEncounterService().getEncounter(4);
		String hl7Output = new HL7QueryController().getEncounters(identifier, identifierTypeUuid, null,
		    expectedEncounter.getEncounterDatetime(), expectedEncounter.getEncounterDatetime(), request).toString();
		
		hl7Output = StringUtils.deleteWhitespace(hl7Output);
		//Ignore timestamp by removing it
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<TS.1>", "</TS.1>"), "");
		//Ignore the uuid of the message
		hl7Output = StringUtils.replace(hl7Output, StringUtils.substringBetween(hl7Output, "<MSH.10>", "</MSH.10>"), "");
		
		Assert.assertEquals(expectedOutput, hl7Output);
	}
}
