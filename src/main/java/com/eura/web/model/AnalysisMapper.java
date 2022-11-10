package com.eura.web.model;

import com.eura.web.model.DTO.AnalysisVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnalysisMapper {
    //미팅 중 감정 데이터 저장
    public Long insertAnalysisData(AnalysisVO analysisVO);
}
