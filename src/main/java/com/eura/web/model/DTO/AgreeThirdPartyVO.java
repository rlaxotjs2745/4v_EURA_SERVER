package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class AgreeThirdPartyVO {
    long idx_third_party_agree;//	number	32			◯		인덱스
    int agree_ver;//number	4					버전	1부터 시작 1씩 증가
    String contents;//	clob						약관 내용
    long idx_admin;//	number	32				◯	등록자
    Date reg_date;//	date						등록일
    Date last_upd_date;//	date						최종 변경일
}
