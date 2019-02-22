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
<link rel="stylesheet" href="/resources/css/rateStars.css" type="text/css"/>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>

<link href="/resources/css/index.css" rel="stylesheet">
<title>MealId</title>
</head>
<body style="background: url(/resources/img/backgroundImage2.jpg)">
	<div class="container"  style="background-color: white;">
		<div class="row">
			<div class="col-lg-2 col-sm-4">
				<br>
				<a class="btnCafe " href="/">Main page</a>
			</div>
			<div class="col-lg-2 col-sm-4">
				<br>
				<a class="btnCafe " href="/menu">Menu</a>
			</div>
			<div class="col-lg-2 col-sm-4">
				<br>
				<a class="btnCafe " href="/ingredient">Ingredients</a>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-4">
				<img class="product-img" src="${meal.photoUrl}?version=${meal.version}" width="400">
			</div>
			<div class="col-8">
				<div class="banner_info">
					<div class="back"></div>
					<div class="front">
						<div>
							<h1 class="center">${meal.name}</h1>
							<div class="itemWeight">
								<div>Weight ${meal.weight}gr</div>
							</div>
							<div class="shortDescription">
								<p>${meal.fullDescription}</p>
							</div>
							<div>
								<p>Rate: ${meal.rate}</p>
							</div>
							<div class="itemPriceRow">
								<div class="price">
									<span class="priceValue">${meal.price}</span> $
								</div>
							</div>
							<div class="buy_row">
								<sec:authorize access="isAnonymous()">
									<a href="/login">
										<button type="button"
											class="btnCafe ">Order</button>
									</a>
								</sec:authorize>
								<sec:authorize access="isAuthenticated()">
									<a href="/place">
										<button type="button"
											class="btnCafe ">Order</button>
									</a>
								</sec:authorize>
								
								<form:form action="/menuItem/${meal.id}" method="POST" modelAttribute="comment">
									<custom:hiddenInputs excludeParams="text, _csrf"/>
									<br>
									<div class="row">
										<div class="col-10 ml-auto" style="color: red;">
											<form:errors path="text" />
											${tasteMeal}
										</div>
									</div>
									<div class="form-group row">
										<div class="col-12">
											<form:textarea class="form-control" id="text" rows="3" path="text" placeholder="Enter your comment"></form:textarea>
										</div>
									</div>
									<div class="star-rating">
										<div class="star-rating__wrap">
											<input class="star-rating__input fa" id="star-rating-5" type="radio" name="rate" value="5" title="5 out of 5 stars"> 
											<input class="star-rating__input fa" id="star-rating-4"	type="radio" name="rate" value="4" title="4 out of 5 stars">
											<input class="star-rating__input fa" id="star-rating-3"	type="radio" name="rate" value="3" title="3 out of 5 stars"> 
											<input class="star-rating__input fa" id="star-rating-2"	type="radio" name="rate" value="2" title="2 out of 5 stars"> 
											<input class="star-rating__input fa" id="star-rating-1"	type="radio" name="rate" value="1" title="1 out of 5 stars">
										</div>
									</div>
									<div class="form-group row">
										<div class="col-8 mr-auto">
											<sec:authorize access="isAnonymous()">
												<a href="/login">Sign up for a meal assessment</a>
											</sec:authorize>
											<sec:authorize access="isAuthenticated()">
												<button class="btn btn-sm btn-outline-success">Save</button>
												<a href="/meal/${meal.id}<custom:allParams/>" class="btn btn-sm btn-outline-warning">Cancel</a>
											</sec:authorize>
										</div>
									</div>
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-12">
				<h3 class="text-center">Comments</h3>
				<c:forEach var="commentsOfmeal" items="${meal.comments}">
					<div class="comment">
						<div class="row">
							<div class="col-2">
								<img src="${commentsOfmeal.user.photoUrl}?version=${commentsOfmeal.user.version}" style="width: 50px"> ${commentsOfmeal.user.email}
							</div>
							<div class="col-10">
								${commentsOfmeal.text}
							</div>
						</div>
					</div>
				</c:forEach>
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
			Lozinskyj</a>
		</div>
	</footer>
</body>
</html>