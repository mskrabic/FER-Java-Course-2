<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Report</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
	}
  </style>
</head>
<body>
	<h1>OS usage</h1>
	<p>
		Here are the results of OS usage in survey that we completed.
	</p>
	<img src="reportImage"/> <br/>
	<a href="/webapp2">Home</a>
</body>
</html>
