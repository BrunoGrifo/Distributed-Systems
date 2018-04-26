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
<title>Alterar propriedades de uma eleição</title>
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
		
		
		<div class="form">
		<div id="register">
			<form action="userProp" method="post">
				<div class="field-wrap">
					<label> Introduza apenas os dados que pretende alterar
					</label>
				</div><br><br><br><br>
				
				<div class="field-wrap">
					<label> Id da pessoa a modificar<span class="req">*</span>
					</label> <input type="text" name="cc" id="cc" required
						autocomplete="off"  />
				</div>
				<div class="field-wrap">
					<label> Password
					</label> <input type="text" name="password" id="password"  />
				</div>
				<div class="field-wrap">
					<label> Telemovel
					</label> <input type="text" name="telemovel" id="telemovel"  />
				</div>
				<div class="field-wrap">
					<label> Morada
					</label> <input type="text" name="morada" id="morada"  />
				</div>
				<div class="field-wrap">
					<label> Validade CC
					</label> <input type="text" name="vcc" id="vcc"  />
				</div>
				<div class="field-wrap">
					<label> Cargo
					</label> <input type="text" name="cargo" id="cargo"  />
				</div>
				
				<div class="field-wrap">
					<label> Para alterar ambos o departamento e a faculdade, basta introduzir o departamento. A respectiva faculdade será introduzida automaticamente
					</label>
				</div><br><br><br><br><br><br><br><br>
				
				<div class="field-wrap">
					<label> Departamento
					</label> <input type="text" name="dep" id="dep"  />
				</div>
				
				<div class="field-wrap">
					<label> Faculdade
					</label> <input type="text" name="fac" id="fac"  />
				</div>
				
				


				<button class="button button-block">Aplicar</button>
			</form>
		</div>
	</div>

	<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="js/index.js"></script>

</body>
</html>