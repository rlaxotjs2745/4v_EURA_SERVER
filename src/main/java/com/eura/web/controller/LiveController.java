package com.eura.web.controller;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.DTO.LiveEmotionVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.service.AnalysisService;
import com.eura.web.util.CONSTANT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.*;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/live")
public class LiveController {
    private final AnalysisService analysisService;
    private final FileServiceMapper fileServiceMapper;
    private final MeetMapper meetMapper;

    @Value("${file.upload-dir}")
    public String filepath;

    /**
     * 강의실 종료
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

        Gson gson = new Gson();
        AnalysisVO analysisVO = gson.fromJson(emotion, AnalysisVO.class);

        LiveEmotionVO liveEmotionVO = new LiveEmotionVO();

        liveEmotionVO.setZuid(zuid);
        liveEmotionVO.setMcid(mcid);
        liveEmotionVO.setToken(token);
        liveEmotionVO.setEmotion(analysisVO);

        List<MultipartFile> fileList = req.getFiles("file");

        if(req.getFiles("file").get(0).getSize() != 0){
            fileList = req.getFiles("file");
        }
        if(fileList.size()>0){
            long time = System.currentTimeMillis();
            String path = "/emotiondata/" + mcid + "/" + zuid + "/";
            String fullpath = this.filepath + path;
            File fileDir = new File(fullpath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            for(MultipartFile mf : fileList) {
                String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                String saveFileName = String.format("%d_%s", time, originFileName);
                try { // 파일생성
                    mf.transferTo(new File(fullpath, saveFileName));
                    MeetingVO paramVo = new MeetingVO();
                    paramVo.setMcid(mcid);
                    paramVo.setZuid(zuid);
                    paramVo.setToken(token);
                    paramVo.setFile_path(path);
                    paramVo.setFile_name(saveFileName);
                    paramVo.setFile_size(mf.getSize());
                    fileServiceMapper.addEmotionFile(paramVo);        // 미팅 동영상 파일 저장
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try{
            analysisService.insertAnalysisData(liveEmotionVO);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("Insert Complete");
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
                if(req.getFiles("file").get(0).getSize() != 0){
                    fileList = req.getFiles("file");
                }
                if(fileList.size()>0){
                    long time = System.currentTimeMillis();
                    String path = "/meetmovie/" + meetingVO.getMcid() + "/";
                    String fullpath = this.filepath + path;
                    File fileDir = new File(fullpath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }

                    for(MultipartFile mf : fileList) {
                        String originFileName = mf.getOriginalFilename();   // 원본 파일 명
                        String saveFileName = String.format("%d_%s", time, originFileName);
                        try { // 파일생성
                            mf.transferTo(new File(fullpath, saveFileName));
                            MeetingVO paramVo = new MeetingVO();
                            paramVo.setMcid(meetingVO.getMcid());
                            paramVo.setFile_path(path);
                            paramVo.setFile_name(saveFileName);
                            paramVo.setFile_size(mf.getSize());
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
