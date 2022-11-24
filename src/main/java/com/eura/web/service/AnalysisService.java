package com.eura.web.service;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.GraphMidVO;
import com.eura.web.model.DTO.PersonalLevelVO;

import java.util.List;
import java.util.Map;

public interface AnalysisService {
    public Long insertAnalysisData(AnalysisVO analysisVO);

    public ConcentrationVO getPersonalRate(PersonalLevelVO personalLevelVO);

    public GraphMidVO getAllUserRate(List<PersonalLevelVO> personalLevelVOList);

    public ConcentrationVO getMeetingRate(List<Double> goodList, List<Double> badList);

    public PersonalLevelVO getPersonalLevel(List<AnalysisVO> analysisVOList, AnalysisVO _Time, Integer _idxjoin, Integer duration) throws Exception;
}
