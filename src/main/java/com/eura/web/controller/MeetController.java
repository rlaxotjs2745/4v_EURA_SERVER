/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import org.springframework.web.bind.annotation.*;

import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/meet")
public class MeetController {
    private UserService userService;

    /**
     * 미팅룸 생성
     * @param meetingVO
     * @return
     */
    @PostMapping("/create")
    public @ResponseBody ResultVO save_room(@RequestBody MeetingVO meetingVO){
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
}
