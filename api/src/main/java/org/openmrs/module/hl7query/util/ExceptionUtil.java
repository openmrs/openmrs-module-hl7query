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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * The ExceptionUtil class is responsible for creating json / xml formatted based on the parameters received from the controller class
 *
 */

public class ExceptionUtil {
	
	public static Object generateMessage(HttpServletRequest request, HttpServletResponse response, ErrorDetailsEnum error, String segment){
		boolean isPipeDelimited = false;
		
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

	private static Object writeJsonMessage(ErrorDetailsEnum error, String segment) {
		String errorMsg = null; 
		ObjectMapper mapper = new ObjectMapper();
		ErrorMessageTemplate errorMessageTemplate = new ErrorMessageTemplate();
		errorMessageTemplate.setError(error);
		
		if(segment != null)
			errorMsg = error.getMessage().toString() + " " + segment;
		else
			errorMsg = error.getMessage().toString();
			
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
		System.out.println(userDataJSON);

		return userDataJSON;
	}
	
	private static Object writeXmlMessage(ErrorDetailsEnum error, String segment) {
		String xmlString = null;
		String errorDescription = null;
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

	            if(segment != null)
	            	errorDescription = error.getMessage().toString() + " " + segment;
	            else
	            	errorDescription = error.getMessage().toString();
	            
	            //add a text element to the child
	            Text errorText = doc.createTextNode(errorDescription);
	            errorMsg.appendChild(errorText);
	            
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

}
