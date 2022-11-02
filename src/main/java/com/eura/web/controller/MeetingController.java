/*
 * ZOOM Meeting Controller
 * =======================
 * 20221102 - SungWoong
 */
package com.eura.web.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.service.MeetingService;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;
import com.eura.web.util.TokenJWT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mt")
public class MeetingController {
    private final UserService userService;
    private final MeetMapper meetMapper;
    private final FileServiceMapper fileServiceMapper;
    private final TokenJWT tokenJWT;
    private final MeetingService meetingService;

    @Value("${file.upload-dir}")
    private String filepath;

    @GetMapping("/uchk")
    public ResultVO chkUser(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();

            // 개인화 - 개인정보
            UserVO uInfo = userService.getUserInfo(1);
            if(uInfo!=null){
                _rs.put("ui_name", uInfo.getUser_name());
                _rs.put("ui_pic", uInfo.getUser_name());
            }
            
            // 개인화 - 다음일정 - 참여중인 미팅룸
            List<MeetingVO> mSi = meetMapper.getMyMeetShortList(1);
            ArrayList<Object> _mSrss = new ArrayList<Object>();
            for(MeetingVO rs0 : mSi){
                Map<String, Object> _mSrs = new HashMap<String, Object>();
                _mSrs.put("mt_idx", rs0.getIdx_meeting());
                _mSrs.put("mt_name", rs0.getMt_name());
                _mSrs.put("mt_status", rs0.getMt_status());
                _mSrs.put("mt_start_dt", rs0.getMt_start_dt());
                _mSrs.put("mt_end_dt", rs0.getMt_end_dt());
                _mSrss.add(_mSrs);
            }
            _rs.put("mt_meetShort", _mSrss);

            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }
}
