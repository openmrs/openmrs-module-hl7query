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

import java.util.List;
import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.hl7query.SenderProfile;
import org.springframework.transaction.annotation.Transactional;

public interface SenderProfileService extends OpenmrsService {

	/**
	 * Gets SenderProfile by ID.
	 * 
	 * @param id
	 * @return SenderProfile or null
	 * @should return profile if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	SenderProfile getSenderProfile(Integer id);

	/**
	 * Gets SenderProfile by UUID.
	 * 
	 * @param uuid
	 * @return SenderProfile or null
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	SenderProfile getSenderProfileByUuid(String uuid);

	/**
	 * Gets SenderProfile by unique name.
	 * 
	 * @param name
	 * @return SenderProfile or null
	 * @should return template if exists
	 * @should return null if does not exist
	 */
	@Transactional(readOnly = true)
	SenderProfile getSenderProfileByName(String name);

	/**
	 * Gets SenderProfiles by partial name.
	 * 
	 * @param name
	 * @return SenderProfiles or empty list
	 * @should return matching profiles
	 * @should return empty list if no matches
	 */
	@Transactional(readOnly = true)
	List<SenderProfile> getSenderProfilesByName(String name);

	/**
	 * Gets SenderProfiles by entity.
	 * 
	 * @param entity
	 * @return SenderProfiles or empty list
	 * @should return matching profiles
	 * @should return empty list if no matches
	 */
	@Transactional(readOnly = true)
	List<SenderProfile> getSenderProfilesByEntity(String entity);

	/**
	 * Saves or updates the template.
	 * 
	 * @param senderProfile
	 * @return SenderProfile
	 * @should save template
	 */
	SenderProfile saveSenderProfile(SenderProfile senderProfile);

	/**
	 * Retires the template.
	 * 
	 * @param senderProfile
	 * @param reason
	 * @return SenderProfile
	 * @should retire template
	 */
	SenderProfile retireSenderProfile(SenderProfile senderProfile, String reason);

	/**
	 * Unretires the template.
	 * 
	 * @param senderProfile
	 * @return SenderProfile
	 * @should unretire template
	 */
	SenderProfile unretireSenderProfile(SenderProfile senderProfile);

	/**
	 * Purges the template.
	 * 
	 * @param senderProfile
	 * @should purge template
	 */
	void purgeSenderProfile(SenderProfile senderProfile);

	/**
	 * Gets all sender profiles. TODO add/require view privileges and update
	 * manageTemplates.jsp to require them
	 * 
	 * @param includeRetired
	 *            specified whether retired profiles should be included or not
	 * @return a list of {@link SenderProfile}s
	 * @should get all profiles
	 * @should exclude retired profiles if include retired is set to false
	 */
	@Transactional(readOnly = true)
	List<SenderProfile> getSenderProfile(boolean includeRetired);
}
