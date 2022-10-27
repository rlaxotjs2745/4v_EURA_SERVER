package com.eura.web.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class VisitDataVO {
    long idx_visit_data;//	number	32			◯		인덱스	auto increase
    Date cur_date;//	date						해당일
    int possible_count;//	number	10		0			견학 가능 수	담당자가 설정, 0이면 불가로 등록된 것
    int resulvation_count;//	number	10		0			예약수
    int is_admin_cancel;//	number	4		0			관리자 강제 취소	0:해당 없음, 1:강제취소, 강제 취소 시 예약자에게 알리는 업무를 위함
    String impossible_reason;//	varchar2	400					불가 이유
    int visit_data_type;//	number	4		0			데이터 생성 방식	0:관리자 하루단위 등록, 1:관리자 그룹단위 드록, 2:배치파일 등록
    String memo;//	varchar2	1000					메모
    long group_idx;//	number	32		0		◯	일괄 등록 시 그룹으로 묶기	그룹 삭제를 위해
    long idx_admin;//	number	32				◯	작성자	idx_admin index pk
    Date reg_date;//	date						최초 등록 일시
}
