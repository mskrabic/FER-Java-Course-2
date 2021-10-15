<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Trigonometric</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
	}
	table, th, td {
		border: 1px solid black;
	}
	.center {
		text-align: center;
		display: block;
	}
  </style>
</head>
<body>
  <h1 class = "center">Trigonometric values for angles between ${a} and ${b}</h1>
  <a class ="center" href="/webapp2">Home</a>
  <table>
  <tr>
    <th>x</th>
    <th>sin(x)</th>
    <th>cos(x)</th>
  </tr>
  <c:forEach var = "i" begin = "0" end = "${table.angleList.size()-1}">
	<tr><td>${table.angleList.get(i)}</td><td>${table.sinList.get(i)}</td><td>${table.cosList.get(i)}</td>
  </c:forEach>
  </table>
</body>
</html>
