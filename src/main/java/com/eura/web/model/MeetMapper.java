package com.eura.web.model;

import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.UserVO;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MeetMapper {
    // 미팅룸 리스트
    public List<MeetingVO> getMeetingList(Integer idx_user);

    // 개인화 - 다음일정 - 참여중인 미팅룸
    public List<MeetingVO> getMyMeetShortList(Integer idx_user);

    // 미팅 달력
    public List<MeetingVO> getMyMeetCalendarList(MeetingVO meetingVO);

    // 개인화 - 나의 미팅룸 - 참여중인 미팅룸 총 수
    public Long getMyMeetListCount(Integer idx_user);

    // 개인화 - 나의 미팅룸 - 참여중인 미팅룸
    public List<MeetingVO> getMyMeetList(MeetingVO meetingVO);

    // 개인화 - 지난 미팅 - 지난 미팅룸 총 수
    public Long getMyMeetEndListCount(Integer idx_user);

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

    // 영상 파일 리스트
    public List<MeetingVO> getMeetMovieFiles(MeetingVO meetingVO) throws Exception;

    // 미팅룸 참여자 리스트
    public List<MeetingVO> getMeetInvites(MeetingVO meetingVO) throws Exception;

    // 미팅룸 참여자 리스트 카운트
    public Integer getMeetInvitesCnt(MeetingVO meetingVO) throws Exception;

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

    // ZOOM 미팅 참가 수신
    public Integer putJoinMeetLiveStart(MeetingVO meetingVO) throws Exception;

    // ZOOM 미팅 Keep Alive : 녹화/감정 분석 진행여부
    public Integer putMeetAlive(MeetingVO meetingVO) throws Exception;

    // 호스트가 설정한 줌의 환경 설정 가져오기
    public MeetingVO getMeetAlive(MeetingVO meetingVO) throws Exception;

    // 미팅 들어가기
    public Integer putMeetLiveJoin(MeetingVO meetingVO) throws Exception;

    // 미팅에 참여중인지 확인
    public MeetingVO chkMeetLiveJoin(MeetingVO meetingVO) throws Exception;

    // 미팅 시간 중복 체크
    public MeetingVO chkRoomDupTime(MeetingVO meetingVO) throws Exception;

    // 일 단위 중복 체크
    public MeetingVO chkRoomDupDate(MeetingVO meetingVO) throws Exception;

    // APP이 mcid, token으로 정보 요청 시
    public MeetingVO chkRoomInvite(MeetingVO meetingVO) throws Exception;

    // 현재 실행되는 미팅 끝내기
    public void closeMeet(MeetingVO meetingVO) throws Exception;

    // APP 미팅룸 종료 시 영상 파일 받기
    public Integer endMeetLive(MeetingVO meetingVO) throws Exception;

    public MeetingVO getMeetLiveIdx(MeetingVO meetingVO) throws Exception;

    // 미팅 참가자 INDEX 정보 추출
    public MeetingVO getMeetInvite(MeetingVO meetingVO) throws Exception;

    // 참여자에서 본인이 있다면 참여자 정보와 연동
    public Integer chkMeetInviteChain(UserVO paramVo) throws Exception;

    //유저 이메일이 기존에 있는 참가자인지 확인
    public Integer getMeetInviteUser(MeetingVO meetingVO) throws Exception;
}
