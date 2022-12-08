package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcentrationVO {
    private Integer idx_meeting_user_join;
    private String level_num;
    private Integer good;
    private Integer bad;
    private Integer cameraOff;
    private Integer totalcnt;
}
