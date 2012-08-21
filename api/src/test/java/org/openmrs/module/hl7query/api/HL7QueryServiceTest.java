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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.hl7query.HL7Template;
import org.openmrs.module.hl7query.api.impl.HL7QueryServiceImpl;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.TestUtil;
import org.openmrs.test.Verifies;

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
	
	/**
	 * @see {@link HL7QueryService#getHL7Templates(null)}
	 */
	@Test
	@Verifies(value = "should exclude retired templates if include retired is set to false", method = "getHL7Templates(null)")
	public void getHL7Templates_shouldExcludeRetiredTemplatesIfIncludeRetiredIsSetToFalse() throws Exception {
		executeDataSet("moduleTestData.xml");
		Assert.assertEquals(10, getService().getHL7Templates(false).size());
	}
	
	/**
	 * @see {@link HL7QueryService#getHL7Templates(null)}
	 */
	@Test
	@Verifies(value = "should get all templates", method = "getHL7Templates(null)")
	public void getHL7Templates_shouldGetAllTemplates() throws Exception {
		executeDataSet("moduleTestData.xml");
		Assert.assertEquals(11, getService().getHL7Templates(true).size());
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplateByUuid(String)
	 * @verifies return null if does not exist
	 */
	@Test
	public void getHL7TemplateByUuid_shouldReturnNullIfDoesNotExist() throws Exception {
		//given
		
		//when
		HL7Template existingTemplate = getService().getHL7TemplateByUuid("nonexistinguuid");
		
		//then
		Assert.assertNull(existingTemplate);
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplateByUuid(String)
	 * @verifies return template if exists
	 */
	@Test
	public void getHL7TemplateByUuid_shouldReturnTemplateIfExists() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template = getService().saveHL7Template(template);
		
		//when
		HL7Template existingTemplate = getService().getHL7TemplateByUuid(template.getUuid());
		
		//then
		Assert.assertNotNull(existingTemplate);
		Assert.assertEquals(template.getId(), existingTemplate.getId());
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByEntity(String)
	 * @verifies return matching templates
	 */
	@Test
	public void getHL7TemplatesByEntity_shouldReturnMatchingTemplates() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name1");
		template.setTemplate("template");
		template.setHl7Entity("different entity");
		template = getService().saveHL7Template(template);
		
		template = new HL7Template();
		template.setName("name2");
		template.setTemplate("template");
		template.setHl7Entity("entity");
		template = getService().saveHL7Template(template);
		
		//when
		List<HL7Template> existingTemplates = getService().getHL7TemplatesByEntity("entity");
		
		//then
		Assert.assertNotNull(existingTemplates);
		Assert.assertEquals(1, existingTemplates.size());
		Assert.assertTrue(existingTemplates.contains(template));
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByEntity(String)
	 * @verifies return empty list if no matches
	 */
	@Test
	public void getHL7TemplatesByEntity_shouldReturnEmptyListIfNoMatches() throws Exception {
		//given
		HL7Template template = new HL7Template();
		template.setName("name");
		template.setTemplate("template");
		template.setHl7Entity("different entity");
		template = getService().saveHL7Template(template);
		
		//when
		List<HL7Template> existingTemplates = getService().getHL7TemplatesByEntity("entity");
		
		//then
		Assert.assertNotNull(existingTemplates);
		Assert.assertEquals(0, existingTemplates.size());
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByName(String)
	 * @verifies return matching templates
	 */
	@Test
	public void getHL7TemplatesByName_shouldReturnMatchingTemplates() throws Exception {
		//given
		HL7Template template1 = new HL7Template();
		template1.setName("name");
		template1.setTemplate("template");
		template1 = getService().saveHL7Template(template1);
		
		HL7Template template2 = new HL7Template();
		template2.setName("different name");
		template2.setTemplate("template");
		template2 = getService().saveHL7Template(template2);
		
		HL7Template template3 = new HL7Template();
		template3.setName("completely different");
		template3.setTemplate("template");
		template3 = getService().saveHL7Template(template3);
		
		//when
		List<HL7Template> existingTemplates = getService().getHL7TemplatesByName("name");
		
		//then
		Assert.assertNotNull(existingTemplates);
		Assert.assertEquals(2, existingTemplates.size());
		Assert.assertTrue("template1", existingTemplates.contains(template1));
		Assert.assertTrue("template2", existingTemplates.contains(template2));
	}
	
	/**
	 * @see HL7QueryService#getHL7TemplatesByName(String)
	 * @verifies return empty list if no matches
	 */
	@Test
	public void getHL7TemplatesByName_shouldReturnEmptyListIfNoMatches() throws Exception {
		//given
		HL7Template template1 = new HL7Template();
		template1.setName("name");
		template1.setTemplate("template");
		template1 = getService().saveHL7Template(template1);
		
		HL7Template template2 = new HL7Template();
		template2.setName("different name");
		template2.setTemplate("template");
		template2 = getService().saveHL7Template(template2);
		
		HL7Template template3 = new HL7Template();
		template3.setName("completely different");
		template3.setTemplate("template");
		template3 = getService().saveHL7Template(template3);
		
		//when
		List<HL7Template> existingTemplates = getService().getHL7TemplatesByName("nonexistingname");
		
		//then
		Assert.assertNotNull(existingTemplates);
		Assert.assertEquals(0, existingTemplates.size());
	}
	
	/**
	 * @see HL7QueryService#evaluateTemplate(HL7Template,Map)
	 * @verifies add the HL7TemplateFunctions class as func to bindings
	 */
	@Test
	public void evaluateTemplate_shouldAddTheHL7TemplateFunctionsClassAsFuncToBindings() throws Exception {
		
		TestUtil.printOutTableContents(getConnection(), "global_property");
		
		HL7Template t = new HL7Template();
		t.setLanguage(HL7QueryService.LANGUAGE_GROOVY);
		t.setTemplate("The value of locale.allowed.list is: ${ func.getGlobalProperty('locale.allowed.list') }");
		
		Map<String, Object> bindings = new HashMap<String, Object>();
		
		String evaluated = getService().evaluateTemplate(t, bindings);
		Assert.assertEquals("The value of locale.allowed.list is: en", evaluated);
	}
	
	/**
	* @see {@link HL7QueryService#renderPipeDelimitedHl7(String)}
	*/
    @Test
    @Verifies(value = "should return pipe delimited hl7 message", method = "renderPipeDelimitedHl7(String)")
    public void renderPipeDelimitedHl7_shouldReturnPipeDelimitedHl7Message() throws Exception {
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample-hl7.xml");
    	String xml = IOUtils.toString(inputStream);
    	String output = new HL7QueryServiceImpl().renderPipeDelimitedHl7(xml);
    	Assert.assertNotNull(output);
    }
}