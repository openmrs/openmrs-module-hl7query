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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.ConceptSource;
import org.openmrs.Obs;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;

public class HL7OBXORUR01TemplateTest extends MockBaseTest {
	
	@Test
	public void shouldEvaluateOBXORUR01TemplateForNumericConcept() throws Exception {
		//given
		InputStream resource = ClassLoader.getSystemResourceAsStream("templates/obx_orur01.xml");
		HL7Template template = new HL7Template();
		template.setName("obx_orur01");
		template.setTemplate(IOUtils.toString(resource));
		
		ConceptSource source = new ConceptSource();
		source.setName("PIH");
		
		ConceptMap map = new ConceptMap();
		map.setSourceCode("100");
		map.setSource(source);
		
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.NUMERIC_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.NUMERIC);
		
		ConceptNumeric concept = new ConceptNumeric();
		concept.setDatatype(datatype);
		concept.addConceptMapping(map);
		concept.addName(new ConceptName("NumericConcept", Locale.ENGLISH));
		concept.setUnits("mg");
		
		Date dateCreated = new Date();
		
		Obs obs = new Obs();
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueNumeric(10d);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("obsIndex", 0);
		bindings.put("obs", obs);
		bindings.put("implementationId", "MVP");
		
		//when
		String evaluatedTemplate = new HL7QueryServiceImpl().evaluateTemplate(template, bindings);
		
		//then
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		Assert.assertEquals(
		    "<ORU_R01.OBSERVATION><OBX>" + "<OBX.1>0</OBX.1><OBX.2>NM</OBX.2>"
		            + "<OBX.3><CE.1>100</CE.1><CE.2>NumericConcept</CE.2><CE.3>PIH</CE.3></OBX.3>" + "<OBX.5>10.0</OBX.5>"
		            + "<OBX.6><CE.1>mg</CE.1><CE.3>UCUM</CE.3></OBX.6>" + "<OBX.14><TS.1>"
		            + StringUtils.deleteWhitespace(dateCreated.toString()) + "</TS.1></OBX.14>"
		            + "</OBX></ORU_R01.OBSERVATION>", evaluatedTemplate);
	}
	
	@Test
	public void shouldEvaluateOBXORUR01TemplateForNumericConceptWithoutMapping() throws Exception {
		//given
		InputStream resource = ClassLoader.getSystemResourceAsStream("templates/obx_orur01.xml");
		HL7Template template = new HL7Template();
		template.setName("obx_orur01");
		template.setTemplate(IOUtils.toString(resource));
		
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.NUMERIC_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.NUMERIC);
		
		ConceptNumeric concept = new ConceptNumeric();
		concept.setId(1);
		concept.setDatatype(datatype);
		concept.addName(new ConceptName("NumericConcept", Locale.ENGLISH));
		concept.setUnits("mg");
		
		Date dateCreated = new Date();
		
		Obs obs = new Obs();
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueNumeric(10d);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("obsIndex", 0);
		bindings.put("obs", obs);
		bindings.put("implementationId", "MVP");
		
		//when
		String evaluatedTemplate = new HL7QueryServiceImpl().evaluateTemplate(template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//then
		Assert.assertEquals(
		    "<ORU_R01.OBSERVATION><OBX>" + "<OBX.1>0</OBX.1><OBX.2>NM</OBX.2>"
		            + "<OBX.3><CE.1>1</CE.1><CE.2>NumericConcept</CE.2><CE.3>MVP</CE.3></OBX.3>" + "<OBX.5>10.0</OBX.5>"
		            + "<OBX.6><CE.1>mg</CE.1><CE.3>UCUM</CE.3></OBX.6>" + "<OBX.14><TS.1>"
		            + StringUtils.deleteWhitespace(dateCreated.toString()) + "</TS.1></OBX.14>"
		            + "</OBX></ORU_R01.OBSERVATION>", evaluatedTemplate);
	}
	
	@Test
	public void shouldEvaluateOBXORUR01TemplateForCodedConceptWithoutMappings() throws Exception {
		//given
		InputStream resource = ClassLoader.getSystemResourceAsStream("templates/obx_orur01.xml");
		HL7Template template = new HL7Template();
		template.setName("obx_orur01");
		template.setTemplate(IOUtils.toString(resource));
		
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.CODED_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.CODED);
		
		Concept concept = new Concept(1);
		concept.setDatatype(datatype);
		concept.addName(new ConceptName("CodedConcept", Locale.ENGLISH));
		
		Concept conceptValue = new Concept(2);
		conceptValue.addName(new ConceptName("AnswerConcept", Locale.ENGLISH));
		
		Date dateCreated = new Date();
		
		Obs obs = new Obs();
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueCoded(conceptValue);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("obsIndex", 0);
		bindings.put("obs", obs);
		bindings.put("implementationId", "MVP");
		
		//when
		String evaluatedTemplate = new HL7QueryServiceImpl().evaluateTemplate(template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//then
		Assert.assertEquals(
		    "<ORU_R01.OBSERVATION><OBX>" + "<OBX.1>0</OBX.1><OBX.2>CWE</OBX.2>"
		            + "<OBX.3><CE.1>1</CE.1><CE.2>CodedConcept</CE.2><CE.3>MVP</CE.3></OBX.3>"
		            + "<OBX.5><CE.1>2</CE.1><CE.2>AnswerConcept</CE.2><CE.3>MVP</CE.3></OBX.5>" + "<OBX.14><TS.1>"
		            + StringUtils.deleteWhitespace(dateCreated.toString()) + "</TS.1></OBX.14>"
		            + "</OBX></ORU_R01.OBSERVATION>", evaluatedTemplate);
	}
	
	@Test
	public void shouldEvaluateOBXORUR01TemplateForCodedConcept() throws Exception {
		//given
		InputStream resource = ClassLoader.getSystemResourceAsStream("templates/obx_orur01.xml");
		HL7Template template = new HL7Template();
		template.setName("obx_orur01");
		template.setTemplate(IOUtils.toString(resource));
		
		ConceptDatatype datatype = new ConceptDatatype();
		datatype.setUuid(ConceptDatatype.CODED_UUID);
		datatype.setHl7Abbreviation(ConceptDatatype.CODED);
		
		Concept concept = new Concept(1);
		concept.setDatatype(datatype);
		concept.addName(new ConceptName("CodedConcept", Locale.ENGLISH));
		
		ConceptSource source = new ConceptSource();
		source.setName("PIH");
		
		ConceptMap map = new ConceptMap();
		map.setSourceCode("100");
		map.setSource(source);
		
		Concept conceptValue = new Concept(2);
		conceptValue.addName(new ConceptName("AnswerConcept", Locale.ENGLISH));
		conceptValue.addConceptMapping(map);
		
		Date dateCreated = new Date();
		
		Obs obs = new Obs();
		obs.setConcept(concept);
		obs.setDateCreated(dateCreated);
		obs.setValueCoded(conceptValue);
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("obsIndex", 0);
		bindings.put("obs", obs);
		bindings.put("implementationId", "MVP");
		
		//when
		String evaluatedTemplate = new HL7QueryServiceImpl().evaluateTemplate(template, bindings);
		evaluatedTemplate = StringUtils.deleteWhitespace(evaluatedTemplate);
		
		//then
		Assert.assertEquals(
		    "<ORU_R01.OBSERVATION><OBX>" + "<OBX.1>0</OBX.1><OBX.2>CWE</OBX.2>"
		            + "<OBX.3><CE.1>1</CE.1><CE.2>CodedConcept</CE.2><CE.3>MVP</CE.3></OBX.3>"
		            + "<OBX.5><CE.1>100</CE.1><CE.2>AnswerConcept</CE.2><CE.3>PIH</CE.3></OBX.5>" + "<OBX.14><TS.1>"
		            + StringUtils.deleteWhitespace(dateCreated.toString()) + "</TS.1></OBX.14>"
		            + "</OBX></ORU_R01.OBSERVATION>", evaluatedTemplate);
	}
}
