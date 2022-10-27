package com.eura.web.controller;

import com.eura.web.model.DTO.*;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class WebAPIController {
    @Resource(name = "userService")
    UserService userService;


    @RequestMapping(value = "/join_default",method = RequestMethod.POST)
    public @ResponseBody
    ResultVO join_default(HttpSession session,
                           @RequestBody UserVO userVo){
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

    @RequestMapping(value = "/save_room",method = RequestMethod.POST)
    public @ResponseBody
    ResultVO save_room(
                          @RequestBody MeetingVO meetingVO){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        if(meetingVO!=null){

            UserVO findUser = userService.getUserInfo(meetingVO.getIdx_user());
            if(findUser!=null){
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("Success");
            }
        }
        return resultVO;
    }

    @RequestMapping(value = "/user_id_check",method = RequestMethod.POST)
    public @ResponseBody
    ResultVO user_id_check(HttpSession session,
                           @RequestBody UserVO userVo){
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

    @RequestMapping(value = "/join_confirm",method = RequestMethod.POST)
    public @ResponseBody
    ResultVO  join_confirm(HttpSession session,
                           @RequestBody UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("회원가입에 필요한 데이터가 없습니다");
        resultVO.setResult_code("ERROR_1000");

        if(userVo!=null && userVo.getUser_id() !=null
                && userVo.getUser_id() !=null){

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
