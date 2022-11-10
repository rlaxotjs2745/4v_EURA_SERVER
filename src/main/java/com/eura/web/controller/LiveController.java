package com.eura.web.controller;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.base.BaseController;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.LiveEmotionVO;
import com.eura.web.model.DTO.MeetEndVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.service.AnalysisService;
import com.eura.web.util.CONSTANT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/live")
public class LiveController extends BaseController {
    private final AnalysisService analysisService;
    private final FileServiceMapper fileServiceMapper;
    private final MeetMapper meetMapper;

    @Value("${file.upload-dir}")
    public String filepath;

    /**
     * 참석자 감정 분석 자료
     * @param req
     * @param liveEmotionVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/analyse")
    public ResultVO analyseFace(MultipartHttpServletRequest req,
                                @RequestPart("mcid") String mcid,
                                @RequestPart("zuid") String zuid,
                                @RequestPart("token") String token,
                                @RequestPart("emotion") String emotion) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try{
            AnalysisVO analysisVO = new Gson().fromJson(emotion, AnalysisVO.class);

            MeetingVO meetingVO = new MeetingVO();
            meetingVO.setSessionid(mcid);
            meetingVO.setToken(token);
            MeetingVO _urs = meetMapper.getMeetInvite(meetingVO);
            if(_urs==null){
                resultVO.setResult_str("이용 가능한 회원이 아닙니다.");
            }else{
                LiveEmotionVO liveEmotionVO = new LiveEmotionVO();
                liveEmotionVO.setToken(token);
                liveEmotionVO.setMcid(mcid);
                liveEmotionVO.setEmotion(analysisVO);
                analysisService.insertAnalysisData(liveEmotionVO);
                
                List<MultipartFile> fileList = req.getFiles("file");
                if(req.getFiles("file").get(0).getSize() != 0){
                    fileList = req.getFiles("file");
                }

                if(fileList.size()>0){
                    String path = "/emotiondata/" + mcid + "/" + _urs.getIdx_user() + "/";
                    String fullpath = this.filepath + path;
                    File fileDir = new File(fullpath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
        
                    for(MultipartFile mf : fileList) {
                        if(mf.getOriginalFilename()!=null){
                            String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                            try { // 파일생성
                                mf.transferTo(new File(fullpath, originFileName));
                                MeetingVO paramVo = new MeetingVO();
                                paramVo.setIdx_analysis(liveEmotionVO.getIdx_analysis());
                                paramVo.setFile_path(path);
                                paramVo.setFile_name(originFileName);
                                paramVo.setFile_size(mf.getSize());
                                fileServiceMapper.addEmotionFile(paramVo);        // 미팅 감정 파일 저장
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("Insert Complete");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultVO;
    }

    /**
     * APP 미팅룸 종료 시 영상 파일 받기 - 강의 종료 데이터 수신
     * @param req
     * @param meetingVO
     * @return
     * @throws Exception
     */
    @PostMapping("/meetend")
    public ResultVO postMeetEnd(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try{
            Integer rs = meetMapper.endMeetLive(meetingVO);
            if(rs == 1){
                List<MultipartFile> fileList = req.getFiles("file");
                if(fileList.size()>0){
                    if(req.getFiles("file").get(0).getSize() != 0){
                        fileList = req.getFiles("file");
                    }
                    String path = "/meetmovie/" + meetingVO.getMcid() + "/";
                    String fullpath = this.filepath + path;
                    File fileDir = new File(fullpath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    Type ulist = new TypeToken<ArrayList<MeetEndVO>>(){}.getType();
                    List<MeetEndVO> _fs0 = new Gson().fromJson(meetingVO.getFilelist(), ulist); 
                    for(MultipartFile mf : fileList) {
                        String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                        try { // 파일생성
                            Integer _fileNo = 0;
                            Integer _duration = 0;
                            String _recordDt = "";
                            
                            for(MeetEndVO _fs1 : _fs0){
                                if(_fs1.getFile_name().equals(originFileName)){
                                    _fileNo = _fs1.getFile_no();
                                    _duration = _fs1.getDuration();
                                    _recordDt = _fs1.getRecord_dt();
                                }
                            }
                            mf.transferTo(new File(fullpath, originFileName));
                            MeetingVO paramVo = new MeetingVO();
                            paramVo.setToken(meetingVO.getToken());
                            paramVo.setMcid(meetingVO.getMcid());
                            paramVo.setFile_path(path);
                            paramVo.setFile_name(originFileName);
                            paramVo.setFile_size(mf.getSize());
                            paramVo.setFile_no(_fileNo);
                            paramVo.setDuration(_duration);
                            paramVo.setRecord_dt(_recordDt);
                            fileServiceMapper.addMeetMovieFile(paramVo);        // 미팅 동영상 파일 저장
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("미팅이 종료되었습니다.");
            }else{
                resultVO.setResult_str("미팅이 종료할 수 없습니다.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }
}
