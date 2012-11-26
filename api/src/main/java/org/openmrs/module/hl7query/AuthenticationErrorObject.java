package org.openmrs.module.hl7query;

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

import java.io.IOException;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This is the Map returned for all objects. The properties are just key/value pairs. If an object
 * has subobjects those are just lists of SimpleObjects
 */
public class AuthenticationErrorObject extends LinkedHashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	
	public AuthenticationErrorObject() {
		super();
	}
	
	/**
	 * Puts a property in this map, and returns the map itself (for chained method calls)
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public AuthenticationErrorObject add(String key, Object value) {
		put(key, value);
		return this;
	}
	
	/**
	 * Removes a property from the map, and returns the map itself (for chained method calls)
	 * 
	 * @param key
	 * @return
	 */
	public AuthenticationErrorObject removeProperty(String key) {
		remove(key);
		return this;
	}
	
	/**
	 * Creates an instance from the given json string.
	 * 
	 * @param json
	 * @return the simpleObject
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static AuthenticationErrorObject parseJson(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		AuthenticationErrorObject object = objectMapper.readValue(json, AuthenticationErrorObject.class);
		return object;
	}
}
