<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ page import="java.util.Random" %>
<%@ page import="java.awt.Color" %>
<!DOCTYPE html>
<html>
<head>
  <title>Funny story</title>
  <style>
	body {
		background-color: <c:choose><c:when test= "${pickedBgCol == null}"> white;</c:when><c:otherwise>${pickedBgCol};</c:otherwise></c:choose>
		color: <%
			Random random = new Random();
			int r = random.nextInt(255);
			int g = random.nextInt(255);
			int b = random.nextInt(255);
			Color c = new Color(r, g, b);
			String hex = "#" + Integer.toHexString(c.getRGB() & 0xFFFFFF) + ";";
			out.write(hex);
		%>
	}
  </style>
</head>
<body>
	<a href="/webapp2">Home</a>
	<p>
		Ovo je priča koju vrlo rado pričam, <br/>
		To je priča o Boži zvanom "Pub". <br/>
		Jedni ga hvale, drugi žale, treći kažu: "E, moj brale, <br/>
		Taj je bio kvaran kao šupalj zub". <br/>
	</p>
	<p>
		Odavde pa sve do Pešte i do Srema na jug <br/>
		Jos priča bajke o njemu mutni kockarski krug <br/>
		I kažu: "Taj u životu nije igr'o na dug!" <br/>
		I svi se slažu kako danas nema takvih kao Boža Pub. <br/>
	</p>
</body>
</html>
