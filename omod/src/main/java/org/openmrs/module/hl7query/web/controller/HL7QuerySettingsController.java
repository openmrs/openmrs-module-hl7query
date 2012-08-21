package org.openmrs.module.hl7query.web.controller;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class HL7QuerySettingsController {
 
		@RequestMapping(value="/module/hl7query/settings")
        public void showSettings(ModelMap model) {
              //Display settings
        }
 
}