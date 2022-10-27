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
  <meta property="og:image" data-role="image" content="/assets/image/sns.png"/>
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
    <header class="header ">
        <h1><a href="index.jsp"><img src="resources/static/assets/image/h1_logo.png" alt=""></a></h1>
    </header>
    <section class="content" id="content">
        <div class="join">
            <h2>회원가입</h2>
            <div class="desc__h2">아래의 정보를 기입해 주세요.</div>

            <div class="step__box">
                <div class="step is-active">
                    <span>1</span>
                    <em>필수 정보</em>
                </div>
                <div class="step ">
                    <span>2</span>
                    <em>선택 정보</em>
                </div>
                <div class="step ">
                    <span>3</span>
                    <em>약관 동의</em>
                </div>
            </div>
            
            <div class="join__box">
                <div class="input__group">
                    <label for="join_name">이름</label>
                    <input type="text" class="text" id="join_name" placeholder="이름을 입력하세요" />
                </div>
                <div class="input__group is-normal">
                    <label for="join_email">아이디(이메일)</label>
                    <input type="text" class="text" id="join_email" placeholder="이메일을 입력하세요" />
                    <div class="input__message">
                        입력하신 이메일로 회원가입 인증메일이 발송됩니다.
                    </div>
                </div>
                <div class="input__group is-success">
                    <label for="join_password">비밀번호</label>
                    <input type="password" class="text" id="join_password" value="비밀번호를 입력하세요" />

                </div>
                <div class="input__group is-alert">
                    <label for="join_password2">비밀번호 확인</label>
                    <input type="password" class="text" id="join_password2" value="비밀번호를 입력하세요" />
                    <div class="input__message">
                        비밀번호가 동일하지 않습니다.
                    </div>
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <a href="join.jsp" class="btn btn__normal">취소</a>
                    <a href="join_info.jsp" class="btn btn__able">다음</a>
                </div>
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
