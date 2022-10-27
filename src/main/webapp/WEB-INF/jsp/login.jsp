<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width,height=device-height,initial-scale=1,user-scalable=no">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <!-- SEO : S -->
  <meta name="description" content="" />
  <meta name="keywords" content="EURA" />
  <meta name="format-detection" content="telephone=no"/>
  <!-- SEO : E -->
  <!-- SNS 공유 : S -->
  <meta property="og:url" data-role="url" content="EURA"/>
  <meta property="og:title" data-role="title" content="EURA"/>
  <meta property="og:image" data-role="image" content="resources/static/assets/image/sns.png"/>
  <meta property="og:description" data-role="description" content="EURA"/>
  <!-- SNS 공유 : E -->
  <title>EURA</title>
  <link rel="shortcut icon" href="resources/static/assets/favicon/favicon.ico">
  <link rel="icon" href="resources/static/assets/favicon/favicon.ico">
  <link rel="stylesheet" type="text/css" href="resources/static/assets/css/jquery-ui.css">
  <link rel="stylesheet" type="text/css" href="resources/static/assets/css/style.css">
</head>
<body>
<div class="wrapper" id="wrapper">
    <section class="content no-head" id="content">
        <div class="login">
            <h2>EURA</h2>
            <div class="desc__h2">Emotion Understanding & Recognition Assistant</div>

            <div class="login__box">
                <form action="api_post_login" method="post">

                    <div class="input__group">
                        <input type="text" class="text" id="user_id" name="user_id" placeholder="아이디(이메일)" />
                    </div>
                    <div class="input__group">
                        <input type="password" class="text" id="user_pwd" name="user_pwd" placeholder="비밀번호" />
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" class="checkbox" id="auto-1" />
                        <label for="auto-1">자동 로그인</label>
                    </div>
                   <!-- alert message
                   <div class="input__group is-alert">
                        <div class="input__message">
                            아이디 또는 비밀번호가 잘못 입력되었어요. 올바른 정보를 입력해 주세요.
                        </div>
                    </div> -->

                    <div class="btn__group">
                        <button id="btn_login" type="submit" class="btn btn__able">로그인</button>
                        <a href="join" class="btn  btn__normal">회원가입하기</a>
                    </div>

                    <div class="anchor__box">
                        <a href="join_temporary" class="login__anchor">비밀번호를 잊으셨나요?</a>
                    </div>
                </form>
            </div>

        </div>
    </section>
</div>
<div class="footer">
  <div class="footer__inner">
    <h2><img src="resources/static/assets/image/logo_postech.png" alt="POSTECH"></h2>
    <div class="footer__quick">
      <a href="#none" class="anchor__quick">이용약관</a><a href="#none" class="anchor__quick">개인정보처리방침</a>
    </div>
    <div class="footer__info">
      <span class="address">(37673) 경상북도 포항시 남구 청암로 77(효자동 산31)</span>
      <span class="tel">TEL. 054-279-0114</span>
      <span class="email">E-Mail. webmaster@postech.ac.kr</span>
      <span class="copyright">Copyright © Pohang University of Science and Technology, Inc. All rights reserved.</span><!--1013 copyright 연도삭제 -->
    </div>
  </div>
</div>
<script src="resources/static/assets/js/lib/jquery-2.2.4.min.js" type="text/javascript"></script>
<script src="resources/static/assets/js/lib/jquery-ui.js" type="text/javascript"></script>
<script src="resources/static/assets/js/lib/swiper.min.js" type="text/javascript"></script>

<script src="resources/static/assets/js/ui.common.js" type="text/javascript"></script>



</body>
</html>
