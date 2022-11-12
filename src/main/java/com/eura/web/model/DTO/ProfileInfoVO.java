package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileInfoVO {
    long idx_user;       // 유저 인덱스
    String file_path;   // 파일 경로
    String file_name;   // 파일 명
    String reg_dt;        // 등록일
}
