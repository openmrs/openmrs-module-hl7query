<%@ include file="/WEB-INF/template/include.jsp"%>
 
<openmrs:require privilege="Manage HL7 Templates" otherwise="/login.htm" 
                 redirect="/module/hl7query/manageTemplates.list"/>

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

<b class="boxHeader"><spring:message code="hl7query.templates"/></b>
<div class="box">
<a href="<openmrs:contextPath />/module/hl7query/hl7Template.form"><spring:message code="hl7query.addTemplate"/></a>
<br />
<br />
<table cellpadding="4" cellspacing="0" style="border: 1px solid #918E90">
	<c:if test="${fn:length(hL7Templates) > 0}">
	<tr style="background-color: #CCCCCC">
		<th valign="top"><spring:message code="hl7query.hl7Entity"/></th>
		<th valign="top"><spring:message code="general.name"/></th>
		<th valign="top"><spring:message code="general.description"/></th>
		<th valign="top"><spring:message code="hl7query.isParent"/></th>
	</tr>
	</c:if>
	<c:forEach var="hl7Template" items="${hl7Templates}" varStatus="varStatus">
	<tr <c:if test="${varStatus.index % 2 == 0}">class='evenRow'</c:if>>
		<td valign="top">${hl7Template.hl7Entity}</td>
		<td valign="top"><a href="<openmrs:contextPath />/module/hl7query/hl7Template.form?hl7TemplateId=${hl7Template.hl7TemplateId}">
		<c:choose>
			<c:when test="${hl7Template.retired == true}">
				<del>${hl7Template.name}</del>
			</c:when>
			<c:otherwise>
				${hl7Template.name}
			</c:otherwise>
		</c:choose></a></td>
		<td valign="top">${hl7Template.description}</td>
		<td valign="top">${hl7Template.isParent}</td>
		
	</tr>
	</c:forEach>
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>