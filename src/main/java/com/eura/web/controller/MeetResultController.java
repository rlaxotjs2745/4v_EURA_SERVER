/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.*;
import com.eura.web.service.AnalysisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.eura.web.base.BaseController;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.util.CONSTANT;

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
        srvinfo = "prod";
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


            List<AnalysisVO> _Time = new ArrayList<AnalysisVO>();

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
                        if(srvinfo.equals("dev")){
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

                    Date sdate = ff.parse(_mlist.getRecord_dt());
                    Integer _sDt = (int) (sdate.getTime()/1000);
                    Integer _eDt = _sDt + _mlist.getDuration();
                    AnalysisVO _rs0 = new AnalysisVO();
                    _rs0.setTimefirst(_sDt);
                    _rs0.setTimeend(_eDt);
                    _rs0.setDuration(_mlist.getDuration());
                    _Time.add(_rs0);
                }
                _rs.put("mtMovieFiles", _mls);
            }else{
                _rs.put("mtMovieFiles", null);
            }

            MeetingVO _data = new MeetingVO();
            _data.setIdx_meeting(meetingVO.getIdx_meeting());
            List<AnalysisVO> allAnalysisList = analysisMapper.getUserAnalysisData(_data);

            // 감정분석 전체 데이터 값만 뽑기
            List<PersonalLevelVO> personalLevelVOList = new ArrayList<>();
            List<PersonalLevelVO> allLevelVOList = new ArrayList<>();
            
            // 참가 인원수
            Integer _joincnt = 0;

            // 참가자 리스트
            ArrayList<Object> _uls = new ArrayList<Object>();
            Integer _ulistsSize = 0;
            List<MeetingVO> _ulists = meetMapper.getMeetInvites(meetingVO);
            ArrayList<Object> _dlists0 = new ArrayList<Object>();
            ArrayList<Object> _dlists1 = new ArrayList<Object>();
            ArrayList<Object> _dlists = new ArrayList<Object>();
            if(_Time!=null){
                Integer _ri = 0;

                if(_ulists != null && !_livein.equals(0)){
                    _ulistsSize = _ulists.size();
                    for(AnalysisVO _Time0 : _Time){
                        for(MeetingVO _ulist : _ulists){
                            Integer _join = 0;

                            // 개인 막대 그래프용 데이터 뽑기
                            PersonalLevelVO personalLevelVO = analysisService.getLevelData(allAnalysisList, _Time0, _ulist.getIdx_meeting_user_join());
                            allLevelVOList.add(personalLevelVO);    // 분석요약용 : 모든 동영상

                            // 지정 동영상 감정분석 데이터 뽑기
                            if(meetingVO.getFile_no()==(_ri+1)){
                                personalLevelVOList.add(personalLevelVO);   // 지정 동영상 연계용

                                if(_auth==1 || (!_livein.equals(0) && _uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join()))){
                                    // 개인 그래프용 데이터 만들기
                                    Map<String, Object> _dlistrs = new HashMap<String, Object>();
                                    ArrayList<Object> _dlists00 = new ArrayList<Object>();
                                    for(ConcentrationVO paramVo : personalLevelVO.getConcentrationList()){
                                        Map<String, Object> _dlist = new HashMap<String, Object>();
                                        _dlist.put("name",paramVo.getLevel_num());   // 동영상 재생 위치
                                        _dlist.put("good",paramVo.getGood());
                                        _dlist.put("bad",paramVo.getBad()*-1);
                                        _dlists00.add(_dlist);
                                    }

                                    // 프로필 사진 -----------------------------------------------
                                    String _upic = "";
                                    if(StringUtils.isNotEmpty(_ulist.getFile_name())){
                                        if(srvinfo.equals("dev")){
                                            _upic = filedomain + "/pic?fnm=" + _ulist.getFile_path() + _ulist.getFile_name();
                                        }else{
                                            _upic = filedomain + _ulist.getFile_path() + _ulist.getFile_name();
                                        }
                                    }
                                    _dlistrs.put("upic",_upic);
                                    // 프로필 사진 -----------------------------------------------

                                    // 미팅 참석 여부
                                    if(StringUtils.isNotEmpty(_ulist.getJoin_dt())){
                                        _join = 1;
                                    }
                                    _dlistrs.put("uname",_ulist.getUser_name()); // 참석자명
                                    _dlistrs.put("uemail",_ulist.getUser_email());
                                    if(_uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                                        _dlistrs.put("is_iam", 1);    // 0:남일때, 1:나일때
                                    }else{
                                        _dlistrs.put("is_iam", 0);    // 0:남일때, 1:나일때
                                    }
                                    _dlistrs.put("is_host", _ulist.getIs_host());   // 호스트 여부 0:참석자, 1:호스트
                                    _dlistrs.put("join",_join);    // 미팅 참석 여부
                                    _dlistrs.put("list",_dlists00);
                                    _dlists0.add(_dlistrs);
                                }
                            }
                        }
                        _ri++;
                    }

                    Integer _join = 0;
                    
                    if(_auth==1 || !_livein.equals(0)){
                        for(MeetingVO _ulist : _ulists){
                            Map<String, Object> _ul = new HashMap<String, Object>();
                            ConcentrationVO concentrationVO = analysisService.getPersonalRate(allLevelVOList, _ulist.getIdx_meeting_user_join());

                            if(_auth==1 || _uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                                // 개인 분석 요약 데이터
                                Map<String, Object> _d2list = new HashMap<String, Object>();
                                _d2list.put("good",concentrationVO.getGood()); // GOOD
                                _d2list.put("bad",concentrationVO.getBad());  // BAD
                                _d2list.put("off",concentrationVO.getCameraOff());  // OFF
                                _d2list.put("tcnt",0); // 현재 점수
                                _d2list.put("acnt",0); // 누적 평균
                                _dlists1.add(_d2list);
                            }

                            if(_uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                                _ul.put("is_iam", 1);    // 0:남일때, 1:나일때
                            }else{
                                _ul.put("is_iam", 0);    // 0:남일때, 1:나일때
                            }

                            _ul.put("is_host", _ulist.getIs_host());   // 호스트 여부 0:참석자, 1:호스트

                            // 프로필 사진 -----------------------------------------------
                            String _upic = "";
                            if(StringUtils.isNotEmpty(_ulist.getFile_name())){
                                if(srvinfo.equals("dev")){
                                    _upic = filedomain + "/pic?fnm=" + _ulist.getFile_path() + _ulist.getFile_name();
                                }else{
                                    _upic = filedomain + _ulist.getFile_path() + _ulist.getFile_name();
                                }
                            }
                            _ul.put("upic",_upic);
                            // 프로필 사진 -----------------------------------------------

                            // 미팅 참석 여부
                            if(StringUtils.isNotEmpty(_ulist.getJoin_dt())){
                                _join = 1;
                                _joincnt++;
                            }
                            _ul.put("idx",_ulist.getIdx_meeting_user_join());    // 참석자 회원 INDEX
                            _ul.put("uname",_ulist.getUser_name()); // 참석자명
                            _ul.put("uemail",_ulist.getUser_email());   // 이메일
                            _ul.put("join",_join);    // 미팅 참석 여부
    
                            // 개인 집중도
                            // _ul.put("goodValue",concentrationVO.getGood());    // 집중도
                            // _ul.put("badValue",concentrationVO.getBad());    // 집중도
                            // _ul.put("cameraOffValue",concentrationVO.getCameraOff());    // 집중도
    
                            if(concentrationVO.getGood()>0){
                                _ul.put("value", concentrationVO.getGood());    // 집중도
                            }else{
                                _ul.put("value", 0.0);    // 집중도
                            }
                            _uls.add(_ul);
                        }
                    }
                }
            }
            
            if(_auth==1){
                // 전체 그래프용 데이터 만들기 : 지정 동영상
                GraphMidVO userRateMap2 = analysisService.getAllUserRate(personalLevelVOList);

                List<Integer> goodList2 = userRateMap2.getGoodList();
                List<Integer> badList2 = userRateMap2.getBadList();
                List<String> lvlList2 = userRateMap2.getLvlList();
                
                if(goodList2!=null && goodList2.size()>0){
                    for(int i = 0;i<goodList2.size();i++){
                        Map<String, Object> _dlist = new HashMap<String, Object>();
                        _dlist.put("name",lvlList2.get(i)); // duration을 시간으로 환산
                        _dlist.put("Good",Math.round(goodList2.get(i)));  // GOOD 100
                        _dlist.put("Bad",(Math.round(badList2.get(i))*-1));  // BAD -100
                        _dlist.put("amt",0);
                        _dlists.add(_dlist);
                    }
                }

                // 전체 집중도율 - 반원 그래프 : 동영상 통합
                GraphMidVO userRateMap = analysisService.getAllUserRate(allLevelVOList);
                
                List<Integer> goodList = userRateMap.getGoodList();
                List<Integer> badList = userRateMap.getBadList();
                List<Integer> totcalCntList = userRateMap.getTotcalCntList();
                
                ConcentrationVO conVO = analysisService.getMeetingRate(goodList, badList, totcalCntList);
                Map<String, Object> _tglist = new HashMap<String, Object>();
                _tglist.put("good",conVO.getGood());
                _tglist.put("bad",conVO.getBad());
                _tglist.put("off",conVO.getCameraOff());

                _rs.put("mtAnalyMid", _dlists);
                _rs.put("mtAnalyTop", _tglist);
            }else{
                _rs.put("mtAnalyMid", null);
                _rs.put("mtAnalyTop", null);
            }
            _rs.put("mtInviteList", _uls);
            _rs.put("mtData0", _dlists0);   // setBtmdata
            _rs.put("mtData1", _dlists1);   // 개인 분석요약

            // 참여 인원수
            Map<String, Object> _uinfo = new HashMap<String, Object>();
            _uinfo.put("user_total",_ulistsSize);
            _uinfo.put("user_invite",_joincnt);
            _rs.put("mtInviteInfo", _uinfo);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("강의 정보 호출 완료");
            resultVO.setData(_rs);
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultVO;
    }
}
