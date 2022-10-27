package com.eura.web.model.DTO;

import lombok.Data;

@Data
public class PagedUserListVO {
    long idx_user;
    String user_id;
    String user_name;
    String mphone_num;
}
