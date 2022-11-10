package com.eura.web.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String storeFile(MultipartFile file);

    public Resource loadFileAsResource(String fileName);

}
