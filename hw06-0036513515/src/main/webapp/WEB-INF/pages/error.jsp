<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <title>ERROR</title>
  <style>
		body {
			background-color: darkseagreen;
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
			display: flex;
			justify-content: center;
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
 <h1 class="center">${error}</h1>
 </body>
</html>