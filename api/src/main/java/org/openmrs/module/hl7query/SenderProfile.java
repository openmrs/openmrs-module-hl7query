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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SenderProfile {

	public static Log log = LogFactory.getLog(SenderProfile.class);
	
	private long sender_profile_id;
	private String name;
	private boolean profile_status;
	private String action;
	private String url;
	private String message_format;
	
	public long getSender_profile_id() {
		return sender_profile_id;
	}
	
	public void setSenderProfileId(long sender_profile_id) {
		this.sender_profile_id = sender_profile_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isProfile_status() {
		return profile_status;
	}
	
	public void setProfile_status(boolean profile_status) {
		this.profile_status = profile_status;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMessage_format() {
		return message_format;
	}
	
	public void setMessage_format(String message_format) {
		this.message_format = message_format;
	}
}
