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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.util.ErrorDetailsEnum;
import org.openmrs.module.hl7query.util.ExceptionUtil;
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
@RequestMapping(value = "/module/hl7query/ORUR01")
public class HL7QueryController extends BaseHL7QueryController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Processes requests to get encounters in hl7 format. Note that if the encounterUuid, then the
	 * patient identifier and the identifier type are not required otherwise they ares required
	 * 
	 * @param patientId the patient identifier
	 * @param idTypeUuid the patient identifier type
	 * @param encounterUuid the uuid of the encounter
	 * @param startDate Only encounters with an encounter datetime equal to or after this date will
	 *            be returned
	 * @param endDate Only encounters with an encounter datetime equal to or before this date will
	 *            be returned
	 * @param request the {@link HttpServletRequest} object
	 * @return the hl7 text
	 * @should return the expected hl7 output as xml if the xml header exists
	 * @should return the expected hl7 in the format that matches the accept header value
	 * @should return the patient encounters given the patient identifier and id type
	 * @should return the patient encounters matching specified start and end encounter dates
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getEncounters(@RequestParam(value = "patientId", required = false) String patientId,
	                            @RequestParam(value = "idTypeUuid", required = false) String idTypeUuid,
	                            @RequestParam(value = "encounterUuid", required = false) String encounterUuid,
	                            @RequestParam(value = "startDate", required = false) Date startDate,
	                            @RequestParam(value = "endDate", required = false) Date endDate, 
	                            HttpServletRequest request, HttpServletResponse response) {
		
		List<Encounter> encounters = new ArrayList<Encounter>();
		EncounterService encounterService = Context.getEncounterService();
		HL7QueryService hL7QueryService = Context.getService(HL7QueryService.class);
		HL7Template template = null;
		Patient patient = null;
		if (StringUtils.isBlank(patientId) && StringUtils.isBlank(encounterUuid))
			return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MISSING_IDENTIFIERS, null);
		
		String templateNameGP = Context.getAdministrationService().getGlobalProperty(
		    HL7QueryConstants.HL7QUERY_GP_ORUR01_TEMPLATE);
		template = hL7QueryService.getHL7TemplateByName(templateNameGP);
		if (template == null)
			return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MISSING_TEMPLATE, templateNameGP);
		
		if (encounterUuid != null) {
			Encounter encounter = encounterService.getEncounterByUuid(encounterUuid);
			if (encounter == null)
				return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MISSING_UUID, encounterUuid);
			patient = encounter.getPatient();
			encounters.add(encounterService.getEncounterByUuid(encounterUuid));
		} else {
			PatientService patientService = Context.getPatientService();
			PatientIdentifierType identifierType = patientService.getPatientIdentifierTypeByUuid(idTypeUuid);
			if (identifierType == null)
				return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MISSING_IDENTIFIER_TYPE, idTypeUuid);
			
			List<PatientIdentifierType> idTypes = new ArrayList<PatientIdentifierType>();
			idTypes.add(identifierType);
			List<Patient> patients = Context.getPatientService().getPatients(null, patientId, idTypes, true);
			if (patients.size() == 0)
				return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MISSING_PATIENT, identifierType.getName() + " " + patientId);
			else if (patients.size() > 1)
				return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.MULTIPLE_PATIENTS, identifierType.getName() + " " + patientId);
			
			patient = patients.get(0);
			encounters.addAll(encounterService.getEncounters(patient, null, startDate, endDate, null, null, null, false));
		}
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("patient", patient);
		bindings.put("encounters", encounters);
		
		String hl7Output = null;
		try{
			hl7Output = hL7QueryService.evaluateTemplate(template, bindings);
		}catch(Exception e){
			return ExceptionUtil.generateMessage(request, response, ErrorDetailsEnum.INTERNAL_SERVER_ERROR, e);
		}
		
		String acceptHeader = request.getHeader("Accept");
		if (acceptHeader == null || !acceptHeader.contains("text/xml")) {
			try {
				hl7Output = hL7QueryService.renderPipeDelimitedHl7(hl7Output);
			}
			catch (Exception e) {
				log.error("Internal error while processing the hl7 message", e);
			}
		}
		
		return hl7Output;
	}
}
