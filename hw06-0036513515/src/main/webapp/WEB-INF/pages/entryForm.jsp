<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>
  <c:choose>
	  <c:when test="${empty form}">New entry</c:when>
	  <c:otherwise>Modify entry</c:otherwise>
  </c:choose>
  </title>
  <style>
  	body {
			background-color: darkseagreen;
	}
	label {
		display: inline-block;
		width:100px;
		text-align: left;
	}
	.error {
		color: #FF0000;
		padding-left: 110px;
	}
	header {
			text-align: center;
			border: 1px solid black;
			width: 20%;
			margin-left: 80%;
			background-color: lightblue;
	}
	a {
		font-size: 1.2em;
	}
	.center {
		display:flex;
		justify-content: center;
	}
	.lightblue {
		background-color: lightblue;
		width:40%;
		margin-left: 30%;
	}
	.error {
		color: #FF0000;
		text-align: center;
	}
  </style>
</head>
 <body>
	<c:choose>
	<c:when test="${sessionScope['current.user.id']==null}">
		<header>
			Not logged in. <br/>
			<a href="<c:url value="/servleti/main" />"> Home </a>
		</header>
	</c:when>
	<c:otherwise>
		<header>
			${sessionScope['current.user.fn']} ${sessionScope['current.user.ln']} <br/>
			<a href="<c:url value="/servleti/author/${sessionScope['current.user.nick']}" />"> Profile </a> <br/>
			<a href="<c:url value="/servleti/main" />"> Home </a>
			<form method="POST" action="<c:url value="/servleti/logout" />">
				<button type="submit">Logout</button>
			</form>
		</header>
	</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${flag.equals('new')}">
		<h1 class="center">New entry</h1>
		<div class="center lightblue">
			<form method="POST" action="<c:url value="/servleti/author/${author.nickname}/new"/>">
				<div>
					<label for="title">Title:</label><input type="text" name="title" value="${form.title}">
				</div>
				<c:if test="${form.hasError('title')}">
					<div class="error">${form.getError('title')}</div>
				</c:if>
				<div>
					<label for="text">Text:</label><textarea name="text" rows="5" cols="50">${form.text}</textarea>
				</div>
				<c:if test="${form.hasError('text')}">
					<div class="error">${form.getError('text')}</div>
				</c:if>
				<button type="submit">Save</button>
			</form>
		</div>
		</c:when>
		<c:otherwise>
			<h1 class="center">Modify entry</h1>
			<div class="center lightblue">
			<form method="POST" action="<c:url value="/servleti/author/${author.nickname}/edit"/>">
				<div>
					<label for="id">ID:</label><input type="text" name="id" value="${form.id}" readonly>
				</div>
				<div>
					<label for="title">Title:</label><input type="text" name="title" value="${form.title}">
				</div>
				<c:if test="${form.hasError('title')}">
					<div class="error">${form.getError('title')}</div>
				</c:if>
				<div>
					<label for="text">Text:</label><textarea name="text" rows="5" cols="50">${form.text}</textarea>
				</div>
				<c:if test="${form.hasError('text')}">
					<div class="error">${form.getError('text')}</div>
				</c:if>
				<div>
					<label></label><button type="submit">Save</button>
				</div>
			</form>
		</div>
		</c:otherwise>
	</c:choose>

 </body>