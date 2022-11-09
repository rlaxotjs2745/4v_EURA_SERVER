package com.eura.web.model.DTO;

import lombok.*;

@Getter
@Setter
public class UserVO extends BaseVO {
    private String user_id;//	varchar	400					유저 아이디	이메일 형식
    private String user_pwd;//	varchar	100					유저 패스워드
    private String user_phone;//	varchar	20					유저 연락처
    private Integer eq_type01;//	tinyint			0			감성지수 타입 1	외향성 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)
    private Integer eq_type02;//	tinyint			0			감성지수 타입 2	친화력 지수 0:보통, 음수(그렇지 않다), 양수(그렇다)
    private Integer eq_type03;//	tinyint			0			감성지수 타입 3	eq 지수 예약
    private Integer eq_type04;//	tinyint			0			감성지수 타입 4	eq 지수 예약
    private Integer eq_type05;//	tinyint			0			감성지수 타입 5	eq 지수 예약
    private Integer eq_type06;//	tinyint			0			감성지수 타입 6	eq 지수 예약
    private Integer eq_type07;//	tinyint			0			감성지수 타입 7	eq 지수 예약
    private Integer eq_type08;//	tinyint			0			감성지수 타입 8	eq 지수 예약
    private Integer eq_type09;//	tinyint			0			감성지수 타입 9	eq 지수 예약
    private Integer eq_type10;//	tinyint			0			감성지수 타입 10	eq 지수 예약
    private Integer user_status;//	tinyint			0			회원 상태	회원 상태 0:가입(인증전), 1:인증완료(정회원), 2:휴면회원, 3:임의 탈퇴, 4:직권탈퇴
    private Integer privacy_terms;//	tinyint			0			개인정보 이용약관 동의 여부	개인정보 이용약관 동의 여부 0:아니오, 1:예
    private Integer service_use_terms;//	tinyint			0			서비스 이용약관 동의 여부	서비스 이용약관 동의 여부 0:아니오, 1:예
    private Integer profile_y;//	tinyint			0			사진 등록 여부	사진등록 여부 0:아니오, 1:예
    private Integer temp_pw_y;//	tinyint			0			임시 비밀번호 발급 여부	임시 비밀번호 여부 0:아니오, 1:예
    private String temp_pw;//	varchar	100					임시 비밀번호	임시 비밀번호 발급이 예이면 값을 가짐
    private String temp_pw_issue_dt;//	datetime						임시 비밀번호 발급 일시	사용 기간 제한위해 사용
    private String last_pw_upd_dt;//	datetime						최종 패스워드 업데이트 일시

    private String authKey; // 가입 인증 번호

    private boolean autoLogin;
    public boolean isAutoLogin(){
        return autoLogin;
    }
    public void setAutoLogin(boolean autoLogin){
        this.autoLogin = autoLogin;
    }
}
