package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleItemVO {

    long idx_schedule_item;//	number	32			◯		인덱스	auto increase
    long idx_schedule;//	number	32					등록 유니크 번호	반복 일정 시 동일해야 함
    Date start_date;//	date						일정 시작일시
    Date end_date;//	date						일정 종료일시
    int schedule_type;//	number	4		0			일정 타입	0:단순, 1:단일기간, 2:단순반복, 3:기간 반복
    int noti_type;//	number	4		0			알림시간	0:등록시, 1:1시간전, 2:1일전, 3:지정일시
    Date noti_date;//	date						알림시간
    int show_type;//	number	4		1			일정 노출 타입	0:모두, 1:소유자
    int grade;//	number	4		99			노출 등급	소스에 하드코딩     99:지정색 사용
    String color_code;//	varchar2	10					지정 색	#000000 rgb
    String contents;//	varchar2	1000					스케줄 내용
    long idx_admin;//	number	32				◯	소유자
    Date reg_date;//	date						최초 등록 일시
    Date last_upd_date;//	date						최종 수정 일시

}
