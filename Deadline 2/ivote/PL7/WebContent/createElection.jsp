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
<title>Criar eleicões</title>
</head>
<body>
		<a href="adminNot.jsp"><button class="buttonA button-blockA">Home</button></a>
		<a href="registaPessoa.jsp"><button class="buttonA button-blockA">RegistarPessoa</button></a> 
		<a href="createElection.jsp"><button class="buttonA button-blockA">Criar Eleição</button></a> 
		<a href="createFaculty.jsp"><button class="buttonA button-blockA">Criar Faculdade</button></a> 
		<a href="createDep.jsp"><button class="buttonA button-blockA">Criar Departamento</button></a> 
		<a href="createTable.jsp"><button class="buttonA button-blockA">Criar mesa de voto</button></a>  
		<a href=""><button class="buttonA button-blockA">Criar listas de candidatos</button></a> 
		<a href="propElection.jsp"><button class="buttonA button-blockA">Alterar propriedades de uma eleição</button></a> 
		<a href="userProps.jsp"><button class="buttonA button-blockA">Alterar propriedades de um eleitor</button></a> 
		<a target="_blank" href="Listagem.jsp"><button class="buttonA button-blockA">Listagens</button></a>
		<!--  <a  href="<s:url action="listFaculty"/>"><button class="buttonA button-blockA">Listar Faculdades</button></a>-->

	
	
	<div class="form">
		<div id="register">
			<form action="createElection" method="post">
				
				<div class="field-wrap">
					<label> Tipo (Geral/Faculdade/Departamento/Núcleo))<span class="req">*</span>
					</label> <input type="text" name="tipo" id="tipo" required
						autocomplete="off" />
				</div>
				
				<div class="field-wrap">
					<label> Entidade(Se geral digite: geral))<span class="req">*</span>
					</label> <input type="text" name="entidade" id="entidade" required
						autocomplete="off" />
				</div>
				
				<div class="field-wrap">
					<label> Tilulo<span class="req">*</span>
					</label> <input type="text" name="titulo" id="titulo" required
						autocomplete="off" />
				</div>
				
				<div class="field-wrap">
					<label> Resumo<span class="req">*</span>
					</label> <input type="text" name="resumo" id="resumo" required
						autocomplete="off" />
				</div>
				
				<div class="field-wrap">
					<label> Data-Inicio(hh:mm:ss dd-mm-yyyy) <span class="req">*</span>
					</label> <input type="text" name="dataI" id="dataI" required
						autocomplete="off" />
				</div>
				
				<div class="field-wrap">
					<label> Data-Fim(hh:mm:ss dd-mm-yyyy)<span class="req">*</span>
					</label> <input type="text" name="dataF" id="dataF" required
						autocomplete="off" />
				</div>
				
				

				<button class="button button-block">Create</button>
			</form>
		</div>
	</div>
	
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="js/index.js"></script>
	
</body>
</html>