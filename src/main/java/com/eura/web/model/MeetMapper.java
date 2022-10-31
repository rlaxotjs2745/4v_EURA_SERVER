package com.eura.web.model;

import com.eura.web.model.DTO.MeetingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetMapper {
    public List<MeetingVO> getMeetingList(Integer idx_user);

    // 미팅룸 생성
    public Integer meet_create(MeetingVO meetingVO) throws Exception;

    // 참여자 리스트 저장
    public Integer meet_invite(MeetingVO meetingVO) throws Exception;

    // 미팅룸 수정
    public Integer meet_modify(MeetingVO meetingVO) throws Exception;

    // 미팅룸 참여자 삭제
    public Integer meet_invite_del(MeetingVO meetingVO) throws Exception;

    // 미팅 메인
    public MeetingVO meet_main(MeetingVO meetingVO) throws Exception;

    // 미팅룸 정보
    public MeetingVO getRoomInfo(MeetingVO meetingVO) throws Exception;

    public List<MeetingVO> getMeetFiles(MeetingVO meetingVO) throws Exception;

    public List<MeetingVO> getMeetInvites(MeetingVO meetingVO) throws Exception;
}
