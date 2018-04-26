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
<title>ListaFaculdades</title>
</head>
<body>
		<a href="<s:url action="listDepartamentos"/>"><button class="buttonA button-blockA">Departamentos</button></a>
		<a href="<s:url action="listEleicoes"/>"><button class="buttonA button-blockA">Eleicoes</button></a> 
		<a href="<s:url action="listFaculty"/>"><button class="buttonA button-blockA">Faculdade</button></a> 
		<a href="<s:url action="listMesas"/>"><button class="buttonA button-blockA">Mesas</button></a> 
		<a href="<s:url action="listResultados"/>"><button class="buttonA button-blockA">Resultados Eleicoes</button></a> 
		<a href="<s:url action="listPessoas"/>"><button class="buttonA button-blockA">Pessoas</button></a> 

	<p>
	<c:forEach items="${heyBean.allEleicoes}" var="value">
		<c:out value="${value}" /><p>
	</c:forEach>
	
</body>
</html>