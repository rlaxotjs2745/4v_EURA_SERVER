package com.eura.web.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveEmotionVO extends BaseVO {
    String token; //user - meeting 토큰
    String mcid; // 강의실 고유 코드 id
    String zuid; // zoom user id
    AnalysisVO emotion; // 감정 데이터
}
