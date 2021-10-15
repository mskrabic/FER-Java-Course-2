<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Color chooser</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>

	}
  </style>
</head>
<body>
	<a href="/webapp2">Home</a>
	<hr>
	<ul>
		<li><a href="setcolor?color=white">WHITE</a></li>
		<li><a href="setcolor?color=red">RED</a></li>
		<li><a href="setcolor?color=green">GREEN</a></li>
		<li><a href="setcolor?color=cyan">CYAN</a></li>
    </ul>
</body>
</html>
