package com.eura.web.model.DTO;

import lombok.Data;

@Data
public class MailVO {
    int idx_mail_reserved;
    long idx_user;
    String receiver;
    String sendTime;
    String title;
    String content;
    int mail_type;
    private Integer idx_meeting_user_join;

}
