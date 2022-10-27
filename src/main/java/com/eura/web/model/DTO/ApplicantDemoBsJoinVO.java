package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicantDemoBsJoinVO {
    long idx_applicant_demo_bs_join;//	number	32			◯		인덱스	auto increase
    int applicant_type;//	number	4		1			실증사업 담당자 타입	0:개인, 1:회사, 2:지자체, 3유관기관, 99:기타
    long idx_user_demo_bs;//	number	32				◯	유저 실증 사업
    long idx_user;//	number	32				◯	담당자
    long join_type;//	number	4		0				0:처음부터 연결, 1:중간에 연결
    Date reg_date;//	date
}
