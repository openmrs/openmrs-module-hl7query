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

public class ErrorMessageTemplate {

	private ErrorDetailsEnum error = null;
    private String errorCode = null;
    private String errorMessage = null;;
    private String errorDetails = null;
    private String errorTrace = null;

	public ErrorDetailsEnum getError() {
		return error;
	}

	public void setError(ErrorDetailsEnum error) {
		this.error = error;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
	public String getErrorTrace() {
		return errorTrace;
	}

	public void setErrorTrace(String errorTrace) {
		this.errorTrace = errorTrace;
	}
    
    
    @Override
    public String toString()
    {
    	if(errorTrace != null){
    		return "ErrorMessageTemplate [errorMessage=" + error.toString() + ", errorCode=" + errorCode + ", errorDetails=" + errorDetails + ", errorTrace=" + errorTrace + "]";
    	}else{
    		return "ErrorMessageTemplate [errorMessage=" + error.toString() + ", errorCode=" + errorCode + ", errorDetails=" + errorDetails + "]";
    	}
	
    }
    
}
