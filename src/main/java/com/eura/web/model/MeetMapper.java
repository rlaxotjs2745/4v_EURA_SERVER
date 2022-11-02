package com.eura.web.model;

import com.eura.web.model.DTO.MeetingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetMapper {
    // 미팅룸 리스트
    public List<MeetingVO> getMeetingList(Integer idx_user);

    // 개인화 - 다음일정 - 참여중인 미팅룸
    public List<MeetingVO> getMyMeetShortList(Integer idx_user);

    // 개인화 - 나의 미팅룸 - 참여중인 미팅룸 총 수
    public Long getMyMeetListCount(Integer idx_user);

    // 개인화 - 나의 미팅룸 - 참여중인 미팅룸
    public List<MeetingVO> getMyMeetList(MeetingVO meetingVO);

    // 개인화 - 지난 미팅
    public List<MeetingVO> getMyMeetEndList(MeetingVO meetingVO);

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

    // 미팅룸 첨부 파일 리스트
    public List<MeetingVO> getMeetFiles(MeetingVO meetingVO) throws Exception;

    // 미팅룸 참여자 리스트
    public List<MeetingVO> getMeetInvites(MeetingVO meetingVO) throws Exception;

    // 미팅룸 공개
    public Integer putMeetOpen(MeetingVO meetingVO) throws Exception;

    // 미팅룸 비공개
    public Integer putMeetClose(MeetingVO meetingVO) throws Exception;

    // 미팅 취소
    public Integer putMeetCacncel(MeetingVO meetingVO) throws Exception;

    // 미팅룸 삭제하기
    public Integer deleteMeet(MeetingVO meetingVO) throws Exception;

    // 미팅 시작하기
    public Integer putMeetLiveStart(MeetingVO meetingVO) throws Exception;

    // 미팅 들어가기
    public Integer putMeetLiveJoin(MeetingVO meetingVO) throws Exception;

    // 미팅에 참여중인지 확인
    public MeetingVO chkMeetLiveJoin(MeetingVO meetingVO) throws Exception;

    // 미팅 시간 중복 체크
    public MeetingVO chkRoomDupTime(MeetingVO meetingVO) throws Exception;

    // 일 단위 중복 체크
    public MeetingVO chkRoomDupDate(MeetingVO meetingVO) throws Exception;
}
