package com.eura.web.model;

import com.eura.web.model.DTO.MeetingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetMapper {
    public List<MeetingVO> getMeetingList(Integer idx_user);

    public Integer meet_create(MeetingVO meetingVO) throws Exception;
}
