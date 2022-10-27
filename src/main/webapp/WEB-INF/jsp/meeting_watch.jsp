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
        <div class="user__box">
            <a href="#none" class="user__hover"><strong>강채연</strong>님</a>
            <div class="user__anchor">
                <ul>
                    <li><a href="#none">내 프로필</a></li>
                    <li><a href="#none" class="log-out">로그아웃</a></li>
                </ul>
            </div>
        </div>
    </header>
    <section class="content" id="content">
        <div class="page">
            <div class="meeting__dash">
                <h3>미팅룸 정보</h3>
                <div class="casing">
                    <div class="case-1">
                        <div class="case__title">인간 공학 개론
                        </div>
                        <dl class="type__host">
                            <dt>강의내용</dt>
                            <dd>이번에는 인간공학개론 제 4절 인간의 형태와 운동기능에 대해 알아봅시다. 자세한 사항은 아래 계획서를 참고하시길 바랍니다.</dd>
                        </dl>
                        <dl class="">
                            <dt>호스트 이름</dt>
                            <dd>박성하</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 08</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>9:00 - 11:00</dd>
                        </dl>
                    </div>
                    <div class="case-2">
                        <div class="case__message is-ready">미팅 시작 전</div>
                    </div>
                    <div class="case-3">
                        <div class="list__upload">
                            <!--1013-->
                            <ul>
                                <li>
                                    <a href="#none">
                                        <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">1주차_인간공학의 개요_ppt.pdf</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="#none">
                                        <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">2주차_인간공학을 위한 인간이해_ppt.pdf</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="#none">
                                        <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">3주차_인간의 감각과 그 구조_ppt.pdf</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="#none">
                                        <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">4주차_인간의 형태와 운동기능_ppt.pdf</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!--//-->
            <div class="meeting__user">
                <h3>미팅 참석자 <div class="user__count"><img src="resources/static/assets/image/ic_participant_24.png" alt="">32</div></h3>
                <div class="usering">
                    <div class="swiper userSwiper">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide">
                                <!--paging 1-->

                                <div class="user is-disabled">
                                    <ul>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div class="swiper-slide">
                                <!--paging 2-->

                                <div class="user is-disabled">
                                    <ul>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                        <li>
                                            <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                                            <span class="team__user">제갈민정<em>postechkim@postech.com</em></span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="swiper-button-next"></div>
                        <div class="swiper-button-prev"></div>
                    </div>
                    <div class="btn__group">
                        <a href="#none" class="btn btn__able btn__xl">참여하기</a>
                    </div>
                </div>
            </div>
            <!--//-->

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

<!-- Initialize Swiper -->
<script>
    var swiper = new Swiper(".userSwiper", {
        spaceBetween: 100,
        slidesPerView: 1,
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });
</script>

</body>
</html>
