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
                <div class="step is-active">
                    <span>2</span>
                    <em>선택 정보</em>
                </div>
                <div class="step ">
                    <span>3</span>
                    <em>약관 동의</em>
                </div>
            </div>
            
            <div class="join__box">
                <div class="input__group is-normal">
                    <label for="user_phone">연락처</label>
                    <input type="text" class="text" id="user_phone" placeholder="연락처를 입력하세요" />
                    <div class="input__message">
                        서비스오류 발생시 연락처로 알림이 수신됩니다.
                    </div>
                </div>
                <div class="input__group option__type">
                    <label>평소 외향적인 성격인가요?</label>
                    <div class="radio__inline">
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-1" value="0">
                            <label for="r-1">매우 그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-2" value="1">
                            <label for="r-2">그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-3" value="2" checked>
                            <label for="r-3">보통이다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-4" value="3">
                            <label for="r-4">그렇다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-5" value="4">
                            <label for="r-5">매우 그렇다</label>
                        </div>
                    </div>
                </div>
                <div class="input__group option__type is-normal">
                    <label>평소 친화력이 있는 성격인가요?</label>
                    <div class="radio__inline">
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-1" value="0">
                            <label for="r2-1">매우 그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-2" value="1">
                            <label for="r2-2">그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-3" value="2" checked>
                            <label for="r2-3">보통이다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-4" value="3">
                            <label for="r2-4">그렇다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-5" value="4">
                            <label for="r2-5">매우 그렇다</label>
                        </div>
                    </div>
                    <div class="input__message">
                        사용자 감성 반응의 맥락 이해에 사용되는 정보입니다.
                    </div>
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <a href="login_page" class="btn btn__normal">이전</a>
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

    var b_phone=false;
    $('#user_phone').bind('input', function() {
        if($(this).val()!=null) b_phone=true;
        else b_phone=false;
        btn_en_check();

    });


    function btn_en_check(){
        if(b_phone ){
            if($('#go_next').hasClass("btn__disable")) {
                $('#go_next').removeClass("btn__disable");
                $('#go_next').addClass("btn__able");
            }
        }
        else{
            if($('#go_next').hasClass("btn__able")){
                $('#go_next').removeClass("btn__able");
                $('#go_next').addClass("btn__disable");
            }
        }
    }

    $("#go_next").click(function(){
        var user_phone = $("#user_phone").val();
        var eq_type01 = $('input[name="option-radio"]:checked').val();
        var eq_type02 = $('input[name="option-radio2"]:checked').val();

        var param = {
            "user_id":'${user.user_id}',
            "user_pwd":'${user.user_pwd}',
            "user_name":'${user.user_name}',
            "user_phone":user_phone,
            "eq_type01":eq_type01,
            "eq_type02":eq_type02,
        };
        console.log(JSON.stringify(param));
        sendPost('join_terms', param);

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
