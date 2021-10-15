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
		.comment {
			border: 2px black solid;
			width: 40%;
			margin-left: 30%;
		}
		.center {
			display: flex;
			justify-content: center;
		}
		.comment_header {
			padding: 5px;
			background-color: beige;
		}
		.comment_text {
			background-color: lightblue;
			font-size: 1.2em;
		}
		.post {
			width:40%;
			margin-left: 30%;
			margin-bottom: 10px;
			text-align: left;
			border: 1px solid black;
			background-color: lightblue;
			font-size: 1.2em;
		}
		.edit {
			width: 40%;
			margin-left: 60%;
		}
		.new-comment {
			margin-top: 10px;
			width: 100%;
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
 <h1 class="center">${entry.title}</h1>
 <h3 class="center"> Posted: ${entry.createdAt}</h3>
  <h3 class="center"> Last modified: ${entry.lastModifiedAt}</h3>
 <p class="post">${entry.text}</p>
 <c:if test="${sessionScope['current.user.nick'] == author.nickname}">
	<div class="edit">
		<form method="GET" action="<c:url value="/servleti/author/${author.nickname}/edit"/>">
			<input type="text" name="id" value="${entry.id}" hidden readonly/>
			<button class="editbtn" type="submit">Edit</button>
		</form>
	</div>
	<br/>
 </c:if>
 <c:forEach var="comment" items="${comments}">
	<div class="comment">
		<div class="comment_header">
			${comment.usersEMail} (${comment.postedOn}):
		</div>
		<div class="comment_text">
			${comment.message}
		</div>
	</div>
 </c:forEach>
 <div class="center">
	 <form method = "POST" action="<c:url value="/servleti/author/${author.nickname}/${entry.id}/newcomment"/>">
		<textarea class="new-comment" name="comment">
		</textarea>
		<button type="submit">Add comment </button>
	 </form>
 </div>
 </body>
</html>