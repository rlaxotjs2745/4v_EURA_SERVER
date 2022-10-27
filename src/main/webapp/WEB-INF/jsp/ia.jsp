<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<!--[if IE 7]><html lang="ko" class="ie ie7"><![endif]-->
<!--[if IE 8]><html lang="ko" class="ie ie8"><![endif]-->
<!--[if IE 9]><html lang="ko" class="ie ie9"><![endif]-->
<!--[if !IE]><!--><html lang="ko"><!--<![endif]-->
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>프로젝트</title>
  <link rel="stylesheet" href="http://fonts.googleapis.com/earlyaccess/notosanskr.css" />
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro" rel="stylesheet">
  <style type="text/css">
    html, body, body div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, abbr, address, cite, code, del, dfn, em, img, ins, kbd, q, samp, small, strong, sub, sup, var, b, i, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, figure, footer, header, menu, nav, section, time, mark, audio, video, details, summary { margin: 0; padding: 0; border: 0; font-size: 100%; font-weight: normal; vertical-align: baseline; background: transparent; }
    html { box-sizing: border-box; } body { line-height: 1.5; font-size: 14px; font-family: 'Malgun Gothic', '맑은 고딕','돋움', dotum, Helvetica, AppleSDGothicNeo, sans-serif; word-wrap: break-word; -webkit-text-size-adjust: none; } article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section { display: block; } main, article, aside, figure, footer, header, nav, section, details, summary {display: block;} *, *:before, *:after { box-sizing: inherit; } html {overflow-y: scroll;} ul {list-style: none;} ol, ul { list-style: none; } blockquote, q {quotes: none;} blockquote:before, blockquote:after, q:before, q:after {content: ''; content: none;} a {margin: 0; padding: 0; font-size: 100%; vertical-align: baseline; background: transparent; text-decoration: none; } del {text-decoration: line-through;} abbr[title], dfn[title] {border-bottom: 1px dotted #000; cursor: help;} table {border-collapse: separate; border-spacing: 0;} th {font-weight: bold; vertical-align: bottom;} td {font-weight: normal; vertical-align: top;} hr {display: block; height: 1px; border: 0; border-top: 1px solid #ccc; margin: 1em 0; padding: 0;} input, select {vertical-align: middle;} pre { white-space: pre; white-space: pre-wrap; white-space: pre-line; word-wrap: break-word; } img, object, embed {max-width: 100%;} input[type="radio"] {vertical-align: text-bottom;} input[type="checkbox"] {vertical-align: bottom;} .ie7 input[type="checkbox"] {vertical-align: baseline;} .ie6 input {vertical-align: text-bottom;} select, input, textarea {font: 99% sans-serif;} table {font-size: inherit; font: 100%; border-collapse: collapse; border-spacing: 0; } small {font-size: 85%;} strong {font-weight: bold;} td, td img {vertical-align: top;} img { vertical-align: top; border: none; } pre, code, kbd, samp {font-family: monospace, sans-serif;} .clickable, label, input[type=button], input[type=submit], input[type=file], button {cursor: pointer;} button, input, select, textarea {margin: 0;} button, input[type=button] {width: auto; overflow: visible;} .ie7 img {-ms-interpolation-mode: bicubic;} .clearfix:after { content: " "; display: block; clear: both; }
    body { line-height: 1.4; font-size: 12px; font-family: 'Noto Sans KR', sans-serif; word-wrap: break-word; -webkit-text-size-adjust: none; -webkit-font-smoothing: antialiased; letter-spacing: 0.5px; } #viewport { position: relative; width: 100%; height: 100%; max-width: 1920px; } .sr-only { position: absolute; left: -9999px; top: -99999px; } header { position: fixed; left: 0; top: 0; width: 100%; height: 50px; border-bottom: 1px solid #e6e6e6; box-sizing: border-box; background-color: #fff; z-index: 999; } header h1 { position: relative;  padding:14px 10px; float:left; font-size: 20px; }  header .progress{position:relative; float:left; padding:20px 0; color:#999;} header .progress strong{color:#333;} header h2{position: absolute; right: 124px; top: 14px; font-size: 14px;} header nav { position: absolute; right: 0; top: 0; height: 50px; } header nav ul { font-size: 0; letter-spacing: 0; } header nav ul li { display: inline-block; height: 50px; border-left: 1px solid #e6e6e6; } header nav ul li a { display: block; font-family: 'Source Sans Pro', sans-serif; font-size: 14px; padding: 15px 16px 0; height: 50px; box-sizing: border-box; color: #333; transition: all 0.2s ease-in; } header nav ul li a:hover { background: #d6b161; color: #fff; } #contents {  padding: 70px 0; } #ia { padding: 0 10px 40px; } #comment { padding: 0 20px; min-height: 500px; } .accordion .subject { margin-bottom: 10px; text-transform: uppercase; font-size: 12px; font-weight: 400; } .accordion .notice { margin-bottom: 10px; padding: 10px 20px 20px; color: #777; font-size: 14px; line-height: 1.8; } .accordion .subject a, .accordion .subject a:after { -webkit-transition: all .27s cubic-bezier(0, 0, .58, 1); transition: all .27s cubic-bezier(0, 0, .58, 1); } .accordion { position: relative; } .accordion:before { position: absolute; z-index: 0; top: 25px; bottom: 10px; left: 15px; content: ""; border-left: 1px dashed #cecece; } .accordion .subject a { position: relative; display: block; padding: 14px 20px 14px 40px; text-decoration: none; color: #222; border: none; } .accordion .subject a:after { position: absolute; top: 50%; left: 0; width: 30px; height: 30px; margin-top: -15px; content: "+"; text-align: center; text-transform: none; color: #fff; background: #222; font-size: 20px; font-weight: bold; font-style: normal; font-variant: normal; line-height: 30px; vertical-align: middle; -webkit-font-smoothing: antialiased; } .accordion .list.selected .subject > a:after, .accordion .list .subject > a:hover:after { content: "-"; color: #fff; background: #d6b161; } .accordion .subject > a time { display: inline-block; margin-left: 8px; color: #888; } .accordion .notice { margin-bottom: 10px; padding: 10px 20px 20px 40px; color: #777; font-size: 14px; line-height: 1.8; } .accordion .notice { display: none; } .accordion .notice .btnDown { color: #d6b161; }
    table { table-layout: fixed; border-collapse: collapse; width: 100%; }
    table tr, table th, table td { border: 1px solid #eee; }
    table th { padding: 10px 0; background-color: #333; font-weight: 400; color: #fff; }
    table td { padding: 5px 0 5px 10px; text-align: left; vertical-align: middle; }
    table tr.wait td { background: #fafafa; }
    table td a { color: #1091d0; text-decoration: none; }
    table td span.tip { color: #ff5843; }
    table td:nth-child(2),
    table td:nth-child(3),
    table td:nth-child(4),
    table td:nth-child(5),
    table td:nth-child(6),
    table td:nth-child(7),
    table td:nth-child(10) {
      text-align: left;
      padding: 0 10px;
    }

    .checkbox { position: relative; width: 18px; height: 18px; margin: 0 auto; }
    .checkbox:before { display: block;  content: "N"; position: absolute; left: 0; top: 0; width: 18px; height: 18px; background-color: #ddd;  color: #fff; text-align: center; }
    .checkbox:after { display: none; content: "Y"; position: absolute; left: 0; top: 0; width: 18px; height: 18px; background-color: #ffa50b; color: #fff; text-align: center; }
    .checkbox.selected:after { display: block; }
    .checkbox.selected:before { display: none; }

  </style>
</head>
<body id="start">
<div id="viewport">
  <header>
    <div class="fixed">
      <h1>프로젝트</h1>
      <div class="progress">
        <p>완료 : <span class="curr_n"></span>page / Total : <span class="total_n"></span> page / <strong>진행율 : <span class="per"></span></strong>
        </p>
      </div>
      <nav>
        <ul>
          <li><a href="#ia">IA</a></li>
        </ul>
      </nav>
    </div>
  </header>
  <!-- contents -->
  <div id="contents">
    <section id="ia">
      <article>
        <table class="codinglist">
          <colgroup>
            <col style="width:50px;"><!--NUM-->
            <col style="width:130px;"><!--D1-->
            <col style="width:170px;"><!--D2-->
            <col style="width:160px;"><!--D3-->
            <col style="width:180px;"><!--NAME-->
            <col style="width:50px;"><!--TASK-->
            <col style="width:100px;"><!--DATE-->
            <col><!--MEMO-->
          </colgroup>
          <thead>
          <tr>
            <th></th>
            <th>Depth1</th>
            <th>Depth2</th>
            <th>Depth3</th>
            <th>Name</th>
            <th>Task</th>
            <th>Date</th>
            <th>Memo</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td></td>
            <td>회원가입</td>
            <td>필수정보</td>
            <td></td>
            <td>
              <a href="join.jsp" target="_blank" class="link">join</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0913</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>(상태값)</td>
            <td>
              <a href="join_case.jsp" target="_blank" class="link">join_case</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0913</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>선택정보</td>
            <td></td>
            <td>
              <a href="join_info.jsp" target="_blank" class="link">join_info</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0914</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>약관동의</td>
            <td></td>
            <td>
              <a href="join_terms.jsp" target="_blank" class="link">join_terms</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0914</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>프로필사진등록</td>
            <td></td>
            <td>
              <a href="join_photo.jsp" target="_blank" class="link">join_photo</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0914</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>프로필사진업로드</td>
            <td></td>
            <td>
              <a href="join_upload" target="_blank" class="link">join_upload</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0914</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td>로그인</td>
            <td></td>
            <td></td>
            <td>
              <a href="login.jsp" target="_blank" class="link">login</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0915</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>(상태값)</td>
            <td></td>
            <td>
              <a href="login_case.jsp" target="_blank" class="link">login_case</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0915</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>임시번호발급</td>
            <td></td>
            <td>
              <a href="join_temporary.jsp" target="_blank" class="link">join_temporary</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0915</td>
            <td></td>
          </tr>

          <tr>
            <td></td>
            <td></td>
            <td>인증메일발송완료</td>
            <td></td>
            <td>
              <a href="join_mail.jsp" target="_blank" class="link">join_mail</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0915</td>

            <td></td>
          </tr>

          <tr>
            <td></td>
            <td>내프로필</td>
            <td></td>
            <td></td>
            <td>
              <a href="profile.jsp" target="_blank" class="link">profile</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0916</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>(상태값)</td>
            <td></td>
            <td>
              <a href="profile_case.jsp" target="_blank" class="link">profile_case</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0916</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td>홈</td>
            <td>(빈화면)</td>
            <td></td>
            <td>
              <a href="main_blank.jsp" target="_blank" class="link">main_blank</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0919</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>(채운화면)</td>
            <td></td>
            <td>
              <a href="index.jsp" target="_blank" class="link">main</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0919</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td>미팅캘린더</td>
            <td></td>
            <td></td>
            <td>
              <a href="calendar" target="_blank" class="link">calendar</a>
            </td>
            <td>
              <div class="checkbox "></div>
            </td>
            <td>개발에서 템플릿 적용</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td>새미팅룸만들기</td>
            <td>정보입력전</td>
            <td></td>
            <td>
              <a href="room_new.jsp" target="_blank" class="link">room_new</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0920</td>
            <td>1013 팝업창 버튼 정렬, input(연도,시간) 사이즈 고정</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>정보입력후</td>
            <td>(참석자단체추가),되풀이미팅</td>
            <td>
              <a href="room_setting.jsp" target="_blank" class="link">room_setting</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0920</td>
            <td>1013 팝업창 버튼 정렬, input(연도,시간) 사이즈 고정</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>미팅룸수정 / 재개설하기</td>
            <td></td>
            <td>
              <a href="room_modify.jsp" target="_blank" class="link">room_modify</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0920</td>
            <td>1013 팝업창 버튼 정렬, input(연도,시간) 사이즈 고정</td>
          </tr>
          <!--<tr>
            <td></td>
            <td></td>
            <td>미팅참여</td>
            <td></td>
            <td>
              <a href="room_join" target="_blank" class="link">room_join</a>
            </td>
            <td>
              <div class="checkbox "></div>
            </td>
            <td></td>
            <td></td>
          </tr>-->
          <tr>
            <td></td>
            <td>미팅룸</td>
            <td>호스트뷰</td>
            <td>(미팅시작전)(레이어팝업)</td>
            <td>
              <a href="meeting_host.jsp" target="_blank" class="link">meeting_host</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>(호스트 늦게시작함)</td>
            <td>
              <a href="meeting_host_2.jsp" target="_blank" class="link">meeting_host_2</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>참석자뷰</td>
            <td></td>
            <td>
              <a href="meeting_watch.jsp" target="_blank" class="link">meeting_watch</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>(미팅시작전)</td>
            <td>
              <a href="meeting_watch_2.jsp" target="_blank" class="link">meeting_watch_2</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>(호스트 늦게시작함)</td>
            <td>
              <a href="meeting_watch_live.jsp" target="_blank" class="link">meeting_watch_live</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>삭제된미팅</td>
            <td>(레이어팝업)</td>
            <td>
              <a href="meeting_cancel.jsp" target="_blank" class="link">meeting_cancel</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>비공개미팅</td>
            <td>(레이어팝업)</td>
            <td>
              <a href="meeting_private.jsp" target="_blank" class="link">meeting_private</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0926</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td>미팅룸_감성모니터</td>
            <td>호스트뷰</td>
            <td></td>
            <td>
              <a href="emotional_host.jsp" target="_blank" class="link">emotional_host</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>(case 2)</td>
            <td>
              <a href="emotional_host_2.jsp" target="_blank" class="link">emotional_host_2</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td>1013 파일다운로드 X 삭제, 참석자 box a태그 제거, 다운로드파일 x삭제, 파일용량 삭제</td>
          </tr>
          <tr>
            <td></td>
            <td>미팅분석결과</td>
            <td>호스트뷰</td>
            <td></td>
            <td>
              <a href="analysis_host.jsp" target="_blank" class="link">analysis_host</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0927</td>
            <td>1013 참석자 a태그 추가</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td>상세분석</td>
            <td>
              <a href="analysis_host_2.jsp" target="_blank" class="link">analysis_host_2</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0927</td>
            <td>1013 참석자 a태그 추가</td>
          </tr>
          <tr>
            <td></td>
            <td>메일폼</td>
            <td>인증메일발급</td>
            <td></td>
            <td>
              <a href="mail_auth.jsp" target="_blank" class="link">mail_auth</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>임시비밀번호발급</td>
            <td></td>
            <td>
              <a href="mail_password.jsp" target="_blank" class="link">mail_password</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>미팅시작알림</td>
            <td></td>
            <td>
              <a href="mail_alarm_start.jsp" target="_blank" class="link">mail_alarm_start</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>미팅시작30분전</td>
            <td></td>
            <td>
              <a href="mail_alarm_30min.jsp" target="_blank" class="link">mail_alarm_30min</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>미팅취소알림</td>
            <td></td>
            <td>
              <a href="mail_alarm_cancel.jsp" target="_blank" class="link">mail_alarm_cancel</a>
            </td>
            <td>
              <div class="checkbox selected"></div>
            </td>
            <td>0929</td>
            <td></td>
          </tr>
          </tbody>
        </table>
      </article>
    </section>
    <section id="comment">
      <article>
        <div class="accordion time-line">
          <div class="list">
            <div class="subject">
              <a href="">
                <strong>TITLE</strong>
                <time>2020-xx-xx</time>
              </a>
            </div>
            <div class="notice">
              <p>
                DESCRIPTION
              </p>
              <a href="/@download/.zip" class="btnDown">다운로드</a>
            </div>
          </div>
          <!-- list//end -->
        </div>
      </article>
    </section>
  </div>
  <!-- contents//end -->
</div>
<!-- #viewpoert//end -->
<script src="https://code.jquery.com/jquery-2.2.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
  function accordionOpener(e) {
    e.preventDefault();
    var chkSelected = $(this).parent(".list").hasClass("selected");
    var clickedSub = $(this);
    var clickedLi = $(this).parent(".list");
    if(chkSelected) {
      clickedLi.removeClass("selected");
      clickedLi.children(".notice").slideUp("100");
    } else {
      clickedLi.addClass("selected");
      clickedLi.children(".notice").slideDown("100");
      clickedLi.siblings(".list").removeClass("selected");
      clickedLi.siblings(".list").children(".notice").slideUp("100");
    }
  }

  function chkList(){
    var tatal_n = $(".codinglist tr").length-1;
    var curr_n = 0;
    $(".codinglist .checkbox").each(function() {
      if($(this).hasClass("selected")){
        curr_n++;
      } else if ($(this).text().length > 0) {
      }
    });
    $('.total_n').text(tatal_n);
    $('.curr_n').text(curr_n);
    $('.per').text(Math.floor((curr_n/tatal_n)*100) + '%');
  }

  $(document).ready(function(){
    $(".accordion .list .subject").bind("click", accordionOpener);
    $(".accordion .list:first-child").addClass("selected");
    $(".accordion .list.selected .notice").slideDown("100");
    $("table").eq(0).find(" tbody tr").each(function(idx, val) {
      $(this).find("td").eq(0).text(idx+1);
    });
    chkList();
  });
</script>
</body>
</html>
