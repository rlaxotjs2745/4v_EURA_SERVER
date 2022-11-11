package com.eura.web.service.impl;

import com.eura.web.model.DTO.ProfileInfoVO;
import com.eura.web.model.FileServiceMapper;
import com.eura.web.service.ProfileFileService;
import org.springframework.stereotype.Service;

@Service("profileFileService")
public class ProfileFileServiceImpl implements ProfileFileService {

    private final FileServiceMapper fileServiceMapper;

    public ProfileFileServiceImpl(FileServiceMapper fileServiceMapper) {
        this.fileServiceMapper = fileServiceMapper;
    }

    @Override
    public void uploadProfileFile(ProfileInfoVO profileInfo) {
        fileServiceMapper.insertProfileInfo(profileInfo);
    }

    @Override
    public void updateProfileFile(ProfileInfoVO profileInfo) {
        fileServiceMapper.updateProfileInfo(profileInfo);
    }
}
