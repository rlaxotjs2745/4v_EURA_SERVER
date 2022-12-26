package com.eura.web.model;

import com.eura.web.model.DTO.MailVO;
import com.eura.web.model.DTO.MeetingVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
   public void insertJoinEmail(MailVO mailSendVO);

   // 미팅룸 참여자 리스트
   public List<MeetingVO> getMeetInvites(MeetingVO paramVo);

   // 메일 발송 완료 처리
   public void saveSendMail(MailVO paramVo);

   public MeetingVO chkSendMail(MeetingVO paramVo);
}
