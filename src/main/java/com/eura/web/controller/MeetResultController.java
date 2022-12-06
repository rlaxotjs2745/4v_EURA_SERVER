/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.*;
import com.eura.web.service.AnalysisService;
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
@RequestMapping("/meet/result")
public class MeetResultController extends BaseController {
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
     * 미팅 분석 결과(미팅 정보)
     * @param req
     * @param meetingVO(idx_meeting)
     * @return
     * @throws Exception
     */
    @GetMapping("/meeting")
    public ResultVO getResultMeeting(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
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
            MeetingVO meetingInfo = meetMapper.getRoomInfo(meetingVO);
            if(meetingInfo==null){
                resultVO.setResult_str("미팅룸 정보가 없습니다.");
                return resultVO;
            }
            Integer _auth = 0;
            if(meetingInfo.getIdx_user().equals(uInfo.getIdx_user())){
                _auth = 1;
            }
            Map<String, Object> _rs = new HashMap<String, Object>();

            // 호스트 여부 - 0:참석자, 1:호스트
            _rs.put("is_host", _auth);

            // 교수명 호스트 이름
            _rs.put("mtName", meetingInfo.getMt_name());

            // 참석여부
            Integer _livein = 0;
            MeetingVO _uin = meetMapper.chkMeetLiveJoin(meetingVO);
            if(_uin!=null){
                if(StringUtils.isNotEmpty(_uin.getToken())){
                    _livein = 1;
                }else{
                    _livein = 2;
                }
            }
            if(_uin==null){
                resultVO.setResult_str("미팅룸 참석자가 아닙니다.");
                return resultVO;
            }
            _rs.put("join", _livein);   // 미팅 참석 여부

            // 강의명
            _rs.put("hostname", meetingInfo.getUser_name());

            // 날짜
            _rs.put("mtMeetiDate", meetingInfo.getMt_start_dt().split("\\s")[0]);

            // 시간
            String _chkHour = meetingInfo.getMt_start_dt().split("\\s")[1];
            String _chkHour2 = meetingInfo.getMt_end_dt().split("\\s")[1];
            _rs.put("mtMeetiTime", _chkHour.substring(0,5) + " ~ " + _chkHour2.substring(0,5));

            if(meetingVO.getFile_no()==null){
                meetingVO.setFile_no(1);
            }

            Integer _dur = 0;
            String _redStartDt = "";

            // 영상 파일 : IDX_MOVIE_FILE, FILE_NO, FILE_PATH, FILE_NAME, DURATION, RECORD_DT
            List<MeetingVO> _mlists = fileServiceMapper.getMeetMovieFile(meetingVO);
            if(_mlists!=null){
                ArrayList<Object> _mls = new ArrayList<Object>();
                for(MeetingVO _mlist : _mlists){
                    Map<String, Object> _ul = new HashMap<String, Object>();
                    _ul.put("idx",_mlist.getIdx_movie_file());    // 참석자 회원 INDEX
                    _ul.put("fileNo",_mlist.getFile_no());      // 파일 순서
                    _ul.put("duration",_mlist.getDuration());   // 재생 길이
                    _ul.put("recordDt",_mlist.getRecord_dt());    // 녹화 시작 시간
                    _ul.put("filename",_mlist.getFile_name());    // 파일명
                    String _furl = "";
                    String _furl2 = "";
                    if(StringUtils.isNotEmpty(_mlist.getFile_name())){
                        if(!srvinfo.equals("dev")){
                            _furl = voddomain + "/meetmovie" + _mlist.getFile_path() + _mlist.getFile_name();
                            _furl2 = voddomain + "/meetmovie" + _mlist.getFile_path() + _mlist.getFile_name();
                        }else{
                            String _mpath = _mlist.getFile_name().replace(".mp4","");
                            _furl = voddomain + "/output/" + _mpath + CONSTANT._movieUrl + _mpath + "_720.m3u8";
                            _furl2 = voddomain + "/output/" + _mpath + CONSTANT._movieMp4 + _mlist.getFile_name();
                        }
                    }
                    _ul.put("fileUrl", _furl);    // 영상
                    _ul.put("fileUrl2", _furl2);    // 영상
                    _mls.add(_ul);

                    if(meetingVO.getFile_no()==_mlist.getFile_no()){
                        _dur = _mlist.getDuration();
                        _redStartDt = _mlist.getRecord_dt();
                    }
                }
                _rs.put("mtMovieFiles", _mls);
            }else{
                _rs.put("mtMovieFiles", null);
            }

            if(meetingVO.getDuration()!=null){
                _dur = meetingVO.getDuration();
            }
            
            SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
            String _rdt = "";
            String _edt = "";
            if(meetingInfo.getIs_live_dt() != null){
                _rdt = meetingInfo.getIs_live_dt();
            }

            if(meetingInfo.getIs_finish() == 1){
                _edt = meetingInfo.getIs_finish_dt();
            }

            // 소요시간
            if(StringUtils.isNotEmpty(_rdt) && StringUtils.isNotEmpty(_edt)){
                long diff = ff.parse(_edt).getTime() - ff.parse(_rdt).getTime();
                if(diff>0){
                    long sec = diff / 1000;
                    _rs.put("mtMeetTimer", getSec2Time((int)sec));
                }else{
                    _rs.put("mtMeetTimer", "00:00:00");
                }
            }else{
                _rs.put("mtMeetTimer", "00:00:00");
            }


            // 미팅 분석 결과(첨부파일)
            List<MeetingVO> _flists = meetMapper.getMeetFiles(meetingVO);
            ArrayList<Object> _fls = new ArrayList<Object>();
            for(MeetingVO _flist : _flists){
                Map<String, Object> _fl = new HashMap<String, Object>();
                _fl.put("idx",_flist.getIdx_attachment_file_info_join());    // 첨부파일 INDEX
                String _furl = "";
                if(StringUtils.isNotEmpty(_flist.getFile_name())){
                    if(srvinfo.equals("dev")){
                        _furl = filedomain + "/download?fnm=" + _flist.getFile_path() + _flist.getFile_name();
                    }else{
                        _furl = filedomain + _flist.getFile_path() + _flist.getFile_name();
                    }
                }
                _fl.put("fileUrl",_furl);    // 첨부파일 URL
                _fl.put("filename",_flist.getFile_name());       // 파일명
                _fls.add(_fl);
            }
            _rs.put("mtAttachedFiles", _fls);

            List<AnalysisVO> analysisVOList = new ArrayList<>();
            PersonalLevelVO personalLevelVO = new PersonalLevelVO();
            PersonalLevelVO personalLevelVO2 = new PersonalLevelVO();
            ConcentrationVO concentrationVO = new ConcentrationVO();
            ConcentrationVO concentrationVO2 = new ConcentrationVO();
            AnalysisVO _Time = analysisMapper.getAnalysisFirstData(meetingVO);
            AnalysisVO _Time2 = new AnalysisVO();
            if(_Time!=null){
                if(StringUtils.isNotEmpty(_redStartDt)){
                    Date sdate = ff.parse(_redStartDt);
                    Integer _sDt = (int) (sdate.getTime()/1000);
                    Integer _eDt = _sDt + _dur;
                    _Time2.setTimefirst(_sDt);
                    _Time2.setTimeend(_eDt);
                }
            }

            // 미팅 분석 결과(강의 참여자 명단) - 미팅 참석자도 참석자 전체 목록 리스팅
            List<MeetingVO> _ulists = meetMapper.getMeetInvites(meetingVO);
            if(_ulists!=null && !_livein.equals(0)){
                Integer _joincnt = 0;
                ArrayList<Object> _uls = new ArrayList<Object>();
                List<PersonalLevelVO> personalLevelVOList = new ArrayList<>();
                List<PersonalLevelVO> personalLevelVOList2 = new ArrayList<>();
                for(MeetingVO _ulist : _ulists){
                    Map<String, Object> _ul = new HashMap<String, Object>();
                    _ul.put("idx",_ulist.getIdx_meeting_user_join());    // 참석자 회원 INDEX
                    _ul.put("uname",_ulist.getUser_name()); // 참석자명
                    _ul.put("uemail",_ulist.getUser_email());   // 이메일
                    Integer _join = 0;

                    MeetingVO _ulinfo = new MeetingVO();
                    _ulinfo.setIdx_meeting_user_join(_ulist.getIdx_meeting_user_join());
                    analysisVOList = analysisMapper.getUserAnalysisData(_ulinfo);
                    if(analysisVOList!=null){
                        // 전체 동영상용
                        if(_Time != null){
                            personalLevelVO = analysisService.getPersonalLevel(analysisVOList, _Time, _ulist.getIdx_meeting_user_join(), _dur);
                            personalLevelVOList.add(personalLevelVO);
                            concentrationVO = analysisService.getPersonalRate(personalLevelVO);
                        }

                        // 동영상 당 그래프용
                        if(StringUtils.isNotEmpty(_redStartDt)){
                            personalLevelVO2 = analysisService.getPersonalLevel(analysisVOList, _Time2, _ulist.getIdx_meeting_user_join(), 0);
                            personalLevelVOList2.add(personalLevelVO2);
                        }
                        
                        // 개인 오른쪽 집중도 - 전체데이터
                        if(_auth==1 || _uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                            _ul.put("goodValue",concentrationVO.getGood());    // 집중도
                            _ul.put("badValue",concentrationVO.getBad());    // 집중도
                            _ul.put("cameraOffValue",concentrationVO.getCameraOff());    // 집중도

                            if(concentrationVO.getGood()>0){
                                _ul.put("value", concentrationVO.getGood());    // 집중도
                            }else{
                                _ul.put("value", 0.0);    // 집중도
                            }
                        }else{
                            _ul.put("goodValue",null);    // 집중도
                            _ul.put("badValue",null);    // 집중도
                            _ul.put("cameraOffValue",null);    // 집중도
                            if(_uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                                _ul.put("value", 0.0);    // 집중도
                            }else{
                                _ul.put("value", null);    // 집중도
                            }
                        }
                        if(_uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                            _ul.put("is_iam", 1);    // 0:남일때, 1:나일때
                        }else{
                            _ul.put("is_iam", 0);    // 0:남일때, 1:나일때
                        }
                        _ul.put("is_host",_ulist.getIs_host());   // 호스트 여부 0:참석자, 1:호스트

                        String _upic = "";
                        if(StringUtils.isNotEmpty(_ulist.getFile_name())){
                            if(srvinfo.equals("dev")){
                                _upic = filedomain + "/pic?fnm=" + _ulist.getFile_path() + _ulist.getFile_name();
                            }else{
                                _upic = filedomain + _ulist.getFile_path() + _ulist.getFile_name();
                            }
                        }
                        _ul.put("upic",_upic);    // 프로필 사진
                        if(StringUtils.isNotEmpty(_ulist.getJoin_dt())){
                            _join = 1;
                            _joincnt++;
                        }
                    }
                    _ul.put("join",_join);    // 미팅 참석 여부
                    _uls.add(_ul);
                }
                _rs.put("mtInviteList", _uls);

                // 전체 참여자 집중도 수치 평균값
                ArrayList<Object> _dlists = new ArrayList<Object>();
                Map<String, Object> _tglist = new HashMap<String, Object>();
                GraphMidVO userRateMap = analysisService.getAllUserRate(personalLevelVOList);

                List<Double> goodList = userRateMap.getGoodList();
                List<Double> badList = userRateMap.getBadList();
                List<Integer> totcalCntList = userRateMap.getTotcalCntList();

                concentrationVO = analysisService.getMeetingRate(goodList, badList, totcalCntList);

                // 전체 집중도율 - 반원 그래프
                // PersonalLevelVO _topVal = analysisService.getTotalValue(meetingVO);
                _tglist.put("good",concentrationVO.getGood());
                _tglist.put("bad",concentrationVO.getBad());
                _tglist.put("off",concentrationVO.getCameraOff());

                // 전체 그래프용 데이터 만들기
                if(StringUtils.isNotEmpty(_redStartDt) && personalLevelVOList2!=null){
                    GraphMidVO userRateMap2 = analysisService.getAllUserRate(personalLevelVOList2);

                    List<Double> goodList2 = userRateMap2.getGoodList();
                    List<Double> badList2 = userRateMap2.getBadList();
                    List<String> lvlList2 = userRateMap2.getLvlList();
                    
                    for(int i = 0;i<goodList2.size();i++){
                        Map<String, Object> _dlist = new HashMap<String, Object>();
                        _dlist.put("name",lvlList2.get(i)); // duration을 시간으로 환산
                        _dlist.put("Good",Math.round(goodList2.get(i)));  // GOOD 100
                        _dlist.put("Bad",(Math.round(badList2.get(i))*-1));  // BAD -100
                        _dlist.put("amt",0);
                        _dlists.add(_dlist);
                    }
                }
                _rs.put("mtAnalyTop", _tglist);

                if(_auth==1){
                    _rs.put("mtAnalyMid", _dlists);
                }else{
                    _rs.put("mtAnalyMid", null);
                }

                // 참여 인원수
                Map<String, Object> _uinfo = new HashMap<String, Object>();
                _uinfo.put("user_total",_ulists.size());
                _uinfo.put("user_invite",_joincnt);
                _rs.put("mtInviteInfo", _uinfo);
            }else{
                _rs.put("mtInviteList", null);

                Map<String, Object> _uinfo = new HashMap<String, Object>();
                _uinfo.put("user_total",0);
                _uinfo.put("user_invite",0);
                _rs.put("mtInviteInfo", null);
                _rs.put("mtAnalyMid", null);

                Map<String, Object> _tglist = new HashMap<String, Object>();
                _tglist.put("good",0);
                _tglist.put("bad",0);
                _tglist.put("off",100);
                _rs.put("mtAnalyTop", _tglist);
            }

            // 개인 인디게이터 데이터 - setBtmdata
            if(_auth==0 && _Time != null){
                // 참석자 용
                // 개인 인디게이터 데이터
                ArrayList<Object> _dlists = new ArrayList<Object>();
                if(!_livein.equals(0)){
                    Map<String, Object> _d2list = new HashMap<String, Object>();

                    MeetingVO _ulinfo = new MeetingVO();
                    _ulinfo.setIdx_meeting_user_join(_uin.getIdx_meeting_user_join());
                    analysisVOList = analysisMapper.getUserAnalysisData(_ulinfo);
                    if(analysisVOList!=null && analysisVOList.size()>0){
                        personalLevelVO = analysisService.getPersonalLevel(analysisVOList, _Time, analysisVOList.get(0).getIdx_meeting_user_join(), 0);

                        // 개인 그래프용 데이터 만들기
                        if(StringUtils.isNotEmpty(_redStartDt)){
                            personalLevelVO2 = analysisService.getPersonalLevel(analysisVOList, _Time2, analysisVOList.get(0).getIdx_meeting_user_join(), _dur);
                            for(ConcentrationVO paramVo : personalLevelVO2.getConcentrationList()){
                                Map<String, Object> _dlist = new HashMap<String, Object>();
                                _dlist.put("name",paramVo.getLevel_num());   // 동영상 재생 위치
                                _dlist.put("good",paramVo.getEnggood());
                                _dlist.put("bad",paramVo.getEngbad()*-1);  // GOOD~BAD 100 ~ -100
                                _dlists.add(_dlist);
                            }
                        }

                        concentrationVO = analysisService.getPersonalRate(personalLevelVO);

                        // 분석 요약 데이터
                        _d2list.put("good",concentrationVO.getGood()); // GOOD
                        _d2list.put("bad",concentrationVO.getBad());  // BAD
                        _d2list.put("off",concentrationVO.getCameraOff());  // OFF
                        _d2list.put("tcnt",0); // 현재 점수
                        _d2list.put("acnt",0); // 누적 평균
                    }else{
                        _d2list.put("good",0); // GOOD
                        _d2list.put("bad",0);  // BAD
                        _d2list.put("off",0);  // OFF
                        _d2list.put("tcnt",0); // 현재 점수
                        _d2list.put("acnt",0); // 누적 평균
                    }
                    _rs.put("mtData0", _dlists);
                    _rs.put("mtData1", _d2list);
                }else{
                    _rs.put("mtData0", _dlists);

                    Map<String, Object> _d2list = new HashMap<String, Object>();
                    _d2list.put("good",0); // GOOD
                    _d2list.put("bad",0);  // BAD
                    _d2list.put("off",0);  // OFF
                    _d2list.put("tcnt",0); // 현재 점수
                    _d2list.put("acnt",0); // 누적 평균

                    _rs.put("mtData1", _d2list);
                }
            }

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
    @GetMapping("/mtinviteinfo")
    public ResultVO getResultInviteInfo(HttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            UserVO uInfo = getChkUserLogin(req);
            if(uInfo==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }

            if(meetingVO.getFile_no()==null){
                meetingVO.setFile_no(1);
            }
            Integer _dur = 0;

            // 영상 파일 : IDX_MOVIE_FILE, FILE_NO, FILE_PATH, FILE_NAME, DURATION, RECORD_DT
            List<MeetingVO> _mlists = fileServiceMapper.getMeetMovieFile(meetingVO);
            if(_mlists!=null){
                for(MeetingVO _mlist : _mlists){
                    if(meetingVO.getFile_no()==_mlist.getFile_no()){
                        _dur = _mlist.getDuration();
                    }
                }
            }

            Map<String, Object> _rs = new HashMap<String, Object>();

            ArrayList<Object> _dlists = new ArrayList<Object>();
            Map<String, Object> _d2list = new HashMap<String, Object>();
            AnalysisVO _Time = analysisMapper.getAnalysisFirstData(meetingVO);
            MeetingVO _ulinfo = new MeetingVO();
            _ulinfo.setIdx_meeting_user_join(meetingVO.getIdx_user());
            List<AnalysisVO> analysisVOList = analysisMapper.getUserAnalysisData(_ulinfo);
            if(analysisVOList!=null && analysisVOList.size()>0){
                PersonalLevelVO personalLevelVO = analysisService.getPersonalLevel(analysisVOList, _Time, analysisVOList.get(0).getIdx_meeting_user_join(), _dur);
                for(ConcentrationVO paramVo : personalLevelVO.getConcentrationList()){
                    Map<String, Object> _dlist = new HashMap<String, Object>();
                    _dlist.put("name",paramVo.getLevel_num());   // 동영상 재생 위치
                    _dlist.put("good",paramVo.getGood());
                    _dlist.put("bad",paramVo.getBad());  // GOOD~BAD 100 ~ -100
                    _dlists.add(_dlist);
                }
                _rs.put("mtData0", _dlists);

                ConcentrationVO concentrationVO = analysisService.getPersonalRate(personalLevelVO);

                // 분석 요약 데이터
                _d2list.put("good",Math.round(concentrationVO.getGood())); // GOOD
                _d2list.put("bad",Math.round(concentrationVO.getBad()*-1));  // BAD
                _d2list.put("off",Math.round(concentrationVO.getCameraOff()));  // OFF
                _d2list.put("tcnt",0); // 현재 점수
                _d2list.put("acnt",0); // 누적 평균
                _rs.put("mtData1", _d2list);
            }else{
                _rs.put("mtData0", _dlists);
                _d2list.put("good",0); // GOOD
                _d2list.put("bad",0);  // BAD
                _d2list.put("off",0);  // OFF
                _d2list.put("tcnt",0); // 현재 점수
                _d2list.put("acnt",0); // 누적 평균
                _rs.put("mtData1", _d2list);
            }

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("미팅 참석자 분석 데이터 호출 완료");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }
}
