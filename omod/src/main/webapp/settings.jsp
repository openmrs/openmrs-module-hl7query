<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="View HL7 Templates" otherwise="/login.htm" 
                 redirect="/module/hl7query/settings.form"/>

<h2><spring:message code="GlobalProperty.manage"/></h2>

<openmrs:portlet url="globalProperties" parameters="title=${title}|propertyPrefix=hl7query.|hidePrefix=true|readOnly=false" />

<%@ include file="/WEB-INF/template/footer.jsp"%>