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

import junit.framework.Assert;

public class DefaultPatientIdentifierTest {
	
HL7QueryServiceImpl service;
	
	@Before
	public void beforeEachTest() {
	
	   service = new HL7QueryServiceImpl();
	 	 	 }
	
	@Test
    public void testPID3SegmentTemplate() throws Exception {

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

            PatientIdentifier patientIdentifier = new PatientIdentifier();
            patientIdentifier.setIdentifier("PATIENT IDENTIFIER");
            
            PatientIdentifierType patientIdentifierType= new PatientIdentifierType();
            patientIdentifierType.setName("IDENTIFIER TYPE");  
            patientIdentifier.setIdentifierType(patientIdentifierType);
            
            
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Templates/DefaultPatientIdentifier.xml");
    String xml = IOUtils.toString(inputStream);

    HL7Template template = new HL7Template();
    template.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
    template.setTemplate(xml);

    Map<String, Object> bindings = new HashMap<String, Object>();
    bindings.put("encounter", encounter);
    bindings.put("patientIdentifier", patientIdentifier);
    bindings.put("patientIdentifierType", patientIdentifierType);
    
    String evaluated = service.evaluateTemplate(template, bindings);
    
    Assert.assertTrue(evaluated.contains("<CX.1>PATIENT IDENTIFIER</CX.1>"));
    Assert.assertTrue(evaluated.contains("<CX.5>IDENTIFIER TYPE</CX.5>"));


}

}
