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
	
	private long senderProfileId;
	private String name;
	private boolean profileStatus;
	private String action;
	private String url;
	private String messageFormat;
	
	public long getSenderProfileId() {
		return senderProfileId;
	}
	
	public void setSenderProfileId(long senderProfileId) {
		this.senderProfileId = senderProfileId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean getProfileStatus() {
		return profileStatus;
	}
	
	public void setProfileStatus(boolean profileStatus) {
		this.profileStatus = profileStatus;
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
	
	public String getMessageFormat() {
		return messageFormat;
	}
	
	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}
}
