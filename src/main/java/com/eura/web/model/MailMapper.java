package com.eura.web.model;

import com.eura.web.model.DTO.MailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
   public void insertJoinEmail(MailVO mailSendVO);
}
