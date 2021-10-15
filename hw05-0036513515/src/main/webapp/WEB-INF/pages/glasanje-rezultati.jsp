<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="oprpp2.hw05.model.PollOption" %>

<!DOCTYPE html>
<html>
 <head>
    <title>Rezultati</title>
	<style type="text/css">
		table.rez td {text-align: center;}
		body {
			background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
		}
	</style>
 </head>
 <body>

 <h1>Rezultati glasanja</h1>
 <p>Ovo su rezultati glasanja.</p>
 <c:choose>
	<c:when test="${results.isEmpty()}">
		<p>Trenutno nema glasova.</p>
	</c:when>
	<c:otherwise>
		 <table border="1" class="rez">
		 <thead><tr><th>Ime</th><th>Broj glasova</th></tr></thead>
		 <tbody>
			<c:forEach var = "result" items="${results}">
				<tr><td>${result.title}</td><td>${result.votesCount}</td></tr>
			</c:forEach>
		 </tbody>
		 </table>
	</c:otherwise>
 </c:choose>

 <h2>Grafički prikaz rezultata</h2>
 <img alt="Pie-chart" src=" <c:url value = "/servleti/glasanje-grafika?pollID=${pollID}"/> " width="400" height="400" />

 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href=" <c:url value = "/servleti/glasanje-xls?pollID=${pollID}"/> ">ovdje</a></p>

 <h2>Razno</h2>
 <p>Linkovi pobjednika:</p>
 <ul>
 	<c:forEach var = "winner" items="${winners}">
		 <li><a href="${winner.link}" target="_blank">${winner.title}</a></li>
	</c:forEach>
 </ul>
 </body>
</html>