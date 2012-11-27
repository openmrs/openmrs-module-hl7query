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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.module.hl7query.AuthenticationErrorObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * The ExceptionUtil class is responsible for creating json / xml formatted based on the parameters received from the controller class
 *
 */

public class ExceptionUtil {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * 
	 * @param request 
	 * 		The http request object made by the user
	 * @param response
	 * 		The http response object returned to the user
	 * @param error
	 * 		The error message object which contains the appopriate error message description and the error code
	 * @param segment
	 * 		An additional (optional) string used to contain additional details (etc. which parameter is causing the error)
	 * @return
	 * 		An appopriately formed error message object containing details of  the error
	 * 
	 * Based on the users request, the generateMessage() method will call either of two methods - writeJsonMessage() or writeXmlMessage().
	 * These methods are responsible for creating a well formed error message 
	 */
	public static Object generateMessage(HttpServletRequest request, HttpServletResponse response, ErrorDetailsEnum error, Object segment){
		boolean isPipeDelimited = false;
		
		//check the users request header, and decide which method to call
		String acceptHeader = request.getHeader("Accept");
		if (acceptHeader == null || !acceptHeader.contains("text/xml"))
				isPipeDelimited = true;
		
		if(isPipeDelimited){
			//If pipe delimited, create message in json format
			response.setContentType("application/json");
			return writeJsonMessage(error, segment);		
			}else{
			//if not, create message in xml format
				response.setContentType("text/xml");
			return writeXmlMessage(error, segment);	
			}
	}

	/**
	 * 
	 * @param error
	 * 		The error message object which contains the appopriate error message description and the error code
	 * @param segment
	 * 		An additional (optional) string used to contain additional details (etc. which parameter is causing the error)
	 * @return
	 * 		An appopriately formed error message object containing details of  the error
	 * 
	 * This message is triggered is the user had wanted the response hl7 message to be in pipe delimited format
	 */
	private static Object writeJsonMessage(ErrorDetailsEnum error, Object segment) {
		String errorMsg = null; 
		ObjectMapper mapper = new ObjectMapper();
		ErrorMessageTemplate errorMessageTemplate = new ErrorMessageTemplate();
		errorMessageTemplate.setError(error);
		
		//The segment is optional, it contains additional error message details	
		if(segment != null) {
			if(segment instanceof String){
			errorMsg = error.getMessage().toString() + " " + segment;
			}else if(segment instanceof Exception){
			errorMsg = error.getMessage().toString() + " " + (ExceptionUtils.getRootCauseMessage((Exception) segment)).toString();
			errorMessageTemplate.setErrorTrace(ExceptionUtils.getFullStackTrace((Exception) segment));
			}
		}else{
			errorMsg = error.getMessage().toString();
		}
			
		errorMessageTemplate.setErrorMessage(errorMsg);
		errorMessageTemplate.setErrorCode(Integer.toString(error.getCode()));

		Writer strWriter = new StringWriter();
		try {
			mapper.writeValue(strWriter, errorMessageTemplate);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String userDataJSON = strWriter.toString();

		return userDataJSON;
	}
	
	/**
	 * 
	 * @param error
	 * 		The error message object which contains the appopriate error message description and the error code
	 * @param segment
	 * 		An additional (optional) string used to contain additional details (etc. which parameter is causing the error)
	 * @return
	 * 		An appopriately formed error message object containing details of  the error
	 * This message is triggered is the user had wanted the response hl7 message to be in pipe delimited format
	 */
	private static Object writeXmlMessage(ErrorDetailsEnum error, Object segment) {
		String xmlString = null;
		String errorDescription = null;
		String stackTrace = null;
		 try {
	            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	            Document doc = docBuilder.newDocument();
	            
	            //create the root element and add it to the document
	            Element root = doc.createElement("error");
	            doc.appendChild(root);

	            //create child element, add an attribute, and add to root
	            Element errorMsg = doc.createElement("errorMessage");
	            root.appendChild(errorMsg);

	            if(segment != null){
	            	if(segment instanceof String){
	            		errorDescription = error.getMessage().toString() + " " + segment;
	            	}else if (segment instanceof Exception){
	            		errorDescription = error.getMessage().toString() + " " + (ExceptionUtils.getRootCauseMessage((Exception) segment)).toString();
	        			stackTrace = ExceptionUtils.getFullStackTrace((Exception) segment);
	            	}
	            }  else{
	            	errorDescription = error.getMessage().toString();
	            }
	            
	            //add a text element to the child	            
	            Text errorText = doc.createTextNode(errorDescription);
	            errorMsg.appendChild(errorText);
	            
	            if(stackTrace != null){
	            	Element errorTrace = doc.createElement("staceTrace");
	 	            root.appendChild(errorTrace);
	 	            
	 	          Text stackTraceText = doc.createTextNode(stackTrace);
	 	          errorTrace.appendChild(stackTraceText);
	            }
	            
	            //create child element, add an attribute, and add to root
	            Element errorCode = doc.createElement("errorCode");
	            root.appendChild(errorCode);
	            
	            //add a text element to the child
	            Text errorCodeText = doc.createTextNode(Integer.toString(error.getCode()));
	            errorCode.appendChild(errorCodeText);

	            //set up a transformer
	            TransformerFactory transfac = TransformerFactory.newInstance();
	            Transformer trans = transfac.newTransformer();
	            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            trans.setOutputProperty(OutputKeys.INDENT, "yes");

	            //create string from xml tree
	            StringWriter sw = new StringWriter();
	            StreamResult result = new StreamResult(sw);
	            DOMSource source = new DOMSource(doc);
	            trans.transform(source, result);
	            xmlString = sw.toString();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return xmlString;
	}

	/**
	 * Wraps the exception message as a SimpleObject to be sent to client
	 * 
	 * @param ex
	 * @param reason
	 * @return
	 */
	public static AuthenticationErrorObject wrapErrorResponse(Exception ex, String reason) {
		LinkedHashMap map = new LinkedHashMap();
		if (reason != null && !reason.isEmpty()) {
			map.put("message", reason);
		} else
			map.put("message", ex.getMessage());
		StackTraceElement ste = ex.getStackTrace()[0];
		map.put("code", ste.getClassName() + ":" + ste.getLineNumber());
		map.put("detail", ExceptionUtils.getStackTrace(ex));
		return new AuthenticationErrorObject().add("error", map);
	}

	/**
	 * Inspects the cause chain for the given throwable, looking for an exception of the given class
	 * (e.g. to find an APIAuthenticationException wrapped in an InvocationTargetException)
	 * 
	 * @param throwable
	 * @param causeClassToLookFor
	 * @return whether any exception in the cause chain of throwable is an instance of
	 *         causeClassToLookFor
	 */
	public static boolean hasCause(Throwable throwable, Class<? extends Throwable> causeClassToLookFor) {
		return ExceptionUtils.indexOfType(throwable, causeClassToLookFor) >= 0;
	}

}
