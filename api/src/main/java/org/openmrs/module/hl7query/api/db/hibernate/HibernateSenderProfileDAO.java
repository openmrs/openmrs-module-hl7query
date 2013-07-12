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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.hl7query.SenderProfile;
import org.openmrs.module.hl7query.api.db.SenderProfileDAO;

public class HibernateSenderProfileDAO implements SenderProfileDAO {
	protected final Log log = LogFactory.getLog(this.getClass());

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
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
	public SenderProfile getSenderProfile(Integer id) {
		return (SenderProfile) sessionFactory.getCurrentSession().get(
				SenderProfile.class, id);
	}

	@Override
	public SenderProfile getSenderProfileByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SenderProfile getSenderProfileByName(String name) {
		return (SenderProfile) sessionFactory.getCurrentSession()
				.createCriteria(SenderProfile.class)
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public List<SenderProfile> getSenderProfilesByName(String name) {
		@SuppressWarnings("unchecked")
		List<SenderProfile> list = sessionFactory.getCurrentSession()
				.createCriteria(SenderProfile.class)
				.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE))
				.addOrder(Order.asc("sender_profile_id")).list();
		return list;
	}

	@Override
	public SenderProfile saveSenderProfile(SenderProfile senderProfile) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		tx = session.beginTransaction();
		session.save(senderProfile);
		session.flush();
		tx.commit();

		/*
		 * try{ System.out.println(senderProfile.getUrl());
		 * System.out.println(sessionFactory);
		 * System.out.println(sessionFactory.getCurrentSession());
		 * sessionFactory.getCurrentSession().saveOrUpdate(senderProfile);
		 * //sessionFactory.getCurrentSession().flush(); }catch(Exception e){
		 * e.printStackTrace(); }
		 * System.out.println("ddddddddddddddddddddddddddddd");
		 */
		return null;

	}

	@Override
	public SenderProfile retireSenderProfile(SenderProfile senderProfile,
			String reason) {

		return senderProfile;
	}

	@Override
	public SenderProfile unretireSenderProfile(SenderProfile senderProfile) {
		// sessionFactory.getCurrentSession().saveOrUpdate(senderProfile);
		return senderProfile;
	}

	@Override
	public void purgeSenderProfile(SenderProfile SenderProfile) {
	}

	@Override
	public List<SenderProfile> getSenderProfiles(boolean includeRetired) {
		// TODO Auto-generated method stub
		return null;
	}

}
