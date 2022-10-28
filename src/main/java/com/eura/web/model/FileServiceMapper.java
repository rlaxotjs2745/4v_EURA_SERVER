package com.eura.web.model;

import org.apache.ibatis.annotations.Mapper;

import com.eura.web.model.DTO.MeetingVO;

@Mapper
public interface FileServiceMapper {
    public Integer addMeetFile(MeetingVO meetingVO) throws Exception;
}
