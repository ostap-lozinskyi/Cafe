<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="custom" uri="/WEB-INF/tags/implicit.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-118033320-4"></script>
	<script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'UA-118033320-4');
	</script>


	<link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
<link rel="stylesheet" href="resources/css/rateStars.css" type="text/css"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>

<link href="/resources/css/index.css" rel="stylesheet">
<title>Menu</title>
</head>
<body style="background: url(/resources/img/backgroundImage2.jpg)">
<header>
	<a class="headerButton" href="/">
		Main page
	</a>
	<a class="headerButton" href="/menu">
		Menu
	</a>
	<a class="headerButtonActive" href="/ingredient">
		Ingredients
	</a>
	<a class="headerButton" href="/place">
		Tables
	</a>
</header>
	<div class="container" style="background-color: white;">
		<div class="row">
			<div class="col-3">
				<form:form action="ingredient" method="GET" modelAttribute="filter">
					<div class="form-group row">
						<div class="col-12">
							<form:input class="form-control" path="search" placeholder="Search"/>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-9 col-xs-12">
				<table class="table table-bordered">
					<tr>
						<th class="text-center">Name</th>
					</tr>
					<c:if test="${empty ingredients.content}">
		    			<tr>
		    			<td colspan=2><h3 class="text-center">Ingredients with such name not found</h3></td>
		    			</tr>
					</c:if>
					<c:forEach var="ingredient" items="${ingredients.content}">
						<tr>
							<td><a href="/ingredient/${ingredient.id}">${ingredient.name}</a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<div class="col-md-3 col-xs-12">
				<div class="row">
					<div class="col-6 text-center">
							<button class="dropdown-toggle btn btn-outline-success btn-sm" type="button" data-toggle="dropdown">Sort
							</button>
							<div class="dropdown-menu">
								<custom:sort innerHtml="Name asc" paramValue="name"/>
								<custom:sort innerHtml="Name desc" paramValue="name,desc"/>
							</div>
					</div>
					<div class="col-6 text-center">
						<custom:size posibleSizes="1,2,5,10" size="${ingredients.size}" />
					</div>
				</div>
			</div>
		</div>
        <br>
		<div class="row">
			<div class="col-12">
				<custom:pageable page="${ingredients}"/>
			</div>
		</div>
	</div>
	<footer class="fixed-bottom">
		<div class="footerFirstRow">
			<div class="footerText">
				© Cafe 2017
			</div>
			<div class="footerFollow">
				Follow Us:
				<a href="https://ua.linkedin.com/in/ostap-lozinskyj" target="_blank">
					<img src="/resources/img/linkedin-logo.jpg" class="footerLogo">
				</a>
			</div>
		</div>
		<div class="footerDevelopedBy">
			Developed by <a href="https://ua.linkedin.com/in/ostap-lozinskyj" target="_blank" class="underlined">Ostap
			Lozinskyi</a>
		</div>
	</footer>
</body>
</html>