/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.*;
import com.eura.web.service.AnalysisService;
import org.omg.PortableServer._ServantActivatorStub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.base.BaseController;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.UserMapper;
import com.eura.web.service.MeetingService;
import com.eura.web.util.CONSTANT;
import com.eura.web.util.TokenJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/meet")
public class MeetController extends BaseController {
    private final MeetMapper meetMapper;
    private final FileServiceMapper fileServiceMapper;
    private final TokenJWT tokenJWT;
    private final MeetingService meetingService;
    private final UserMapper userMapper;
    private final AnalysisMapper analysisMapper;

    private final AnalysisService analysisService;

    @Value("${srvinfo}")
    public String srvinfo;

    @Value("${file.upload-dir}")
    private String filepath;

    @Value("${domain}")
    private String domain;

    @Value("${filedomain}")
    private String filedomain;

    @Value("${voddomain}")
    private String voddomain;

    /**
     * 미팅 메인
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/main")
    public ResultVO getMain(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();
            
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }else{
                // 개인화 - 개인정보
                _rs.put("ui_name", uInfo.getUser_name());
                ProfileInfoVO uPic = fileServiceMapper.selectUserProfileFile(uInfo.getIdx_user());
                String _upic = "";
                if(uPic != null && StringUtils.isNotEmpty(uPic.getFile_name())){
                    if(srvinfo.equals("dev")){
                        _upic = filedomain + "/pic?fnm=" + uPic.getFile_path() + uPic.getFile_name();
                    }else{
                        _upic = filedomain + uPic.getFile_path() + uPic.getFile_name();
                    }
                }
                _rs.put("ui_pic", _upic);
                
                // 개인화 - 다음일정 - 참여중인 미팅룸
                List<MeetingVO> mSi = meetMapper.getMyMeetShortList(uInfo.getIdx_user());
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
            }

            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 개인화 - 나의 미팅룸 - 참여중인 미팅룸
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/main/list")
    public ResultVO getMainList(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() != null){
                meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
                meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            }
            Map<String, Object> _rs = new HashMap<String, Object>();

            Long mInfoCnt = meetMapper.getMyMeetListCount(uInfo.getIdx_user());   // 참여중인 미팅룸 총 수

            if(meetingVO.getPageSort()==null){
                meetingVO.setPageSort(1);
            }
            List<MeetingVO> mInfo = meetMapper.getMyMeetList(meetingVO);    // 참여중인 미팅룸
            ArrayList<Object> _mrss = new ArrayList<Object>();
            for(MeetingVO rs0 : mInfo){
                Integer _auth = 0;
                if(rs0.getIdx_user().equals(uInfo.getIdx_user())){
                    _auth = 1;
                }
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("mt_hostname", rs0.getUser_name());
                _mrs.put("mt_status", rs0.getMt_status());
                _mrs.put("mt_start_dt", rs0.getMt_start_dt());
                _mrs.put("mt_end_dt", rs0.getMt_end_dt());
                _mrs.put("mt_live", rs0.getIs_live());
                _mrs.put("is_host", _auth);
                _mrss.add(_mrs);
            }
            _rs.put("mt_meetMyList", _mrss);    // 참여중인 미팅룸
            _rs.put("mt_meetMyListCount", mInfoCnt);    // 참여중인 미팅룸 총 수
            
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 개인화 - 지난 미팅
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/main/endlist")
    public ResultVO getMainEndList(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() != null){
                meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
                meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            }
            Map<String, Object> _rs = new HashMap<String, Object>();

            Long mInfoCnt = meetMapper.getMyMeetEndListCount(uInfo.getIdx_user());   // 지난 미팅룸 총 수

            // 개인화 - 지난 미팅
            if(meetingVO.getPageSort()==null){
                meetingVO.setPageSort(1);
            }
            List<MeetingVO> mEnd = meetMapper.getMyMeetEndList(meetingVO);    // 지난 미팅
            ArrayList<Object> _merss = new ArrayList<Object>();
            for(MeetingVO rs0 : mEnd){
                Integer _auth = 0;
                if(rs0.getIdx_user().equals(uInfo.getIdx_user())){
                    _auth = 1;
                }
                Map<String, Object> _mers = new HashMap<String, Object>();
                _mers.put("mt_idx", rs0.getIdx_meeting());
                _mers.put("mt_name", rs0.getMt_name());
                _mers.put("mt_hostname", rs0.getUser_name());
                _mers.put("mt_status", rs0.getMt_status());
                _mers.put("mt_start_dt", rs0.getMt_start_dt());
                _mers.put("mt_end_dt", rs0.getMt_end_dt());
                _mers.put("mt_live", rs0.getIs_live());
                _mers.put("is_host", _auth);
                _mers.put("is_join", rs0.getIs_join()); // 참석여부 0:미참석, 1:참석
                
                // 참여도 표시
                Integer _hmind = 0;
                if(meetingVO.getIdx_user().equals(rs0.getIdx_user())){
                    _hmind = 1;
                }
                _mers.put("mt_iDataDisplay", _hmind);   // 호스트 여부에 따라 참여도 ON/OFF - 0:게스트, 1:호스트
                _mers.put("mt_iData", 0); // 참여도

                _merss.add(_mers);
            }
            _rs.put("mt_meetEndMyList", _merss);    // 지난 미팅
            _rs.put("mt_meetMyListCount", mInfoCnt);    // 지난 미팅룸 총 수

            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 참석자 검색
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/invite")
    public ResultVO getMeetInvite(HttpServletRequest req, UserVO userVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.success);
        resultVO.setResult_str("회원 정보를 불러왔습니다.");
        resultVO.setData(null);

        try {
            if(!userVO.getSearchTxt().isEmpty()){
                UserVO uInfo = getChkUserLogin(req);
                if(uInfo==null){
                    resultVO.setResult_str("로그인 후 이용해주세요.");
                    return resultVO;
                }
                Map<String, Object> _rs = new HashMap<String, Object>();
                
                userVO.setIdx_user(uInfo.getIdx_user());
                List<UserVO> irs = userMapper.getUserSearch(userVO);
                ArrayList<Object> _irss = new ArrayList<Object>();
                for(UserVO irs0 : irs){
                    Map<String, Object> _irs = new HashMap<String, Object>();
                    _irs.put("idx", irs0.getIdx_user());
                    _irs.put("uname", irs0.getUser_name());
                    _irs.put("email", irs0.getUser_id());
                    String _upic = "";
                    if(StringUtils.isNotEmpty(irs0.getFile_name())){
                        if(srvinfo.equals("dev")){
                            _upic = filedomain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
                        }else{
                            _upic = filedomain + irs0.getFile_path() + irs0.getFile_name();
                        }
                    }
                    _irs.put("ui_pic", _upic);
                    _irss.add(_irs);
                }
                _rs.put("mt_invites", _irss);
                
                resultVO.setData(_rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 메인 - 호스트뷰, 참석자뷰 메인
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/room/main")
    public ResultVO getRoomMain(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                String _auth = "0"; // 게스트 권한 부여

                // 호스트 권한 부여
                if(rs.getIdx_user().equals(uInfo.getIdx_user())){
                    _auth = "1";
                }

                // 미팅이 종료된 상태는 분석 페이지로 이동하게
                if(rs.getIs_finish()==1){
                    resultVO.setResult_code(CONSTANT.fail+"01");
                    resultVO.setResult_str("미팅이 종료되어 분석 페이지로 이동합니다.");
                    return resultVO;
                }
                
                if(_auth.equals("0")){
                    // 공개 상태 이외에서는 호스트만 볼 수 있게
                    if(rs.getMt_status()!=1){
                        resultVO.setResult_str("미팅룸에 참여할 권한이 없습니다.");
                        return resultVO;
                    }
                    if(rs.getIs_live()==0 && getDateTimeDiff(rs.getMt_end_dt(),new Date())<0){
                        resultVO.setResult_str("종료된 미팅입니다.");
                        return resultVO;
                    }
                }

                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_ishost", _auth);
                _rs.put("mt_name", rs.getMt_name());
                _rs.put("mt_hostname", rs.getUser_name());
                _rs.put("mt_start_dt", rs.getMt_start_dt());
                _rs.put("mt_end_dt", rs.getMt_end_dt());
                _rs.put("mt_remind_type", rs.getMt_remind_type());
                _rs.put("mt_remind_count", rs.getMt_remind_count());
                _rs.put("mt_remind_week", rs.getMt_remind_week());
                _rs.put("mt_remind_end", rs.getMt_remind_end());
                _rs.put("mt_info", rs.getMt_info());
                _rs.put("mt_status", rs.getMt_status());
                _rs.put("mt_live", rs.getIs_live());
                _rs.put("mt_live_dt", rs.getIs_live_dt());
                _rs.put("mt_finish", rs.getIs_finish());
                _rs.put("mt_finish_dt", rs.getIs_finish_dt());
                _rs.put("mt_regdt", rs.getReg_dt());

                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);
                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    Map<String, Object> _frs = new HashMap<String, Object>();
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_name());
                    String _furl = "";
                    if(StringUtils.isNotEmpty(frs0.getFile_name())){
                        if(srvinfo.equals("dev")){
                            _furl = filedomain + "/download?fnm=" + frs0.getFile_path() + frs0.getFile_name();
                        }else{
                            _furl = filedomain + frs0.getFile_path() + frs0.getFile_name();
                        }
                    }
                    _frs.put("download", _furl);
                    _frss.add(_frs);
                }
                _rs.put("mt_files", _frss);

                resultVO.setData(_rs);

                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
            }else{
                resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 메인 - 참석자 리스트(더보기)
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/room/invite")
    public ResultVO getRoomInvite(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            Integer _auth = 0;
            if(uInfo.getIdx_user().equals(rs.getIdx_user())){
                _auth = 1;
            }
            Map<String, Object> _rs = new HashMap<String, Object>();
            
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() != null){
                meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
                meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            }
            List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);
            ArrayList<Object> _irss = new ArrayList<Object>();
            Integer _incnt = 0;

            for(MeetingVO irs0 : irs){
                Map<String, Object> _irs = new HashMap<String, Object>();
                _irs.put("idx", irs0.getIdx_user());        // 참석자 회원 INDEX
                _irs.put("uname", irs0.getUser_name());     // 참석자명
                _irs.put("email", irs0.getUser_email());    // 참석자 이메일 주소
                _irs.put("is_host", irs0.getIs_host());     // 호스트 여부 - 0:참석자, 1:호스트

                Integer _iam = 0;
                if(irs0.getIdx_user().equals(uInfo.getIdx_user())){
                    _iam = 1;
                }
                _irs.put("is_iam", _iam); // 0:남일때, 1:나일때

                Integer _stat = 0;  // 감정 상태 - 0:미참여, 1:참여중, 2:GOOD, 3:BAD, 4:Camera Off
                if(_auth==1){
                    _stat = irs0.getIs_live();
                }else{
                    if(_iam == 1){
                        _stat = irs0.getIs_live();
                    }
                }
                // 참여중인 상태에서 호스트에게는 모든 참석자 상태 표시하고, 참석자는 본인 것만 GOOD,BAD 표시
                if(_stat==1 && (_auth==1 || _iam==1)){
                    if(irs0.getGood()>=irs0.getBad()){
                        _stat = 2;  // GOOD
                    }else{
                        _stat = 3;  // BAD
                    }
                }
                // 호스트이거나 나일 때 참석중이 아닐 경우
                if((_auth==1 || _iam == 1) && irs0.getIs_alive() == 0 && irs0.getIs_live()==1){
                    _stat = 4;
                }
                _irs.put("is_status", _stat);  // 감정 상태 - 0:미참여, 1:참여중, 2:GOOD, 3:BAD, 4:Camera Off

                String _pic = "";
                if(StringUtils.isNotEmpty(irs0.getFile_name())){
                    if(srvinfo.equals("dev")){
                        _pic = filedomain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
                    }else{
                        _pic = filedomain + irs0.getFile_path() + irs0.getFile_name();
                    }
                }
                _irs.put("ui_pic", _pic);       // 참석자 프로필 사진
                _irss.add(_irs);

                if(irs0.getIs_live()==1){
                    _incnt++;
                }
            }
            _rs.put("mt_invites", _irss);
            _rs.put("is_host", _auth);      // 호스트 여부

            _rs.put("mt_cnttotal", irs.size());     // 참석자 총 인원수
            _rs.put("mt_cntinvite", _incnt);        // 참석한 인원수
            
            resultVO.setData(_rs);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 공개
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PutMapping("/room/open")
    public ResultVO putMeetOpen(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                        // 일정 지난 비공개 미팅일 경우 팝업 메시지 창 띄우고 강제로 수정 페이지로 이동하게 만들기
                        if(getDateTimeDiff(rrs.getMt_end_dt(), new Date())<0){
                            resultVO.setResult_code(CONSTANT.fail+"01");
                            resultVO.setResult_str("일정이 지난 미팅은 공개할 수 없어서 수정페이지로 이동합니다.");
                            return resultVO;
                        }
                        Integer rs = meetMapper.putMeetOpen(meetingVO);
                        if(rs==1){
                            // 참가자 이메일 전송
                            meetingService.sendMail(meetingVO, rrs, 4);

                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸을 공개하였습니다.");
                        }else{
                            resultVO.setResult_str("미팅룸을 공개하지 못하였습니다.");
                        }
                    }else{
                        resultVO.setResult_str("이용권한이 없습니다.");
                    }
                }else{
                    resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 비공개
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/room/close")
    public ResultVO putMeetClose(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 비공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                        if(rrs.getIs_live()==1){
                            resultVO.setResult_str("미팅 중에는 비공개 할 수 없습니다.");
                            return resultVO;
                        }
                        Integer rs = meetMapper.putMeetClose(meetingVO);
                        if(rs==1){
                            // 참가자 이메일 전송
                            meetingService.sendMail(meetingVO, rrs, 4);

                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸을 비공개하였습니다.");
                        }else{
                            resultVO.setResult_str("미팅룸을 비공개하지 못하였습니다.");
                        }
                    }else{
                        resultVO.setResult_str("이용권한이 없습니다.");
                    }
                }else{
                    resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 취소
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/room/cancel")
    public ResultVO putMeetCacncel(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                    if(rrs.getIs_live()==1){
                        resultVO.setResult_str("미팅 중에는 취소 할 수 없습니다.");
                        return resultVO;
                    }
                    Integer rs = meetMapper.putMeetCacncel(meetingVO);
                    if(rs==1){
                        if(rrs.getMt_status()==1){
                            // 참가자 이메일 전송
                            meetingService.sendMail(meetingVO, rrs, 3);
                        }

                        resultVO.setResult_code(CONSTANT.success);
                        resultVO.setResult_str("미팅룸을 취소하였습니다.");
                    }else{
                        resultVO.setResult_str("미팅룸을 취소하지 못하였습니다.");
                    }
                }else{
                    resultVO.setResult_str("이용권한이 없습니다.");
                }
            }else{
                resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 삭제
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/room/erase")
    public ResultVO deleteMeet(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                    if(rrs.getIs_live()==1){
                        resultVO.setResult_str("미팅 중에는 삭제 할 수 없습니다.");
                        return resultVO;
                    }
                    Integer rs = meetMapper.deleteMeet(meetingVO);
                    if(rs==1){
                        if(rrs.getMt_status()==1){
                            // 참가자 이메일 전송
                            meetingService.sendMail(meetingVO, rrs, 3);
                        }

                        resultVO.setResult_code(CONSTANT.success);
                        resultVO.setResult_str("미팅룸을 삭제하였습니다.");
                    }else{
                        resultVO.setResult_str("미팅룸을 삭제하지 못하였습니다.");
                    }
                }else{
                    resultVO.setResult_str("이용권한이 없습니다.");
                }
            }else{
                resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅 달력
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/main/calendar")
    public ResultVO getMainCalendar(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());
            long time = System.currentTimeMillis(); 
            SimpleDateFormat tYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat tMonth = new SimpleDateFormat("MM");
            Integer _tYear = Integer.valueOf(tYear.format(new Date(time)));
            Integer _tMonth = Integer.valueOf(tMonth.format(new Date(time)));
            if(meetingVO.getCalYear() == null){
                meetingVO.setCalYear(_tYear);
            }
            if(meetingVO.getCalMonth() == null){
                meetingVO.setCalMonth(_tMonth);
            }
            Map<String, Object> _rs = new HashMap<String, Object>();
            List<MeetingVO> mInfo = meetMapper.getMyMeetCalendarList(meetingVO);    // 참여중인 미팅룸
            ArrayList<Object> _mrss = new ArrayList<Object>();
            for(MeetingVO rs0 : mInfo){
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("mt_date", rs0.getMt_start_dt());
                _mrs.put("is_finish", rs0.getIs_finish());  // 지난 미팅일 경우 분석 페이지로 이동하게 함 - 0:안지남, 1:지남
                _mrss.add(_mrs);
            }
            _rs.put("mt_meetMyList", _mrss);    // 참여중인 미팅룸
            
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅 달력 일정
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/main/calendar/info")
    public ResultVO getMainCalendarInfo(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());

            Map<String, Object> _rs = new HashMap<String, Object>();
            ArrayList<Object> _mrss = new ArrayList<Object>();
            List<MeetingVO> mInfo = meetMapper.getMyMeetCalendarList(meetingVO);    // 참여중인 미팅룸
            for(MeetingVO rs0 : mInfo){
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("hostname", rs0.getUser_name());
                _mrs.put("timer", rs0.getMt_start_dt().substring(11,16) + " - " + rs0.getMt_end_dt().substring(11,16));
                _mrs.put("mt_info", rs0.getMt_info());
                _mrs.put("is_finish", rs0.getIs_finish());  // 종료 여부에 따라서 분석 페이지로 이동하게 하기
                _mrss.add(_mrs);
            }
            _rs.put("mt_meetMyList", _mrss);    // 참여중인 미팅룸
            
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 정보 - 미팅룸 수정용
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @GetMapping("/room/info")
    public ResultVO getRoomInfo(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                if(!rs.getIdx_user().equals(uInfo.getIdx_user())){
                    resultVO.setResult_str("수정 권한이 없습니다.");
                    return resultVO;
                }else if(rs.getMt_status() == 3){
                    resultVO.setResult_str("삭제된 미팅은 수정 할 수 없습니다.");
                    return resultVO;
                }else if(rs.getIs_live()==1){
                    resultVO.setResult_str("미팅 중에는 수정 할 수 없습니다.");
                    return resultVO;
                }else{
                    Map<String, Object> _rs = new HashMap<String, Object>();
                    _rs.put("mt_name", rs.getMt_name());
                    _rs.put("mt_hostname", rs.getUser_name());
                    _rs.put("mt_start_dt", rs.getMt_start_dt());
                    _rs.put("mt_end_dt", rs.getMt_end_dt());
                    _rs.put("mt_remind_type", rs.getMt_remind_type());
                    _rs.put("mt_remind_count", rs.getMt_remind_count());
                    _rs.put("mt_remind_week", rs.getMt_remind_week());
                    _rs.put("mt_remind_end", rs.getMt_remind_end());
                    _rs.put("mt_info", rs.getMt_info());    // 미팅룸 정보
                    _rs.put("mt_status", rs.getMt_status());    // 미팅룸 상태 - 0:비공개, 1:공개, 2:취소, 3:삭제

                    // 미팅 첨부파일
                    List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);
                    ArrayList<Object> _frss = new ArrayList<Object>();
                    for(MeetingVO frs0 : frs){
                        Map<String, Object> _frs = new HashMap<String, Object>();
                        _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                        _frs.put("files", frs0.getFile_name());
                        _frs.put("fileSize", frs0.getFile_size());
                        String _furl = "";
                        if(StringUtils.isNotEmpty(frs0.getFile_name())){
                            if(srvinfo.equals("dev")){
                                _furl = filedomain + "/download?fnm=" + frs0.getFile_path() + frs0.getFile_name();
                            }else{
                                _furl = filedomain + frs0.getFile_path() + frs0.getFile_name();
                            }
                        }
                        _frs.put("download", _furl);
                        _frss.add(_frs);
                    }
                    _rs.put("mt_files", _frss);     // 미팅 첨부파일

                    // 미팅 참석자
                    List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);
                    ArrayList<Object> _irss = new ArrayList<Object>();
                    for(MeetingVO irs0 : irs){
                        Map<String, Object> _irs = new HashMap<String, Object>();
                        _irs.put("idx", irs0.getIdx_user());
                        _irs.put("uname", irs0.getUser_name());
                        _irs.put("email", irs0.getUser_email());
                        String _upic = "";
                        if(StringUtils.isNotEmpty(irs0.getFile_name())){
                            if(srvinfo.equals("dev")){
                                _upic = filedomain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
                            }else{
                                _upic = filedomain + irs0.getFile_path() + irs0.getFile_name();
                            }
                        }
                        _irs.put("ui_pic", _upic);
                        _irss.add(_irs);
                    }
                    _rs.put("mt_invites", _irss);   // 미팅 참석자

                    resultVO.setData(_rs);

                    resultVO.setResult_code(CONSTANT.success);
                    resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
                }
            }else{
                resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 생성
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/create")
    public ResultVO create(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }

            if(meetingVO != null){
                Integer _dayChk = 0;

                // 미팅룸 정보 생성
                meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

                /*
                * MT_REMIND_TYPE - 0:없음, 1:매일, 2:주, 3:격주, 4:월
                * MT_REMIND_COUNT - 주기
                * MT_REMIND_WEEK - 반복요일 - 월,화,수,목,금,토,일
                * MT_REMIND_END - 되풀이 미팅 종료일
                */
                if(meetingVO.getMt_remind_type()==null){
                    meetingVO.setMt_remind_type(0);
                }
                if(StringUtils.isEmpty(meetingVO.getMt_end_dt()) && meetingVO.getMt_remind_type()>0){
                    resultVO.setResult_str("미팅 종료 날짜를 골라주세요.");
                    return resultVO;
                }
                if(StringUtils.isEmpty(meetingVO.getMt_start_dt())){
                    resultVO.setResult_str("미팅 시작 시간을 골라주세요.");
                    return resultVO;
                }
                if(StringUtils.isEmpty(meetingVO.getMt_end_dt())){
                    resultVO.setResult_str("미팅 종료 시간을 골라주세요.");
                    return resultVO;
                }
                if(getDateTimeDiff(meetingVO.getMt_start_dt(),meetingVO.getMt_end_dt())>=0){
                    resultVO.setResult_str("미팅 종료 시간은 시작 시간보다 빠르거나 같을 수 없습니다.");
                    return resultVO;
                }
                if(getDateTimeDiff(meetingVO.getMt_start_dt(), new Date())<0){
                    resultVO.setResult_str("미팅 시간은 현재 시간보다 빠를 수 없습니다.");
                    return resultVO;
                }
                if(meetingVO.getMt_remind_type().equals(0)){
                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, 0, meetingVO);
                    if(_dayChk.equals(0)){
                        resultVO = meetingService.createMeetRoom(req, meetingVO);
                        Integer _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                        meetingService.meetFileSave(req, _idx);
                        resultVO.setResult_code(CONSTANT.success);
                        resultVO.setResult_str("미팅룸을 생성하였습니다.");
                    }else{
                        resultVO.setResult_str("중복 일정이 있어 미팅룸 생성을 중단합니다.");
                    }

                // 일 주기
                }else if(meetingVO.getMt_remind_type().equals(1)){
                    Integer _cnt = meetingVO.getMt_remind_count();
                    if(_cnt > 30){
                        resultVO.setResult_str("일 단위 되풀이 주기는 30일까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        // 일 단위 중복 체크
                        MeetingVO param = meetingVO;
                        String _enddt = meetingVO.getMt_remind_end();
                        String _sd = meetingVO.getMt_start_dt();
                        String _ed = meetingVO.getMt_end_dt();

                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);

                        if(_dayChk.equals(0)){
                            Integer _idx = 0;
                            List<MeetingVO> _frss = new ArrayList<>();
                            for(Integer i=0;i<_cnt;i++){
                                String _sdate = getCalDate(_sd, 0, 0, i);
                                String _edate = getCalDate(_ed, 0, 0, i);
                                param.setMt_start_dt(_sdate);
                                param.setMt_end_dt(_edate);
                                if(i>0){
                                    param.setMt_remind_type(0);
                                    param.setMt_remind_count(0);
                                    param.setMt_remind_week(null);
                                    param.setMt_remind_end(null);
                                }
                                if(getDateDiff(_sdate, _enddt)<0){
                                    resultVO = meetingService.createMeetRoom(req, param);
                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                    if(i==0){
                                        // 미팅룸 첨부파일 저장
                                        log.info("미팅룸 첨부파일 저장");
                                        _frss = meetingService.meetFileSave(req, _idx);
                                    }else{
                                        // 미팅룸 첨부파일 복사
                                        if(!_idx.equals(0)){
                                            meetingService.meetFileCopy(_idx, _frss);
                                        }
                                    }
                                }
                            }
                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸을 생성하였습니다.");
                        }else{
                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                        }
                        resultVO.setData(null);
                    }
                
                // 주 주기
                }else if(meetingVO.getMt_remind_type().equals(2)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 주
                    if(_cnt > 12){
                        resultVO.setResult_str("주 단위 되풀이 주기는 12주까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        if(meetingVO.getMt_remind_week().isEmpty() || meetingVO.getMt_remind_week()==null){
                            resultVO.setResult_str("주 단위 되풀이 주기는 요일을 선택해주세요.");
                        }else{
                            // 주 단위 중복 체크
                            MeetingVO param = meetingVO;
                            String _enddt = meetingVO.getMt_remind_end();
                            String[] _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                            String _sd = meetingVO.getMt_start_dt();
                            String _ed = meetingVO.getMt_end_dt();
                            for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                    Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*7));
                                    for(String _w : _week){ // 입력 값 체크
                                        if(Integer.valueOf(_w) == _dw){
                                            String _sdate = getCalDate(_sd, 0, 0, j+((i-1)*7));
                                            String _edate = getCalDate(_ed, 0, 0, j+((i-1)*7));
                                            param.setMt_start_dt(_sdate);
                                            param.setMt_end_dt(_edate);
                                            if(getDateDiff(_sdate, _enddt)<0){
                                                _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                            }
                                        }
                                    }
                                }
                            }
                            if(_dayChk.equals(0)){
                                Integer _idx = 0;
                                Integer _fcnt = 0;
                                List<MeetingVO> _frss = new ArrayList<>();
                                for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                    for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                        Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*7));
                                        for(String _w : _week){ // 입력 값 체크
                                            if(Integer.valueOf(_w).equals(_dw)){
                                                String _sdate = getCalDate(_sd, 0, 0, j+((i-1)*7));
                                                String _edate = getCalDate(_ed, 0, 0, j+((i-1)*7));
                                                param.setMt_start_dt(_sdate);
                                                param.setMt_end_dt(_edate);
                                                if(_fcnt>0){
                                                    param.setMt_remind_type(0);
                                                    param.setMt_remind_count(0);
                                                    param.setMt_remind_week("");
                                                    param.setMt_remind_end(null);
                                                }
                                                if(getDateDiff(_sdate, _enddt)<0){
                                                    resultVO = meetingService.createMeetRoom(req, param);
                                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                                    if(_fcnt==0){
                                                        _fcnt++;
                                                        // 미팅룸 첨부파일 저장
                                                        _frss = meetingService.meetFileSave(req, _idx);
                                                    }else{
                                                        if(!_idx.equals(0)){
                                                            _fcnt++;
                                                            meetingService.meetFileCopy(_idx, _frss);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅룸을 생성하였습니다.");
                            }else{
                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                            }
                        }
                        resultVO.setData(null);
                    }

                // 격주 주기
                }else if(meetingVO.getMt_remind_type().equals(3)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 주
                    if(_cnt > 12){
                        resultVO.setResult_str("주 단위 되풀이 주기는 12주까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        if(meetingVO.getMt_remind_week().isEmpty() || meetingVO.getMt_remind_week()==null){
                            resultVO.setResult_str("주 단위 되풀이 주기는 요일을 선택해주세요.");
                        }else{
                            // 주 단위 중복 체크
                            MeetingVO param = meetingVO;
                            String _enddt = meetingVO.getMt_remind_end();
                            String[] _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                            String _sd = meetingVO.getMt_start_dt();
                            String _ed = meetingVO.getMt_end_dt();
                            Integer _sdw = getCalDayOfWeek(_sd, 0, 0, 0);

                            for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                    Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*14));
                                    for(String _w : _week){ // 입력 값 체크
                                        if(Integer.valueOf(_w) == _dw){
                                            //반복하고자 하는 요일이 오늘(요일)이거나 보다 이후
                                            if(Integer.valueOf(_w) >= _sdw){
                                                String _sdate = getCalDate(_sd, 0, 0, j+((i-1)*14));
                                                String _edate = getCalDate(_ed, 0, 0, j+((i-1)*14));
                                                param.setMt_start_dt(_sdate);
                                                param.setMt_end_dt(_edate);
                                                if(getDateDiff(_sdate, _enddt)<0){
                                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                }
                                            //반복하고자 하는 요일이 오늘(요일) 보다 이전
                                            } else if(Integer.valueOf(_w) < _sdw){
                                                String _sdate = getCalDate(_sd, 0, 0, 7+j+((i-1)*14));
                                                String _edate = getCalDate(_ed, 0, 0, 7+j+((i-1)*14));
                                                param.setMt_start_dt(_sdate);
                                                param.setMt_end_dt(_edate);
                                                if(getDateDiff(_sdate, _enddt)<0){
                                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(_dayChk.equals(0)){
                                Integer _idx = 0;
                                Integer _fcnt = 0;
                                List<MeetingVO> _frss = new ArrayList<>();
                                for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                    for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                        Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*14));
                                        for(String _w : _week){ // 입력 값 체크
                                            if(Integer.valueOf(_w).equals(_dw)){
                                                String _sdate = "";
                                                String _edate = "";
                                                //반복하고자 하는 요일이 오늘(요일) 보다 이후
                                                if(Integer.valueOf(_w) >= _sdw){
                                                    _sdate = getCalDate(_sd, 0, 0, j+((i-1)*14));
                                                    _edate = getCalDate(_ed, 0, 0, j+((i-1)*14));
                                                    param.setMt_start_dt(_sdate);
                                                    param.setMt_end_dt(_edate);
                                                //반복하고자 하는 요일이 오늘(요일) 보다 이전
                                                } else if(Integer.valueOf(_w) < _sdw){
                                                    _sdate = getCalDate(_sd, 0, 0, 7+j+((i-1)*14));
                                                    _edate = getCalDate(_ed, 0, 0, 7+j+((i-1)*14));
                                                    param.setMt_start_dt(_sdate);
                                                    param.setMt_end_dt(_edate);
                                                }
                                                if(_fcnt>0){
                                                    param.setMt_remind_type(0);
                                                    param.setMt_remind_count(0);
                                                    param.setMt_remind_week("");
                                                    param.setMt_remind_end(null);
                                                }
                                                if(getDateDiff(_sdate, _enddt)<0){
                                                    resultVO = meetingService.createMeetRoom(req, param);
                                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                                    if(_fcnt==0){
                                                        _fcnt++;
                                                        // 미팅룸 첨부파일 저장
                                                        _frss = meetingService.meetFileSave(req, _idx);
                                                    }else{
                                                        if(!_idx.equals(0)){
                                                            _fcnt++;
                                                            meetingService.meetFileCopy(_idx, _frss);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅룸을 생성하였습니다.");
                            }else{
                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                            }
                        }
                        resultVO.setData(null);
                    }

                // 월 주기
                }else if(meetingVO.getMt_remind_type().equals(4)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 월
                    if(_cnt > 12){
                        resultVO.setResult_str("월 단위 되풀이 주기는 12개월까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        //매월 특정일자 반복
                        if(meetingVO.getMt_remind_monthType() == 1) {

                            MeetingVO param = meetingVO;
                            String _enddt = meetingVO.getMt_remind_end();
                            String _startdt = meetingVO.getMt_start_dt();

                            String _sd = meetingVO.getMt_start_dt().substring(0,8) + meetingVO.getMt_remind_monthDay() + meetingVO.getMt_start_dt().substring(10);
                            String _ed = meetingVO.getMt_end_dt().substring(0,8) + meetingVO.getMt_remind_monthDay() + meetingVO.getMt_end_dt().substring(10);

                            _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);

                            if(_dayChk.equals(0)){
                                Integer _idx = 0;
                                List<MeetingVO> _frss = new ArrayList<>();
                                int creatN = 0;
                                int i = 0;

                                while (creatN <_cnt){
                                    String _sdate = getCalDate(_sd, 0, i, 0);
                                    String _edate = getCalDate(_ed, 0, i, 0);
                                    param.setMt_start_dt(_sdate);
                                    param.setMt_end_dt(_edate);
                                    if(creatN>0){
                                        param.setMt_remind_type(0);
                                        param.setMt_remind_count(0);
                                        param.setMt_remind_week("");
                                        param.setMt_remind_end(null);
                                    }

                                    String _loofday = _sdate.substring(8,10);

                                    if(getDateDiff(_sdate, _enddt)<0){
                                        if(_loofday.equals(meetingVO.getMt_remind_monthDay()) && getDateDiff(_startdt, _sdate)<0){
                                            resultVO = meetingService.createMeetRoom(req, param);
                                            _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                            if(creatN==0){
                                                // 미팅룸 첨부파일 저장
                                                _frss = meetingService.meetFileSave(req, _idx);
                                            }else{
                                                if(!_idx.equals(0)){
                                                    meetingService.meetFileCopy(_idx, _frss);
                                                }
                                            }
                                            ++creatN;
                                        }
                                    } else { break; }
                                    ++i;
                                }
                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅룸을 생성하였습니다.");
                            }else{
                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                            }
                            resultVO.setData(null);
                        }

                        //매월 특정번째 요일 반복
                        if(meetingVO.getMt_remind_monthType() == 2) {

                            MeetingVO param = meetingVO;
                            String _enddt = meetingVO.getMt_remind_end();
                            int sequence = meetingVO.getMt_remind_sequence();   // 요일이 해당되는 순차 (n번째 요일)
                            String week = meetingVO.getMt_remind_week();   // 요일 선택
                            String _sd = meetingVO.getMt_start_dt();
                            String _ed = meetingVO.getMt_end_dt();

                            _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);

                            if(_dayChk.equals(0)){
                                Integer _idx = 0;
                                List<MeetingVO> _frss = new ArrayList<>();

                                Calendar cal = Calendar.getInstance();
                                cal.set(Integer.parseInt(_sd.substring(0,4)), Integer.parseInt(_sd.substring(5,7))-1, Integer.parseInt(_sd.substring(8,10)), Integer.parseInt(_sd.substring(11,13)), Integer.parseInt(_sd.substring(14,16)));

                                int creatN = 0;
                                int i = 0;

                                while (creatN <_cnt){
                                    cal.set(cal.DAY_OF_WEEK, Integer.valueOf(week));
                                    if(sequence <= 4){
                                        cal.set(cal.DAY_OF_WEEK_IN_MONTH, sequence);
                                    } else if(sequence==5){
                                        cal.set(cal.DAY_OF_WEEK_IN_MONTH, -1);
                                    }

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String _sdate = format.format(cal.getTime());
                                    String _edate = _sdate.substring(0,10) + _ed.substring(10);

                                    param.setMt_start_dt(_sdate);
                                    param.setMt_end_dt(_edate);
                                    if(creatN>0){
                                        param.setMt_remind_type(0);
                                        param.setMt_remind_count(0);
                                        param.setMt_remind_week("");
                                        param.setMt_remind_end(null);
                                    }
                                    if(getDateDiff(_sdate, _enddt)<0){
                                        if(getDateDiff(_sd, _sdate)<0){
                                            resultVO = meetingService.createMeetRoom(req, param);
                                            _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                            if(creatN==0){
                                                // 미팅룸 첨부파일 저장
                                                _frss = meetingService.meetFileSave(req, _idx);
                                            }else{
                                                if(!_idx.equals(0)){
                                                    meetingService.meetFileCopy(_idx, _frss);
                                                }
                                            }
                                            ++creatN;
                                        }
                                    } else { break; }
                                    ++i;
                                    cal.add(Calendar.MONTH, 1);
                                }


                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅룸을 생성하였습니다.");
                            }else{
                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                            }
                            resultVO.setData(null);
                        }


                    }
                }
                // 년 주기
                /*
                else if(meetingVO.getMt_remind_type().equals(4)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 년
                    if(_cnt > 5){
                        resultVO.setResult_str("년 단위 되풀이 주기는 5년까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        // 년 단위 중복 체크
                        MeetingVO param = meetingVO;
                        String _enddt = meetingVO.getMt_remind_end();
                        String _sd = meetingVO.getMt_start_dt();
                        String _ed = meetingVO.getMt_end_dt();

                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);

                        if(_dayChk.equals(0)){
                            Integer _idx = 0;
                            List<MeetingVO> _frss = new ArrayList<>();
                            for(Integer i=0;i<_cnt;i++){
                                String _sdate = getCalDate(_sd, i, 0, 0);
                                String _edate = getCalDate(_ed, i, 0, 0);
                                param.setMt_start_dt(_sdate);
                                param.setMt_end_dt(_edate);
                                if(i>0){
                                    param.setMt_remind_type(0);
                                    param.setMt_remind_count(0);
                                    param.setMt_remind_week("");
                                    param.setMt_remind_end(null);
                                }
                                if(getDateDiff(_sdate, _enddt)<0){
                                    resultVO = meetingService.createMeetRoom(req, param);

                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());
                                    if(i==0){
                                        // 미팅룸 첨부파일 저장
                                        _frss = meetingService.meetFileSave(req, _idx);
                                    }else{
                                        if(!_idx.equals(0)){
                                            meetingService.meetFileCopy(_idx, _frss);
                                        }
                                    }
                                }
                            }
                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸을 생성하였습니다.");
                        }else{
                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                        }
                        resultVO.setData(null);
                    }
                }
                */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 수정
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/modify")
    public ResultVO modify(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");
        resultVO.setData(null);

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            Integer rs = 0;
            if(meetingVO != null){
                // 미팅룸 정보 수정
                meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);

                if(rrs!=null){
                    String _edittxt = "수정";
                    if(rrs.getMt_status() == 2){
                        _edittxt = "재개설";

                        // 취소 상태에서 수정하면 재개설이므로 상태는 비공개로 전환됨.
                        meetingVO.setMt_status(0);
                    }
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                        if(StringUtils.isEmpty(meetingVO.getMt_start_dt())){
                            resultVO.setResult_str("미팅 시작 시간을 골라주세요.");
                            return resultVO;
                        }
                        if(StringUtils.isEmpty(meetingVO.getMt_end_dt())){
                            resultVO.setResult_str("미팅 종료 시간을 골라주세요.");
                            return resultVO;
                        }
                        if(rrs.getIs_live()==1){
                            if(getDateTimeDiff(rrs.getMt_start_dt(), meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(), meetingVO.getMt_end_dt())!=0){
                                resultVO.setResult_str("미팅 중에는 예약 시간을 변경 할 수 없습니다.");
                                return resultVO;
                            }
                        }
                        if(meetingVO.getMt_remind_type()==null){
                            meetingVO.setMt_remind_type(0);
                        }
                        if(StringUtils.isEmpty(meetingVO.getMt_end_dt()) && meetingVO.getMt_remind_type()>0){
                            resultVO.setResult_str("미팅 종료 날짜를 골라주세요.");
                            return resultVO;
                        }

                        if(rrs.getIs_finish()==1){
                            resultVO.setResult_str("종료된 미팅은 "+ _edittxt +"할 수 없습니다.");
                            return resultVO;
                        }
                        if(getDateTimeDiff(meetingVO.getMt_start_dt(),meetingVO.getMt_end_dt())>=0){
                            resultVO.setResult_str("미팅 종료 시간은 시작 시간보다 빠르거나 같을 수 없습니다.");
                            return resultVO;
                        }
                        if(getDateTimeDiff(meetingVO.getMt_start_dt(), new Date())<0){
                            resultVO.setResult_str("미팅 시간은 현재 시간보다 빠를 수 없습니다.");
                            return resultVO;
                        }

                        if(meetingVO.getMt_remind_type().equals(0)){
                            Integer _dayChk = 0;
                            if(getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0){
                                _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, 0, meetingVO);
                            }
                            if(_dayChk.equals(0)){
                                rs = meetMapper.meet_modify(meetingVO);
                                if(rs > 0){
                                    // 미팅룸의 참석자 모두 미팅 시작 전까지만 수정&삭제 가능하게 변경 2022-11-17 16:30
                                    // 미팅룸 참여자 삭제 리스트
                                    if(meetingVO.getInvite_del() != null && meetingVO.getInvite_del() != ""){
                                        String[] inInvite = meetingVO.getInvite_del().split(",");
                                        for(String ue : inInvite){
                                            MeetingVO ee = new MeetingVO();
                                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                            ee.setUser_email(ue);
                                            meetMapper.meet_invite_del(ee);

                                            if(rrs.getMt_status()==1) {
                                                meetingService.sendModifyMail(ee, 3);
                                            }
                                        }
                                    }

                                    // 미팅룸 첨부파일 삭제
                                    if(meetingVO.getFile_del() != null && !meetingVO.getFile_del().isEmpty() && !meetingVO.getFile_del().equals("")){
                                        String[] fileIdx = meetingVO.getFile_del().split(",");
                                        for(String fidx : fileIdx){
                                            MeetingVO ee = new MeetingVO();
                                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                            ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                            // 파일 삭제
                                            MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                            if(finfo!=null){
                                                if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                    File f = new File(this.filepath + finfo.getFile_path() + finfo.getFile_name());
                                                    if(f.exists()){
                                                        f.delete();
                                                    }
                                                    fileServiceMapper.delMeetFile(ee);
                                                }
                                            }
                                        }
                                    }

                                    // 미팅룸 참여자 추가 저장
                                    meetingService.saveMeetInvite(meetingVO);

                                    // 미팅룸 첨부파일 저장
                                    meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                    if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                        meetingService.sendMail(meetingVO, rrs, 7);
                                    }

                                    resultVO.setResult_code(CONSTANT.success);
                                    resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                    return resultVO;
                                }else{
                                    resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                    return resultVO;
                                }
                            }else{
                                resultVO.setResult_str("중복 일정이 있어 미팅룸 "+ _edittxt +"을 중단합니다.");
                                return resultVO;
                            }
                        }else{
                            Integer _chkDup = 0;
                            Integer _dayChk = 0;
                            MeetingVO param = meetingVO;
                            String _enddt = meetingVO.getMt_remind_end();
                            String _sd = meetingVO.getMt_start_dt();
                            String _ed = meetingVO.getMt_end_dt();
                            Integer _cnt = meetingVO.getMt_remind_count();
                            String[] _week = null;
                            if(meetingVO.getMt_remind_end() == null){
                                resultVO.setResult_str("종료일을 선택해주세요.");
                                return resultVO;
                            }else{

                                // 일 주기
                                if(meetingVO.getMt_remind_type().equals(1)){
                                    if(_cnt > 30){
                                        resultVO.setResult_str("일 단위 되풀이 주기는 30일까지 입니다.");
                                        _chkDup = 1;
                                        return resultVO;
                                    }else{
                                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                        if(!_dayChk.equals(0)){
                                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 "+ _edittxt +"을 중단합니다.");
                                            _chkDup = 1;
                                            return resultVO;
                                        }
                                    }

                                // 주 주기
                                }else if(meetingVO.getMt_remind_type().equals(2)){
                                    if(_cnt > 12){
                                        resultVO.setResult_str("주 단위 되풀이 주기는 12주까지 입니다.");
                                        _chkDup = 1;
                                        return resultVO;
                                    }else {
                                        if (meetingVO.getMt_remind_week().isEmpty() || meetingVO.getMt_remind_week() == null) {
                                            resultVO.setResult_str("주 단위 되풀이 주기는 요일을 선택해주세요.");
                                            _chkDup = 1;
                                            return resultVO;
                                        } else {
                                            _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                                            for (Integer i = 1; i <= _cnt; i++) {   // 주 반복
                                                for (Integer j = 0; j <= 6; j++) {  // 일~월 1~7
                                                    Integer _dw = getCalDayOfWeek(_sd, 0, 0, j + ((i - 1) * 7));
                                                    for (String _w : _week) { // 입력 값 체크
                                                        if (Integer.valueOf(_w).equals(_dw)) {
                                                            String _sdate = getCalDate(_sd, 0, 0, j + ((i - 1) * 7));
                                                            // String _edate = getCalDate(_ed, 0, 0, j+((i-1)*7));
                                                            // param.setMt_start_dt(_sdate);
                                                            // param.setMt_end_dt(_edate);
                                                            if (getDateDiff(_sdate, _enddt) < 0) {
                                                                _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if (!_dayChk.equals(0)) {
                                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 " + _edittxt + "을 중단합니다.");
                                                _chkDup = 1;
                                                return resultVO;
                                            }
                                        }
                                    }
                                }
                                // 격주 주기
                                else if(meetingVO.getMt_remind_type().equals(3)){
                                    if(_cnt > 12){
                                        resultVO.setResult_str("주 단위 되풀이 주기는 12주까지 입니다.");
                                        _chkDup = 1;
                                        return resultVO;
                                    }else{
                                        if(meetingVO.getMt_remind_week().isEmpty() || meetingVO.getMt_remind_week()==null){
                                            resultVO.setResult_str("주 단위 되풀이 주기는 요일을 선택해주세요.");
                                            _chkDup = 1;
                                            return resultVO;
                                        }else{
                                            _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                                            Integer _sdw = getCalDayOfWeek(_sd, 0, 0, 0);
                                            for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                                for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                                    Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*7));
                                                    for(String _w : _week){ // 입력 값 체크
                                                        if(Integer.valueOf(_w).equals(_dw)){
                                                            //반복하고자 하는 요일이 오늘(요일)이거나 보다 이후
                                                            if(Integer.valueOf(_w) >= _sdw){
                                                                String _sdate = getCalDate(_sd, 0, 0, j+((i-1)*14));
                                                                String _edate = getCalDate(_ed, 0, 0, j+((i-1)*14));
                                                                param.setMt_start_dt(_sdate);
                                                                param.setMt_end_dt(_edate);
                                                                if(getDateDiff(_sdate, _enddt)<0){
                                                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                                }
                                                                //반복하고자 하는 요일이 오늘(요일) 보다 이전
                                                            } else if(Integer.valueOf(_w) < _sdw){
                                                                String _sdate = getCalDate(_sd, 0, 0, 7+j+((i-1)*14));
                                                                String _edate = getCalDate(_ed, 0, 0, 7+j+((i-1)*14));
                                                                param.setMt_start_dt(_sdate);
                                                                param.setMt_end_dt(_edate);
                                                                if(getDateDiff(_sdate, _enddt)<0){
                                                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if(!_dayChk.equals(0)){
                                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 "+ _edittxt +"을 중단합니다.");
                                                _chkDup = 1;
                                                return resultVO;
                                            }
                                        }
                                    }
        
                                // 월 주기
                                }else if(meetingVO.getMt_remind_type().equals(4)){
                                    if(_cnt > 12){
                                        resultVO.setResult_str("월 단위 되풀이 주기는 12개월까지 입니다.");
                                        _chkDup = 1;
                                        return resultVO;
                                    }else{
                                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                        if(!_dayChk.equals(0)){
                                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 "+ _edittxt +"을 중단합니다.");
                                            _chkDup = 1;
                                            return resultVO;
                                        }
                                    }
                                }

                                /*
                                // 년 주기
                                else if(meetingVO.getMt_remind_type().equals(4)){
                                    if(_cnt > 5){
                                        resultVO.setResult_str("년 단위 되풀이 주기는 5년까지 입니다.");
                                        _chkDup = 1;
                                        return resultVO;
                                    }else{
                                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                        if(!_dayChk.equals(0)){
                                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 "+ _edittxt +"을 중단합니다.");
                                            _chkDup = 1;
                                            return resultVO;
                                        }
                                    }
                                }
                                 */

                                // 기간 제한 통과 && 중복 날짜 통과
                                if(_chkDup==0){
                                    // 미팅룸 참여자 삭제 리스트
                                    if(meetingVO.getInvite_del() != null && meetingVO.getInvite_del() != ""){
                                        String[] inInvite = meetingVO.getInvite_del().split(",");
                                        for(String uidx : inInvite){
                                            MeetingVO ee = new MeetingVO();
                                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                            ee.setIdx_user(Integer.valueOf(uidx));
                                            meetMapper.meet_invite_del(ee);
                                        }
                                    }

                                    // 미팅룸 첨부파일 삭제
                                    if(meetingVO.getFile_del() != null && meetingVO.getFile_del() != ""){
                                        String[] fileIdx = meetingVO.getFile_del().split(",");
                                        for(String fidx : fileIdx){
                                            MeetingVO ee = new MeetingVO();
                                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                            ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));
                                            fileServiceMapper.getMeetFile(ee);

                                            // 파일 삭제
                                            MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                            if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                File f = new File(this.filepath + finfo.getFile_path() + finfo.getFile_name());
                                                if(f.exists()){
                                                    f.delete();
                                                }
                                                fileServiceMapper.delMeetFile(ee);
                                            }
                                        }
                                    }

                                    Integer _idx = 0;
                                    List<MeetingVO> _frss = new ArrayList<>();

                                    // 일 주기
                                    if(meetingVO.getMt_remind_type().equals(1)){
                                        for(Integer i=0;i<_cnt;i++){
                                            String _sdate = getCalDate(_sd, 0, 0, i);
                                            String _edate = getCalDate(_ed, 0, 0, i);
                                            param.setMt_start_dt(_sdate);
                                            param.setMt_end_dt(_edate);
                                            if(i>0){
                                                param.setMt_remind_type(0);
                                                param.setMt_remind_count(0);
                                                param.setMt_remind_week(null);
                                                param.setMt_remind_end(null);
                                            }
                                            if(getDateDiff(_sdate, _enddt)<0){
                                                if(i==0){
                                                    rs = meetMapper.meet_modify(meetingVO);
                                                    if(rs > 0){
                                                        // 미팅룸 참여자 추가 저장
                                                        meetingService.saveMeetInvite(meetingVO);

                                                        // 미팅룸 첨부파일 저장
                                                        _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                        if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                                            meetingService.sendMail(meetingVO, rrs, 7);
                                                        }

                                                        resultVO.setResult_code(CONSTANT.success);
                                                        resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                    }else{
                                                        resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                                    }
                                                }else{
                                                    resultVO = meetingService.createMeetRoom(req, param);
                                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                    // 미팅룸 기존파일 가져오기
                                                    if(i==1 && meetingVO.getOri_file() != null && meetingVO.getOri_file() != ""){
                                                        String[] fileIdx = meetingVO.getOri_file().split(",");
                                                        for(String fidx : fileIdx){
                                                            MeetingVO ee = new MeetingVO();
                                                            ee.setIdx_meeting(rrs.getIdx_meeting());
                                                            ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                                            MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                                            if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                                finfo.setIdx_meeting(_idx);
                                                                _frss.add(finfo);
                                                            }
                                                        }
                                                    }

                                                    // 미팅룸 첨부파일 복사
                                                    if(!_idx.equals(0)){
                                                        meetingService.meetFileCopy(_idx, _frss);
                                                    }

                                                    resultVO.setResult_code(CONSTANT.success);
                                                    resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                }
                                            }
                                        }
                                    // 주 주기
                                    }else if(meetingVO.getMt_remind_type().equals(2)){
                                        Integer _fcnt = 0;
                                        _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택

                                        for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                            for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                                Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*7));
                                                for(String _w : _week){ // 입력 값 체크
                                                    if(Integer.valueOf(_w).equals(_dw)){
                                                        String _sdate = getCalDate(_sd, 0, 0, j+((i-1)*7));
                                                        String _edate = getCalDate(_ed, 0, 0, j+((i-1)*7));
                                                        param.setMt_start_dt(_sdate);
                                                        param.setMt_end_dt(_edate);
                                                        if(_fcnt>0){
                                                            param.setMt_remind_type(0);
                                                            param.setMt_remind_count(0);
                                                            param.setMt_remind_week("");
                                                            param.setMt_remind_end(null);
                                                        }
                                                        if(getDateDiff(_sdate, _enddt)<0){
                                                            if(_fcnt==0){
                                                                rs = meetMapper.meet_modify(meetingVO);
                                                                if(rs > 0){
                                                                    // 미팅룸 참여자 추가 저장
                                                                    meetingService.saveMeetInvite(meetingVO);
                
                                                                    // 미팅룸 첨부파일 저장
                                                                    _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                                    if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                                                        meetingService.sendMail(meetingVO, rrs, 7);
                                                                    }

                                                                    resultVO.setResult_code(CONSTANT.success);
                                                                    resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                                    _fcnt++;
                                                                }else{
                                                                    resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                                                }
                                                            }else{
                                                                resultVO = meetingService.createMeetRoom(req, param);
                                                                _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                                System.out.println("_idx = " + _idx);
                                                                System.out.println("_fcnt = " + _fcnt);

                                                                // 미팅룸 기존파일 가져오기
                                                                if(_fcnt==1 && meetingVO.getOri_file() != null && meetingVO.getOri_file() != ""){
                                                                    String[] fileIdx = meetingVO.getOri_file().split(",");
                                                                    for(String fidx : fileIdx){
                                                                        MeetingVO ee = new MeetingVO();
                                                                        ee.setIdx_meeting(rrs.getIdx_meeting());
                                                                        ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                                                        MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                                                        if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                                            finfo.setIdx_meeting(_idx);
                                                                            _frss.add(finfo);
                                                                        }
                                                                    }
                                                                }

                                                                // 미팅룸 첨부파일 복사
                                                                if(!_idx.equals(0)){
                                                                    meetingService.meetFileCopy(_idx, _frss);
                                                                }

                                                                ++_fcnt;

                                                                resultVO.setResult_code(CONSTANT.success);
                                                                resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    // 격주 주기
                                    }else if(meetingVO.getMt_remind_type().equals(3)){
                                        Integer _fcnt = 0;
                                        _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                                        Integer _sdw = getCalDayOfWeek(_sd, 0, 0, 0);
                                        for(Integer i=1;i<=_cnt;i++){   // 주 반복
                                            for(Integer j=0;j<=6;j++){  // 일~월 1~7
                                                Integer _dw = getCalDayOfWeek(_sd, 0, 0, j+((i-1)*7));
                                                for(String _w : _week){ // 입력 값 체크
                                                    if(Integer.valueOf(_w).equals(_dw)){
                                                        String _sdate = "";
                                                        String _edate = "";
                                                        //반복하고자 하는 요일이 오늘(요일) 보다 이후
                                                        if(Integer.valueOf(_w) >= _sdw){
                                                            _sdate = getCalDate(_sd, 0, 0, j+((i-1)*14));
                                                            _edate = getCalDate(_ed, 0, 0, j+((i-1)*14));
                                                            param.setMt_start_dt(_sdate);
                                                            param.setMt_end_dt(_edate);
                                                        //반복하고자 하는 요일이 오늘(요일) 보다 이전
                                                        } else if(Integer.valueOf(_w) < _sdw){
                                                            _sdate = getCalDate(_sd, 0, 0, 7+j+((i-1)*14));
                                                            _edate = getCalDate(_ed, 0, 0, 7+j+((i-1)*14));
                                                            param.setMt_start_dt(_sdate);
                                                            param.setMt_end_dt(_edate);
                                                        }
                                                        if(_fcnt>0){
                                                            param.setMt_remind_type(0);
                                                            param.setMt_remind_count(0);
                                                            param.setMt_remind_week("");
                                                            param.setMt_remind_end(null);
                                                        }
                                                        if(getDateDiff(_sdate, _enddt)<0){
                                                            if(_fcnt==0){
                                                                rs = meetMapper.meet_modify(meetingVO);
                                                                if(rs > 0){
                                                                    // 미팅룸 참여자 추가 저장
                                                                    meetingService.saveMeetInvite(meetingVO);

                                                                    // 미팅룸 첨부파일 저장
                                                                    _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                                    if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                                                        meetingService.sendMail(meetingVO, rrs, 7);
                                                                    }

                                                                    resultVO.setResult_code(CONSTANT.success);
                                                                    resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                                    _fcnt++;
                                                                }else{
                                                                    resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                                                }
                                                            }else{
                                                                resultVO = meetingService.createMeetRoom(req, param);
                                                                _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                                // 미팅룸 기존파일 가져오기
                                                                if(_fcnt==1 && meetingVO.getOri_file() != null && meetingVO.getOri_file() != ""){
                                                                    String[] fileIdx = meetingVO.getOri_file().split(",");
                                                                    for(String fidx : fileIdx){
                                                                        MeetingVO ee = new MeetingVO();
                                                                        ee.setIdx_meeting(rrs.getIdx_meeting());
                                                                        ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                                                        MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                                                        if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                                            finfo.setIdx_meeting(_idx);
                                                                            _frss.add(finfo);
                                                                        }
                                                                    }
                                                                }

                                                                // 미팅룸 첨부파일 복사
                                                                if(!_idx.equals(0)){
                                                                    meetingService.meetFileCopy(_idx, _frss);
                                                                }

                                                                _fcnt++;

                                                                resultVO.setResult_code(CONSTANT.success);
                                                                resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    // 월 주기
                                    }else if(meetingVO.getMt_remind_type().equals(4)){

                                        String _startdt = rrs.getMt_start_dt();

                                        if(meetingVO.getMt_remind_monthType() == 1) {
                                            String _sdM = _sd.substring(0,8) + meetingVO.getMt_remind_monthDay() + _sd.substring(10);
                                            String _edM = _ed.substring(0,8) + meetingVO.getMt_remind_monthDay() + _ed.substring(10);

                                            int creatN = 0;
                                            int i = 0;

                                            while (creatN <_cnt){
                                                String _sdate = getCalDate(_sdM, 0, i, 0);
                                                String _edate = getCalDate(_edM, 0, i, 0);
                                                param.setMt_start_dt(_sdate);
                                                param.setMt_end_dt(_edate);
                                                if(creatN>0){
                                                    param.setMt_remind_type(0);
                                                    param.setMt_remind_count(0);
                                                    param.setMt_remind_week("");
                                                    param.setMt_remind_end(null);
                                                }

                                                String _loofday = _sdate.substring(8,10);

                                                if(getDateDiff(_sdate, _enddt)<0){
                                                    if(_loofday.equals(meetingVO.getMt_remind_monthDay()) && getDateDiff(_startdt, _sdate)< 0){
                                                        if(creatN==0){
                                                            rs = meetMapper.meet_modify(meetingVO);
                                                            if(rs > 0){
                                                                // 미팅룸 참여자 추가 저장
                                                                meetingService.saveMeetInvite(meetingVO);

                                                                // 미팅룸 첨부파일 저장
                                                                _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                                if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                                                    meetingService.sendMail(meetingVO, rrs, 7);
                                                                }
                                                                
                                                                resultVO.setResult_code(CONSTANT.success);
                                                                resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                            }else{
                                                                resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                                            }
                                                        }else{
                                                            resultVO = meetingService.createMeetRoom(req, param);
                                                            _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                            // 미팅룸 기존파일 가져오기
                                                            if(creatN==1 && meetingVO.getOri_file() != null && meetingVO.getOri_file() != ""){
                                                                String[] fileIdx = meetingVO.getOri_file().split(",");
                                                                for(String fidx : fileIdx){
                                                                    MeetingVO ee = new MeetingVO();
                                                                    ee.setIdx_meeting(rrs.getIdx_meeting());
                                                                    ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                                                    MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                                                    if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                                        finfo.setIdx_meeting(_idx);
                                                                        _frss.add(finfo);
                                                                    }
                                                                }
                                                            }

                                                            // 미팅룸 첨부파일 복사
                                                            if(!_idx.equals(0)){
                                                                meetingService.meetFileCopy(_idx, _frss);
                                                            }
                                                            resultVO.setResult_code(CONSTANT.success);
                                                            resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                        }
                                                        ++creatN;
                                                    }
                                                } else { break; }
                                                ++i;
                                            }
                                        }

                                        if(meetingVO.getMt_remind_monthType() == 2) {
                                            int sequence = meetingVO.getMt_remind_sequence();   // 요일이 해당되는 순차 (n번째 요일)
                                            String week = meetingVO.getMt_remind_week();   // 요일 선택

                                            Calendar cal = Calendar.getInstance();
                                            cal.set(Integer.parseInt(_sd.substring(0,4)), Integer.parseInt(_sd.substring(5,7))-1, Integer.parseInt(_sd.substring(8,10)), Integer.parseInt(_sd.substring(11,13)), Integer.parseInt(_sd.substring(14,16)));

                                            int creatN = 0;
                                            int i = 0;

                                            while (creatN <_cnt) {
                                                cal.set(cal.DAY_OF_WEEK, Integer.valueOf(week));
                                                if(sequence <= 4){
                                                    cal.set(cal.DAY_OF_WEEK_IN_MONTH, sequence);
                                                } else if(sequence==5){
                                                    cal.set(cal.DAY_OF_WEEK_IN_MONTH, -1);
                                                }
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String _sdate = format.format(cal.getTime());
                                                String _edate = _sdate.substring(0,10) + _ed.substring(10);

                                                param.setMt_start_dt(_sdate);
                                                param.setMt_end_dt(_edate);
                                                if(creatN>0){
                                                    param.setMt_remind_type(0);
                                                    param.setMt_remind_count(0);
                                                    param.setMt_remind_week("");
                                                    param.setMt_remind_end(null);
                                                }

                                                if(getDateDiff(_sdate, _enddt)<0) {
                                                    if(getDateDiff(_sd, _sdate)<0) {
                                                        if (creatN == 0) {
                                                            rs = meetMapper.meet_modify(meetingVO);
                                                            if (rs > 0) {
                                                                // 미팅룸 참여자 추가 저장
                                                                meetingService.saveMeetInvite(meetingVO);

                                                                // 미팅룸 첨부파일 저장
                                                                _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                                if (rrs.getMt_status() == 1 && (getDateTimeDiff(rrs.getMt_start_dt(), meetingVO.getMt_start_dt()) != 0 || getDateTimeDiff(rrs.getMt_end_dt(), meetingVO.getMt_end_dt()) != 0)) {
                                                                    meetingService.sendMail(meetingVO, rrs, 7);
                                                                }
                                                                resultVO.setResult_code(CONSTANT.success);
                                                                resultVO.setResult_str("미팅룸을 " + _edittxt + "하였습니다.");
                                                            } else {
                                                                resultVO.setResult_str("미팅룸 " + _edittxt + "이 실패 되었습니다.");
                                                            }
                                                        } else {
                                                            resultVO = meetingService.createMeetRoom(req, param);
                                                            _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                            // 미팅룸 기존파일 가져오기
                                                            if(creatN==1 && meetingVO.getOri_file() != null && meetingVO.getOri_file() != ""){
                                                                String[] fileIdx = meetingVO.getOri_file().split(",");
                                                                for(String fidx : fileIdx){
                                                                    MeetingVO ee = new MeetingVO();
                                                                    ee.setIdx_meeting(rrs.getIdx_meeting());
                                                                    ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));

                                                                    MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                                                    if(StringUtils.isNotEmpty(finfo.getFile_name())){
                                                                        finfo.setIdx_meeting(_idx);
                                                                        _frss.add(finfo);
                                                                    }
                                                                }
                                                            }

                                                            // 미팅룸 첨부파일 복사
                                                            if(!_idx.equals(0)){
                                                                meetingService.meetFileCopy(_idx, _frss);
                                                            }
                                                            resultVO.setResult_code(CONSTANT.success);
                                                            resultVO.setResult_str("미팅룸을 " + _edittxt + "하였습니다.");
                                                        }
                                                        ++creatN;
                                                    }
                                                }else { break; }
                                                ++i;
                                                cal.add(Calendar.MONTH, 1);
                                            }
                                        }
                                    }

                                    /*
                                    // 년 주기
                                    else if(meetingVO.getMt_remind_type().equals(4)){
                                        for(Integer i=0;i<_cnt;i++){
                                            String _sdate = getCalDate(_sd, i, 0, 0);
                                            String _edate = getCalDate(_ed, i, 0, 0);
                                            param.setMt_start_dt(_sdate);
                                            param.setMt_end_dt(_edate);
                                            if(i>0){
                                                param.setMt_remind_type(0);
                                                param.setMt_remind_count(0);
                                                param.setMt_remind_week("");
                                                param.setMt_remind_end(null);
                                            }
                                            if(getDateDiff(_sdate, _enddt)<0){
                                                if(i==0){
                                                    rs = meetMapper.meet_modify(meetingVO);
                                                    if(rs > 0){
                                                        // 미팅룸 참여자 추가 저장
                                                        meetingService.saveMeetInvite(meetingVO);

                                                        // 미팅룸 첨부파일 저장
                                                        _frss = meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                                        if(rrs.getMt_status()==1 && (getDateTimeDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0 || getDateTimeDiff(rrs.getMt_end_dt(),meetingVO.getMt_end_dt())!=0)){
                                                            meetingService.sendMail(meetingVO, rrs, 7);
                                                        }

                                                        resultVO.setResult_code(CONSTANT.success);
                                                        resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                    }else{
                                                        resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                                    }
                                                }else{
                                                    resultVO = meetingService.createMeetRoom(req, param);
                                                    _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                    // 미팅룸 첨부파일 복사
                                                    if(!_idx.equals(0)){
                                                        meetingService.meetFileCopy(_idx, _frss);
                                                    }
                                                    resultVO.setResult_code(CONSTANT.success);
                                                    resultVO.setResult_str("미팅룸을 "+ _edittxt +"하였습니다.");
                                                }
                                            }
                                        }
                                    }
                                    */
                                    else{
                                        resultVO.setResult_str("미팅룸 "+ _edittxt +"이 실패 되었습니다.");
                                    }
                                    resultVO.setData(null);
                                }
                            }
                        }
                    }else{
                        resultVO.setResult_str("이용권한이 없습니다.");
                    }
                }else{
                    resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅 시작
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/room/start")
    public ResultVO putMeetStart(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO urs = getChkUserLogin(req);
            if(urs==null){
                resultVO.setResult_str("로그인 후에 이용해주세요.");
                return resultVO;
            }else{
                meetingVO.setIdx_user(urs.getIdx_user());   // 미팅룸 호스트
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getMt_status() != 1){
                        resultVO.setResult_str("미팅룸이 공개 상태에서만 시작할 수 있습니다.");
                        return resultVO;
                    }
                    Integer _auth = 0; // 게스트 권한 부여
                    
                    // 호스트 권한 부여
                    if(rrs.getIdx_user().equals(urs.getIdx_user())){
                        _auth = 1;
                    }
                    
                    if(rrs.getIs_finish() == 1){
                        if(_auth==1){
                            resultVO.setResult_str("미팅룸이 종료되어 시작할 수 없습니다.");
                        }else{
                            resultVO.setResult_str("미팅룸이 종료되어 참여할 수 없습니다.");
                        }
                        return resultVO;
                    }
                    // 미팅 10분전에는 시작 못하게 막기 --------------//
                    if(getTimeDiff(rrs.getMt_start_dt(), 600)==0){
                        if(_auth==1){
                            resultVO.setResult_str("미팅 시간 10분 전부터 시작할 수 있습니다.");
                        }else{
                            resultVO.setResult_str("미팅 시간 10분 전부터 참여할 수 있습니다.");
                        }
                        return resultVO;
                    }
                    // ---------------------------------------//

                    // 미팅에 참여중인지 확인
                    MeetingVO _chkMeet = meetMapper.chkMeetLiveJoin(meetingVO);
                    if(_chkMeet != null && StringUtils.isNotEmpty(_chkMeet.getJoin_dt())){
                        Map<String, Object> _rs = new HashMap<String, Object>();
                        _rs.put("mcid", "euraclass" + meetingVO.getIdx_meeting().toString());
                        _rs.put("token", _chkMeet.getToken());
                        resultVO.setData(_rs);

                        resultVO.setResult_code(CONSTANT.success);
                        resultVO.setResult_str("이미 미팅에 참여를 시작하여 미팅 장소로 이동합니다.");
                    }else{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date edate = dateFormat.parse(rrs.getMt_end_dt());
                        Long _sDt = Instant.now().getEpochSecond(); // 현재 시간
                        Long _eDt = edate.getTime();
        
                        String _userPk = "euraclass" + rrs.getIdx_meeting().toString();
                        Map<String, Object> _data = new HashMap<String, Object>();
                        _data.put("userid",urs.getUser_id());
                        String _jwt = tokenJWT.createToken(_userPk, _sDt, _eDt, rrs.getMt_end_dt(), _data, _auth);
                        meetingVO.setToken(_jwt);
                        meetingVO.setSessionid(_userPk);
        
                        Map<String, Object> _rs = new HashMap<String, Object>();
                        _rs.put("mcid", "euraclass" + meetingVO.getIdx_meeting().toString());
                        _rs.put("token", _jwt);
                        resultVO.setData(_rs);
                        
                        // 호스트
                        if(_auth == 1){
                            Integer rs = meetMapper.putMeetLiveStart(meetingVO);
                            if(rs==1){
                                meetMapper.putMeetLiveJoin(meetingVO);  // 미팅룸에 들어가기용 데이터 저장
                                
                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅을 시작하였습니다.");
                                
                                // 참가자 이메일 전송
                                meetingService.sendMail(meetingVO, rrs, 1);
                            }else{
                                resultVO.setResult_str("미팅을 시작할 수 없습니다.");
                            }
                        
                        // 참가자
                        }else{
                            meetMapper.putMeetLiveJoin(meetingVO);  // 미팅룸에 들어가기용 데이터 저장

                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸에 참여합니다.");
                        }
                    }
                }else{
                    resultVO.setResult_str("미팅룸 정보가 존재하지 않습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅룸 종료
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PutMapping("/room/finish")
    public ResultVO closeLiveMeeting(HttpServletRequest req, MeetingVO meetingVO) throws Exception{
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try{
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }
            meetingVO.setIdx_user(uInfo.getIdx_user());
            MeetingVO _rs = meetMapper.getRoomInfo(meetingVO);
            if(_rs.getIdx_user().equals(uInfo.getIdx_user())){
                if(_rs.getIs_live() == 1){
                    meetMapper.closeMeet(meetingVO);
                    resultVO.setResult_code(CONSTANT.success);
                    resultVO.setResult_str("강의를 종료하였습니다.");
                } else {
                    resultVO.setResult_code(CONSTANT.fail);
                    resultVO.setResult_str("해당 강의가 진행중이 아닙니다.");
                }
            }else{
                resultVO.setResult_str("호스트만 종료할 수 있습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }
}
