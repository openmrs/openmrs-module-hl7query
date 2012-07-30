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
package org.openmrs.module.hl7query.api.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.hl7query.HL7TemplateException;


/**
 * Integration tests for {@link GroovyTemplateFactory}
 */
public class GroovyTemplateFactoryTest {
	
	GroovyTemplateFactory factory;
	
	@Before
	public void beforeEachTest() {
		factory = new GroovyTemplateFactory();
	}
	
	
	@Test
	public void testEvaluatingTemplateWithNullBindings() throws Exception {
		String evaluated = factory.prepareTemplate("Easy as ${ [ 1, 2, 3 ].join(', ') }").evaluate(null);
		Assert.assertEquals("Easy as 1, 2, 3", evaluated);
	}
	
	@Test
	public void testEvaluatingTemplateWithBindings() throws Exception {
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("list", Arrays.asList("1", "2", "3"));
		String evaluated = factory.prepareTemplate("Easy as ${ list.join(', ') }").evaluate(bindings);
		Assert.assertEquals("Easy as 1, 2, 3", evaluated);
	}
	
	@Test(expected=HL7TemplateException.class)
	public void testFailureIfTemplateRefersToMissingProperties() throws Exception {
		Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("list", Arrays.asList("1", "2", "3"));
		factory.prepareTemplate("Easy as ${ wrongName.join(', ') }").evaluate(bindings);
	}
	
}
