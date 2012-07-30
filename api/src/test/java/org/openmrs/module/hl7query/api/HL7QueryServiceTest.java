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
package org.openmrs.module.hl7query.api;

import static org.junit.Assert.assertNotNull;
import junit.framework.Assert;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link $ HL7QueryModuleService} .
 */
public class HL7QueryServiceTest extends BaseModuleContextSensitiveTest {
	
	public HL7QueryService getService() {
		return Context.getService(HL7QueryService.class);
	}
	
	@Test
	public void shouldSetupContext() {
		assertNotNull(getService());
	}
	
	/**
	 * @see HL7QueryService#getHL7Template(Integer)
	 * @verifies return template if exists
	 */
	@Test
	public void getHL7Template_shouldReturnTemplateIfExists() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		
		//when
		HL7Template existingTemplate = getService().getHL7Template(template.getId());
		
		//then
		Assert.assertNotNull(existingTemplate);
		Assert.assertEquals(template.getId(), existingTemplate.getId());
	}
	
	/**
	 * @see HL7QueryService#getHL7Template(Integer)
	 * @verifies return null if does not exist
	 */
	@Test
	public void getHL7Template_shouldReturnNullIfDoesNotExist() throws Exception {
		//given
		
		//when
		HL7Template existingTemplate = getService().getHL7Template(1);
		
		//then
		Assert.assertNull(existingTemplate);
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplateByName(String)
	 * @verifies return template if exists
	 */
	@Test
	public void getHL7TemplateByName_shouldReturnTemplateIfExists() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		
		template = new HL7Template();
		template.setName("different name");
		template.setTemplate("different template");
		template = getService().saveHL7Template(template);
		
		//when
		HL7Template existingTemplate = getService().getHL7TemplateByName("name");
		
		//then
		Assert.assertNotNull(existingTemplate);
		Assert.assertEquals("name", existingTemplate.getName());
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplateByName(String)
	 * @verifies return null if does not exist
	 */
	@Test
	public void getHL7TemplateByName_shouldReturnNullIfDoesNotExist() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("different name");
		template.setTemplate("different template");
		template = getService().saveHL7Template(template);
		
		//when
		HL7Template existingTemplate = getService().getHL7TemplateByName("name");
		
		//then
		Assert.assertNull(existingTemplate);
	}
	
	/**
	 * @see HL7QueryService#purgeHL7Template(HL7Template)
	 * @verifies purge template
	 */
	@Test
	public void purgeHL7Template_shouldPurgeTemplate() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		
		//when
		getService().purgeHL7Template(template);
		
		//then
		HL7Template existingTemplate = getService().getHL7Template(template.getId());
		Assert.assertNull(existingTemplate);
	}
	
	/**
	 * @see HL7QueryService#retireHL7Template(HL7Template,String)
	 * @verifies retire template
	 */
	@Test
	public void retireHL7Template_shouldRetireTemplate() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		
		//when
		getService().retireHL7Template(template, "retire reason");
		
		//then
		Assert.assertTrue(template.isRetired());
		Assert.assertEquals("retire reason", template.getRetireReason());
		Assert.assertEquals(Context.getAuthenticatedUser(), template.getRetiredBy());
	}
	
	/**
	 * @see HL7QueryService#saveHL7Template(HL7Template)
	 * @verifies save template
	 */
	@Test
	public void saveHL7Template_shouldSaveTemplate() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		
		//when
		template = getService().saveHL7Template(template);
		
		//then
		HL7Template existingTemplate = getService().getHL7Template(template.getId());
		
		//then
		Assert.assertNotNull(existingTemplate);
		Assert.assertEquals(template.getId(), existingTemplate.getId());
	}
	
	/**
	 * @see HL7QueryService#unretireHL7Template(HL7Template)
	 * @verifies unretire template
	 */
	@Test
	public void unretireHL7Template_shouldUnretireTemplate() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		getService().retireHL7Template(template, "retire reason");
		
		//when
		getService().unretireHL7Template(template);
		
		//then
		Assert.assertFalse(template.isRetired());
		Assert.assertNull(template.getRetireReason());
		Assert.assertNull(template.getRetiredBy());
	}
}
