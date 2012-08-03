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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.APIException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.util.HL7QueryConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The main controller.
 */
@Controller
public class HL7QueryController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/hl7query/ORUR01", method = RequestMethod.GET)
	@ResponseBody
	public Object getEncounters(@RequestParam(value = "patientId", required = false) String patientId,
	                            @RequestParam(value = "idTypeUuid", required = false) String idTypeUuid,
	                            @RequestParam(value = "encounterUuid", required = false) String encounterUuid,
	                            @RequestParam(value = "startDate", required = false) Date startDate,
	                            @RequestParam(value = "endDate", required = false) Date endDate, HttpServletRequest request) {
		
		List<Encounter> encounters = new ArrayList<Encounter>();
		EncounterService encounterService = Context.getEncounterService();
		HL7QueryService hL7QueryService = Context.getService(HL7QueryService.class);
		HL7Template template = null;
		Patient patient = null;
		if (StringUtils.isBlank(patientId) && StringUtils.isBlank(encounterUuid))
			throw new APIException("Patient identifier cannot be blank when the encounter uuid is also blank");
		
		String templateNameGP = Context.getAdministrationService().getGlobalProperty(HL7QueryConstants.HL7QUERY_GP_TEMPLATE);
		template = hL7QueryService.getHL7TemplateByName(templateNameGP);
		if (template == null)
			throw new APIException("Cannot find template with name '" + templateNameGP + "'");
		
		if (encounterUuid != null) {
			Encounter encounter = encounterService.getEncounterByUuid(encounterUuid);
			if (encounter == null)
				throw new APIException("Cannot find an encounter with uuid:" + encounterUuid);
			patient = encounter.getPatient();
			encounters.add(encounterService.getEncounterByUuid(encounterUuid));
		} else {
			PatientService patientService = Context.getPatientService();
			PatientIdentifierType identifierType = patientService.getPatientIdentifierTypeByUuid(idTypeUuid);
			if (identifierType == null)
				throw new APIException("Cannot find a patient identifier type with uuid:" + idTypeUuid);
			
			List<PatientIdentifierType> idTypes = new ArrayList<PatientIdentifierType>();
			idTypes.add(identifierType);
			List<Patient> patients = Context.getPatientService().getPatients(null, patientId, idTypes, true);
			if (patients.size() == 0)
				throw new APIException("Cannot find a patient with " + identifierType.getName() + " :" + patientId);
			else if (patients.size() > 1)
				throw new APIException("Found multiple patients with " + identifierType.getName() + " :" + patientId);
			
			patient = patients.get(0);
			encounters.addAll(encounterService.getEncounters(patient, null, startDate, endDate, null, null, null, false));
		}
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("patient", patient);
		bindings.put("encounters", encounters);
		
		String hl7Output = hL7QueryService.evaluateTemplate(template, bindings);
		
		String acceptHeader = request.getHeader("Accept");
		if (acceptHeader == null || !acceptHeader.contains("text/xml")) {
			//hl7Output = hL7QueryService.renderPipeDelimitedHl7(hl7Output);
		}
		
		return hl7Output;
	}
}
