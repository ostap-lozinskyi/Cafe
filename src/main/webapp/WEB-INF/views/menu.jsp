<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

    <link rel="stylesheet" href="/resources/css/rateStars.css" type="text/css"/>
    <script src="/resources/js/jquery.js"></script>
    <link href="/resources/css/index.css" rel="stylesheet">
    <script src="/resources/js/cart.js"></script>
    <title>Menu</title>
</head>
<body style="background: url(/resources/img/backgroundImage2.jpg)">
<header>
    <a class="headerButton" href="/">
        Main page
    </a>
    <a class="headerButtonActive" href="/menu">
        Menu
    </a>
    <a class="headerButton" href="/ingredient">
        Ingredients
    </a>
    <a class="headerButton" href="/place">
        Tables
    </a>
    <div class="searchMealDropdown">
        <a class="headerButton" href="">
            Search meal
        </a>
        <div class="searchMealDropdown-content">
            <form:form action="/menu" method="GET" modelAttribute="mealFilter">
                <div class="form-group searchMealContainer">
                    <div class="searchMealRows">
                        <div class="searchMealLeftRow">
                            <div class="searchItem">
                                <form:input path="search" class="form-control" placeholder="By name"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="minRate" class="form-control" placeholder="Min rate"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="maxRate" class="form-control" placeholder="Max rate"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="minPrice" class="form-control" placeholder="Min price"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="maxPrice" class="form-control" placeholder="Max price"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="minWeight" class="form-control" placeholder="Min weight"/>
                            </div>
                            <div class="searchItem">
                                <form:input path="maxWeight" class="form-control" placeholder="Max weight"/>
                            </div>
                        </div>
                        <div class="searchMealRightRow">
                            <div class="searchCheckbox">
                                <form:checkboxes items="${cuisines}" path="cuisineName" element="div" class = "searchCheckboxes"/>
                            </div>
                        </div>
                    </div>
                    <div class="searchItem">
                        <button type="submit" class="btnCafe ">Search</button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
    <a id="openCart" class="headerButton">
        <c:if test="${empty selectedMeals}">
            <img class="footerCartIcon" src="/resources/img/cart.png">
        </c:if>
        <c:if test="${!empty selectedMeals}">
            <img class="footerCartIcon" src="/resources/img/cartFilled.png">
        </c:if>
    </a>
</header>
<!-- The Modal -->
<div id="cartModal" class="cartModal">

    <!-- Modal content -->
    <div class="cartModal-content">
        <span class="cartClose">&times;</span>
        <h1>Selected meals:</h1>
        <div class="cartMeals">
            <c:forEach var="selectedMeal" items="${selectedMeals.mealDTOS}">
                <div class="cartMeal">
                    <div>
                        <img src="${selectedMeal.photoUrl}?version=${selectedMeal.version}"
                             class="cartMealPhoto">
                    </div>
                    <div><h2>${selectedMeal.name}</h2></div>
                    <div class="cartRightRow">
                        <div>
                            <a href="/removeMealFromOrder/${selectedMeal.id}">
                                <h2>Remove</h2>
                            </a>
                        </div>
                        <div class="cartPrice">${selectedMeal.price}</div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <a href="/setStatusAccepted">
            <button type="button" class="btnCafe">
                Confirm order
            </button>
        </a>
    </div>
</div>
<div class="text-center">
    <c:if test="${empty meals.content}">
        <h2 class="text-center">Meals with such parameters not found</h2>
    </c:if>
</div>
<div class="mealsContainer">
    <c:forEach var="meal" items="${meals.content}">
        <div class="itemContainer">
            <div class="item">
                <div class="itemImage">
                    <a href="/menuItem/${meal.id}"> <img
                            src="${meal.photoUrl}?version=${meal.version}"
                            style="width: 313px">
                    </a>
                    <div class="clear"></div>
                </div>
                <div class="hover">
                    <div class="itemMealName">
                        <a href="/menuItem/${meal.id}"><span>${meal.name}</span></a>
                    </div>
                    <div class="hide">
                        <div class="rating">
                            <div class="message"></div>
                            Rate: ${meal.rate}
                        </div>
                        <div class="itemWeight">
                            <div class="weight">${meal.weight}gr</div>
                        </div>
                        <div class="itemShortDescription">
                            <p>${meal.shortDescription}</p>
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
                            <a href="/menu/addMealToOrder/${meal.id}">
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
<footer>
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