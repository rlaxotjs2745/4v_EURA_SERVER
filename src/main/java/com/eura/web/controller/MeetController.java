/*
 * 미팅 컨트롤러
 */
package com.eura.web.controller;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eura.web.model.FileServiceMapper;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.model.DTO.UserVO;
import com.eura.web.service.UserService;
import com.eura.web.util.CONSTANT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/meet")
public class MeetController {
    private final UserService userService;
    private final MeetMapper meetMapper;
    private final FileServiceMapper fileServiceMapper;

    @Value("${file.upload-dir}")
    private String filepath;

    /**
     * 미팅룸 생성
     * @param req
     * @param meetingVO
     * @return
     */
    @PostMapping("/create")
    public ResultVO create(MultipartHttpServletRequest req, MeetingVO meetingVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        try {
            Integer rs = 0;
            if(meetingVO != null){
                // 미팅룸 정보 생성
                rs = meetMapper.meet_create(meetingVO);
                if(rs == 1){
                    // 미팅룸 참여자 리스트 저장

                    // 미팅룸 첨부파일 저장
                    List<MultipartFile> fileList = req.getFiles("file");
                    if(req.getFiles("file").get(0).getSize() != 0){
                        fileList = req.getFiles("file");
                    }
                    if(fileList.size()>0){
                        long time = System.currentTimeMillis();
                        String path = "meetroom/" + String.valueOf(time) + "/";
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
                                paramVo.setFile_path(path);
                                paramVo.setFile_name(saveFileName);
                                fileServiceMapper.addMeetFile(paramVo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    resultVO.setResult_code(CONSTANT.success);
                    resultVO.setResult_str("Success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }
}
