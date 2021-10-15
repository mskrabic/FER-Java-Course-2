<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <title>Glasanje</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
	}
  </style>
</head>
 <body>
 <h1>Dostupne ankete:</h1>
 <ol>
	<c:forEach var = "i" begin = "0" end = "${polls.size()-1}">
		<li><a href="<c:url value = "/servleti/glasanje?pollID=${polls.get(i).id}"/>">${polls.get(i).title}</a></li>
	</c:forEach>
 </ol>
 </body>
</html>