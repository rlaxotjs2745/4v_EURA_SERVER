package com.eura.web.service;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.ConcentrationVO;
import com.eura.web.model.DTO.GraphMidVO;
import com.eura.web.model.DTO.MeetResultVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.PersonalLevelVO;
import com.eura.web.model.DTO.ResultVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eura.web.util.CONSTANT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class MeetResultService {
    @Autowired
    private MeetMapper meetMapper;

    @Autowired
    private AnalysisMapper analysisMapper;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private FileServiceMapper fileServiceMapper;

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
     * 분석 데이터 생성하여 DB에 저장하기
     * @param resultVO
     * @param meetingVO
     * @param _Time
     * @param _auth
     * @param _uin
     * @param _rs
     * @return ResultVO
     * @throws Exception
     */
    public ResultVO getResultData(ResultVO resultVO, MeetingVO meetingVO, List<AnalysisVO> _Time, Integer _auth, MeetingVO _uin, Map<String, Object> _rs) throws Exception {
        Integer _livein = 0;
        if(_uin!=null){
            if(StringUtils.isNotEmpty(_uin.getToken())){
                _livein = 1;
            }else{
                _livein = 2;
            }
        }else{
            _uin = new MeetingVO();
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
        if(_Time!=null){
            Integer _ri = 0;

            if(_ulists != null && !_livein.equals(0)){
                _ulistsSize = _ulists.size();
                for(AnalysisVO _Time0 : _Time){
                    for(MeetingVO _ulist : _ulists){
                        Integer _join = 0;
                        Integer _dataChk = 0;
                        if(_ulist.getToken() != null){
                            _dataChk = 1;
                        }

                        // 개인 막대 그래프용 데이터 뽑기
                        PersonalLevelVO personalLevelVO = analysisService.getLevelData(allAnalysisList, _Time0, _ulist.getIdx_meeting_user_join(), _dataChk);
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
                                if(StringUtils.isNotEmpty(_ulist.getToken())){
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

                if(_auth==1 || !_livein.equals(0)){
                    for(MeetingVO _ulist : _ulists){
                        Integer _join = 0;
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
                        if(StringUtils.isNotEmpty(_ulist.getToken())){
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
            ArrayList<Object> _dlists = new ArrayList<Object>();

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
            _rs.put("mtAnalyMid", _dlists);

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
        return resultVO;
    }

    /**
     * 저장된 분석 데이터 DB 추출
     * @param resultVO
     * @param meetingVO
     * @param _Time
     * @param _auth
     * @param _uin
     * @param _rs
     * @return ResultVO
     * @throws Exception
     */
    public ResultVO getResultDataDB(ResultVO resultVO, MeetingVO meetingVO, List<AnalysisVO> _Time, Integer _auth, MeetingVO _uin, Map<String, Object> _rs) throws Exception {
        Integer _livein = 0;
        if(_uin!=null){
            if(StringUtils.isNotEmpty(_uin.getToken())){
                _livein = 1;
            }else{
                _livein = 2;
            }
        }else{
            _uin = new MeetingVO();
        }

        // 참가 인원수
        Integer _joincnt = 0;

        // 참가자 리스트
        List<MeetingVO> _ulists = meetMapper.getMeetInvites(meetingVO);
        List<ConcentrationVO> _pbdrs = analysisMapper.getPersoanlBarData(meetingVO);
        Integer _ulistsSize = 0;
        ArrayList<Object> _uls = new ArrayList<Object>();
        ArrayList<Object> _dlists0 = new ArrayList<Object>();
        ArrayList<Object> _dlists1 = new ArrayList<Object>();
        if(_Time!=null){
            if(_ulists != null && !_livein.equals(0)){
                _ulistsSize = _ulists.size();
                for(MeetingVO _ulist : _ulists){
                    Integer _join = 0;

                    if(_auth==1 || (!_livein.equals(0) && _uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join()))){
                        // 개인 그래프용 데이터 만들기
                        Map<String, Object> _dlistrs = new HashMap<String, Object>();
                        ArrayList<Object> _dlists00 = new ArrayList<Object>();
                        for(ConcentrationVO paramVo : _pbdrs){
                            Map<String, Object> _dlist = new HashMap<String, Object>();
                            if(_ulist.getIdx_meeting_user_join().equals(paramVo.getIdx_meeting_user_join())){
                                _dlist.put("name",paramVo.getLvl());   // 동영상 재생 위치
                                _dlist.put("good",paramVo.getGood());
                                _dlist.put("bad",paramVo.getBad()*-1);
                                _dlists00.add(_dlist);
                            }
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
                        if(StringUtils.isNotEmpty(_ulist.getToken())){
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

                
                if(_auth==1 || !_livein.equals(0)){
                    List<ConcentrationVO> _tprs = analysisMapper.getPersoanlTotalData(meetingVO);
                    for(MeetingVO _ulist : _ulists){
                        Integer _join = 0;
                        Integer _value = 0;
                        Map<String, Object> _ul = new HashMap<String, Object>();

                        if(_auth==1 || _uin.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                            // 개인 분석 요약 데이터
                            for(ConcentrationVO paramVo : _tprs){
                                if(paramVo.getIdx_meeting_user_join().equals(_ulist.getIdx_meeting_user_join())){
                                    Map<String, Object> _d2list = new HashMap<String, Object>();
                                    _d2list.put("good",paramVo.getTotal_good()); // GOOD
                                    _d2list.put("bad",paramVo.getTotal_bad());  // BAD
                                    _d2list.put("off", (100 - (paramVo.getTotal_good()+paramVo.getTotal_bad()) ) );  // OFF
                                    _d2list.put("tcnt",0); // 현재 점수
                                    _d2list.put("acnt",0); // 누적 평균
                                    _dlists1.add(_d2list);

                                    _value = paramVo.getTotal_good();
                                }
                            }
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
                        if(StringUtils.isNotEmpty(_ulist.getToken())){
                            _join = 1;
                            _joincnt++;
                        }
                        _ul.put("idx",_ulist.getIdx_meeting_user_join());    // 참석자 회원 INDEX
                        _ul.put("uname",_ulist.getUser_name()); // 참석자명
                        _ul.put("uemail",_ulist.getUser_email());   // 이메일
                        _ul.put("join",_join);    // 미팅 참석 여부
                        _ul.put("value", _value);    // 집중도
                        _uls.add(_ul);
                    }
                }
            }
        }
        
        if(_auth==1){
            // 전체 그래프용 데이터 만들기 : 지정 동영상
            List<ConcentrationVO> _tbdrs = analysisMapper.getTotalBarData(meetingVO);
            ArrayList<Object> _dlists = new ArrayList<Object>();
            if(_tbdrs!=null && _tbdrs.size()>0){
                for(ConcentrationVO paramVo : _tbdrs){
                    Map<String, Object> _dlist = new HashMap<String, Object>();
                    _dlist.put("name",paramVo.getLvl()); // duration을 시간으로 환산
                    _dlist.put("Good",paramVo.getGood());  // GOOD 100
                    _dlist.put("Bad",paramVo.getBad());  // BAD -100
                    _dlist.put("amt",0);
                    _dlists.add(_dlist);
                }
            }

            // 전체 집중도율 - 반원 그래프 : 동영상 통합
            MeetResultVO _tdrs = analysisMapper.getMeetResultData(meetingVO);
            Map<String, Object> _tglist = new HashMap<String, Object>();
            if(_tdrs!=null){
                _tglist.put("good",_tdrs.getTotal_good());
                _tglist.put("bad",_tdrs.getTotal_bad());
                _tglist.put("off",(100 - (_tdrs.getTotal_good()+_tdrs.getTotal_bad())));
            }

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
        return resultVO;
    }

    /**
     * 분석결과 데이터 DB 삽입
     * @param meetingVO
     * @throws Exception
     */
    public void makeResultData(MeetingVO meetingVO) throws Exception {
        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

        MeetingVO _data = new MeetingVO();
        _data.setIdx_meeting(meetingVO.getIdx_meeting());
        List<AnalysisVO> allAnalysisList = analysisMapper.getUserAnalysisData(_data);

        List<AnalysisVO> _Time = new ArrayList<AnalysisVO>();
        List<MeetingVO> _mlists = fileServiceMapper.getMeetMovieFile(meetingVO);
        if(_mlists!=null){
            for(MeetingVO _mlist : _mlists){
                Date sdate = ff.parse(_mlist.getRecord_dt());
                Integer _sDt = (int) (sdate.getTime()/1000);
                Integer _eDt = _sDt + _mlist.getDuration();
                AnalysisVO _rs0 = new AnalysisVO();
                _rs0.setTimefirst(_sDt);
                _rs0.setTimeend(_eDt);
                _rs0.setDuration(_mlist.getDuration());
                _Time.add(_rs0);
            }
        }

        // 감정분석 전체 데이터 값만 뽑기
        List<PersonalLevelVO> allLevelVOList = new ArrayList<>();
        
        // 참가자 리스트
        List<MeetingVO> _ulists = meetMapper.getMeetInvites(meetingVO);

        Integer _ri = 0;
        if(_ulists != null){
            Map<String, Object> _sptd = new HashMap<String, Object>();
            ArrayList<Object> _sptd0 = new ArrayList<Object>();
            for(AnalysisVO _Time0 : _Time){
                List<PersonalLevelVO> personalLevelVOList = new ArrayList<>();

                for(MeetingVO _ulist : _ulists){
                    Integer _dataChk = 0;
                    if(_ulist.getToken() != null){
                        _dataChk = 1;
                    }

                    // 개인 막대 그래프용 데이터 뽑기
                    PersonalLevelVO personalLevelVO = analysisService.getLevelData(allAnalysisList, _Time0, _ulist.getIdx_meeting_user_join(), _dataChk);
                    allLevelVOList.add(personalLevelVO);    // 분석요약용 : 모든 동영상
                    personalLevelVOList.add(personalLevelVO);

                    for(ConcentrationVO paramVo : personalLevelVO.getConcentrationList()){
                        ConcentrationVO param = new ConcentrationVO();
                        param.setGood(paramVo.getGood());
                        param.setBad(paramVo.getBad());
                        param.setLvl(paramVo.getLevel_num());
                        param.setIdx_meeting(meetingVO.getIdx_meeting());
                        param.setMovie_no((_ri+1));
                        param.setIdx_meeting_user_join(_ulist.getIdx_meeting_user_join());
                        _sptd0.add(param);
                    }
                }

                // 전체 그래프용 데이터 만들기 : 동영상 당
                GraphMidVO userRateMap2 = analysisService.getAllUserRate(personalLevelVOList);

                List<Integer> goodList2 = userRateMap2.getGoodList();
                List<Integer> badList2 = userRateMap2.getBadList();
                List<String> lvlList2 = userRateMap2.getLvlList();
                
                if(goodList2!=null && goodList2.size()>0){
                    Map<String, Object> _dlists1 = new HashMap<String, Object>();
                    ArrayList<Object> _dlists = new ArrayList<Object>();
                    for(int i = 0;i<goodList2.size();i++){
                        ConcentrationVO param = new ConcentrationVO();
                        param.setGood(Math.round(goodList2.get(i)));
                        param.setBad((Math.round(badList2.get(i))*-1));
                        param.setLvl(lvlList2.get(i));
                        param.setIdx_meeting(meetingVO.getIdx_meeting());
                        param.setMovie_no((_ri+1));
                        _dlists.add(param);
                    }
                    _dlists1.put("list",_dlists);
                    analysisMapper.saveTotalBarData(_dlists1);
                }
                _ri++;
            }
            _sptd.put("list",_sptd0);
            analysisMapper.savePersoanlBarData(_sptd);

            Map<String, Object> _sptd1 = new HashMap<String, Object>();
            ArrayList<Object> _sptd10 = new ArrayList<Object>();
            for(MeetingVO _ulist : _ulists){
                ConcentrationVO concentrationVO = analysisService.getPersonalRate(allLevelVOList, _ulist.getIdx_meeting_user_join());

                ConcentrationVO param = new ConcentrationVO();
                param.setGood(concentrationVO.getGood());
                param.setBad(concentrationVO.getBad());
                param.setIdx_meeting(meetingVO.getIdx_meeting());
                param.setIdx_meeting_user_join(_ulist.getIdx_meeting_user_join());
                _sptd10.add(param);
            }
            _sptd1.put("list",_sptd10);
            analysisMapper.savePersoanlTotalData(_sptd1);
        }

        // 전체 집중도율 - 반원 그래프 : 동영상 통합
        GraphMidVO userRateMap = analysisService.getAllUserRate(allLevelVOList);
        
        List<Integer> goodList = userRateMap.getGoodList();
        List<Integer> badList = userRateMap.getBadList();
        List<Integer> totcalCntList = userRateMap.getTotcalCntList();
        
        ConcentrationVO conVO = analysisService.getMeetingRate(goodList, badList, totcalCntList);
        ConcentrationVO param = new ConcentrationVO();
        param.setGood(conVO.getGood());
        param.setBad(conVO.getBad());
        param.setIdx_meeting(meetingVO.getIdx_meeting());
        analysisMapper.saveMeetResultData(param);
    }
}
