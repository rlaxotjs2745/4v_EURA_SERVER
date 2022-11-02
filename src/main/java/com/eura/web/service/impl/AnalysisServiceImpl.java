package com.eura.web.service.impl;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("analysisService")
public class AnalysisServiceImpl implements AnalysisService {
    private final AnalysisMapper analysisMapper;

    @Autowired
    public AnalysisServiceImpl (AnalysisMapper analysisMapper){
        this.analysisMapper = analysisMapper;
    }

    @Override
    public void insertAnalysisData(AnalysisVO analysisVO) {
        try {
            analysisMapper.insertAnalysisData(analysisVO);
        } catch (Exception e){
            throw new RuntimeException("표정 분석 데이터 입력 중 문제가 발생했습니다.",e);
        }
    }
}
