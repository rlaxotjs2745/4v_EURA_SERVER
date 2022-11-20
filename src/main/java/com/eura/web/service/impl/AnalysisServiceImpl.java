package com.eura.web.service.impl;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.PersonalLevelVO;
import com.eura.web.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 개인별 집중도 점수 파악
    @Override
    public ConcentrationVO getPersonalRate(PersonalLevelVO personalLevelVO){

        ConcentrationVO realResult = new ConcentrationVO();

        int goodNum = 0;
        int badNum = 0;
        int count = personalLevelVO.getMaxLevel();

        for (ConcentrationVO con : personalLevelVO.getConcentrationList()){
            goodNum += con.getGood();
            badNum += con.getBad();
        }

        double goodAvg = goodNum/ count;
        double badAvg = badNum/count;

        double cameraOff = 100 -(goodAvg+badAvg);

        realResult.setGood(goodAvg);
        realResult.setBad(badAvg);
        realResult.setCameraOff(cameraOff);

        return realResult;

    }

    @Override
    public Map getAllUserRate(List<PersonalLevelVO> personalLevelVOList) {

        List<Double> goodList = new ArrayList<>(personalLevelVOList.get(0).getMaxLevel());
        List<Double> badList = new ArrayList<>(personalLevelVOList.get(0).getMaxLevel());

        for(PersonalLevelVO person : personalLevelVOList){
            List<ConcentrationVO> contList = person.getConcentrationList();
            for(int idx=0; idx< person.getMaxLevel(); idx++){
                goodList.set(contList.get(idx).getLevel_num(), goodList.get(contList.get(idx).getLevel_num()) == null ? contList.get(idx).getGood() : goodList.get(contList.get(idx).getLevel_num()) + contList.get(idx).getGood());
                badList.set(contList.get(idx).getLevel_num(), badList.get(contList.get(idx).getLevel_num()) == null ? contList.get(idx).getBad() : badList.get(contList.get(idx).getLevel_num()) + contList.get(idx).getBad());
            }
        }

        Map result = new HashMap();

        result.put("goodList", goodList);
        result.put("badList", badList);

        return result;
    }

    @Override
    public ConcentrationVO getMeetingRate(List goodList, List badList) {
        ConcentrationVO concentrationVO = new ConcentrationVO();


        Double resultGood = 0.0;
        Double resultBad = 0.0;

        for(int i=0; i<goodList.size(); i++){
            resultGood += (Double) goodList.get(i);
            resultBad += (Double) badList.get(i);
        }

        concentrationVO.setGood(resultGood/goodList.size());
        concentrationVO.setBad(resultBad/badList.size());
        concentrationVO.setCameraOff(100-(concentrationVO.getGood() + concentrationVO.getBad()));

        return concentrationVO;
    }


    public PersonalLevelVO getPersonalLevel(List<AnalysisVO> analysisVOList){
        PersonalLevelVO personalLevelVO = new PersonalLevelVO();

        int level = 600; //10분

        List<ConcentrationVO> result = new ArrayList<>();

        if(analysisVOList!=null){
            Integer first = analysisVOList.get(0).getTime_stamp();
            Integer count = first + level;

            int level_num = 1;
            int eng1 = 0;
            int eng0 = 0;
            int att1 = 0;
            int att0 = 0;

            for(AnalysisVO analysisVO: analysisVOList){

                if(analysisVO.getTime_stamp() >= count){
                    ConcentrationVO concentrationVO = new ConcentrationVO();

                    concentrationVO.setIdx_meeting_user_join(analysisVO.getIdx_meeting_user_join());
                    concentrationVO.setLevel_num(level_num);
                    concentrationVO.setGood((eng1 + att1) / (eng0+eng1+att0+att1) * 100);
                    concentrationVO.setBad((eng0 + att0) / (eng0+eng1+att0+att1) * 100);

                    result.add(concentrationVO);

                    eng1 = 0;
                    eng0 = 0;
                    att1 = 0;
                    att0 = 0;

                    count+= level;
                    ++level_num;
                }

                if(analysisVO.getEngagement() >=0.25){
                    eng1++;
                } else if (analysisVO.getEngagement() < 0.25){
                    eng0++;
                }

                if(analysisVO.getAttention() >= 0.75){
                    att1++;
                } else if (analysisVO.getAttention() < 0.75) {
                    att0++;
                }

            }
            
            ConcentrationVO concentrationVO = new ConcentrationVO();

            concentrationVO.setIdx_meeting_user_join(analysisVOList.get(analysisVOList.size()-1).getIdx_meeting_user_join());
            concentrationVO.setLevel_num(level_num);
            concentrationVO.setGood((eng1 + att1) / (eng0+eng1+att0+att1) * 100);
            concentrationVO.setBad((eng0 + att0) / (eng0+eng1+att0+att1) * 100);
            
            result.add(concentrationVO);
            
            count+= level;
            
            
            personalLevelVO.setMaxLevel(count);
            personalLevelVO.setConcentrationList(result);
        }

        return personalLevelVO;
    }
}
