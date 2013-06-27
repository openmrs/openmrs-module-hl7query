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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.SenderProfile;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The controller for the manage templates jsp
 */

@Controller
public class HL7TemplateController extends SimpleFormController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@RequestMapping(value = "/module/hl7query/hl7Template")
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj,
	                                BindException errors) throws Exception {
		
		HttpSession httpSession = request.getSession();
		String view = getFormView();
		ModelAndView toReturn = new ModelAndView(new RedirectView(view));
		
		if (Context.isAuthenticated()) {
				
				HL7Template hl7Template = (HL7Template) obj;
				HL7QueryService qs = Context.getService(HL7QueryService.class);
				
				//to save the patient identifier type
				if (request.getParameter("save") != null) {
					
					SenderProfile sp = new SenderProfile();
					sp.setAction("dummy action");
					sp.setMessage_format("dummy messseage format");
					sp.setName("dummy name");
					sp.setProfile_status(true);
					sp.setUrl("dummy_url");
					
					qs.saveHL7Template(hl7Template);
					httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "hl7query.saved");
					toReturn = new ModelAndView(new RedirectView("hl7Template.form?hl7TemplateId=" + hl7Template.getHl7TemplateId()));
				}
				
				//to retire the patient identifier type
				if (request.getParameter("retire") != null) {	
					String retireReason = request.getParameter("retireReason");
					if (hl7Template.getHl7TemplateId() != null && !(StringUtils.hasText(retireReason))) {
						errors.reject("retireReason", "general.retiredReason.empty");
						return showForm(request, response, errors);
					}
					qs.retireHL7Template(hl7Template, retireReason);
					httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "hl7query.retired");
					toReturn = new ModelAndView(new RedirectView("hl7Template.form?hl7TemplateId=" + hl7Template.getHl7TemplateId()));
				}
				
				//to restore the patient identifier type
				if (request.getParameter("restore") != null) {	
					if (hl7Template.getHl7TemplateId() != null) {
						qs.unretireHL7Template(hl7Template);
						httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "hl7query.restored");
						toReturn = new ModelAndView(new RedirectView("hl7Template.form?hl7TemplateId=" + hl7Template.getHl7TemplateId()));
					}
				}
		}
		
		return toReturn;
	}
	
	@RequestMapping(value = "/module/hl7query/hl7Template")
	public void showHL7Templates(ModelMap model,  HttpServletRequest request) {
		HL7Template hl7Template = null;
		
		if (Context.isAuthenticated()) {
			HL7QueryService qs = Context.getService(HL7QueryService.class);
			String hl7TemplateId = request.getParameter("hl7TemplateId");
			if (hl7TemplateId != null)
				hl7Template = qs.getHL7Template(Integer.valueOf(hl7TemplateId));
		}
		
		if (hl7Template == null)
			hl7Template = new HL7Template();
		
		model.addAttribute("hl7Template", hl7Template);
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	protected Map<String, Object> referenceData(HttpServletRequest request, Object obj, Errors errs) throws Exception {
		
		HL7Template hl7Template = (HL7Template) obj;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (Context.isAuthenticated()) {
			if (hl7Template != null)
				map.put("hl7Template", hl7Template);
		}
		
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		HL7Template hl7Template = null;
		
		if (Context.isAuthenticated()) {
			HL7QueryService qs = Context.getService(HL7QueryService.class);
			String hl7TemplateId = request.getParameter("hl7TemplateId");
			if (hl7TemplateId != null)
				hl7Template = qs.getHL7Template(Integer.valueOf(hl7TemplateId));
		}
		
		if (hl7Template == null)
			hl7Template = new HL7Template();
		
		return hl7Template;
	}
}
