package com.eura.web.service;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;

import java.util.List;

public interface AnalysisService {
    public Long insertAnalysisData(AnalysisVO analysisVO);

    public ConcentrationVO getConcentrationRate(AnalysisVO analysisVO);

    public List<ConcentrationVO> getPersonalRate(List<AnalysisVO> analysisVOList, int level);
}
