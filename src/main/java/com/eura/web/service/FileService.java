package com.eura.web.service;

import com.eura.web.model.DTO.FileUploadResponseVO;
import com.eura.web.model.DTO.MeetingVO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);

}
