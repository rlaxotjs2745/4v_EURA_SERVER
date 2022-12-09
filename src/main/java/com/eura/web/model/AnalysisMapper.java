package com.eura.web.model;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.MeetResultVO;
import com.eura.web.model.DTO.MeetingVO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisMapper {
    //미팅 중 감정 데이터 저장
    public Long insertAnalysisData(AnalysisVO analysisVO);

    public void insertAnalysisDataUserRate(AnalysisVO analysisVO);

    public AnalysisVO getAnalysisData(MeetingVO meetingVO) throws Exception;

    public List<AnalysisVO> getUserAnalysisData(MeetingVO meetingVO);

    // 분석 요약
    public MeetResultVO getMeetResultData(MeetingVO meetingVO) throws Exception;

    // 분석 요약 저장
    public Integer saveMeetResultData(ConcentrationVO meetingVO) throws Exception;

    // 개인 막대그래프용 데이터
    public List<ConcentrationVO> getPersoanlBarData(MeetingVO meetingVO) throws Exception;

    // 개인 막대그래프용 데이터 저장
    public Integer savePersoanlBarData(Map<String, Object> meetingVO) throws Exception;

    // 개인 분석요약 데이터
    public List<ConcentrationVO> getPersoanlTotalData(MeetingVO meetingVO) throws Exception;

    // 개인 분석요약 데이터 저장
    public Integer savePersoanlTotalData(Map<String, Object> meetingVO) throws Exception;

    // 전체 막대그래프용 데이터
    public List<ConcentrationVO> getTotalBarData(MeetingVO meetingVO) throws Exception;

    // 전체 막대그래프용 데이터 저장
    public Integer saveTotalBarData(Map<String, Object> meetingVO) throws Exception;
}
