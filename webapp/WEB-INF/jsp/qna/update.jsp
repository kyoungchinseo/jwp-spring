<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<!DOCTYPE html>
<html lang="kr">
<head>
	<%@ include file="/include/header.jspf" %>
</head>
<body>
<%@ include file="/include/navigation.jspf" %>

<div class="container" id="main">
	<div class="col-md-12 col-sm-12 col-lg-10 col-lg-offset-1">
		<div class="panel panel-default content-main">
			<form:form name="question" method="post" action="/qna/update"
				modelAttribute="question">
				<form:hidden path="questionId" value="${question.questionId }" />
				<div class="form-group">
					<label for="title">제목</label>
					<form:input path="title" cssClass="form-control" placeholder="제목"
						value="${question.title }" />
				</div>
				<div class="form-group">
					<label for="contents">내용</label>
					<form:textarea path="contents" rows="5" cssClass="form-control"
						value="${question.contents }" />
				</div>
				<button type="submit" class="btn btn-success clearfix pull-right">수정하기</button>
				<div class="clearfix" />
			</form:form>
		</div>
	</div>
</div>

	<%@ include file="/include/footer.jspf" %>
</body>
</html>