<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <title>Blog entries</title>
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
		ul {
			text-align: left;
			list-style-position: inside;
		}
		.center {
			display:flex;
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
 <h1 class="center">${author.nickname}'s blog entries</h1>
 <c:choose>
	<c:when test="${empty entries}">
		<h2 class="center">This user has no entries.</h2>
	</c:when>
	<c:otherwise>
	<div class ="center">
		<ul>
			<c:forEach var = "entry" items= "${entries}">
				<li><a href="<c:url value = "/servleti/author/${author.nickname}/${entry.id}" />">${entry.title}</a></li>
			</c:forEach>
		</ul>
	</div>
	</c:otherwise>
 </c:choose>
 <div class="center">
	<c:if test="${sessionScope['current.user.nick'] == author.nickname}">
		<form method = "GET" action="<c:url value="/servleti/author/${sessionScope['current.user.nick']}/new"/>">
			<button type="submit">New entry</button>
		</form>
	</c:if>
</div>


 </body>
</html>