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
            resultVO.setData(null);
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
            MeetingVO rs = meetMapper.getRoomInfo(meetingVO);
            if(rs!=null){
                List<MeetingVO> frs = meetMapper.getMeetFiles(meetingVO);
                List<MeetingVO> irs = meetMapper.getMeetInvites(meetingVO);
                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("미팅룸 정보를 불러왔습니다.");
                Map<String, Object> _rs = new HashMap<String, Object>();
                _rs.put("mt_name", rs.getMt_name());
                _rs.put("mt_start_dt", rs.getMt_info());
                _rs.put("mt_end_dt", rs.getMt_info());
                _rs.put("mt_remind_type", rs.getMt_remind_type());
                _rs.put("mt_remind_count", rs.getMt_remind_count());
                _rs.put("mt_remind_week", rs.getMt_remind_week());
                _rs.put("mt_remind_end", rs.getMt_remind_end());

                Map<String, Object> _frs = new HashMap<String, Object>();
                ArrayList<Object> _frss = new ArrayList<Object>();
                for(MeetingVO frs0 : frs){
                    _frs.put("idx", frs0.getIdx_attachment_file_info_join());
                    _frs.put("files", frs0.getFile_path() + frs0.getFile_name());
                    _frss.add(_frs);
                }
                _rs.put("mt_files", _frss);

                Map<String, Object> _irs = new HashMap<String, Object>();
                ArrayList<Object> _irss = new ArrayList<Object>();
                for(MeetingVO irs0 : irs){
                    _irs.put("idx", irs0.getIdx_user());
                    _irs.put("uname", irs0.getUser_name());
                    _irs.put("email", irs0.getUser_email());
                    _irss.add(_irs);
                }
                _rs.put("mt_invites", _irss);

                _rs.put("mt_info", rs.getMt_info());
                resultVO.setData(_rs);
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
            Integer rs = 0;
            if(meetingVO != null){
                // 미팅룸 정보 생성
                rs = meetMapper.meet_create(meetingVO);
                if(rs > 0){
                    // 미팅룸 참여자 리스트 저장
                    if(meetingVO.getMt_invite_email() != null && meetingVO.getMt_invite_email() != ""){
                        String[] inEmail = meetingVO.getMt_invite_email().split(",");
                        for(String uemail : inEmail){
                            MeetingVO ee = new MeetingVO();
                            ee.setIdx_meeting(meetingVO.getIdx_meeting());
                            ee.setUser_email(uemail);
                            meetMapper.meet_invite(ee);
                        }
                    }

                    // 미팅룸 첨부파일 저장
                    List<MultipartFile> fileList = req.getFiles("file");
                    if(req.getFiles("file").get(0).getSize() != 0){
                        fileList = req.getFiles("file");
                    }
                    if(fileList.size()>0){
                        long time = System.currentTimeMillis();
                        String path = "/meetroom/" + String.valueOf(time) + "/";
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
                        String path = "/meetroom/" + String.valueOf(time) + "/";
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }
}
