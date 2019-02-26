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
    <link rel="stylesheet" href="resources/css/rateStars.css" type="text/css"/>

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
<div class="container" style="background-color: white;">
    <div class="row">
        <div class="col-lg-10 col-sm-12">
            <img src="/resources/img/tables.jpg">
        </div>
        <div class="col-lg-2 col-sm-12">
            <c:if test="${not empty myPlaces}">
                <h3 class="text-center">My tables:</h3>
            </c:if>
            <c:forEach var="myPlace" items="${myPlaces}">
                <a class="btnCafe " href="/place/${myPlace.id}/order">My table ${myPlace.number}</a>
            </c:forEach>
            <h3>Free tables</h3>
            <c:forEach var="place" items="${places}">
                <c:if test="${place.isFree() == 'true'}">
                    <div class="row">
                        <sec:authorize access="isAnonymous()">
                            <div class="col-12">
                                <a class="btnCafe " href="/login">${place.number}</a>
                            </div>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <div class="col-12">
                                <a class="btnCafe " href="/place/setUser/${place.id}">${place.number}</a>
                            </div>
                        </sec:authorize>
                    </div>
                    <br>
                </c:if>
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
        Lozinskyi</a>
    </div>
</footer>
</body>
</html>