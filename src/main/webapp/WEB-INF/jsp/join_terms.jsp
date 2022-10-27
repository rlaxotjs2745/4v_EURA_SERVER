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
                <div class="step is-checked">
                    <span>1</span>
                    <em>필수 정보</em>
                </div>
                <div class="step is-checked">
                    <span>2</span>
                    <em>선택 정보</em>
                </div>
                <div class="step is-active">
                    <span>3</span>
                    <em>약관 동의</em>
                </div>
            </div>
            
            <div class="join__box">
                <div class="input__group terms__type">
                    <div class="terms__text">
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at consectetur deserunt distinctio fugiat id, incidunt ipsum iusto necessitatibus nostrum omnis pariatur quibusdam quo sit soluta temporibus tenetur ullam veniam?
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at consectetur deserunt distinctio fugiat id, incidunt ipsum iusto necessitatibus nostrum omnis pariatur quibusdam quo sit soluta temporibus tenetur ullam veniam?
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus alias dolor laboriosam perspiciatis sit vel. Ab, accusamus aut ducimus eveniet in ipsam nesciunt omnis quis sapiente, temporibus voluptatem voluptatibus? Consequatur.
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at consectetur deserunt distinctio fugiat id, incidunt ipsum iusto necessitatibus nostrum omnis pariatur quibusdam quo sit soluta temporibus tenetur ullam veniam?
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus alias dolor laboriosam perspiciatis sit vel. Ab, accusamus aut ducimus eveniet in ipsam nesciunt omnis quis sapiente, temporibus voluptatem voluptatibus? Consequatur.
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at consectetur deserunt distinctio fugiat id, incidunt ipsum iusto necessitatibus nostrum omnis pariatur quibusdam quo sit soluta temporibus tenetur ullam veniam?
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus alias dolor laboriosam perspiciatis sit vel. Ab, accusamus aut ducimus eveniet in ipsam nesciunt omnis quis sapiente, temporibus voluptatem voluptatibus? Consequatur.
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi at consectetur deserunt distinctio fugiat id, incidunt ipsum iusto necessitatibus nostrum omnis pariatur quibusdam quo sit soluta temporibus tenetur ullam veniam?
                        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus alias dolor laboriosam perspiciatis sit vel. Ab, accusamus aut ducimus eveniet in ipsam nesciunt omnis quis sapiente, temporibus voluptatem voluptatibus? Consequatur.
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" class="checkbox" id="cb-1" />
                        <label for="cb-1">위의 개인정보 처리방침에 동의합니다.</label>
                    </div>
                    <div class="checkbox">
                        <input type="checkbox" class="checkbox" id="cb-2" />
                        <label for="cb-2">위의 서비스 이용 약관에 동의합니다.</label>
                    </div>
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <button id="go_prev" class="btn btn__normal">이전</button>
                    <button id="go_next" class="btn btn__able">다음</button>
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
<script>
    $("#go_prev").click(function(){
        var param = {
            "user_id":'${user.user_id}',
            "user_pwd":'${user.user_pwd}',
            "user_name":'${user.user_name}',
        };
        console.log(JSON.stringify(param));
        sendPost('join_info', param);
    });

    $("#go_next").click(function(){

        var param = {
            "user_id":'${user.user_id}',
            "user_pwd":'${user.user_pwd}',
            "user_name":'${user.user_name}',
            "user_phone":'${user.user_phone}',
            "eq_type01":'${user.eq_type01}',
            "eq_type02":'${user.eq_type02}',
        };
        console.log(JSON.stringify(param));
        sendPost('join_photo', param);

    });

    function sendPost(url, params) {
        var form = document.createElement('form');
        form.setAttribute('method', 'post'); //POST 메서드 적용
        form.setAttribute('action', url);	// 데이터를 전송할 url
        document.charset = "utf-8";
        for ( var key in params) {	// key, value로 이루어진 객체 params
            var hiddenField = document.createElement('input');
            hiddenField.setAttribute('type', 'hidden'); //값 입력
            hiddenField.setAttribute('name', key);
            hiddenField.setAttribute('value', params[key]);
            form.appendChild(hiddenField);
        }
        document.body.appendChild(form);
        console.log(form);
        form.submit();	// 전송~
    }
</script>


</body>
</html>
