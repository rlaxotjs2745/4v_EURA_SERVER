package com.eura.web.service;

import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
    public UserVO getAuthUser(String user_id, String user_pwd);
    public UserVO getUserInfo(long idx_user);
    public UserVO login(String user_id, String user_pwd);
    public ResultVO logout(String user_id);
    public long join(UserVO userVO);
    public UserVO findUserById(String user_id);
    public UserVO findUserID(String user_name, String mphone_number);
    public UserVO findUserPW(String user_id);
    public ResultVO checkIdDuplicate(String user_id);
}
