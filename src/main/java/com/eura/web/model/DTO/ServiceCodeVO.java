package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ServiceCodeVO {

    long idx_service_code;//	number	32			◯		인덱스
    int code;//	number	4					코드일련번호
    String code_string_kor;//	varchar2	20					코드 대응 한글
    String code_string_eng;//	varchar2	20					코드 대응 영어
    String code_string_chn;//	varchar2	20					코드 대응 중국어
    String code_string_jpn;//	varchar2	20					코드 대응 일본어
    String code_explanation;//	varchar2	100					코드 설명
    Date reg_date;//	date						최초 등록 일시

}
