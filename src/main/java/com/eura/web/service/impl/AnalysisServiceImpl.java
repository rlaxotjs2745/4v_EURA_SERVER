package com.eura.web.service.impl;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("analysisService")
public class AnalysisServiceImpl implements AnalysisService {
    private final AnalysisMapper analysisMapper;

    @Autowired
    public AnalysisServiceImpl (AnalysisMapper analysisMapper){
        this.analysisMapper = analysisMapper;
    }

    @Override
    public Long insertAnalysisData(AnalysisVO analysisVO) {
        Long analysisIdx = 0L;
        try {
            analysisIdx = analysisMapper.insertAnalysisData(analysisVO);
            System.out.println("anal Idx: " + analysisIdx);
        } catch (Exception e){
            throw new RuntimeException("표정 분석 데이터 입력 중 문제가 발생했습니다.",e);
        }
        return analysisIdx;
    }

    @Override
    public ConcentrationVO getConcentrationRate(AnalysisVO analysisVO){
        ConcentrationVO result = new ConcentrationVO();

        return result;
    }

    @Override
    public List<ConcentrationVO> getPersonalRate(List<AnalysisVO> analysisVOList, int level){
        List<ConcentrationVO> result = null;
        Integer first = analysisVOList.get(0).getTime_stamp();
        Integer count = first + level;

        int eng1 = 0;
        int eng0 = 0;
        int att1 = 0;
        int att0 = 0;

        for(AnalysisVO analysisVO: analysisVOList){
            if(analysisVO.getTime_stamp() >= count){
                count+= level;
                ConcentrationVO concentrationVO = new ConcentrationVO();

                concentrationVO.setGood((eng1 + att1) / (eng0+eng1+att0+att1) * 100);
                concentrationVO.setBad((eng0 + att0) / (eng0+eng1+att0+att1) * 100);
            }
            if(analysisVO.getEngagement() >=0.25){
                eng1++;
            } else if (analysisVO.getEngagement() < 0.25){
                eng0++;
            }


        }

        return result;
    }
}
