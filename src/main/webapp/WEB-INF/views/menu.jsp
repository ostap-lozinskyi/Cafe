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


    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/resources/css/rateStars.css" type="text/css"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
            integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"
            integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4"
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"
            integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1"
            crossorigin="anonymous"></script>

    <link href="/resources/css/index.css" rel="stylesheet">
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
    <a class="headerButton " href=""
            data-toggle="collapse" data-target="#firstCollapse"
            aria-expanded="false" aria-controls="firstCollapse">
        Search meal
    </a>
    <a class="headerButton" href="/place">
        <c:if test="${empty selectedMeals}">
            <img class="footerCartIcon" src="/resources/img/cart.png">
        </c:if>
        <c:if test="${!empty selectedMeals}">
            <img class="footerCartIcon" src="/resources/img/cartFilled.png">
        </c:if>
    </a>

</header>
<div class="container">
    <div class="collapse" id="firstCollapse">
        <div class="card card-body">
            <form:form action="/menu" method="GET" modelAttribute="mealFilter">
                <div class="form-group searchContainer">
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
                    <div class="searchItem">
                        <form:input path="search" class="form-control" placeholder="By name"/>
                    </div>
                    <div class="searchItem">
                        <button class="selectButton" type="button"
                                data-toggle="collapse" data-target="#secondCollapse"
                                aria-expanded="false" aria-controls="secondCollapse">
                            Cuisine
                        </button>
                        <div class="collapse" id="secondCollapse">
                            <div class="card card-body">
                                <form:checkboxes items="${cuisines}" path="cuisineName" element="div"/>
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