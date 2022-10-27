package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class VisitReqVO {
    long idx_visit_req;//	number	32			◯		인덱스	auto increase
    int visit_req_num;//	number	20					방문 요청번호	1시작 1증가
    String group_name;//	varchar2	40					단체이름
    String visitor;//	varchar2	40					신청자이름
    Date resulvation_date;//	date						예약일
    int visitor_count;//	number	10					방문자 수
    String visitor_mphone_num;//	varchar2	20					연락처
    String visitor_email;//	varchar2	250					방문자 이메일
    String visitor_adress;//	varchar2	250					방문자 주소	0:상담요청등록, 1:상담자 지정, 2:상담 계획 보냄, 3: 상담계획 변경, 4:상담완료, 99:상담불가
    String visit_goal;//	varchar2	400					방문목표
    String question;//	varchar2	400					방문전 질문사항
    int visit_req_status;//	number	4		0			방문 요청 상태	0:신청, 1:접수, 2:승인, 3:방문, 4:방문 완료, 5:방문 취소, 6:승인 거절, 99:기타
    int is_privacy_accept;//	number	4		1			개인정보 수집 이용동의	0:동의안함, 1:동의
    int is_group;//	number	4		0			그룹 여부	0:개인, 1:그룹
    int is_cancel;//	number	4		0			취소 여부	0:취소안함, 1:취소함
    int is_manager;//	number	4		0			담당자 배정 여부	0:배정안됨, 1:배정됨
    long idx_manager;//	number	32		0		◯	담당자
    int is_duration;//	number	4		0			여러날 여부	0:하루, 1:여러날
    int duration_amount;//	number	10					해당일 포함 기간
    String memo;//	varchar2	1000					메모
    long idx_visit_data;//	number	32		0		◯	관리를 위한 연결
    long idx_user;//	number	32		0		◯	작성자
    long idx_admin;//	number	32		0		◯	확인자/업무 지시자	idx_admin index pk
    Date reg_date;//	date						최초 등록 일시
}
