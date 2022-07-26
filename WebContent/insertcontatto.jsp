<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	HttpSession session2 = request.getSession();
		String messaggio = (String)session2.getAttribute("messaggio");
		
%>

<h2 style="color: red"><%=messaggio %></h2>
<form method="post" action="http://localhost:8080/RubricaWeb/miaservlet">
	<h1>INSERISCI I DATI DEL CONTATTO (Con numero singolo)</h1>
	Nome: <input type="text" name="nome"><br><br>
	Cognome: <input type="text" name="cognome"><br><br>
	E-Mail: <input type="text" name="email"><br><br>
	<br><h1>Numero di Telefono</h1>
		Numero: <input type="text" name="numero1"><br>
		<input type="hidden" name="operazione" value="insert">
		<input type="submit" value="invia"><br>
	</form>
</body>
</html>