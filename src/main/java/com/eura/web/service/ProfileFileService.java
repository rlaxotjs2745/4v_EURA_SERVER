package com.eura.web.service;

import com.eura.web.model.DTO.ProfileInfoVO;

public interface ProfileFileService {


    public void uploadProfileFile(ProfileInfoVO profileInfo);

    public ProfileInfoVO getUserProfileFile(int idx_user);

    public void updateProfileFile(ProfileInfoVO profileInfo);
}
