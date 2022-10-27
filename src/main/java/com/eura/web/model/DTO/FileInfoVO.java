package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class FileInfoVO {
    long idx_file_info;//	number	32			◯		인덱스	auto increase
    String file_name;//	varchar2	256					파일 이름
    int file_type;//	number	4		0			파일 타입	0:해당없음, 1:실증계획, 2:실증보고, …
    int file_status;//	number	4		0			파일 상태	0:정상, 1:사용불가
    String mime_type;//	varchar2	100					마임 타입
    int encoding;//	number	4		0			파일 인코딩 종류	0:해당없음, 1:utf-8, …
    String extention;//	varchar2	20					확장자
    int file_secure_type;//	number	4		0			파일 보안 종류	0:일반, 1:보안
    String file_path;//	varchar2	256					파일 경로
    long file_size;//	number	32					파일 크기
    int checksum;//	number	20					파일 무결성 체크 첵섬
    int owner;//	number	4		0			소유자	0:어드민, 1:유저, 99:소유자 없음
    long idx_user;//	number	32				◯	소유자가 유저일경우
    long idx_admin;//	number	32				◯	소유자가 관리자일경우
    Date reg_date;//	date						최초 등록 일시
}
