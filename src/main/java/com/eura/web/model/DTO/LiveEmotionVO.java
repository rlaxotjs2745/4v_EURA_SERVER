package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveEmotionVO extends BaseVO {
    UdataVO udata;
    AnalysisVO mdata;
}
