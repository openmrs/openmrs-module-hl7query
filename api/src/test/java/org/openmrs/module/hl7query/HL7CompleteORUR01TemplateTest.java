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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HL7CompleteORUR01TemplateTest extends MockBaseTest {	
	
	@Test
	public void shouldEvaluateCompleteORUR01Template() throws Exception {
		//given
		
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		
		//when
		HL7Template hl7Template = hl7QueryService.getHL7TemplateByName("Generic ORUR01");
		String evaluatedTemplate = hl7QueryService.evaluateTemplate(hl7Template, bindings);
		
		//then
		
	}
}
