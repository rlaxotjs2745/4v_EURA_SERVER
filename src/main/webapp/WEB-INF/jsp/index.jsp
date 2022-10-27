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
    <header class="header ">
        <h1><a href="index.jsp"><img src="resources/static/assets/image/h1_logo.png" alt=""></a></h1>
        <div class="user__box">
            <a href="#none" class="user__hover"><strong>${user.user_name}</strong>님</a>
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
                    <strong>${user.user_name}님</strong>
                </div>
                <div class="board__time">12:00</div>
            </div>
            <!--//-->
            <div class="main__schedule">
                <h2>다음일정</h2>
                <ul>
                    <!--<li><strong>다음 일정이 없습니다.</strong></li>-->
                    <li><a href="#none"><strong>연구참여 주간회의</strong> <em>9:00 - 11:00</em></a></li>
                    <li><a href="#none"><strong>공업논리 및 논술</strong> <em>12:00 - 13:00</em></a></li>
                    <li><a href="#none"><strong>전자공학 응용실험</strong> <em>14:00 - 16:00</em></a></li>
                </ul>
                <a href="#none" class="btn btn__calendar">
                    <img src="resources/static/assets/image/ic_arrow_light_circle_24.png" alt="">
                    캘린더 보기
                </a>
            </div>
            <!--//-->
            <div class="main__meetingroom">
                <h3><img src="resources/static/assets/image/ic_video.png" alt=""> 나의 미팅룸 <em>(9)</em>
                    <button id="room_new" class="btn btn__make"><img src="resources/static/assets/image/ic_plus.png" alt="">새 미팅룸 만들기</button>
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
                    <div class="box">
                        <div class="box__badge"><span class="type__live">LIVE</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">연구참여 주간회의</div>
                        <dl class="type__host">
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
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__ready">3:00뒤 시작</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">공업논리 및 논술</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd>박길수</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 08</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>12:00 - 13:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box is-before">
                        <div class="box__badge"><span class="type__private">비공개</span></div>
                        <div class="box__setup"><a href="#popup__notice" class="btn btn__setting js-modal-alert">공개하기</a></div>
                        <div class="box__title">인간공학개론</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd><img src="resources/static/assets/image/ic_host.png" alt="">강채연</dd>
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
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__ready">3:00뒤 시작</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">전자공학 응용실험</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd>박길수</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 08</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>12:00 - 13:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__dday">D-3</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">UX/UI 디자인</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd>서가희</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 12</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>10:00 - 12:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__dday">D-3</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">반도체 센서공학</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd>이유정</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 12</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>14:00 - 15:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__dday">D-4</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">전기설비공학</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd>홍현수</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 13</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>14:00 - 15:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box is-cancel">
                        <div class="box__badge"><span class="type__cancel">취소된 미팅</span></div>
                        <div class="box__setup"><a href="#none" class="btn btn__setting">재개설 하기</a></div>
                        <div class="box__title">공학기초설계</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd><img src="resources/static/assets/image/ic_host.png" alt="">강채연</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 13</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>14:00 - 15:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                </div>
                <div class="btn__group">
                    <a href="#none" class="btn btn__more">더 보기</a>
                </div>
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

                    <div class="box">
                        <div class="box__badge"><!--<span class="type__ready">3:00뒤 시작</span>--></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">공업교육론</div>
                        <dl class="type__host">
                            <dt>호스트 이름</dt>
                            <dd><img src="resources/static/assets/image/ic_host.png" alt="">강채연</dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 03</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>9:00 - 11:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><!--<span class="type__ready">3:00뒤 시작</span>--></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">전력전자공학</div>
                        <dl class="">
                            <dt>호스트 이름</dt>
                            <dd>김춘배</dd>
                        </dl>
                        <dl class="">
                            <dt>참여도 82%</dt>
                            <dd><div class="graph"><span class="graph__gage" style="width:82%;"></span></div></dd>
                        </dl>
                        <dl>
                            <dt>날짜</dt>
                            <dd>2022. 08. 04</dd>
                        </dl>
                        <dl>
                            <dt>시간</dt>
                            <dd>9:00 - 11:00</dd>
                        </dl>
                    </div>
                    <!--//-->
                    <div class="box">
                        <div class="box__badge"><span class="type__private">미참석</span></div>
                        <!--<div class="box__setup"><a href="#none" class="btn btn__setting">공개하기</a></div>-->
                        <div class="box__title">인간공학개론</div>
                        <dl class="">
                            <dt>호스트 이름</dt>
                            <dd>홍현수</dd>
                        </dl>
                        <dl class="">
                            <dt>참여도 0%</dt>
                            <dd><div class="graph"><span class="graph__gage" style="width:0%;"></span></div></dd>
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
                    <!--//-->
                </div>
            </div>
            <!--//-->


            <div id="popup__notice" class="pop__detail">
                <a href="#none" class="btn__close js-modal-close"><img src="resources/static/assets/image/ic_close_24.png" alt=""></a>
                <div class="popup__cnt">
                    <div class="pop__message">
                        <img src="resources/static/assets/image/ic_warning_80.png" alt="">
                        <strong>미팅룸을 공개하면 다시 비공개로 설정할 수 없습니다. <br>
                            미팅룸을 공개 하시겠습니까?</strong>
                        <span>미팅을 공개하면 초대한 참석자들에게 메일이 발송됩니다.</span>
                    </div>
                    <div class="btn__group">
                        <a href="#none" class="btn btn__able btn__s">예</a>
                        <a href="#none" class="btn btn__normal btn__s js-modal-close">아니오</a>
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
<script>
    $("#room_new").click(function(){

        var param = {
            "idx_user":'${user.idx_user}',
            "user_id":'${user.user_id}',
            "user_name":'${user.user_name}',
        };
        console.log(JSON.stringify(param));
        sendPost('room_new', param);

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
