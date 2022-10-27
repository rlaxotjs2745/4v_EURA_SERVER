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
        <div class="my">
            <h2>내 프로필</h2>
            
            <div class="my__box">
                <div class="input__group">
                    <label for="join_name">프로필사진</label>
                    <div class="input__group upload__type">
                        <input type="file" class="upload__btn"  onchange="readURL(this);">
                        <div class="upload__image"><img  id="preview" src="resources/static/assets/image/image_profile.png" alt=""></div>
                    </div>
                </div>
                <div class="input__group ">
                    <label for="join_email">아이디(이메일)</label>
                    <input type="text" class="text" id="join_email" value="rkdcodus12@postech.com" disabled />
                </div>
                <div class="input__group">
                    <label for="join_name">이름</label>
                    <input type="text" class="text" id="join_name" value="강채연" />
                    <div class="modify__box">
                        <!--<a href="#none" class="btn btn-modify">편집하기</a>-->
                        <a href="#none" class="btn btn__able btn__s">변경하기</a>
                        <a href="#none" class="btn btn__normal btn__s">취소</a>
                    </div>

                </div>
                <div class="input__group">
                    <label for="join_password">비밀번호</label>
                    <input type="password" class="text" id="join_password" value="비밀번호를 입력하세요" />
                    <div class="modify__box">
                        <a href="profile_case.jsp" class="btn btn-modify">편집하기</a>
                        <!--<a href="#none" class="btn btn__able btn__s">변경하기</a>
                        <a href="#none" class="btn btn__normal btn__s">취소</a>-->
                    </div>
                </div>



               <!-- <div class="input__group">
                    <label for="join_password_before">이전 비밀번호</label>
                    <input type="password" class="text" id="join_password_before" placeholder="현재 사용하고 있는 비밀번호를 입력해 주세요" />
                </div>
                <div class="input__group">
                    <label for="join_password_new">새로운 비밀번호</label>
                    <input type="password" class="text" id="join_password_new" placeholder="새로운 비밀번호를 입력해 주세요" />
                </div>
                <div class="input__group">
                    <label for="join_password_new2">새로운 비밀번호 확인</label>
                    <input type="password" class="text" id="join_password_new2" placeholder="새롭게 입력한 비밀번호를 다시 입력해 주세요" />
                    <div class="modify__box">
                        <a href="#none" class="btn btn-modify">편집하기</a>
                        <a href="#none" class="btn btn__able btn__s">변경하기</a>
                        <a href="#none" class="btn btn__normal btn__s">취소</a>
                    </div>
                </div>-->
                <div class="input__group">
                    <label for="join_tel">연락처</label>
                    <input type="text" class="text" id="join_tel" value="010-0000-0000" />
                    <div class="modify__box">
                        <a href="#none" class="btn btn-modify">편집하기</a>
                        <!--<a href="#none" class="btn btn__able btn__s">변경하기</a>
                        <a href="#none" class="btn btn__normal btn__s">취소</a>-->
                    </div>
                </div>
            </div>


            <h2>성향 선택 문항</h2>

            <div class="my__box">
                <div class="input__group option__type">
                    <label for="join_tel">평소 외향적인 성격인가요?</label>
                    <div class="modify__box">
                        <a href="#none" class="btn btn-modify">편집하기</a>
                    </div>
                    <div class="radio__inline">
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-1" onclick="return(false);">
                            <label for="r-1">매우 그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-2" onclick="return(false);">
                            <label for="r-2">그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-3" checked>
                            <label for="r-3">보통이다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-4" onclick="return(false);">
                            <label for="r-4">그렇다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio" id="r-5" onclick="return(false);">
                            <label for="r-5">매우 그렇다</label>
                        </div>
                    </div>
                </div>
                <div class="input__group option__type is-normal">
                    <label for="join_tel">평소 친화력이 있는 성격인가요?</label>
                    <div class="radio__inline">
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-1" onclick="return(false);">
                            <label for="r2-1">매우 그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-2" onclick="return(false);">
                            <label for="r2-2">그렇지 않다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-3" checked>
                            <label for="r2-3">보통이다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-4" onclick="return(false);">
                            <label for="r2-4">그렇다</label>
                        </div>
                        <div class="radio">
                            <input type="radio" name="option-radio2" id="r2-5" onclick="return(false);">
                            <label for="r2-5">매우 그렇다</label>
                        </div>
                    </div>
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
</body>
</html>
