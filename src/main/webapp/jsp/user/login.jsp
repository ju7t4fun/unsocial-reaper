<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="l" uri="http://lab.epam.com/spider/locale" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Creative - Bootstrap 3 Responsive Admin Template">
    <meta name="author" content="GeeksLabs">
    <meta name="keyword" content="Creative, Dashboard, Admin, Template, Theme, Bootstrap, Responsive, Retina, Minimal">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/icons/favicon.png">

    <title>Login Page</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-theme.css" rel="stylesheet">
    <!--external css-->
    <!-- Plugin CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/creative.css" type="text/css">
    <!-- font icon -->
    <link href="${pageContext.request.contextPath}/css/elegant-icons-style.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/font-awesome.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style-responsive.css" rel="stylesheet"/>


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}js/language.js"></script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="${pageContext.request.contextPath}/js/html5shiv.js"></script>
    <script src="${pageContext.request.contextPath}/js/respond.min.js"></script>
    <![endif]-->
</head>

<body class="login-img3-body">
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top">

    <c:choose>
        <c:when test="${user.role == 'ADMIN'}">
            <jsp:forward page="admin_index.jsp"/>
        </c:when>
        <c:when test="${user.role == 'USER'}">
            <jsp:forward page="user_index.jsp"/>
        </c:when>
        <c:otherwise>
            <header class="header dark-bg" style="background: rgb(26, 39, 50)">
                <div class="main">
                    <a href="/" class="logo">Social <span class="lite">Spider</span></a>
                </div>

            </header>
        </c:otherwise>
    </c:choose>
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand page-scroll" href="/">Socail spider</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->

        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<header class="back-header">
    <div class="container">

        <form class="login-form" action="/login?action=signIn" method="post" id="register_form">
            <div class="login-wrap">
                <p class="login-img"><i class="icon_lock_alt"></i></p>
                ${loginMessage}
                <div class="input-group">
                    <span class="input-group-addon"><i class="icon_mail_alt"></i></span>
                    <l:resource key="login.email"><input type="email" value="${login}" name="email" id="email"
                                                         class="form-control" placeholder=""
                                                         style="border-color:#ffffff;"
                                                         required/></l:resource>
                </div>
                <div class="input-group">
                    <span class="input-group-addon"><i class="icon_key_alt"></i></span>
                    <l:resource key="login.password"><input type="password" name="password" id="password"
                                                            class="form-control" placeholder="" required/></l:resource>
                </div>
                <label class="checkbox">
                    <span class="pull-right"> <a href="/forgot_password"> <l:resource key="login.forgotpw"/> </a></span>
                </label>
                <button class="btn btn-primary btn-lg btn-block" type="submit"
                        style="margin-bottom:20px;margin-right:10px;"><l:resource key="login.signin"/>
                </button>

                <span class="pull-right"> <a href="/register"> <l:resource key="login.signup"/> </a> </span>
            <span> <l:resource key="login.signinwith"/> <a href="/login?action=vkAuth"><img
                    src="${pageContext.request.contextPath}/img/vk.png"></a></span>
            <span> <l:resource key="login.or"/> <a href="/login?action=fbAuth"><img
                    src="${pageContext.request.contextPath}/img/fb.png"></a> </span>


            </div>
        </form>
    </div>
</header>
<script>
    $(document).ready(function () {
        $.vegas('slideshow', {
            backgrounds: [
                {src: '../img/header.jpg', fade: 2000, delay: 9000},
                {src: '../img/2.jpg', fade: 2000, delay: 9000},
                {src: '../img/4.jpg', fade: 2000, delay: 9000},
            ]
        });
    });
</script>
<jsp:include page="../pagecontent/simple_footer.jsp"/>

<!-- javascripts -->
<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<!-- nice scroll -->
<script src="${pageContext.request.contextPath}/js/jquery.scrollTo.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.nicescroll.js" type="text/javascript"></script>
<!-- jquery validate js -->
<script type="${pageContext.request.contextPath}/text/javascript" src="js/jquery.validate.min.js"></script>

<!-- custom form validation script for this page-->
<script src="${pageContext.request.contextPath}/js/form-validation-script.js"></script>
<!--custome script for all page-->
<script src="${pageContext.request.contextPath}/js/scripts.js"></script>

<!-- EASING SCROLL SCRIPTS PLUGIN -->
<script src="${pageContext.request.contextPath}/js/jquery.vegas.min.js"></script>
<!-- VEGAS SLIDESHOW SCRIPTS -->
<script src="${pageContext.request.contextPath}/js/jquery.easing.min.js"></script>

</body>
</html>