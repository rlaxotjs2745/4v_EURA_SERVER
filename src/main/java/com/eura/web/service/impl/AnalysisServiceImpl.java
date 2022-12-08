package com.eura.web.service.impl;

import com.eura.web.base.BaseController;
import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.GraphMidVO;
import com.eura.web.model.DTO.PersonalLevelVO;
import com.eura.web.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("analysisService")
public class AnalysisServiceImpl extends BaseController implements AnalysisService {
    @Autowired
    private AnalysisMapper analysisMapper;

    @Override
    public Long insertAnalysisData(AnalysisVO analysisVO) {
        Long analysisIdx = 0L;
        try {
            analysisIdx = analysisMapper.insertAnalysisData(analysisVO);
        } catch (Exception e){
            throw new RuntimeException("감정 분석 데이터 입력 중 문제가 발생했습니다.",e);
        }
        return analysisIdx;
    }

    /**
     * 종합 데이터를 이용하여 구간당 개별 몰입도를 뽑는다.
     * @param analysisVOList
     * @param _Time
     * @param _idxjoin
     * @return PersonalLevelVO
     * @throws Exception
     */
    public PersonalLevelVO getLevelData(List<AnalysisVO> analysisVOList, AnalysisVO _Time, Integer _idxjoin) throws Exception{
        PersonalLevelVO personalLevelVO = new PersonalLevelVO();
        Integer duration = _Time.getDuration();
        int _tcnt = 60;  // 구간 수
        int level = 60; // 60초
        int level_num = 0;
        int eng1 = 0;
        int eng0 = 0;
        int nowtime = 0;

        level = (int) Math.ceil((double)duration/60);  // 60등분
        if(duration<=600 && duration > 60){
            level = 10;  // 600초 이하 10초 간격
            _tcnt = (int) Math.ceil((double)duration/10);
        }else if(duration<=60){
            level = 1;  // 600초 이하 10초 간격
            _tcnt = duration;
        }

        List<ConcentrationVO> result = new ArrayList<>();
        
        for(int i=0;i<_tcnt;i++){
            Integer count0 = _Time.getTimefirst() + (level*i);
            Integer count = _Time.getTimefirst() + (level*(i+1));
            if(i==(_tcnt-1)){
                nowtime = duration;
            }else{
                nowtime = level*(i+1);
            }
            if(analysisVOList.size()>0){
                Integer _ncnt = 0;
                for(AnalysisVO analysisVO : analysisVOList){
                    if(_Time.getTimeend() >= analysisVO.getTimestamp()){
                        // 만약 반복문 내 현재 데이터가 10분 이후의 데이터일 시
                        if(count0 <= analysisVO.getTimestamp() && count > analysisVO.getTimestamp()){
                            if(analysisVO.getIdx_meeting_user_join() == _idxjoin){
                                if(analysisVO.getEngagement() >= 0.25){
                                    eng1++;
                                } else {
                                    eng0++;
                                }
                            }
                            _ncnt++;
                        }
                    }
                }

                ConcentrationVO concentrationVO = new ConcentrationVO();
                concentrationVO.setIdx_meeting_user_join(_idxjoin);
                concentrationVO.setLevel_num(getSec2Time(nowtime) + "");
                concentrationVO.setGood(eng1);
                concentrationVO.setBad(eng0);
                concentrationVO.setTotalcnt(_ncnt);
                result.add(concentrationVO);

                eng1 = 0;
                eng0 = 0;
                _ncnt = 0;
            }else{
                ConcentrationVO concentrationVO = new ConcentrationVO();
                concentrationVO.setIdx_meeting_user_join(_idxjoin);
                concentrationVO.setLevel_num(getSec2Time(nowtime));
                concentrationVO.setGood(0);
                concentrationVO.setBad(0);
                concentrationVO.setTotalcnt(0);
                result.add(concentrationVO);
            }

            level_num++; // 레벨 순서 업데이트
        }

        personalLevelVO.setIdx_meeting_user_join(_idxjoin);
        personalLevelVO.setMaxLevel(level_num);
        personalLevelVO.setConcentrationList(result);

        return personalLevelVO;
    }

    // 개인 분석 요약 데이터 : 개인별 집중도 수치(참석자별 몰입도 %)
    // 매개변수: AnalysisService getPersonalLevel을 통해 나온 개인 데이터
    @Override
    public ConcentrationVO getPersonalRate(List<PersonalLevelVO> allLevelVOList, Integer _idx){
        ConcentrationVO realResult = new ConcentrationVO();
        int goodAvg = 0;
        int badAvg = 0;
        int cameraOff = 100;
        if(allLevelVOList!=null && allLevelVOList.size()>0){
            int goodNum = 0;
            int badNum = 0;
            int timecnt = 0;
            for(PersonalLevelVO personalLevelVO : allLevelVOList){
                if(personalLevelVO.getIdx_meeting_user_join() == _idx){
                    int count = personalLevelVO.getMaxLevel();
                    if(personalLevelVO.getConcentrationList()!=null && personalLevelVO.getConcentrationList().size()>0){
                        if(count>0){
                            for (ConcentrationVO con : personalLevelVO.getConcentrationList()){
                                goodNum += con.getGood();
                                badNum += con.getBad();
                                timecnt += con.getTotalcnt();
                            }
                        }
                    }
                }
            }
            double good0 = goodNum / (double)timecnt;
            double bad0 = badNum / (double)timecnt;
            goodAvg = (int) (Math.round(good0 * 100));
            badAvg = (int) (Math.round(bad0 * 100));
            cameraOff = 100 - (goodAvg+badAvg);
            // 100에서 good, bad의 평균값을 제외할 경우 남는 값이 cameraOff
        }
        realResult.setGood(goodAvg);
        realResult.setBad(badAvg);
        realResult.setCameraOff(cameraOff);

        return realResult;
    }


    // 미팅의 레벨당 전체 참여자 집중도 수치 평균값(전체 그래프)
    // 매개변수: AnalysisService getPersonalLevel을 통해 나온 모든 참여자의 개인 데이터 리스트
    @Override
    public GraphMidVO getAllUserRate(List<PersonalLevelVO> allLevelVOList) {
        GraphMidVO result = new GraphMidVO();

        Integer _psize = allLevelVOList.size();
        if(allLevelVOList != null && _psize > 0){
            // ArrayList의 인덱스 값 = 레벨 순서
            Integer maxLevel = allLevelVOList.get(0).getMaxLevel();
            List<String> lvlList = new ArrayList<>();
            List<Integer> goodList = new ArrayList<>();
            List<Integer> badList = new ArrayList<>();
            List<Integer> goodList0 = new ArrayList<>();
            List<Integer> badList0 = new ArrayList<>();
            List<Integer> totcalCntList = new ArrayList<>();
            List<Integer> totcalCntList0 = new ArrayList<>();
            for(int i=0; i < maxLevel; i++){
                goodList.add(0);
                badList.add(0);
                goodList0.add(0);
                badList0.add(0);
                lvlList.add("");
                totcalCntList.add(0);
                totcalCntList0.add(0);
            }
            if(maxLevel>0){
                for(PersonalLevelVO person : allLevelVOList){ // 참여자 한명씩 탐색함
                    List<ConcentrationVO> contList = person.getConcentrationList();
                    if(contList!=null && contList.size()>0){
                        for(int i=0; i < goodList0.size(); i++){ // 참여자 한명의 모든 레벨 데이터 불러오기
                            goodList0.set(i, goodList0.get(i) + contList.get(i).getGood());
                            badList0.set(i, badList0.get(i) + contList.get(i).getBad());
                            lvlList.set(i, contList.get(i).getLevel_num());
                            totcalCntList0.set(i, totcalCntList0.get(i) + contList.get(i).getTotalcnt());
                            // 모든 참여자의 레벨 당 good, bad 값을 더함
                        }
                    }
                }
                for(int i=0; i < maxLevel; i++){ // 참여자 한명의 모든 레벨 데이터 불러오기
                    goodList.set(i, goodList0.get(i));
                    badList.set(i, badList0.get(i));
                    totcalCntList.set(i, totcalCntList0.get(i));
                    // 모든 참여자의 레벨 당 good, bad 값을 더함
                }
            }
            result.setGoodList(goodList);
            result.setBadList(badList);
            result.setLvlList(lvlList);
            result.setTotcalCntList(totcalCntList);
        }else{
            result.setGoodList(null);
            result.setBadList(null);
            result.setTotcalCntList(null);
        }
        return result;
    }

    // 미팅의 전체 집중도 수치 평균값(상단 반원 그래프)
    // 매개변수: AnalysisService getAllUserRate를 통해 나온 모든 레벨 당 전체 참여자 데이터 평균 리스트
    @Override
    public ConcentrationVO getMeetingRate(List<Integer> goodList, List<Integer> badList, List<Integer> totcalCntList) {
        // AnalysisService getAllUserRate의 값은 Map이기 때문에 매개변수에 goodList, badList를 나누어 적용해야 함.

        ConcentrationVO concentrationVO = new ConcentrationVO();

        Double Good = 0.0;
        Double Bad = 0.0;
        Integer resultGood = 0;
        Integer resultBad = 0;
        Integer resultTotcalCnt = 0;

        if(goodList != null && goodList.size() > 0 && badList != null && badList.size() > 0){
            for(int i=0; i<goodList.size(); i++){
                resultGood += goodList.get(i);
                resultBad += badList.get(i);
                resultTotcalCnt += totcalCntList.get(i);
                // 모든 레벨 당 데이터를 더함
            }
            Good = resultGood/(double)resultTotcalCnt * 100;
            Bad = resultBad/(double)resultTotcalCnt * 100;
        }

        concentrationVO.setGood((int)Math.round(Good)); // 모든 레벨당 데이터를 더한 값을 레벨 갯수로 나눈어 평균값 도출
        concentrationVO.setBad((int)Math.round(Bad));
        concentrationVO.setCameraOff((int)(100-(Math.round(Good) + Math.round(Bad))));
        // 100에서 good, bad의 평균값을 제외할 경우 남는 값이 cameraOff

        return concentrationVO;
    }


}
