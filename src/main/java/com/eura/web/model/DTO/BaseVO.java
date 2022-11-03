package com.eura.web.model.DTO;

import lombok.*;

@Getter
@Setter
public class BaseVO {
    private Integer idx_user;//	int unsigned 회원 INDEX
    private Integer idx_meeting;//	int unsigned 미팅룸 INDEX
    private String user_email;
    private String user_name;//	varchar	20					유저 이름

    private String reg_dt;//	datetime						최초 등록 일시
    private String last_upd_dt;//	datetime						최종 수정 일시
    private String del_dt;          // 삭제 일시
    
    private Integer delete_stat;    // 삭제여부 0:미삭제, 1:삭제
    private String remote_ip;   // 접속자 IP
    private String file_path;
    private String file_name;
    private Long file_size;
    private Integer recordCountPerPage;
    private Integer firstIndex;
    private Integer currentPage;
    private Integer pageSort;       // 페이징 정렬 방식 - 1:최신순, 2:미팅 시간순, 3:비공개 미팅순, 4:취소된 미팅순
    // private Integer pageSortType;   // 페이징 정령 순서 - 1:내림차순, 2:오름차순

    private Integer chkcnt;
    private String mcid;    // ZOOM의 연결점 중 강의실 고유번호(ID) - TB_MEETING Table의 IDX_MEETING 인덱스 컬럼
}
