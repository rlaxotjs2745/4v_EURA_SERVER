package com.eura.web.model;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.MeetingVO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnalysisMapper {
    //미팅 중 감정 데이터 저장
    public Long insertAnalysisData(AnalysisVO analysisVO);

    public AnalysisVO getAnalysisData(MeetingVO meetingVO) throws Exception;

    public List<AnalysisVO> getUserAnalysisData(MeetingVO meetingVO);

    public AnalysisVO getAnalysisFirstData(MeetingVO meetingVO) throws Exception;
}
