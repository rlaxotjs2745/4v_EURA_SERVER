package com.eura.web.model;

import com.eura.web.model.DTO.MeetingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetMapper {
    List<MeetingVO> getMeetingList(int idx_user);
}
