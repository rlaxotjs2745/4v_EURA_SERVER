package com.eura.web.model.DTO;

import lombok.Data;

@Data
public class FileUploadResponseVO {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    public FileUploadResponseVO(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

}
