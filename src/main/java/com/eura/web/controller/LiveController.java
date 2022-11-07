package com.eura.web.controller;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.LiveEmotionVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.service.AnalysisService;
import com.eura.web.service.MeetingService;
import com.eura.web.util.CONSTANT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/live")
public class LiveController {
    private final MeetingService meetingService;
    private final AnalysisService analysisService;


    /**
     * 강의실 종료
     * @param req
     * @param liveEmotionVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/analyse")
    public ResultVO analyseFace(HttpServletRequest req,
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

        try{
            analysisService.insertAnalysisData(liveEmotionVO);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("Insert Complete");
        } catch (Exception e){
            e.printStackTrace();
        }

        return resultVO;
    }


}
