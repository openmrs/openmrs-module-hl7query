<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
 
<openmrs:require privilege="View HL7 Templates" otherwise="/login.htm" 
                 redirect="/module/hl7query/hl7Template.form"/>

<%@ include file="template/localHeader.jsp"%>

<form method="post">
<h2><spring:message code="hl7query.manageTemplates" /></h2>
<fieldset>
<table>
	<tr>
		<td><spring:message code="hl7query.name"/></td>
		<td>
			<spring:bind path="hl7Template.name">
				<input type="text" name="name" value="${status.value}" size="25" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="hl7query.entity"/></td>
		<td>
			<spring:bind path="hl7Template.hl7Entity">
				<input type="text" name="hl7Entity" value="${status.value}" size="25" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td><spring:message code="hl7query.language"/></td>
		<td>
			<spring:bind path="hl7Template.language">
				<input type="text" name="language" value="${status.value}" size="25" />
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="hl7query.template"/></td>
		<td valign="top">
			<spring:bind path="hl7Template.template">
				<textarea name="template" rows="12" cols="70">${status.value}</textarea>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<c:if test="${!(hl7Template.creator == null)}">
		<tr>
			<td><spring:message code="general.createdBy" /></td>
			<td>
				${hl7Template.creator.personName} -
				<openmrs:formatDate date="${hl7Template.dateCreated}" type="long" />
			</td>
		</tr>
	</c:if>
</table>
<br />
<input type="submit" value="<spring:message code="hl7query.saveTemplate"/>" name="save" />
</fieldset>
</form>

<br/>
<c:if test="${not hl7Template.retired && not empty hl7Template.hl7TemplateId}">
	<form method="post">
		<fieldset>
			<h4><spring:message code="hl7query.retire"/></h4>
			
			<b><spring:message code="general.reason"/></b>
			<input type="text" value="" size="40" name="retireReason" />
			<spring:hasBindErrors name="hl7Template">
				<c:forEach items="${errors.allErrors}" var="error">
					<c:if test="${error.code == 'retireReason'}"><span class="error"><spring:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span></c:if>
				</c:forEach>
			</spring:hasBindErrors>
			<br/>
			<input type="submit" value='<spring:message code="hl7query.retire"/>' name="retire"/>
		</fieldset>
	</form>
</c:if>

<br/>
<c:if test="${hl7Template.retired && not empty hl7Template.hl7TemplateId}">
	<form method="post">
		<fieldset>
			<h4><spring:message code="hl7query.restore"/></h4>
			<input type="submit" value='<spring:message code="hl7query.restore"/>' name="restore"/>
		</fieldset>
	</form>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>