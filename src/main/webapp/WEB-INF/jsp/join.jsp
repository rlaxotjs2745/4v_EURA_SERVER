<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
                    <label for="user_name">이름</label>
                    <input type="text" class="text" id="user_name" placeholder="이름을 입력하세요" />
                </div>
                <div class="input__group ">
                    <label for="user_id">아이디(이메일)</label>
                    <input type="text" class="text" id="user_id" placeholder="이메일을 입력하세요" />
                </div>
                <div class="input__group">
                    <label for="user_pwd">비밀번호</label>
                    <input type="password" class="text" id="user_pwd" placeholder="비밀번호를 입력하세요" />
                </div>
                <div class="input__group">
                    <label for="user_pwd_confirm">비밀번호 확인</label>
                    <input type="password" class="text" id="user_pwd_confirm" placeholder="비밀번호를 입력하세요" />
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <a href="login_page" class="btn btn__normal">취소</a>
                    <button id="go_next" class="btn btn__disable">확인</button>
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

    var b_id=false,b_pwd=false,b_pwd_confirm=false,b_name=false;
    $('#user_id').bind('input', function() {
        if($(this).val()!=null) b_id=true;
        else b_id=false;
        btn_en_check();

    });
    $('#user_pwd').bind('input', function() {
        if($(this).val()!=null) b_pwd=true;
        else b_pwd=false;
        btn_en_check();

    });
    $('#user_pwd_confirm').bind('input', function() {
        if($(this).val()!=null) b_pwd_confirm=true;
        else b_pwd_confirm=false;
        btn_en_check();
    });
    $('#user_name').bind('input', function() {
        if($(this).val()!=null) b_name=true;
        else b_name=false;
        btn_en_check();
    });

    function btn_en_check(){
        if(b_id && b_pwd && b_pwd_confirm && b_name){
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

        var user_id = $("#user_id").val();
        var user_pwd = $("#user_pwd").val();
        var user_pwd_confirm = $("#user_pwd_confirm").val();
        var user_name = $("#user_name").val();

        if(!CheckEmail(user_id)){
            alert("아이디는 이메일형식이어야 합니다");
            return;
        }
        if(user_pwd<10){
            alert("비밀번호는 10자 이상이어야합니다");
            return;
        }
        if(user_pwd!=user_pwd_confirm){
            alert("비밀번호가 동일하지 않습니다");
            return;
        }
        var param = {
            "user_id":user_id,
            "user_pwd":user_pwd,
            "user_name":user_name,

        };
        console.log(JSON.stringify(param));

        $.ajax({
            type: 'post',
            url :'join_default', //데이터를 주고받을 파일 주소 입력
            data: JSON.stringify(param),//보내는 데이터
            contentType:"application/json; charset=utf-8;",//보내는 데이터 타입
            dataType:'json',//받는 데이터 타입
            success: function(result){
                //작업이 성공적으로 발생했을 경우
                if(result.result_code=="SUCCESS"){

                    sendPost('join_info', param);

                }
                else {
                    alert(result.result_str);
                }
            },
            error:function(){
                //에러가 났을 경우 실행시킬 코드
            }
        });
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

    function CheckEmail(str)
    {
        var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
        if(!reg_email.test(str)) {
            return false;
        }
        else {
            return true;
        }
    }

</script>

</body>
</html>
