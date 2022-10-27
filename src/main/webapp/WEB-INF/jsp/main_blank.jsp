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
            <div class="main__board">
                <div class="board__user">
                    <figure><img src="resources/static/assets/image/image_profile.png" alt=""></figure>
                    <span>안녕하세요</span>
                    <strong>강채연님</strong>
                </div>
                <div class="board__time">12:00</div>
            </div>
            <!--//-->
            <div class="main__schedule">
                <h2>다음일정</h2>
                <ul>
                    <li><strong>다음 일정이 없습니다.</strong></li>
                    <!--<li><a href="#none"><strong>연구참여 주간회의</strong> <em>9:00 - 11:00</em></a></li>
                    <li><a href="#none"><strong>공업논리 및 논술</strong> <em>12:00 - 13:00</em></a></li>
                    <li><a href="#none"><strong>전자공학 응용실험</strong> <em>14:00 - 16:00</em></a></li>-->
                </ul>
                <a href="#none" class="btn btn__calendar">
                    <img src="resources/static/assets/image/ic_arrow_light_circle_24.png" alt="">
                    캘린더 보기
                </a>
            </div>
            <!--//-->
            <div class="main__meetingroom">
                <h3><img src="resources/static/assets/image/ic_video.png" alt=""> 나의 미팅룸 <em>(9)</em>
                    <a href="#none" class="btn btn__make"><img src="resources/static/assets/image/ic_plus.png" alt="">새 미팅룸 만들기</a>
                    <div class="sorting">
                        <select name="" id="">
                            <option value="">최신순</option>
                            <option value="">미팅 시간 순</option>
                            <option value="">비공개 미팅 순</option>
                            <option value="">취소된 미팅 순</option>
                        </select>
                    </div>
                </h3>

                <div class="boxing">
                    <div class="msg__nodata">
                        <span>미팅 일정이 없습니다.</span>
                    </div>
                </div>
                <!--<div class="btn__group">
                    <a href="#none" class="btn btn__more">더 보기</a>
                </div>-->
            </div>
            <!--//-->
            <div class="main__history">
                <h3><img src="" alt=""><img src="resources/static/assets/image/ic_last.png" alt=""> 지난 미팅 <em>(2)</em>
                    <div class="sorting">
                        <select name="" id="">
                            <option value="">최신순</option>
                            <option value="">미팅 시간 순</option>
                            <option value="">비공개 미팅 순</option>
                            <option value="">취소된 미팅 순</option>
                        </select>
                    </div>
                </h3>

                <div class="boxing">
                    <div class="msg__nodata">
                        <span>지난 미팅 일정이 없습니다.</span>
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


</body>
</html>
