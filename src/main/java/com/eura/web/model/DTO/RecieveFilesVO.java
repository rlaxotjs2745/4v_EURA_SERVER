package com.eura.web.model.DTO;

import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecieveFilesVO {
    long sender;
    String sender_name;//	전송자
    int type;// 전송 타입
    @NonNull
    MultipartFile file1;
    @NonNull
    MultipartFile file2;
    @NonNull
    MultipartFile file3;
    @Nullable
    MultipartFile file4;
    @Nullable
    MultipartFile file5;
    @Nullable
    MultipartFile file6;
    @Nullable
    MultipartFile file7;
    @Nullable
    MultipartFile file8;
    @Nullable
    MultipartFile file9;
}
