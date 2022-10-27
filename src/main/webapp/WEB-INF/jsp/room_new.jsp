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
        <div class="room">
            <h2>새 미팅룸 만들기</h2>
            
            <div class="room__box">
                <div class="input__group ">
                    <label for="mt_name">미팅 이름</label>
                    <input type="text" class="text" id="mt_name" placeholder="미팅 이름을 입력해주세요."  />
                    <!--//-->
                    <hr>
                    <!--1013 input 사이즈 추가-->
                    <label for="make_date"><img src="resources/static/assets/image/ic_calendar_24.png" alt=""></label>
                    <input id="make_date" type="date" class="text under-scope width-flexble" style="width:140px;">
                    <label for="make_time" class="input__time"><img src="resources/static/assets/image/ic_time_24.png" alt=""></label>
                    <input id="make_time" type="time" pattern="[0-9]{2}:[0-9]{2}" class="text under-scope width-flexble" style="width:130px;"> <span class="bar">-</span> <input id="make_time2" type="time" class="text under-scope width-flexble" style="width:130px;">
                    <!--//-->

                    <hr>
                    <div class="checkbox type__square">
                        <input type="checkbox" class="checkbox" id="cb-2" />
                        <label for="cb-2">되풀이 미팅</label>
                    </div>
                    <!--//-->
                    <!--되풀이 미팅-->
                </div>
                
                <div class="input__group">
                    <label for="mt_info">미팅 정보</label>
                    <textarea name="" id="mt_info" cols="10" rows="3" placeholder="미팅정보를 입력해주세요."></textarea>
                </div>

                <div class="input__group">
                    <label for="">참석자 명단</label>
                    <div class="list__count">총 00명</div>
                    <div class="list__guest">
                        <!--<ul>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1510227272981-87123e259b17?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=3759e09a5b9fbe53088b23c615b6312e" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1510227272981-87123e259b17?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=3759e09a5b9fbe53088b23c615b6312e" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1510227272981-87123e259b17?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=3759e09a5b9fbe53088b23c615b6312e" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </a></li>
                        </ul>-->
                    </div>
                </div>

                <div class="input__group">
                    <label for="make_team">참석자 추가</label>
                    <div class="list__count"><a href="#none" class="btn btn__download">엑셀 양식 다운로드</a></div>
                    <div class="input__inline">
                        <input id="make_team" type="text" class="text" placeholder="이메일 또는 이름을 입력해 참석자를 추가하세요.">
                        <a href="#popup__team" class="btn btn__team js-modal-alert"><img src="resources/static/assets/image/ic_participant_14.png" alt="">단체추가하기</a>
                    </div>
                    <div class="flow__team">
                        <ul>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1510227272981-87123e259b17?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=3759e09a5b9fbe53088b23c615b6312e" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-1" name="user" />
                                    <label for="team-1"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-2" name="user" />
                                    <label for="team-2"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=046c29138c1335ef8edee7daf521ba50" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-3" name="user" />
                                    <label for="team-3"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-4" name="user" />
                                    <label for="team-4"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-5" name="user" />
                                    <label for="team-5"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=046c29138c1335ef8edee7daf521ba50" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-6" name="user" />
                                    <label for="team-6"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=046c29138c1335ef8edee7daf521ba50" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-7" name="user" />
                                    <label for="team-7"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-8" name="user" />
                                    <label for="team-8"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-9" name="user" />
                                    <label for="team-9"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-10" name="user" />
                                    <label for="team-10"></label>
                                </div>
                            </a></li>
                            <li><a href="#none">
                                <figure><img src="https://images.unsplash.com/photo-1487735829822-4aa5382f8ed4?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&s=fd78248154392ee88a2a8da254058508" alt=""></figure>
                                <span class="team__user">권민수
                                 <em>rnjsals12@gmail.com</em></span>
                                <div class="checkbox type__square">
                                    <input type="checkbox" class="checkbox" id="team-11" name="user" />
                                    <label for="team-11"></label>
                                </div>
                            </a></li>
                        </ul>
                    </div>

                </div>

                <div class="input__group">
                    <label for="">첨부파일 <a href="#none" class="btn btn__download"><img src="resources/static/assets/image/ic_attachment_14.png" alt="">파일 업로드</a></label>
                    <div class="list__upload">
                       <!-- <ul>
                            <li>
                                <a href="#none">
                                <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">1주차_인간공학의 개요_ppt.pdf</span><em class="file__size">230KB</em>
                                </a>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </li>
                            <li>
                                <a href="#none">
                                    <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">2주차_인간공학을 위한 인간이해_ppt.pdf
</span><em class="file__size">680KB</em>
                                </a>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </li>
                            <li>
                                <a href="#none">
                                    <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">3주차_인간의 감각과 그 구조_ppt.pdf</span><em class="file__size">558KB</em>
                                </a>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </li>
                            <li>
                                <a href="#none">
                                    <img src="resources/static/assets/image/ic_file_14.png" alt=""><span class="file__name">4주차_인간의 형태와 운동기능_ppt.pdf</span><em class="file__size">680KB</em>
                                </a>
                                <a href="#none" class="btn btn__delete"><img src="resources/static/assets/image/ic_cancle-circle_18.png" alt="삭제"></a>
                            </li>
                        </ul>-->
                    </div>
                </div>
            </div>

            <div class="btn__box">
                <div class="btn__group">
                    <a href="#none" class="btn btn__normal">최소</a>
                    <button id="save_room" class="btn btn__able">저장</button>
                </div>
            </div>

            <div id="popup__team" class="pop__detail ">
                <!--1013  <a href="#none" class="btn__close js-modal-close"><img src="resources/static/assets/image/ic_close_24.png" alt=""></a>-->
                <div class="popup__cnt">
                    <div class="pop__message">
                        <h3>참석자 단체 등록하기</h3>
                        <div class="pop__body">
                            <div class="upload__box ">
                                <input class="upload-name" value="이메일이 입력된 엑셀파일을 첨부해주세요." disabled="disabled">
                                <label for="ex_filename"><img src="resources/static/assets/image/ic_attachment_24.png" alt=""></label>
                                <input type="file" id="ex_filename" class="upload-hidden">
                            </div>
                            <!--<div class="upload__count">총 52명</div>-->
                            <div class="upload__list">
                                <!--<span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>wlsdksms11@postech.com</span>
                                <span>rlsgus@postech.com</span>
                                <span>rlsgus@postech.com</span>-->
                            </div>
                        </div>
                    </div>
                    <div class="btn__group align__right"><!--1013-->
                        <a href="#none" class="btn btn__normal btn__s">취소</a>
                        <a href="#none" class="btn btn__able btn__s js-modal-close">완료</a>
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
    $(document).ready(function(){
        var fileTarget = $('.upload__box .upload-hidden');

        fileTarget.on('change', function(){
            if(window.FileReader){
                var filename = $(this)[0].files[0].name;
            } else {
                var filename = $(this).val().split('/').pop().split('\\').pop();
            }

            $(this).siblings('.upload-name').val(filename);
        });
    });

    $("#save_room").click(function(){
        var mt_name = $("#mt_name").val();
        var mt_start_dt = $("#make_date").val();
        var mt_info = $("#mt_info").val();

        var param = {
            "mt_name":mt_name,
            "mt_start_dt":mt_start_dt,
            "mt_info":mt_info,
        };

        console.log(JSON.stringify(param));

        $.ajax({
            type: 'post',
            url :'save_room', //데이터를 주고받을 파일 주소 입력
            data: JSON.stringify(param),//보내는 데이터
            contentType:"application/json; charset=utf-8;",//보내는 데이터 타입
            dataType:'json',//받는 데이터 타입
            success: function(result){
                //작업이 성공적으로 발생했을 경우
                if(result.result_code=="SUCCESS"){

                    location.href="/";

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

</script>
</body>
</html>
