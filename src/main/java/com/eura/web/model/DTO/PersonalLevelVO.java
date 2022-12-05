package com.eura.web.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PersonalLevelVO {
    int maxLevel;
    int idx_meeting_user_join;
    List<ConcentrationVO> concentrationList;

    Integer Good;
    Integer Bad;
    Integer Off;
}
