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
 <h1>Glasanje za omiljeni bend:</h1>
 <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
glasali!</p>
 <ol>
	<c:forEach var = "i" begin = "0" end = "${bands.size()-1}">
		<li><a href="glasanje-glasaj?id=${bands.get(i).id}">${bands.get(i).name}</a></li>
	</c:forEach>
 </ol>
 </body>
</html>