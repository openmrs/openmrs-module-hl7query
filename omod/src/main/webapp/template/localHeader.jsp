<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/manage.form") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/hl7query/manage.form"><spring:message
				code="hl7query.manage" /></a>
	</li>
	
	<li
		<c:if test='<%= request.getRequestURI().contains("/manageTemplates") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/hl7query/manageTemplates.list"><spring:message
				code="hl7query.manageTemplates" /></a>
	</li>
	
	<!-- Add further links here -->
</ul>
<h2>
	<spring:message code="hl7query.title" />
</h2>
