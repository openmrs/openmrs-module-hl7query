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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;

public class HL7CompleteORUR01TemplateTest extends MockBaseTest {
	
	@Test
	public void shouldParseHL7StringToORUR01Object() throws Exception {
		Patient patient = new Patient();
		List<Encounter> encounters = new ArrayList<Encounter>();
		
		String locationUUID = UUID.randomUUID().toString();
		
		Location location = new Location();
		location.setUuid(locationUUID);
		location.setName("locationName");
		
		Date encounterDatetime = new Date();
		
		Encounter encounter = new Encounter();
		encounter.setPatient(patient);
		encounter.setLocation(location);
		encounter.setEncounterType(new EncounterType("encounterTypeName", ""));
		encounter.setEncounterDatetime(encounterDatetime);
		
		encounters.add(encounter);
		
		Date encounter2Datetime = new Date();
		
		Encounter encounter2 = new Encounter();
		encounter2.setPatient(patient);
		encounter2.setLocation(location);
		encounter2.setEncounterType(new EncounterType("encounter2TypeName", ""));
		encounter2.setEncounterDatetime(encounter2Datetime);
		
		encounters.add(encounter2);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("patient", patient);
		bindings.put("encounters", encounters);
		
		//when
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic ORUR01");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		
		//Parsing the String using HAPI will validate if it contains a properly 
		//formatted ORUR01 message
		GenericParser parser = new GenericParser();
		Message message;
		try {
			message = parser.parse(evaluatedTemplate);
		}
		catch (EncodingNotSupportedException e) {
			//TODO Un comment the line below after the MSH segment is finished
			//Assert.assertTrue(false);
		}
		catch (HL7Exception e) {
			Assert.assertTrue(false);
		}
		
	}
	
	@Test
	public void shouldEvaluateCompleteORUR01Template() throws Exception {
		//given
		Patient patient = new Patient();
		List<Encounter> encounters = new ArrayList<Encounter>();
		
		String locationUUID = "8bf21a4b-dc7e-4bd2-92f6-fac5eedf7db9";
		
		Location location = new Location();
		location.setUuid(locationUUID);
		location.setName("locationName");
		
		Date encounterDatetime = new Date();
		
		Encounter encounter = new Encounter();
		encounter.setPatient(patient);
		encounter.setLocation(location);
		encounter.setEncounterType(new EncounterType("encounterTypeName", ""));
		encounter.setEncounterDatetime(encounterDatetime);
		
		encounters.add(encounter);
		
		Date encounter2Datetime = new Date();
		
		Encounter encounter2 = new Encounter();
		encounter2.setPatient(patient);
		encounter2.setLocation(location);
		encounter2.setEncounterType(new EncounterType("encounter2TypeName", ""));
		encounter2.setEncounterDatetime(encounter2Datetime);
		
		encounters.add(encounter2);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("patient", patient);
		bindings.put("encounters", encounters);
		
		//when
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic ORUR01");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//TODO Add the ORU_R01.ORDER_OBSERVATION tags(obs) to the test file 'expectedORUR01Output.xml'
		//When https://tickets.openmrs.org/browse/HLQRY-38 is completed
		String expectedOutput = IOUtils
		        .toString(getClass().getClassLoader().getResourceAsStream("expectedORUR01Output.xml"));
		expectedOutput = StringUtils.deleteWhitespace(expectedOutput);
		
		HL7TemplateFunctions func = new HL7TemplateFunctions();
		
		//replace the encounter datetime place holders with the actual dates
		expectedOutput = StringUtils.replace(expectedOutput, "${ENCOUNTER_1_DATETIME}",
		    func.formatDate(encounterDatetime, null));
		expectedOutput = StringUtils.replace(expectedOutput, "${ENCOUNTER_2_DATETIME}",
		    func.formatDate(encounter2Datetime, null));
		
		//check for for the text in the MSH tag
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.4><HD.1>OPENMRS</HD.1></MSH.4>")); //MSH-4: Source
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.6><HD.1></HD.1></MSH.6>")); //MSH-6: Facility
		Assert.assertTrue(evaluatedTemplate.matches(".*<MSH\\.7><TS\\.1>\\d{12}\\d{2}?</TS\\.1></MSH\\.7>.*")); //MSH-7: Date/Time
		Assert.assertTrue(evaluatedTemplate
		        .contains("<MSH.9><MSG.1>ORU</MSG.1><MSG.2>R01</MSG.2><MSG.3>ORU_R01</MSG.3></MSH.9>")); //MSH-9: Message Type
		Assert.assertTrue(evaluatedTemplate
		        .matches(".*<MSH\\.10>[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}</MSH\\.10>.*")); //MSH-10: Message ID
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.11><PT.1>D</PT.1><PT.2>C</PT.2></MSH.11>")); //MSH-11: Processing Info
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.12><VID.1>2.5</VID.1><VID.2><CE.1>RWA</CE.1></VID.2></MSH.12>")); //MSH-12: Version and I18N Code
		Assert.assertTrue(evaluatedTemplate.contains("<MSH.21><EI.1>CLSM_V0.83</EI.1></MSH.21></MSH>")); //MSH-21: Profile
		
		//Check that the rest of the text from the other templates exists, i.e after '</MSH>' tag
		Assert.assertEquals(expectedOutput.substring(expectedOutput.indexOf("</MSH>") + 6),
		    evaluatedTemplate.substring(evaluatedTemplate.indexOf("</MSH>") + 6));
	}
}
