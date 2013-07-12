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

import java.util.List;

public class HL7QueryEvent {
	private String event_id;
	private String event_name;
	private String event_firing_class;
	private boolean tracked;
	private List<SenderProfile> senderProfiles;
	
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getEvent_firing_class() {
		return event_firing_class;
	}
	public void setEvent_firing_class(String event_firing_class) {
		this.event_firing_class = event_firing_class;
	}
	public boolean isTracked() {
		return tracked;
	}
	public void setTracked(boolean tracked) {
		this.tracked = tracked;
	}
	public List<SenderProfile> getSenderProfiles() {
		return senderProfiles;
	}
	public void setSenderProfiles(List<SenderProfile> senderProfiles) {
		this.senderProfiles = senderProfiles;
	}
	
	
}
