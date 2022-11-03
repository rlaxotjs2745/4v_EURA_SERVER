package com.eura.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.util.CONSTANT;
import com.eura.web.util.MailSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeetingService {
    @Autowired
    private MeetMapper meetMapper;
    
    private MailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private FileServiceMapper fileServiceMapper;

    @Value("${file.upload-dir}")
    public String filepath;

    /**
     * 미팅룸 생성
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    public ResultVO createMeetRoom(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        // log.info(meetingVO.getMt_name());
        Integer rs = meetMapper.meet_create(meetingVO);
        if(rs > 0){
            UserVO uInfo = userService.getUserInfo(meetingVO.getIdx_user());

            // 미팅룸 호스트 참여자 리스트에 저장
            MeetingVO es = new MeetingVO();
            es.setIdx_meeting(meetingVO.getIdx_meeting());
            es.setIdx_user(uInfo.getIdx_user());
            es.setUser_email(uInfo.getUser_id());
            meetMapper.meet_invite(es);

            // 미팅룸 참여자 리스트 저장
            if(meetingVO.getMt_invite_email() != null && meetingVO.getMt_invite_email() != ""){
                String[] inEmail = meetingVO.getMt_invite_email().split(",");
                for(String uemail : inEmail){
                    if(!uemail.isEmpty()){
                        MeetingVO ee = new MeetingVO();
                        ee.setIdx_meeting(meetingVO.getIdx_meeting());
                        ee.setUser_email(uemail);
                        meetMapper.meet_invite(ee);
                    }
                }
            }

            Map<String, Object> _rs = new HashMap<String, Object>();
            _rs.put("key",meetingVO.getIdx_meeting());
            resultVO.setData(_rs);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸을 생성하였습니다.");
        }else{
            resultVO.setResult_str("미팅룸 생성에 실패하였습니다.");
        }
        return resultVO;
    }

    /**
     * 메일폼 추출
     * @param _stat
     * @return
     * @throws Exception
     */
    public String getMailForm(Integer _stat) throws Exception {
        String mail = "";
        if(_stat==1){
            mail = "mail_alarm_start.html";
        }
        if(_stat==2){
            mail = "mail_alarm_30min.html";
        }
        if(_stat==3){
            mail = "mail_alarm_cancel.html";
        }
        if(_stat==4){
            mail = "mail_alarm_open.html";
        }
        String _fpath = filepath + "/html/" + mail;
        FileInputStream fis = new FileInputStream(_fpath);
        return IOUtils.toString(fis, "UTF-8");
    }

    /**
     * 참가자 이메일 전송
     * @param meetingVO
     * @param rrs
     * @param _mFTyp
     * @throws Exception
     */
    public void sendMailMeetInvites(MeetingVO meetingVO, MeetingVO rrs, Integer _mFTyp) throws Exception {
        // 이메일 데이터 호출
        String _data = getMailForm(_mFTyp);

        // 참가자 이메일 전송
        List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);
        for(MeetingVO _ss : irs){
            String _unm = _ss.getUser_name();
            if(_ss.getUser_name().equals("") || _ss.getUser_name().isEmpty()){
                _unm = _ss.getUser_email();
            }
            String _ebody = _data.replace("${MEETNAME}", rrs.getMt_name())
                                .replace("${USERNAME}", _unm);
            mailSender.sender(_ss.getUser_email(), rrs.getMt_name(), _ebody);
        }
    }
}
