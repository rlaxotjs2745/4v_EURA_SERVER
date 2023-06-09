package com.eura.web.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.base.BaseController;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MailMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.MailVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.util.CONSTANT;
import com.eura.web.util.MailSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeetingService extends BaseController {
    @Autowired
    private MeetMapper meetMapper;

    @Autowired
    private MailMapper mailMapper;
    
    @Autowired
    private MailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FileServiceMapper fileServiceMapper;

    @Value("${srvinfo}")
    public String srvinfo;

    @Value("${file.upload-dir}")
    public String filepath;

    @Value("${domain}")
    public String domain;

    @Value("${w3domain}")
    public String w3domain;

    /**
     * 미팅룸 생성
     * @param req
     * @param meetingVO
     * @return ResultVO
     * @throws Exception
     */
    public ResultVO createMeetRoom(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

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
            this.saveMeetInvite(meetingVO);

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
     * 미팅룸 참여자 리스트 저장
     * @param meetingVO
     * @throws Exception
     */
    public void saveMeetInvite(MeetingVO meetingVO) throws Exception {

        MeetingVO orM = meetMapper.getRoomInfo(meetingVO);

        if(meetingVO.getMt_invite_email() != null && meetingVO.getMt_invite_email() != ""){
            String[] inEmail = meetingVO.getMt_invite_email().split(",");
            for(String uemail : inEmail){
                if(!uemail.isEmpty()){
                    MeetingVO ee = new MeetingVO();
                    ee.setIdx_meeting(meetingVO.getIdx_meeting());
                    ee.setUser_email(uemail);
                    ee.setMt_start_dt(meetingVO.getMt_start_dt());

                    int num= meetMapper.getMeetInviteUser(ee);
                    meetMapper.meet_invite(ee);

                    //공개된 상태에서 새로 추가된 회원은 초대메일 발송
                    if(orM.getMt_status() == 1 && num == 0){
                        sendModifyMail(ee, 4);
                    }
                }
            }
        }
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
            mail = "mail_alarm_invite.html";
        }
        if(_stat==5){
            mail = "mail_auth.html";
        }
        if(_stat==6){
            mail = "mail_password.html";
        }
        if(_stat==7){
            mail = "mail_alarm_modify.html";
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

    public void sendMail(MeetingVO meetingVO, MeetingVO rrs, Integer _mFTyp) throws Exception {
        // 이메일 데이터 호출
        String _data = getMailForm(_mFTyp);
        String _ebody = _data.replace("${DOMAIN}", w3domain);
        String _sebody = "";

        // 회원가입 인증 메일
        if(_mFTyp==5){
            _sebody = _ebody.replace("${URL}", w3domain + "/login?confirm=true&email=" + meetingVO.getUser_email() + "&authKey=" + meetingVO.getAuthKey())
                            .replace("${USEREMAIL}", meetingVO.getUser_email())
                            .replace("${USERNAME}", meetingVO.getUser_name());
            try {
                mailSender.sender(meetingVO.getUser_email(), meetingVO.getTitle(), _sebody);
            } catch (Exception e) {
                e.printStackTrace();
            }

        // 임시 비밀번호
        }else if(_mFTyp==6){
            _sebody = _ebody.replace("${PASSWD}", meetingVO.getTemp_pw())
                            .replace("${USEREMAIL}", meetingVO.getUser_email())
                            .replace("${USERNAME}", meetingVO.getUser_name());
            mailSender.sender(meetingVO.getUser_email(), meetingVO.getTitle(), _sebody);

        // 참가자 이메일 전송
        }else{
            List<MeetingVO> irs = mailMapper.getMeetInvites(meetingVO);
            String _subject = "";
            for(MeetingVO _ss : irs){
                String _unm = _ss.getUser_name();
                if(StringUtils.isEmpty(_unm)){
                    _unm = _ss.getUser_email();
                }

                if(_mFTyp==1 || _mFTyp==2){
                    _sebody = _ebody.replace("${USERNAME}", _unm)
                            .replace("${USEREMAIL}", _ss.getUser_email())
                            .replace("${MEETNAME}", rrs.getMt_name())
                            .replace("${URL}", w3domain + "/meetingroom/" + meetingVO.getIdx_meeting());
                    if(_mFTyp==1){
                        _subject = " 미팅 시작 안내입니다.";
                    }
                    if(_mFTyp==2){
                        _subject = " 미팅 시작 30분전 안내입니다.";
                    }
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                Date _mod = dateFormat.parse(rrs.getMt_start_dt());
                dateFormat.applyPattern("yyyy년 MM월 dd일, a hh:mm");

                if(_mFTyp==3 || _mFTyp==4){
                    _sebody = _ebody.replace("${USERNAME}", _unm)
                            .replace("${USEREMAIL}", _ss.getUser_email())
                            .replace("${MEETNAME}", rrs.getMt_name())
                            .replace("${URL}", w3domain + "/meetingroom/" + meetingVO.getIdx_meeting())
                            .replace("${MEETORIDATE}", dateFormat.format(_mod));

                    if(_mFTyp==3){
                        _subject = " 미팅이 취소되었습니다.";
                    }
                    if(_mFTyp==4){
                        _subject = " 미팅에 초대되었습니다.";
                    }
                }
                if(_mFTyp==7){
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
                    Date _md = dateFormat.parse(meetingVO.getMt_start_dt());
                    dateFormat.applyPattern("yyyy년 MM월 dd일, a hh:mm");

                    _sebody = _ebody.replace("${USERNAME}", _unm)
                            .replace("${USEREMAIL}", _ss.getUser_email())
                            .replace("${MEETNAME}", rrs.getMt_name())
                            .replace("${URL}", w3domain + "/meetingroom/" + meetingVO.getIdx_meeting())
                            .replace("${MEETDATE}", dateFormat.format(_md))
                            .replace("${MEETORIDATE}", dateFormat.format(_mod));
                    _subject = " 미팅 일정이 변경되었습니다.";
                }

                mailSender.sender(_ss.getUser_email(), "[EURA] \"" + rrs.getMt_name() + "\"" + _subject, _sebody);
                MailVO paramVo = new MailVO();
                paramVo.setIdx_user(_ss.getIdx_user());
                paramVo.setReceiver(_ss.getUser_email());
                paramVo.setTitle("[EURA] " + rrs.getMt_name());
                paramVo.setContent(_sebody);
                paramVo.setMail_type(_mFTyp);
                paramVo.setIdx_meeting_user_join(_ss.getIdx_meeting_user_join());
                mailMapper.saveSendMail(paramVo);   // 보낸 메일 저장
            }
        }
    }

    public void sendModifyMail(MeetingVO ee, Integer _mFTyp) throws Exception {
        MeetingVO meetVO = mailMapper.chkSendMail(ee);
        if(meetVO!=null){
            // 이메일 데이터 호출
            String _data = getMailForm(_mFTyp);
            String _ebody = _data.replace("${DOMAIN}", w3domain);

            UserVO userVO = userService.findUserById(ee.getUser_email());
            MeetingVO meetingVO = meetMapper.getRoomInfo(ee);
            String _subject = "";
            String _unm = "";
            Integer _uidx = 0;

            if(userVO!=null){
                _uidx = userVO.getIdx_user();
                _unm = userVO.getUser_name();
            } else {
                _unm = ee.getUser_email();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);
            Date _md = dateFormat.parse(meetingVO.getMt_start_dt());
            dateFormat.applyPattern("yyyy년 MM월 dd일, a hh:mm");

            String _sebody = _ebody.replace("${USERNAME}", _unm)
                    .replace("${USEREMAIL}", ee.getUser_email())
                    .replace("${MEETNAME}", meetingVO.getMt_name())
                    .replace("${MEETORIDATE}", dateFormat.format(_md))
                    .replace("${URL}", w3domain + "/meetingroom/" + meetingVO.getIdx_meeting());

            if(_mFTyp==3) {
                _subject = " 미팅이 취소되었습니다.";
            }

            if(_mFTyp==4) {
                _subject = " 미팅에 초대되었습니다.";
            }

            mailSender.sender(ee.getUser_email(), "[EURA] \"" + meetingVO.getMt_name() + "\"" + _subject, _sebody);

            MailVO paramVo = new MailVO();
            paramVo.setIdx_user(_uidx);
            paramVo.setReceiver(ee.getUser_email());
            paramVo.setTitle("[EURA] " + meetingVO.getMt_name());
            paramVo.setContent(_sebody);
            paramVo.setMail_type(_mFTyp);
            paramVo.setIdx_meeting_user_join(meetVO.getIdx_meeting_user_join());
            mailMapper.saveSendMail(paramVo);   // 보낸 메일 저장
        }
    }


    /**
     * 파일 저장
     * @param req
     * @param _idx
     * @return MeetingVO
     * @throws Exception
     */
    public MeetingVO saveFile(MultipartFile _file, String _path) throws Exception {
        MeetingVO paramVo = new MeetingVO();
        String originFileName = _file.getOriginalFilename();
        if(originFileName != ""){
            try { // 파일생성
                if(srvinfo.equals("dev")){
                    String fullpath = this.filepath + _path;
                    File fileDir = new File(fullpath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    _file.transferTo(new File(fullpath, originFileName));
                }else{
                    s3Service.upload(_file, "upload" + _path + originFileName);
                }
                paramVo.setFile_path(_path);
                paramVo.setFile_name(originFileName);
                paramVo.setFile_size(_file.getSize());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paramVo;
    }

    /**
     * 미팅룸 첨부파일 저장
     * @param req
     * @param _idx
     * @return
     * @throws Exception
     */
    public List<MeetingVO> meetFileSave(MultipartHttpServletRequest req, Integer _idx) throws Exception {
        List<MeetingVO> _frss = new ArrayList<>();

        List<MultipartFile> fileList = req.getFiles("file");
        if(fileList.size()>0){
            if(req.getFiles("file").get(0).getSize() != 0){
                fileList = req.getFiles("file");
            }
            String path = "/meetroom/" + _idx + "/";
            String fullpath = this.filepath + path;
            if(srvinfo.equals("dev")){
                File fileDir = new File(fullpath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
            }

            for(MultipartFile mf : fileList) {
                String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                try { // 파일생성
                    if(srvinfo.equals("dev")){
                        mf.transferTo(new File(fullpath, originFileName));
                    }else{
                        s3Service.upload(mf, "upload" + path + originFileName);
                    }
                    MeetingVO paramVo = new MeetingVO();
                    paramVo.setIdx_meeting(_idx);
                    paramVo.setFile_path(path);
                    paramVo.setFile_name(originFileName);
                    paramVo.setFile_size(mf.getSize());
                    fileServiceMapper.addMeetFile(paramVo);
                    _frss.add(paramVo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return _frss;
    }

    /**
     * 미팅룸 첨부파일 복사
     * @param _idx
     * @param _frss
     * @throws Exception
     */
    public void meetFileCopy(Integer _idx, List<MeetingVO> _frss) throws Exception {
        try {
            String path = "/meetroom/" + _idx + "/";
            String fullpath = this.filepath + path;
            if(srvinfo.equals("dev")){
                File fileDir = new File(fullpath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
            }
            for(MeetingVO _frsa : _frss){
                String _sfnm = _frsa.getFile_name();
                Long _sfze = Long.valueOf(_frsa.getFile_size().toString());
                if(srvinfo.equals("dev")){
                    File file = new File(this.filepath + _frsa.getFile_path() + _sfnm);
                    File newFile = new File(fullpath + _sfnm);
                    FileUtils.copyFile(file, newFile);
                }else{
                    s3Service.copy("upload"+_frsa.getFile_path() + _sfnm, "upload" + path + _sfnm);
                }

                MeetingVO paramVo = new MeetingVO();
                paramVo.setIdx_meeting(_idx);
                paramVo.setFile_path(path);
                paramVo.setFile_name(_sfnm);
                paramVo.setFile_size(_sfze);
                fileServiceMapper.addMeetFile(paramVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 일,월,년 단위 중복 체크
     * @param _tp = 1:일,4:월
     * @param _dayChk
     * @param _cnt
     * @param meetingVO
     * @return Integer
     * @throws Exception
     */
    public Integer chkRoomDup(Integer _tp, Integer _dayChk, Integer _cnt, MeetingVO meetingVO) throws Exception{
        String _sd = meetingVO.getMt_start_dt();
        String _ed = meetingVO.getMt_end_dt();

        if(_cnt>0){
            if(_tp!=4) {
                for(Integer i=0;i<_cnt;i++){
                    String _sdate = getCalDate(_sd, 0, 0, 0);
                    String _edate = getCalDate(_ed, 0, 0, 0);
                    if(_tp==1){
                        _sdate = getCalDate(_sd, 0, 0, i);
                        _edate = getCalDate(_ed, 0, 0, i);
                    }
                /* else if(_tp==4){
                    _sdate = getCalDate(_sd, i, 0, 0);
                    _edate = getCalDate(_ed, i, 0, 0);
                } */
                    meetingVO.setMt_start_dt(_sdate);
                    meetingVO.setMt_end_dt(_edate);
                    MeetingVO _chk = meetMapper.chkRoomDupDate(meetingVO);
                    _dayChk=_dayChk+_chk.getChkcnt();
                }
            } else {    // 월 주기
                String _sdate = getCalDate(_sd, 0, 0, 0);
                String _edate = getCalDate(_ed, 0, 0, 0);

                if(meetingVO.getMt_remind_monthType() == 1) {   // 특정일자 반복
                    String _sdM = meetingVO.getMt_start_dt().substring(0,8) + meetingVO.getMt_remind_monthDay() + meetingVO.getMt_start_dt().substring(10,18);
                    String _edM = meetingVO.getMt_end_dt().substring(0,8) + meetingVO.getMt_remind_monthDay() + meetingVO.getMt_end_dt().substring(10,18);

                    int creatN = 0;
                    int i = 0;

                    while (creatN <_cnt){
                        _sdate = getCalDate(_sdM, 0, i, 0);
                        _edate = getCalDate(_edM, 0, i, 0);

                        String _loofday = _sdate.substring(8,10);
                        if(_loofday.equals(meetingVO.getMt_remind_monthDay())){
                            meetingVO.setMt_start_dt(_sdate);
                            meetingVO.setMt_end_dt(_edate);
                            MeetingVO _chk = meetMapper.chkRoomDupDate(meetingVO);
                            _dayChk=_dayChk+_chk.getChkcnt();
                            ++creatN;
                        }
                        ++i;
                    }
                }
                if(meetingVO.getMt_remind_monthType() == 2) {   // 특정번째요일 반복
                    int sequence = meetingVO.getMt_remind_sequence();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(_sd.substring(0,4)), Integer.parseInt(_sd.substring(5,7))-1, Integer.parseInt(_sd.substring(8,10)), Integer.parseInt(_sd.substring(11,13)), Integer.parseInt(_sd.substring(14,16)));

                    for(Integer i=0;i<_cnt;i++){
                        for(Integer j=0;j<=6;j++) {  // 일~월 1~7
                            Integer _dw = getCalDayOfWeek(_sd, 0, i, j);
                            if(Integer.valueOf(meetingVO.getMt_remind_week()) == _dw){
                                cal.set(cal.DAY_OF_WEEK, Integer.valueOf(meetingVO.getMt_remind_week()));
                                if(sequence <= 4){
                                    cal.set(cal.DAY_OF_WEEK_IN_MONTH, sequence);
                                } else if(sequence==5){
                                    cal.set(cal.DAY_OF_WEEK_IN_MONTH, -1);
                                }
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                _sdate = format.format(cal.getTime());
                                _edate = _sdate.substring(0, 10) + _ed.substring(10);

                                meetingVO.setMt_start_dt(_sdate);
                                meetingVO.setMt_end_dt(_edate);
                                MeetingVO _chk = meetMapper.chkRoomDupDate(meetingVO);
                                _dayChk=_dayChk+_chk.getChkcnt();

                                cal.add(Calendar.MONTH, 1);
                            }
                        }
                    }
                }
            }

        }else{
            MeetingVO _chk = meetMapper.chkRoomDupDate(meetingVO);
            _dayChk=_dayChk+_chk.getChkcnt();
        }
        return _dayChk;
    }
}
