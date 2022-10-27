package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeVO {
    long idx_notice;//	number	32			◯		인덱스	auto increase
    String notice_code;//	varchar2	100					발행 코드	서브 타이틀 역할도 함
    int notice_num;//	number	20					노티스 넘버링	중복 허용함
    String subject;//	varchar2	100					어드민 패스워드	최소 8자리, 단방향 암호화
    String notice_contents;//	clob						어드민  이름
    int is_file;//	number	4		0			파일 포함여부	0:포함안함 1:포함
    int is_new;//	number	4		0			신규등록여부	0:신규아님 1:신규
    int is_main_page;//	number	4		0			메인페이지에 노출여부	0:노출안함 1:노출
    int is_important;//	number	4		0			공지사항 제일 처음부분에 고정	0:고정안함 1:고정
    int is_show;//	number	4		0			노출여부	0:노출안함 1:노출
    int view_count;//	number	20					조회수
    int notice_status;//	number	4		0			공지사항 게시 프로세스 상태	0:임시저장, 1:허락, 2:수정 발행, 3:게시 종료, 99:기타
    Date posting_start_date;//	date						게시 시작 일시분초
    Date posting_end_date;//	date						게시 종료 일시분초
    int doc_version;//	number	4		1			공지 버전	default:1, increase 1
    String memo;//	varchar2	1000					메모	공고 관련 내용
    long search_tag;//	number	32					검색 태그
    String author;//	varchar2	40					게시자	게시자 이름
    long idx_admin;//	number	32				◯	최초 등록 어드민
    long confirm_admin_idx;//	number	32				◯	허가자
    Date reg_date;//	date						최초 등록 일시
    Date last_upd_date;//	date						최종 수정 일시
}
