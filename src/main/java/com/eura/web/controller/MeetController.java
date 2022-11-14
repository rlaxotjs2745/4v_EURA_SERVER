/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.base.BaseController;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ProfileInfoVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.service.MeetingService;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;
import com.eura.web.util.TokenJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/meet")
public class MeetController extends BaseController {
    private final UserService userService;
    private final MeetMapper meetMapper;
    private final FileServiceMapper fileServiceMapper;
    private final TokenJWT tokenJWT;
    private final MeetingService meetingService;
    private final UserMapper userMapper;

    @Value("${file.upload-dir}")
    private String filepath;

    @Value("${domain}")
    private String domain;

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
            
            // 개인화 - 개인정보
            UserVO uInfo = userService.findUserById(getUserID(req));
            if(uInfo!=null){
                _rs.put("ui_name", uInfo.getUser_name());
                ProfileInfoVO uPic = fileServiceMapper.selectUserProfileFile(uInfo.getIdx_user());
                String _upic = "";
                if(!uPic.getFile_name().isEmpty() && uPic.getFile_name() != null){
                    _upic = domain + "/pic?fnm=" + uPic.getFile_path() + uPic.getFile_name();
                }
                _rs.put("ui_pic", _upic);
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() == null){
                meetingVO.setCurrentPage(1);
            }
            meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
            meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            Map<String, Object> _rs = new HashMap<String, Object>();

            Long mInfoCnt = meetMapper.getMyMeetListCount(1);   // 참여중인 미팅룸 총 수

            if(meetingVO.getPageSort()==null){
                meetingVO.setPageSort(2);
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() == null){
                meetingVO.setCurrentPage(1);
            }
            meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
            meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            Map<String, Object> _rs = new HashMap<String, Object>();

            // 개인화 - 지난 미팅
            if(meetingVO.getPageSort()==null){
                meetingVO.setPageSort(2);
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
                UserVO uInfo = userService.findUserById(getUserID(req));
                Map<String, Object> _rs = new HashMap<String, Object>();
                
                userVO.setIdx_user(uInfo.getIdx_user());
                List<UserVO> irs = userMapper.getUserSearch(userVO);
                ArrayList<Object> _irss = new ArrayList<Object>();
                for(UserVO irs0 : irs){
                    Map<String, Object> _irs = new HashMap<String, Object>();
                    _irs.put("idx", irs0.getIdx_user());
                    _irs.put("uname", irs0.getUser_name());
                    _irs.put("email", irs0.getUser_email());
                    String _upic = "";
                    if(!irs0.getFile_name().isEmpty() && irs0.getFile_name() != null){
                        _upic = domain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);

                String _auth = "0"; // 게스트 권한 부여

                // 호스트 권한 부여
                if(rs.getIdx_user().equals(uInfo.getIdx_user())){
                    _auth = "1";
                }
                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_useridx", rs.getIdx_user());
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

                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    Map<String, Object> _frs = new HashMap<String, Object>();
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_name());
                    _frs.put("download", domain + "/download?fnm=" + frs0.getFile_path() + frs0.getFile_name());
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            Map<String, Object> _rs = new HashMap<String, Object>();
            
            meetingVO.setIdx_user(uInfo.getIdx_user());
            if(meetingVO.getCurrentPage() == null){
                meetingVO.setCurrentPage(1) ;
            }
            meetingVO.setRecordCountPerPage(CONSTANT.default_pageblock);
            meetingVO.setFirstIndex((meetingVO.getCurrentPage()-1) * CONSTANT.default_pageblock);
            List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);
            ArrayList<Object> _irss = new ArrayList<Object>();
            for(MeetingVO irs0 : irs){
                Map<String, Object> _irs = new HashMap<String, Object>();
                _irs.put("idx", irs0.getIdx_user());
                _irs.put("uname", irs0.getUser_name());
                _irs.put("email", irs0.getUser_email());
                _irs.put("is_live", irs0.getIs_live());
                _irs.put("is_alive", irs0.getIs_alive());
                String _pic = "";
                if(irs0.getFile_name()!="" && irs0.getFile_name() != null){
                    _pic = domain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
                }
                _irs.put("ui_pic", _pic);
                _irss.add(_irs);
            }
            _rs.put("mt_invites", _irss);
            
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
    public ResultVO putMeetOpen(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
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
    @PutMapping("/room/close")
    public ResultVO putMeetClose(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 비공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
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
    @PutMapping("/room/cancel")
    public ResultVO putMeetCacncel(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                    Integer rs = meetMapper.putMeetCacncel(meetingVO);
                    if(rs==1){
                        // 참가자 이메일 전송
                        meetingService.sendMail(meetingVO, rrs, 3);

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
    @DeleteMapping("/room/erase")
    public ResultVO deleteMeet(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                    Integer rs = meetMapper.deleteMeet(meetingVO);
                    if(rs==1){
                        // 참가자 이메일 전송
                        meetingService.sendMail(meetingVO, rrs, 3);

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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());
            long time = System.currentTimeMillis(); 
            SimpleDateFormat tYear = new SimpleDateFormat("yyyy");
            SimpleDateFormat tMonth = new SimpleDateFormat("mm");
            Integer _tYear = Integer.valueOf(tYear.format(new Date(time)));
            Integer _tMonth = Integer.valueOf(tMonth.format(new Date(time)));
            if(meetingVO.getCalYear() == null){
                meetingVO.setCalYear(_tYear);
            }
            if(meetingVO.getCalMonth() == null){
                meetingVO.setCalYear(_tMonth);
            }
            Map<String, Object> _rs = new HashMap<String, Object>();
            List<MeetingVO> mInfo = meetMapper.getMyMeetList(meetingVO);    // 참여중인 미팅룸
            ArrayList<Object> _mrss = new ArrayList<Object>();
            for(MeetingVO rs0 : mInfo){
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("mt_date", rs0.getMt_start_dt());
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());

            Map<String, Object> _rs = new HashMap<String, Object>();
            ArrayList<Object> _mrss = new ArrayList<Object>();
            List<MeetingVO> mInfo = meetMapper.getMyMeetList(meetingVO);    // 참여중인 미팅룸
            for(MeetingVO rs0 : mInfo){
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("hostname", rs0.getUser_name());
                _mrs.put("timer", rs0.getMt_start_dt().substring(11,16) + " - " + rs0.getMt_end_dt().substring(11,16));
                _mrs.put("mt_info", rs0.getMt_info());
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
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);
                List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);

                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_name", rs.getMt_name());
                _rs.put("mt_hostname", rs.getUser_name());
                _rs.put("mt_start_dt", rs.getMt_start_dt());
                _rs.put("mt_end_dt", rs.getMt_info());
                _rs.put("mt_remind_type", rs.getMt_remind_type());
                _rs.put("mt_remind_count", rs.getMt_remind_count());
                _rs.put("mt_remind_week", rs.getMt_remind_week());
                _rs.put("mt_remind_end", rs.getMt_remind_end());
                _rs.put("mt_info", rs.getMt_info());    // 미팅룸 정보
                _rs.put("mt_status", rs.getMt_status());    // 미팅룸 상태 - 0:비공개, 1:공개, 2:취소, 3:삭제
                _rs.put("mt_live", rs.getIs_live());        // 미팅 시작 여부 - 0:아니오, 1:예
                _rs.put("mt_live_dt", rs.getIs_live_dt());        // 미팅 시작 일시
                _rs.put("mt_finish", rs.getIs_finish());    // 미팅 종료 여부 - 0:아니오, 1:예
                _rs.put("mt_finish_dt", rs.getIs_finish_dt());    // 미팅 종료 일시
                _rs.put("mt_regdt", rs.getReg_dt());        // 미팅룸 등록일시

                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    Map<String, Object> _frs = new HashMap<String, Object>();
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_name());
                    _frs.put("download", domain + "/download?fnm=" + frs0.getFile_path() + frs0.getFile_name());
                    _frss.add(_frs);
                }
                _rs.put("mt_files", _frss);     // 미팅 첨부파일

                ArrayList<Object> _irss = new ArrayList<Object>();
                for(MeetingVO irs0 : irs){
                    Map<String, Object> _irs = new HashMap<String, Object>();
                    _irs.put("idx", irs0.getIdx_user());
                    _irs.put("uname", irs0.getUser_name());
                    _irs.put("email", irs0.getUser_email());
                    String _upic = "";
                    if(!irs0.getFile_name().isEmpty() && irs0.getFile_name() != null){
                        _upic = domain + "/pic?fnm=" + irs0.getFile_path() + irs0.getFile_name();
                    }
                    _irs.put("ui_pic", _upic);
                    _irss.add(_irs);
                }
                _rs.put("mt_invites", _irss);   // 미팅 참석자

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
            UserVO uInfo = userService.findUserById(getUserID(req));
            if(meetingVO != null){
                Integer _dayChk = 0;

                // 미팅룸 정보 생성
                meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트

                /*
                * MT_REMIND_TYPE - 0:없음, 1:매일, 2:주, 3:월, 4:년
                * MT_REMIND_COUNT - 주기
                * MT_REMIND_WEEK - 반복요일 - 월,화,수,목,금,토,일
                * MT_REMIND_END - 되풀이 미팅 종료일
                */
                if(meetingVO.getMt_remind_type()==null){
                    meetingVO.setMt_remind_type(0);
                }else{
                    if(!Character.isDigit(meetingVO.getMt_remind_type())){
                        meetingVO.setMt_remind_type(0);
                    }
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
                        resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 생성을 중단합니다.");
                    }

                // 일 주기
                }else if(meetingVO.getMt_remind_type().equals(1)){
                    Integer _cnt = meetingVO.getMt_remind_count();
                    if(_cnt > 90){
                        resultVO.setResult_str("일 단위 되풀이 주기는 90일까지 입니다.");
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
                // 월 주기
                }else if(meetingVO.getMt_remind_type().equals(3)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 월
                    if(_cnt > 12){
                        resultVO.setResult_str("월 단위 되풀이 주기는 12개월까지 입니다.");
                    }else if(meetingVO.getMt_remind_end() == null){
                        resultVO.setResult_str("종료일을 선택해주세요.");
                    }else{
                        // 월 단위 중복 체크
                        MeetingVO param = meetingVO;
                        String _enddt = meetingVO.getMt_remind_end();
                        String _sd = meetingVO.getMt_start_dt();
                        String _ed = meetingVO.getMt_end_dt();

                        _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);

                        if(_dayChk.equals(0)){
                            Integer _idx = 0;
                            List<MeetingVO> _frss = new ArrayList<>();
                            for(Integer i=0;i<_cnt;i++){
                                String _sdate = getCalDate(_sd, 0, i, 0);
                                String _edate = getCalDate(_ed, 0, i, 0);
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
                // 년 주기
                }else if(meetingVO.getMt_remind_type().equals(4)){
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

        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            Integer rs = 0;
            if(meetingVO != null){
                // 미팅룸 정보 수정
                meetingVO.setIdx_user(uInfo.getIdx_user());   // 미팅룸 호스트
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(uInfo.getIdx_user())){
                        if(meetingVO.getMt_remind_type()==null){
                            meetingVO.setMt_remind_type(0);
                        }
                        if(meetingVO.getMt_remind_type().equals(0)){
                            Integer _dayChk = 0;
                            if(getDateDiff(rrs.getMt_start_dt(),meetingVO.getMt_start_dt())!=0){
                                _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, 0, meetingVO);
                            }
                            if(_dayChk.equals(0)){
                                rs = meetMapper.meet_modify(meetingVO);
                                if(rs > 0){
                                    // 미팅룸 참여자 삭제 리스트
                                    if(meetingVO.getInvite_del() != null && meetingVO.getInvite_del() != ""){
                                        String[] inInvite = meetingVO.getInvite_del().split(",");
                                        for(String ue : inInvite){
                                            MeetingVO ee = new MeetingVO();
                                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                            ee.setUser_email(ue);
                                            meetMapper.meet_invite_del(ee);
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
                                                File f = new File(this.filepath + finfo.getFile_path() + finfo.getFile_name());
                                                if(f.exists()){
                                                    f.delete();
                                                }
                                                fileServiceMapper.delMeetFile(ee);
                                            }
                                        }
                                    }

                                    // 미팅룸 참여자 추가 저장
                                    meetingService.saveMeetInvite(meetingVO);

                                    // 미팅룸 첨부파일 저장
                                    meetingService.meetFileSave(req, meetingVO.getIdx_meeting());

                                    resultVO.setResult_code(CONSTANT.success);
                                    resultVO.setResult_str("미팅룸을 수정하였습니다.");
                                }else{
                                    resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
                                }
                            }else{
                                resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 수정을 중단합니다.");
                            }
                            resultVO.setData(null);
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
                            }else{

                            // 일 주기
                            if(meetingVO.getMt_remind_type().equals(1)){
                                if(_cnt > 90){
                                    resultVO.setResult_str("일 단위 되풀이 주기는 90일까지 입니다.");
                                    _chkDup = 1;
                                }else{
                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                    if(!_dayChk.equals(0)){
                                        resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 수정을 중단합니다.");
                                        _chkDup = 1;
                                    }
                                }

                            // 주 주기
                            }else if(meetingVO.getMt_remind_type().equals(2)){
                                if(_cnt > 12){
                                    resultVO.setResult_str("주 단위 되풀이 주기는 12주까지 입니다.");
                                    _chkDup = 1;
                                }else{
                                    if(meetingVO.getMt_remind_week().isEmpty() || meetingVO.getMt_remind_week()==null){
                                        resultVO.setResult_str("주 단위 되풀이 주기는 요일을 선택해주세요.");
                                        _chkDup = 1;
                                    }else{
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
                                                        if(getDateDiff(_sdate, _enddt)<0){
                                                            _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if(!_dayChk.equals(0)){
                                            resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 수정을 중단합니다.");
                                            _chkDup = 1;
                                        }
                                    }
                                }
    
                            // 월 주기
                            }else if(meetingVO.getMt_remind_type().equals(3)){
                                if(_cnt > 12){
                                    resultVO.setResult_str("월 단위 되풀이 주기는 12개월까지 입니다.");
                                    _chkDup = 1;
                                }else{
                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                    if(!_dayChk.equals(0)){
                                        resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 수정을 중단합니다.");
                                        _chkDup = 1;
                                    }
                                }

                            // 년 주기
                            }else if(meetingVO.getMt_remind_type().equals(4)){
                                if(_cnt > 5){
                                    resultVO.setResult_str("년 단위 되풀이 주기는 5년까지 입니다.");
                                    _chkDup = 1;
                                }else{
                                    _dayChk = meetingService.chkRoomDup(meetingVO.getMt_remind_type(), _dayChk, _cnt, meetingVO);
                                    if(!_dayChk.equals(0)){
                                        resultVO.setResult_str("되풀이 주기 중 중복 일정이 있어 미팅룸 수정을 중단합니다.");
                                        _chkDup = 1;
                                    }
                                }
                            }

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
                                        File f = new File(this.filepath + finfo.getFile_path() + finfo.getFile_name());
                                        if(f.exists()){
                                            f.delete();
                                        }
                                        fileServiceMapper.delMeetFile(ee);
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

                                                    resultVO.setResult_code(CONSTANT.success);
                                                    resultVO.setResult_str("미팅룸을 수정하였습니다.");
                                                }else{
                                                    resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
                                                }
                                            }else{
                                                resultVO = meetingService.createMeetRoom(req, param);
                                                _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                // 미팅룸 첨부파일 복사
                                                if(!_idx.equals(0)){
                                                    meetingService.meetFileCopy(_idx, _frss);
                                                }
                                                resultVO.setResult_code(CONSTANT.success);
                                                resultVO.setResult_str("미팅룸을 수정 & 생성하였습니다.");
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
            
                                                                resultVO.setResult_code(CONSTANT.success);
                                                                resultVO.setResult_str("미팅룸을 수정하였습니다.");
                                                                _fcnt++;
                                                            }else{
                                                                resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
                                                            }
                                                        }else{
                                                            resultVO = meetingService.createMeetRoom(req, param);
                                                            _idx = Integer.valueOf(resultVO.getData().get("key").toString());
            
                                                            // 미팅룸 첨부파일 복사
                                                            if(!_idx.equals(0)){
                                                                _fcnt++;
                                                                meetingService.meetFileCopy(_idx, _frss);
                                                            }
                                                            resultVO.setResult_code(CONSTANT.success);
                                                            resultVO.setResult_str("미팅룸을 수정 & 생성하였습니다.");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                // 월 주기
                                }else if(meetingVO.getMt_remind_type().equals(3)){
                                    for(Integer i=0;i<_cnt;i++){
                                        String _sdate = getCalDate(_sd, 0, i, 0);
                                        String _edate = getCalDate(_ed, 0, i, 0);
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

                                                    resultVO.setResult_code(CONSTANT.success);
                                                    resultVO.setResult_str("미팅룸을 수정하였습니다.");
                                                }else{
                                                    resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
                                                }
                                            }else{
                                                resultVO = meetingService.createMeetRoom(req, param);
                                                _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                // 미팅룸 첨부파일 복사
                                                if(!_idx.equals(0)){
                                                    meetingService.meetFileCopy(_idx, _frss);
                                                }
                                                resultVO.setResult_code(CONSTANT.success);
                                                resultVO.setResult_str("미팅룸을 수정 & 생성하였습니다.");
                                            }
                                        }
                                    }
                                // 년 주기
                                }else if(meetingVO.getMt_remind_type().equals(4)){
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

                                                    resultVO.setResult_code(CONSTANT.success);
                                                    resultVO.setResult_str("미팅룸을 수정하였습니다.");
                                                }else{
                                                    resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
                                                }
                                            }else{
                                                resultVO = meetingService.createMeetRoom(req, param);
                                                _idx = Integer.valueOf(resultVO.getData().get("key").toString());

                                                // 미팅룸 첨부파일 복사
                                                if(!_idx.equals(0)){
                                                    meetingService.meetFileCopy(_idx, _frss);
                                                }
                                                resultVO.setResult_code(CONSTANT.success);
                                                resultVO.setResult_str("미팅룸을 수정 & 생성하였습니다.");
                                            }
                                        }
                                    }
                                }else{
                                    resultVO.setResult_str("미팅룸 수정이 실패 되었습니다.");
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
    public ResultVO putMeetStart(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO urs =  getChkUserLogin(req);
            if(urs==null){
                resultVO.setResult_str("로그인 후에 이용해주세요.");
            }else{
                meetingVO.setIdx_user(urs.getIdx_user());   // 미팅룸 호스트
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    Integer _auth = 0; // 게스트 권한 부여

                    // 호스트 권한 부여
                    if(rrs.getIdx_user().equals(urs.getIdx_user())){
                        _auth = 1;
                    }

                    // 미팅에 참여중인지 확인
                    MeetingVO _chkMeet = meetMapper.chkMeetLiveJoin(meetingVO);
                    if(_chkMeet != null){
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
                            if(rrs.getIs_live().equals(1)){
                                meetMapper.putMeetLiveJoin(meetingVO);  // 미팅룸에 들어가기용 데이터 저장

                                resultVO.setResult_code(CONSTANT.success);
                                resultVO.setResult_str("미팅룸에 참여합니다.");
                            }else{
                                resultVO.setResult_str("미팅이 시작하지 않아 참여가 불가합니다.");
                            }
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
     * 강의실 종료
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
            UserVO uInfo = userService.findUserById(getUserID(req));
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

    /**
     * 미팅 분석 결과(미팅 정보)
     * @param req
     * @param meetingVO(idx_meeting)
     * @return
     * @throws Exception
     */
    @GetMapping("/result/meeting")
    public ResultVO getResultMeeting(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");
        try {
            UserVO uInfo = userService.findUserById(getUserID(req));
            meetingVO.setIdx_user(uInfo.getIdx_user());
            MeetingVO meetingInfo = meetMapper.getRoomInfo(meetingVO);

            Map<String, Object> _rs = new HashMap<String, Object>();

            // 호스트명
            _rs.put("mtName", meetingInfo.getMt_name());

            // 날짜
            _rs.put("mtMeetiDate", "2022-11-11");

            // 시간
            _rs.put("mtMeetiTime", "09:00 ~ 10:00");

            // 감정 데이터 리스팅
            // AnalysisVO analyinfo = analysisMapper.getAnalysisData(meetingVO);

            // 영상 파일 : IDX_MOVIE_FILE, FILE_NO, FILE_PATH, FILE_NAME, DURATION, RECORD_DT
            List<MeetingVO> _mlists = fileServiceMapper.getMeetMovieFile(meetingVO);
            ArrayList<Object> _mls = new ArrayList<Object>();
            for(MeetingVO _mlist : _mlists){
                Map<String, Object> _ul = new HashMap<String, Object>();
                _ul.put("idx",_mlist.getIdx_movie_file());    // 참석자 회원 INDEX
                _ul.put("fileNo",_mlist.getFile_no()); // 파일 순서
                _ul.put("duration",_mlist.getDuration());   // 재생 길이
                _ul.put("recordDt",_mlist.getRecord_dt());    // 녹화 시작 시간
                _ul.put("fileUrl", domain + "/pic?fnm="+ _mlist.getFile_path() + _mlist.getFile_name());    // 영상
                _mls.add(_ul);
            }
            _rs.put("mtMovieFiles", _mls);

            List<MeetingVO> _mlist = fileServiceMapper.getMeetMovieFile(meetingVO);
            String _rdt = "";
            String _edt = meetingInfo.getIs_finish_dt();
            if(_mlist != null && _mlist.size() > 0){
                _rdt = _mlist.get(0).getRecord_dt();
            }else{
                _rdt = meetingInfo.getIs_live_dt();
            }


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long diff = format.parse(_edt).getTime() - format.parse(_rdt).getTime();
            String hours = (diff / 1000) / 60 / 60 % 24 < 10 ? "0" + (diff / 1000) / 60 / 60 % 24 : "" + (diff / 1000) / 60 / 60 % 24;
            String minutes = (diff / 1000) / 60 % 60 < 10 ? "0" + (diff / 1000) / 60 % 60 : "" + (diff / 1000) / 60 % 60;
            String seconds = (diff / 1000) % 60 < 10 ? "0" + (diff / 1000) % 60 : "" + (diff / 1000) % 60;

            // 소요시간
            _rs.put("mtMeetTimer", hours + ":" + minutes + ":" + seconds);


            // 미팅 분석 결과(첨부파일)
            _rs.put("mtAttachedFiles", meetMapper.getMeetFiles(meetingVO));

            // 미팅 분석 결과(강의 참여자 명단)
            List<MeetingVO> _ulists = meetMapper.getMeetInvites(meetingVO);
            ArrayList<Object> _uls = new ArrayList<Object>();
            for(MeetingVO _ulist : _ulists){
                Map<String, Object> _ul = new HashMap<String, Object>();
                _ul.put("idx",_ulist.getIdx_user());    // 참석자 회원 INDEX
                _ul.put("uname",_ulist.getUser_name()); // 참석자명
                _ul.put("uemail",_ulist.getUser_email());   // 이메일
                _ul.put("value",66);    // 집중도
                String _upic = "";
                if(!_ulist.getFile_name().isEmpty() && _ulist.getFile_name() != null){
                    _upic = domain + "/pic?fnm=" + _ulist.getFile_path() + _ulist.getFile_name();
                }
                _ul.put("upic",_upic);    // 프로필 사진
                _uls.add(_ul);
            }
            _rs.put("mtInviteList", _uls);

        
            Map<String, Object> _uinfo = new HashMap<String, Object>();
            _uinfo.put("user_total",30);
            _uinfo.put("user_invite",29);
            _rs.put("mtInviteInfo", _uinfo);

            // ArrayList<Object> _tglists = new ArrayList<Object>();
            Map<String, Object> _tglist = new HashMap<String, Object>();
            _tglist.put("good",65);
            _tglist.put("bad",25);
            _tglist.put("off",10);

            // 전체 분석 상단 그래프 데이터
            _rs.put("mtAnalyTop", _tglist);

            // 전체 분석 하단 인디게이터 데이터

            ArrayList<Object> _dlists = new ArrayList<Object>();
            Map<String, Object> _dlist = new HashMap<String, Object>();
            _dlist.put("duration",5);   // 동영상 재생 위치
            _dlist.put("timer","00:00:05"); // duration을 시간으로 환산
            _dlist.put("value",5);  // GOOD~BAD 10 ~ -10
            _dlists.add(_dlist);
            _rs.put("mtAnalyBtm", _dlist);

            // 미구현 내용: 미팅 녹화 내용 담아주어야 함
            // 미구현 내용: 감정 분석 결과 분석으로 집중도 분석 요약 그래프에 들어갈 퍼센트 만들어야 함

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("강의 정보 호출 완료");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * 미팅 참석자 분석 데이터
     * @param req
     * @param meetingVO(idx_meeting)
     * @return
     * @throws Exception
     */
    @GetMapping("/result/mtinviteinfo")
    public ResultVO getResultInviteInfo(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();

            // 인디게이터 데이터
            ArrayList<Object> _dlists = new ArrayList<Object>();
            Map<String, Object> _dlist = new HashMap<String, Object>();
            _dlist.put("duration",5);   // 동영상 재생 위치
            _dlist.put("timer","00:00:05"); // duration을 시간으로 환산
            _dlist.put("value",5);  // GOOD~BAD 10 ~ -10
            _dlists.add(_dlist);

            _rs.put("mtData0", _dlists);

            // 분석 요약 데이터
            Map<String, Object> _d2list = new HashMap<String, Object>();
            _d2list.put("good",62); // GOOD
            _d2list.put("bad",13);  // BAD
            _d2list.put("off",25);  // OFF
            _d2list.put("tcnt",73); // 현재 점수
            _d2list.put("acnt",52); // 누적 평균
            _rs.put("mtData1", _d2list);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅 참석자 분석 데이터 호출 완료");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }
}
