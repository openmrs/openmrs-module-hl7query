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
package org.openmrs.module.hl7query.util;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;

public class HL7QueryConstants {
	private static Log log = LogFactory.getLog(HL7QueryConstants.class);
	
	/*
	 * The Global Properties for MSH   
	 */
	
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_FIELD_SEPARATOR ="hl7query.msh.field.separator";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_ENCODING_CHARACTERS="hl7query.msh.encoding.characters"; 
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_SENDING_APPLICATION ="hl7query.msh.sending.application";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_SENDING_FACILITY="hl7query.msh.sending.facility"; 
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_RECEIVING_APPLICATION ="hl7query.msh.receiving.application"; 
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_RECEIVING_FACILITY ="hl7query.msh.receiving.facility";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_DATE_TIME_OF_MESSAGE ="hl7query.date.time.of.message";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_SECURITY ="hl7query.msh.security";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_MESSAGE_TYPE ="hl7query.msh.message.type";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_MESSAGE_CONTROL_ID ="hl7query.msh.message.control.id";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_PROCESSING_ID="hl7query.msh.processing.id"; 
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_VERSION_ID ="hl7query.msh.version.id";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_SEQUENCE_NUMBER ="hl7query.msh.sequence.number";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_CONTINUATION_POINTER="hl7query.msh.continuation.pointer"; 
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_ACCEPT_ACKNOWLEDGEMENT_TYPE ="hl7query.msh.accept.acknowledgement.type";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_APPLICATION_ACKNOWLEDGEMENT_TYPE ="hl7query.msh.application.acknowledgement.type";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_COUNTRY_CODE ="hl7query.msh.country.code";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_CHARACTER_SET ="hl7query.msh.character.set";
	public static final String HL7QUERY_GLOBAL_PROPERTY_MSH_PRINCIPAL_LANGUAGE_OF_MESSAGE="hl7query.msh.principal.language.of.message";
	
	public static final List<GlobalProperty> CORE_GLOBAL_PROPERTIES() {
		Date dateAndTimeOfMessage=new Date();
		String strDateAndTimeOfMessage=dateAndTimeOfMessage.toString();
		List<GlobalProperty> props = new Vector<GlobalProperty>();
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_FIELD_SEPARATOR, " ","This is the field seperator. It is a constant. The user should NOT be allowed to change it"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_ENCODING_CHARACTERS, " ","Thease are the encoding characters. The user should NOT be allowed to change it"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_SENDING_APPLICATION, " ","Sending Application"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_SENDING_FACILITY, "OpenMRS","Sending Facility "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_RECEIVING_APPLICATION, " ","Receiving Application "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_RECEIVING_FACILITY, " ","Receiving Facility "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_DATE_TIME_OF_MESSAGE,strDateAndTimeOfMessage,"Date/Time of Message"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_SECURITY, " ","Security "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_MESSAGE_TYPE, " ","Message Type "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_MESSAGE_CONTROL_ID, " ","Message Control Id "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_PROCESSING_ID, " ","Processing Id "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_VERSION_ID, " ","Version Id "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_SEQUENCE_NUMBER, " ","Sequence Number "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_CONTINUATION_POINTER, " ","Continuation Pointer "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_ACCEPT_ACKNOWLEDGEMENT_TYPE, " ","Accept Acknowledgement Type"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_APPLICATION_ACKNOWLEDGEMENT_TYPE, " ","Application Acknowledgement Type "));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_COUNTRY_CODE, " ","Country Code"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_CHARACTER_SET, " ","Character Set"));
		props.add(new GlobalProperty(HL7QUERY_GLOBAL_PROPERTY_MSH_PRINCIPAL_LANGUAGE_OF_MESSAGE, " ","Principal Language of Message"));
		return props;
	}
}
