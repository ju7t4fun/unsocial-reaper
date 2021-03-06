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

    <title>Sign Up</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap theme -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-theme.css" rel="stylesheet">
    <!--external css-->
    <!-- Plugin CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/animate.min.css" type="text/css">

    <script type='text/javascript'>
        var captchaContainer = null;
        var flag = null;
        var loadCaptcha = function () {
            captchaContainer = grecaptcha.render('captcha_container', {
                'sitekey': '6LexdAoTAAAAABI5O83KDE_GuQTrSwvlhn1nqwN2',
                'callback': function (response) {
                    if (response == 0) {
                        flag = false;
                    } else if (response != 0) {
                        flag = true;
                    }
                }
            });
        };
    </script>

    <%--for google capcha--%>
    <script src="https://www.google.com/recaptcha/api.js?hl=en&onload=loadCaptcha&render=explicit" async
            defer></script>

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/creative.css" type="text/css">
    <!-- font icon -->
    <link href="${pageContext.request.contextPath}/css/elegant-icons-style.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/font-awesome.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style-responsive.css" rel="stylesheet"/>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/language.jsp"></script>

    <link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css"/>
    <script src="${pageContext.request.contextPath}/js/toastr.js"></script>
    <script type="text/javascript">

        // При завантаженні сторінки
        setTimeout(function () {
            if (${toastr_notification!=null}) {
                var args = "${toastr_notification}".split("|");
                toastrNotification(args[0], args[1]);
            }
        }, 500);

    </script>

</head>

<body class="login-img3-body">
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top">
    <header class="header dark-bg" style="background: rgb(26, 39, 50)">
        <div class="main">
            <a href="${pageContext.request.contextPath}/" class="logo">Social <span class="lite">Spider</span></a>
        </div>
    </header>
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand page-scroll" href="/">Socail spider</a>
        </div>
    </div>
</nav>
<header class="back-header">
    <div class="container">

        <form class="login-form" action="${pageContext.request.contextPath}/register?action=register" method="post" id="register_form">
            <input type="hidden" name="vkId" value="${vkId}">
            <input type="hidden" name="photo_200" value="${photo_200}">

            <div class="login-wrap">
                <p class="login-img"><i class="icon_house_alt"></i></p>

                <div class="form-group">
                    <!--<span class="input-group-addon"><i class="icon_profile"></i></span>-->
                    <l:resource key="reg.name"><input type="text" class="form-control" id="name" name="name"
                                                      maxlength="45"
                                                      value="${name}" placeholder=""
                                                      pattern="^[a-zA-Z\u0400-\u04ff]+$"/></l:resource>
                </div>
                <div class="form-group">
                    <!--<span class="input-group-addon"><i class="icon_profile"></i></span>-->
                    <l:resource key="reg.surname"><input type="text" class="form-control" id="surname" name="surname"
                                                         maxlength="45" value="${surname}"  placeholder=""
                                                         /></l:resource>
                </div>
                <div class="form-group">
                    <!-- <span class="input-group-addon"><i class="icon_mail_alt"></i></span>-->
                    <l:resource key="login.email"><input type="email" class="form-control" id="email" name="email"
                                                         maxlength="255" value="${email}"  placeholder=""
                                                         style="border-color:#ffffff;"/></l:resource>
                </div>
                <div class="form-group">
                    <!-- <span class="input-group-addon"><i class="icon_key_alt"></i></span>-->
                    <l:resource key="login.password"><input type="password" class="form-control" id="password"
                                                            name="password" placeholder=""
                                                           /></l:resource>
                </div>

                <div class="form-group">
                    <div id="captcha_container"></div>
                </div>

                <i class="btn btn-primary btn-lg btn-block" onclick="submitRegistration();"
                   style="margin-bottom:20px;margin-right:10px;font-style: normal;"><l:resource key="reg.signup"/>
                </i>
                <script>
                    function submitRegistration() {
                        if (flag == null || flag == false) {
                            toastrNotification("error", "Wrong captcha!");
                        } else {
                            $('#register_form').submit();
                        }
                    }
                </script>
            </div>
        </form>

    </div>
</header>
<script>
    $(document).ready(function () {
        $.vegas('slideshow', {
            backgrounds: [
                {src: '${pageContext.request.contextPath}/img/header.jpg', fade: 2000, delay: 9000},
                {src: '${pageContext.request.contextPath}/img/2.jpg', fade: 2000, delay: 9000},
                {src: '${pageContext.request.contextPath}/img/4.jpg', fade: 2000, delay: 9000},
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>

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

