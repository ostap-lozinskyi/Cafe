<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ostap cafe</title>
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
    <script src="/resources/js/cartModal.js"></script>
</head>

<body>
<div class="containerResize" style="background: url(/resources/img/mainBackgroundImage.jpg); height: 714px;">
    <script>
        $('.containerResize').css('height', $(window).height());
    </script>
    <div class="shadowBlock"></div>
    <div class="content">
        <div class="headerRow">
            <div class="contactUsContainer">
                <a class="contactUsLink">Contact us</a>
                <br><a class="phoneLink"> +380 67 67 55 230</a>
            </div>
            <div class="mainLogoContainer">
                <img class="mainLogo" src="/resources/img/cafe-logo.png" alt="Cafe" title="Cafe">
            </div>
            <div class="headerButtonsContainer">
                <h2>${message}</h2>
                <div class="horizontalButtons">
                    <sec:authorize access="isAnonymous()">
                        <button type="button" class="transparentButton" onclick="location.href='/registration'">
                            Register
                        </button>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN')">
                        <button type="button" class="transparentButton" onclick="location.href='/userCabinet'">
                            Cabinet
                        </button>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <button type="button" class="transparentButton" onclick="location.href='/admin'">
                            Admin page
                        </button>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        <button type="button" class="transparentButton" onclick="location.href='/login'">
                            Login
                        </button>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <form:form action="/logout">
                            <button class="transparentButton">Logout</button>
                        </form:form>
                    </sec:authorize>
                    <sec:authorize access="isAuthenticated()">
                        <!-- Trigger/Open The Modal -->
                        <button id="openCart" class="transparentButton">
                            <c:if test="${empty selectedMeals.mealDTOS}">
                                <img class="cartIcon" src="/resources/img/cart.png">
                            </c:if>
                            <c:if test="${!empty selectedMeals.mealDTOS}">
                                <img class="cartIcon" src="/resources/img/cartFilled.png">
                            </c:if>
                        </button>

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
                                <a href="/order">
                                    <button type="button" class="btnCafe">
                                        Confirm order
                                    </button>
                                </a>
                            </div>
                        </div>
                    </sec:authorize>
                </div>
            </div>
        </div>
        <div class="buttonContainerWrapper">
            <div class="buttonContainer">
                <a href="/menu" class="centralButton buttonLeft">Menu</a><a href="/place"
                                                                            class="centralButton buttonRight">Tables</a>
            </div>
        </div>
    </div>
    <div class="scroll"></div>
    <script>
        $('.scroll').click(function () {
            $("html, body").animate({scrollTop: $(window).height()}, 600);
        });
    </script>
</div>

<div style="background: url(/resources/img/backgroundImage2.jpg);">
    <h1 class="menuHeadline">Top rated meals</h1>
    <div class="mealsContainer">
        <c:forEach var="meal" items="${meals}" end="4">
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
    <div class="newsSection">
        <div>
            <h2 class="menuHeadline">The latest news</h2>
        </div>
        <div class="newsContent">
            <div class="newsItem">
                <div class="newsDate">20.11.2018</div>
                <div class="newsText">
                    Project migrated to Spring Boot version 2.0.6
                </div>
                <div class="newsDetails">
                    <button type="button" class="newsDetailsButton" onclick="location.href='/registration'">
                        See details
                    </button>
                </div>
            </div>
            <div class="newsItem">
                <div class="newsDate">15.11.2018</div>
                <div class="newsText">
                    Bugs fixed
                </div>
                <div class="newsDetails">
                    <button type="button" class="newsDetailsButton" onclick="location.href='/registration'">
                        See details
                    </button>
                </div>
            </div>
            <div class="newsItem">
                <div class="newsDate">03.11.2018</div>
                <div class="newsText">
                    Section 'News' added.
                </div>
                <div class="newsDetails">
                    <button type="button" class="newsDetailsButton" onclick="location.href='/registration'">
                        See details
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="col-12 text-center">
        <h2 class="menuHeadline">Find us</h2>
    </div>
</div>
<iframe class="map"
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d20582.930364647156!2d24.026948622711416!3d49.84496974426703!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x473add6daf2af431%3A0xd1b86b864117cb0c!2z0JvRjNCy0ZbQstGB0YzQutCwINGA0LDRgtGD0YjQsCwg0JvRjNCy0ZbQsiwg0JvRjNCy0ZbQstGB0YzQutCwINC-0LHQu9Cw0YHRgtGMLCA3OTAwMA!5e0!3m2!1suk!2sua!4v1547501573337"
        width="600" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>

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