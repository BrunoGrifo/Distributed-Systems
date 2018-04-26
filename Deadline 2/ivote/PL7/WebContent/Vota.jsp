<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" href="css/style.css">
<title>Tables</title>
</head>
<body>
		<a href="hey.jsp"><button class="buttonA button-blockA">Inicio</button></a>
		<a href="listaEleicoes.jsp"><button class="buttonA button-blockA">Lista Eleicoes e listas</button></a> 
		<a href="EscolheEleicao.jsp"><button class="buttonA button-blockA">Votar</button></a> 
		<a href="index.jsp"><button class="buttonA button-blockA">Logoff</button></a> 

	<c:forEach items="${heyBean.cenasEleicao}" var="value">
		<c:out value="${value}" /><br>
	</c:forEach>
	
	<div class="form">
		<div id="escolher_lista">
			<form action="escolher_lista" method="post">

				<div class="field-wrap">
					<label> Id Lista <span class="req">*</span>
					</label> <input type="text" name="id_lista" id="id_lista" required
						autocomplete="off" />
				</div>

				<button class="button button-block">Vote</button>
			</form>
		</div>
	</div>

<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="js/index.js"></script>

</body>
</html>
