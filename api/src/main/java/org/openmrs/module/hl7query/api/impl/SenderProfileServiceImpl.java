package org.openmrs.module.hl7query.api.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.hl7query.SenderProfile;
import org.openmrs.module.hl7query.api.SenderProfileService;
import org.openmrs.module.hl7query.api.db.SenderProfileDAO;

public class SenderProfileServiceImpl implements SenderProfileService {
	
	protected final Log log = LogFactory.getLog(this.getClass());

	private SenderProfileDAO dao;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(SenderProfileDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @return the dao
	 */
	public SenderProfileDAO getDao() {
		return dao;
	}
	
	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SenderProfile getSenderProfile(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SenderProfile getSenderProfileByUuid(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SenderProfile getSenderProfileByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SenderProfile> getSenderProfilesByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SenderProfile> getSenderProfilesByEntity(String entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SenderProfile saveSenderProfile(SenderProfile senderProfile) {
		return dao.saveSenderProfile(senderProfile);
	}
	
	@Override
	public SenderProfile retireSenderProfile(SenderProfile senderProfile,
			String reason) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SenderProfile unretireSenderProfile(SenderProfile senderProfile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void purgeSenderProfile(SenderProfile senderProfile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SenderProfile> getSenderProfile(boolean includeRetired) {
		// TODO Auto-generated method stub
		return null;
	}

}