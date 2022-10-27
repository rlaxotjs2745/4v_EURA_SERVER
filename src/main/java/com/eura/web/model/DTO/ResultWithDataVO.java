package com.eura.web.model.DTO;

import lombok.Data;

@Data
public class ResultWithDataVO<E> {
    String result_code;
    String result_str;
    E element;
}

