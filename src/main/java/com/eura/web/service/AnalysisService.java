package com.eura.web.service;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.GraphMidVO;
import com.eura.web.model.DTO.PersonalLevelVO;

import java.util.List;

public interface AnalysisService {
    public Long insertAnalysisData(AnalysisVO analysisVO);

    // 개인 분석 요약 데이터
    public ConcentrationVO getPersonalRate(List<PersonalLevelVO> allLevelVOList, Integer _idx);

    public GraphMidVO getAllUserRate(List<PersonalLevelVO> allLevelVOList);

    public ConcentrationVO getTotalRate(List<PersonalLevelVO> allLevelVOList);

    public ConcentrationVO getMeetingRate(List<Integer> goodList, List<Integer> badList, List<Integer> totcalCntList);

    public PersonalLevelVO getLevelData(List<AnalysisVO> analysisVOList, AnalysisVO _Time, Integer _idxjoin, Integer _dataChk) throws Exception;


}
