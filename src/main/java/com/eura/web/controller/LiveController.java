package com.eura.web.controller;

import com.eura.web.model.DTO.AnalysisVO;
import com.eura.web.model.DTO.MeetingVO;
import com.eura.web.model.DTO.ResultVO;
import com.eura.web.service.AnalysisService;
import com.eura.web.service.MeetingService;
import com.eura.web.util.CONSTANT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/live")
public class LiveController {
    private final MeetingService meetingService;
    private final AnalysisService analysisService;







}
