package com.eura.web.service.impl;

import com.eura.web.model.SchedulerMapper;
import com.eura.web.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerMapper schedulerMapper;

    @Autowired
    public SchedulerServiceImpl(SchedulerMapper schedulerMapper){this.schedulerMapper = schedulerMapper;}

}
