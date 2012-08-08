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

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;

public class HL7PatientORUR01TemplateTest extends MockBaseTest {
	
	@Test
	public void shouldEvaluatePatientORUR01Template() throws Exception {
		//given
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
		location.setName("locationName");
		
		Date encounterDatetime = new Date(213231421890234L);
		
		Encounter encounter = new Encounter();
		encounter.setLocation(location);
		encounter.setEncounterType(new EncounterType("encounterTypeName", ""));
		encounter.setEncounterDatetime(encounterDatetime);
		encounter.setPatient(patient);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("encounter", encounter);
		
		//when
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic Patient");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//then
		Assert.assertEquals("<ORU_R01.PATIENT><PID><PID.1>1</PID.1><PID.3><CX.1>PATIENTIDENTIFIER</CX.1>"
		        + "<CX.5>IDENTIFIERTYPE</CX.5></PID.3><PID.3><CX.1>PATIENTIDENTIFIER2</CX.1>"
		        + "<CX.5>IDENTIFIERTYPE</CX.5></PID.3><PID.5><XPN.1><FN.1>PATIENTFAMILYNAME</FN.1></XPN.1>"
		        + "<XPN.2>PATIENTGIVENNAME</XPN.2><XPN.3>PATIENTMIDDLENAME</XPN.3></PID.5></PID>"
		        + "<ORU_R01.VISIT><PV1><PV1.2>0</PV1.2><PV1.3><PL.1>3d8bb0d6-6b5b-4f6d-92a3-d9b8e4972d48</PL.1>"
		        + "<PL.4><HD.1>locationName</HD.1></PL.4></PV1.3><PV1.4>encounterTypeName</PV1.4><PV1.7>"
		        + "<XCN.1>null</XCN.1><XCN.2><FN.1>null</FN.1></XCN.2><XCN.3>null</XCN.3><XCN.13>NID</XCN.13></PV1.7>"
		        + "<PV1.44><TS.1>" + new HL7TemplateFunctions().formatDate(encounterDatetime, null) + "</TS.1></PV1.44></PV1></ORU_R01.VISIT></ORU_R01.PATIENT>",
		    evaluatedTemplate);
	}
}
