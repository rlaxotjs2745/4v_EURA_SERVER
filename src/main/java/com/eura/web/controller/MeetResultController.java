/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.AnalysisMapper;
import com.eura.web.model.DTO.*;
import com.eura.web.service.MeetResultService;

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
    private final MeetResultService meetResultService;

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

            if(meetingVO.getFileno()==null){
                meetingVO.setFileno(1);
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

                    AnalysisVO _rs0 = new AnalysisVO();
                    if(_mlist.getRecord_dt()!=null){
                        Date sdate = ff.parse(_mlist.getRecord_dt());
                        Integer _sDt = (int) (sdate.getTime()/1000);
                        Integer _eDt = _sDt + _mlist.getDuration();
                        _rs0.setTimefirst(_sDt);
                        _rs0.setTimeend(_eDt);
                        _rs0.setDuration(_mlist.getDuration());
                        _Time.add(_rs0);
                    }else{
                        _rs0.setTimefirst(0);
                        _rs0.setTimeend(0);
                        _rs0.setDuration(_mlist.getDuration());
                        _Time.add(_rs0);
                    }
                }
                _rs.put("mtMovieFiles", _mls);
            }else{
                _rs.put("mtMovieFiles", null);
            }

            MeetingVO _data = new MeetingVO();
            _data.setIdx_meeting(meetingVO.getIdx_meeting());
            MeetResultVO _rss = analysisMapper.getMeetResultData(_data);
            if(_rss == null){
                resultVO = meetResultService.getResultData(resultVO, meetingVO, _Time, _auth, _uin, _rs);
            }else{
                resultVO = meetResultService.getResultDataDB(resultVO, meetingVO, _Time, _auth, _uin, _rs);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultVO;
    }
}
