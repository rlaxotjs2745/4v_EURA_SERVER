package com.eura.web.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ProfileInfoVO;

@Mapper
public interface FileServiceMapper {
    // 미팅룸 첨부파일 저장
    public Integer addMeetFile(MeetingVO meetingVO) throws Exception;

    // 미팅룸 첨부파일 삭제
    public Integer delMeetFile(MeetingVO meetingVO) throws Exception;

    // 미팅룸 첨부파일 정보
    public MeetingVO getMeetFile(MeetingVO meetingVO) throws Exception;

    // 미팅 동영상 파일 저장
    public Integer addMeetMovieFile(MeetingVO meetingVO) throws Exception;

    // 미팅 동영상 파일 리스트
    public List<MeetingVO> getMeetMovieFile(MeetingVO meetingVO) throws Exception;

    // 감정 분석 데이터 내의 파일 저장
    public Integer addEmotionFile(MeetingVO meetingVO) throws Exception;

    //프로필사진 저장
    public void insertProfileInfo(ProfileInfoVO profileInfo);

    //프로필 정보 가져요기
    public ProfileInfoVO selectUserProfileFile(Integer idx_user);

    //프로필사진 수정
    public void updateProfileInfo(ProfileInfoVO profileInfo);
}
