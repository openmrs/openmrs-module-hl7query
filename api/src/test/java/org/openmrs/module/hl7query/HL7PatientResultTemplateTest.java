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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;

/**
 * Contains methods for testing the Generic Patient Result template
 */
public class HL7PatientResultTemplateTest extends MockBaseTest {
	
	@Test
	public void shouldEvaluateOBXORUR01TemplateForNumericConcept() throws Exception {
		PatientIdentifierType patientIdentifierType = new PatientIdentifierType();
		patientIdentifierType.setName("IDENTIFIER TYPE");
		
		PatientIdentifier patientIdentifier = new PatientIdentifier();
		patientIdentifier.setIdentifier("PATIENT IDENTIFIER");
		patientIdentifier.setPreferred(true);
		patientIdentifier.setIdentifierType(patientIdentifierType);
		
		PatientIdentifier patientIdentifier2 = new PatientIdentifier();
		patientIdentifier2.setIdentifier("PATIENT IDENTIFIER2");
		patientIdentifier2.setPreferred(true);
		patientIdentifier2.setIdentifierType(patientIdentifierType);
		
		PersonName personName = new PersonName("PATIENT GIVENNAME", "PATIENT MIDDLENAME", "PATIENT FAMILYNAME");
		personName.setPreferred(true);
		
		PersonName personName2 = new PersonName("PATIENT GIVENNAME2", "PATIENT MIDDLENAME2", "PATIENT FAMILYNAME2");
		personName2.setPreferred(false);
		
		Patient patient = new Patient();
		patient.addIdentifier(patientIdentifier);
		patient.addIdentifier(patientIdentifier2);
		patient.addName(personName);
		patient.addName(personName2);
		
		String locationUUID = "3d8bb0d6-6b5b-4f6d-92a3-d9b8e4972d48";
		
		Location location = new Location();
		location.setUuid(locationUUID);
		location.setName("LOCATION NAME");
		
		Date encounterDatetime = new Date(213231421890234L);
		
		Encounter encounter = new Encounter();
		encounter.setLocation(location);
		encounter.setEncounterType(new EncounterType("ENCOUNTER TYPE NAME", ""));
		encounter.setEncounterDatetime(encounterDatetime);
		encounter.setPatient(patient);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("encounter", encounter);
		
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic Patient Result");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		
		//TODO Add the ORU_R01.ORDER_OBSERVATION tags(obs) to the test file 'expectedPatientResultOutput.xml'
		//When https://tickets.openmrs.org/browse/HLQRY-38 is completed
		String expected = IOUtils.toString(getClass().getClassLoader()
		        .getResourceAsStream("expectedPatientResultOutput.xml"));
		
		//remove tabs before comparing
		expected = StringUtils.replaceEach(evaluatedTemplate, new String[] { "\t" }, new String[] { "" });
		evaluatedTemplate = StringUtils.replace(evaluatedTemplate, "\t", "");
		Assert.assertEquals(expected, evaluatedTemplate);
	}
}
