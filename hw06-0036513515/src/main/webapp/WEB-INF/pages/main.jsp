<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
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
		.login {
			display: flex;
			justify-content: center;
			background-color: lightblue;
			border: 1px solid black;
			width: 40%;
			margin-left: 30%;
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
	 <div class="login">
		 <form method="POST" action="<c:url value="/servleti/main"/>">
			<label for="nickname">Nickname:</label><input type="text" name="nickname" value="${nickname}"><br/>
			<label for="password">Password:</label><input type="password" name="password"><br/>
			<label></label><button type="submit">Login</button> <br/>
			<label></label><a href="<c:url value="/servleti/register"/>">Register</a>
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
		 </form>
	</div>
	 
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

 <h2>Registered users:</h2>
  <ul>
	<c:forEach var = "user" items="${users}">
		<li><a href="<c:url value = "/servleti/author/${user.nickname}" />">${user.nickname}</a></li>
	</c:forEach>
 </ul>
 </body>
</html>