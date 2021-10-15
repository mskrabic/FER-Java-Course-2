<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Home</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
		text-align: center;
	}
  </style>
</head>
<body>
	<h1><a href="setcolor">Background color chooser</a></h1>
	<h1><a href="trigonometric?a=0&b=90">Trigonometric values</a></h1>
	<form action="trigonometric" method="GET">
		Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>
	<h1><a href="report.jsp">OS usage report</a></h1>
	<h1><a href="power?a=1&b=100&n=3">Generate MS Excel file</a></h1>
	<h1><a href="appinfo.jsp">App info </a></h1>
	<h1><a href="glasanje">Voting application </a></h1>
</body>
</html>
