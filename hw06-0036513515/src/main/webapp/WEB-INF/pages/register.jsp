<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
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
		text-align: center;
	}
	.login {
		display: flex;
		justify-content: center;
		background-color: lightblue;
		border: 1px solid black;
		width: 40%;
		margin-left: 30%;
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
		text-align: center;
	}
  </style>
</head>
 <body>
 <header>
	<a href="<c:url value="/servleti/main" />"> Home </a>
 </header>
 <h1 class="center">Registration</h1>
 <div class="login">
 <form method="POST" action="<c:url value="/servleti/register"/>">
 	<div>
		<div>
			<label for="firstName">First name:</label><input type="text" name="firstName" value=${form.firstName}>
		</div>
		<c:if test="${form.hasError('firstName')}">
			<div class="error"><c:out value="${form.getError('firstName')}"/></div>
		</c:if>
	</div>
	<div>
		<div>
			<label for="lastName">Last name:</label><input type="text" name="lastName" value=${form.lastName}>
		</div>
		<c:if test="${form.hasError('lastName')}">
			<div class="error"><c:out value="${form.getError('lastName')}"/></div>
		</c:if>
	</div>
	<div>
		<div>
			<label for="email">E-mail:</label><input type="text" name="email" value=${form.email}>
		</div>
		<c:if test="${form.hasError('email')}">
			<div class="error"><c:out value="${form.getError('email')}"/></div>
		</c:if>
	</div>
	<div>
		<div>
			<label for="nickname">Nickname:</label><input type="text" name="nickname" value=${form.nickname}>
		</div>
		<c:if test="${form.hasError('nickname')}">
			<div class="error"><c:out value="${form.getError('nickname')}"/></div>
		</c:if>
	</div>
	<div>
		<div>
			<label for="password">Password:</label><input type="password" name="password" value=${form.password}>
		</div>
		<c:if test="${form.hasError('password')}">
			<div class="error"><c:out value="${form.getError('password')}"/></div>
		</c:if>
	</div>
	<label></label><button type="submit">Register</button>
 </form>
 </div>
 </body>