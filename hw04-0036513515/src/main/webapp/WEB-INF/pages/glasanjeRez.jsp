<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="oprpp2.hw04.servlets.GlasanjeServlet.Band" %>
<%@ page import="java.util.List" %>
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
		 <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
		 <tbody>
			<c:forEach var = "result" items="${results}">
				<tr><td>${result.band.name}</td><td>${result.votes}</td></tr>
			</c:forEach>
		 </tbody>
		 </table>
	</c:otherwise>
 </c:choose>

 <h2>Grafički prikaz rezultata</h2>
 <img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

 <h2>Rezultati u XLS formatu</h2>
 <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a></p>

 <h2>Razno</h2>
 <p>Primjeri pjesama pobjedničkih bendova:</p>
 <ul>
 	<c:forEach var = "winner" items="${winners}">
		 <li><a href="${winner.band.url}" target="_blank">${winner.band.name}</a></li>
	</c:forEach>
 </ul>
 </body>
</html>