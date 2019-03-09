<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-118033320-4"></script>
    <script>
        window.dataLayer = window.dataLayer || [];

        function gtag() {
            dataLayer.push(arguments);
        }

        gtag('js', new Date());

        gtag('config', 'UA-118033320-4');
    </script>

    <link href="/resources/css/index.css" rel="stylesheet">
    <script src="/resources/js/jquery.js"></script>
    <script src="/resources/js/radioButton.js"></script>
    <title>Order</title>
</head>
<body style="background: url(/resources/img/backgroundImage2.jpg)">
<header>
    <a class="headerButton" href="/">
        Main page
    </a>
    <a class="headerButton" href="/menu">
        Menu
    </a>
    <a class="headerButton" href="/ingredient">
        Ingredients
    </a>
    <a class="headerButton" href="/place">
        Tables
    </a>
</header>

<div class="mealsContainer">
    <c:forEach var="meal" items="${selectedMeals.mealDTOS}" end="4">
        <div class="itemContainer">
            <div class="item">
                <div class="itemImage">
                    <a href="/menuItem/${meal.id}">
                        <img src="${meal.photoUrl}?version=${meal.version}" style="width: 313px">
                    </a>
                </div>
                <div class="hover">
                    <div class="itemMealName">
                        <a href="/menuItem/${meal.id}">${meal.name}</a>
                    </div>
                    <div class="hide">
                        <div class="rating">
                            Rate: ${meal.rate}
                        </div>
                        <div class="itemWeight">
                            <div class="weight">${meal.weight} gr</div>
                        </div>
                        <div class="itemShortDescription">
                            <p>${meal.fullDescription}</p>
                        </div>
                    </div>
                    <div class="itemPriceRow">
                        <div class="price">
                            <p class="priceValue">${meal.price}</p>
                            <p class="priceText">$</p>
                        </div>
                    </div>
                    <div class="itemOrderRow">
                        <sec:authorize access="isAnonymous()">
                            <a href="/login">
                                <button type="button" class="btnCafe">
                                    Order
                                </button>
                            </a>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <a href="/addMealToOrder/${meal.id}">
                                <button type="button" class="btnCafe">
                                    Order
                                </button>
                            </a>
                        </sec:authorize>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
<div>
    <h1 class="menuHeadline">Select table</h1>
</div>
<div class="placeRow">
    <div class="placesList">
        <h1>Free tables</h1>
        <form:form action="/order/acceptOrder" method="POST" modelAttribute="order">
            <custom:hiddenInputs excludeParams="name, _csrf"/>
            <%--<div class="row">--%>
            <%--<div class="col-10 ml-auto" style="color: red;">--%>
            <%--<form:errors path="name"/>--%>
            <%--</div>--%>
            <%--</div>--%>
            <div class="form-group">
                <div class="freePlacesList">
                    <div class="freePlaceItem">
                        <h3>For 2 person</h3>
                        <c:forEach var="place" items="${places}">
                            <c:if test="${place.free && place.countOfPeople == 2}">
                                <label class="radio-inline">
                                    <input type="radio" name="place" id="customer" value="${place.id}"/>${place.name}
                                </label>
                            </c:if>
                        </c:forEach>
                    </div>
                    <div class="freePlaceItem">
                        <h3>For 4 person</h3>
                        <c:forEach var="place" items="${places}">
                            <c:if test="${place.free && place.countOfPeople == 4}">
                                <label class="radio-inline">
                                    <input type="radio" name="place" id="customer" value="${place.id}"/>${place.name}
                                </label>
                            </c:if>
                        </c:forEach>
                    </div>
                    <div class="freePlaceItem">
                        <h3>For 6 person</h3>
                        <c:forEach var="place" items="${places}">
                            <c:if test="${place.free && place.countOfPeople == 6}">
                                <label class="radio-inline">
                                    <input type="radio" name="place" id="customer" value="${place.id}"/>${place.name}
                                </label>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <button class="btnCafe">
                    Confirm order
                </button>
                    <%--<a href="/admin/adminCuisine/cancel<custom:allParams/>"--%>
                    <%--class="btn btn-sm btn-outline-warning">Cancel</a>--%>
            </div>
        </form:form>
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