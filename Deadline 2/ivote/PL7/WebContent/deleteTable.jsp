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
		<a href="adminNot.jsp"><button class="buttonA button-blockA">Home</button></a>
		<a href="registaPessoa.jsp"><button class="buttonA button-blockA">RegistarPessoa</button></a> 
		<a href=""><button class="buttonA button-blockA">CriarEleição</button></a> 
		<a href=""><button class="buttonA button-blockA">Criarmesa de voto</button></a> 
		<a href=""><button class="buttonA button-blockA">Criarlistas de candidatos</button></a> 
		<a href=""><button class="buttonA button-blockA">Alterar propriedades de uma eleição</button></a> 
		<a target="_blank" href="Listagem.jsp"><button class="buttonA button-blockA">Listagens</button></a>
		<a href="tables.jsp"><button class="buttonA button-blockA">Alterar mesas eleicao</button></a>
		
		<p>
		<p>
		<a href="addPersonTable.jsp"><button class="buttonA button-blockA">Add pessoa Mesa</button></a> 
		<a href="addTable.jsp"><button class="buttonA button-blockA">Add Mesa</button></a> 
		<a href="deleteTable.jsp"><button class="buttonA button-blockA">Apagar Mesa</button></a> 
		<a href="changeLocationTable.jsp"><button class="buttonA button-blockA">Change Location Mesa</button></a>
		<a href="deletePersonTable.jsp"><button class="buttonA button-blockA">Delete person Mesa</button></a>
		

		<p>
		<c:forEach items="${heyBean.tablesDelete}" var="value">
		<c:out value="${value}" /><p>
		</c:forEach>
		
		
		<div class="form">
		<div id="delete_table">
			<form action="delete_table" method="post">

				<div class="field-wrap">
					<label> Id election <span class="req">*</span>
					</label> <input type="text" name="id" id="id" required
						autocomplete="off" />
				</div>

				<div class="field-wrap">
					<label> Name of table<span class="req">*</span>
					</label> <input type="text" name="nome_mesa" id="nome_mesa" required autocomplete="off" />
				</div>

				<button class="button button-block">Delete</button>
			</form>
		</div>
	</div>


	
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="js/index.js"></script>
		
	
	
</body>
</html>