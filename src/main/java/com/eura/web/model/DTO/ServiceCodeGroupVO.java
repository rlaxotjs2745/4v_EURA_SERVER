package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceCodeGroupVO {

    long idx_service_code_group;//	number	32			◯		인덱스
    int code_group;//	number	4					코드일련번호
    String code_group_explanation;//	varchar2	100					코드그룹 설명
    Date reg_date;//	date						최초 등록 일시

}
