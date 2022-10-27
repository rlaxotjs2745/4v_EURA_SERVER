package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UsageFeeVO {

    long idx_usage_fee;//	number	32			◯		인덱스	auto increase
    String usage_fee_name;//	varchar2	100					운영요금 이름
    int fee_order;//	number	4					진도 순서
    String main_name;//	varchar2	100					대분류 이름
    String sub_name;//	varchar2	100					중분류 이름
    String fee_memo;//	varchar2	100					이용 내용 상세
    int fee_type;//	number	4		0			이용내용 타입	0:계획, 1:이용중, 2:이용완료, 3: 정산완료
    int area_size;//	number	20		1			면적	기본값:1
    int fee_rate;//	number	20		1			수량	기본값:1
    int usage_count;//	number	20		0			사용일수
    long usage_fee;//	number	32					이용요금
    Date start_date;//	date						시작 날짜
    Date end_date;//	date						종료 날짜
    long idx_corp_info;//	number	32				◯	법인 연결	tb_corp_info index pk
    long idx_user_demo_bs;//	number	32				◯	유저 실증 사업 연결	idx_user_demo_bs index pk
    Date reg_date;//	date						최초 등록 일시

}
