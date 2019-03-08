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

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>

    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
          crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>

    <link href="/resources/css/index.css" rel="stylesheet">
    <title>ReserveTable</title>
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
    <a class="headerButtonActive" href="/place">
        Tables
    </a>
</header>
<div class="placeRow">
    <div class="placesList">
        <c:if test="${not empty myPlaces}">
            <h3>Your tables:</h3>
        </c:if>
        <c:forEach var="myPlace" items="${myPlaces}">
            <a class="placeButton" href="/place/${myPlace.id}/order">Your table ${myPlace.name}</a>
        </c:forEach>
        <h3>Free tables</h3>
        <div class="freePlacesList">
            <div class="freePlaceItem">
                <h3>For 2 person</h3>
                <c:forEach var="place" items="${places}">
                    <c:if test="${place.free && place.countOfPeople == 2}">
                        <button type="button" class="placeButton" data-toggle="modal"
                                data-target="#exampleModalCenter${place.name}">
                                ${place.name}
                        </button>
                        <!-- Modal -->
                        <div class="modal" id="exampleModalCenter${place.name}" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="placeModal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Table #${place.name} is free for now</h5>
                                    </div>
                                    <div class="modal-body">
                                        You can order this table
                                    </div>
                                    <sec:authorize access="isAnonymous()">
                                        <a class="placeButton" href="/login">Order</a>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <a class="placeButton" href="/order">Order</a>
                                    </sec:authorize>
                                </div>
                            </div>
                        </div>
                        <br>
                    </c:if>
                </c:forEach>
            </div>
            <div class="freePlaceItem">
                <h3>For 4 person</h3>
                <c:forEach var="place" items="${places}">
                    <c:if test="${place.free && place.countOfPeople == 4}">
                        <button type="button" class="placeButton" data-toggle="modal"
                                data-target="#exampleModalCenter${place.name}">
                                ${place.name}
                        </button>
                        <!-- Modal -->
                        <div class="modal fade" id="exampleModalCenter${place.name}" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="placeModal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Table #${place.name} is free for now</h5>
                                    </div>
                                    <div class="modal-body">
                                        You can order this table
                                    </div>
                                    <sec:authorize access="isAnonymous()">
                                        <a class="placeButton" href="/login">Order</a>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <a class="placeButton" href="/order">Order</a>
                                    </sec:authorize>
                                </div>
                            </div>
                        </div>
                        <br>
                    </c:if>
                </c:forEach>
            </div>
            <div class="freePlaceItem">
                <h3>For 6 person</h3>
                <c:forEach var="place" items="${places}">
                    <c:if test="${place.free && place.countOfPeople == 6}">
                        <button type="button" class="placeButton" data-toggle="modal"
                                data-target="#exampleModalCenter${place.name}">
                                ${place.name}
                        </button>
                        <!-- Modal -->
                        <div class="modal fade" id="exampleModalCenter${place.name}" tabindex="-1" role="dialog"
                             aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered" role="document">
                                <div class="placeModal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Table #${place.name} is free for now</h5>
                                    </div>
                                    <div class="modal-body">
                                        You can order this table
                                    </div>
                                    <sec:authorize access="isAnonymous()">
                                        <a class="placeButton" href="/login">Order</a>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <a class="placeButton" href="/order">Order</a>
                                    </sec:authorize>
                                </div>
                            </div>
                        </div>
                        <br>
                    </c:if>
                </c:forEach>
            </div>
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