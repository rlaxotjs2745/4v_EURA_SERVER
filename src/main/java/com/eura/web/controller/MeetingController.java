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

import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.util.CONSTANT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mt")
public class MeetingController {
    private final MeetMapper meetMapper;

    @Value("${file.upload-dir}")
    private String filepath;

    /**
     * APP으로부터 사용자 확인 후 미팅룸 정보를 전달
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping(value="/uchk", consumes = "application/json", produces = "application/json")
    public ResultVO chkUser(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");
        log.info("mcid: {}", meetingVO.getMcid());

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();

            meetingVO.setSessionid(meetingVO.getMcid());
            MeetingVO rrs = meetMapper.chkRoomInvite(meetingVO);
            if(rrs!=null){
                _rs.put("mt_name",rrs.getMt_name());
                _rs.put("mt_start_dt",rrs.getMt_start_dt());
                _rs.put("mt_end_dt",rrs.getMt_end_dt());
                _rs.put("mt_status",rrs.getMt_status());
                _rs.put("host_name",rrs.getHost_name());
                _rs.put("ishost",rrs.getIshost());
                _rs.put("user_name",rrs.getUser_name());
                _rs.put("session_name",rrs.getSessionid());
                _rs.put("keepalive",CONSTANT.keepalive);
                _rs.put("mdatasec",CONSTANT.mdatasec);
                _rs.put("rurl",CONSTANT.rurl);
                resultVO.setData(_rs);
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }
}
