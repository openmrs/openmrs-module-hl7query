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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.api.HL7QueryService;
import org.openmrs.module.hl7query.api.db.HL7QueryDAO;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Setups basic mocks for tests.
 * <p>
 * It provides a spied HL7QueryServiceImpl, which can be accessed through
 * <code>Context.getService(HL7QueryService.class)</code> or the <code>hl7QueryService</code> field.
 * By default all methods from HL7QueryServiceImpl will be called, but you can stub some if you want
 * to return different results.
 * <p>
 * The spied HL7QueryServiceImpl uses a dao mock stored in the <code>hl7QueryDAOMock</code> field.
 * You must stub all methods in that mock, which are called by HL7QueryServiceImpl.
 * <p>
 * If you want to call <code>Context.getService(HL7QueryService.class).getHL7Template(1)</code>, you
 * need to precede it with <code> HL7Template someTemplate = new HL7Template(1);
 * Mockito.when(hl7QueryDAOMock.getHL7Template(1)).thenReturn(someTemplate);</code>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@PowerMockIgnore("org.apache.log4j.*")
public abstract class MockBaseTest {
	
	protected final HL7QueryService hl7QueryService;
	
	protected final HL7QueryDAO hl7QueryDAOMock;
	
	public MockBaseTest() {
		AdministrationService administrationService = Mockito.mock(AdministrationService.class);
		Mockito.when(administrationService.getAllowedLocales()).thenReturn(Arrays.asList(Locale.ENGLISH));
		Mockito.when(administrationService.getGlobalProperty("hl7query.messageSource")).thenReturn("OPENMRS");
		
		hl7QueryDAOMock = Mockito.mock(HL7QueryDAO.class);
		HL7QueryServiceImpl hl7QueryServiceImpl = Mockito.spy(new HL7QueryServiceImpl());
		hl7QueryServiceImpl.setDao(hl7QueryDAOMock);
		hl7QueryService = hl7QueryServiceImpl;
		
		PowerMockito.mockStatic(Context.class);
		Mockito.when(Context.getLocale()).thenReturn(Locale.ENGLISH);
		Mockito.when(Context.getAdministrationService()).thenReturn(administrationService);
		Mockito.when(Context.getService(HL7QueryService.class)).thenReturn(hl7QueryService);
		
		try {
			setupStandardTemplates();
		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void setupStandardTemplates() throws Exception {
		Map<String, String> templateNamesToPaths = new HashMap<String, String>();
		templateNamesToPaths.put("Generic PV1", "org/openmrs/module/hl7query/api/templates/PV1.xml");
		templateNamesToPaths.put("Generic OBX", "templates/obx_orur01.xml");
		templateNamesToPaths.put("Generic ORUR01", "templates/complete_orur01.xml");
		templateNamesToPaths.put("Generic PID", "templates/PID.xml");
		templateNamesToPaths.put("Generic Patient", "templates/patient_orur01.xml");
		templateNamesToPaths.put("Default Patient Identifier", "templates/DefaultPatientIdentifier.xml");
		templateNamesToPaths.put("Default Patient Name", "templates/DefaultPatientNameTemplate.xml");
		templateNamesToPaths.put("Generic MSH", "templates/MSH.xml");

		for (Entry<String, String> templateNameToPath : templateNamesToPaths.entrySet()) {
			InputStream resource = ClassLoader.getSystemResourceAsStream(templateNameToPath.getValue());
			HL7Template template = new HL7Template();
			template.setName(templateNameToPath.getKey());
			template.setTemplate(IOUtils.toString(resource));
			Mockito.when(hl7QueryDAOMock.getHL7TemplateByName(templateNameToPath.getKey())).thenReturn(template);
		}
	}
	
}
