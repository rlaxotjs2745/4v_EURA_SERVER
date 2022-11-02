package com.eura.web.model.DTO;

import lombok.*;

@Getter
@Setter
public class MeetingVO extends BaseVO {
    private String mt_name;         //	varchar	100					미팅룸 이름
    private String mt_start_dt;     //	datetime						미팅 시작 일시
    private String mt_end_dt;       //	datetime						미팅 종료 일시	duration은 계산으로…
    private Integer mt_remind_type; // 되풀이 미팅 - 0:없음, 1:매일, 2:주, 3:월, 4:년
    private Integer mt_remind_count;    // 주기 일,주,월,년
    private String mt_remind_week;  // 반복요일 - 월,화,수,목,금,토,일
    private String mt_remind_end;   // 되풀이 미팅 종료일
    private String mt_info;         //	varchar	3000					미팅 정보
    private Integer mt_status;      //	tinyint			0			미팅룸 상태	0:비공개, 1:공개, 2:취소, 3:삭제(정보를 남겨야,, db 정리할 때 유리)
    private Integer is_live;        //	tinyint			0			라이브 시작 여부	0:아니오, 1:예

    private Integer idx_meeting_user_join;
    private Integer idx_attachment_file_info_join;

    private String mt_invite_email; // 참석자 email 리스트
    private String file_del;        // 미팅룸 첨부파일 삭제 리스트 TB_ATTACHMENT_FILE_INFO_JOIN IDX_ATTACHMENT_FILE_INFO_JOIN
    private String invite_del;      // 미팅룸 참여자 삭제 리스트 TB_USER IDX_USER

    private Integer calYear;    // 미팅 달력 년
    private Integer calMonth;   // 미팅 달력 월

    private Integer mFTyp;      // 메일폼 종류 - 1:미팅 시작, 2:미팅시작 30분전, 3:미팅취소, 4:미팅룸 공개
    private String token;
    private String sessionid;
}
