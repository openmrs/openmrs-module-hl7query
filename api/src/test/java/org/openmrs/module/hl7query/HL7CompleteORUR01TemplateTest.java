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

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;

@Ignore
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
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
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
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//then
		Assert.assertEquals("<ORU_R01xmlns=\"urn:hl7-org:v2xml\"><ORU_R01.PATIENT_RESULT><ORU_R01.PATIENT><PID><PID.1>1</PID.1></PID><ORU_R01.VISIT><PV1><PV1.2>0</PV1.2>"
		            + "<PV1.3><PL.1>"
		            + locationUUID
		            + "</PL.1><PL.4><HD.1>locationName</HD.1></PL.4></PV1.3>"
		            + "<PV1.4>encounterTypeName</PV1.4><PV1.7><XCN.1>null</XCN.1><XCN.2><FN.1>null</FN.1></XCN.2><XCN.3>null</XCN.3><XCN.13>NID</XCN.13></PV1.7>"
		            + "<PV1.44><TS.1>"
		            + StringUtils.deleteWhitespace(new HL7TemplateFunctions().formatDate(encounterDatetime, null))
		            + "</TS.1></PV1.44></PV1></ORU_R01.VISIT>"
		            + "</ORU_R01.PATIENT><ORU_R01.PATIENT><PID><PID.1>1</PID.1></PID>"
		            + "<ORU_R01.VISIT><PV1><PV1.2>0</PV1.2>"
		            + "<PV1.3><PL.1>"
		            + locationUUID
		            + "</PL.1><PL.4><HD.1>locationName</HD.1></PL.4></PV1.3>"
		            + "<PV1.4>encounter2TypeName</PV1.4><PV1.7><XCN.1>null</XCN.1><XCN.2><FN.1>null</FN.1></XCN.2><XCN.3>null</XCN.3><XCN.13>NID</XCN.13></PV1.7>"
		            + "<PV1.44><TS.1>" + StringUtils.deleteWhitespace(new HL7TemplateFunctions().formatDate(encounter2Datetime, null))
		            + "</TS.1></PV1.44></PV1></ORU_R01.VISIT></ORU_R01.PATIENT></ORU_R01>", evaluatedTemplate);
	}
}
