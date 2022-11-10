package com.eura.web.service;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;

public interface AnalysisService {
    public Long insertAnalysisData(AnalysisVO analysisVO);

    public ConcentrationVO getConcentrationRate(AnalysisVO analysisVO);
}
