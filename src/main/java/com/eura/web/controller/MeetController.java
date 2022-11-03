/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.base.BaseController;
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
@RequestMapping("/meet")
public class MeetController extends BaseController {
    private final UserService userService;
    private final MeetMapper meetMapper;
    private final FileServiceMapper fileServiceMapper;
    private final TokenJWT tokenJWT;
    private final MeetingService meetingService;

    @Value("${file.upload-dir}")
    private String filepath;

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
            meetingVO.setIdx_user(1);
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
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("mt_hostname", rs0.getUser_name());
                _mrs.put("mt_status", rs0.getMt_status());
                _mrs.put("mt_start_dt", rs0.getMt_start_dt());
                _mrs.put("mt_end_dt", rs0.getMt_end_dt());
                _mrs.put("mt_live", rs0.getIs_live());
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
            meetingVO.setIdx_user(1);
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
                Map<String, Object> _mers = new HashMap<String, Object>();
                _mers.put("mt_idx", rs0.getIdx_meeting());
                _mers.put("mt_name", rs0.getMt_name());
                _mers.put("mt_hostname", rs0.getUser_name());
                _mers.put("mt_status", rs0.getMt_status());
                _mers.put("mt_start_dt", rs0.getMt_start_dt());
                _mers.put("mt_end_dt", rs0.getMt_end_dt());
                _mers.put("mt_live", rs0.getIs_live());
                
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
     * 미팅룸 정보
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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);
                List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);

                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_name", rs.getMt_name());
                _rs.put("mt_hostname", rs.getUser_name());
                _rs.put("mt_start_dt", rs.getMt_info());
                _rs.put("mt_end_dt", rs.getMt_info());
                _rs.put("mt_remind_type", rs.getMt_remind_type());
                _rs.put("mt_remind_count", rs.getMt_remind_count());
                _rs.put("mt_remind_week", rs.getMt_remind_week());
                _rs.put("mt_remind_end", rs.getMt_remind_end());

                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    Map<String, Object> _frs = new HashMap<String, Object>();
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_path() + frs0.getFile_name());
                    _frss.add(_frs);
                }
                _rs.put("mt_files", _frss);

                ArrayList<Object> _irss = new ArrayList<Object>();
                for(MeetingVO irs0 : irs){
                    Map<String, Object> _irs = new HashMap<String, Object>();
                    _irs.put("idx", irs0.getIdx_user());
                    _irs.put("uname", irs0.getUser_name());
                    _irs.put("email", irs0.getUser_email());
                    _irss.add(_irs);
                }
                _rs.put("mt_invites", _irss);

                _rs.put("mt_info", rs.getMt_info());
                _rs.put("mt_status", rs.getMt_status());
                _rs.put("mt_live", rs.getIs_live());
                _rs.put("mt_regdt", rs.getReg_dt());
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
     * 미팅룸 메인
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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);

                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_name", rs.getMt_name());
                _rs.put("mt_hostname", rs.getUser_name());
                _rs.put("mt_start_dt", rs.getMt_info());
                _rs.put("mt_end_dt", rs.getMt_info());
                _rs.put("mt_remind_type", rs.getMt_remind_type());
                _rs.put("mt_remind_count", rs.getMt_remind_count());
                _rs.put("mt_remind_week", rs.getMt_remind_week());
                _rs.put("mt_remind_end", rs.getMt_remind_end());
                _rs.put("mt_info", rs.getMt_info());
                _rs.put("mt_status", rs.getMt_status());
                _rs.put("mt_live", rs.getIs_live());
                _rs.put("mt_regdt", rs.getReg_dt());

                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    Map<String, Object> _frs = new HashMap<String, Object>();
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_path() + frs0.getFile_name());
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
            Map<String, Object> _rs = new HashMap<String, Object>();
            
            meetingVO.setIdx_user(1);
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
            if(meetingVO != null){
                // 미팅룸 정보 생성
                meetingVO.setIdx_user(1);   // 미팅룸 호스트
                /*
                 * MT_REMIND_TYPE - 0:없음, 1:매일, 2:주, 3:월, 4:년
                 * MT_REMIND_COUNT - 주기
                 * MT_REMIND_WEEK - 반복요일 - 월,화,수,목,금,토,일
                 * MT_REMIND_END - 되풀이 미팅 종료일
                 */
                if(meetingVO.getMt_remind_type().equals(0)){
                    resultVO = meetingService.createMeetRoom(req, meetingVO);

                // 일 주기
                }else if(meetingVO.getMt_remind_type().equals(1)){
                    Integer _cnt = meetingVO.getMt_remind_count();
                    if(_cnt > 90){
                        resultVO.setResult_str("일 단위 반복 주기는 90일까지 입니다.");
                    }else{
                        for(Integer i=1;i<=_cnt;i++){
                            String _sdate = getCalDate(meetingVO.getMt_start_dt(), 0, 0, i);
                            String _edate = getCalDate(meetingVO.getMt_end_dt(), 0, 0, i);
                            meetingVO.setMt_start_dt(_sdate);
                            meetingVO.setMt_end_dt(_edate);
                            if(i>1){
                                meetingVO.setMt_remind_type(0);
                                meetingVO.setMt_remind_count(0);
                                meetingVO.setMt_remind_week("");
                                meetingVO.setMt_remind_end(null);
                            }
                            resultVO = meetingService.createMeetRoom(req, meetingVO);
                        }
                    }
                
                // 주 주기
                }else if(meetingVO.getMt_remind_type().equals(2)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 주
                    if(_cnt > 12){
                        resultVO.setResult_str("주 단위 반복 주기는 12주까지 입니다.");
                    }else{
                        String[] _week = meetingVO.getMt_remind_week().split(",");   // 요일 선택
                        for(Integer i=1;i<=_cnt;i++){

                            String _sdate = getCalDate(meetingVO.getMt_start_dt(), 0, 0, 0);
                            String _edate = getCalDate(meetingVO.getMt_end_dt(), 0, 0, 0);
                            meetingVO.setMt_start_dt(_sdate);
                            meetingVO.setMt_end_dt(_edate);
                            if(i>1){
                                meetingVO.setMt_remind_type(0);
                                meetingVO.setMt_remind_count(0);
                                meetingVO.setMt_remind_week("");
                                meetingVO.setMt_remind_end(null);
                            }
                            resultVO = meetingService.createMeetRoom(req, meetingVO);
                        }
                    }
                // 월 주기
                }else if(meetingVO.getMt_remind_type().equals(3)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 월
                    if(_cnt > 12){
                        resultVO.setResult_str("월 단위 반복 주기는 12개월까지 입니다.");
                    }else{
                        for(Integer i=1;i<=_cnt;i++){
                            String _sdate = getCalDate(meetingVO.getMt_start_dt(), 0, i, 0);
                            String _edate = getCalDate(meetingVO.getMt_end_dt(), 0, i, 0);
                            meetingVO.setMt_start_dt(_sdate);
                            meetingVO.setMt_end_dt(_edate);
                            if(i>1){
                                meetingVO.setMt_remind_type(0);
                                meetingVO.setMt_remind_count(0);
                                meetingVO.setMt_remind_week("");
                                meetingVO.setMt_remind_end(null);
                            }
                            resultVO = meetingService.createMeetRoom(req, meetingVO);
                        }
                    }
                // 년 주기
                }else if(meetingVO.getMt_remind_type().equals(4)){
                    Integer _cnt = meetingVO.getMt_remind_count();  // 반복 년
                    if(_cnt > 5){
                        resultVO.setResult_str("년 단위 반복 주기는 5년까지 입니다.");
                    }else{
                        for(Integer i=1;i<=_cnt;i++){
                            String _sdate = getCalDate(meetingVO.getMt_start_dt(), i, 0, 0);
                            String _edate = getCalDate(meetingVO.getMt_end_dt(), i, 0, 0);
                            meetingVO.setMt_start_dt(_sdate);
                            meetingVO.setMt_end_dt(_edate);
                            if(i>1){
                                meetingVO.setMt_remind_type(0);
                                meetingVO.setMt_remind_count(0);
                                meetingVO.setMt_remind_week("");
                                meetingVO.setMt_remind_end(null);
                            }
                            resultVO = meetingService.createMeetRoom(req, meetingVO);
                        }
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
            Integer rs = 0;
            if(meetingVO != null){
                // 미팅룸 정보 수정
                meetingVO.setIdx_user(1);   // 미팅룸 호스트
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(1)){
                        rs = meetMapper.meet_modify(meetingVO);
                        if(rs > 0){
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

                            // 미팅룸 참여자 추가 저장
                            if(meetingVO.getMt_invite_email() != null && meetingVO.getMt_invite_email() != ""){
                                String[] inEmail = meetingVO.getMt_invite_email().split(",");
                                for(String uemail : inEmail){
                                    MeetingVO ee = new MeetingVO();
                                    ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                    ee.setUser_email(uemail);
                                    meetMapper.meet_invite(ee);
                                }
                            }

                            // 미팅룸 첨부파일 삭제
                            if(meetingVO.getFile_del() != null && meetingVO.getFile_del() != ""){
                                String[] fileIdx = meetingVO.getFile_del().split(",");
                                for(String fidx : fileIdx){
                                    MeetingVO ee = new MeetingVO();
                                    ee.setIdx_meeting(meetingVO.getIdx_meeting());
                                    ee.setIdx_attachment_file_info_join(Integer.valueOf(fidx));
                                    meetMapper.meet_invite(ee);

                                    // 파일 삭제
                                    MeetingVO finfo = fileServiceMapper.getMeetFile(ee);
                                    File f = new File(this.filepath + finfo.getFile_path() + finfo.getFile_name());
                                    if(f.exists()){
                                        f.delete();
                                    }
                                }
                            }

                            // 미팅룸 첨부파일 저장
                            List<MultipartFile> fileList = req.getFiles("file");
                            if(req.getFiles("file").get(0).getSize() != 0){
                                fileList = req.getFiles("file");
                            }
                            if(fileList.size()>0){
                                long time = System.currentTimeMillis();
                                String path = "/meetroom/" + meetingVO.getIdx_meeting() + "/";
                                String fullpath = this.filepath + path;
                                File fileDir = new File(fullpath);
                                if (!fileDir.exists()) {
                                    fileDir.mkdirs();
                                }
                                // FileUploadResponseVO paramVo = new FileUploadResponseVO();
                                for(MultipartFile mf : fileList) {
                                    String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                                    String saveFileName = String.format("%d_%s", time, originFileName);
                                    try { // 파일생성
                                        mf.transferTo(new File(fullpath, saveFileName));
                                        MeetingVO paramVo = new MeetingVO();
                                        paramVo.setIdx_meeting(meetingVO.getIdx_meeting());
                                        paramVo.setFile_path(path);
                                        paramVo.setFile_name(saveFileName);
                                        paramVo.setFile_size(mf.getSize());
                                        fileServiceMapper.addMeetFile(paramVo);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅룸을 생성하였습니다.");
                        }else{
                            resultVO.setResult_str("미팅룸 생성에 실패하였습니다.");
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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(1)){
                        Integer rs = meetMapper.putMeetOpen(meetingVO);
                        if(rs==1){
                            // 참가자 이메일 전송
                            meetingService.sendMailMeetInvites(meetingVO, rrs, 4);

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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트

            // 미팅 시간 중복 체크
            MeetingVO _chk = meetMapper.chkRoomDupTime(meetingVO);
            if(_chk.getChkcnt().equals(1)){
                resultVO.setResult_str("미팅 시간이 중복되어 비공개할 수 없습니다.");
            }else{
                MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
                if(rrs!=null){
                    if(rrs.getIdx_user().equals(1)){
                        Integer rs = meetMapper.putMeetClose(meetingVO);
                        if(rs==1){
                            // 참가자 이메일 전송
                            meetingService.sendMailMeetInvites(meetingVO, rrs, 4);

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
     * 미팅 취소
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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(1)){
                    Integer rs = meetMapper.putMeetCacncel(meetingVO);
                    if(rs==1){
                        // 참가자 이메일 전송
                        meetingService.sendMailMeetInvites(meetingVO, rrs, 3);

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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                if(rrs.getIdx_user().equals(1)){
                    Integer rs = meetMapper.deleteMeet(meetingVO);
                    if(rs==1){
                        // 참가자 이메일 전송
                        meetingService.sendMailMeetInvites(meetingVO, rrs, 3);

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
            meetingVO.setIdx_user(1);
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
            meetingVO.setIdx_user(1);

            Map<String, Object> _rs = new HashMap<String, Object>();
            List<MeetingVO> mInfo = meetMapper.getMyMeetList(meetingVO);    // 참여중인 미팅룸
            ArrayList<Object> _mrss = new ArrayList<Object>();
            for(MeetingVO rs0 : mInfo){
                Map<String, Object> _mrs = new HashMap<String, Object>();
                _mrs.put("mt_idx", rs0.getIdx_meeting());
                _mrs.put("mt_name", rs0.getMt_name());
                _mrs.put("mt_hostname", rs0.getUser_name());
                _mrs.put("mt_status", rs0.getMt_status());
                _mrs.put("mt_start_dt", rs0.getMt_start_dt());
                _mrs.put("mt_end_dt", rs0.getMt_end_dt());
                _mrs.put("mt_live", rs0.getIs_live());
                _mrs.put("mt_info", rs0.getMt_info());
                _mrss.add(_mrs);
            }
            _rs.put("mt_meetInfo", _mrss);    // 참여중인 미팅룸
            
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
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
            meetingVO.setIdx_user(1);   // 미팅룸 호스트
            MeetingVO rrs = meetMapper.getRoomInfo(meetingVO);
            if(rrs!=null){
                String _auth = "0"; // 게스트 권한 부여

                // 호스트 권한 부여
                if(rrs.getIdx_user().equals(1)){
                    _auth = "1";
                }

                // 미팅에 참여중인지 확인
                MeetingVO _chkMeet = meetMapper.chkMeetLiveJoin(meetingVO);
                if(!_chkMeet.getToken().isEmpty()){
                    Map<String, Object> _rs = new HashMap<String, Object>();
                    _rs.put("mid", meetingVO.getIdx_meeting());
                    _rs.put("token", _chkMeet.getToken());
                    resultVO.setData(_rs);

                    resultVO.setResult_code(CONSTANT.success);
                    resultVO.setResult_str("이미 미팅에 참여를 시작하여 미팅 장소로 이동합니다.");
                }else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                    Date sdate = dateFormat.parse(rrs.getMt_start_dt());
                    Date edate = dateFormat.parse(rrs.getMt_end_dt());
                    Long _sDt = sdate.getTime();
                    Long _eDt = edate.getTime();
    
                    String _userPk = "euraclass" + rrs.getIdx_meeting().toString();
                    String _jwt = tokenJWT.createToken(_userPk, _sDt, _eDt, rrs.getMt_end_dt(), null, _auth);
                    meetingVO.setToken(_jwt);
                    meetingVO.setSessionid(_userPk);
    
                    Map<String, Object> _rs = new HashMap<String, Object>();
                    _rs.put("mid", meetingVO.getIdx_meeting());
                    _rs.put("token", _jwt);
                    resultVO.setData(_rs);
                    
                    if(rrs.getIdx_user().equals(1)){
                        Integer rs = meetMapper.putMeetLiveStart(meetingVO);
                        if(rs==1){
                            // 참가자 이메일 전송
                            meetingService.sendMailMeetInvites(meetingVO, rrs, 1);

                            meetMapper.putMeetLiveJoin(meetingVO);  // 미팅룸에 들어가기용 데이터 저장

                            resultVO.setResult_code(CONSTANT.success);
                            resultVO.setResult_str("미팅을 시작하였습니다.");
                        }else{
                            resultVO.setResult_str("미팅을 시작할 수 없습니다.");
                        }
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
    public ResultVO closeLiveMeeting(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception{
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try{
            if(meetMapper.getRoomInfo(meetingVO).getIs_live() == 1){
                meetMapper.closeMeet(meetingVO);
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("Update Complete");
            } else {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("해당 강의가 진행중이 아닙니다.");
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
    public ResultVO getResultMeeting(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");
        try {
            MeetingVO meetingInfo = meetMapper.getRoomInfo(meetingVO);

            Map<String, Object> _rs = new HashMap<String, Object>();

            _rs.put("mtName", meetingInfo.getMt_name());
            _rs.put("mtStartTime", meetingInfo.getMt_start_dt());
            _rs.put("mtEndTime", meetingInfo.getMt_end_dt());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            long diff = format.parse(meetingInfo.getMt_end_dt()).getTime() - format.parse(meetingInfo.getMt_start_dt()).getTime();
            String hours = (diff / 1000) / 60 / 60 % 24 < 10 ? "0" + (diff / 1000) / 60 / 60 % 24 : "" + (diff / 1000) / 60 / 60 % 24;
            String minutes = (diff / 1000) / 60 % 60 < 10 ? "0" + (diff / 1000) / 60 % 60 : "" + (diff / 1000) / 60 % 60;
            String seconds = (diff / 1000) % 60 < 10 ? "0" + (diff / 1000) % 60 : "" + (diff / 1000) % 60;

            _rs.put("mtMeetingTime", hours + ":" + minutes + ":" + seconds);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("강의 정보를 불러오는 데에 성공했습니다.");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }

    /**
     * 미팅 분석 결과(첨부파일)
     * @param req
     * @param meetingVO(idx_meeting)
     * @return
     * @throws Exception
     */
    @GetMapping("/result/attachfile")
    public ResultVO getResultAttachFile(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Map<String, Object> _rs = new HashMap<String, Object>();

            _rs.put("mtAttachedFiles", meetMapper.getMeetFiles(meetingVO));

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("파일 정보를 불러오는 데에 성공했습니다.");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }

    /**
     * 미팅 분석 결과(강의 참여자 명단)
     * @param req
     * @param meetingVO(idx_meeting)
     * @return
     * @throws Exception
     */
    @GetMapping("/result/participant")
    public ResultVO getResultParticipant(HttpServletRequest req, @RequestBody MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try{

        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }

}
