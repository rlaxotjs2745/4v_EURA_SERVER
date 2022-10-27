package com.eura.web.service.impl;

import com.eura.web.model.WebFrontMapper;
import com.eura.web.service.WebFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("webFrontService")
public class WebFrontServiceImpl implements WebFrontService {
    private final WebFrontMapper webFrontMapper;

    @Autowired
    public WebFrontServiceImpl(WebFrontMapper webFrontMapper){ this.webFrontMapper = webFrontMapper;}
}
