package com.eura.web.model;

import com.eura.web.model.DTO.PagedUserListVO;
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
    public List<UserVO> getUserPagingList(Integer page_num, Integer amount, String order_field);
    public List<PagedUserListVO> getUserSpecialInfoPagingList(Integer page_num, Integer amount, String order_field);

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

    public long findUserIdx(String user_id);

    public void updateUserPw(UserVO userVo);
}
