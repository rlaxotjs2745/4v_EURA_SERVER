package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MeetingVO {
    int idx_meeting;//	int unsigned				◯		인덱스	auto increase
    int idx_user;//	int unsigned					◯	호스트 인덱스
    String mt_name;//	varchar	100					미팅룸 이름
    Date mt_start_dt;//	datetime						미팅 시작 일시
    Date mt_end_dt;//	datetime						미팅 종료 일시	duration은 계산으로…
    String mt_info;//	varchar	3000					미팅 정보
    int mt_status;//	tinyint			0			미팅룸 상태	0:비공개, 1:공개, 2:취소, 3:삭제(정보를 남겨야,, db 정리할 때 유리)
    int is_live;//	tinyint			0			라이브 시작 여부	0:아니오, 1:예
    Date reg_dt;//	datetime						최초 등록 일시
    Date last_upd_dt;//	datetime						최종 수정 일시
}
