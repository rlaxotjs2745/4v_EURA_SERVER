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

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();
            ArrayList<Object> _rs0 = new ArrayList<Object>();
            ArrayList<Object> _rs1 = new ArrayList<Object>();
            
            meetingVO.setSessionid(meetingVO.getMcid());
            MeetingVO rrs = meetMapper.chkRoomInvite(meetingVO);
            if(rrs!=null){
                Map<String, Object> _mrss0 = new HashMap<String, Object>();
                Map<String, Object> _mrss1 = new HashMap<String, Object>();
                _mrss1.put("mt_name",rrs.getMt_name());
                _mrss1.put("mt_start_dt",rrs.getMt_start_dt());
                _mrss1.put("mt_end_dt",rrs.getMt_end_dt());
                _mrss1.put("mt_status",rrs.getMt_status());
                _mrss1.put("teacher_name",rrs.getHost_name());
                _mrss1.put("keepalive",CONSTANT.keepalive);
                _mrss1.put("mdatasec",CONSTANT.mdatasec);
                // _mrss1.put("rurl",CONSTANT.rurl);

                _mrss0.put("ishost",rrs.getIshost());
                _mrss0.put("user_name",rrs.getUser_name());
                _mrss0.put("session_name",rrs.getSessionid());
                _mrss0.put("token",meetingVO.getToken());

                _rs0.add(_mrss0);
                _rs1.add(_mrss1);

                _rs.put("session",_rs1);
                _rs.put("data",_rs0);

                resultVO.setData(_rs);
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }


    /**
     * ZOOM 미팅 참가 수신
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PutMapping(value="/join", consumes = "application/json", produces = "application/json")
    public ResultVO putJoinMeetLiveStart(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Integer rs = meetMapper.putMeetLiveStart(meetingVO);
            if(rs == 1){
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("참가 정보 수신을 완료하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * ZOOM 미팅 Keep Alive : 녹화/감정 분석 진행여부
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PutMapping(value="/alive", consumes = "application/json", produces = "application/json")
    public ResultVO putMeetAlive(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            if(meetingVO.getIsAnalysis() == null){
                meetingVO.setIs_alive(0);
            }else{
                meetingVO.setIs_alive(meetingVO.getIsAnalysis());
            }
            Integer rs = meetMapper.putMeetAlive(meetingVO);
            if(rs == 1){
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("정보 수신을 완료하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }
}
