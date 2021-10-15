<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.Duration" %>
<!DOCTYPE html>
<html>
<head>
  <title>App Info</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
	}
	.center {
		display: block;
		text-align: center;
	}
  </style>
</head>
<body>
	<h1 class = "center"> This application has been running for:</h1>
	<h2 class = "center"> 
		<% 
			Instant start = (Instant)request.getServletContext().getAttribute("START");
			Duration d = Duration.between(start, Instant.now());
			out.write(d.toDays() + " days, " + d.toHours()%24 + " hours, " + d.toMinutes()%60 + " minutes and " + d.toSeconds() % 60 + " seconds.");
		%>
	</h2>
	<a class = "center" href="/webapp2">Home</a>
	
</body>
</html>
