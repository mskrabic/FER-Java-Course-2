<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
	}
  </style>
</head>
<body>
	<h1>Invalid parameters passed.</h1>
	<p>
		a must be in range [-100, 100], b must be in range [-100, 100] and n must be in range [1, 5].
	</p>
	<a href="/webapp2">Home</a>
</body>
</html>
