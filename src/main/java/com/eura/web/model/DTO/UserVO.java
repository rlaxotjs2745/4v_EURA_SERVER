package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    int idx_user;//	int unsigned				◯		인덱스	auto increase
    String user_name;//	varchar	20					유저 이름
    String user_id;//	varchar	400					유저 아이디	이메일 형식
    String user_pwd;//	varchar	100					유저 패스워드
    String user_phone;//	varchar	20					유저 연락처
    int eq_type01;//	tinyint			0			감성지수 타입 1	외향성 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)
    int eq_type02;//	tinyint			0			감성지수 타입 2	친화력 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)
    int eq_type03;//	tinyint			0			감성지수 타입 3	eq 지수 예약
    int eq_type04;//	tinyint			0			감성지수 타입 4	eq 지수 예약
    int eq_type05;//	tinyint			0			감성지수 타입 5	eq 지수 예약
    int eq_type06;//	tinyint			0			감성지수 타입 6	eq 지수 예약
    int eq_type07;//	tinyint			0			감성지수 타입 7	eq 지수 예약
    int eq_type08;//	tinyint			0			감성지수 타입 8	eq 지수 예약
    int eq_type09;//	tinyint			0			감성지수 타입 9	eq 지수 예약
    int eq_type10;//	tinyint			0			감성지수 타입 10	eq 지수 예약
    int user_status;//	tinyint			0			회원 상태	회원 상태 0:가입(인증전), 1:인증완료(정회원), 2:휴면회원, 3:임의 탈퇴, 4:직권탈퇴
    int privacy_terms;//	tinyint			0			개인정보 이용약관 동의 여부	개인정보 이용약관 동의 여부 0:아니오, 1:예
    int service_use_terms;//	tinyint			0			서비스 이용약관 동의 여부	서비스 이용약관 동의 여부 0:아니오, 1:예
    int profile_y;//	tinyint			0			사진 등록 여부	사진등록 여부 0:아니오, 1:예
    int temp_pw_y;//	tinyint			0			임시 비밀번호 발급 여부	임시 비밀번호 여부 0:아니오, 1:예
    String temp_pw;//	varchar	100					임시 비밀번호	임시 비밀번호 발급이 예이면 값을 가짐
    Date temp_pw_issue_dt;//	datetime						임시 비밀번호 발급 일시	사용 기간 제한위해 사용
    Date reg_dt;//	datetime						최초 등록 일시
    Date last_pw_upd_dt;//	datetime						최종 패스워드 업데이트 일시
    Date last_upd_dt;//	datetime						최종 수정 일시
}
