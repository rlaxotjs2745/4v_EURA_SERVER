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
        <div class="join profile">
            <h2>프로필 사진 등록</h2>
            <div class="desc__h2">아래를 클릭하여 마음에 드는 사진을 업로드 하세요.</div>

            <div class="join__box">
                <div class="input__group upload__type">
                    <input type="file" class="upload__btn"  onchange="readURL(this);">
                    <div class="upload__image"><img  id="preview" src="resources/static/assets/image/image_upload-pic.png" alt=""></div>
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <button id="go_next" class="btn btn__normal">지금은 넘어가기</button>
                    <!--<a href="join_upload" class="btn btn__able">완료</a>-->
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
    const realUpload = document.querySelector('.upload__btn');
    const upload = document.querySelector('.upload__image');

    upload.addEventListener('click', () => realUpload.click());

    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('preview').src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        } else {
            document.getElementById('preview').src = "";
        }
    }
</script>

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
            "eq_type01":${user.eq_type01},
            "eq_type02":${user.eq_type02}
        };
        console.log(JSON.stringify(param));
        sendPost('join_mail', param);

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
