package com.eura.web.service;

import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.CustomUser;
import com.eura.web.model.DTO.ParamVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eura.web.util.CONSTANT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserVO getAuthUser(String user_id, String user_pw){
        UserVO findUser = userMapper.getAuthUser(user_id, user_pw);
        return findUser;
    }

    public UserVO getUserInfo(Integer idx_user) throws Exception {
        UserVO rs = null;
        try {
            rs = userMapper.getUserInfoByIdx(idx_user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public UserVO findUserById(String user_id){
        UserVO findUser = userMapper.getUserInfoById( user_id);
        return findUser;
    }

    public UserVO login(String user_id, String user_pwd){
/*        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("user_id",user_id);
        paramMap.put("user_pw",user_pw);*/
        ParamVO paramVo = new ParamVO();
        paramVo.setUser_id(user_id);
        paramVo.setUser_pwd(user_pwd);

        UserVO findUserVO =userMapper.getAuthUser(user_id, user_pwd);
        return findUserVO;
    }

    public ResultVO logout(String user_id){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.success);
        resultVO.setResult_str("로그아웃 되었습니다");

        Integer upResult = userMapper.updateUserInfoForLogout(user_id);

        if(upResult!=1)
        {
            resultVO.setResult_code(CONSTANT.fail);
            resultVO.setResult_str("사용자를 찾을 수 없습니다");
        }

        return resultVO;

    }

    public Integer join(UserVO userVO){

        SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String now_date = fm.format(Calendar.getInstance().getTime());

        userVO.setReg_dt(now_date);
        userVO.setTemp_pw_issue_dt(now_date);
        userVO.setLast_upd_dt(now_date);
        userVO.setLast_pw_upd_dt(now_date);

        userMapper.insertUserInfo(userVO);
        Integer ret_idx = userMapper.findUserIdx(userVO.getUser_id());

        return ret_idx;
    }

    public UserVO findUserID(String user_name, String mphone_number){
        UserVO findUser = userMapper.getUserInfoByNameAndMPhoneNum(user_name,mphone_number);
        return findUser;
    }

    public UserVO findUserPW(String user_id){
        UserVO findUser = userMapper.getUserInfoById("test04");
        return findUser;
    }

    public ResultVO checkIdDuplicate(String user_id){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.success);
        resultVO.setResult_str("사용가능합니다");

        UserVO findUser = userMapper.getUserInfoById(user_id);

        if(findUser !=null)
        {
            resultVO.setResult_code(CONSTANT.fail);
            resultVO.setResult_str("이미 등록되었습니다");
        }

        return resultVO;
    }

    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        UserVO userVO = userMapper.getUserInfoById(user_id);
        if(userVO == null){
            userVO = new UserVO();
            //userVO.set(CONSTANT.ROLE_GUEST);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"/*CONSTANT.getRoleString(adminVO.getAdmin_type())*/));

        return new CustomUser(userVO.getUser_id(), userVO.getUser_pwd(), authorities, userVO.getUser_name());
    }

    public UserVO getUserProfileFile(long idx_user) {
        return userMapper.selectUserProfileFile(idx_user);
    }

    public void addNewProfile(UserVO findUserVO) {
        userMapper.setProfile_y(findUserVO);
    }

    public void updateAuthKey(UserVO userVo) {
        userMapper.updateAuthKey(userVo);
    }

    public void updateAuthStatus(UserVO userVo) {
        userMapper.updateAuthStatus(userVo);
    }

    public void updateUserPw(UserVO userVo) {
        userMapper.updateUserPw(userVo);
    }

}
