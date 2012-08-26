<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<openmrs:hasPrivilege privilege="Manage Global Properties">
	<li
		<c:if test='<%= request.getRequestURI().contains("/settings") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/hl7query/settings.form"><spring:message
				code="hl7query.manage" /></a>
	</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage HL7 Templates">
	<li
		<c:if test='<%= request.getRequestURI().contains("/manageTemplates") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/hl7query/manageTemplates.list"><spring:message
				code="hl7query.manageTemplates" /></a>
	</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege="Manage HL7 Templates">
	<li
		<c:if test='<%= request.getRequestURI().contains("/hl7Template") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/hl7query/hl7Template.form"><spring:message
				code="hl7query.manageTemplate" /></a>
	</li>
	</openmrs:hasPrivilege>
	
	<!-- Add further links here -->
</ul>

