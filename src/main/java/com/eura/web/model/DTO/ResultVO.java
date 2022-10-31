package com.eura.web.model.DTO;

import java.util.Map;

import lombok.*;

@Data
public class ResultVO{
    private String result_code;
    private String result_str;
    private Map<String, Object> data;
}
