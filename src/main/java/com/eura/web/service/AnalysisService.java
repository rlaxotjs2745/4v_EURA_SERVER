package com.eura.web.service;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.LiveEmotionVO;

public interface AnalysisService {
    public void insertAnalysisData(LiveEmotionVO liveEmotionVO);

    public ConcentrationVO getConcentrationRate(AnalysisVO analysisVO);
}
