package com.eura.web.service.impl;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.GraphMidVO;
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

    // 개인의 10분 단위 감정분석 데이터를 수식에 적용한 평균값, 이하 레벨
    // 매개변수: 한 미팅의 개인 전체 데이터 리스트(time_stamp sorted)
    public PersonalLevelVO getPersonalLevel(List<AnalysisVO> analysisVOList){
        PersonalLevelVO personalLevelVO = new PersonalLevelVO();

        int level_num = 0;
        int level = 600; //10분
        // time_stamp 1당 1초

        List<ConcentrationVO> result = new ArrayList<>();
        // ConcentrationVO
        // good 00%, bad 00%, levelNum 현재 몇번째 레벨인지
        
        if(analysisVOList!=null && analysisVOList.size()>0){
            Integer first = analysisVOList.get(0).getTimestamp(); // time_stamp에 의해 sorting된 첫 데이터의 시간을 기준으로 10분
            Integer _uidx = analysisVOList.get(0).getIdx_meeting_user_join();
            
            Integer count = first + level;
            int eng1 = 0;
            int eng0 = 0;
            int att1 = 0;
            int att0 = 0;
            Integer _ncnt = 0;
            for(AnalysisVO analysisVO : analysisVOList){
                if(analysisVO.getTimestamp() > count){ // 만약 반복문 내 현재 데이터가 10분 이후의 데이터일 시
                    ConcentrationVO concentrationVO = new ConcentrationVO();

                    concentrationVO.setIdx_meeting_user_join(_uidx);
                    concentrationVO.setLevel_num(level_num);
                    concentrationVO.setGood((eng1 + att1) / (eng0+eng1+att0+att1) * 100);
                    concentrationVO.setBad((eng0 + att0) / (eng0+eng1+att0+att1) * 100);

                    result.add(concentrationVO); // 현재값 저장

                    eng1 = 0;
                    eng0 = 0;
                    att1 = 0;
                    att0 = 0; // 초기화

                    count+= level; // 다음 측정 최대 시간 설정
                    level_num++; // 레벨 순서 업데이트
                }else{
                    if(_ncnt == analysisVOList.size()){
                        level_num++;
                    }
                }

                if(analysisVO.getEngagement() >= 0.25){
                    eng1++;
                } else if (analysisVO.getEngagement() < 0.25){
                    eng0++;
                }

                if(analysisVO.getAttention() >= 0.75){
                    att1++;
                } else if (analysisVO.getAttention() < 0.75) {
                    att0++;
                }
                _ncnt++;
            }

            ConcentrationVO concentrationVO = new ConcentrationVO();
            concentrationVO.setIdx_meeting_user_join(_uidx);
            concentrationVO.setLevel_num(level_num);
            concentrationVO.setGood((eng1 + att1) / (eng0+eng1+att0+att1) * 100);
            concentrationVO.setBad((eng0 + att0) / (eng0+eng1+att0+att1) * 100);

            result.add(concentrationVO);
            // 다 돌고 나서 남은 데이터 입력
        }

        personalLevelVO.setMaxLevel(level_num);
        personalLevelVO.setConcentrationList(result);

        return personalLevelVO;
    }

    // 개인별 집중도 수치(웹뷰 오른쪽 개인 집중 %)
    // 매개변수: AnalysisService getPersonalLevel을 통해 나온 개인 데이터
    @Override
    public ConcentrationVO getPersonalRate(PersonalLevelVO personalLevelVO){

        ConcentrationVO realResult = new ConcentrationVO();
        realResult.setGood(0);
        realResult.setBad(0);
        realResult.setCameraOff(0);

        int goodNum = 0;
        int badNum = 0;
        if(personalLevelVO!=null){
            int count = personalLevelVO.getMaxLevel();
            if(personalLevelVO.getConcentrationList()!=null && personalLevelVO.getConcentrationList().size()>0){
                if(count>0){
                    for (ConcentrationVO con : personalLevelVO.getConcentrationList()){
                        goodNum += con.getGood();
                        badNum += con.getBad();
                    }

                    double goodAvg = (double) (goodNum / count);
                    double badAvg = (double) (badNum / count);
                    double cameraOff = 100 - (goodAvg+badAvg);
                    // 100에서 good, bad의 평균값을 제외할 경우 남는 값이 cameraOff
                    realResult.setGood(goodAvg);
                    realResult.setBad(badAvg);
                    realResult.setCameraOff(cameraOff);
                }
            }
        }

        return realResult;
    }


    // 미팅의 레벨당 전체 참여자 집중도 수치 평균값(웹뷰 동영상 밑 막대 그래프)
    // 매개변수: AnalysisService getPersonalLevel을 통해 나온 모든 참여자의 개인 데이터 리스트
    @Override
    public GraphMidVO getAllUserRate(List<PersonalLevelVO> personalLevelVOList) {
        GraphMidVO result = new GraphMidVO();

        List<Double> goodList = new ArrayList<>();
        List<Double> badList = new ArrayList<>();
        Integer _psize = personalLevelVOList.size();

        if(personalLevelVOList != null && _psize > 0){
            Integer maxLevel = personalLevelVOList.get(0).getMaxLevel();
            goodList = new ArrayList<>(maxLevel);
            badList = new ArrayList<>(maxLevel);
            // ArrayList의 인덱스 값 = 레벨 순서
            for(PersonalLevelVO person : personalLevelVOList){ // 참여자 한명씩 탐색함
                List<ConcentrationVO> contList = person.getConcentrationList();
                if(contList!=null){
                    for(int idx=0; idx < maxLevel; idx++){ // 참여자 한명의 모든 레벨 데이터 불러오기
                        Integer _lvlnum = contList.get(idx).getLevel_num()-1;
                        Double _good = contList.get(idx).getGood();
                        Double _bad = contList.get(idx).getBad();
                        goodList.set(_lvlnum, goodList.get(_lvlnum) == null ? _good : goodList.get(_lvlnum) + _good);
                        badList.set(_lvlnum, badList.get(_lvlnum) == null ? _bad : badList.get(_lvlnum) + _bad);
                        // 모든 참여자의 레벨 당 good, bad 값을 더함
                    }
                }
            }

            if(_psize>0){
                for (int i = 0; i < maxLevel; i++){
                    goodList.set(i, goodList.get(i) / _psize);
                    badList.set(i, badList.get(i) / _psize);
                }
            }
            // 모든 참여자의 레벨당 데이터 합산 값 / 모든 참여자 수 == 모든 참여자의 레벨당 집중도 평균
            // 이 경우 데이터 예시
            // [88.0, 12.33, 32.36] 식으로 데이터가 쌓이게 된다.
            // 이 경우 '0번 레벨의 전체 참여자의 집중도 평균은 88.0%, 1번 레벨은 12.33%, ...' 라고 해석할 수 있다.

        }
        
        // result.put("goodList", goodList);
        // result.put("badList", badList);
        result.setGoodList(goodList);
        result.setBadList(badList);
        return result;
    }

    // 미팅의 전체 집중도 수치 평균값(웹뷰 미팅 분석결과 우측 원형 그래프)
    // 매개변수: AnalysisService getAllUserRate를 통해 나온 모든 레벨 당 전체 참여자 데이터 평균 리스트
    @Override
    public ConcentrationVO getMeetingRate(List<Double> goodList, List<Double> badList) {
        // AnalysisService getAllUserRate의 값은 Map이기 때문에 매개변수에 goodList, badList를 나누어 적용해야 함.

        ConcentrationVO concentrationVO = new ConcentrationVO();

        Double resultGood = 0.0;
        Double resultBad = 0.0;

        if(goodList != null && goodList.size() > 0 && badList != null && badList.size() > 0){
            for(int i=0; i<goodList.size(); i++){
                resultGood += (Double) goodList.get(i);
                resultBad += (Double) badList.get(i);
                // 모든 레벨 당 데이터를 더함
            }
            resultGood = resultGood/goodList.size();
            resultBad = resultBad/badList.size();
        }

        concentrationVO.setGood(resultGood); // 모든 레벨당 데이터를 더한 값을 레벨 갯수로 나눈어 평균값 도출
        concentrationVO.setBad(resultBad);
        concentrationVO.setCameraOff(100-(resultGood + resultBad));
        // 100에서 good, bad의 평균값을 제외할 경우 남는 값이 cameraOff

        return concentrationVO;
    }



}
