package com.eura.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.DTO.*;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;

import lombok.RequiredArgsConstructor;

// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
// import java.util.*;

@RequiredArgsConstructor
@RestController
public class WebAPIController {
    private final UserService userService;

    /**
     * 회원 가입 데이터 저장
     * @param session
     * @param userVo
     * @return JSON
     */
    @PostMapping("/join_default")
    public ResultVO join_default(HttpServletRequest req, HttpSession session, UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");
        if(userVo.getUser_name()!=null &&
            userVo.getUser_id()!=null&&
            userVo.getUser_pwd()!=null){

            //1.data valic check
                //1.1.id email 규칙
                //1.2. psw 규칙

            //2.id duplicate check

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("Success");
        }
        return resultVO;
    }

    /**
     * 회원 아이디 검사
     * @param session
     * @param userVo
     * @return JSON
     */
    @PostMapping("/user_id_check")
    public ResultVO user_id_check(HttpServletRequest req, HttpSession session, UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("아이디 형식을 확인해 주세요");
        resultVO.setResult_code("ERROR_1000");

        if(userVo!=null && userVo.getUser_id() !=null){
            UserVO userVO = userService.findUserById(userVo.getUser_id());
            if(userVO !=null){
                resultVO.setResult_str("아이디를 사용할 수 없습니다");
                resultVO.setResult_code("ERROR_1001");
            }
            else {
                resultVO.setResult_str("아이디를 사용할 수 있습니다");
                resultVO.setResult_code("SUCCESS");
            }
        }
        return resultVO;
    }

    @PostMapping("/join_confirm")
    public ResultVO join_confirm(HttpServletRequest req, HttpSession session, UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("회원가입에 필요한 데이터가 없습니다");
        resultVO.setResult_code("ERROR_1000");

        if(userVo!=null && userVo.getUser_id() !=null && userVo.getUser_id() !=null){
            UserVO findUserVo = userService.findUserById(userVo.getUser_id());
            if(findUserVo !=null){
                resultVO.setResult_str("이미 사용중인 아이디입니다");
                resultVO.setResult_code("ERROR_1001");
            }
            else {
                long idx_user = userService.join(userVo);
                if(idx_user>0){
                    resultVO.setResult_str("가입되었습니다");
                    resultVO.setResult_code(CONSTANT.success);
                }
            }
        }//필수 데이터 체크 if
        return resultVO;
    }
}
