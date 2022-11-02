package com.eura.web.service;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.util.MailSender;

@Service
public class MeetingService {
    private MeetMapper meetMapper;
    private MailSender mailSender;

    @Value("${file.upload-dir}")
    public String filepath;

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
