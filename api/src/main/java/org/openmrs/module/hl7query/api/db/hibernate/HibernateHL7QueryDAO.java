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
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.hl7query.HL7Template;
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
	public HL7Template getHL7Template(Integer id) {
		return (HL7Template) sessionFactory.getCurrentSession().get(HL7Template.class, id);
	}
	
	@Override
	public HL7Template getHL7TemplateByUuid(String uuid) {
		return (HL7Template) sessionFactory.getCurrentSession().createCriteria(HL7Template.class)
		        .add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}
	
	@Override
	public HL7Template getHL7TemplateByName(String name) {
		return (HL7Template) sessionFactory.getCurrentSession().createCriteria(HL7Template.class)
		        .add(Restrictions.eq("name", name)).uniqueResult();
	}
	
	@Override
	public List<HL7Template> getHL7TemplatesByName(String name) {
		@SuppressWarnings("unchecked")
		List<HL7Template> list = sessionFactory.getCurrentSession().createCriteria(HL7Template.class)
		        .add(Restrictions.ilike("name", name, MatchMode.ANYWHERE)).addOrder(Order.asc("templateId")).list();
		return list;
	}
	
	@Override
	public List<HL7Template> getHL7TemplatesByEntity(String entity) {
		@SuppressWarnings("unchecked")
		List<HL7Template> list = sessionFactory.getCurrentSession().createCriteria(HL7Template.class)
		        .add(Restrictions.eq("hl7Entity", entity)).addOrder(Order.asc("templateId")).list();
		return list;
	}
	
	@Override
	public HL7Template saveHL7Template(HL7Template template) {
		sessionFactory.getCurrentSession().saveOrUpdate(template);
		return template;
	}
	
	@Override
	public HL7Template retireHL7Template(HL7Template template, String reason) {
		sessionFactory.getCurrentSession().saveOrUpdate(template);
		return template;
	}
	
	@Override
	public HL7Template unretireHL7Template(HL7Template template) {
		sessionFactory.getCurrentSession().saveOrUpdate(template);
		return template;
	}
	
	@Override
	public void purgeHL7Template(HL7Template template) {
		sessionFactory.getCurrentSession().delete(template);
	}

	@SuppressWarnings("unchecked")
    @Override
    public List<HL7Template> getHL7Templates(boolean includeRetired) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(HL7Template.class);
		if(!includeRetired)
			c.add(Restrictions.eq("retired", includeRetired));
		return c.list();
    }
}
