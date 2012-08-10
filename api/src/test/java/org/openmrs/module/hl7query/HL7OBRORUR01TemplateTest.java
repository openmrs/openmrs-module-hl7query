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
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.ConceptSource;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.PersonName;


/**
 * Tests creation of OBR Obs group segment of an ORUR01 message
 */
public class HL7OBRORUR01TemplateTest extends MockBaseTest {

	@Test
	public void shouldEvaluateOBRORUR01TemplateForOBSGroup() throws Exception {
		//given
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
		
		
		ConceptSource source = new ConceptSource();
		source.setName("AMPATH");
		
		ConceptMap map = new ConceptMap();
		map.setSourceCode("200");
		map.setSource(source);
		
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.NUMERIC_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.NUMERIC);
		
		ConceptNumeric concept = new ConceptNumeric();
		concept.setDatatype(datatype);
		concept.addConceptMapping(map);
		concept.addName(new ConceptName("NumericConcept", Locale.ENGLISH));
		concept.setUnits("mg");
		
		Date dateCreated = new Date(213231421890234L);
		
		Obs obs = new Obs(2);
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueNumeric(10d);
		
		obs.setObsGroup(getObsGroup());
		obs.getObsGroup().addGroupMember(obs);
		
		encounter.addObs(obs);
		
		obs = new Obs(3);
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueNumeric(23d);
		encounter.addObs(obs);
		
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("encounter", encounter);
		
		//when
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic Obs Group");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		
		//then
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);		
		Assert.assertEquals("<ORU_R01.ORDER_OBSERVATION><OBR><OBR.1>0</OBR.1><OBR.4><CE.1>100</CE.1>" +
				"<CE.2>MEDICALRECORDOBSERVATIONS</CE.2><CE.3>LOCAL</CE.3></OBR.4><OBR.18>0</OBR.18>" +
				"<OBR.29><EIP.2><EI.3>ENCOUNTERUUID</EI.3></EIP.2></OBR.29></OBR><ORU_R01.OBSERVATION>" +
				"<OBX><OBX.1>1</OBX.1><OBX.2>NM</OBX.2><OBX.3><CE.1>200</CE.1><CE.2>NumericConcept</CE.2>" +
				"<CE.3>AMPATH</CE.3></OBX.3><OBX.5>23.0</OBX.5><OBX.6><CE.1>mg</CE.1><CE.3>UCUM</CE.3></OBX.6>" +
				"<OBX.14><TS.1>" + new HL7TemplateFunctions().formatDate(dateCreated, null) + "</TS.1>" +
				"</OBX.14></OBX></ORU_R01.OBSERVATION></ORU_R01.ORDER_OBSERVATION>" +
				"<ORU_R01.ORDER_OBSERVATION><OBR><OBR.1>2</OBR.1><OBR.4><CE.1>100</CE.1><CE.2>MedSet</CE.2>" +
				"<CE.3>LOCAL</CE.3></OBR.4><OBR.18>0</OBR.18><OBR.29><EIP.2><EI.3>ENCOUNTERUUID</EI.3></EIP.2></OBR.29" +
				"></OBR><ORU_R01.OBSERVATION><OBX><OBX.1>2</OBX.1><OBX.2>NM</OBX.2><OBX.3><CE.1>200</CE.1>" +
				"<CE.2>NumericConcept</CE.2><CE.3>AMPATH</CE.3></OBX.3><OBX.5>10.0</OBX.5><OBX.6><CE.1>mg</CE.1>" +
				"<CE.3>UCUM</CE.3></OBX.6><OBX.14><TS.1>" + new HL7TemplateFunctions().formatDate(dateCreated, null) +
				"</TS.1></OBX.14></OBX></ORU_R01.OBSERVATION></ORU_R01.ORDER_OBSERVATION>", evaluatedTemplate);
	}
	
	private Obs getObsGroup() {
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.CODED_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.CODED);
		
		Concept concept = new Concept(1);
		concept.setDatatype(datatype);
		concept.addName(new ConceptName("MedSet", Locale.ENGLISH));
		
		ConceptSource source = new ConceptSource();
		source.setName("LOCAL");
		
		ConceptMap map = new ConceptMap();
		map.setSourceCode("100");
		map.setSource(source);

		concept.addConceptMapping(map);
		
		Date dateCreated = new Date(213231421890234L);
		
		Obs obs = new Obs(1);
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		
		return obs;
	}
}
