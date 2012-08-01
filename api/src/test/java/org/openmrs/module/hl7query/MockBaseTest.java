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

import java.util.Arrays;
import java.util.Locale;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Setups basic mocks for tests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public abstract class MockBaseTest {
	
	@Before
	public void setupMocks() {
		AdministrationService administrationService = Mockito.mock(AdministrationService.class);
		Mockito.when(administrationService.getAllowedLocales()).thenReturn(Arrays.asList(Locale.ENGLISH));
		
		PowerMockito.mockStatic(Context.class);
		Mockito.when(Context.getLocale()).thenReturn(Locale.ENGLISH);
		Mockito.when(Context.getAdministrationService()).thenReturn(administrationService);
	}
	
}
