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
package org.openmrs.module.hl7query.api.db;

import java.util.List;

import org.openmrs.module.hl7query.SenderProfile;
import org.openmrs.module.hl7query.api.SenderProfileService;

public interface SenderProfileDAO {
	/**
	 * @see SenderProfileService#getSenderProfile(Integer)
	 */
	SenderProfile getSenderProfile(Integer id);
	
	/**
	 * @see SenderProfileService#getSenderProfileByUuid(String)
	 */
	SenderProfile getSenderProfileByUuid(String uuid);
	
	/**
	 * @see SenderProfileService#getSenderProfileByName(String)
	 */
	SenderProfile getSenderProfileByName(String name);
	
	/**
	 * @see SenderProfileService#getSenderProfilesByName(String)
	 */
	List<SenderProfile> getSenderProfilesByName(String name);
	
	/**
	 * @see SenderProfileService#saveSenderProfile(SenderProfile)
	 */
	SenderProfile saveSenderProfile(SenderProfile SenderProfile);
	
	/**
	 * @see SenderProfileService#retireSenderProfile(SenderProfile, String)
	 */
	SenderProfile retireSenderProfile(SenderProfile SenderProfile, String reason);
	
	/**
	 * @see SenderProfileService#unretireSenderProfile(SenderProfile)
	 */
	SenderProfile unretireSenderProfile(SenderProfile SenderProfile);
	
	/**
	 * @see SenderProfileService#purgeSenderProfile(SenderProfile)
	 */
	void purgeSenderProfile(SenderProfile SenderProfile);
	
	/**
	 * @see SenderProfileService#getSenderProfiles(boolean)
	 */
	List<SenderProfile> getSenderProfiles(boolean includeRetired);

}
