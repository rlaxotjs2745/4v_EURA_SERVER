package com.eura.web.model;

import com.eura.web.model.DTO.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface UserMapper {
    public UserVO getUserInfoById(String user_id);
    public UserVO getUserInfoByIdx(Integer idx_user) throws Exception;
    public UserVO getAuthUser(String user_id, String user_pwd);
    //public UserVO getAuthUser(ParamVO user);
    public UserVO getUserInfoByNameAndMPhoneNum(String user_name, String mphone_num);

    public List<UserVO> getAllUserList();

    public Integer getUserCount(Integer user_type);//99 모든 유저

    public int insertUserInfo(UserVO userVO);

    public Integer updateUserInfo(UserVO userVO);
    public Integer updateUserInfoForLogout(String user_id);

    public Integer deleteUserInfoByID(String user_id);
    public Integer deleteUserInfoByIdx(Long idx_user);

    public UserVO selectUserProfileFile(long idx_user);

    public void setProfile_y(UserVO findUserVO);

    public void updateAuthKey(UserVO userVo);

    public void updateAuthStatus(UserVO userVo);

    public Integer findUserIdx(String user_id);

    public void updateUserPw(UserVO userVo);

    // 회원 로그인 히스토리
    public void putUserLoginHistory(UserVO userVo);

    // 참석자 검색
    public List<UserVO> getUserSearch(UserVO userVo);
}
