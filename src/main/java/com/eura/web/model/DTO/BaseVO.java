package com.eura.web.model.DTO;

import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class BaseVO {
    private Long idx_user;//	int unsigned 회원 INDEX
    private Integer idx_meeting;//	int unsigned 미팅룸 INDEX

    private Date reg_dt;//	datetime						최초 등록 일시
    private Date last_upd_dt;//	datetime						최종 수정 일시

    private String remote_ip;   // 접속자 IP
    private String file_path;
    private String file_name;

}
