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
package org.openmrs.module.hl7query.api.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.hl7query.Template;
import org.openmrs.module.hl7query.api.db.HL7QueryDAO;

/**
 * It is a default implementation of {@link HL7QueryDAO}.
 */
public class HibernateHL7QueryDAO implements HL7QueryDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public Template getTemplate(Integer id) {
		return (Template) sessionFactory.getCurrentSession().get(Template.class, id);
	}
	
	@Override
	public Template getTemplateByUuid(String uuid) {
		return (Template) sessionFactory.getCurrentSession().createCriteria(Template.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	public Template getTemplateByName(String name) {
		return (Template) sessionFactory.getCurrentSession().createCriteria(Template.class)
		        .add(Restrictions.eq("name", name)).uniqueResult();
	}
	
	@Override
	public List<Template> getTemplatesByName(String name) {
		@SuppressWarnings("unchecked")
		List<Template> list = sessionFactory.getCurrentSession().createCriteria(Template.class)
		        .add(Restrictions.ilike("name", name, MatchMode.ANYWHERE)).addOrder(Order.asc("templateId")).list();
		return list;
	}
	
	@Override
	public List<Template> getTemplatesByEntity(String entity) {
		@SuppressWarnings("unchecked")
		List<Template> list = sessionFactory.getCurrentSession().createCriteria(Template.class)
		        .add(Restrictions.eq("hl7Entity", entity)).addOrder(Order.asc("templateId")).list();
		return list;
	}
	
	@Override
	public Template saveTemplate(Template template) {
		return (Template) sessionFactory.getCurrentSession().save(template);
	}
	
	@Override
	public Template retireTemplate(Template template, String reason) {
		return (Template) sessionFactory.getCurrentSession().save(template);
	}
	
	@Override
	public Template unretireTemplate(Template template) {
		return (Template) sessionFactory.getCurrentSession().save(template);
	}
	
	@Override
	public void purgeTemplate(Template template) {
		sessionFactory.getCurrentSession().delete(template);
	}
}
