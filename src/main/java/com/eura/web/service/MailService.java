package com.eura.web.service;

import com.eura.web.model.DTO.MailVO;

public interface MailService {
    public String generateAuthNo(int num);

    public void insertJoinEmail(MailVO mailSendVO);

    public String getRamdomPassword();
}
