<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Global Properties" otherwise="/login.htm" redirect="/module/hl7query/hl7QueryAdmin.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>

<%@ include file="localHeader.jsp" %>

<openmrs:portlet url="globalProperties" parameters="title=hl7query|propertyPrefix=hl7query.|excludePrefix=hl7query.started|hidePrefix=false|readOnly=false" />

<%@ include file="/WEB-INF/template/footer.jsp" %>
