package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConcentrationVO {
    private Integer idx_meeting_user_join;
    private String level_num;
    private double good;
    private double bad;
    private double enggood;
    private double engbad;
    private double cameraOff;
    private Integer totalcnt;
}
